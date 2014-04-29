package jsimple.json.text;

import jsimple.io.Reader;
import jsimple.json.JsonException;
import jsimple.json.objectmodel.JsonNull;


/**
 * @author Bret Johnson
 * @since 5/6/12 12:18 AM
 */
public final class Token {
    private Reader reader;
    private char[] buffer = new char[BUFFER_SIZE];
    private int bufferLength;
    private int currIndex;      // Next character to be processed
    private TokenType type;
    private Object primitiveValue;

    private static final int BUFFER_SIZE = 256;

    public Token(Reader reader) {
        this.reader = reader;
        currIndex = 0;
        bufferLength = 0;

        // Initialize to avoid warnings
        type = TokenType.PRIMITIVE;
        primitiveValue = JsonNull.singleton;

        advance();
    }

    /**
     * Return true if the particular character should be considered a control character and specified via Unicode escape
     * sequence in JSON.  The JSON spec is a bit ambiguous (as far as I can tell) on which exact Unicode characters are
     * "control characters" and must be escaped, so we have to make some reasonable assumption.  We consider control
     * characters to be the 65 characters in the ranges U+0000..U+001F and U+007F..U+009F, according to
     * http://unicode.org/glossary/#control_codes.  Note that this includes '\0', which is the end of string for us when
     * parsing.
     *
     * @param c character in question
     * @return whether specified character is a control character
     */
    public static boolean isControlCharacter(char c) {
        return c <= '\u001F' || (c >= '\u007F' && c <= '\u009F');
    }

    public TokenType getType() {
        return type;
    }

    public Object getPrimitiveValue() {
        return primitiveValue;
    }

    /**
     * Get a user friendly description of the token.  This is typically used for error messages, saying we encountered
     * such & such, which isn't what we expected.
     *
     * @return token description phrase
     */
    public String getDescription() {
        if (type == TokenType.PRIMITIVE) {
            if (primitiveValue instanceof Integer || primitiveValue instanceof Long)
                return primitiveValue.toString();
            else if (primitiveValue instanceof Double)
                return primitiveValue.toString();
            else if (primitiveValue instanceof Boolean)
                return ((boolean) (Boolean) primitiveValue) ? "true" : "false";
            else if (primitiveValue instanceof String)
                return "\"" + primitiveValue.toString() + "\"";
            else if (primitiveValue instanceof JsonNull)
                return "null";
            else throw new JsonException("Unknown token primitive type");
        } else return getTokenTypeDescription(type);
    }

    public void advance() {
        while (true) {
            char lookahead = lookaheadChar();

            switch (lookahead) {
                // Skip whitespace
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    ++currIndex;
                    continue;

                case '{':
                    ++currIndex;
                    type = TokenType.LEFT_BRACE;
                    return;

                case '}':
                    ++currIndex;
                    type = TokenType.RIGHT_BRACE;
                    return;

                case '[':
                    ++currIndex;
                    type = TokenType.LEFT_BRACKET;
                    return;

                case ']':
                    ++currIndex;
                    type = TokenType.RIGHT_BRACKET;
                    return;

                case ',':
                    ++currIndex;
                    type = TokenType.COMMA;
                    return;

                case ':':
                    ++currIndex;
                    type = TokenType.COLON;
                    return;

                case '"':
                    primitiveValue = readStringToken();
                    type = TokenType.PRIMITIVE;
                    return;

                case 't':
                    checkAndAdvancePast("true");
                    type = TokenType.PRIMITIVE;
                    primitiveValue = Boolean.TRUE;
                    return;

                case 'f':
                    checkAndAdvancePast("false");
                    type = TokenType.PRIMITIVE;
                    primitiveValue = Boolean.FALSE;
                    return;

                case 'n':
                    checkAndAdvancePast("null");
                    type = TokenType.PRIMITIVE;
                    primitiveValue = JsonNull.singleton;
                    return;

                case '\0':
                    type = TokenType.EOF;
                    return;
            }

            if (lookahead == '-' || (lookahead >= '0' && lookahead <= '9')) {
                primitiveValue = readNumberToken(lookahead);
                type = TokenType.PRIMITIVE;
                return;
            } else
                throw new JsonParsingException("Unexpected character '" + lookahead +
                        "' in JSON; if that character should start a string, it must be quoted");
        }
    }

    private void checkAndAdvancePast(String expected) {
        int length = expected.length();

        for (int i = 0; i < length; ++i) {
            char c = readChar();
            if (c != expected.charAt(i)) {
                String encountered = expected.substring(0, i);
                if (c != '\0')
                    encountered += c;
                throw new JsonParsingException(quote(expected), quote(encountered));
            }
        }
    }

    /**
     * Validate that the token is the expected type (throwing an exception if it isn't), then advance to the next
     * token.
     *
     * @param expectedType expected token type
     */
    public void checkAndAdvance(TokenType expectedType) {
        check(expectedType);
        advance();
    }

    /**
     * Validate that the token is the expected type (throwing an exception if it isn't).
     *
     * @param expectedType expected token type
     */
    public void check(TokenType expectedType) {
        if (type != expectedType)
            throw new JsonParsingException(Token.getTokenTypeDescription(expectedType), this);
    }

    private String readStringToken() {
        StringBuilder string = new StringBuilder();

        ++currIndex;   // Skip past the leading "

        while (true) {
            char c = readChar();

            if (c == '"')
                return string.toString();
            else if (c == '\\')
                string.append(readEscapedChar());
            else if (isControlCharacter(c))
                throw new JsonParsingException(charDescription('\"'), charDescription(c));
            else
                string.append(c);
        }
    }

    private char readEscapedChar() {
        char c = readChar();
        switch (c) {
            case '\"':
                return '\"';

            case '\\':
                return '\\';

            case '/':
                return '/';

            case 'b':
                return '\b';

            case 'f':
                return '\f';

            case 'n':
                return '\n';

            case 'r':
                return '\r';

            case 't':
                return '\t';

            case 'u':
                return readUnicodeChar();
        }

        if (isControlCharacter(c))
            throw new JsonParsingException("Invalid escape character code following backslash: " + charDescription(c));
        else throw new JsonParsingException("Invalid character escape: '\\" + c + "'");
    }

    private char lookaheadChar() {
        if (currIndex < bufferLength)
            return buffer[currIndex];
        else {
            int amountRead = reader.read(buffer);

            currIndex = 0;
            if (amountRead == -1 || amountRead == 0) {
                bufferLength = 0;
                return '\0';
            } else {
                bufferLength = amountRead;
                return buffer[currIndex];
            }
        }
    }

    private char readChar() {
        if (currIndex < bufferLength)
            return buffer[currIndex++];
        else {
            char c = lookaheadChar();
            ++currIndex;
            return c;
        }
    }

    private char readUnicodeChar() {
        int value = 0;
        for (int i = 0; i < 4; ++i)
            value = value * 16 + readHexDigit();

        return (char) value;
    }

    private int readHexDigit() {
        char digitChar = readChar();

        if (digitChar >= '0' && digitChar <= '9')
            return digitChar - '0';
        else if (digitChar >= 'a' && digitChar <= 'f')
            return 10 + (digitChar - 'a');
        else if (digitChar >= 'A' && digitChar <= 'F')
            return 10 + (digitChar - 'A');
        else throw new JsonParsingException("valid hex digit for unicode escape", charDescription(digitChar));
    }

    private String charDescription(char c) {
        if (c == '\0')
            return "end of JSON text";
        else if (c == '\r')
            return "carriage return";
        else if (c == '\n')
            return "newline";
        else if (c == '\t')
            return "tab";
        else if (isControlCharacter(c)) {
            int remaining = c;

            int digit4 = remaining % 16;
            remaining /= 16;

            int digit3 = remaining % 16;
            remaining /= 16;

            int digit2 = remaining % 16;
            remaining /= 16;

            int digit1 = remaining;

            StringBuilder description = new StringBuilder("\\u");
            description.append(numToHexDigitChar(digit1));
            description.append(numToHexDigitChar(digit2));
            description.append(numToHexDigitChar(digit3));
            description.append(numToHexDigitChar(digit4));
            return description.toString();
        } else return "'" + c + "'";
    }

    private String quote(String s) {
        // Escape any characters that won't print well and should be escaped
        s = s.replace("\\", "\\\\");
        s = s.replace("\r", "\\r");
        s = s.replace("\n", "\\n");
        s = s.replace("\t", "\\t");
        s = s.replace("\f", "\\f");
        s = s.replace("\b", "\\b");

        // Use single quotes as they seem a little clearer in error messages since JSON never has single quoted strings
        // in content
        return "'" + s + "'";
    }

    private char numToHexDigitChar(int num) {
        if (num >= 0 && num <= 9)
            return (char) ((int) '0' + num);
        else if (num >= 10 && num <= 15)
            return (char) ((int) 'a' + (num - 10));
        else throw new JsonException("Digit {} should be < 16 (and > 0) in call to numToHexDigitChar", num);
    }

    private Object readNumberToken(char lookahead) {
        boolean negative = false;
        if (lookahead == '-') {
            negative = true;
            readChar();  // Skip past the minus sign
            lookahead = lookaheadChar();

            if (!(lookahead >= '0' && lookahead <= '9'))
                throw new JsonParsingException("a digit to follow a minus sign", quote(Character.toString(lookahead)));
        }

        long value = 0;

        while (true) {
            lookahead = lookaheadChar();
            if (lookahead >= '0' && lookahead <= '9') {
                int digit = lookahead - '0';

                if (negative) {
                    if (-1 * value < (Long.MIN_VALUE + digit) / 10)
                        throw new JsonParsingException("Negative number is too big, overflowing the size of a long");
                } else if (value > (Long.MAX_VALUE - digit) / 10)
                    throw new JsonParsingException("Number is too big, overflowing the size of a long");

                value = 10 * value + digit;
                ++currIndex;
            } else if (lookahead == '.') {
                double doubleValue = (double) value + readFractionalPartOfDouble();
                if (negative)
                    doubleValue = -doubleValue;
                return doubleValue;
            } else if (lookahead == 'e' || lookahead == 'E')
                throw new JsonParsingException("Numbers in scientific notation aren't currently supported");
            else break;
        }

        if (negative)
            value = -1 * value;

        if (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE)
            return (Integer) ((int) value);
        else return (Long) value;
    }

    /**
     * Parse and return the fractional part of a floating point number--the decimal point and what's after it.  Note
     * that our implementation is somewhat more forgiving than the JSON standard in that "123." is treated as valid-- it
     * doesn't require a digit to follow the decimal point.   Scientific notation is currently not supported--if that's
     * ever needed, we can implement it.
     *
     * @return double representing what follows the decimal point; the returned value is >= 0 and < 1
     */
    private double readFractionalPartOfDouble() {
        if (lookaheadChar() != '.')
            throw new JsonParsingException("fraction to start with a '.'", Character.toString(lookaheadChar()));
        ++currIndex;

        double value = 0;
        double place = 0.1;

        while (true) {
            char lookahead = lookaheadChar();
            if (lookahead >= '0' && lookahead <= '9') {
                int digit = lookahead - '0';
                ++currIndex;

                value += digit * place;
                place /= 10;
            } else if (lookahead == 'e' || lookahead == 'E')
                throw new JsonParsingException("Numbers in scientific notation aren't currently supported");
            else break;
        }

        return value;
    }

    static String getTokenTypeDescription(TokenType type) {
        switch (type) {
            case LEFT_BRACE:
                return "'{'";
            case RIGHT_BRACE:
                return "'}'";
            case LEFT_BRACKET:
                return "'['";
            case RIGHT_BRACKET:
                return "']'";
            case COMMA:
                return "','";
            case COLON:
                return "':'";
            case PRIMITIVE:
                return "primitive (string/number/true/false/null)";
            case EOF:
                return "end of JSON text";
            default:
                throw new JsonException("Unknown TokenType: {}", type.toString());
        }
    }
}
