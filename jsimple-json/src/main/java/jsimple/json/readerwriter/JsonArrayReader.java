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

import jsimple.json.objectmodel.JsonNull;
import jsimple.json.text.JsonParsingException;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;

/**
 * JsonArrayReader lets the caller parse the items in an array one by one, iterating through them & processing them as
 * they're parsed.  If the array is very big--often when it's the outermost object in a JSON text--then this method is
 * more memory efficient than parsing the whole array into memory at once.
 *
 * @author Bret Johnson
 * @since 7/28/12 11:09 PM
 */
public class JsonArrayReader {
    protected Token token;
    private boolean atBeginning = true;
    private boolean atEnd = false;

    public JsonArrayReader(Token token) {
        this.token = token;
        token.checkAndAdvance(TokenType.LEFT_BRACKET);
    }

    protected void readElementPrefix() {
        if (atBeginning)
            atBeginning = false;
        else token.checkAndAdvance(TokenType.COMMA);
    }

    public boolean atEnd() {
        if (!atEnd && token.getType() == TokenType.RIGHT_BRACKET) {
            token.advance();
            atEnd = true;
        }

        return atEnd;
    }

    public Object readPrimitive() {
        readElementPrefix();

        Object value = token.getPrimitiveValue();
        if (value == JsonNull.singleton)
            throw new JsonParsingException("non-null value", token);

        token.advance();
        return value;
    }

    public boolean readBoolean() {
        return (boolean) (Boolean) readPrimitive();
    }

    public String readString() {
        return (String) readPrimitive();
    }

    public int readInt() {
        return (int) (Integer) readPrimitive();
    }

    // TODO: Automatically convert int to long
    public long readLong() {
        return (long) (Long) readPrimitive();
    }

    public double readDouble() {
        return (double) (Double) readPrimitive();
    }

    public JsonObjectReader readObject() {
        readElementPrefix();

        if (token.getType() != TokenType.LEFT_BRACE)
            throw new JsonParsingException("object as the value, starting with {", token);

        return new JsonObjectReader(token);
    }

    public JsonArrayReader readArray() {
        readElementPrefix();

        if (token.getType() != TokenType.LEFT_BRACKET)
            throw new JsonParsingException("array as the value, starting with [", token);

        return new JsonArrayReader(token);
    }
}
