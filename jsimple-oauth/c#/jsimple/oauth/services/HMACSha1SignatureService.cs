using System;
using System.Diagnostics;

namespace jsimple.oauth.services
{

	using IOUtils = jsimple.io.IOUtils;
	using OAuthSignatureException = jsimple.oauth.exceptions.OAuthSignatureException;
	using OAuthEncoder = jsimple.oauth.utils.OAuthEncoder;
	using Sha1 = jsimple.oauth.utils.Sha1;
	using Base64 = jsimple.util.Base64;

	/// <summary>
	/// HMAC-SHA1 implementation of {@SignatureService}
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class HMACSha1SignatureService : SignatureService
	{
		private const string METHOD = "HMAC-SHA1";

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string getSignature(string baseString, string apiSecret, string tokenSecret)
		{
			try
			{
				Debug.Assert(baseString != null && baseString.Length > 0, "Base string cant be null or empty string");
				Debug.Assert(apiSecret != null && apiSecret.Length > 0, "Api secret cant be null or empty string");

				return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
			}
			catch (Exception e)
			{
				throw new OAuthSignatureException(baseString, e);
			}
		}

		private string doSign(string toSign, string keyString)
		{
			//byte[] toUtf8BytesFromString(String s, int[] length) {

			sbyte[] signature = Sha1.mac(IOUtils.toUtf8BytesFromString(keyString), IOUtils.toUtf8BytesFromString(toSign));
			return Base64.encodeBase64AsString(signature);

			/*
			SecretKeySpec key = new SecretKeySpec(keyString.getBytes(UTF8), HMAC_SHA1);
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(key);
			byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
			return new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
			*/
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string SignatureMethod
		{
			get
			{
				return METHOD;
			}
		}
	}

}