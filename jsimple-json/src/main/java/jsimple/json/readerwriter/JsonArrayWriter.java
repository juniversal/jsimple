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

package jsimple.json.readerwriter;

import jsimple.json.text.Serializer;

/**
 * JsonArrayWriter lets the caller serialize the items in an array one by one, iterating through them & serializing each
 * in turn.  If the array is big--often true when it's the outermost object in a JSON text--then this method is more
 * memory efficient than creating JSON objects for the entire array in memory at once.
 * <p>
 * The array is also formatted a little differently when serialized this way, optimized for a large array that's at the
 * outermost level in a file. Here each value in the array is on a separate line, preceded by a blank line to make the
 * individual values a bit easier to grep.  The normal array serializer will sometimes put values all on one line in
 * certain simple cases, but that never happens here.
 *
 * @author Bret Johnson
 * @since 7/29/12 3:08 PM
 */
public class JsonArrayWriter extends jsimple.lang.AutoCloseable {
    Serializer serializer;
    boolean flushWhenDone = false;
    boolean singleLine = false;
    boolean outputSomething = false;

    public JsonArrayWriter(Serializer serializer) {
        this.serializer = serializer;
    }

    public JsonArrayWriter(Serializer serializer, boolean flushWhenDone) {
        this.serializer = serializer;
        this.flushWhenDone = flushWhenDone;
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    /**
     * Add a value to the array, serializing it.  value can be any valid JSON value class (JsonObject, JsonArray,
     * String, Integer, Long, Boolean, or JsonNull).
     *
     * @param value JSON value for array item
     */
    public void writeValue(Object value) {
        writeElementPrefix();
        serializer.writeValue(value);
    }

    public JsonObjectWriter writeObject() {
        writeElementPrefix();
        return new JsonObjectWriter(serializer);
    }

    public JsonArrayWriter writeArray() {
        writeElementPrefix();
        return new JsonArrayWriter(serializer);
    }

    private void writeElementPrefix() {
        if (singleLine) {
            if (!outputSomething) {
                serializer.write("[");
                outputSomething = true;
            } else serializer.write(", ");
        } else {
            if (!outputSomething) {
                serializer.write("[\n");
                serializer.addToIndent(2);
                outputSomething = true;
            } else serializer.write(",\n");

            serializer.writeIndent();
        }
    }

    /**
     * Finish the serialization, outputting the closing bracket and returning the resulting string.
     *
     * @return string containing JSON text for entire serialized array
     */
    @Override public void close() {
        if (!outputSomething)
            serializer.write("[]");
        else {
            if (singleLine) {
                serializer.write("]");
            } else {
                serializer.write("\n");
                serializer.addToIndent(-2);
                serializer.writeIndent();
                serializer.write("]");
            }
        }

        if (flushWhenDone)
            serializer.flush();
    }
}
