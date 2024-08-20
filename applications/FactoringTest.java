package applications;

import static org.junit.Assert.*;

import org.junit.Test;

import consistency.Utility;

public class FactoringTest {
	// In ConsistencyInternals, set
	// final static boolean SHORT_CIRCUIT_CONSTANTS = false;
	@Test
	public void testTwoBitNumbers_01() {
		testFactoring( 1, 2);
 	}

	// In ConsistencyInternals, set
	// final static boolean SHORT_CIRCUIT_CONSTANTS = false;
	@Test
	public void testTwoBitNumbers_10() {
		testFactoring( 1, 2);
 	}

	// In ConsistencyInternals, set
	// final static boolean SHORT_CIRCUIT_CONSTANTS = false;
	@Test
	public void testTwoBitNumbers_11() {
		testFactoring( 1, 2);
 	}

	// takes 10 seconds
//	@Test
	public void testFourBitNumbers_01() {
		testFactoring( 1, 4);
 	}

//	@Test
	public void testFourBitNumbers_02() {
		testFactoring( 2, 4);
	}

//	@Test
	public void testFourBitNumbers_03() {
		testFactoring( 3, 4);
	}

//	@Test
	public void testFourBitNumbers_04() {
		testFactoring( 4, 4);
 	}

//	@Test
	public void testFourBitNumbers_05() {
		testFactoring( 5, 4);
	}

//	@Test
	public void testFourBitNumbers_06() {
		testFactoring( 6, 4);
	}

//	@Test
	public void testFourBitNumbers_07() {
		testFactoring( 7, 4);
	}

//	@Test
	public void testFourBitNumbers_08() {
		testFactoring( 8, 4);
	}

//	@Test
	public void testFourBitNumbers_09() {
		testFactoring( 9, 4);
	}

//	@Test
	public void testFourBitNumbers_10() {
		testFactoring(10, 4);
	}

//	@Test
	public void testFourBitNumbers_11() {
		testFactoring(11, 4);
	}

//	@Test
	public void testFourBitNumbers_12() {
		testFactoring(12, 4);
	}

//	@Test
	public void testFourBitNumbers_13() {
		testFactoring(13, 4);
	}

//	@Test
	public void testFourBitNumbers_14() {
		testFactoring(14, 4);
	}

//	@Test
	public void testFourBitNumbers_15() {
		testFactoring(15, 4);
 	}

//	@Test
	public void test_00000001() {
		testFactoring(1, 8);
	}

//	@Test
	public void test_017_in_8() {
		testFactoring(17, 8);
	}

//	// primes: 17, 251
	// composites: 18, 24, 225

	// about 3.5 hours
//	@Test
	public void test_10001111() {
		testFactoring(11 * 13, 8);
	}

	void testFactoring(final int productAsInt, final int numberOfBitsPerProduct) {
		Utility.insist(Utility.isPowerOfTwo(numberOfBitsPerProduct), "number of bits per product must be a power of 2");
//		Utility.insist(numberOfBitsPerProduct >= 4, "number of bits per product must be at least 4");
		Utility.insist(productAsInt < (1 << numberOfBitsPerProduct), "product must fit in number of bits");

//		System.out.println("solving " + productAsInt);
		final FactoringNumber   product                 = new FactoringNumber(productAsInt, numberOfBitsPerProduct);
		final FactoringProblem  factoringProblem        = new FactoringProblem(product);
		final FactoringSolution actualFactoringSolution = factoringProblem.solve();
//		System.out.println("computed solution " + actualFactoringSolution);

		final FactoringSolution[] possibleFactoringSolutions = FactoringNumber.pairsOfFactors(productAsInt, numberOfBitsPerProduct / 2);
		for(final FactoringSolution possibleFactoringSolution : possibleFactoringSolutions) {
			System.out.println("given solution " + possibleFactoringSolution);
		}

		assertTrue(actualFactoringSolution.equalsAny(possibleFactoringSolutions));
	}
}
