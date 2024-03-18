package applications;

import static org.junit.Assert.*;

import org.junit.Test;

public class FactoringTest {
	@Test
	public void test_01() {
		factor("01", "1", "1");
	}

	@Test
	public void test_10() {
		factor("10", null, null);
	}

	@Test
	public void test_11() {
		factor("11", null, null);
	}

	@Test
	public void test_0001() {
 		factor("0001", "01", "01");
 	}

	@Test
	public void test_0010() {
		factor("0010", "01", "10");
	}

	@Test
	public void test_0011() {
		factor("0011", "01", "11");
	}

	@Test
	public void test_0100() {
		factor("0100", "10", "10");
	}

	@Test
	public void test_0101() {
		factor("0101", null, null);
	}

	@Test
	public void test_0110() {
		factor("0110", "10", "11");
	}

	@Test
	public void test_0111() {
		factor("0111", null, null);
	}

	@Test
	public void test_1000() {
		factor("1000", null, null);
	}

	@Test
	public void test_1001() {
		factor("1001", "11", "11");
	}

	@Test
	public void test_1010() {
		factor("1010", null, null);
	}

	@Test
	public void test_1011() {
		factor("1011", null, null);
	}

	@Test
	public void test_1100() {
		factor("1100", null, null);
	}

	@Test
	public void test_1101() {
		factor("1101", null, null);
	}

	@Test
	public void test_1110() {
		factor("1110", null, null);
	}

	@Test
	public void test_1111() {
		factor("1111", null, null);
	}

	void factor(
		final String productAsString,
		final String factor0AsString,
		final String factor1AsString
	) {
		System.out.println(productAsString);

		final FactoringNumber product    = new FactoringNumber(productAsString);
		final FactoringNumber factor0    = factor0AsString == null ? null : new FactoringNumber(factor0AsString);
		final FactoringNumber factor1    = factor1AsString == null ? null : new FactoringNumber(factor1AsString);

		final FactoringProblem  problem  = new FactoringProblem(product);
		final FactoringSolution solution = FactoringAlgorithm.solve(problem);

		assertTrue(FactoringNumber.isEqual(factor0, solution.factor0) && FactoringNumber.isEqual(factor1, solution.factor1)
				|| FactoringNumber.isEqual(factor0, solution.factor1) && FactoringNumber.isEqual(factor1, solution.factor0));
	}
}
