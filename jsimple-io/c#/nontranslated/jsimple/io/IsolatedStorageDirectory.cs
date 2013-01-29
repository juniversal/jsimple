using jsimple.io;

namespace jsimple.io
{
    public class IsolatedStorageDirectory : Directory
    {
        private string name;

        public IsolatedStorageDirectory(string name)
        {
            this.name = name;
        }

        public override string Name
        {
            get { return name; }
        }

        /*
        public override Directory Parent
        {
            get { throw new System.NotImplementedException(); }
        }
         */
        
        public override File getFile(string fileName)
        {
            throw new System.NotImplementedException();
        }

        public override File createFile(string name)
        {
            throw new System.NotImplementedException();
        }

        public override Directory getDirectory(string name)
        {
            throw new System.NotImplementedException();
        }

        public override Directory getOrCreateDirectory(string name)
        {
            throw new System.NotImplementedException();
        }

        public override void visitChildren(DirectoryVisitor visitor)
        {
            throw new System.NotImplementedException();
        }
    }
}