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

		/*
		public abstract File getAtomicFile();
		*/

		public virtual OutputStream openForCreateAtomic()
		{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final String fileName = getName();
			string fileName = Name;
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final File tempFile= getParent().getFile(fileName + "-temp");
			File tempFile = Parent.getFile(fileName + "-temp");

			OutputStream stream = tempFile.openForCreate();

			stream.ClosedListener = new ClosedListenerAnonymousInnerClassHelper(this, fileName, tempFile);

			return stream;
		}

		private class ClosedListenerAnonymousInnerClassHelper : ClosedListener
		{
			private readonly File outerInstance;

			private string fileName;
			private jsimple.io.File tempFile;

			public ClosedListenerAnonymousInnerClassHelper(File outerInstance, string fileName, jsimple.io.File tempFile)
			{
				this.outerInstance = outerInstance;
				this.fileName = fileName;
				this.tempFile = tempFile;
			}

			public virtual void onClosed()
			{

				outerInstance.delete();
				tempFile.rename(fileName);
			}
		}

		/// <summary>
		/// Delete this file.
		/// </summary>
		public abstract void delete();

		/// <summary>
		/// See if the file exists.  If there's an error checking if the file exists, this method throws an exception when
		/// possible, though for some platform implementations it'll just return false if platform can't distinguish not
		/// existing from there being an error checking.
		/// </summary>
		/// <returns> true if the file exists </returns>
		public virtual bool exists()
		{
			return false;
		}

		/// <summary>
		/// Rename this file, giving it a new name in the same directory.  If a file with the specified name already exists,
		/// an exception is thrown.  If/when there's the need to move a File to a different directory, we'll add separate
		/// support for that.
		/// </summary>
		/// <param name="newName"> </param>

		public abstract void rename(string newName);

		public abstract Directory Parent {get;}
	}

}