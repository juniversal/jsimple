namespace jsimple.net
{

	using InputStream = jsimple.io.InputStream;
	using OutputStream = jsimple.io.OutputStream;

	/// <summary>
	/// @author Bret Johnson
	/// @since 7/24/13 11:07 PM
	/// </summary>
	public abstract class Socket : jsimple.lang.AutoCloseable
	{
		public abstract InputStream InputStream {get;}

		public abstract OutputStream OutputStream {get;}

		public abstract bool Closed {get;}
	}

}