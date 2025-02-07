package com.linbit.locks;

import com.linbit.ImplementationError;
import com.linbit.linstor.core.CoreModule;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.inject.Singleton;

@Singleton
public class LockGuardFactory
{
    public interface LockGuardBuilder
    {
        LockGuardBuilder setDefer(boolean defer);

        LockGuardBuilder write(LockObj... lockIdList);

        LockGuardBuilder read(LockObj... lockIdList);

        LockGuardBuilder lock(LockObj lockId, LockType lockType);

        LockGuard build();

        default LockGuard buildDeferred()
        {
            setDefer(true);
            return build();
        }
    }

    public enum LockObj
    {
        RECONFIGURATION(0),
        CTRL_CONFIG(1),
        NODES_MAP(2),
        RSC_DFN_MAP(3),
        STOR_POOL_DFN_MAP(4),
        KVS_MAP(5),
        RSC_GRP_MAP(6),
        EXT_FILE_MAP(7),
        REMOTE_MAP(8);

        public final int lockIdx;

        LockObj(final int idx)
        {
            lockIdx = idx;
        }
    }

    public enum LockType
    {
        READ,
        WRITE
    }

    private final ReadWriteLock nodesMapLock;
    private final ReadWriteLock rscDfnMapLock;
    private final ReadWriteLock storPoolDfnMapLock;
    private final ReadWriteLock ctrlConfigLock;
    private final ReadWriteLock reconfigurationLock;
    private final ReadWriteLock kvsMapLock;
    private final ReadWriteLock rscGrpMapLock;
    private final ReadWriteLock extFileMapLock;
    private final ReadWriteLock remoteMapLock;

    @Inject
    public LockGuardFactory(
        @Named(CoreModule.RECONFIGURATION_LOCK) ReadWriteLock reconfigurationLockRef,
        @Named(CoreModule.NODES_MAP_LOCK) ReadWriteLock nodesMapLockRef,
        @Named(CoreModule.RSC_DFN_MAP_LOCK) ReadWriteLock rscDfnMapLockRef,
        @Named(CoreModule.STOR_POOL_DFN_MAP_LOCK) ReadWriteLock storPoolDfnMapLockRef,
        @Named(CoreModule.CTRL_CONF_LOCK) ReadWriteLock ctrlConfigLockRef,
        @Named(CoreModule.KVS_MAP_LOCK) ReadWriteLock kvsMapLockRef,
        @Named(CoreModule.RSC_GROUP_MAP_LOCK) ReadWriteLock rscGrpMapLockRef,
        @Named(CoreModule.EXT_FILE_MAP_LOCK) ReadWriteLock extFileMapLockRef,
        @Named(CoreModule.REMOTE_MAP_LOCK) ReadWriteLock remoteMapLockRef
    )
    {
        reconfigurationLock = reconfigurationLockRef;
        nodesMapLock = nodesMapLockRef;
        rscDfnMapLock = rscDfnMapLockRef;
        storPoolDfnMapLock = storPoolDfnMapLockRef;
        ctrlConfigLock = ctrlConfigLockRef;
        kvsMapLock = kvsMapLockRef;
        rscGrpMapLock = rscGrpMapLockRef;
        extFileMapLock = extFileMapLockRef;
        remoteMapLock = remoteMapLockRef;
    }

    public LockGuardBuilder create()
    {
        return new LockGuardBuilderImpl();
    }

    public LockGuardBuilder createDeferred()
    {
        return new LockGuardBuilderImpl(true);
    }

    public LockGuard build(LockType type, LockObj... lockIdList)
    {
        return build(new LockGuardBuilderImpl(), type, lockIdList);
    }

    public LockGuard buildDeferred(LockType type, LockObj... lockIdList)
    {
        return build(new LockGuardBuilderImpl(true), type, lockIdList);
    }

    private LockGuard build(
        LockGuardBuilder lockGuardBuilder,
        LockType type,
        LockObj[] lockIdList
    )
    {
        LockGuardBuilder tmpLGB;
        if (type == LockType.READ)
        {
            tmpLGB = lockGuardBuilder.read(lockIdList);
        }
        else
        {
            tmpLGB = lockGuardBuilder.write(lockIdList);
        }
        return tmpLGB.build();
    }


    private ReadWriteLock lockObjToLock(LockObj lockId)
    {
        ReadWriteLock lock;
        switch (lockId)
        {
            case RECONFIGURATION:
                lock = reconfigurationLock;
                break;
            case NODES_MAP:
                lock = nodesMapLock;
                break;
            case RSC_DFN_MAP:
                lock = rscDfnMapLock;
                break;
            case STOR_POOL_DFN_MAP:
                lock = storPoolDfnMapLock;
                break;
            case CTRL_CONFIG:
                lock = ctrlConfigLock;
                break;
            case KVS_MAP:
                lock = kvsMapLock;
                break;
            case RSC_GRP_MAP:
                lock = rscGrpMapLock;
                break;
            case EXT_FILE_MAP:
                lock = extFileMapLock;
                break;
            case REMOTE_MAP:
                lock = remoteMapLock;
                break;
            default:
                throw new ImplementationError("Unknown lock identifier " + lockId.name());
        }
        return lock;
    }

    private class LockGuardBuilderImpl implements LockGuardBuilder
    {
        private final TreeMap<LockObj, LockType> locks;

        private boolean defer = false;

        private LockGuardBuilderImpl()
        {
            locks = new TreeMap<>((lock1st, lock2nd) -> Integer.compare(lock1st.lockIdx, lock2nd.lockIdx));
        }

        private LockGuardBuilderImpl(boolean deferRef)
        {
            this();
            setDefer(deferRef);
        }

        @Override
        public LockGuardBuilder setDefer(boolean deferRef)
        {
            defer = deferRef;
            return this;
        }

        @Override
        public LockGuardBuilder read(LockObj... lockObjs)
        {
            for (LockObj lockObj : lockObjs)
            {
                lock(lockObj, LockType.READ);
            }
            return this;
        }

        @Override
        public LockGuardBuilder write(LockObj... lockObjs)
        {
            for (LockObj lockObj : lockObjs)
            {
                lock(lockObj, LockType.WRITE);
            }
            return this;
        }

        @Override
        public LockGuardBuilder lock(LockObj lockId, LockType type)
        {
            locks.put(lockId, type);
            return this;
        }

        @Override
        public LockGuard buildDeferred()
        {
            setDefer(true);
            return build();
        }

        @Override
        public LockGuard build()
        {
            if (!locks.isEmpty() && !locks.containsKey(LockObj.RECONFIGURATION))
            {
                locks.put(LockObj.RECONFIGURATION, LockType.READ);
            }

            Lock[] lockArr = new Lock[locks.size()];
            int lockIdx = 0;
            for (Entry<LockObj, LockType> entry : locks.entrySet())
            {
                Lock lock;
                if (entry.getValue() == LockType.READ)
                {
                    lock = lockObjToLock(entry.getKey()).readLock();
                }
                else
                {
                    lock = lockObjToLock(entry.getKey()).writeLock();
                }
                lockArr[lockIdx] = lock;
                ++lockIdx;
            }
            return new LockGuard(defer, lockArr);
        }
    }
}
