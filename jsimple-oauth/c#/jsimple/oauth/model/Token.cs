using System;

namespace jsimple.oauth.model
{




	/// <summary>
	/// Represents an OAuth token (either request or access token) and its secret
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class Token // implements Serializable
	{
		//private static final long serialVersionUID = 715000866082812683L;

		private readonly string token;
		private readonly string secret;
		private readonly string rawResponse;

		/// <summary>
		/// Default constructor
		/// </summary>
		/// <param name="token">  token value </param>
		/// <param name="secret"> token secret </param>
		public Token(string token, string secret) : this(token, secret, null)
		{
		}

		public Token(string token, string secret, string rawResponse)
		{
			this.token = token;
			this.secret = secret;
			this.rawResponse = rawResponse;
		}

		public virtual string TokenString
		{
			get
			{
				return token;
			}
		}

		public virtual string Secret
		{
			get
			{
				return secret;
			}
		}

		public virtual string RawResponse
		{
			get
			{
				if (rawResponse == null)
					throw new Exception("This token object was not constructed by scribe and does not have a rawResponse");
				return rawResponse;
			}
		}

		public override string ToString()
		{
			return string.Format("Token[{0} , {1}]", token, secret);
		}
	}

}