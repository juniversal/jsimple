using System;
using System.Text;

namespace jsimple.util {



    /// <summary>
    /// @author Bret Johnson
    /// @since 11/25/12 4:00 PM
    /// </summary>
    public class CharIterator {
        private string str;
        private int index = 0;
        private int length;

        public CharIterator(string @string) {
            this.str = @string;
            this.length = @string.Length;
        }

        public virtual string String {
            get {
                return str;
            }
        }

        public virtual string Remaining {
            get {
                return str.Substring(index);
            }
        }

        public virtual int Index {
            get {
                return index;
            }
        }

        public virtual bool atEnd() {
            return index >= length;
        }

        /// <returns> current character or '\0' if at end of str </returns>
        public virtual char curr() {
            if (index >= length)
                return '\0';
            else
                return str[index];
        }

        /// <summary>
        /// Return the current character (same as curr()) then advance to the next one.
        /// </summary>
        /// <returns> current character or -1 if at end of str </returns>
        public virtual char read() {
            char c = curr();
            advance();
            return c;
        }

        /// <summary>
        /// Advance the iterator to the next character.  If already at the end of the str, this method is a no-op.
        /// </summary>
        public virtual void advance() {
            if (index < length)
                ++index;
        }

        private void advance(int charCount) {
            index += charCount;
            if (index > length)
                index = length;
        }

        public virtual bool match(char value) {
            if (value != curr())
                return false;
            advance();
            return true;
        }

        public virtual bool match(char value, StringBuilder buffer) {
            if (value != curr())
                return false;
            buffer.Append(read());
            return true;
        }

        public virtual bool match(string substring) {
            if (Remaining.StartsWith(substring)) {
                advance(substring.Length);
                return true;
            }
            return false;
        }

        public virtual double? matchDouble() {
            StringBuilder buffer = new StringBuilder();

            int savedIndex = index;
            match('-', buffer);
            if (!matchDigits(buffer)) {
                index = savedIndex;
                return null;
            }

            if (match('.', buffer))
                matchDigits(buffer);

            return Convert.ToDouble(buffer.ToString());
        }

        public virtual bool matchDigits(StringBuilder buffer) {
            if (!matchDigit(buffer))
                return false;
            while (matchDigit(buffer))
                ;
            return true;
        }

        public virtual bool matchCharRange(int minChar, int maxChar, StringBuilder buffer) {
            int lookahead = curr();
            if (lookahead < minChar || lookahead > maxChar)
                return false;

            buffer.Append(read());
            return true;
        }

        public virtual bool matchDigit(StringBuilder buffer) {
            return matchCharRange('0', '9', buffer);
        }

        /// <summary>
        /// Search for the the next occurrence of the specified substring & advance the iterator just past it.  If that
        /// substring isn't found, an exception is thrown.
        /// </summary>
        /// <param name="substr"> substring to search for </param>
        public virtual void skipAheadPast(string substr) {
            if (!skipAheadPastIfExists(substr))
                throw new InvalidFormatException("'{}' not found in string '{}'", substr, str);
        }

        /// <summary>
        /// Search for the the next occurrence of the specified substring & advance the iterator just past it.  Return true
        /// if the substring is found, false if it isn't.
        /// </summary>
        /// <param name="substr"> substring to search for </param>
        /// <returns> true if the substring is found </returns>
        public virtual bool skipAheadPastIfExists(string substr) {
            int substringIndex = str.IndexOf(substr, index);
            if (substringIndex == -1)
                return false;

            index = substringIndex + substr.Length;
            return true;
        }

        /// <summary>
        /// Verify that the current character is c, throwing an exception if it isn't, then advance to the next character.
        /// </summary>
        /// <param name="c"> character to verify that occurs at the current iterator position </param>
        public virtual void checkAndAdvance(char c) {
            if (atEnd())
                throw new InvalidFormatException("Already at end of string");

            if (str[index] != c)
                throw new InvalidFormatException("Character '{}' expected but not found at position '{}'", c, CurrentPositionWithContext);

            ++index;
        }

        private string CurrentPositionWithContext {
            get {
                int start = index - 50;
                if (start < 0)
                    start = 0;
    
                int length = str.Length;
                int end = index + 50;
                if (end > length)
                    end = length;
    
                StringBuilder markedUpText = new StringBuilder();
    
                if (start > 0)
                    markedUpText.Append("...");
    
                markedUpText.Append(str.Substring(start, index - start));
                markedUpText.Append("[^^^^]");
                markedUpText.Append(str.Substring(index, end - index));
    
                if (end < length)
                    markedUpText.Append("...");
    
                return markedUpText.ToString();
            }
        }

        public virtual bool LineBreak {
            get {
                int c = curr();
                return c == '\r' || c == '\n';
            }
        }

        /// <returns> true if the current character is a whitespace character. </returns>
        public virtual bool Whitespace {
            get {
                int c = curr();
                return c == ' ' || c == '\t' || c == '\r' || c == '\n';
            }
        }

        public virtual bool Digit {
            get {
                int c = curr();
                return c >= '0' && c <= '9';
            }
        }

        public virtual bool AsciiLetter {
            get {
                int c = curr();
                return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
            }
        }

        public virtual bool WhitespaceOnSameLine {
            get {
                int c = curr();
                return c == ' ' || c == '\t';
            }
        }

        /// <summary>
        /// Advance past any whitespace characters in the str, leaving the iterator at the first non-whitespace character
        /// following.
        /// </summary>
        public virtual void advancePastWhitespace() {
            while (Whitespace)
                advance();
        }

        public virtual void advancePastWhitespaceOnSameLine() {
            while (WhitespaceOnSameLine)
                advance();
        }

        public virtual string readWhitespaceDelimitedToken() {
            StringBuilder token = new StringBuilder();
            while (!Whitespace && !atEnd())
                token.Append(read());
            return token.ToString();
        }

        public virtual string readWhitespaceDelimitedTokenOnSameLine() {
            StringBuilder token = new StringBuilder();
            while (!WhitespaceOnSameLine && !LineBreak && !atEnd())
                token.Append(read());
            return token.ToString();
        }
    }

}