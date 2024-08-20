package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

//this class tests the internals of the algorithm
//it uses the high-level consistency interface, but the
//intent is to test individual and pairs of butterflies

public class EquationButterflyTrinaryTest {
	@Test
	public void test_4() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(4);
		testAllTwoOfThreeConstraintsWithAllVectorsOfSize(4);
	}

	@Test
	public void test_8() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(8);
		testAllTwoOfThreeConstraintsWithAllVectorsOfSize(8);
	}

	// this takes ~4 hours
//	@Test
	public void test_16() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(16);
		testAllTwoOfThreeConstraintsWithAllVectorsOfSize(16);
	}

//	@Test
	public void test_32() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(32);
		testAllTwoOfThreeConstraintsWithAllVectorsOfSize(32);
	}

	// we discovered that when a == b or b == c, it doesn't work, should never occur
	// we discovered that when nt == 0, it doesn't work, should never occur
	// when nt < number of actual trues, is doesn't need to work
	void testAllOneOfThreeConstraintsWithAllVectorsOfSize(final int N) {
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				for(int c = 0; c < N; c++) {
					if(a < b && b < c) {
						for(int nt = 1; nt <= N / 2; nt++) {
							System.out.println(a + " " + b + " " + c + " " + nt + " " + N);
							final PropertyButterfly propertyButterfly = PropertyButterfly.make(N, DomainGenerator.boundedDomainGenerator(nt, N));
							final ConsistencyConstraint constraint = ConsistencyConstraint.exactlyOneOfThree(a, b, c, N);
							final EquationButterfly equationButterfly = new EquationButterfly(propertyButterfly, constraint);
							equationButterfly.fill();

//							System.out.println(equationButterfly);
							for(long bits = 0; bits < (1L << N); bits++) {
								final int numberOfOnes = Long.bitCount(bits);
								final int[] rootHadamards = Utility.toInts(bits, N);
								testAssertEquals(
									equationButterfly.areValidRootHadamards(rootHadamards),
									rootHadamards[a] + rootHadamards[b] + rootHadamards[c] == 1 && numberOfOnes == nt,
									"a = " + a + ", b = " + b + ", c = " + c + ", t = " + nt + ", N = " + N + ", bits = " + bits
								);
							}
						}
					}
				}
			}
		}
	}

	void testAllTwoOfThreeConstraintsWithAllVectorsOfSize(final int N) {
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				for(int c = 0; c < N; c++) {
					if(a < b && b < c) {
						for(int nt = 1; nt <= N / 2; nt++) {
							System.out.println(a + " " + b + " " + c + " " + nt);
							final PropertyButterfly propertyButterfly = PropertyButterfly.make(N, DomainGenerator.boundedDomainGenerator(nt, N));
							final ConsistencyConstraint constraint = ConsistencyConstraint.exactlyTwoOfThree(a, b, c, N);
							final EquationButterfly equationButterfly = new EquationButterfly(propertyButterfly, constraint);
							equationButterfly.fill();

//							System.out.println(equationButterfly);
							for(long bits = 0; bits < (1L << N); bits++) {
								final int numberOfOnes = Long.bitCount(bits);
								final int[] rootHadamards = Utility.toInts(bits, N);
								testAssertEquals(
									equationButterfly.areValidRootHadamards(rootHadamards),
									rootHadamards[a] + rootHadamards[b] + rootHadamards[c] == 2 && numberOfOnes == nt,
									"a = " + a + ", b = " + b + ", c = " + c + ", t = " + nt + ", N = " + N + ", bits = " + bits
								);
							}
						}
					}
				}
			}
		}
	}

	void testAssertEquals(final boolean isValid, final boolean shouldBeValid, final String description) {
		final String errorMessage = "should be " + shouldBeValid + ": " + description;
		if(shouldBeValid != isValid) {
			System.out.println(errorMessage);
		}
		assertEquals(errorMessage, isValid, shouldBeValid);
	}
}
