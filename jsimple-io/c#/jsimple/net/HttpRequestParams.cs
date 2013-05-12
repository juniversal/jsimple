using System.Collections.Generic;

namespace jsimple.net
{

	using BasicException = jsimple.util.BasicException;



	/// <summary>
	/// HttpRequestParams represents a set of string name/value pairs which can (eventually) be passed as URL or form
	/// parameters for an HTTP request.  The entries are ordered--the order that they are added matches the order they
	/// are returned from getNames.  The same name can't be added twice.
	/// 
	/// @author Bret Johnson
	/// @since 12/23/12 1:54 AM
	/// </summary>
	public class HttpRequestParams
	{
		private List<string> names = new List<string>();
		private Dictionary<string, string> values = new Dictionary<string, string>();

		public virtual HttpRequestParams add(string name, string value)
		{
			if (values.ContainsKey(name))
				throw new BasicException("Parameter {} already added", name);

			names.Add(name);
			values[name] = value;
			return this;
		}

		public virtual string getValueOrNull(string name)
		{
			return values.GetValueOrNull(name);
		}

		public virtual string getValue(string name)
		{
			string value = values.GetValueOrNull(name);
			if (value == null)
				throw new BasicException("Value {} not present in HTTPRequestParams", name);
			return value;
		}

		public virtual IList<string> Names
		{
			get
			{
				return names;
			}
		}
	}

}