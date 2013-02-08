namespace jsimple.io
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/6/12 2:56 AM
	/// </summary>
	public abstract class PathAttributes
	{
		/// <returns> last time the directory/file was modified, in millis. </returns>
		public abstract long LastModifiedTime {get;}

		/// <returns> for a file its size in bytes and for a directory the results are implementation specific </returns>
		public abstract long Size {get;}
	}

}