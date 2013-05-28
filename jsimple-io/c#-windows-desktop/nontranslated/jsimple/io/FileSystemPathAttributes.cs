using System.IO;
using jsimple.util;

namespace jsimple.io
{
    public class FileSystemPathAttributes : PathAttributes
    {
        private readonly FileSystemInfo fileSystemInfo;

        public FileSystemPathAttributes(FileSystemInfo fileSystemInfo)
        {
            this.fileSystemInfo = fileSystemInfo;
        }

        public override long LastModifiedTime
        {
            get { return PlatformUtils.toMillisFromDateTime(fileSystemInfo.LastWriteTimeUtc); }
        }

        public override long Size
        {
            get
            {
                FileInfo fileInfo = fileSystemInfo as FileInfo;
                return fileInfo != null ? fileInfo.Length : 0;
            }
        }
    }
}