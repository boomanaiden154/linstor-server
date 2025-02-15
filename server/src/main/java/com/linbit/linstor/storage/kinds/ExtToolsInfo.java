package com.linbit.linstor.storage.kinds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExtToolsInfo
{
    private final ExtTools extTool;
    private boolean isSupported;

    private final Version version;

    private final List<String> notSupportedReasons;

    public ExtToolsInfo(
        ExtTools extToolRef,
        boolean isSupportedRef,
        Integer versionMajorRef,
        Integer versionMinorRef,
        /**
         * versionPath might be null even if the ExtTool is supported (versionMajor and versionMinor have to be not
         * null)
         */
        Integer versionPatchRef,
        List<String> notSupportedReasonsRef
    )
    {
        extTool = extToolRef;
        isSupported = isSupportedRef;
        version = new Version(versionMajorRef, versionMinorRef, versionPatchRef);
        notSupportedReasons = new ArrayList<>();
        if (notSupportedReasonsRef != null)
        {
            notSupportedReasons.addAll(notSupportedReasonsRef);
        }
    }

    public final ExtTools getTool()
    {
        return extTool;
    }

    public final boolean isSupported()
    {
        return isSupported;
    }

    public final Version getVersion()
    {
        return version;
    }

    public boolean isSupportedAndHasVersionOrHigher(Version versionRef)
    {
        return isSupported && versionRef.greaterOrEqual(versionRef);
    }

    public final Integer getVersionMajor()
    {
        return version.major;
    }

    public final Integer getVersionMinor()
    {
        return version.minor;
    }

    /**
     * versionPath might be null even if the ExtTool is supported (versionMajor and versionMinor have to be not null)
     */
    public final Integer getVersionPatch()
    {
        return version.patch;
    }

    public final List<String> getNotSupportedReasons()
    {
        return notSupportedReasons;
    }

    public void setSupported(boolean isSupportedRef)
    {
        isSupported = isSupportedRef;
    }

    public void addUnsupportedReason(String reason)
    {
        isSupported = false;
        notSupportedReasons.add(reason);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtToolsInfo [").append(extTool).append(" is supported: ").append(isSupported);
        if (isSupported)
        {
            sb.append(", version: ").append(version.toString());
        }
        else
        {
            sb.append(", reasons: ").append(notSupportedReasons);
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean hasVersionOrHigher(Version ver)
    {
        return version.greaterOrEqual(ver);
    }

    public static class Version implements Comparable<Version>
    {
        private final Integer major;
        private final Integer minor;
        private final Integer patch;
        private final String additionalInfo; // "rc1" or whatever

        public Version()
        {
            this(null, null, null, null);
        }

        public Version(int majRef)
        {
            this(majRef, null, null, null);
        }

        public Version(int majRef, int minRef)
        {
            this(majRef, minRef, null, null);
        }

        public Version(Integer majRef, Integer minRef, Integer patchRef)
        {
            this(majRef, minRef, patchRef, null);
        }

        public Version(Integer majRef, Integer minRef, Integer patchRef, String additionalInfoRef)
        {
            major = majRef;
            minor = minRef;
            patch = patchRef;
            additionalInfo = additionalInfoRef;
        }

        /**
         * Returns true if the version of "this" object is greater or equal to the parameter.
         * <p>
         * null-values on either side also fulfill "greater or equal"
         */
        public boolean greaterOrEqual(Version vsn)
        {
            // DO NOT rely on compareTo method, because of different handling of null values
            int cmp = (major == null || vsn.major == null) ? 1 : Integer.compare(major, vsn.major);
            if (cmp == 0)
            {
                cmp = minor == null || vsn.minor == null ? 1 : Integer.compare(minor, vsn.minor);
                if (cmp == 0)
                {
                    cmp = patch == null | vsn.patch == null ? 1 : Integer.compare(patch, vsn.patch);
                }
            }
            return cmp >= 0;
        }

        @Override
        public int compareTo(Version vsn)
        {
            int cmp = compare(major, vsn.major); // equals
            if (cmp == 0)
            {
                cmp = compare(minor, vsn.minor);
                if (cmp == 0)
                {
                    cmp = compare(patch, vsn.patch);
                    if (cmp == 0)
                    {
                        boolean localNullOrEmpty = additionalInfo == null || additionalInfo.isEmpty();
                        boolean otherNullOrEmpty = vsn.additionalInfo == null || additionalInfo.isEmpty();

                        if (localNullOrEmpty)
                        {
                            cmp = otherNullOrEmpty ? 0 : -1;
                        }
                        else
                        {
                            cmp = otherNullOrEmpty ? 1 : 0;
                        }
                    }
                }
            }
            return cmp;
        }

        private int compare(Integer v1, Integer v2)
        {
            // null will be sorted before not-null
            int cmp;
            if (Objects.equals(v1, v2))
            {
                cmp = 0;
            }
            else if (v1 == null && v2 != null)
            {
                cmp = -1;
            }
            else if (v1 != null && v2 == null)
            {
                cmp = 1;
            }
            else
            {
                cmp = Integer.compare(v1, v2);
            }
            return cmp;
        }

        private int compare(String v1, String v2)
        {
            // null will be sorted before not-null
            int cmp;
            if (Objects.equals(v1, v2))
            {
                cmp = 0;
            }
            else if (v1 == null && v2 != null)
            {
                cmp = -1;
            }
            else if (v1 != null && v2 == null)
            {
                cmp = 1;
            }
            else
            {
                cmp = v1.compareTo(v2);
            }
            return cmp;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((major == null) ? 0 : major.hashCode());
            result = prime * result + ((minor == null) ? 0 : minor.hashCode());
            result = prime * result + ((patch == null) ? 0 : patch.hashCode());
            result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (obj == null)
            {
                return false;
            }
            if (getClass() != obj.getClass())
            {
                return false;
            }
            Version other = (Version) obj;
            return Objects.equals(major, other.major) &&
                Objects.equals(minor, other.minor) &&
                Objects.equals(patch, other.patch) &&
                Objects.equals(additionalInfo, other.additionalInfo);
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            if (major != null)
            {
                sb.append(major);
            }
            if (minor != null)
            {
                sb.append(".").append(minor);
            }
            if (patch != null)
            {
                sb.append(".").append(patch);
            }
            if (additionalInfo != null)
            {
                sb.append(additionalInfo);
            }
            return sb.toString();
        }
    }
}
