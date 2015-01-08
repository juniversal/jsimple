package jsimple.util;

import jsimple.unit.UnitTest;
import org.junit.Test;

/**
 * @author Dorin Suletea
 * @since 12/1/2014
 */
public class ListUtilTest extends UnitTest {
	@Test public void testListEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean listsEqual = ListUtils.arrayListsEqual(a, b);
		assertTrue(listsEqual);
		
	}
	
	@Test public void testListNegativeEquals(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(1);
		a.add(2);
		
		
		ArrayList<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(2);
		b.add(3);
		
		boolean listsEqual = ListUtils.arrayListsEqual(a, b);
		assertFalse(listsEqual);
	}
	
	@Test public void testListDistinctElement(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(3);
		a.add(2);
		a.add(2);
		a.add(4);

		List<Integer> output = ListUtils.distinctElements(a);
		
		assertEquals(3, output.size());
		assertEquals(3, (int) output.get(0));
		assertEquals(2, (int) output.get(1));
		assertEquals(4, (int) output.get(2));
	}
}
