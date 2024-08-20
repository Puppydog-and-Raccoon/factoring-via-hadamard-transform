package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class EquationButterflyBinaryTest {
	@Test
	public void test_2() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(4);
	}

	@Test
	public void test_4() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(4);
	}

	@Test
	public void test_8() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(8);
	}

//	@Test
	public void test_16() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(16);
	}

//	@Test
	public void test_32() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(32);
	}

	void testAllSingleBinaryConstraintsWithAllVectorsOfSize(final int N) {
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				if(a < b) {
					for(int numberOfTruesInProblem = 0; numberOfTruesInProblem <= N / 2; numberOfTruesInProblem++) {
						System.out.println(a + " " + b + " " + numberOfTruesInProblem);
						final ConsistencyConstraint constraint = ConsistencyConstraint.exactlyOneOfTwo(a, b, N);
						final PropertyButterfly propertyButterfly = PropertyButterfly.make(N, DomainGenerator.boundedDomainGenerator(numberOfTruesInProblem, N));
						final EquationButterfly equationButterfly = new EquationButterfly(propertyButterfly, constraint);
						equationButterfly.fill();

						for(long bits = 0; bits < (1L << N); bits++) {
							testAssertEquals(
								equationButterfly.areValidRootHadamards(Utility.toInts(bits, N)),
								(((bits >>> a) & 1) + ((bits >>> b) & 1)) == 1 && Long.bitCount(bits) == numberOfTruesInProblem,
								"a = " + a + ", b = " + b + ", t = " + numberOfTruesInProblem + ", N = " + N + ", bits = " + bits
							);
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
//		} else {
//			System.out.println(" okay " + description);
		}
		assertEquals(errorMessage, isValid, shouldBeValid);
	}
}
