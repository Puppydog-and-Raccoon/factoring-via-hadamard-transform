package consistency;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class UniqueTest {
	@Test
	public void testUnique() {
		final Integer i0 = new Integer(1);
		final Integer i1 = new Integer(1);
		final Unique<Integer> unique = new Unique<Integer>();
		final Integer j0 = unique.unique(i0);
		final Integer j1 = unique.unique(i1);

		assertTrue(i0 != i1);
		assertEquals(i0, i1);
		assertEquals(i0, j0);
		assertEquals(i1, j1);
		assertTrue(j0 == j1);
		assertEquals(j0, j1);
	}

	@Test
	public void testHashSetRemovesDuplicateIntegers() {
		final Integer i0 = new Integer(1);
		final Integer i1 = new Integer(1);
		final HashSet<Integer> hashSet = new HashSet<Integer>();
		hashSet.add(i0);
		hashSet.add(i1);

		assertTrue(i0 != i1);
		assertEquals(i0, i1);
		assertEquals(hashSet.size(), 1);
		assertTrue(hashSet.contains(i0));
		assertTrue(hashSet.contains(i1));
	}
}
