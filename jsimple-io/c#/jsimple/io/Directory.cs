namespace jsimple.io
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/22/12 12:14 AM
	/// </summary>
	public abstract class Directory : Path
	{
		/// <summary>
		/// Get a file under the directory, which may or may not already exist.
		/// </summary>
		/// <param name="name"> file name </param>
		/// <returns> File object, that's a child of this directory </returns>
		public abstract File getFile(string name);

		/// <summary>
		/// Get the child directory, which must already exist under this directory.  If the directory doesn't exist, the
		/// results are undefined (for some implementations it will fail right away & others will fail later, like when visit
		/// the children of the directory or open a file).
		/// </summary>
		/// <param name="name"> directory name </param>
		/// <returns> Directory object, that's a child of this directory </returns>
		public abstract Directory getDirectory(string name);

		/// <summary>
		/// Get the child directory, creating it if it doesn't already exist.  For all implementations, the persistent
		/// directory will actually exist after this method returns.
		/// </summary>
		/// <param name="name"> directory name </param>
		/// <returns> child directory </returns>
		public abstract Directory getOrCreateDirectory(string name);

		/// <summary>
		/// Visit the child elements of this path--basically list the files and subdirectories of a directory, calling the
		/// visitor for each.  Just direct children are listed, not all descendants; callers can call this method recursively
		/// if they want to visit all descendants.
		/// </summary>
		public abstract void visitChildren(DirectoryVisitor visitor);

		/// <summary>
		/// Delete this directory.  The directory must be empty; if it isn't the results are undefined--for some
		/// implementations it will fail and for others delete the directory and its contents.
		/// </summary>
		public abstract void delete();
	}

}