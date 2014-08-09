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

package jsimple.json.readerwriter;

import jsimple.json.text.Serializer;

/**
 * @author Bret Johnson
 * @since 5/21/13 11:10 PM
 */
public class JsonObjectWriter extends jsimple.lang.AutoCloseable {
    Serializer serializer;
    boolean flushWhenDone = false;
    boolean singleLine = false;
    boolean outputSomething = false;

    public JsonObjectWriter(Serializer serializer) {
        this.serializer = serializer;
    }

    public JsonObjectWriter(Serializer serializer, boolean flushWhenDone) {
        this.serializer = serializer;
        this.flushWhenDone = flushWhenDone;
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    public void writeProperty(JsonProperty property, Object value) {
        writePropertyName(property.getName());
        serializer.writeValue(value);
    }

    public void writeProeprty(String propertyName, Object value) {
        writePropertyName(propertyName);
        serializer.writeValue(value);
    }

    public void writePropertyName(String propertyName) {
        if (singleLine) {
            if (!outputSomething) {
                serializer.write("{ ");
                outputSomething = true;
            } else serializer.write(", ");

            serializer.writeString(propertyName);
            serializer.write(": ");
        } else {
            if (!outputSomething) {
                serializer.write("{\n");
                serializer.indent(2);
                outputSomething = true;
            } else serializer.write(",\n");

            serializer.writeIndent();
            serializer.writeString(propertyName);
            serializer.write(": ");
        }
    }

    public JsonObjectWriter writeObjectProperty(JsonObjectProperty property) {
        writePropertyName(property.getName());
        return new JsonObjectWriter(serializer);
    }

    public JsonArrayWriter writeArrayProperty(JsonArrayProperty property) {
        writePropertyName(property.getName());
        return new JsonArrayWriter(serializer);
    }

    @Override public void close() {
        if (!outputSomething)
            serializer.write("{}");
        else {
            if (singleLine) {
                serializer.write(" }");
            } else {
                serializer.write("\n");
                serializer.indent(-2);
                serializer.writeIndent();
                serializer.write("}");
            }
        }

        if (flushWhenDone)
            serializer.flush();
    }
}
