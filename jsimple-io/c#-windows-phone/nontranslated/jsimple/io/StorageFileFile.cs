using System.IO;
using Windows.Storage;
using Windows.Storage.Streams;

namespace jsimple.io
{
    public class StorageFileFile : File
    {
        private readonly StorageFolderDirectory parent;
        private readonly StorageFile storageFile;

        public StorageFileFile(StorageFolderDirectory parent, StorageFile storageFile)
        {
            this.parent = parent;
            this.storageFile = storageFile;
        }

        public override string Name
        {
            get { return storageFile.Name; }
        }

        public override Directory Parent
        {
            get { return parent; }
        }

        public override InputStream openForRead()
        {
            IInputStream inputStream = storageFile.OpenSequentialReadAsync().DoSynchronously();

            // Use the default 16K buffer for AsStreamForRead()
            return new DotNetStreamInputStream(inputStream.AsStreamForRead());
        }

        public override OutputStream openForCreate()
        {
            // TODO: Possibly rework this some, since for one thing openForCreate won't truncate the file if it already exists.  Reconsider the spec here.
            IRandomAccessStream randomAccessStream = storageFile.OpenAsync(FileAccessMode.ReadWrite).DoSynchronously();

            IOutputStream outputStream = randomAccessStream.GetOutputStreamAt(0);

            // Use the default buffer for AsStreamForWrite()
            return new DotNetStreamOutputStream(outputStream.AsStreamForWrite());
        }

        public override void delete()
        {
            storageFile.DeleteAsync(StorageDeleteOption.PermanentDelete).DoSynchronously();
        }

        public override void rename(string newName)
        {
            // TODO: Implement this
            try
            {
                Windows.Storage.IStorageFolder storageFolder = (IStorageFolder)this.parent;
                storageFile.MoveAsync(storageFolder, newName);
            }
            catch (IOException e)
            {
                throw new System.NotImplementedException();
            }
        }
    }
}
