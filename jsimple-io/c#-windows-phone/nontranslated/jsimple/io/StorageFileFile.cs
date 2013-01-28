using System.IO;
using Windows.Storage;
using Windows.Storage.Streams;

namespace jsimple.io
{
    public class StorageFileFile : File
    {
        private readonly StorageFile storageFile;

        public StorageFileFile(StorageFile storageFile)
        {
            this.storageFile = storageFile;
        }

        public override string Name
        {
            get { return storageFile.Name; }
        }

        public override InputStream openForRead()
        {
            IInputStream inputStream = storageFile.OpenSequentialReadAsync().DoSynchronously();

            // Use the default 16K buffer for AsStreamForRead()
            return new DotNetStreamInputStream(inputStream.AsStreamForRead());
        }
    }
}