using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace jsimple.io
{
    public class FileSystemDirectory : Directory
    {
        private readonly string directoryPath;

        public FileSystemDirectory(string directoryPath)
        {
            this.directoryPath = directoryPath;
        }

        public override string Name
        {
            get
            {
                return System.IO.Path.GetFileName(directoryPath);
            }
        }

        public override File getFile(string name)
        {
            return new FileSystemFile(this, getChildPath(name));
        }

        public override Directory getDirectory(string name)
        {
            return new FileSystemDirectory(getChildPath(name));
        }

        public override Directory getOrCreateDirectory(string name)
        {
            String subdirectoryPath = getChildPath(name);

            DirectoryInfo directoryInfo = new DirectoryInfo(subdirectoryPath);
            if (! directoryInfo.Exists)
                directoryInfo.Create();

            return new FileSystemDirectory(subdirectoryPath);
        }

        public string getChildPath(string name)
        {
            StringBuilder childPath = new StringBuilder(directoryPath);

            int length = childPath.Length;
            if (length == 0)
                throw new Exception("FileSystemDirectory path is empty string");

            char lastCharacter = directoryPath[length - 1];
            if (lastCharacter != System.IO.Path.DirectorySeparatorChar &&
                lastCharacter != System.IO.Path.AltDirectorySeparatorChar)
                childPath.Append("/");         // JSimple always uses / as the separator

            childPath.Append(name);

            return childPath.ToString();
        }

        public override void visitChildren(DirectoryVisitor visitor)
        {
            DirectoryInfo directoryInfo = new DirectoryInfo(directoryPath);

            try
            {
                foreach (FileSystemInfo fileSystemInfo in directoryInfo.EnumerateFileSystemInfos())
                {
                    FileSystemPathAttributes fileSystemPathAttributes = new FileSystemPathAttributes(fileSystemInfo);

                    if ((fileSystemInfo.Attributes & FileAttributes.Directory) == FileAttributes.Directory)
                        visitor.visit(new FileSystemDirectory(fileSystemInfo.FullName),
                                      fileSystemPathAttributes);
                    else visitor.visit(new FileSystemFile(this, fileSystemInfo.FullName),
                                      fileSystemPathAttributes);
                }
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
                System.IO.Directory.Delete(directoryPath);
            }
            catch (System.IO.IOException e)
            {
                throw DotNetIOUtils.jSimpleExceptionFromDotNetIOException(e);
            }
        }
    }
}