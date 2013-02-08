using System.Collections.Generic;

namespace jsimple.oauth.model
{


	/// <summary>
	/// The representation of an OAuth HttpRequest.
	/// <p/>
	/// Adds OAuth-related functionality to the <seealso cref="Request"/>
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class OAuthRequest : Request
	{
		private const string OAUTH_PREFIX = "oauth_";
		private IDictionary<string, string> oauthParameters;

		/// <summary>
		/// Default constructor.
		/// </summary>
		/// <param name="verb"> Http verb/method </param>
		/// <param name="url">  resource URL </param>
		public OAuthRequest(string verb, string url) : base(verb, url)
		{
			this.oauthParameters = new Dictionary<string, string>();
		}

		/// <summary>
		/// Adds an OAuth parameter.
		/// </summary>
		/// <param name="key">   name of the parameter </param>
		/// <param name="value"> value of the parameter </param>
		/// <exception cref="IllegalArgumentException"> if the parameter is not an OAuth parameter </exception>
		public virtual void addOAuthParameter(string key, string value)
		{
			oauthParameters[checkKey(key)] = value;
		}

		private string checkKey(string key)
		{
			if (key.StartsWith(OAUTH_PREFIX) || key.Equals(OAuthConstants.SCOPE))
				return key;
			else
				throw new System.ArgumentException(string.Format("OAuth parameters must either be '{0}' or start with '{1}'", OAuthConstants.SCOPE, OAUTH_PREFIX));
		}

		/// <summary>
		/// Returns the <seealso cref="Map"/> containing the key-value pair of parameters.
		/// </summary>
		/// <returns> parameters as map </returns>
		public virtual IDictionary<string, string> OauthParameters
		{
			get
			{
				return oauthParameters;
			}
		}

		public override string ToString()
		{
			return string.Format("@OAuthRequest({0}, {1})", Verb, Url);
		}
	}

}