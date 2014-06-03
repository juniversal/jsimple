using Windows.Storage;
using Windows.Storage.FileProperties;
using jsimple.util;

namespace jsimple.io
{
    public class StorageItemPathAttributes : PathAttributes
    {
        private readonly IStorageItem storageItem;
        private BasicProperties basicProperties = null;

        public StorageItemPathAttributes(IStorageItem storageItem)
        {
            this.storageItem = storageItem;
        }

        public BasicProperties getBasicProperties()
        {
            if (basicProperties == null)
                basicProperties = storageItem.GetBasicPropertiesAsync().DoSynchronously();
            return basicProperties;
        }

        public override long LastModifiedTime
        {
            get { return PlatformUtils.toMillisFromDateTimeOffset(getBasicProperties().DateModified); }
        }

        public override long Size
        {
            get { return (long)getBasicProperties().Size; }
        }
    }
}