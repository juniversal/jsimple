using System;

namespace jsimple.oauth.utils
{

	using StringUtils = jsimple.util.StringUtils;

	/// <summary>
	/// @author Sent from Nurmi Raimo.T (Nokia-MP/Espoo) <raimo.t.nurmi@nokia.com> & Lindholm Samu (Nokia-MP/Espoo)
	///         <Samu.Lindholm@nokia.com>.  They said "this is heavily used and tested [on S40 I assume].  I'm not sure where
	///         the code originally came from before that.  It's original package name was "mango.client.util".
	/// @since 10/26/12 2:15 AM
	/// </summary>
	public class Sha1
	{
		public const int SIZE = 20;

		private int[] m_state = new int[]{0x67452301, -271733879, -1732584194, 0x10325476, -1009589776};
		private long m_lCount = 0;
		private sbyte[] m_digestBits = new sbyte[SIZE];
		private int[] m_block = new int[16];
		private int m_nBlockIndex = 0;

		public Sha1()
		{
		}

//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: private static final int rol(final int nValue, final int nBits)
		private static int rol(int nValue, int nBits)
		{
			return ((nValue << nBits) | ((int)((uint)nValue >> (32 - nBits))));
		}

//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: private final int blk0(final int nI)
		private int blk0(int nI)
		{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final int[] b = m_block;
			int[] b = m_block;
			return b[nI] = ((rol(b[nI], 24) & -16711936) | (rol(b[nI], 8) & 0x00ff00ff)); // Need to express as negative decimal, not hex, so C# treats as int, not long
		}

//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: private final int blk(final int nI)
		private int blk(int nI)
		{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final int[] b = m_block;
			int[] b = m_block;
			return (b[nI & 15] = rol(b[(nI + 13) & 15] ^ b[(nI + 8) & 15] ^ b[(nI + 2) & 15] ^ b[nI & 15], 1));
		}

		private void transform()
		{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final int[] data = new int[5];
			int[] data = new int[5];
			data[0] = m_state[0];
			data[1] = m_state[1];
			data[2] = m_state[2];
			data[3] = m_state[3];
			data[4] = m_state[4];

			int nV = 1;
			int nW = 2;
			int nX = 3;
			int nY = 4;
			int nZ = 0;
			int phase = 0;
			for (int nI = 0; nI < 80; nI++)
			{
				//argument rotation
				int tmp = nZ;
				nZ = nY;
				nY = nX;
				nX = nW;
				nW = nV;
				nV = tmp;

				//Inlined r0,r1,r2,r3,r4 boxes
				switch (phase)
				{
					//0..15
					case 0:
					{
						data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY]) + blk0(nI) + 0x5a827999 + rol(data[nV], 5);
						data[nW] = rol(data[nW], 30);
						if (nI == 15)
							phase++;
					}
					continue;

					//16..19
					case 1:
					{
						data[nZ] += ((data[nW] & (data[nX] ^ data[nY])) ^ data[nY]) + blk(nI) + 0x5a827999 + rol(data[nV], 5);
						data[nW] = rol(data[nW], 30);
						if (nI == 19)
							phase++;
					}
					continue;

					//20..39
					case 2:
					{
						data[nZ] += (data[nW] ^ data[nX] ^ data[nY]) + blk(nI) + 0x6eD9eba1 + rol(data[nV], 5);
						data[nW] = rol(data[nW], 30);
						if (nI == 39)
							phase++;
					}
					continue;

					//39..59
					case 3:
					{
						data[nZ] += (((data[nW] | data[nX]) & data[nY]) | (data[nW] & data[nX])) + blk(nI) + -1894007588 + rol(data[nV], 5); // Need to express as negative decimal, not hex, so C# treats as int, not long
						data[nW] = rol(data[nW], 30);
						if (nI == 59)
							phase++;
					}
					continue;

					//59..79
					case 4:
					{
						data[nZ] += (data[nW] ^ data[nX] ^ data[nY]) + blk(nI) + -899497514 + rol(data[nV], 5); // Need to express as negative decimal, not hex, so C# treats as int, not long
						data[nW] = rol(data[nW], 30);
					}
					continue;
				}
			}

			m_state[0] += data[0];
			m_state[1] += data[1];
			m_state[2] += data[2];
			m_state[3] += data[3];
			m_state[4] += data[4];
		}

		public virtual void update(sbyte bB)
		{
			int nMask = (m_nBlockIndex & 3) << 3;

			m_lCount += 8;
			m_block[m_nBlockIndex >> 2] &= ~(0xff << nMask);
			m_block[m_nBlockIndex >> 2] |= (bB & 0xff) << nMask;
			m_nBlockIndex++;
			if (m_nBlockIndex == 64)
			{
				transform();
				m_nBlockIndex = 0;
			}
		}

		public virtual void update(sbyte[] data)
		{
			update(data, 0, data.Length);
		}

		public virtual void update(sbyte[] data, int nOfs, int nLen)
		{
			for (int nEnd = nOfs + nLen; nOfs < nEnd; nOfs++)
				update(data[nOfs]);
		}

		public virtual void doFinal(sbyte[] data)
		{
			update(data, 0, data.Length);
			finish();
		}


		/// <summary>
		/// Finalizes the digest.
		/// </summary>
		private void finish()
		{
			int nI;
			sbyte[] bits = new sbyte[8];

			for (nI = 0; nI < 8; nI++)
				bits[nI] = (sbyte)(((long)((ulong)m_lCount >> (((7 - nI) << 3)))) & 0xff);


			update(unchecked((sbyte)(unchecked((sbyte) - 128)))); // Was 128 in original code but -128 for signed byte 0x80 is more correct
			while (m_nBlockIndex != 56)
				update((sbyte) 0);

			for (nI = 0; nI < bits.Length; nI++)
				update(bits[nI]);

			for (nI = 0; nI < 20; nI++)
				m_digestBits[nI] = (sbyte)((m_state[nI >> 2] >> ((3 - (nI & 3)) << 3)) & 0xff);
		}


		/// <summary>
		/// Retrieves the digest.
		/// </summary>
		/// <returns> the digst bytes as an array if DIGEST_SIZE bytes </returns>
		public virtual sbyte[] Digest
		{
			get
			{
				sbyte[] result = new sbyte[SIZE];
				Array.Copy(m_digestBits, 0, result, 0, SIZE);
				return result;
			}
		}

		/*
		* makes a binhex string representation of the current digest
		* @return the string representation
		*/
		public override string ToString()
		{
			return StringUtils.toHexStringFromBytes(m_digestBits);
		}

//JAVA TO C# CONVERTER WARNING: 'final' parameters are not allowed in .NET:
//ORIGINAL LINE: public static byte[] mac(final byte[] key, final byte[] text)
		public static sbyte[] mac(sbyte[] key, sbyte[] text)
		{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final byte[] pkey = new byte[64];
			sbyte[] pkey = new sbyte[64];
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final byte[] buf = new byte[64];
			sbyte[] buf = new sbyte[64];
			if (key != null && key.Length > 0)
			{
				if (key.Length <= 64)
					Array.Copy(key, 0, pkey, 0, key.Length);
				else
				{
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final Sha1 shakey = new Sha1();
					Sha1 shakey = new Sha1();
					shakey.doFinal(key);
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final byte[] keya = shakey.getDigest();
					sbyte[] keya = shakey.Digest;
					Array.Copy(keya, 0, pkey, 0, keya.Length);
				}
			}

			for (int i = 0; i < 64; i++)
				buf[i] = (sbyte)((0x36 ^ pkey[i]) & 0xff);

//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final Sha1 sha1 = new Sha1();
			Sha1 sha1 = new Sha1();
			sha1.update(buf);
			sha1.doFinal(text);
//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final byte[] h1 = sha1.getDigest();
			sbyte[] h1 = sha1.Digest;

			for (int i = 0; i < 64; i++)
				buf[i] = (sbyte)((0x5C ^ pkey[i]) & 0xff);

//JAVA TO C# CONVERTER WARNING: The original Java variable was marked 'final':
//ORIGINAL LINE: final Sha1 sha2 = new Sha1();
			Sha1 sha2 = new Sha1();
			sha2.update(buf);
			sha2.doFinal(h1);
			return sha2.Digest;
		}
	}

}