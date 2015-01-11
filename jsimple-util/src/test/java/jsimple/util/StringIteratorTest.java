package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class StringIteratorTest extends UnitTest {
	
	@Test public void testAdvance() {
		StringIterator sut = new StringIterator("Test 2string");
		sut.advance();
		sut.advance();

		char currentChr = sut.curr();
		assertEquals('s', currentChr);
	}

	@Test public void testAdvanceWhitespace() {
		StringIterator sut = new StringIterator("   Test 2string");
		sut.advancePastWhitespace();
		char currentChr = sut.curr();
		assertEquals('T', currentChr);
	}

	@Test public void testAtEnd() {
		StringIterator sut = new StringIterator("Test 2string");
		assertFalse(sut.atEnd());

		for (int i = 0; i < sut.getString().length(); i++) {
			sut.advance();
		}

		assertTrue(sut.atEnd());
	}

	@Test public void testGetIndex() {
		StringIterator sut = new StringIterator("Test 2string");
		assertEquals(0, sut.getIndex());
		sut.advance();
		sut.advance();
		assertEquals(2, sut.getIndex());
	}

	@Test public void testGetRemaining() {
		StringIterator sut = new StringIterator("Test 2string");
		sut.advance();
		String remaining = sut.getRemaining();
		String expected = "est 2string";

		assertEquals(remaining, expected);
	}

	@Test public void testIsAsciiOrDigit() {
		StringIterator sut = new StringIterator("Te2string");
		sut.advance();

		assertTrue(sut.isAsciiLetter());
		sut.advance();
		assertTrue(sut.isDigit());
		sut.advance();
		assertTrue(sut.isAsciiLetter());
	}

	@Test public void testIsNewline() {
		StringIterator sut = new StringIterator("T\nstring");
		sut.advance();
		assertTrue(sut.isLineBreak());
	}

	@Test public void testIsWhitespace() {
		StringIterator sut = new StringIterator("T string");
		sut.advance();
		assertTrue(sut.isWhitespace());
	}

	@Test public void testIsWhitespaceSameLine() {
		StringIterator sut = new StringIterator("T\n string");
		sut.advance();

		assertTrue(sut.isWhitespace());
		assertFalse(sut.isWhitespaceOnSameLine());

		sut.advance();
		assertTrue(sut.isWhitespaceOnSameLine());
		assertTrue(sut.isWhitespace());
	}

	// TODO: Add this back
/*
	@Test
	public void testMatchDouble() {
		CharIterator sut = new CharIterator("T 2string");
		sut.advance();
		sut.advance();
		double d = sut.matchDouble();
		
		assertTrue(2.0d == d);
	}
*/

	@Test public void testMatchSubstring() {
		StringIterator sut = new StringIterator("T 23string");
		
		assertTrue(sut.match("T 2"));
		
		assertTrue(sut.match("3"));
	}
}
