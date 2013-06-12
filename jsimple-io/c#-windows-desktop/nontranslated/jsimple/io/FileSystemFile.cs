using System;
using System.IO;
using jsimple.util;

namespace jsimple.io
{
    public class FileSystemFile : File
    {
        private readonly FileSystemDirectory parent;
        private string filePath;

        public FileSystemFile(FileSystemDirectory parent, string filePath)
        {
            this.parent = parent;
            this.filePath = filePath;
        }

        /*
        public override File AtomicFile
        {
            get
            {
                FileSystemFile atomicFile = new FileSystemFile(this.parent, this.filePath + "-temp");
                return atomicFile;
            }
        }
         */

        public override string Name
        {
            get
            {
                return System.IO.Path.GetFileName(filePath);
            }
        }

        public override Directory Parent
        {
            get { return parent; }
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


        public override OutputStream openForCreate()
        {
            try
            {
                FileStream fileStream = System.IO.File.OpenWrite(filePath);
                return new DotNetStreamOutputStream(fileStream);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override bool exists()
        {
            return System.IO.File.Exists(filePath);
        }
        
        public override void delete()
        {
            try
            {
                System.IO.File.Delete(filePath);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override void rename(string newName)
        {
            try
            {
                // TODO: Catch exception and overwrite file if already exists
                System.IO.File.Move(filePath, parent.getChildPath(newName));
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }

        public override long LastModifiedTime
        {
            get
            {
                try
                {
                    return PlatformUtils.toMillisFromDateTime(System.IO.File.GetLastWriteTimeUtc(filePath));
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }

            set
            {
                try
                {
                    System.IO.File.SetLastWriteTimeUtc(filePath, PlatformUtils.toDotNetDateTimeFromMillis(value));
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }
        }
    }
}