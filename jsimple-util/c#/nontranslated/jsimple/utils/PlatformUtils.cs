using System;
using Windows.Security.Cryptography;
using Windows.Security.Cryptography.Core;
using Windows.Storage.Streams;

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
    public class PlatformUtils : PlatformUtilsBase
    {
        /// <summary>
        /// Get the number of milliseconds since Jan 1, 1970, UTC time.  That's also known as epoch time.  It's the time unit
        /// we generally use in JSimple.
        /// </summary>
        /// <returns> number of milliseconds since 1/1/70 UTC/GMT </returns>
        /// 
        private static readonly DateTime Jan1st1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
        public static long CurrentTimeMillis
        {
            get
            {
                return (long) (DateTime.UtcNow - Jan1st1970).TotalMilliseconds;
            }
        }

		/// <summary>
		/// Given the specified bytes to sign and key, return the HMAC SHA1 signature for those bytes.
		/// </summary>
		/// <param name="toSign">   bytes to sign (perhaps a string converted to UTF8) </param>
		/// <param name="keyBytes"> key </param>
		/// <returns> HMAC SHA1 signature bytes (which could then be base64 encoded, if appropriate) </returns>
        public byte[] getHmacSha1Signature(byte[] toSign, int toSignLength, byte[] keyBytes, int keyBytesLength)
        {
            MacAlgorithmProvider hmacSha1 = MacAlgorithmProvider.OpenAlgorithm(MacAlgorithmNames.HmacSha1);

            CryptographicKey key = hmacSha1.CreateKey(CryptographicBuffer.CreateFromByteArray(keyBytes));

            IBuffer signature = CryptographicEngine.Sign(key, CryptographicBuffer.CreateFromByteArray(toSign));

            byte[] signatureBytes;
            CryptographicBuffer.CopyToByteArray(signature, out signatureBytes);

            return signatureBytes;
        }

    }
}