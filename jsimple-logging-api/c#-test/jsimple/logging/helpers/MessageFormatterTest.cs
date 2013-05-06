using System;

/// <summary>
/// Copyright (c) 2004-2011 QOS.ch
/// All rights reserved.
/// 
/// Permission is hereby granted, free  of charge, to any person obtaining
/// a  copy  of this  software  and  associated  documentation files  (the
/// "Software"), to  deal in  the Software without  restriction, including
/// without limitation  the rights to  use, copy, modify,  merge, publish,
/// distribute,  sublicense, and/or sell  copies of  the Software,  and to
/// permit persons to whom the Software  is furnished to do so, subject to
/// the following conditions:
/// 
/// The  above  copyright  notice  and  this permission  notice  shall  be
/// included in all copies or substantial portions of the Software.
/// 
/// THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
/// EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
/// MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
/// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
/// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
/// OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
/// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
/// 
/// </summary>
namespace jsimple.logging.helpers
{


	using UnitTest = jsimple.unit.UnitTest;
	using NUnit.Framework;

	/// <summary>
	/// @author Ceki Gulcu
	/// @author Bret Johnson modified for JSimple
	/// </summary>
	public class MessageFormatterTest : UnitTest
	{
		private bool InstanceFieldsInitialized = false;

		public MessageFormatterTest()
		{
			if (!InstanceFieldsInitialized)
			{
				InitializeInstanceFields();
				InstanceFieldsInitialized = true;
			}
		}

		private void InitializeInstanceFields()
		{
			ia0 = new int?[]{i1, i2, i3};
		}

		internal int? i1 = new int?(1);
		internal int? i2 = new int?(2);
		internal int? i3 = new int?(3);
		internal int?[] ia0;
		internal int?[] ia1 = new int?[]{new int?(10), new int?(20), new int?(30)};

		internal string result;

		[Test] public virtual void testNullParam()
		{
			result = MessageFormatter.format("Value is {}.", null).Message;
			assertEquals("Value is null.", result);

			result = MessageFormatter.format("Val1 is {}, val2 is {}.", null, null).Message;
			assertEquals("Val1 is null, val2 is null.", result);

			result = MessageFormatter.format("Val1 is {}, val2 is {}.", i1, null).Message;
			assertEquals("Val1 is 1, val2 is null.", result);

			result = MessageFormatter.format("Val1 is {}, val2 is {}.", null, i2).Message;
			assertEquals("Val1 is null, val2 is 2.", result);

			result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}", new int?[]{null, null, null}).Message;
			assertEquals("Val1 is null, val2 is null, val3 is null", result);

			result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}", new int?[]{null, i2, i3}).Message;
			assertEquals("Val1 is null, val2 is 2, val3 is 3", result);

			result = MessageFormatter.arrayFormat("Val1 is {}, val2 is {}, val3 is {}", new int?[]{null, null, i3}).Message;
			assertEquals("Val1 is null, val2 is null, val3 is 3", result);
		}

		[Test] public virtual void testOneParameter()
		{
			result = MessageFormatter.format("Value is {}.", i3).Message;
			assertEquals("Value is 3.", result);

			result = MessageFormatter.format("Value is {", i3).Message;
			assertEquals("Value is {", result);

			result = MessageFormatter.format("{} is larger than 2.", i3).Message;
			assertEquals("3 is larger than 2.", result);

			result = MessageFormatter.format("No subst", i3).Message;
			assertEquals("No subst", result);

			result = MessageFormatter.format("Incorrect {subst", i3).Message;
			assertEquals("Incorrect {subst", result);

			result = MessageFormatter.format("Value is {bla} {}", i3).Message;
			assertEquals("Value is {bla} 3", result);

			result = MessageFormatter.format("Escaped \\{} subst", i3).Message;
			assertEquals("Escaped {} subst", result);

			result = MessageFormatter.format("{Escaped", i3).Message;
			assertEquals("{Escaped", result);

			result = MessageFormatter.format("\\{}Escaped", i3).Message;
			assertEquals("{}Escaped", result);

			result = MessageFormatter.format("File name is {{}}.", "App folder.zip").Message;
			assertEquals("File name is {App folder.zip}.", result);

			// escaping the escape character
			result = MessageFormatter.format("File name is C:\\\\{}.", "App folder.zip").Message;
			assertEquals("File name is C:\\App folder.zip.", result);
		}

		[Test] public virtual void testTwoParameters()
		{
			result = MessageFormatter.format("Value {} is smaller than {}.", i1, i2).Message;
			assertEquals("Value 1 is smaller than 2.", result);

			result = MessageFormatter.format("Value {} is smaller than {}", i1, i2).Message;
			assertEquals("Value 1 is smaller than 2", result);

			result = MessageFormatter.format("{}{}", i1, i2).Message;
			assertEquals("12", result);

			result = MessageFormatter.format("Val1={}, Val2={", i1, i2).Message;
			assertEquals("Val1=1, Val2={", result);

			result = MessageFormatter.format("Value {} is smaller than \\{}", i1, i2).Message;
			assertEquals("Value 1 is smaller than {}", result);

			result = MessageFormatter.format("Value {} is smaller than \\{} tail", i1, i2).Message;
			assertEquals("Value 1 is smaller than {} tail", result);

			result = MessageFormatter.format("Value {} is smaller than \\{", i1, i2).Message;
			assertEquals("Value 1 is smaller than \\{", result);

			result = MessageFormatter.format("Value {} is smaller than {tail", i1, i2).Message;
			assertEquals("Value 1 is smaller than {tail", result);

			result = MessageFormatter.format("Value \\{} is smaller than {}", i1, i2).Message;
			assertEquals("Value {} is smaller than 1", result);
		}

		[Test] public virtual void testExceptionIn_toString()
		{
			object o = new ObjectAnonymousInnerClassHelper(this);
			result = MessageFormatter.format("Troublesome object {}", o).Message;
			assertEquals("Troublesome object [FAILED toString()]", result);

		}

		private class ObjectAnonymousInnerClassHelper : object
		{
			private readonly MessageFormatterTest outerInstance;

			public ObjectAnonymousInnerClassHelper(MessageFormatterTest outerInstance)
			{
				this.outerInstance = outerInstance;
			}

			public override string ToString()
			{
				throw new IllegalStateException("a");
			}
		}

		// tests the case when the parameters are supplied in a single array
		[Test] public virtual void testArrayFormat()
		{
			result = MessageFormatter.arrayFormat("Value {} is smaller than {} and {}.", ia0).Message;
			assertEquals("Value 1 is smaller than 2 and 3.", result);

			result = MessageFormatter.arrayFormat("{}{}{}", ia0).Message;
			assertEquals("123", result);

			result = MessageFormatter.arrayFormat("Value {} is smaller than {}.", ia0).Message;
			assertEquals("Value 1 is smaller than 2.", result);

			result = MessageFormatter.arrayFormat("Value {} is smaller than {}", ia0).Message;
			assertEquals("Value 1 is smaller than 2", result);

			result = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia0).Message;
			assertEquals("Val=1, {, Val=2", result);

			result = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia0).Message;
			assertEquals("Val=1, {, Val=2", result);

			result = MessageFormatter.arrayFormat("Val1={}, Val2={", ia0).Message;
			assertEquals("Val1=1, Val2={", result);
		}

		[Test] public virtual void testArrayThrowable()
		{
			FormattingTuple ft;
			Exception t = new Exception();
			object[] ia = new object[]{i1, i2, i3, t};
			object[] iaWitness = new object[]{i1, i2, i3};

			ft = MessageFormatter.arrayFormat("Value {} is smaller than {} and {}.", ia);
			assertEquals("Value 1 is smaller than 2 and 3.", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("{}{}{}", ia);
			assertEquals("123", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Value {} is smaller than {}.", ia);
			assertEquals("Value 1 is smaller than 2.", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Value {} is smaller than {}", ia);
			assertEquals("Value 1 is smaller than 2", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Val={}, {, Val={}", ia);
			assertEquals("Val=1, {, Val=2", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Val={}, \\{, Val={}", ia);
			assertEquals("Val=1, \\{, Val=2", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Val1={}, Val2={", ia);
			assertEquals("Val1=1, Val2={", ft.Message);
			assertTrue(Arrays.Equals(iaWitness, ft.ArgArray));
			assertEquals(t, ft.Throwable);

			ft = MessageFormatter.arrayFormat("Value {} is smaller than {} and {} -- {} .", ia);
			assertEquals("Value 1 is smaller than 2 and 3 -- " + t.ToString() + " .", ft.Message);
			assertTrue(Arrays.Equals(ia, ft.ArgArray));
			assertNull(ft.Throwable);

			ft = MessageFormatter.arrayFormat("{}{}{}{}", ia);
			assertEquals("123" + t.ToString(), ft.Message);
			assertTrue(Arrays.Equals(ia, ft.ArgArray));
			assertNull(ft.Throwable);
		}
	}

}