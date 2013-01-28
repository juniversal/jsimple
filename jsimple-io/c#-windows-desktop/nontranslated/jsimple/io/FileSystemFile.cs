using System;
using System.IO;

namespace jsimple.io
{
    public class FileSystemFile : File
    {
        private string filePath;

        public FileSystemFile(string filePath)
        {
            this.filePath = filePath;
        }

        public override string Name
        {
            get
            {
                return System.IO.Path.GetFileName(filePath);
            }
        }

        public override InputStream openForRead()
        {
            try
            {
                FileStream fileStream = System.IO.File.OpenRead(filePath);
                return new DotNetStreamInputStream(fileStream);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
        
        public override Directory Parent
        {
            get
            {
                return new FileSystemDirectory(System.IO.Path.GetDirectoryName(filePath));
            }
        }
    }
}