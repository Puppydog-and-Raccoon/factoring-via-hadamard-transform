package consistency;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class RelationButterflyBinaryTest {
	@Test
	public void test_2() {
		testAllSingleBinaryConstraintsWithAllVectorsOfSize(2);
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
		final PositionButterfly positionButterfly = new PositionButterfly(N, null);
		final Vector<ConsistencyConstraint> allExactlyOneOfTwo = ConsistencyConstraint.newAllExactlyOneOfTwo(N);
		for(final ConsistencyConstraint constraint : allExactlyOneOfTwo) {
			for(int numberOfTrues = 0; numberOfTrues <= N / 2; numberOfTrues++) {
				System.out.println(constraint + " and number of trues = " + numberOfTrues);
				final SolutionButterfly solutionButterfly = new SolutionButterfly(positionButterfly);
				final RelationButterfly relationButterfly = RelationButterfly.newRelationButterfly(positionButterfly, constraint, numberOfTrues, solutionButterfly);
				for(long bits = 0; bits < (1L << N); bits++) {
					// TODO: move this to solution butterfly?
					final int[] ints = Utility.toInts(bits, N);
					final boolean[] booleans = Utility.toBooleans(ints);
					testAssertEquals(
						relationButterfly.isValid(ints),
						constraint.matches(booleans) && Long.bitCount(bits) == numberOfTrues,
						constraint + ", number of trues = " + numberOfTrues + ", N = " + N + ", bits = " + bits
					);
				}
			}
		}
	}

	@Test
	public void test_pairs_2() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(2);
	}

	@Test
	public void test_pairs_4() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(4);
	}

	@Test
	public void test_pairs_8() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(8);
	}

//	@Test
	public void test_pairs_16() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(16);
	}

//	@Test
	public void test_pairs_32() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(32);
	}

	void testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(final int N) {
		final PositionButterfly positionButterfly = new PositionButterfly(N, null);
		final Vector<ConsistencyConstraint> allExactlyOneOfTwo = ConsistencyConstraint.newAllExactlyOneOfTwo(N);
		for(final ConsistencyConstraint constraintA : allExactlyOneOfTwo) {
			for(final ConsistencyConstraint constraintB : allExactlyOneOfTwo) {
				if(constraintA.sortedAndUniqueDecisionIds[0] <= constraintB.sortedAndUniqueDecisionIds[0]) {
					for(int numberOfTrues = 0; numberOfTrues <= N / 2; numberOfTrues++) {
						System.out.println(constraintA + " " + constraintB + " #trues = " + numberOfTrues);
						final SolutionButterfly solutionButterfly = new SolutionButterfly(positionButterfly);
						final RelationButterfly relationButterflyA = RelationButterfly.newRelationButterfly(positionButterfly, constraintA, numberOfTrues, solutionButterfly);
						final RelationButterfly relationButterflyB = RelationButterfly.newRelationButterfly(positionButterfly, constraintB, numberOfTrues, solutionButterfly);
						for(long bits = 0; bits < (1L << N); bits++) {
							// TODO: move this to solution butterfly?
							final int[] ints = Utility.toInts(bits, N);
							final boolean[] booleans = Utility.toBooleans(ints);
							testAssertEquals(
								relationButterflyA.isValid(ints) && relationButterflyB.isValid(ints),
								constraintA.matches(booleans) && constraintB.matches(booleans) && Long.bitCount(bits) == numberOfTrues,
								constraintA + ", " + constraintB + ", #trues = " + numberOfTrues + ", N = " + N + ", bits = " + bits
							);
						}
					}
				}
			}
		}
	}

	// TODO: move!
	static void testAssertEquals(final boolean isValid, final boolean shouldBeValid, final String description) {
		final String errorMessage = "should be " + shouldBeValid + ": " + description;
		if(shouldBeValid != isValid) {
//			System.out.println(errorMessage);
		} else {
//			System.out.println(" okay " + description);
		}
		assertEquals(errorMessage, shouldBeValid, isValid);
	}
}
