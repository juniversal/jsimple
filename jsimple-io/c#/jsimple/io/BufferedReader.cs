using System.Text;

namespace jsimple.io {

    using PlatformUtils = jsimple.util.PlatformUtils;

    /// <summary>
    /// This class was based on, and modified from, the Apache Harmony java.io.BufferedReader class.  This class supports
    /// mark and reset, while the base JSimple Reader class doesn't; unlike standard Java there's no markSupported method
    /// since the only Reader that may support mark/reset--BufferedReader--always supports it.  Also, unlike the standard
    /// BufferedReader, this class isn't synchronized & thus isn't thread safe--the caller needs to synchronize if required.
    /// Basically, this class has these purposes in life: (1) Add buffering, though that's not required as much since
    /// Utf8InputStreamReader has a little 1K buffer built in and StringReader is one huge buffer (2) Add mark & reset
    /// support, so you can "unread" stuff and (3) add readLine (which requires unreading the get the line terminators
    /// right).
    /// <p/>
    /// Wraps an existing <seealso cref="Reader"/> and <em>buffers</em> the input. Expensive interaction with the underlying reader is
    /// minimized, since most (smaller) requests can be satisfied by accessing the buffer alone. The drawback is that some
    /// extra space is required to hold the buffer and that copying takes place when filling that buffer, but this is usually
    /// outweighed by the performance benefits.
    /// <p/>
    /// <p/>A typical application pattern for the class looks like this:
    /// <pre>
    /// BufferedReader buf = new BufferedReader(new FileReader("file.java"));
    /// </pre>
    /// </summary>
    public class BufferedReader : Reader {
        private Reader reader;

        /// <summary>
        /// The characters that can be read and refilled in bulk. We maintain three
        /// indices into this buffer:<pre>
        ///     { X X X X X X X X X X X X - - }
        ///           ^     ^             ^
        ///           |     |             |
        ///         mark   pos           end</pre>
        /// Pos points to the next readable character. End is one greater than the last readable character. When {@code pos
        /// == end}, the buffer is empty and must be <seealso cref="#fillBuf() filled"/> before characters can be read.
        /// <p/>
        /// <para>Mark is the value pos will be set to on calls to <seealso cref="#reset"/>. Its value is in the range {@code [0...pos]}.
        /// If the mark is {@code -1}, the buffer cannot be reset.
        /// <p/>
        /// </para>
        /// <para>MarkLimit limits the distance between the mark and the pos. When this limit is exceeded, <seealso cref="#reset"/> is
        /// permitted (but not required) to throw an exception. For shorter distances, <seealso cref="#reset"/> shall not throw (unless
        /// the reader is closed).
        /// </para>
        /// </summary>
        private char[] buf;
        private int pos;
        private int end;
        private int markPos = -1;
        private int markLimit = -1;

        /// <summary>
        /// Constructs a new BufferedReader on the Reader {@code in}. The buffer gets the default size (8 KB).
        /// </summary>
        /// <param name="reader"> the Reader that is buffered. </param>
        public BufferedReader(Reader reader) {
            this.reader = reader;
            buf = new char[8192];
        }

        /// <summary>
        /// Constructs a new BufferedReader on the Reader {@code in}. The buffer size is specified by the parameter {@code
        /// size}.
        /// </summary>
        /// <param name="reader"> the Reader that is buffered. </param>
        /// <param name="size">   the size of the buffer to allocate. </param>
        /// <exception cref="IllegalArgumentException"> if {@code size <= 0}. </exception>
        public BufferedReader(Reader reader, int size) {
            this.reader = reader;
            buf = new char[size];
        }

        /// <summary>
        /// Closes this reader. This implementation closes the buffered source reader and releases the buffer. Nothing is
        /// done if this reader has already been closed.
        /// </summary>
        public override void close() {
            if (!Closed) {
                reader.close();
                buf = null;
            }
        }

        /// <summary>
        /// Populates the buffer with data. It is an error to call this method when the buffer still contains data; ie. if
        /// {@code pos < end}.
        /// </summary>
        /// <returns> the number of bytes read into the buffer, or -1 if the end of the source stream has been reached. </returns>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: private int fillBuf() throws IOException
        private int fillBuf() {
            // assert(pos == end);

            if (markPos == -1 || (pos - markPos >= markLimit)) {
                /* mark isn't set or has exceeded its limit. use the whole buffer */
                int result = reader.read(buf, 0, buf.Length);
                if (result > 0) {
                    markPos = -1;
                    pos = 0;
                    end = result;
                }
                return result;
            }

            if (markPos == 0 && markLimit > buf.Length) {
                /* the only way to make room when mark=0 is by growing the buffer */
                int newLength = buf.Length * 2;
                if (newLength > markLimit)
                    newLength = markLimit;
                char[] newbuf = new char[newLength];
                PlatformUtils.copyChars(buf, 0, newbuf, 0, buf.Length);
                buf = newbuf;
            }
            else if (markPos > 0) {
                /* make room by shifting the buffered data to left mark positions */
                PlatformUtils.copyChars(buf, markPos, buf, 0, buf.Length - markPos);
                pos -= markPos;
                end -= markPos;
                markPos = 0;
            }

            /* Set the new position and mark position */
            int count = reader.read(buf, pos, buf.Length - pos);
            if (count != -1)
                end += count;

            return count;
        }

        /// <summary>
        /// Indicates whether or not this reader is closed.
        /// </summary>
        /// <returns> {@code true} if this reader is closed, {@code false} otherwise. </returns>
        private bool Closed {
            get {
                return buf == null;
            }
        }

        /// <summary>
        /// Sets a mark position in this reader. The parameter {@code markLimit} indicates how many characters can be read
        /// before the mark is invalidated. Calling {@code reset()} will reposition the reader back to the marked position if
        /// {@code markLimit} has not been surpassed.
        /// </summary>
        /// <param name="markLimit"> the number of characters that can be read before the mark is invalidated. </param>
        /// <seealso cref= #reset() </seealso>
        public virtual void mark(int markLimit) {
            verifyNotClosed();
            this.markLimit = markLimit;
            markPos = pos;
        }

        /// <summary>
        /// Reads a single character from this reader and returns it with the two higher-order bytes set to 0. If possible,
        /// BufferedReader returns a character from the buffer. If there are no characters available in the buffer, it fills
        /// the buffer and then returns a character. It returns -1 if there are no more characters in the source reader.
        /// </summary>
        /// <returns> the character read or -1 if the end of the source reader has been reached. </returns>
        /// <exception cref="IOException"> if this reader is closed or some other I/O error occurs. </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: @Override public int read() throws IOException
        public override int read() {
            verifyNotClosed();

            /* Are there buffered characters available? */
            if (pos < end || fillBuf() != -1)
                return buf[pos++];

            return -1;
        }

        /// <summary>
        /// Reads at most {@code length} characters from this reader and stores them at {@code offset} in the character array
        /// {@code buffer}. Returns the number of characters actually read or -1 if the end of the source reader has been
        /// reached. If all the buffered characters have been used, a mark has not been set and the requested number of
        /// characters is larger than this readers buffer size, BufferedReader bypasses the buffer and simply places the
        /// results directly into {@code buffer}.
        /// </summary>
        /// <param name="buffer"> the character array to store the characters read. </param>
        /// <param name="offset"> the initial position in {@code buffer} to store the bytes read from this reader. </param>
        /// <param name="length"> the maximum number of characters to read, must be non-negative. </param>
        /// <returns> number of characters read or -1 if the end of the source reader has been reached. </returns>
        /// <exception cref="IndexOutOfBoundsException"> if {@code offset < 0} or {@code length < 0}, or if {@code offset + length} is
        ///                                   greater than the size of {@code buffer}. </exception>
        public override int read(char[] buffer, int offset, int length) {
            verifyNotClosed();

            if (offset < 0 || offset > buffer.Length - length || length < 0)
                throw new System.IndexOutOfRangeException();

            int outstanding = length;
            while (outstanding > 0) {

                /*
                 * If there are bytes in the buffer, grab those first.
                 */
                int available = end - pos;
                if (available > 0) {
                    int count = available >= outstanding ? outstanding : available;
                    PlatformUtils.copyChars(buf, pos, buffer, offset, count);
                    pos += count;
                    offset += count;
                    outstanding -= count;
                }

                /*
                 * Before attempting to read from the underlying stream, make
                 * sure we really, really want to. We won't bother if we're
                 * done, or if we've already got some bytes and reading from the
                 * underlying stream would block.
                 * [This used to also check ready() on the underlying reader, but
                 * removed that in the JSimple implementation.
                 */
                if (outstanding == 0)
                    break;

                // assert(pos == end);

                /*
                 * If we're unmarked and the requested size is greater than our
                 * buffer, read the bytes directly into the caller's buffer. We
                 * don't read into smaller buffers because that could result in
                 * a many reads.
                 */
                if ((markPos == -1 || (pos - markPos >= markLimit)) && outstanding >= buf.Length) {
                    int count = reader.read(buffer, offset, outstanding);
                    if (count > 0) {
                        offset += count;
                        outstanding -= count;
                        markPos = -1;
                    }

                    break; // assume the source stream gave us all that it could
                }

                if (fillBuf() == -1)
                    break; // source is exhausted
            }

            int amountRead = length - outstanding;
            return (amountRead > 0 || amountRead == length) ? amountRead : -1;
        }

        /// <summary>
        /// Peeks at the next input character, refilling the buffer if necessary. If this character is a newline character
        /// ("\n"), it is discarded.
        /// </summary>
        internal void chompNewline() {
            if ((pos != end || fillBuf() != -1) && buf[pos] == '\n')
                pos++;
        }

        /// <summary>
        /// Returns the next line of text available from this reader. A line is represented by zero or more characters
        /// followed by {@code '\n'}, {@code '\r'}, {@code "\r\n"} or the end of the reader. The string does not include the
        /// newline sequence.
        /// </summary>
        /// <returns> the contents of the line or {@code null} if no characters were read before the end of the reader has been
        /// reached. </returns>
        public virtual string readLine() {
            verifyNotClosed();

            /* has the underlying stream been exhausted? */
            if (pos == end && fillBuf() == -1)
                return null;

            for (int charPos = pos; charPos < end; charPos++) {
                char ch = buf[charPos];
                if (ch > '\r')
                    continue;
                if (ch == '\n') {
                    string res = new string(buf, pos, charPos - pos);
                    pos = charPos + 1;
                    return res;
                }
                else if (ch == '\r') {
                    string res = new string(buf, pos, charPos - pos);
                    pos = charPos + 1;
                    if (((pos < end) || (fillBuf() != -1)) && (buf[pos] == '\n'))
                        pos++;
                    return res;
                }
            }

            char eol = '\0';
            StringBuilder result = new StringBuilder(80);
                /* Typical Line Length */

            result.Append(buf, pos, end - pos);
            while (true) {
                pos = end;

                /* Are there buffered characters available? */
                if (eol == '\n')
                    return result.ToString();
                // attempt to fill buffer
                if (fillBuf() == -1)
                    // characters or null.
                    return result.Length > 0 || eol != '\0' ? result.ToString() : null;
                for (int charPos = pos; charPos < end; charPos++) {
                    char c = buf[charPos];
                    if (eol == '\0') {
                        if ((c == '\n' || c == '\r'))
                            eol = c;
                    }
                    else if (eol == '\r' && c == '\n') {
                        if (charPos > pos)
                            result.Append(buf, pos, charPos - pos - 1);
                        pos = charPos + 1;
                        return result.ToString();
                    }
                    else {
                        if (charPos > pos)
                            result.Append(buf, pos, charPos - pos - 1);
                        pos = charPos;
                        return result.ToString();
                    }
                }
                if (eol == '\0')
                    result.Append(buf, pos, end - pos);
                else
                    result.Append(buf, pos, end - pos - 1);
            }

        }

        /// <summary>
        /// Resets this reader's position to the last {@code mark()} location. Invocations of {@code read()} and {@code
        /// skip()} will occur from this new location.
        /// </summary>
        /// <exception cref="IOException"> if this reader is closed or no mark has been set. </exception>
        /// <seealso cref= #mark(int) </seealso>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public void reset() throws IOException
        public virtual void reset() {
            verifyNotClosed();

            if (markPos == -1)
                throw new IOException("Attempt to call reset on BufferedReader when mark is invalid");

            pos = markPos;
        }

        /// <summary>
        /// Skips {@code amount} characters in this reader. Subsequent {@code read()}s will not return these characters
        /// unless {@code reset()} is used. Skipping characters may invalidate a mark if {@code markLimit} is surpassed.
        /// </summary>
        /// <param name="amount"> the maximum number of characters to skip. </param>
        /// <returns> the number of characters actually skipped. </returns>
        /// <exception cref="IllegalArgumentException"> if {@code amount < 0}. </exception>
        /// <exception cref="IOException">              if this reader is closed or some other I/O error occurs. </exception>
        /// <seealso cref= #mark(int) </seealso>
        /// <seealso cref= #reset() </seealso>
        public virtual long skip(long amount) {
            if (amount < 0)
                throw new System.ArgumentException();

            verifyNotClosed();
            if (amount < 1)
                return 0;

            if (end - pos >= amount) {
                pos += (int)amount;
                return amount;
            }

            long read = end - pos;
            pos = end;
            while (read < amount) {
                if (fillBuf() == -1)
                    return read;
                if (end - pos >= amount - read) {
                    pos += (int)(amount - read);
                    return amount;
                }
                // Couldn't get all the characters, skip what we read
                read += (end - pos);
                pos = end;
            }

            return amount;
        }

        private void verifyNotClosed() {
            if (Closed)
                throw new IOException("BufferedReader is closed");
        }
    }

}