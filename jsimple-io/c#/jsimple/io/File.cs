namespace jsimple.io
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/22/12 12:14 AM
	/// </summary>
	public abstract class File : Path
	{
		public abstract InputStream openForRead();

		/// <summary>
		/// Open the file for writing.  If the file already exists, it is truncated.  If the file doesn't exist, it is
		/// created (assuming Directory.createFile was called to get this File object, otherwise the results are undefined).
		/// Note that the current JSimple file I/O model is that files, when written, are completely rewritten.
		/// </summary>
		/// <returns> output stream, to write the file contents </returns>
		public abstract OutputStream openForCreate();

		/// <summary>
		/// Delete this file.
		/// </summary>
		public abstract void delete();
	}

}