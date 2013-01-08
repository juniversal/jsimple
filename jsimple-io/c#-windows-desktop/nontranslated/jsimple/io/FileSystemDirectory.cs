using System;
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

        public override Directory Parent
        {
            get
            {
                string parentDirectoryName = System.IO.Path.GetDirectoryName(directoryPath);
                if (parentDirectoryName == null)
                    return null;
                
                return new FileSystemDirectory(parentDirectoryName);
            }
        }

        public override File getChildFile(string name)
        {
            return new FileSystemFile(getChildPath(name));
        }

        public override Directory getChildDirectory(string name)
        {
            return new FileSystemDirectory(getChildPath(name));
        }

        private string getChildPath(string name)
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
            // TODO: Implement this
            throw new System.NotImplementedException();
        }
    }
}