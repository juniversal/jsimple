using System.Text;

namespace jsimple.net {

    using IOUtils = jsimple.io.IOUtils;
    using ByteArrayRange = jsimple.util.ByteArrayRange;
    using TextualPath = jsimple.util.TextualPath;

    /// <summary>
    /// @author Bret Johnson
    /// @since 11/25/12 9:03 PM
    /// </summary>
    public class UrlEncoder {
        /// <summary>
        /// Encodes the given string {@code s} in a x-www-form-urlencoded string using UTF8 character encoding.
        /// <p/>
        /// All characters except letters ('a'..'z', 'A'..'Z') and numbers ('0'..'9') and characters '.', '-', '*', '_' are
        /// converted into their hexadecimal value prepended by '%'. For example: '#' -> %23. In addition, spaces are
        /// substituted by '+'
        /// </summary>
        /// <param name="s"> the string to be encoded. </param>
        /// <returns> the encoded string. </returns>
        /// <exception cref="java.io.UnsupportedEncodingException"> if the specified encoding scheme is invalid. </exception>
        public static string encode(string s) {
            // Guess a bit bigger for encoded form
            StringBuilder buffer = new StringBuilder(s.Length + 16);
            int start = -1;
            for (int i = 0; i < s.Length; i++) {
                char ch = s[i];
                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || " .-*_".IndexOf(ch) > -1) {
                    if (start >= 0) {
                        convert(s.Substring(start, i - start), buffer);
                        start = -1;
                    }
                    if (ch != ' ')
                        buffer.Append(ch);
                    else
                        buffer.Append('+');
                }
                else {
                    if (start < 0)
                        start = i;
                }
            }

            if (start >= 0)
                convert(s.Substring(start, s.Length - start), buffer);

            return buffer.ToString();
        }

        public static string encode(TextualPath path) {
            if (path.Empty)
                return "/";
            else {
                StringBuilder encodedPath = new StringBuilder();
                foreach (string pathElement in path.PathElements) {
                    encodedPath.Append("/");
                    encodedPath.Append(encode(pathElement));
                }
                return encodedPath.ToString();
            }
        }

        private const string digits = "0123456789ABCDEF";

        private static void convert(string s, StringBuilder buffer) {
            ByteArrayRange byteArrayRange = IOUtils.toUtf8BytesFromString(s);
            int length = byteArrayRange.Length;
            sbyte[] bytes = byteArrayRange.Bytes;

            for (int j = byteArrayRange.Position; j < length; j++) {
                buffer.Append('%');
                sbyte currByte = bytes[j];
                buffer.Append(digits[(currByte & 0xf0) >> 4]);
                buffer.Append(digits[currByte & 0xf]);
            }
        }
    }

}