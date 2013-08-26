using System.IO;
using jsimple.util;

namespace jsimple.io
{
    public class FileSystemFile : File
    {
        private readonly string filePath;
        private readonly FileSystemDirectory parent;

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
            get { return System.IO.Path.GetFileName(filePath); }
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
			// TODO: Change to catch directory not existing exception & ignore
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
            string destPath = parent.getChildPath(newName);

            try
            {
                System.IO.File.Move(filePath, destPath);
            }
            catch (System.IO.IOException e)
            {
                // If the destination file already exists (0x800700B7 is "Cannot create a file when that file already
                // exists"), then delete it and retry the move.
                if (e.HResult == unchecked((int) 0x800700B7))
                {
                    try
                    {
                        System.IO.File.Delete(destPath);
                        System.IO.File.Move(filePath, destPath);
                    }
                    catch (System.IO.IOException e2)
                    {
                        throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e2);
                    }
                }
            else
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

        public override long Size
        {
            get
            {
                try
                {
                    return new FileInfo(filePath).Length;
                }
                catch (System.IO.IOException e)
                {
                    throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
                }
            }
        }
    }
}
