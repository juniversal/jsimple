using Windows.Storage;

namespace jsimple.io
{
    public class StorageFileFile : File
    {
        private StorageFile storageFile;

        public StorageFileFile(StorageFile storageFile)
        {
            this.storageFile = storageFile;
        }

        public override string Name
        {
            get { throw new System.NotImplementedException(); }
        }

        public override Directory Parent
        {
            get { throw new System.NotImplementedException(); }
        }

        public override InputStream openForRead()
        {
            throw new System.NotImplementedException();
        }
    }
}