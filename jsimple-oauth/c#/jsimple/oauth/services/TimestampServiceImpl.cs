using System;

namespace jsimple.oauth.services
{

	using PlatformUtils = jsimple.util.PlatformUtils;

	/// <summary>
	/// Implementation of <seealso cref="TimestampService"/> using plain java classes.
	/// 
	/// @author Pablo Fernandez
	/// </summary>
	public class TimestampServiceImpl : TimestampService
	{
		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string Nonce
		{
			get
			{
				long? ts = Ts;
				return Convert.ToString(ts + (new Random()).Next());
			}
		}

		/// <summary>
		/// {@inheritDoc}
		/// </summary>
		public virtual string TimestampInSeconds
		{
			get
			{
				return Convert.ToString(Ts);
			}
		}

		private long? Ts
		{
			get
			{
				return PlatformUtils.CurrentTimeMillis / 1000;
			}
		}
	}

}