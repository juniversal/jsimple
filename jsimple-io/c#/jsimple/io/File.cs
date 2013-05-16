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

		public abstract OutputStream openForCreateAtomic();
		/*
	
		public OutputStream openForCreateAtomic() {
		    // FIRST CREATE OutputStream, to file name + "-temp"
	
		    // SET ClosedListener for OutputStream SO WHEN CALLBACK TO IT, AFTER CLOSE, CAN DELETE ORIGINAL FILE AND
		    // RENAME -temp to ORIGINAL FILE
	
		    /*
	
		    File tempFile = new File(...);
		    OutputStream stream = tempFile.openForCreate();
		    stream.setCLosedHandler(new OutputStream.ClosedHandler {
		        void onClosed() {
		            // DELETE ORIGINAL FILE
		            // RENAME -temp TO ORIGINAL NAME
		        }
		    });
	
	
	
	
		    // TODO: IMPLEMENT THIS
		    return null;
		}
		*/

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