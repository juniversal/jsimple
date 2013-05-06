using System.Diagnostics;
using System.Collections.Generic;
using System.Text;

namespace jsimple.oauth.model
{

	using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;
	using PlatformUtils = jsimple.util.PlatformUtils;


	/// <summary>
	/// @author: Pablo Fernandez
	/// </summary>
	public class ParameterList
	{
		private const char QUERY_STRING_SEPARATOR_CHAR = '?';
		private const string QUERY_STRING_SEPARATOR = "?";
		private const string PARAM_SEPARATOR = "&";
		private const string PAIR_SEPARATOR = "=";
		private const string EMPTY_STRING = "";

		private readonly List<Parameter> parameters;

		public ParameterList()
		{
			parameters = new List<Parameter>();
		}

		internal ParameterList(IList<Parameter> parameters)
		{
			this.parameters = new List<Parameter>(parameters);
		}

		public ParameterList(IDictionary<string, string> map) : this()
		{
			foreach (KeyValuePair<string, string> entry in map)
				parameters.Add(new Parameter(entry.Key, entry.Value));
		}

		public virtual void add(string key, string value)
		{
			parameters.Add(new Parameter(key, value));
		}

		public virtual string appendTo(string url)
		{
			Debug.Assert(url != null, "Cannot append to null URL");

			string queryString = asFormUrlEncodedString();
			if (queryString.Equals(EMPTY_STRING))
				return url;
			else
			{
				url += url.IndexOf(QUERY_STRING_SEPARATOR_CHAR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
				url += queryString;
				return url;
			}
		}

		public virtual string asOauthBaseString()
		{
			return OAuthEncoder.encode(asFormUrlEncodedString());
		}

		public virtual string asFormUrlEncodedString()
		{
			if (parameters.Count == 0)
				return EMPTY_STRING;

			StringBuilder builder = new StringBuilder();
			foreach (Parameter p in parameters)
			{
				builder.Append('&');
				builder.Append(p.asUrlEncodedPair());
			}

			return builder.ToString().Substring(1);
		}

		public virtual void addAll(ParameterList other)
		{
			parameters.AddRange(other.parameters);
		}

		public virtual void addQueryString(string queryString)
		{
			if (queryString != null && queryString.Length > 0)
			{
				foreach (string param in queryString.Split(PARAM_SEPARATOR, true))
				{
					string[] pair = param.Split(PAIR_SEPARATOR, true);
					string key = OAuthEncoder.decode(pair[0]);
					string value = pair.Length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
					parameters.Add(new Parameter(key, value));
				}
			}
		}

		public virtual bool contains(Parameter param)
		{
			return parameters.Contains(param);
		}

		public virtual int size()
		{
			return parameters.Count;
		}

		public virtual ParameterList sort()
		{
			ParameterList sorted = new ParameterList(parameters);
			// TODO (Bret): This doesn't seem exactly right--the OAuth spec says that parameters should be sorted in byte order
			PlatformUtils.sortList(sorted.parameters);
			return sorted;
		}
	}

}