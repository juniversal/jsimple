namespace jsimple.unit
{



	public abstract class UnitTestBase
	{
		/// <summary>
		/// Get the Java project directory, with the Java source etc. underneath it.  This directory can then be used to
		/// locate test data files, etc.
		/// 
		/// @return
		/// </summary>
		public abstract string JavaProjectDirectory {get;}

		/// <summary>
		/// Asserts that two objects are equal. If they are not, a test framework assertion error without a message is
		/// thrown. If <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
		/// </summary>
		/// <param name="expected"> expected value </param>
		/// <param name="actual">   the value to check against <code>expected</code> </param>
		public virtual void assertEquals(object expected, object actual)
		{
			assertEquals(null, expected, actual);
		}

		/// <summary>
		/// Asserts that two objects are equal. If they are not, a test framework assertion error is thrown with the given
		/// message. If <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
		/// </summary>
		/// <param name="message">  the identifying message for the assertion error (<code>null</code> okay) </param>
		/// <param name="expected"> expected value </param>
		/// <param name="actual">   actual value </param>
		public abstract void assertEquals(string message, object expected, object actual);

		/// <summary>
		/// Asserts that two longs are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expected"> expected long value. </param>
		/// <param name="actual">   actual long value </param>
		public virtual void assertEquals(long expected, long actual)
		{
			assertEquals(null, expected, actual);
		}

		/// <summary>
		/// Asserts that two longs are equal. If they are not, a test framework assertion error is thrown with the given
		/// message.
		/// </summary>
		/// <param name="message">  the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expected"> long expected value. </param>
		/// <param name="actual">   long actual value </param>
		public virtual void assertEquals(string message, long expected, long actual)
		{
			assertEquals(message, (long?) expected, (long?) actual);
		}

		/// <summary>
		/// Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown with the
		/// given message. If <code>expecteds</code> and <code>actuals</code> are <code>null</code>, they are considered
		/// equal.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> Object array or array of arrays (multi-dimensional array) with expected values. </param>
		/// <param name="actuals">   Object array or array of arrays (multi-dimensional array) with actual values </param>
		public abstract void assertArrayEquals(string message, object[] expecteds, object[] actuals);

		/// <summary>
		/// Asserts that two object arrays are equal. If they are not, a test framework assertion error is thrown. If
		/// <code>expected</code> and <code>actual</code> are <code>null</code>, they are considered equal.
		/// </summary>
		/// <param name="expecteds"> Object array or array of arrays (multi-dimensional array) with expected values </param>
		/// <param name="actuals">   Object array or array of arrays (multi-dimensional array) with actual values </param>
		public virtual void assertArrayEquals(object[] expecteds, object[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that two byte arrays are equal. If they are not, a test framework assertion error is thrown with the
		/// given message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> byte array with expected values </param>
		/// <param name="actuals">   byte array with actual values </param>
		public abstract void assertArrayEquals(string message, sbyte[] expecteds, sbyte[] actuals);

		/// <summary>
		/// Asserts that two byte arrays are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expecteds"> byte array with expected values. </param>
		/// <param name="actuals">   byte array with actual values </param>
		public virtual void assertArrayEquals(sbyte[] expecteds, sbyte[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that two char arrays are equal. If they are not, a test framework assertion error is thrown with the
		/// given message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> char array with expected values. </param>
		/// <param name="actuals">   char array with actual values </param>
		public abstract void assertArrayEquals(string message, char[] expecteds, char[] actuals);

		/// <summary>
		/// Asserts that two char arrays are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expecteds"> char array with expected values. </param>
		/// <param name="actuals">   char array with actual values </param>
		public virtual void assertArrayEquals(char[] expecteds, char[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that two short arrays are equal. If they are not, a test framework assertion error is thrown with the
		/// given message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> short array with expected values. </param>
		/// <param name="actuals">   short array with actual values </param>
		public abstract void assertArrayEquals(string message, short[] expecteds, short[] actuals);

		/// <summary>
		/// Asserts that two short arrays are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expecteds"> short array with expected values. </param>
		/// <param name="actuals">   short array with actual values </param>
		public virtual void assertArrayEquals(short[] expecteds, short[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that two int arrays are equal. If they are not, a test framework assertion error is thrown with the given
		/// message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> int array with expected values. </param>
		/// <param name="actuals">   int array with actual values </param>
		public abstract void assertArrayEquals(string message, int[] expecteds, int[] actuals);

		/// <summary>
		/// Asserts that two int arrays are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expecteds"> int array with expected values. </param>
		/// <param name="actuals">   int array with actual values </param>
		public virtual void assertArrayEquals(int[] expecteds, int[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that two long arrays are equal. If they are not, a test framework assertion error is thrown with the
		/// given message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="expecteds"> long array with expected values. </param>
		/// <param name="actuals">   long array with actual values </param>
		public abstract void assertArrayEquals(string message, long[] expecteds, long[] actuals);

		/// <summary>
		/// Asserts that two long arrays are equal. If they are not, a test framework assertion error is thrown.
		/// </summary>
		/// <param name="expecteds"> long array with expected values. </param>
		/// <param name="actuals">   long array with actual values </param>
		public virtual void assertArrayEquals(long[] expecteds, long[] actuals)
		{
			assertArrayEquals(null, expecteds, actuals);
		}

		/// <summary>
		/// Asserts that a condition is true. If it isn't it throws a test framework assertion error with the given message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="condition"> condition to be checked </param>
		public virtual void assertTrue(string message, bool condition)
		{
			if (!condition)
				fail(message);
		}

		/// <summary>
		/// Asserts that a condition is true. If it isn't it throws a test framework assertion error without a message.
		/// </summary>
		/// <param name="condition"> condition to be checked </param>
		public virtual void assertTrue(bool condition)
		{
			assertTrue(null, condition);
		}

		/// <summary>
		/// Asserts that a condition is false. If it isn't it throws a test framework assertion error with the given
		/// message.
		/// </summary>
		/// <param name="message">   the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="condition"> condition to be checked </param>
		public virtual void assertFalse(string message, bool condition)
		{
			assertTrue(message, !condition);
		}

		/// <summary>
		/// Asserts that a condition is false. If it isn't it throws a test framework assertion error without a message.
		/// </summary>
		/// <param name="condition"> condition to be checked </param>
		public virtual void assertFalse(bool condition)
		{
			assertFalse(null, condition);
		}

		/// <summary>
		/// Asserts that an object isn't null. If it is a test framework assertion error is thrown with the given message.
		/// </summary>
		/// <param name="message"> the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="object">  Object to check or <code>null</code> </param>
		public virtual void assertNotNull(string message, object @object)
		{
			assertTrue(message, @object != null);
		}

		/// <summary>
		/// Asserts that an object isn't null. If it is a test framework assertion error is thrown.
		/// </summary>
		/// <param name="object"> Object to check or <code>null</code> </param>
		public virtual void assertNotNull(object @object)
		{
			assertNotNull(null, @object);
		}

		/// <summary>
		/// Asserts that an object is null. If it is not, a test framework assertion error is thrown with the given message.
		/// </summary>
		/// <param name="message"> the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		/// <param name="object">  Object to check or <code>null</code> </param>
		public virtual void assertNull(string message, object @object)
		{
			assertTrue(message, @object == null);
		}

		/// <summary>
		/// Asserts that an object is null. If it isn't a test framework assertion error is thrown.
		/// </summary>
		/// <param name="object"> Object to check or <code>null</code> </param>
		public virtual void assertNull(object @object)
		{
			assertNull(null, @object);
		}

		/// <summary>
		/// Fails a test with the given message.
		/// </summary>
		/// <param name="message"> the identifying message for the test framework assertion error (<code>null</code> okay) </param>
		public abstract void fail(string message);

		/// <summary>
		/// Fails a test with no message.
		/// </summary>
		public virtual void fail()
		{
			fail(null);
		}
	}

}