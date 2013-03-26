namespace jsimple.io
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 3/23/13 4:39 PM
	/// </summary>
	public class MemoryPathAttributes : PathAttributes
	{
		private long lastModifiedTime;
		private long size;

		public MemoryPathAttributes(long lastModifiedTime, long size)
		{
			this.lastModifiedTime = lastModifiedTime;
			this.size = size;
		}

		/// <returns> last time the directory/file was modified, in millis. </returns>
		public override long LastModifiedTime
		{
			get
			{
				return 0; //To change body of implemented methods use File | Settings | File Templates.
			}
		}

		/// <returns> for a file its size in bytes and for a directory the results are implementation specific </returns>
		public override long Size
		{
			get
			{
				return 0; //To change body of implemented methods use File | Settings | File Templates.
			}
		}
	}

}