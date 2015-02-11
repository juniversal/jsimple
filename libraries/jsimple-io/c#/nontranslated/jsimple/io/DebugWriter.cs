/*
 * Copyright (c) 2012-2014, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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