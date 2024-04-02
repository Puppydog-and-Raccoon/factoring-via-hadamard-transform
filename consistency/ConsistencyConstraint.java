package consistency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

public final class ConsistencyConstraint {
	public final int   numberOfTruesInConstraint;
	public final int[] sortedAndUniqueDecisionIds;   // sorting and removing duplicates simplifies several functions
	public final int   numberOfDecisionsInProblem;

	private static Unique<ConsistencyConstraint> unique = new Unique<ConsistencyConstraint>();

	private ConsistencyConstraint(
		final int   numberOfTruesInConstraint,
		final int[] decisionIds,
		final int   numberOfDecisionsInProblem
	) {
		final int[] sortedAndUniqueDecisionIds = Utility.sortAndRemoveDuplicates(decisionIds);

		// validate numbers
		Utility.insist(0 <= numberOfTruesInConstraint, "number of trues must greater than or equal to 0");
		Utility.insist(numberOfTruesInConstraint <= sortedAndUniqueDecisionIds.length, "number of trues " + numberOfTruesInConstraint + " must less than or equal to the number of unique decision ids");
		for(final int decisionId : decisionIds) {
			Utility.insist(0 <= decisionId, "decision id " + decisionId + " must be greater than or equal to 0");
			Utility.insist(decisionId < numberOfDecisionsInProblem, "decision id " + decisionId + " must be less than the number of decisions in problem");
		}
		Utility.insist(Utility.isPowerOfTwo(numberOfDecisionsInProblem), "number of decisons " + numberOfDecisionsInProblem + " must be a power of 2");

		this.numberOfTruesInConstraint  = numberOfTruesInConstraint;
		this.sortedAndUniqueDecisionIds = sortedAndUniqueDecisionIds;
		this.numberOfDecisionsInProblem = numberOfDecisionsInProblem;
	}

	public int[] constraintVector() {
		final int[] rootHadamards = new int[numberOfDecisionsInProblem];   // defaults to 0s
		for(int decisionId : sortedAndUniqueDecisionIds) {
			rootHadamards[decisionId] = 1;
		}
		final int[] leafHadamards = Utility.fastSylvesterTransform(rootHadamards);
		return leafHadamards;
	}

	public int totalSum() {
		return numberOfTruesInConstraint * numberOfDecisionsInProblem;
	}

	public boolean isConstant() {
		return numberOfTruesInConstraint == 0 || numberOfTruesInConstraint == sortedAndUniqueDecisionIds.length;
	}

	@Override
	public String toString() {
		return "[Constraint: " + numberOfTruesInConstraint + " of " + Arrays.toString(sortedAndUniqueDecisionIds) + " in " + numberOfDecisionsInProblem + "]";
	}

	@Override
	public boolean equals(final Object otherObject) {
		return otherObject                != null
			&& getClass()                 == otherObject.getClass()
			&& numberOfTruesInConstraint  == ((ConsistencyConstraint)otherObject).numberOfTruesInConstraint
			&& Arrays.equals(sortedAndUniqueDecisionIds, ((ConsistencyConstraint)otherObject).sortedAndUniqueDecisionIds)
			&& numberOfDecisionsInProblem == ((ConsistencyConstraint)otherObject).numberOfDecisionsInProblem;
	}

	@Override
	public int hashCode() {
		int hashCode = numberOfTruesInConstraint;
		int rotation = 7;

		for(final int decisionId : sortedAndUniqueDecisionIds) {
			hashCode ^= Integer.rotateLeft(decisionId, rotation);
			rotation = (rotation + 7) % Integer.SIZE;
		}
		hashCode ^= Integer.rotateLeft(numberOfDecisionsInProblem, rotation);

		return hashCode;
	}

	boolean matches(final boolean[] decisions) {
		int numberOfTruesinDecisions = 0;
		for(final int decisionId : sortedAndUniqueDecisionIds) {
			numberOfTruesinDecisions += decisions[decisionId] ? 1 : 0;
		}
		return numberOfTruesinDecisions == numberOfTruesInConstraint;
	}

	// TODO: move?? used for testing
	static Vector<ConsistencyConstraint> newAllExactlyOneOfTwo(final int N) {
		final Vector<ConsistencyConstraint> constraints = new Vector<ConsistencyConstraint>();
		for(int a = 0; a < N; a++) {
			for(int b = 0; b < N; b++) {
				if(a < b) {
					constraints.add(exactlyOneOfTwo(a, b, N));
				}
			}
		}
		return constraints;
	}

	public static ConsistencyConstraint exactlyZeroOfOne(
		final int decisionId,
		final int numberOfDecisionsInProblem
	) {
		final int   numbersOfTrues = 0;
		final int[] decisionIds    = new int[]{decisionId};
		return unique.unique(new ConsistencyConstraint(numbersOfTrues, decisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint exactlyOneOfTwo(
		final int decisionId0,
		final int decisionId1,
		final int numberOfDecisionsInProblem
	) {
		final int   numbersOfTrues = 1;
		final int[] decisionIds    = new int[]{decisionId0, decisionId1};
		return unique.unique(new ConsistencyConstraint(numbersOfTrues, decisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint exactlyOneOfThree(
		final int decisionId0,
		final int decisionId1,
		final int decisionId2,
		final int numberOfDecisionsInProblem
	) {
		final int   numbersOfTrues = 1;
		final int[] decisionIds    = new int[]{decisionId0, decisionId1, decisionId2};
		return unique.unique(new ConsistencyConstraint(numbersOfTrues, decisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint exactlyTwoOfThree(
		final int decisionId0,
		final int decisionId1,
		final int decisionId2,
		final int numberOfDecisionsInProblem
	) {
		final int   numbersOfTrues = 2;
		final int[] decisionIds    = new int[]{decisionId0, decisionId1, decisionId2};
		return unique.unique(new ConsistencyConstraint(numbersOfTrues, decisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint exactlyOneOf(
		final HashSet<Integer> setOfDecisionIds,
		final int              numberOfDecisionsInProblem
	) {
		return exactlyNOf(1, setOfDecisionIds, numberOfDecisionsInProblem);
	}

	public static ConsistencyConstraint exactlyNOf(
		final int              n,
		final HashSet<Integer> setOfDecisionIds,
		final int              numberOfDecisionsInProblem
	) {
		final int   numbersOfTrues     = n;
		final int[] arrayOfDecisionIds = Utility.toArrayFromSet(setOfDecisionIds);
		return unique.unique(new ConsistencyConstraint(numbersOfTrues, arrayOfDecisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint exactlyMOfFirstN(
		final int m,
		final int n,
		final int numberOfDecisionsInProblem
	) {
		final int numberOfTrues = m;
		final int[] decisionIds = new int[n];
		for(int i = 0; i < n; i++) {
			decisionIds[i] = i;
		}
		return unique.unique(new ConsistencyConstraint(numberOfTrues, decisionIds, numberOfDecisionsInProblem));
	}
}
