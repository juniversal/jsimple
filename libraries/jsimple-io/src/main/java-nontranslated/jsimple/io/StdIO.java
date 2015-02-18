/*
 * Copyright (c) 2012-2015, Microsoft Mobile
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

package jsimple.io;

/**
 * The StdIO class can be used to write to stdout & stderr and read from stdin.   It's similar to System.out, System.in,
 * and System.err but in a separate class here, instead of SystemUtils, to keep it in the jsimple-io module and avoid
 * jsimple-util depending on I/O.
 * <p/>
 * Note that unlike the standard Java System in, out, and err fields, here both Reader/Writer and Stream objects are
 * provided.   The former use UTF-8 encoding; use the Stream fields if you want a different encoding.
 *
 * @author Bret Johnson
 * @since 12/2/12 12:50 AM
 */
public class StdIO {
    public static final OutputStream outStream = new JSimpleOutputStreamOnJavaStream(System.out);
    public static final OutputStream errStream = new JSimpleOutputStreamOnJavaStream(System.err);
    public static final InputStream inStream = new JSimpleInputStreamOnJavaStream(System.in);

    public static final Writer out = new AutoFlushWriter(new Latin1OutputStreamWriter(outStream));
    public static final Writer err = new AutoFlushWriter(new Latin1OutputStreamWriter(errStream));
    public static final BufferedReader in = new BufferedReader(new Latin1InputStreamReader(inStream));
}
