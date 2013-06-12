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
		internal MemoryDirectory parent;
		private string name;
		private long lastModifiedTime;
		private sbyte[] data = null;

		/// <summary>
		/// Construct a new MemoryFile, with the specified name.  Note that the MemoryFile doesn't actually exist in the
		/// "file system" until openForCreate is called, some contents optionally written, and the stream is closed.  The
		/// file is actually created when the stream is closed.
		/// </summary>
		/// <param name="name"> file name </param>
		public MemoryFile(MemoryDirectory parent, string name)
		{
			this.parent = parent;
			this.name = name;
			this.lastModifiedTime = PlatformUtils.CurrentTimeMillis;
		}

		public override Directory Parent
		{
			get
			{
				return parent;
			}
		}

		public override InputStream openForRead()
		{
			if (data == null)
				throw new PathNotFoundException("MemoryFile {} doesn't currently exist", name);
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
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			byteArrayOutputStream.ClosedListener = new ClosedListenerAnonymousInnerClassHelper(this, byteArrayOutputStream);

			return byteArrayOutputStream;
		}

		private class ClosedListenerAnonymousInnerClassHelper : ClosedListener
		{
			private readonly MemoryFile outerInstance;

			private jsimple.io.ByteArrayOutputStream byteArrayOutputStream;

			public ClosedListenerAnonymousInnerClassHelper(MemoryFile outerInstance, jsimple.io.ByteArrayOutputStream byteArrayOutputStream)
			{
				this.outerInstance = outerInstance;
				this.byteArrayOutputStream = byteArrayOutputStream;
			}

			public virtual void onClosed()
			{
				sbyte[] data = byteArrayOutputStream.closeAndGetByteArray();
				outerInstance.Data = data;

				outerInstance.parent.addOrReplaceFile(outerInstance);
			}
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

		/*
		@Override public File getAtomicFile(){
		    MemoryFile atomicFile = new MemoryFile(this.parent, this.parent.getName());
		    return atomicFile;
		}
		*/

		/// <summary>
		/// Override the data in the file, setting it to the passed byte array.
		/// </summary>
		/// <param name="data"> data for file </param>
		public virtual sbyte[] Data
		{
			set
			{
				this.data = value;
				this.lastModifiedTime = PlatformUtils.CurrentTimeMillis;
			}
		}

		public override long LastModifiedTime
		{
			set
			{
				if (data == null)
					throw new IOException("File {} hasn't yet been created", name);
    
				lastModifiedTime = value;
			}
			get
			{
				if (data == null)
					throw new IOException("File {} hasn't yet been created", name);
    
				return lastModifiedTime;
			}
		}


		public virtual int Size
		{
			get
			{
				if (data == null)
					throw new IOException("File {} hasn't yet been created", name);
    
				return data.Length;
			}
		}

		public override void delete()
		{
			if (!parent.deleteFile(name))
				throw new PathNotFoundException("File {} doesn't exist", name);
		}

		public override bool exists()
		{
			return data != null;
		}

		public override void rename(string newName)
		{
			if (data == null)
				throw new IOException("File {} hasn't yet been created", name);

			// Renaming to current name is a no-op
			if (newName.Equals(name))
				return;

			// Delete any existing file with the old name
			parent.deleteFile(name);

			name = newName;
		}
	}

}