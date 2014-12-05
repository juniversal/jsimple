package jsimple.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class ListUtilTest {
	@Test
	public void testListEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(new Integer(1));
		a.add(2);
		a.add(3);
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean equals = ListUtils.arrayListsEqual(a, b);
		assertTrue(equals);
		
	}
	
	@Test
	public void testListNegativeEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(1);
		a.add(2);
		
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean equals = ListUtils.arrayListsEqual(a, b);
		assertFalse(equals);
	}
	
	@Test
	public void testListDistinctElement(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(2);
		a.add(2);
		a.add(4);
		
		
		List<Integer> output = ListUtils.distinctElements(a);
		
		assertEquals(3, output.size());
		assertEquals(new Integer(3), output.get(0));
		assertEquals(new Integer(2), output.get(1));
		assertEquals(new Integer(4), output.get(2));
	}
}
