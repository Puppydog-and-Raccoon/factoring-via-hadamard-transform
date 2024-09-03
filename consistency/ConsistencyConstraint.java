package consistency;

import java.util.Arrays;
import java.util.Vector;

/**
 * A constraint defines a given set of decisions and some number of them must be true.
 * 
 * Current applications require that there must be exactly one number of decisions must be true.
 * However, the original applications required either zero or one of them to be true,
 * and this class can represent any number of numbers of trues.
 * 
 * The factory methods that create the constraints are arbitrary and defined
 * for convenience of applications. They should be expanded as applications require.
 * Also, factory methods enable disallowing duplicate constraint objects.
 * 
 * NOTE: Sorting and removing duplicates from internal arrays simplifies several methods.
 * NOTE: The constraint vector is a constant, to avoid computing it repeatedly.
 * NOTE: Decision ids is a set, because duplicate decision ids would make the number of trues would be ambiguous.
 */
public final class ConsistencyConstraint {
	public final int[] sortedAndUniqueNumbersOfTrues;
	public final int[] sortedAndUniqueDecisionIds;
	public final int[] constraintVector;
	public final int   numberOfDecisionsInProblem;

	private static Canonicalizer<ConsistencyConstraint> canonicalizer = new Canonicalizer<ConsistencyConstraint>();

    /**
     * Constructor.
     * 
     * @param numbersOfTrues a set of the valid numbers of trues of the chosen decisions, often just one number
     * @param decisionIds a set of the decision ids to consider
     * @param numberOfDecisionsInProblem the number of decisions in the problem, used to make the constraint vector
     */
	private ConsistencyConstraint(
		final SimpleHashSet<Integer> numbersOfTrues,
		final SimpleHashSet<Integer> decisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		final int[] sortedAndUniqueNumbersOfTrues = numbersOfTrues.toSortedIntArray();
		final int[] sortedAndUniqueDecisionIds    = decisionIds.toSortedIntArray();
		final int[] constraintVector              = makeConstraintVector(sortedAndUniqueDecisionIds, numberOfDecisionsInProblem);

		validate(sortedAndUniqueNumbersOfTrues, sortedAndUniqueDecisionIds, numberOfDecisionsInProblem);

		this.sortedAndUniqueNumbersOfTrues = sortedAndUniqueNumbersOfTrues;
		this.sortedAndUniqueDecisionIds    = sortedAndUniqueDecisionIds;
		this.constraintVector              = constraintVector;
		this.numberOfDecisionsInProblem    = numberOfDecisionsInProblem;
	}

    /**
     * Validates the constructor parameters. Errors throw an exception.
     * 
     * @param sortedAndUniqueNumbersOfTrues an array of valid numbers of trues of the chosen decisions
     * @param sortedAndUniqueDecisionIds an array of the decisions to consider
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     */
	private static void validate(
		final int[] sortedAndUniqueNumbersOfTrues,
		final int[] sortedAndUniqueDecisionIds,
		final int   numberOfDecisionsInProblem
	) {
		Utility.insist(sortedAndUniqueNumbersOfTrues.length > 0, "must have at least one number of trues");
		Utility.insist(Utility.sortedAndUnique(sortedAndUniqueNumbersOfTrues), "numbers of trues must be sorted and unique");
		for(final int numberOfTrues : sortedAndUniqueNumbersOfTrues) {
			Utility.insistInRange(0, numberOfTrues, sortedAndUniqueDecisionIds.length);
		}

		Utility.insist(sortedAndUniqueDecisionIds.length > 0, "must have at least one decision id");
		Utility.insist(Utility.sortedAndUnique(sortedAndUniqueDecisionIds), "decision ids must be sorted and unique");
		for(final int decisionId : sortedAndUniqueDecisionIds) {
			Utility.insistInRange(0, decisionId, numberOfDecisionsInProblem - 1);
		}

		Utility.insistIsPowerOfTwo(numberOfDecisionsInProblem);
	}

    /**
     * Makes the constraint vector. The constraint vector is the Hadamard transform
     * of the binary vector with all chosen decision ids set to 1.
     * The constraint vector will form the dot-product with the unknown vector
     * to define the partial sums of the equation butterfly.
     * 
     * @param sortedAndUniqueDecisionIds an array of the decisions to consider
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint vector
     */
	private static int[] makeConstraintVector(
		final int[] sortedAndUniqueDecisionIds,
		final int   numberOfDecisionsInProblem
	) {
		final int[] rootHadamards = new int[numberOfDecisionsInProblem];   // defaults to 0s
		for(int decisionId : sortedAndUniqueDecisionIds) {
			rootHadamards[decisionId] = 1;
		}
		final int[] leafHadamards = Utility.fastSylvesterTransform(rootHadamards);
		return leafHadamards;
	}

    /**
     * Determine whether the chosen decisions must have constant values or not.
     * 
     * @return whether the decisions have constant values
     */
	public boolean isConstant() {
		return sortedAndUniqueNumbersOfTrues.length == 1
			&& (sortedAndUniqueNumbersOfTrues[0] == 0 || sortedAndUniqueNumbersOfTrues[0] == sortedAndUniqueDecisionIds.length);
	}

    /**
     * When the constraint is a constant, determines what the constant value is.
     * Used in garbage can packing.
     * 
     * @return the boolean value of the constant
     */
	public boolean valueOfConstant() {
		return sortedAndUniqueNumbersOfTrues[0] != 0;
	}

    /**
     * Determines whether the decisions argument satisfies the constraint.
     * 
     * @param decisions a boolean array of decisions
     * @return whether the decisions satisfy the constraint
     */
	boolean matches(final boolean[] decisions) {
		int population = 0;
		for(final int decisionId : sortedAndUniqueDecisionIds) {
			population += Utility.toInt(decisions[decisionId]);
		}
		return Utility.contains(population, sortedAndUniqueNumbersOfTrues);
	}

    /**
     * Determines whether the decisions argument satisfies the constraint.
     * 
     * @param decisions an int array of decisions
     * @return whether the decisions satisfy the constraint
     */
	boolean matches(final int[] decisions) {
		int population = 0;
		for(final int decisionId : sortedAndUniqueDecisionIds) {
			Utility.insistInRange(0, decisions[decisionId], 1);
			population += decisions[decisionId];
		}
		return Utility.contains(population, sortedAndUniqueNumbersOfTrues);
	}

	// -----------------------------------------------------------------------
	// factory methods

    /**
     * Create a constraint, where the given decision must be false.
     * 
     * @param decisionId the given decision
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyZeroOfOne(
		final int decisionId,
		final int numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(0));
		final SimpleHashSet<Integer> setOfDecisionIds    = SimpleHashSet.makeHashSet(new Integer(decisionId));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where exactly one of the two given decisions must be true.
     * 
     * @param decisionId0 the first given decision
     * @param decisionId1 the second given decision
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyOneOfTwo(
		final int decisionId0,
		final int decisionId1,
		final int numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(1));
		final SimpleHashSet<Integer> setOfDecisionIds    = SimpleHashSet.makeHashSet(new Integer(decisionId0), new Integer(decisionId1));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where exactly one of the three given decisions must be true.
     * 
     * @param decisionId0 the first given decision
     * @param decisionId1 the second given decision
     * @param decisionId2 the third given decision
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyOneOfThree(
		final int decisionId0,
		final int decisionId1,
		final int decisionId2,
		final int numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(1));
		final SimpleHashSet<Integer> setOfDecisionIds    = SimpleHashSet.makeHashSet(new Integer(decisionId0), new Integer(decisionId1), new Integer(decisionId2));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where exactly two of the three given decisions must be true.
     * 
     * @param decisionId0 the first given decision
     * @param decisionId1 the second given decision
     * @param decisionId2 the third given decision
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyTwoOfThree(
		final int decisionId0,
		final int decisionId1,
		final int decisionId2,
		final int numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(2));
		final SimpleHashSet<Integer> setOfDecisionIds    = SimpleHashSet.makeHashSet(new Integer(decisionId0), new Integer(decisionId1), new Integer(decisionId2));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where all of the given decisions must be false.
     * 
     * @param setOfDecisionIds hash set of the given decisions
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyZeroOf(
		final SimpleHashSet<Integer> setOfDecisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		return exactlyNOf(0, setOfDecisionIds, numberOfDecisionsInProblem);
	}

    /**
     * Create a constraint, where exactly one of the given decisions must be true.
     * 
     * @param setOfDecisionIds hash set of the given decisions
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyOneOf(
		final SimpleHashSet<Integer> setOfDecisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		return exactlyNOf(1, setOfDecisionIds, numberOfDecisionsInProblem);
	}

    /**
     * Create a constraint, where exactly zero or one of the given decisions must be true.
     * 
     * @param setOfDecisionIds hash set of the given decisions
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint atMostOneOf(
		final SimpleHashSet<Integer> setOfDecisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(0), new Integer(1));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where exactly n of the given decisions must be true.
     * 
     * @param n the number of decisions that must be true
     * @param setOfDecisionIds hash set of the given decisions
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyNOf(
		final int                    n,
		final SimpleHashSet<Integer> setOfDecisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(n));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a constraint, where exactly m of the first n even decisions must be true.
     * Presume binary group domains.
     * 
     * @param m the number of decisions that must be true
     * @param n the number of decisions that are chosen
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the constraint
     */
	public static ConsistencyConstraint exactlyMOfFirstNEvens(
		final int m,
		final int n,
		final int numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSet(new Integer(m));
		final SimpleHashSet<Integer> setOfDecisionIds    = SimpleHashSet.makeHashSet(Utility.enumerateAscending(0, 2 * n - 2, 2));
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

	public static ConsistencyConstraint atLeastOne(
		final SimpleHashSet<Integer> setOfDecisionIds,
		final int                    numberOfDecisionsInProblem
	) {
		final SimpleHashSet<Integer> setOfNumbersOfTrues = SimpleHashSet.makeHashSetRange(1, setOfDecisionIds.size());
		return canonicalizer.canonicalize(new ConsistencyConstraint(setOfNumbersOfTrues, setOfDecisionIds, numberOfDecisionsInProblem));
	}

    /**
     * Create a vector containing all exactly one of two constraints for problems with the specified size.
     * This is used for testing.
     * 
     * @param numberOfDecisionsInProblem the number of decisions in the problem
     * @return the vector of constraints
     */
	public static Vector<ConsistencyConstraint> newAllExactlyOneOfTwo(final int numberOfDecisionsInProblem) {
		final Vector<ConsistencyConstraint> constraints = new Vector<ConsistencyConstraint>();
		for(int a = 0; a < numberOfDecisionsInProblem; a++) {
			for(int b = 0; b < numberOfDecisionsInProblem; b++) {
				if(a < b) {
					constraints.add(exactlyOneOfTwo(a, b, numberOfDecisionsInProblem));
				}
			}
		}
		return constraints;
	}

	// -----------------------------------------------------------------------
	// boilerplate functions

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(sortedAndUniqueNumbersOfTrues, 0)
							   ^ DoubleTabulationHashing.primaryHash(sortedAndUniqueDecisionIds,    1)
							   ^ DoubleTabulationHashing.primaryHash(numberOfDecisionsInProblem,    2);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(final Object otherObject) {
		return otherObject                != null
			&& getClass()                 == otherObject.getClass()
			&& Arrays.equals(sortedAndUniqueNumbersOfTrues, ((ConsistencyConstraint) otherObject).sortedAndUniqueNumbersOfTrues)
			&& Arrays.equals(sortedAndUniqueDecisionIds,    ((ConsistencyConstraint) otherObject).sortedAndUniqueDecisionIds)
			&& numberOfDecisionsInProblem == ((ConsistencyConstraint) otherObject).numberOfDecisionsInProblem;
	}
	
	@Override
	public String toString() {
		return "<constraint " + Utility.toStringAsSet(sortedAndUniqueNumbersOfTrues) + " of " + Utility.toStringAsSet(sortedAndUniqueDecisionIds) + " in " + numberOfDecisionsInProblem + ">";
	}
}
