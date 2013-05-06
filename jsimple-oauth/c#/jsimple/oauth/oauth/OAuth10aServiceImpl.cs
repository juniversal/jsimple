using System;
using System.Diagnostics;
using System.Collections.Generic;

namespace jsimple.oauth.oauth
{

	using DefaultOAuthApi10a = jsimple.oauth.builder.api.DefaultOAuthApi10a;
	using jsimple.oauth.model;
	using MapUtils = jsimple.oauth.utils.MapUtils;


	/// <summary>
	/// OAuth 1.0a implementation of <seealso cref="OAuthService"/>
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class OAuth10aServiceImpl : OAuthService
	{
		private const string VERSION = "1.0";

		private OAuthConfig config;
		private DefaultOAuthApi10a api;

		/// <summary>
		/// Default constructor
		/// </summary>
		/// <param name="api">    OAuth1.0a api information </param>
		/// <param name="config"> OAuth 1.0a configuration param object </param>
		public OAuth10aServiceImpl(DefaultOAuthApi10a api, OAuthConfig config)
		{
			this.api = api;
			this.config = config;
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual Token RequestToken
		{
			get
			{
				config.log("obtaining request token from " + api.RequestTokenEndpoint);
				OAuthRequest request = new OAuthRequest(api.RequestTokenVerb, api.RequestTokenEndpoint);
    
				config.log("setting oauth_callback to " + config.Callback);
				request.addOAuthParameter(OAuthConstants.CALLBACK, config.Callback);
				addOAuthParams(request, OAuthConstants.EMPTY_TOKEN);
				appendSignature(request);
    
				config.log("sending request...");
				OAuthResponse response = request.send();
				string body = response.Body;
    
				config.log("response status code: " + response.Code);
				config.log("response body: " + body);
				return api.RequestTokenExtractor.extract(body);
			}
		}

		private void addOAuthParams(OAuthRequest request, Token token)
		{
			request.addOAuthParameter(OAuthConstants.TIMESTAMP, api.TimestampService.TimestampInSeconds);
			request.addOAuthParameter(OAuthConstants.NONCE, api.TimestampService.Nonce);
			request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, config.ApiKey);
			request.addOAuthParameter(OAuthConstants.SIGN_METHOD, api.SignatureService.SignatureMethod);
			request.addOAuthParameter(OAuthConstants.VERSION, Version);
			string scope = config.Scope;
			if (scope != null)
				request.addOAuthParameter(OAuthConstants.SCOPE, scope);
			request.addOAuthParameter(OAuthConstants.SIGNATURE, getSignature(request, token));

			config.log("appended additional OAuth parameters: " + MapUtils.toStringFromMap(request.OauthParameters));
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual Token getAccessToken(Token requestToken, Verifier verifier)
		{
			Debug.Assert(requestToken != null, "nullness");

			config.log("obtaining access token from " + api.AccessTokenEndpoint);
			OAuthRequest request = new OAuthRequest(api.AccessTokenVerb, api.AccessTokenEndpoint);
			request.addOAuthParameter(OAuthConstants.TOKEN, requestToken.TokenString);
			request.addOAuthParameter(OAuthConstants.VERIFIER, verifier.Value);

			config.log("setting token to: " + requestToken + " and verifier to: " + verifier);
			addOAuthParams(request, requestToken);
			appendSignature(request);
			OAuthResponse response = request.send();
			return api.AccessTokenExtractor.extract(response.Body);
		}


		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual Token refreshAccessToken(Token refreshOrAccessToken, bool includeSecret)
		{
			throw new Exception("Refresh token is not supported in Scribe OAuth 1.0");
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual void signRequest(Token token, OAuthRequest request)
		{
			config.log("signing request: " + request.CompleteUrl);
			request.addOAuthParameter(OAuthConstants.TOKEN, token.TokenString);

			config.log("setting token to: " + token);
			addOAuthParams(request, token);
			appendSignature(request);
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string Version
		{
			get
			{
				return VERSION;
			}
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string getAuthorizationUrl(Token requestToken)
		{
			Debug.Assert(requestToken != null, "nullness");
			return api.getAuthorizationUrl(requestToken);
		}

		private string getSignature(OAuthRequest request, Token token)
		{
			config.log("generating signature...");
			string baseString = api.BaseStringExtractor.extract(request);
			string signature = api.SignatureService.getSignature(baseString, config.ApiSecret, token.Secret);

			config.log("base string is: " + baseString);
			config.log("signature is: " + signature);
			return signature;
		}

		private void appendSignature(OAuthRequest request)
		{
			SignatureType signatureType = config.SignatureType;

			if (signatureType == SignatureType.Header)
			{
				config.log("using Http Header signature");

				string oauthHeader = api.HeaderExtractor.extract(request);
				request.addHeader(OAuthConstants.HEADER, oauthHeader);
			}
			else if (signatureType == SignatureType.QueryString)
			{
				config.log("using Querystring signature");

				foreach (KeyValuePair<string, string> entry in request.OauthParameters)
					request.addQueryStringParameter(entry.Key, entry.Value);
			}
		}
	}

}