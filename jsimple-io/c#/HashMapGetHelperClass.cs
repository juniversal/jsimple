//---------------------------------------------------------------------------------------------------------
//	Copyright © 2007 - 2013 Tangible Software Solutions Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class is used to replace calls to Java HashMap or Hashtable 'get' with the strict C# equivalent.
//---------------------------------------------------------------------------------------------------------
internal static class HashMapGetHelperClass
{
	internal static TValue GetValueOrNull<TKey, TValue>(this System.Collections.Generic.IDictionary<TKey, TValue> dictionary, TKey key)
	{
		TValue ret;
		dictionary.TryGetValue(key, out ret);
		return ret;
	}
}