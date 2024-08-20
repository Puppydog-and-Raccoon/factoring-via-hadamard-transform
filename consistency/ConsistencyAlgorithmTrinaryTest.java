package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

// test the internals of the algorithm using the high-level consistency interface,
// the intent is to test individual and pairs of butterflies

public class ConsistencyAlgorithmTrinaryTest {
	@Test
	public void test_one_of_size_2() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(2);
	}

	@Test
	public void test_one_of_size_4() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(4);
	}

	@Test
	public void test_one_of_size_8() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(8);
	}

	@Test
	public void test_one_of_size_16() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(16);
	}

//	@Test
	public void test_one_of_size_32() {
		testAllOneOfThreeConstraintsWithAllVectorsOfSize(32);
	}

	void testAllOneOfThreeConstraintsWithAllVectorsOfSize(final int N) {
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				for(int c = 0; c < N; c++) {
					if(a < b && b < c) {
						for(int nt = 1; nt <= N / 2; nt++) {
							System.out.println(a + " " + b + " " + c + " " + nt);
							final ConsistencyConstraint constraintABC = ConsistencyConstraint.exactlyOneOfThree(a, b, c, N);
							final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
							constraints.add(constraintABC);
							final DomainGenerator domainGenerator = DomainGenerator.boundedDomainGenerator(nt, N);
							final ConsistencyProblem consistencyProblem = new ConsistencyProblem(N, constraints, domainGenerator, null);
							final ConsistencyInternals consistencyInternals = new ConsistencyInternals(consistencyProblem);
							consistencyProblem.solve();

							for(long bits = 0; bits < (1L << N); bits++) {
								final int numberOfTruesInVector = Long.bitCount(bits);
								final int[] vector = Utility.toInts(bits, N);
								testAssertEquals(
									consistencyInternals.isValidSolution(vector),
									constraintABC.matches(vector) && numberOfTruesInVector == nt,
									"a = " + a + ", b = " + b + ", c = " + c + ", nt = " + nt + ", N = " + N + ", bits = " + bits
								);
							}
						}
					}
				}
			}
		}
	}

	@Test
	public void test_pairs_2() {
		testAllPairsOfConstraintsAndAllVectorsOfSize(2);
	}

	@Test
	public void test_pairs_4() {
		testAllPairsOfConstraintsAndAllVectorsOfSize(4);
	}

	@Test
	public void test_pairs_8() {
		testAllPairsOfConstraintsAndAllVectorsOfSize(8);
	}

//	@Test
	public void test_pairs_16() {
		testAllPairsOfConstraintsAndAllVectorsOfSize(16);
	}

//	@Test
	public void test_pairs_32() {
		testAllPairsOfConstraintsAndAllVectorsOfSize(32);
	}

	public void testAllPairsOfConstraintsAndAllVectorsOfSize(final int N) {
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				for(int c = 0; c < N; c++) {
					for(int d = 0; d < N; d++) {
						if(a <= b && c <= d) {
							for(int nt = 1; nt <= N / 2; nt++) {
								System.out.println(a + " " + b + " " + c + " " + d + " " + nt);
								final ConsistencyConstraint constraintAB = ConsistencyConstraint.exactlyOneOfTwo(a, b, N);
								final ConsistencyConstraint constraintCD = ConsistencyConstraint.exactlyOneOfTwo(c, d, N);
								final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
								constraints.add(constraintAB);
								constraints.add(constraintCD);
								final DomainGenerator domainGenerator = DomainGenerator.boundedDomainGenerator(nt, N);
								final ConsistencyProblem consistencyProblem = new ConsistencyProblem(N, constraints, domainGenerator, null);
								final ConsistencyInternals consistencyInternals = new ConsistencyInternals(consistencyProblem);
								consistencyProblem.solve();

								for(long bits = 0; bits < (1L << N); bits++) {
									final int[] vector = Utility.toInts(bits, N);
									final int numberOfTruesInVector = Long.bitCount(bits);
	
									String bitsString = Long.toHexString(bits);
									testAssertEquals(
										consistencyInternals.isValidSolution(vector),
										constraintAB.matches(vector) && constraintCD.matches(vector) && nt == numberOfTruesInVector,
										"a = " + a + ", b = " + b + ", c = " + c + ", d = " + d + ", N = " + N + ", bits = " + bitsString
									);
								}
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
//			System.out.println(errorMessage);
		}
		assertEquals(errorMessage, isValid, shouldBeValid);
	}
}
