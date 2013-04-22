namespace jsimple.net
{

	using UnitTest = jsimple.unit.UnitTest;
	using NUnit.Framework;

	/// <summary>
	/// @author Bret Johnson
	/// @since 11/25/12 10:58 PM
	/// </summary>
	public class UrlEncoderTest : UnitTest
	{
		[Test] public virtual void testEncode()
		{
			assertEquals(".-_*", UrlEncoder.encode(".-_*")); // None encoded
			assertEquals("%24%2B%21%27%28%29%2C", UrlEncoder.encode("$+!'(),")); // All encoded
			assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", UrlEncoder.encode("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")); // None encoded
			assertEquals("x%C3%AB%C4%86%D2%B8abc%EF%AE%9A", UrlEncoder.encode("xëĆҸabcﮚ"));
			assertEquals("abc%3Ddef%3Fsdf%26hil+sdlkfj+l+sdf+slkdfj+123%26def", UrlEncoder.encode("abc=def?sdf&hil sdlkfj l sdf slkdfj 123&def"));
		}
	}

}