package consistency;

import static org.junit.Assert.*;
import hash_set.MyInteger;

import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

// TODO: move performance tests to another class

public class SimpleHashSetTest {
	private static final Random randomNumberGenerator = new Random();

	@Test
	public void firstSimpleTest() {
		final SimpleHashSet<MyInteger> hashSet = new SimpleHashSet<>();
		assertEquals(hashSet.size(), 0);
		assertEquals(hashSet.isEmpty(), true);
		assertEquals(hashSet.contains(new MyInteger(1)), false);

		hashSet.add(new MyInteger(1));
		assertEquals(hashSet.size(), 1);
		assertEquals(hashSet.isEmpty(), false);
		assertEquals(hashSet.contains(new MyInteger(1)), true);
		assertEquals(hashSet.contains(new MyInteger(2)), false);

		hashSet.add(new MyInteger(2));
		assertEquals(hashSet.size(), 2);
		assertEquals(hashSet.isEmpty(), false);
		assertEquals(hashSet.contains(new MyInteger(1)), true);
		assertEquals(hashSet.contains(new MyInteger(2)), true);
		assertEquals(hashSet.contains(new MyInteger(3)), false);

		hashSet.remove(new MyInteger(2));
		assertEquals(hashSet.size(), 1);
		assertEquals(hashSet.isEmpty(), false);
		assertEquals(hashSet.contains(new MyInteger(1)), true);
		assertEquals(hashSet.contains(new MyInteger(2)), false);

		hashSet.remove(new MyInteger(1));
		assertEquals(hashSet.size(), 0);
		assertEquals(hashSet.isEmpty(), true);
		assertEquals(hashSet.contains(new MyInteger(1)), false);
	}

	@Test
	public void secondSimpleTest() {
		final int bound = 10_000_000;
		final SimpleHashSet<MyInteger> hashSet = new SimpleHashSet<>();

		for(int i = 1; i <= bound; i++) {
			assertEquals(hashSet.size(), i - 1);
			assertEquals(hashSet.contains(new MyInteger(i)), false);
			hashSet.add(new MyInteger(i));
			assertEquals(hashSet.size(), i);
			assertEquals(hashSet.contains(new MyInteger(i)), true);
		}

		for(int i = bound; i >= 1; i--) {
			assertEquals(hashSet.size(), i);
			assertEquals(hashSet.contains(new MyInteger(i)), true);
			hashSet.remove(new MyInteger(i));
			assertEquals(hashSet.size(), i - 1);
			assertEquals(hashSet.contains(new MyInteger(i)), false);
		}

//		hashSet.dumpStats();
	}

	@Test
	public void secondJavaTest() {
		final int bound = 10_000_000;
		final HashSet<MyInteger> hashSet = new HashSet<>();

		for(int i = 1; i <= bound; i++) {
			assertEquals(hashSet.size(), i - 1);
			assertEquals(hashSet.contains(new MyInteger(i)), false);
			hashSet.add(new MyInteger(i));
			assertEquals(hashSet.size(), i);
			assertEquals(hashSet.contains(new MyInteger(i)), true);
		}

		for(int i = bound; i >= 1; i--) {
			assertEquals(hashSet.size(), i);
			assertEquals(hashSet.contains(new MyInteger(i)), true);
			hashSet.remove(new MyInteger(i));
			assertEquals(hashSet.size(), i - 1);
			assertEquals(hashSet.contains(new MyInteger(i)), false);
		}
	}

//	@Test
	public void lookAtHashingIntegers() {
		for(int i = 0; i < 32; i++) {
			final MyInteger myInteger = new MyInteger(i);
			System.out.println(myInteger.intValue() + " " + myInteger.hashCode());
		}
		for(int i = 0; i < 32; i++) {
			final Integer integer = new Integer(i);
			System.out.println(integer.intValue() + " " + integer.hashCode());
		}
	}

	@Test
	public void chooseTest() {
		final int SIZE_OF_SET = 100;
		final int NUMBER_OF_TRIES = 100_000_000;

		final SimpleHashSet<MyInteger> hashSet = new SimpleHashSet<>();
		for(int i = 0; i < SIZE_OF_SET; i++) {
			hashSet.add(new MyInteger(i));
		}

		final int[] counts = new int[SIZE_OF_SET];
		for(int count = 0; count < NUMBER_OF_TRIES; count++) {
			final MyInteger myInteger = hashSet.chooseRandomElement();
			counts[myInteger.intValue()] += 1;
		}
		for(int i = 0; i < SIZE_OF_SET; i++) {
//			System.out.println(i + " " + counts[i]);
			assertTrue(999_000 < counts[i] && counts[i] < 1_001_000);
		}
	}

	@Test
	public void enumerationTest() {
		final int MAXIMUM_VALUES = 100_000;
		final int NUMBER_OF_TRIES = 100;

		for(int i = 0; i < NUMBER_OF_TRIES; i++) {
			final int numberOfValues = randomNumberGenerator.nextInt(MAXIMUM_VALUES);
//			System.out.println("try " + i + " " + numberOfValues);

			final SimpleHashSet<MyInteger> hashSet = new SimpleHashSet<>();
			for(int j = 0; j < numberOfValues; j++) {
				while(true) {
					final MyInteger myInteger = new MyInteger(randomNumberGenerator.nextInt());
					if(!hashSet.contains(myInteger)) {
						hashSet.add(myInteger);
						break;
					}
				}
			}
			assertEquals(numberOfValues, hashSet.size());
			int count = 0;
			for(final MyInteger myInteger : hashSet) {
				assertTrue(hashSet.contains(myInteger));
				count += 1;
			}
			assertEquals(count, numberOfValues);
		}
	}

	@Test
	public void addAll0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(4));
		hashSetA.add(new MyInteger(5));
		hashSetA.add(new MyInteger(6));

		hashSetA.addAll(hashSetB);
		assertEquals(hashSetA.size(), 6);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
		assertTrue(hashSetA.contains(new MyInteger(4)));
		assertTrue(hashSetA.contains(new MyInteger(5)));
		assertTrue(hashSetA.contains(new MyInteger(6)));
	}

	@Test
	public void addAll1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));
		hashSetB.add(new MyInteger(4));

		hashSetA.addAll(hashSetB);
		assertEquals(hashSetA.size(), 4);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
		assertTrue(hashSetA.contains(new MyInteger(4)));
	}

	@Test
	public void addAll2Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));

		hashSetA.addAll(hashSetB);
		assertEquals(hashSetA.size(), 3);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void containsAll0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));
		hashSetA.add(new MyInteger(4));
		hashSetA.add(new MyInteger(5));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));

		assertTrue(hashSetA.containsAll(hashSetB));
	}

	@Test
	public void containsAll1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));
		hashSetA.add(new MyInteger(4));
		hashSetA.add(new MyInteger(5));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();

		assertTrue(hashSetA.containsAll(hashSetB));
	}

	@Test
	public void containsAll2Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));
		hashSetA.add(new MyInteger(4));
		hashSetA.add(new MyInteger(5));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(0));
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));

		assertFalse(hashSetA.containsAll(hashSetB));
	}

	@Test
	public void removeAll0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));

		hashSetA.removeAll(hashSetB);
		assertEquals(hashSetA.size(), 0);
		assertFalse(hashSetA.contains(new MyInteger(1)));
		assertFalse(hashSetA.contains(new MyInteger(2)));
		assertFalse(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void removeAll1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();

		hashSetA.removeAll(hashSetB);
		assertEquals(hashSetA.size(), 3);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void removeAll2Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));
		hashSetB.add(new MyInteger(4));

		hashSetA.removeAll(hashSetB);
		assertEquals(hashSetA.size(), 1);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertFalse(hashSetA.contains(new MyInteger(2)));
		assertFalse(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void removeIf0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));
		hashSetA.add(new MyInteger(4));
		hashSetA.add(new MyInteger(5));
		hashSetA.add(new MyInteger(6));
		hashSetA.add(new MyInteger(7));

		hashSetA.removeIf(myInteger -> (((MyInteger) myInteger).intValue() & 1) == 1);
		assertEquals(hashSetA.size(), 3);
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(4)));
		assertTrue(hashSetA.contains(new MyInteger(6)));
	}

	@Test
	public void clear0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final boolean result = hashSetA.clear();
		assertEquals(hashSetA.size(), 0);
		assertEquals(result, true);
	}

	@Test
	public void clear1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();

		final boolean result = hashSetA.clear();
		assertEquals(hashSetA.size(), 0);
		assertEquals(result, false);
	}

	@Test
	public void retainAll0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));

		hashSetA.retainAll(hashSetB);
		assertEquals(hashSetA.size(), 3);
		assertTrue(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void retainAll1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));
		hashSetB.add(new MyInteger(4));

		hashSetA.retainAll(hashSetB);
		assertEquals(hashSetA.size(), 2);
		assertFalse(hashSetA.contains(new MyInteger(1)));
		assertTrue(hashSetA.contains(new MyInteger(2)));
		assertTrue(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void retainAll2Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(4));
		hashSetB.add(new MyInteger(5));
		hashSetB.add(new MyInteger(6));

		hashSetA.retainAll(hashSetB);
		assertEquals(hashSetA.size(), 0);
		assertFalse(hashSetA.contains(new MyInteger(1)));
		assertFalse(hashSetA.contains(new MyInteger(2)));
		assertFalse(hashSetA.contains(new MyInteger(3)));
	}

	@Test
	public void retainAll3Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		for(int i = 0; i <= 50_000; i++) {
			hashSetA.add(new MyInteger(i));
		}

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		for(int i = 50_000; i <= 100_000; i++) {
			hashSetB.add(new MyInteger(1));
		}

		hashSetA.retainAll(hashSetB);

		assertEquals(hashSetA.size(), 1);
		assertEquals(hashSetA.capacity(), 8);
	}

	@Test
	public void equals0Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(1));
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));

		assertTrue(hashSetA.equals(hashSetB));
	}

	@Test
	public void equals1Test() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		hashSetA.add(new MyInteger(1));
		hashSetA.add(new MyInteger(2));
		hashSetA.add(new MyInteger(3));

		final SimpleHashSet<MyInteger> hashSetB = new SimpleHashSet<>();
		hashSetB.add(new MyInteger(2));
		hashSetB.add(new MyInteger(3));
		hashSetB.add(new MyInteger(4));

		assertFalse(hashSetA.equals(hashSetB));
	}

	@Test
	public void iteratorSimpleTest() {
		final SimpleHashSet<MyInteger> hashSetA = new SimpleHashSet<>();
		for(int i = 0; i < 1_000_000; i++) {
			hashSetA.add(new MyInteger(i));
		}

		long counter = 0;
		for(int loop = 0; loop < 500; loop++) {
			for(MyInteger myInteger : hashSetA) {
				counter += myInteger.intValue();
			}
		}
//		System.out.println(counter);
	}

	@Test
	public void iteratorJavaTest() {
		final HashSet<MyInteger> hashSetA = new HashSet<>();
		for(int i = 0; i < 1_000_000; i++) {
			hashSetA.add(new MyInteger(i));
		}

		long counter = 0;
		for(int loop = 0; loop < 500; loop++) {
			for(MyInteger myInteger : hashSetA) {
				counter += myInteger.intValue();
			}
		}
//		System.out.println(counter);
	}
}
