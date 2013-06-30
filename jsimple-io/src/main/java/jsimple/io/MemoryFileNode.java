package jsimple.io;

import jsimple.util.PlatformUtils;
import org.jetbrains.annotations.Nullable;

/**
* @author Bret Johnson
* @since 6/29/13 5:25 PM
*/
class MemoryFileNode {
    private long lastModifiedTime ;
    private byte[] data;

    public MemoryFileNode() {
        lastModifiedTime = PlatformUtils.getCurrentTimeMillis();
        data = new byte[0];
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Nullable public byte[] getData() {
        return data;
    }

    public void setData(@Nullable byte[] data) {
        this.data = data;
    }
}
