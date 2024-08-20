package consistency;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class ConsistencyInternalsTest {
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

	void testAllPairsOfBinaryConstraintsWithAllVectorsOfSize(final int numberOfDecisionsInProblem) {
		final Vector<ConsistencyConstraint> allExactlyOneOfTwo = ConsistencyConstraint.newAllExactlyOneOfTwo(numberOfDecisionsInProblem);

		for(final ConsistencyConstraint constraintA : allExactlyOneOfTwo) {
			for(final ConsistencyConstraint constraintB : allExactlyOneOfTwo) {
				if(constraintA == constraintB) { // guards against case where they get merged
					continue;
				}
				if(constraintA.sortedAndUniqueDecisionIds[0] > constraintB.sortedAndUniqueDecisionIds[0]) {
					continue;
				}
				for(int numberOfTruesInProblem = 0; numberOfTruesInProblem <= numberOfDecisionsInProblem / 2; numberOfTruesInProblem++) {
//					if(constraintB.sortedAndUniqueDecisionIds[0] != 2 || constraintB.sortedAndUniqueDecisionIds[1] != 4 || numberOfTruesInProblem != 4) {
//						continue;
//					}
					System.out.println(constraintA + " " + constraintB + " #trues " + numberOfTruesInProblem);

					final SimpleHashSet<ConsistencyConstraint> consistencyConstraints = SimpleHashSet.makeHashSet(constraintA, constraintB);
					final DomainGenerator domainGenerator = DomainGenerator.boundedDomainGenerator(numberOfTruesInProblem, numberOfDecisionsInProblem);
					final ConsistencyProblem consistencyProblem = new ConsistencyProblem(numberOfDecisionsInProblem, consistencyConstraints, domainGenerator, null);
					final ConsistencyInternals internal = new ConsistencyInternals(consistencyProblem);
					internal.mergeBottomUp();
//					System.out.println(internal.simultaneousButterfly);

					for(long bits = 0; bits < (1L << numberOfDecisionsInProblem); bits++) {
						// TODO: move this to solution butterfly?
//						System.out.println(constraintB + " " + bits);
						final int[] rootHadamards = Utility.toInts(bits, numberOfDecisionsInProblem);
//						internal.equationButterflies[1].printMatching(ints);
						testAssertEquals(
							internal.solutionButterfly.areValidRootHadamards(rootHadamards), // fails
//							internal.equationButterflies[0].areValidRootHadamards(rootHadamards) && internal.equationButterflies[1].areValidRootHadamards(rootHadamards), // this seems to work
							constraintA.matches(rootHadamards) && constraintB.matches(rootHadamards) && Long.bitCount(bits) == numberOfTruesInProblem,
							constraintA + " " + constraintB + " #trues=" + numberOfTruesInProblem + " N=" + numberOfDecisionsInProblem + ", bits = " + bits
						);
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
