using System;
using System.IO;

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
                System.IO.File.Move(filePath, newName);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
    }
}