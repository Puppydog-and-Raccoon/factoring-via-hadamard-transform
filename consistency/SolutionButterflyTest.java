package consistency;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

// NOTE: This test gives us confidence that all
//       leaf hadamard vectors that correspond
//       to valid (binary) root hadamard vectors
//       produce the correct result, when rippled
//       up through canonical spine and population
//       values. The problem we need to solve now
//       concerns filtering out invalid leaf
//       hadamard values.

// NOTE: we cannot test all valid 16 bit root vectors
//       we could test many random 16 bit leaf vectors

public class SolutionButterflyTest {
	@Test
	public void test_2() {
		testAllVectorsOfSize(2);
	}

	@Test
	public void test_4() {
		testAllVectorsOfSize(4);
	}

	@Test
	public void test_8() {
		testAllVectorsOfSize(8);
	}

//	@Test
	public void test_16() {
		testAllVectorsOfSize(16);
	}

//	@Test
	public void test_32() {
		testAllVectorsOfSize(32);
	}

	// we discovered that when a == b or b == c, it doesn't work, should never occur
	// we discovered that when nt == 0, it doesn't work, should never occur
	// when nt < number of actual trues, is doesn't need to work
	void testAllVectorsOfSize(final int N) {
		final PropertyButterfly propertyButterfly = PropertyButterfly.make(N, DomainGenerator.STANDARD);
		final SolutionButterfly solutionButterfly = new SolutionButterfly(propertyButterfly);
		solutionButterfly.fillAllFactsAndDeltas();

		final int[] leafHadamards = firstLeafVector(N);
		do {
			System.out.println("leaves " + Arrays.toString(leafHadamards));
			boolean comesFromValidHadamardVector = comesFromValidHadamardVector(leafHadamards);
			boolean areValidLeafHadamards = solutionButterfly.areValidLeafHadamards(leafHadamards);
			if(comesFromValidHadamardVector != areValidLeafHadamards) {
				System.out.println("****** " + comesFromValidHadamardVector + " " + areValidLeafHadamards + " " + Arrays.toString(leafHadamards));
			}
			assertTrue(Arrays.toString(leafHadamards), comesFromValidHadamardVector == areValidLeafHadamards);
		} while(nextLeafVector(leafHadamards));
	}

	@Test
	public void testFirstLeafVector() {
		assertArrayEquals(firstLeafVector(1), new int[]{0});
		assertArrayEquals(firstLeafVector(2), new int[]{0, -1});
		assertArrayEquals(firstLeafVector(4), new int[]{0, -2, -2, -2});
		assertArrayEquals(firstLeafVector(8), new int[]{0, -4, -4, -4, -4, -4, -4, -4});
	}

	int[] firstLeafVector(final int N) {
		final int divisor = 2; // really should be 2
		final int[] leafVector = new int[N];
		for(int i = 0; i < N; i++) {
			leafVector[i] = i == 0 ? 0 : -N/divisor;
		}
		return leafVector;
	}

	@Test
	public void testNextLeafVector_1() {
		int[] leafVector = firstLeafVector(1);
		assertArrayEquals(leafVector, new int[]{0});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{1});
		assertFalse(nextLeafVector(leafVector));
	}

	@Test
	public void testNextLeafVector_2() {
		int[] leafVector = firstLeafVector(2);
		assertArrayEquals(leafVector, new int[]{0, -1});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{0,  0});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{0,  1});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{1, -1});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{1,  0});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{1,  1});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{2, -1});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{2,  0});
		assertTrue(nextLeafVector(leafVector));
		assertArrayEquals(leafVector, new int[]{2,  1});
		assertFalse(nextLeafVector(leafVector));
	}

	// returns whether there is another vector or not
	boolean nextLeafVector(final int[] leafVector) {
		final int divisor = 2; // really should be 2
		final int N = leafVector.length;
		for(int i = N-1; i >= 0; i--) {
			final int min = i == 0 ? 0 : -N/divisor;
			final int max = i == 0 ? N :  N/divisor;
			if(leafVector[i] == max) {
				leafVector[i] = min;
			} else {
				leafVector[i] += 1;
				return true;
			}
		}
		return false;
	}

	@Test
	public void testIsValidHadamardVector() {
		assertTrue(comesFromValidHadamardVector(new int[]{0,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{1,  1}));
		assertTrue(comesFromValidHadamardVector(new int[]{1, -1}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  0}));

		assertFalse(comesFromValidHadamardVector(new int[]{0,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{0, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1}));

		assertTrue(comesFromValidHadamardVector(new int[]{0,  0,  0,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{1,  1,  1,  1}));
		assertTrue(comesFromValidHadamardVector(new int[]{1, -1,  1, -1}));
		assertTrue(comesFromValidHadamardVector(new int[]{1,  1, -1, -1}));
		assertTrue(comesFromValidHadamardVector(new int[]{1, -1, -1,  1}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  2,  0,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  0,  2,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  0,  0,  2}));
		assertTrue(comesFromValidHadamardVector(new int[]{2, -2,  0,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  0, -2,  0}));
		assertTrue(comesFromValidHadamardVector(new int[]{2,  0,  0, -2}));
		assertTrue(comesFromValidHadamardVector(new int[]{3,  1,  1, -1}));
		assertTrue(comesFromValidHadamardVector(new int[]{3, -1,  1,  1}));
		assertTrue(comesFromValidHadamardVector(new int[]{3,  1, -1,  1}));
		assertTrue(comesFromValidHadamardVector(new int[]{3, -1, -1, -1}));
		assertTrue(comesFromValidHadamardVector(new int[]{4,  0,  0,  0}));

		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -2,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2, -1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  0, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  0, -1}));
//		assertFalse(isValidHadamardVector(new int[]{2, -2,  0,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  0,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  0,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -2,  2,  2}));

		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -2,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1, -1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  0, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  0, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  0,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  0,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  0,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2, -1,  2,  2}));

		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -2, -1}));
//		assertFalse(isValidHadamardVector(new int[]{2,  0, -2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -2,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0, -1,  2}));
//		assertFalse(isValidHadamardVector(new int[]{2,  0,  0, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  0, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  0,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  0,  1}));
//		assertFalse(isValidHadamardVector(new int[]{2,  0,  0,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  2, -1}));
//		assertFalse(isValidHadamardVector(new int[]{2,  0,  2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  0,  2,  2}));

		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -2,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1, -1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  0, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  0, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  0,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  0,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  0,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  1,  2,  2}));

		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -2,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2, -1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  0, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  0, -1}));
//		assertFalse(isValidHadamardVector(new int[]{2,  2,  0,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  0,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  0,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  1, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  1, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  1,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  1,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  1,  2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  2, -2}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  2, -1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  2,  0}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  2,  1}));
		assertFalse(comesFromValidHadamardVector(new int[]{2,  2,  2,  2}));
	}

	static boolean comesFromValidHadamardVector(final int[] vector) {
		final int[] hadamards = Utility.fastSylvesterTransform(vector);
		for(final int hadamard : hadamards) {
			if(hadamard != 0 && hadamard != vector.length) {
				return false;
			}
		}
		return true;
	}
}
