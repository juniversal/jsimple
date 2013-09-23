using System.Collections.Generic;

namespace jsimple.json.objectmodel
{


	/// <summary>
	/// A JsonObject represents an ordered list of name/value pairs.  Note that unlike a HashMap, the list is ordered, so
	/// that the order in the serialized text can be controlled.  For now, queries just do a linear search, but we might create
	/// a HashMap later, say if there are > 5 items in the object, for performance.  There's currently no prohibition against
	/// a name being duplicated in the object; if that happens the value of the first occurrence is returned currently but
	/// future implementations may change that.
	/// 
	/// @author Bret Johnson
	/// @since 5/6/12 12:29 AM
	/// </summary>
	public sealed class JsonObject : JsonObjectOrArray
	{
		private List<string> names = new List<string>();
		private List<object> values = new List<object>();

		/// <summary>
		/// Get number of name/value pairs in the object.
		/// </summary>
		/// <returns> number of name/value pairs </returns>
		public int size()
		{
			return names.Count;
		}

		/// <summary>
		/// Get name at the specified index.
		/// </summary>
		/// <param name="index"> index in question; must be < size() </param>
		/// <returns> name at that index </returns>
		public string getName(int index)
		{
			return names[index];
		}

		/// <summary>
		/// Get value at the specified index.
		/// </summary>
		/// <param name="index"> index in question; must be < size() </param>
		/// <returns> value at that index </returns>
		public object getValue(int index)
		{
			return values[index];
		}

		/// <summary>
		/// Return the value of the specified key.  If the key isn't present, an exception is thrown.  If the key is present
		/// and explicitly has the value null, then the JsonNull singleton is returned. Technically, nothing prevents a key
		/// being duplicated in a JSON file; if that happens, the value of the first occurrence is returned here.
		/// </summary>
		/// <param name="name"> key name </param>
		/// <returns> value of key </returns>
		public object get(string name)
		{
			int length = names.Count;

			for (int i = 0; i < length; ++i)
			{
				if (names[i].Equals(name))
					return values[i];
			}

			throw new JsonException("JSON object is expected to have a value for {} but doesn't", name);
		}

		/// <summary>
		/// Return the value of the specified key.  If the key isn't present, null is returned.  If the key is present and
		/// explicitly has the value null, then the JsonNull singleton is returned. Technically, nothing prevents a key being
		/// duplicated in a JSON file; if that happens, the value of the first occurrence is returned here.
		/// </summary>
		/// <param name="name"> key name </param>
		/// <returns> value of key </returns>
		public object getOrNull(string name)
		{
			int length = names.Count;

			for (int i = 0; i < length; ++i)
			{
				if (names[i].Equals(name))
					return values[i];
			}

			return null;
		}

		public bool getBoolean(string name)
		{
			return (bool)(bool?) get(name);
		}

		public bool? getBooleanOrNull(string name)
		{
			return (bool?) getOrNull(name);
		}

		public bool getBooleanOrDefault(string name, bool defaultValue)
		{
			bool? value = getBooleanOrNull(name);
			return value == null ? defaultValue : (bool) value;
		}

		public string getString(string name)
		{
			return (string) get(name);
		}

		public string getStringOrNull(string name)
		{
			return (string) getOrNull(name);
		}

		public string getStringOrDefault(string name, string defaultValue)
		{
			string value = getStringOrNull(name);
			return value == null ? defaultValue : (string) value;
		}

		public int getInt(string name)
		{
			return (int)(int?) get(name);
		}

		public int? getIntOrNull(string name)
		{
			return (int?) getOrNull(name);
		}

		public int getIntOrDefault(string name, int defaultValue)
		{
			int? value = getIntOrNull(name);
			return value == null ? defaultValue : (int) value;
		}

		public long getLong(string name)
		{
			object value = get(name);
			if (value is int?)
				return (long)(int?) value;
			else
				return (long)(long?) value;
		}

		public long? getLongOrNull(string name)
		{
			object value = get(name);
			if (value == null)
				return null;
			else if (value is int?)
				return (long)(int?) value;
			else
				return (long?) value;
		}

		public long getLongOrDefault(string name, long defaultValue)
		{
			long? value = getLongOrNull(name);
			return value == null ? defaultValue : (long) value;
		}

		public JsonObject getJsonObject(string name)
		{
			return (JsonObject) get(name);
		}

		public JsonObject getJsonObjectOrNull(string name)
		{
			return (JsonObject) getOrNull(name);
		}

		public JsonArray getJsonArray(string name)
		{
			return (JsonArray) get(name);
		}

		public JsonArray getJsonArrayOrNull(string name)
		{
			return (JsonArray) getOrNull(name);
		}

		/// <summary>
		/// Return true if the object contains the specified key, false otherwise.  Like a Java HashMap, calling containsKey
		/// is the only way to distinguish between a key not being present and it being present but explicitly set to null in
		/// the JSON as the getOrNull method returns null in both cases here.
		/// </summary>
		/// <param name="keyName"> key keyName </param>
		/// <returns> true iff the object contains the key (even if its value is null) </returns>
		public bool containsKey(string keyName)
		{
			foreach (string name in names)
			{
				if (name.Equals(keyName))
					return true;
			}
			return false;
		}

		public JsonObject add(string name, object value)
		{
			names.Add(name);
			values.Add(value);
			return this;
		}

		public JsonObject addChildObject(string name)
		{
			JsonObject childObject = new JsonObject();
			names.Add(name);
			values.Add(childObject);
			return childObject;
		}

		public JsonArray addChildArray(string name)
		{
			JsonArray childArray = new JsonArray();
			names.Add(name);
			values.Add(childArray);
			return childArray;
		}
	}

}