namespace jsimple.json.readerwriter {

    using Serializer = jsimple.json.text.Serializer;

    /// <summary>
    /// JsonArrayWriter lets the caller serialize the items in an array one by one, iterating through them & serializing each
    /// in turn.  If the array is big--often true when it's the outermost object in a JSON text--then this method is more
    /// memory efficient than creating JSON objects for the entire array in memory at once.
    /// <p/>
    /// The array is also formatted a little differently when serialized this way, optimized for a large array that's at the
    /// outermost level in a file. Here each value in the array is on a separate line, preceded by a blank line to make the
    /// individual values a bit easier to grep.  The normal array serializer will sometimes put values all on one line in
    /// certain simple cases, but that never happens here.
    /// 
    /// @author Bret Johnson
    /// @since 7/29/12 3:08 PM
    /// </summary>
    public class JsonArrayWriter : jsimple.lang.AutoCloseable {
        internal Serializer serializer;
        internal bool flushWhenDone = false;
        internal bool singleLine = false;
        internal bool outputSomething = false;

        public JsonArrayWriter(Serializer serializer) {
            this.serializer = serializer;
        }

        public JsonArrayWriter(Serializer serializer, bool flushWhenDone) {
            this.serializer = serializer;
            this.flushWhenDone = flushWhenDone;
        }

        public virtual bool SingleLine {
            get {
                return singleLine;
            }
            set {
                this.singleLine = value;
            }
        }


        /// <summary>
        /// Add a value to the array, serializing it.  value can be any valid JSON value class (JsonObject, JsonArray,
        /// String, Integer, Long, Boolean, or JsonNull).
        /// </summary>
        /// <param name="value"> JSON value for array item </param>
        public virtual void writeValue(object value) {
            writeElementPrefix();
            serializer.writeValue(value);
        }

        public virtual JsonObjectWriter writeObject() {
            writeElementPrefix();
            return new JsonObjectWriter(serializer);
        }

        public virtual JsonArrayWriter writeArray() {
            writeElementPrefix();
            return new JsonArrayWriter(serializer);
        }

        private void writeElementPrefix() {
            if (singleLine) {
                if (!outputSomething) {
                    serializer.write("[");
                    outputSomething = true;
                }
                else
                    serializer.write(", ");
            }
            else {
                if (!outputSomething) {
                    serializer.write("[\n");
                    serializer.indent(2);
                    outputSomething = true;
                }
                else
                    serializer.write(",\n");

                serializer.writeIndent();
            }
        }

        /// <summary>
        /// Finish the serialization, outputting the closing bracket and returning the resulting string.
        /// </summary>
        /// <returns> string containing JSON text for entire serialized array </returns>
        public override void close() {
            if (!outputSomething)
                serializer.write("[]");
            else {
                if (singleLine)
                    serializer.write("]");
                else {
                    serializer.write("\n");
                    serializer.indent(-2);
                    serializer.writeIndent();
                    serializer.write("]");
                }
            }

            if (flushWhenDone)
                serializer.flush();
        }
    }

}