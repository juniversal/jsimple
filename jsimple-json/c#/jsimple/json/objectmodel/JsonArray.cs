using System.Collections.Generic;

namespace jsimple.json.objectmodel {

    /// <summary>
    /// @author Bret Johnson
    /// @since 5/7/12 12:40 AM
    /// </summary>
    public sealed class JsonArray : JsonObjectOrArray {
        private List<object> values = new List<object>();

        /// <summary>
        /// Return the number of values in the array.
        /// </summary>
        /// <returns> count of values in the array </returns>
        public int size() {
            return values.Count;
        }

        /// <summary>
        /// Return the value at the specified index.  If the index is out of range, an exception is thrown.
        /// </summary>
        /// <param name="index"> index of item in array </param>
        /// <returns> value at specified index </returns>
        public object get(int index) {
            return values[index];
        }

        public bool getBoolean(int index) {
            return (bool)(bool?) get(index);
        }

        public string getString(int index) {
            return (string) get(index);
        }

        public int getInt(int index) {
            return (int)(int?) get(index);
        }

        public long getLong(int index) {
            return (long)(long?) get(index);
        }

        public double getDouble(int index) {
            return (double)(double?) get(index);
        }

        public JsonObject getJsonObject(int index) {
            return (JsonObject) get(index);
        }

        public JsonArray getJsonArray(int index) {
            return (JsonArray) get(index);
        }

        /// <summary>
        /// Add the specified value to the array, at the end.
        /// </summary>
        /// <param name="value"> value to add </param>
        public JsonArray add(object value) {
            values.Add(value);
            return this;
        }
    }

}