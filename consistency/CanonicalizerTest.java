package consistency;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Random;

import hash_set.MyInteger;

import org.junit.Test;

public class CanonicalizerTest {
	final static Random randomNumberGenerator = new Random();

	@Test
	public void testUnique() {
		final Integer i0 = new Integer(1);
		final Integer i1 = new Integer(1);
		final Canonicalizer<Integer> canonicalizer = new Canonicalizer<Integer>();
		final Integer j0 = canonicalizer.canonicalize(i0);
		final Integer j1 = canonicalizer.canonicalize(i1);

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
		final Canonicalizer<Integer> hashSet = new Canonicalizer<>();
		final Integer c0 = hashSet.canonicalize(i0);
		final Integer c1 = hashSet.canonicalize(i1);

		assertTrue(i0 != i1);
		assertEquals(i0, i1);
		assertTrue(c0 == c1);
		assertEquals(c0, c1);
	}

	@Test
	public void canonicalTest() {
		final int NUMBER_OF_VALUES =   500_000;
		final int NUMBER_OF_TRIES  = 1_000_000;

		final Canonicalizer<MyInteger> hashSet = new Canonicalizer<>();
		final HashMap<MyInteger, MyInteger> hashMap = new HashMap<>();

		for(int i = 0; i < NUMBER_OF_TRIES; i++) {
			final MyInteger myInteger = new MyInteger(randomNumberGenerator.nextInt(NUMBER_OF_VALUES));
			final MyInteger previousValue = hashMap.putIfAbsent(myInteger, myInteger);
			final MyInteger canonical = hashSet.canonicalize(myInteger);
			if(previousValue != null) {
				assertTrue(myInteger != canonical);
				assertEquals(myInteger, canonical);
			} else {
				assertTrue(myInteger == canonical);
				assertEquals(myInteger, canonical);
			}
		}
	}
}
