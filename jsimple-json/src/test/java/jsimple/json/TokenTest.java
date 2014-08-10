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

package jsimple.json;

import jsimple.io.StringReader;
import jsimple.json.objectmodel.JsonNull;
import jsimple.json.text.JsonParsingException;
import jsimple.json.text.Token;
import jsimple.json.text.TokenType;
import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Bret Johnson
 * @since 5/6/12 2:17 AM
 */
public class TokenTest extends UnitTest {
    @Test public void testCharacterTokens() {
        String json = "{ } [ ] , :";
        Token token = new Token(new StringReader(json));

        assertEquals(TokenType.LEFT_BRACE, token.getType());
        token.advance();

        assertEquals(TokenType.RIGHT_BRACE, token.getType());
        token.advance();

        assertEquals(TokenType.LEFT_BRACKET, token.getType());
        token.advance();

        assertEquals(TokenType.RIGHT_BRACKET, token.getType());
        token.advance();

        assertEquals(TokenType.COMMA, token.getType());
        token.advance();

        assertEquals(TokenType.COLON, token.getType());
        token.advance();

        assertEquals(TokenType.EOF, token.getType());
    }

    @Test public void testTrueFalseNullTokens() {
        String json = "true false null";
        Token token = new Token(new StringReader(json));

        assertEquals(true, token.getPrimitiveValue());
        token.advance();

        assertEquals(false, token.getPrimitiveValue());
        token.advance();

        assertEquals(JsonNull.singleton, token.getPrimitiveValue());
        token.advance();

        validateParsingException("'true'", "'tra'", "traffic sdf sdf sd  sdfs df");
        validateParsingException("'true'", "'trx'", "trx");
        validateParsingException("'true'", "'tr'", "tr");
        validateParsingException("'true'", "'tr\\r'", "tr\r");
        validateParsingException("'true'", "'tr\\t'", "tr\t");
        validateParsingException("'true'", "'tr\\n'", "tr\n");
    }

    @Test public void testStringTokens() {
        validateParsingException("'\"'", "end of JSON text", "\"abc");
        validateParsingException("'\"'", "carriage return", "\"abc\r");
        validateParsingException("'\"'", "newline", "\"abc\n");

        validateStringToken("abc", "abc");
        validateStringToken("", "");
        validateStringToken("\\", "\\\\");
        validateStringToken("\"", "\\\"");
        validateStringToken("/ \b \f \n \r \t", "\\/ \\b \\f \\n \\r \\t");
        validateStringToken("\u1234", "\\u1234");
        validateStringToken("\u0003", "\\u0003");
        validateStringToken("\uaaaa", "\\uaaaa");
        validateStringToken("\uaaaa", "\\uaaAA");
        validateStringToken("\uffff", "\\uFFfF");
        validateStringToken("\u009f", "\\u009F");
        validateStringToken("\u0000", "\\u0000");

        validateParsingException("Invalid character escape: '\\a'", "\" \\a \"");
        validateParsingException("Invalid character escape: '\\ '", "\" \\ \"");
        validateParsingException("Invalid escape character code following backslash: \\u0005", "\"abc\\\u0005\"");
        validateParsingException("Invalid escape character code following backslash: \\u001f", "\"abc\\\u001F\"");
        validateParsingException("Invalid escape character code following backslash: \\u007f", "\"abc\\\u007F\"");
        validateParsingException("Invalid character escape: '\\ '", "\"abc\\\u0020\"");
        validateParsingException("Invalid character escape: '\\\u2222'", "\"abc\\\u2222\"");
        validateParsingException("Invalid character escape: '\\\u2222'", "\"abc\\\u2222\"");
        validateParsingException("Invalid character escape: '\\\uabcd'", "\"abc\\\uaBcD\"");
    }

    private void validateStringToken(String expectedPrimitiveValue, String stringToken) {
        String json = "\"" + stringToken + "\"";
        Token token = new Token(new StringReader(json));
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    @Test public void testNumberTokens() {
        validateNumberToken(123, "123");
        validateNumberToken(123456, "123456");
        validateParsingException("Expected a digit to follow a minus sign but encountered ' '", "- ");
        validateParsingException("Expected a digit to follow a minus sign but encountered 'a'", "-a");
        validateNumberToken(0x7fffffff, "2147483647");
        validateNumberToken(0x80000000L, "2147483648");
        validateNumberToken(-0x80000000, "-2147483648");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(0, "0");
        validateNumberToken(0, "-0");
        validateNumberToken(0, "-0000");
        validateNumberToken(0, "0000");

        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(-0x80000001L, "-2147483649");

        validateNumberToken(0x7FFFFFFFFFFFFFFFL, "9223372036854775807");  // Max long
        validateNumberToken(-0x7FFFFFFFFFFFFFFFL, "-9223372036854775807");  // Min long (well, min long + 1)

        validateParsingException("Number is too big, overflowing the size of a long",
                "9223372036854775808"); // Max long + 1
        validateParsingException("Number is too big, overflowing the size of a long",
                "999999999999999999999999999999");
        validateParsingException("Negative number is too big, overflowing the size of a long",
                "-9223372036854775809");  // Min long - 1
        validateParsingException("Negative number is too big, overflowing the size of a long",
                "-999999999999999999999999999999");

        validateFloatingPointNumberToken(123.3, "123.3");
        validateFloatingPointNumberToken(-123.0, "-123.");
        validateFloatingPointNumberToken(-123.0, "-123.0");

        validateFloatingPointNumberToken(-0.1, "-0.1");
        validateFloatingPointNumberToken(-0.1, "-0.1000000");
        validateFloatingPointNumberToken(-1.0, "-1.0000");
        validateFloatingPointNumberToken(11.010, "11.010");

        validateFloatingPointNumberToken(11., "11.");
        validateParsingException("Expected a digit to follow a minus sign but encountered '.'", "-.1");

        // 17 digits is about at the limit of precision for a double--17 digit precision is preserved, 18 is not
        validateFloatingPointNumberToken(123456789012345.11, "123456789012345.11");
        validateFloatingPointNumberToken(123456789012345.111, "123456789012345.112");

        validateParsingException("Unexpected character '.' in JSON; if that character should start a string, it must be quoted", ".23");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123e5");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123E5");

        validateParsingException("Numbers in scientific notation aren't currently supported", "123.2e5");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123.E5");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123.12E5");
    }

    private void validateNumberToken(Integer expectedPrimitiveValue, String numberToken) {
        Token token = new Token(new StringReader(numberToken));
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    private void validateNumberToken(Long expectedPrimitiveValue, String numberToken) {
        Token token = new Token(new StringReader(numberToken));
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    private void validateFloatingPointNumberToken(Double expectedPrimitiveValue, String doubleToken) {
        Token token = new Token(new StringReader(doubleToken));
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    private void validateParsingException(String exceptionExpected, String exceptionActual, String tokenString) {
        validateParsingException("Expected " + exceptionExpected + " but encountered " + exceptionActual, tokenString);
    }

    private void validateParsingException(String exceptionMessage, String tokenString) {
        try {
            new Token(new StringReader(tokenString));
            fail();
        } catch (JsonParsingException e) {
            assertEquals(exceptionMessage, e.getMessage());
        }
    }

    private void assertParsingExceptionMatches(JsonParsingException e, String expectedMessage) {
        assertEquals(expectedMessage, e.getMessage());
    }
}
