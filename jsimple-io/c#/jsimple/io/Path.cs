namespace jsimple.io
{



	/// <summary>
	/// A Path is an abstraction over file system-like data structures.  A Directory path can contain other Directories and
	/// Files.  The caller can get an input stream / output stream to read/write a File.
	/// 
	/// @author Bret Johnson
	/// @since 11/22/12 12:14 AM
	/// </summary>
	public abstract class Path
	{
		/// <summary>
		/// Get the name of this file/directory--the last component of the path.
		/// </summary>
		/// <returns> name of this file/directory </returns>
		public abstract string Name {get;}

		public override string ToString()
		{
			return Name;
		}

		/// <summary>
		/// Get the extension (the text after the period) from the specified file/directory name.  The period itself isn't
		/// returned, just the text after.  If there's no extension, the empty string is returned. s
		/// </summary>
		/// <param name="name"> file/directory name </param>
		/// <returns> extension part of the name (minus the period) or the empty string if there's no extension </returns>
		public static string getNameExtension(string name)
		{
			int periodIndex = name.LastIndexOf('.');

			if (periodIndex == -1)
				return "";
			else
				return name.Substring(periodIndex + 1);
		}
	}

}