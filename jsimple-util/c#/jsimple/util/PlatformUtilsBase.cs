using System;

namespace jsimple.util
{

	/// <summary>
	/// This class is used to wrap one off utility methods that need to be implemented in a platform dependent way.  Methods
	/// here are static & not actually defined in the base class--just a template comment is given here, and the method
	/// should actually be implemented in the platform dependent PlatformUtils subclass.  Callers should call, for example,
	/// with the syntax PlatformUtils.stringGetChars().
	/// <p/>
	/// By using the above scheme, calls are static & most efficient, there's no need for extra infrastructure around a
	/// factory class, and perhaps most importantly if the platform doesn't implement a method which is actually needed by
	/// some code it uses, that problem is caught at compile time not run time.
	/// 
	/// @author Bret Johnson
	/// @since 10/21/12 3:26 PM
	/// </summary>
	public class PlatformUtilsBase
	{
		public static long currentTimeOverride = DateTime.NULL_DATE;

		/// <summary>
		/// Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
		/// we generally use in JSimple.
		/// </summary>
		/// <returns> number of milliseconds since 1/1/70 UTC/GMT </returns>
		public static long CurrentTimeMillis
		{
			get
			{
				if (currentTimeOverride != DateTime.NULL_DATE)
					return currentTimeOverride;
				else
					return PlatformUtils.platformGetCurrentTimeMillis();
			}
		}

		/// <summary>
		/// Set an override for the current time, that will then be returned from getCurrentTimeMillis instead of the actual
		/// time being returned.   This method can be used in test jigs, to simulate the time, so tests are deterministic
		/// with respect to time or perhaps to make a certain amount of time pass for code that checks getCurrentTimeMillis.
		/// If the specified time is DateTime.NULL_DATE, then any override is cleared and the real time will be returned
		/// again from getCurrentTimeMillis.
		/// </summary>
		/// <param name="currentTimeOverrideInMillis"> override for current time, in millis, or DateTime.NULL_DATE to clear override </param>
		public static long CurrentTimeOverride
		{
			set
			{
				PlatformUtilsBase.currentTimeOverride = value;
			}
		}

		/// <summary>
		/// Sort the elements of the list in their natural order (that is, as specified by the Comparable interface they
		/// implement).  The sortList isn't guaranteed to be stable (it actually is in the Java version but not in the C# version).
		/// </summary>
		/// <param name="list"> input list </param>
		/// @param <T>  list element type </param>
		//public static <T extends Comparable<? super T>> void sortList(List<T> list)

		/// <summary>
		/// Sort the elements of the list based on the Comparator callback passed in.  The sort isn't guaranteed to be stable
		/// (it actually is in the Java version but not in the C# version).
		/// </summary>
		/// <param name="list"> input list </param>
		/// @param <T>  list element type </param>
		//public static <T> void sortList(List<T> list, Comparator<? super T> comparator)

		/// <summary>
		/// Get the default line separator (typically newline or carriage return/newline) for the platform.
		/// </summary>
		/// <returns> default line separator for the platform </returns>
		//public static String getLineSeparator()

		/// <summary>
		/// Get the message + stack trace associated with this exception.
		/// </summary>
		/// <param name="e"> throwable in question </param>
		/// <returns> string containing the message & stack trace for the exception </returns>
		//public static String getMessageAndStackTrace(Throwable e);

		/// <summary>
		/// Make the current thread sleep for the specified number of milliseconds.
		/// </summary>
		/// <param name="sleepTimeInMilliseconds"> time to sleep, in milliseconds </param>
		//public static void sleep(int sleepTimeInMilliseconds);

		/// <summary>
		/// Copy data from source byte array to destination byte array.
		/// </summary>
		/// <param name="src"> source array </param>
		/// <param name="srcPos"> starting position in source array </param>
		/// <param name="dest"> destination array </param>
		/// <param name="destPos"> position in destination array </param>
		/// <param name="length"> number of elements to copy </param>
		//public static void copyBytes(byte[] src, int srcPos, byte[] dest, int destPos, int length);

		/// <summary>
		/// Copy data from source char array to destination char array.
		/// </summary>
		/// <param name="src"> source array </param>
		/// <param name="srcPos"> starting position in source array </param>
		/// <param name="dest"> destination array </param>
		/// <param name="destPos"> position in destination array </param>
		/// <param name="length"> number of elements to copy </param>
		//public static void copyChars(char[] src, int srcPos, char[] dest, int destPos, int length);

		/// <summary>
		/// Use the whatever proxy settings are configured in the OS.  On some platform, by default no web proxy is used.
		/// Calling this method causes the OS proxy default to be used instead.
		/// </summary>
		//public static void useSystemProxy() {
	}

}