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
 * This is a platform independent class used to represent file not found exceptions, taking the place of
 * java.io.FileNotFoundException in JSimple based code.
 *
 * @author Bret Johnson
 * @since 10/9/12 12:15 PM
 */
public class PathNotFoundException extends IOException {
    public PathNotFoundException(Throwable cause) {
        super(cause);
    }

    public PathNotFoundException(String message) {
        super(message);
    }

    public PathNotFoundException(String message, Object arg1) {
        super(message, arg1);
    }

    public PathNotFoundException(String message, Object arg1, Object arg2) {
        super(message, arg1, arg2);
    }
}
