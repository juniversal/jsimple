namespace jsimple.io
{

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/6/12 2:51 AM
	/// </summary>
	public abstract class DirectoryVisitor
	{
		public abstract bool visit(File file, PathAttributes attributes);

		public abstract bool visit(Directory directory, PathAttributes attributes);

		/// <summary>
		/// Called to indicate that the visit to the file/directory fails (e.g. because don't have rights to read the
		/// file/directory attributes).
		/// </summary>
		/// <param name="name"> </param>
		/// <param name="exception"> </param>
		/// <returns> true to continue, false to abort </returns>
		public virtual bool visitFailed(string name, IOException exception)
		{
			throw exception;
		}
	}

}