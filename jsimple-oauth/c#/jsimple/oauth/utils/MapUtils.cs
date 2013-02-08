using System.Collections.Generic;
using System.Text;

namespace jsimple.oauth.utils
{


	/// <summary>
	/// @author: Pablo Fernandez
	/// </summary>
	public class MapUtils
	{
		public static string ToString<K, V>(IDictionary<K, V> map)
		{
			if (map == null)
				return "";
			if (map.Count == 0)
				return "{}";

			StringBuilder result = new StringBuilder();
			result.Append("{");

			bool addedSomething = false;
			foreach (KeyValuePair<K, V> entry in map)
			{
				if (addedSomething)
					result.Append(", ");
				else
					addedSomething = true;

				result.Append(entry.Key.ToString());
				result.Append(" -> ");
				result.Append(entry.Value.ToString());

				result.Append(string.Format(", {0} -> {1} ", entry.Key.ToString(), entry.Value.ToString()));
			}

			result.Append("}");

			return result.ToString();
		}
	}

}