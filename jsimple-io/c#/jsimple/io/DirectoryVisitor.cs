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
		public abstract bool visitFailed(string name, IOException exception);
	}

}