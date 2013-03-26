namespace jsimple.io
{

	using PlatformUtils = jsimple.util.PlatformUtils;


	/// <summary>
	/// A MemoryFile is a File object that exists completely in memory.  It can be used in unit tests, where it can be more
	/// convenient to construct directories/files in memory rather than on disk.  It can also be used for "temporary" files.
	/// 
	/// @author Bret Johnson
	/// @since 3/23/13 1:51 PM
	/// </summary>
	public class MemoryFile : File
	{
		private string name;
		private long lastModificationTime;
		private sbyte[] data = null;

		/// <summary>
		/// Create a new MemoryFile, with the specified name.
		/// </summary>
		/// <param name="name"> file name </param>
		public MemoryFile(string name)
		{
			this.name = name;
			this.lastModificationTime = PlatformUtils.CurrentTimeMillis;
		}

		public override InputStream openForRead()
		{
			if (data == null)
				throw new FileNotFoundException("MemoryFile " + name + " doesn't currently exist");
			else
				return new ByteArrayInputStream(data);
		}

		/// <summary>
		/// Open the file for writing.  If the file already exists, it is overwritten.  If the file doesn't exist, it is
		/// created.  In both cases, the file contents only get set when the returned stream is closed, so the caller must be
		/// sure to close it (like any stream, results are undefined if not closed).
		/// </summary>
		/// <returns> output stream, to write the file contents </returns>
		public override OutputStream openForCreate()
		{
			return new MemoryFileByteArrayOutputStream(this);
		}

		/// <summary>
		/// Get the name of this file/directory--the last component of the path.
		/// </summary>
		/// <returns> name of this file/directory </returns>
		public override string Name
		{
			get
			{
				return name;
			}
		}

		/// <summary>
		/// Override the data in the file, setting it to the passed byte array.
		/// </summary>
		/// <param name="data"> data for file </param>
		public virtual sbyte[] Data
		{
			set
			{
				this.data = value;
				this.lastModificationTime = PlatformUtils.CurrentTimeMillis;
			}
		}

		public virtual long LastModificationTime
		{
			get
			{
				return lastModificationTime;
			}
		}

		public virtual int Size
		{
			get
			{
				if (data == null)
					throw new IOException("File " + name + " hasn't yet been created");
    
				return data.Length;
			}
		}

		private class MemoryFileByteArrayOutputStream : ByteArrayOutputStream
		{
			internal MemoryFile memoryFile;
			internal bool closed = false;

			public MemoryFileByteArrayOutputStream(MemoryFile memoryFile)
			{
				this.memoryFile = memoryFile;
			}

			/// <summary>
			/// Closes this stream. This releases system resources used for this stream.  The closed flag is needed so that the
			/// stream (like any stream) can be closed multiple times, but only the first close actually does anything, grabbing
			/// the data and saving it off.  Subsequent closes are ignored.
			/// </summary>
			public override void close()
			{
				if (!closed)
				{
					sbyte[] data = base.closeAndGetByteArray();
					closed = true;
					memoryFile.Data = data;
				}
			}
		}
	}

}