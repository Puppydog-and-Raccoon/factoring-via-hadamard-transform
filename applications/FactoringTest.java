package applications;

import static org.junit.Assert.*;

import org.junit.Test;

import consistency.Utility;

public class FactoringTest {
	@Test
	public void test_0001() {
		testFactoring(1, 4);
 	}

	@Test
	public void test_0010() {
		testFactoring(2, 4);
	}

	@Test
	public void test_0011() {
		testFactoring(3, 4);
	}

	@Test
	public void test_0100() {
		testFactoring(4, 4);
	}

	@Test
	public void test_0101() {
		testFactoring(5, 4);
	}

	@Test
	public void test_0110() {
		testFactoring(6, 4);
	}

	@Test
	public void test_0111() {
		testFactoring(7, 4);
	}

	@Test
	public void test_1000() {
		testFactoring(8, 4);
	}

	@Test
	public void test_1001() {
		testFactoring(9, 4);
	}

	@Test
	public void test_1010() {
		testFactoring(10, 4);
	}

	@Test
	public void test_1011() {
		testFactoring(11, 4);
	}

	@Test
	public void test_1100() {
		testFactoring(12, 4);
	}

	@Test
	public void test_1101() {
		testFactoring(13, 4);
	}

	@Test
	public void test_1110() {
		testFactoring(14, 4);
	}

	@Test
	public void test_1111() {
		testFactoring(15, 4);
	}

//	@Test
	public void test_00000001() {
		testFactoring(1, 8);
	}

//	@Test
	public void test_10001111() {
		testFactoring(11 * 13, 8);
	}

	void testFactoring(final int productAsInt, final int numberOfBitsPerProduct) {
		Utility.insist(Utility.isPowerOfTwo(numberOfBitsPerProduct), "number of bits per product must be a power of 2");
		Utility.insist(numberOfBitsPerProduct >= 4, "number of bits per product must be at least 4");
		Utility.insist(productAsInt < (1 << numberOfBitsPerProduct), "product must fit in number of bits");

		final FactoringNumber     product            = new FactoringNumber(productAsInt, numberOfBitsPerProduct);
		final FactoringSolution[] factoringSolutions = FactoringNumber.pairsOfFactors(productAsInt, numberOfBitsPerProduct / 2);

		final FactoringProblem  factoringProblem  = new FactoringProblem(product);
		final FactoringSolution factoringSolution = FactoringAlgorithm.solve(factoringProblem);

		assertTrue(factoringSolution.matchesOne(factoringSolutions));
	}
}
