package com.linbit.drbdmanage;

import com.linbit.ImplementationError;
import com.linbit.WorkerPool;
import com.linbit.drbdmanage.debug.DebugErrorReporter;
import com.linbit.drbdmanage.security.AccessContext;
import com.linbit.drbdmanage.security.AccessDeniedException;
import com.linbit.drbdmanage.security.AccessType;
import com.linbit.drbdmanage.security.Identity;
import com.linbit.drbdmanage.security.IdentityName;
import com.linbit.drbdmanage.security.Initializer;
import com.linbit.drbdmanage.security.ObjectProtection;
import com.linbit.drbdmanage.security.Privilege;
import com.linbit.drbdmanage.security.PrivilegeSet;
import com.linbit.drbdmanage.security.SecurityType;
import com.linbit.fsevent.FileSystemWatch;
import com.linbit.timer.Action;
import com.linbit.timer.GenericTimer;
import com.linbit.timer.Timer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * drbdmanageNG controller prototype
 *
 * @author Robert Altnoeder &lt;robert.altnoeder@linbit.com&gt;
 */
public class Controller implements Runnable, CoreServices
{
    public static final String PROGRAM = "drbdmanageNG";
    public static final String MODULE = "Controller";
    public static final String VERSION = "experimental 2017-03-02_001";

    public static final int MIN_WORKER_QUEUE_SIZE = 32;
    public static final int MAX_CPU_COUNT = 1024;

    // Defaults
    private int cpuCount = 8;
    private int workerThreadCount = 8;
    // Queue slots per worker thread
    private int workerQueueFactor = 4;
    private int workerQueueSize = MIN_WORKER_QUEUE_SIZE;

    public static final String SCREEN_DIV =
        "------------------------------------------------------------------------------";

    private final AccessContext sysCtx;
    private String[] args;

    private final GenericTimer<String, Action<String>> timerEventSvc;
    private final FileSystemWatch fsEventSvc;

    private WorkerPool workers = null;
    private ErrorReporter errorLog = null;

    private ObjectProtection shutdownProt;

    public Controller(AccessContext sysCtxRef, String[] argsRef)
        throws IOException
    {
        sysCtx = sysCtxRef;
        args = argsRef;

        // Create the timer event service
        timerEventSvc = new GenericTimer<>();

        // Create the filesystem event service
        try
        {
            fsEventSvc = new FileSystemWatch();
        }
        catch (IOException ioExc)
        {
            logFailure("Initialization of the FileSystemWatch service failed");
            // FIXME: Generate a startup exception
            throw ioExc;
        }

        cpuCount = Runtime.getRuntime().availableProcessors();

        shutdownProt = new ObjectProtection(sysCtx);
    }

    @Override
    public void run()
    {
        try
        {
            logInfo("Entering debug console");
            AccessContext debugCtx;
            {
                AccessContext impCtx = sysCtx.clone();
                impCtx.getEffectivePrivs().enablePrivileges(Privilege.PRIV_SYS_ALL);
                debugCtx = impCtx.impersonate(
                    new Identity(impCtx, new IdentityName("LocalDebugConsole")),
                    sysCtx.subjectRole,
                    sysCtx.subjectDomain,
                    sysCtx.getLimitPrivs().toArray()
                );
            }
            debugCtx.getEffectivePrivs().enablePrivileges(Privilege.PRIV_OBJ_VIEW);
            DebugConsole dbgConsole = new DebugConsole(this, debugCtx);
            dbgConsole.stdStreamsConsole();
        }
        catch (Throwable error)
        {
            errorLog.reportError(error);
        }

        try
        {
            AccessContext shutdownCtx = sysCtx.clone();
            // Just in case that someone removed the access control list entry
            // for the system's role or changed the security type for shutdown,
            // override access controls with the system context's privileges
            shutdownCtx.getEffectivePrivs().enablePrivileges(Privilege.PRIV_OBJ_USE, Privilege.PRIV_MAC_OVRD);
            shutdown(shutdownCtx);
        }
        catch (AccessDeniedException accExc)
        {
            throw new ImplementationError(
                "Cannot shutdown() using the system's security context. " +
                "Suspected removal of privileges from the system context.",
                null
            );
        }
    }

    public void cmdThreads()
    {
        int thrCount = Thread.activeCount();
        Thread[] activeThreads = new Thread[thrCount + 20];
        thrCount = Thread.enumerate(activeThreads);

        System.out.printf(
            "%-32s %18s %4s %-6s %-6s %-6s\n",
            "Thread name", "Id", "Prio", "Alive", "Daemon", "Intr"
        );
        System.out.printf("%s\n", SCREEN_DIV);

        int slot = 0;
        for (Thread thr : activeThreads)
        {
            System.out.printf(
                "%-32s %18d %4d %-6s %-6s %-6s\n",
                thr.getName(),
                thr.getId(),
                thr.getPriority(),
                thr.isAlive() ? "Y" : "N",
                thr.isDaemon() ? "Y" : "N",
                thr.isInterrupted() ? "Y" : "N"
            );

            ++slot;
            if (slot >= thrCount)
            {
                break;
            }
        }
        System.out.printf("%s\n", SCREEN_DIV);
    }

    public void initialize(ErrorReporter errorLogRef)
    {
        errorLog = errorLogRef;

        System.out.printf("\n%s\n\n", SCREEN_DIV);
        programInfo();
        System.out.printf("\n%s\n\n", SCREEN_DIV);

        logInit("Applying base security policy to system objects");
        applyBaseSecurityPolicy();

        logInit("Starting timer event service");
        // Start the timer event service
        timerEventSvc.setTimerName("TimerEventService");
        timerEventSvc.start();

        logInit("Starting filesystem event service");
        // Start the filesystem event service
        fsEventSvc.start();

        logInit("Starting worker thread pool");
        workerThreadCount = cpuCount <= MAX_CPU_COUNT ? cpuCount : MAX_CPU_COUNT;
        {
            int qSize = workerThreadCount * workerQueueFactor;
            workerQueueSize = qSize > MIN_WORKER_QUEUE_SIZE ? qSize : MIN_WORKER_QUEUE_SIZE;
        }
        workers = WorkerPool.initialize(workerThreadCount, workerQueueSize, true, "MainWorkerPool");

        System.out.printf("\n%s\n\n", SCREEN_DIV);
        runTimeInfo();
        System.out.printf("\n%s\n\n", SCREEN_DIV);
    }

    public void shutdown(AccessContext accCtx) throws AccessDeniedException
    {
        shutdownProt.requireAccess(accCtx, AccessType.USE);

        logInfo("Shutdown in progress");
        logInfo("Shutting down filesystem event service");
        // Stop the filesystem event service
        fsEventSvc.shutdown();

        logInfo("Shutting down timer event service");
        timerEventSvc.shutdown();

        logInfo("Shutting down worker thread pool");
        workers.shutdown();

        logInfo("Shutdown complete");
    }

    public void displayContextInfo(AccessContext accCtx)
    {
        System.out.printf("%-24s: %s\n", "Identity", accCtx.getIdentity().name.displayValue);
        System.out.printf("%-24s: %s\n", "Role", accCtx.getRole().name.displayValue);
        System.out.printf("%-24s: %s\n", "Domain", accCtx.getDomain().name.displayValue);

        String privSeparator = String.format("\n%-24s  ", "");
        String limitPrivs;
        {
            StringBuilder limitPrivsList = new StringBuilder();
            for (Privilege priv : accCtx.getLimitPrivs().getEnabledPrivileges())
            {
                if (limitPrivsList.length() > 0)
                {
                    limitPrivsList.append(privSeparator);
                }
                limitPrivsList.append(priv.name);
            }
            if (limitPrivsList.length() <= 0)
            {
                limitPrivsList.append("None");
            }
            limitPrivs = limitPrivsList.toString();
        }
        System.out.printf("%-24s: %s\n", "Limit privileges", limitPrivs);

        String effPrivs;
        {
            StringBuilder effPrivsList = new StringBuilder();
            for (Privilege priv : accCtx.getEffectivePrivs().getEnabledPrivileges())
            {
                if (effPrivsList.length() > 0)
                {
                    effPrivsList.append(privSeparator);
                }
                effPrivsList.append(priv.name);
            }
            if (effPrivsList.length() <= 0)
            {
                effPrivsList.append("None");
            }
            effPrivs = effPrivsList.toString();
        }
        System.out.printf("%-24s: %s\n", "Effective privileges", effPrivs);
    }

    public ErrorReporter getErrorReporter()
    {
        return errorLog;
    }

    public Timer<String, Action<String>> getTimer()
    {
        return (Timer) timerEventSvc;
    }

    public FileSystemWatch getFsWatch()
    {
        return fsEventSvc;
    }

    public static final void logInit(String what)
    {
        System.out.println("INIT      " + what);
    }

    public static final void logInfo(String what)
    {
        System.out.println("INFO      " + what);
    }

    public static final void logBegin(String what)
    {
        System.out.println("BEGIN     " + what);
    }

    public static final void logEnd(String what)
    {
        System.out.println("END       " + what);
    }

    public static final void logFailure(String what)
    {
        System.err.println("FAILED    " + what);
    }

    public static final void printField(String fieldName, String fieldContent)
    {
        System.out.printf("  %-32s: %s\n", fieldName, fieldContent);
    }


    public final void programInfo()
    {
        System.out.println(
            "Software information\n" +
            "--------------------\n"
        );

        printField("PROGRAM", PROGRAM);
        printField("MODULE", MODULE);
        printField("VERSION", VERSION);
    }

    public final void runTimeInfo()
    {
        Properties sysProps = System.getProperties();
        String jvmSpecVersion = sysProps.getProperty("java.vm.specification.version");
        String jvmVendor = sysProps.getProperty("java.vm.vendor");
        String jvmVersion = sysProps.getProperty("java.vm.version");
        String osName = sysProps.getProperty("os.name");
        String osVersion = sysProps.getProperty("os.version");
        String sysArch = sysProps.getProperty("os.arch");

        System.out.println(
            "Execution environment information\n" +
            "--------------------------------\n"
        );

        Runtime rt = Runtime.getRuntime();
        long freeMem = rt.freeMemory() / 1048576;
        long availMem = rt.maxMemory() / 1048576;

        printField("JAVA PLATFORM", jvmSpecVersion);
        printField("RUNTIME IMPLEMENTATION", jvmVendor + ", Version " + jvmVersion);
        System.out.println();
        printField("SYSTEM ARCHITECTURE", sysArch);
        printField("OPERATING SYSTEM", osName + " " + osVersion);
        printField("AVAILABLE PROCESSORS", Integer.toString(cpuCount));
        if (availMem == Long.MAX_VALUE)
        {
            printField("AVAILABLE MEMORY", "OS ALLOCATION LIMIT");
        }
        else
        {
            printField("AVAILABLE MEMORY", String.format("%10d MiB", availMem));
        }
        printField("FREE MEMORY", String.format("%10d MiB", freeMem));
        System.out.println();
        printField("WORKER THREADS", Integer.toString(workers.getThreadCount()));
        printField("WORKER QUEUE SIZE", Integer.toString(workers.getQueueSize()));
        printField("WORKER SCHEDULING", workers.isFairQueue() ? "FIFO" : "Random");
    }

    private void applyBaseSecurityPolicy()
    {
        PrivilegeSet effPriv = sysCtx.getEffectivePrivs();
        try
        {
            // Enable all privileges
            effPriv.enablePrivileges(Privilege.PRIV_SYS_ALL);

            // Allow CONTROL access by domain SYSTEM to type SYSTEM
            SecurityType sysType = sysCtx.getDomain();
            sysType.addEntry(sysCtx, sysType, AccessType.CONTROL);

            // Allow USE access by role SYSTEM to shutdownProt
            shutdownProt.addAclEntry(sysCtx, sysCtx.getRole(), AccessType.USE);
        }
        catch (AccessDeniedException accExc)
        {
            logFailure("Applying the base security policy failed");
            errorLog.reportError(accExc);
        }
        finally
        {
            effPriv.disablePrivileges(Privilege.PRIV_SYS_ALL);
        }
    }

    public static void main(String[] args)
    {
        logInit("System components initialization in progress");

        logInit("Constructing error reporter instance");
        ErrorReporter errorLog = new DebugErrorReporter();

        try
        {
            logInit("Initializing system security context");
            Initializer sysInit = new Initializer();
            logInit("Constructing controller instance");
            Controller instance = sysInit.initController(args);

            logInit("Initializing controller services");
            instance.initialize(errorLog);

            logInit("Initialization complete");
            System.out.println();

            Thread.currentThread().setName("MainLoop");

            logInfo("Starting controller module");
            instance.run();
        }
        catch (ImplementationError implError)
        {
            errorLog.reportError(implError);
        }
        catch (IOException ioExc)
        {
            errorLog.reportError(ioExc);
        }
        catch (Throwable error)
        {
            errorLog.reportError(error);
        }

        System.out.println();
    }

    private static class DebugConsole
    {
        private enum ParamParserState
        {
            SKIP_COMMAND,
            SPACE,
            OPTIONAL_SPACE,
            READ_KEY,
            READ_VALUE,
            ESCAPE
        };

        private final Controller controller;
        private final AccessContext debugCtx;

        public static final String CONSOLE_PROMPT = "Command ==> ";

        private Map<String, String> parameters;

        private boolean exitConsole = false;

        DebugConsole(Controller controllerRef, AccessContext accCtx)
        {
            controller = controllerRef;
            debugCtx = accCtx;
            parameters = new TreeMap<>();
        }

        public void stdStreamsConsole()
        {
            streamsConsole(System.in, System.out, System.err, true);
        }

        public void streamsConsole(
            InputStream debugIn,
            PrintStream debugOut,
            PrintStream debugErr,
            boolean prompt
        )
        {
            try
            {
                BufferedReader cmdIn = new BufferedReader(
                    new InputStreamReader(debugIn)
                );

                String inputLine;
                commandLineLoop:
                while (!exitConsole)
                {
                    // Print the console prompt if required
                    if (prompt)
                    {
                        debugOut.print("\n");
                        debugOut.print(CONSOLE_PROMPT);
                        debugOut.flush();
                    }

                    inputLine = cmdIn.readLine();
                    if (inputLine != null)
                    {
                        processCommandLine(inputLine);
                    }
                    else
                    {
                        // End of command input stream, exit console
                        break;
                    }
                }
            }
            catch (IOException ioExc)
            {
                controller.errorLog.reportError(ioExc);
            }
            catch (Throwable error)
            {
                controller.errorLog.reportError(error);
            }
        }

        public void processCommandLine(String inputLine)
        {
            String commandLine = inputLine.trim();
            if (!inputLine.isEmpty())
            {
                // Parse the debug command line
                char[] commandChars = commandLine.toCharArray();
                String command = parseCommandName(commandChars);

                try
                {
                    parseCommandParameters(commandChars, parameters);
                    processCommand(command, parameters);
                }
                catch (ParseException parseExc)
                {
                    System.err.println(parseExc.getMessage());
                }
                finally
                {
                    parameters.clear();
                }
            }
        }

        public void processCommand(String command, Map<String, String> parameters)
        {
            // BEGIN DEBUG
            System.out.println(command);
            for (Map.Entry<String, String> paramEntry : parameters.entrySet())
            {
                System.out.printf(
                    "    %-20s: %s\n",
                    paramEntry.getKey(), paramEntry.getValue()
                );
            }
            // END DEBUG
        }

        private String parseCommandName(char[] commandChars)
        {
            int commandLength = commandChars.length;
            for (int idx = 0; idx < commandChars.length; ++idx)
            {
                if (commandChars[idx] == ' ' || commandChars[idx] == '\t')
                {
                    commandLength = idx;
                    break;
                }
            }
            String command = new String(commandChars, 0, commandLength).toUpperCase();
            return command;
        }

        private void parseCommandParameters(char[] commandChars, Map<String, String> parameters)
            throws ParseException
        {
            int keyOffset = 0;
            int valueOffset = 0;
            int length = 0;

            ParamParserState state = ParamParserState.SKIP_COMMAND;
            String key = null;
            String value = null;
            for (int idx = 0; idx < commandChars.length; ++idx)
            {
                char cc = commandChars[idx];
                switch (state)
                {
                    case SKIP_COMMAND:
                        if (cc == ' ' || cc == '\t')
                        {
                            state = ParamParserState.OPTIONAL_SPACE;
                        }
                        break;
                    case SPACE:
                        if (cc != ' ' && cc != '\t')
                        {
                            errorParser(idx);
                        }
                        state = ParamParserState.OPTIONAL_SPACE;
                        break;
                    case OPTIONAL_SPACE:
                        if (cc == ' ' || cc == '\t')
                        {
                            break;
                        }
                        keyOffset = idx;
                        state = ParamParserState.READ_KEY;
                        // fall-through
                    case READ_KEY:
                        if (cc == '(')
                        {
                            length = idx - keyOffset;
                            if (length < 1)
                            {
                                errorInvalidKey(keyOffset);
                            }
                            key = new String(commandChars, keyOffset, length);
                            valueOffset = idx + 1;
                            state = ParamParserState.READ_VALUE;
                        }
                        else
                        if (!((cc >= 'a' && cc <= 'z') || (cc >= 'A' && cc <= 'Z')))
                        {
                            if (!(cc >= '0' && cc <= '9' && idx > keyOffset))
                            {
                                errorInvalidKey(keyOffset);
                            }
                        }
                        break;
                    case READ_VALUE:
                        if (cc == ')')
                        {
                            length = idx - valueOffset;
                            value = new String(commandChars, valueOffset, length);
                            if (key != null)
                            {
                                parameters.put(key, value);
                            }
                            else
                            {
                                errorInvalidKey(keyOffset);
                            }
                            state = ParamParserState.OPTIONAL_SPACE;
                        }
                        else
                        if (cc == '\\')
                        {
                            // Ignore the next character's special meaning
                            state = ParamParserState.ESCAPE;
                        }
                        break;
                    case ESCAPE:
                        // Only values can be escaped, so the next state is always READ_VALUE
                        state = ParamParserState.READ_VALUE;
                        break;
                    default:
                        throw new ImplementationError(
                            String.format(
                                "Missing case label for enum member '%s'",
                                state.name()
                            ),
                            null
                        );
                }
            }
            if (state != ParamParserState.SKIP_COMMAND && state != ParamParserState.OPTIONAL_SPACE)
            {
                errorIncompleteLine(commandChars.length);
            }
        }

        private void errorInvalidKey(int pos) throws ParseException
        {
            throw new ParseException(
                String.format(
                    "The command line is not valid. " +
                    "An invalid parameter key was encountered at position %d.",
                    pos
                ),
                pos
            );
        }

        private void errorParser(int pos) throws ParseException
        {
            throw new ParseException(
                String.format(
                    "The command line is not valid. " +
                    "The parser encountered an error at position %d.",
                    pos
                ),
                pos
            );
        }

        private void errorIncompleteLine(int pos) throws ParseException
        {
            throw new ParseException(
                "The command line is not valid. The input line appears to have been truncated.",
                pos
            );
        }
    }

    static class TestException extends Exception
    {
        TestException()
        {
        }

        TestException(String message)
        {
            super(message);
        }

        TestException(Throwable cause)
        {
            super(cause);
        }

        TestException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }
}
