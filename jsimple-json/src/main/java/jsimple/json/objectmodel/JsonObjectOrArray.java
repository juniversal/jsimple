/*
    Copyright (c) 2012-2014, Microsoft Mobile

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/

package jsimple.json.objectmodel;

import jsimple.io.ByteArrayOutputStream;
import jsimple.io.StringWriter;
import jsimple.io.Utf8OutputStreamWriter;
import jsimple.io.Writer;
import jsimple.json.text.Serializer;
import jsimple.util.ByteArrayRange;

/**
 * This class is simply used to represent, in a type safe way, either a JSON object or an array.  According to the spec
 * a JSON text, at the highest level, is allowed to be either a JSON object or array, so this class captures that.
 *
 * @author Bret Johnson
 * @since 7/8/12 1:56 PM
 */
abstract public class JsonObjectOrArray {
    public void write(Writer writer) {
        Serializer serializer = new Serializer(writer);
        serializer.writeValue(this);
        serializer.write("\n");    // Terminate the last line
        serializer.flush();
    }

    @Override public String toString() {
        StringWriter stringWriter = new StringWriter();
        write(stringWriter);
        return stringWriter.toString();
    }

    public ByteArrayRange toUtf8Bytes() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (Utf8OutputStreamWriter writer = new Utf8OutputStreamWriter(byteArrayOutputStream)) {
                write(writer);
                return byteArrayOutputStream.closeAndGetByteArray();
            }
        }
    }
}
