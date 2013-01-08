using System.Diagnostics;
using System.Text;

namespace jsimple.io
{
    /// <summary>
    ///     This class writes to debug output, via Debug.WriteLine.  It'll go to the debug window when
    ///     running in the debugger.
    /// </summary>
    public class DebugWriter : Writer
    {
        private readonly StringBuilder buffer = new StringBuilder();

        public override void close()
        {
        }

        public override void flush()
        {
        }

        public override void write(char[] buf, int offset, int count)
        {
            // Since the only thing we can write to debug output is entire lines, buffer up a line,
            // then output the buffer when we encounter a '\n' character
            for (int i = offset; i < count; ++i)
            {
                char c = buf[i];

                if (c == '\r') // Ignore carriage returns; only pay attention to newlines
                {
                }
                else if (c == '\n')
                {
                    Debug.WriteLine(buffer);
                    buffer.Length = 0;
                }
                else
                {
                    buffer.Append(c);
                }
            }
        }
    }
}