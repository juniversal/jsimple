namespace jsimple.json {

    using StringReader = jsimple.io.StringReader;
    using JsonNull = jsimple.json.objectmodel.JsonNull;
    using JsonParsingException = jsimple.json.text.JsonParsingException;
    using Token = jsimple.json.text.Token;
    using TokenType = jsimple.json.text.TokenType;
    using UnitTest = jsimple.unit.UnitTest;
    using NUnit.Framework;

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/6/12 2:17 AM
    /// </summary>
    public class TokenTest : UnitTest {
        [Test] public virtual void testCharacterTokens()
        {
            string json = "{ } [ ] , :";
            Token token = new Token(new StringReader(json));

            assertEquals(TokenType.LEFT_BRACE, token.Type);
            token.advance();

            assertEquals(TokenType.RIGHT_BRACE, token.Type);
            token.advance();

            assertEquals(TokenType.LEFT_BRACKET, token.Type);
            token.advance();

            assertEquals(TokenType.RIGHT_BRACKET, token.Type);
            token.advance();

            assertEquals(TokenType.COMMA, token.Type);
            token.advance();

            assertEquals(TokenType.COLON, token.Type);
            token.advance();

            assertEquals(TokenType.EOF, token.Type);
        }

        [Test] public virtual void testTrueFalseNullTokens()
        {
            string json = "true false null";
            Token token = new Token(new StringReader(json));

            assertEquals(true, token.PrimitiveValue);
            token.advance();

            assertEquals(false, token.PrimitiveValue);
            token.advance();

            assertEquals(JsonNull.singleton, token.PrimitiveValue);
            token.advance();

            validateParsingException("'true'", "'tra'", "traffic sdf sdf sd  sdfs df");
            validateParsingException("'true'", "'trx'", "trx");
            validateParsingException("'true'", "'tr'", "tr");
            validateParsingException("'true'", "'tr\\r'", "tr\r");
            validateParsingException("'true'", "'tr\\t'", "tr\t");
            validateParsingException("'true'", "'tr\\n'", "tr\n");
        }

        [Test] public virtual void testStringTokens()
        {
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

        private void validateStringToken(string expectedPrimitiveValue, string stringToken) {
            string json = "\"" + stringToken + "\"";
            Token token = new Token(new StringReader(json));
            assertEquals(expectedPrimitiveValue, token.PrimitiveValue);
        }

        [Test] public virtual void testNumberTokens()
        {
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

            validateNumberToken(0x7FFFFFFFFFFFFFFFL, "9223372036854775807"); // Max long
            validateNumberToken(-0x7FFFFFFFFFFFFFFFL, "-9223372036854775807"); // Min long (well, min long + 1)

            validateParsingException("Number is too big, overflowing the size of a long", "9223372036854775808"); // Max long + 1
            validateParsingException("Number is too big, overflowing the size of a long", "999999999999999999999999999999");
            validateParsingException("Negative number is too big, overflowing the size of a long", "-9223372036854775809"); // Min long - 1
            validateParsingException("Negative number is too big, overflowing the size of a long", "-999999999999999999999999999999");

            validateFloatingPointNumberToken(123.3, "123.3");
            validateFloatingPointNumberToken(-123.0, "-123.");
            validateFloatingPointNumberToken(-123.0, "-123.0");

            validateFloatingPointNumberToken(-0.1, "-0.1");
            validateFloatingPointNumberToken(-0.1, "-0.1000000");
            validateFloatingPointNumberToken(-1.0, "-1.0000");
            validateFloatingPointNumberToken(11.010, "11.010");

            validateFloatingPointNumberToken(11.0, "11.");
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

        private void validateNumberToken(int? expectedPrimitiveValue, string numberToken) {
            Token token = new Token(new StringReader(numberToken));
            assertEquals(expectedPrimitiveValue, token.PrimitiveValue);
        }

        private void validateNumberToken(long? expectedPrimitiveValue, string numberToken) {
            Token token = new Token(new StringReader(numberToken));
            assertEquals(expectedPrimitiveValue, token.PrimitiveValue);
        }

        private void validateFloatingPointNumberToken(double? expectedPrimitiveValue, string doubleToken) {
            Token token = new Token(new StringReader(doubleToken));
            assertEquals(expectedPrimitiveValue, token.PrimitiveValue);
        }

        private void validateParsingException(string exceptionExpected, string exceptionActual, string tokenString) {
            validateParsingException("Expected " + exceptionExpected + " but encountered " + exceptionActual, tokenString);
        }

        private void validateParsingException(string exceptionMessage, string tokenString) {
            try {
                new Token(new StringReader(tokenString));
                fail();
            }
            catch (JsonParsingException e) {
                assertEquals(exceptionMessage, e.Message);
            }
        }

        private void assertParsingExceptionMatches(JsonParsingException e, string expectedMessage) {
            assertEquals(expectedMessage, e.Message);
        }
    }

}