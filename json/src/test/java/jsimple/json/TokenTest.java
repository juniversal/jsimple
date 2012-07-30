package jsimple.json;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bret Johnson
 * @since 5/6/12 2:17 AM
 */
public class TokenTest {
    @Test public void testCharacterTokens() throws Exception {
        String json = "{ } [ ] , :";
        Token token = new Token(json);

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

    @Test public void testTrueFalseNullTokens() throws Exception {
        String json = "true false null";
        Token token = new Token(json);

        assertEquals(true, token.getPrimitiveValue());
        token.advance();

        assertEquals(false, token.getPrimitiveValue());
        token.advance();

        assertEquals(JsonNull.singleton, token.getPrimitiveValue());
        token.advance();

        validateParsingException("'true'", "'traf'", "traffic sdf sdf sd  sdfs df");
        validateParsingException("'true'", "'trx'", "trx");
        validateParsingException("'true'", "'tr'", "tr");
        validateParsingException("'true'", "'tr\\r'", "tr\r");
        validateParsingException("'true'", "'tr\\t'", "tr\t");
        validateParsingException("'true'", "'tr\\n'", "tr\n");
    }

    @Test public void testStringTokens() throws Exception {
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
        Token token = new Token("\"" + stringToken + "\"");
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    @Test public void testNumberTokens() throws Exception {
        validateNumberToken(123, "123");
        validateNumberToken(123456, "123456");
        validateParsingException("Expected a digit to follow a minus sign", "- ");
        validateNumberToken(0x7fffffff, "2147483647");
        validateNumberToken(0x80000000L, "2147483648");
        validateNumberToken(-0x80000000, "-2147483648");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(0x80000000, "-2147483648");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(0, "0");
        validateNumberToken(0, "-0");
        validateNumberToken(0, "-0000");
        validateNumberToken(0, "0000");

        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(-0x80000001L, "-2147483649");
        validateNumberToken(-0x80000001L, "-2147483649");

        validateNumberToken(0x7FFFFFFFFFFFFFFFL, "9223372036854775807");  // Max long
        validateNumberToken(-0x8000000000000000L, "-9223372036854775808");  // Min long

        validateParsingException("Number is too big, overflowing the size of a long",
                "9223372036854775808"); // Max long + 1
        validateParsingException("Number is too big, overflowing the size of a long",
                "999999999999999999999999999999");
        validateParsingException("Negative number is too big, overflowing the size of a long",
                "-9223372036854775809");  // Min long - 1
        validateParsingException("Negative number is too big, overflowing the size of a long",
                "-999999999999999999999999999999");

        validateParsingException("Floating point numbers aren't currently supported", "123.45");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123e5");
        validateParsingException("Numbers in scientific notation aren't currently supported", "123E5");
    }

    private void validateNumberToken(Integer expectedPrimitiveValue, String numberToken) {
        Token token = new Token(numberToken);
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    private void validateNumberToken(Long expectedPrimitiveValue, String numberToken) {
        Token token = new Token(numberToken);
        assertEquals(expectedPrimitiveValue, token.getPrimitiveValue());
    }

    private void validateParsingException(String exceptionExpected, String exceptionActual, String tokenString) {
        validateParsingException("Expected " + exceptionExpected + " but encountered " + exceptionActual, tokenString);
    }

    private void validateParsingException(String exceptionMessage, String tokenString) {
        try {
            new Token(tokenString);
            fail();
        } catch (JsonParsingException e) {
            assertEquals(exceptionMessage, e.getMessage());
        }
    }

    private void assertParsingExceptionMatches(JsonParsingException e, String expectedMessage) {
        assertEquals(expectedMessage, e.getMessage());
    }
}
