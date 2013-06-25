namespace jsimple.io
{

	/// <summary>
	/// Created with IntelliJ IDEA.
	/// 
	/// @author Bret Johnson
	/// @since 1/19/13 4:31 PM
	/// </summary>
	public class PathsBase
	{
		/// <returns> the directory where the application should store its private data. </returns>
		//public static Directory getApplicationDataDirectory();

		/// <summary>
		/// Return the Directory object corresponding to the string serialized representation.  If the directory doesn't
		/// exist, it's implementation dependent whether this method throws a PathNotFoundException or a
		/// PathNotFoundException is thrown when the returned directory is used--different implementations do different
		/// things there.
		/// <p/>
		/// Not all implementations of Directory support serializing it as a string.  An exception is thrown if it's
		/// not supported.
		/// </summary>
		/// <param name="directoryString">
		/// @return </param>
		//public static Directory getDirectory(String directoryPathString);
	}

}