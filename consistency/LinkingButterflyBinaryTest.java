package consistency;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

public class LinkingButterflyBinaryTest {
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

	// ran 50% in 24 hours
	@Test
	public void test_pairs_16() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(16);
	}

//	@Test
	public void test_pairs_32() {
		testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(32);
	}

	void testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(final int numberOfDecisionsInProblem) {
		final Vector<ConsistencyConstraint> allExactlyOneOfTwo = ConsistencyConstraint.newAllExactlyOneOfTwo(numberOfDecisionsInProblem);
		for(final ConsistencyConstraint constraintA : allExactlyOneOfTwo) {
			for(final ConsistencyConstraint constraintB : allExactlyOneOfTwo) {
				if(constraintA.sortedAndUniqueDecisionIds[0] <= constraintB.sortedAndUniqueDecisionIds[0]) {
					for(int numberOfTruesInProblem = 0; numberOfTruesInProblem <= numberOfDecisionsInProblem / 2; numberOfTruesInProblem++) {
						System.out.println(constraintA + " " + constraintB + " #trues = " + numberOfTruesInProblem);

//						final ConsistencyProblem consistencyProblem = new ConsistencyProblem();
						final PropertyButterfly propertyButterfly = PropertyButterfly.make(numberOfDecisionsInProblem, DomainGenerator.boundedDomainGenerator(numberOfTruesInProblem, numberOfDecisionsInProblem));
						final EquationButterfly equationButterflyA = new EquationButterfly(propertyButterfly, constraintA);
						equationButterflyA.fill();
						final EquationButterfly equationButterflyB = new EquationButterfly(propertyButterfly, constraintB);
						equationButterflyB.fill();
						final SolutionButterfly solutionButterfly = new SolutionButterfly(propertyButterfly);
						final LinkingButterfly linkingButterfly = new LinkingButterfly(equationButterflyA, equationButterflyB, solutionButterfly, true);
						linkingButterfly.fill();
//						System.out.println("before wring");
						linkingButterfly.wringUntilNoChange();
//						System.out.println("after wring");

						for(long bits = 0; bits < (1L << numberOfDecisionsInProblem); bits++) {
							// TODO: move this to solution butterfly?
							final int[] ints = Utility.toInts(bits, numberOfDecisionsInProblem);
//							System.out.println(Arrays.toString(ints));
							final boolean[] booleans = Utility.toBooleans(ints);
							testAssertEquals(
								linkingButterfly.isValidRootHadamards(ints), // this seems to work
								// equationButterflyA.isValidRootVector(ints) && equationButterflyB.isValidRootVector(ints), // this seems to work
								constraintA.matches(booleans) && constraintB.matches(booleans) && Long.bitCount(bits) == numberOfTruesInProblem, // this seems to work
								constraintA + " " + constraintB + " #trues=" + numberOfTruesInProblem + " N=" + numberOfDecisionsInProblem + ", bits = " + bits
							);
						}
					}
				}
			}
		}
	}

	// TODO: move to utility?
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
