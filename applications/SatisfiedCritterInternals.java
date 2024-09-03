package applications;

import java.util.HashMap;

import consistency.*;

/**
 * The heart of the satisfied-critter algorithm.
 * First, it translates satisfied-critter problems into consistency problems
 * Then, it translates consistency solutions into satisfied-critter solutions.
 */
public class SatisfiedCritterInternals {
	private final SatisfiedCritterProblem       satisfiedCritterProblem;
	private final HashMap<GarbageItem, Integer> fromGarbageItemToConsistencyPosition;

	/**
	 * The constructor.
	 * 
	 * @param satisfiedCritterProblem the problem to solve
	 */
	public SatisfiedCritterInternals(
		final SatisfiedCritterProblem satisfiedCritterProblem
	) {
		this.satisfiedCritterProblem              = satisfiedCritterProblem;
		this.fromGarbageItemToConsistencyPosition = makeFromGarbageItemToConsistencyPosition(satisfiedCritterProblem.clawses);
	}

	/**
	 * Convert the satisfied-critter problem into a consistency problem.
	 * 
	 * @return the consistency problem
	 */
	public ConsistencyProblem makeConsistencyProblem() {
		Utility.insist(satisfiedCritterProblem.isValid(), "the satisfied critter problem must be valid");

		final int numberOfDecisionsInProblem = Utility.roundUpToPowerOfTwo(2 * fromGarbageItemToConsistencyPosition.keySet().size());
		final SimpleHashSet<ConsistencyConstraint> constraints = makeConstraints(satisfiedCritterProblem.clawses, fromGarbageItemToConsistencyPosition, numberOfDecisionsInProblem);
		final int[][] testSolutions = satisfiedCritterProblem.testSolutions;
		return new ConsistencyProblem(numberOfDecisionsInProblem, constraints, DomainGenerator.BINARY_GROUP, testSolutions);
	}

	/**
	 * Convert the consistency solution into a satisfied-critter solution.
	 * 
	 * @param consistencySolution the consistency solution to convert
	 * @return the satisfied critter solution
	 */
	public SatisfiedCritterSolution makeSatisfiedCritterSolution(
		final ConsistencySolution consistencySolution
	) {
		if(consistencySolution.decisions == null) {
			return new SatisfiedCritterSolution(null);
		} else {
			final HashMap<GarbageItem, Boolean> fromGarbageItemToPresent = new HashMap<>();
			for(final GarbageItem garbage : fromGarbageItemToConsistencyPosition.keySet()) {
				final Integer decisionId = fromGarbageItemToConsistencyPosition.get(garbage);
				final boolean present = consistencySolution.decisions[2 * decisionId.intValue()];
				fromGarbageItemToPresent.put(garbage, new Boolean(present));
			}
			return new SatisfiedCritterSolution(fromGarbageItemToPresent);
		}
	}

	// -----------------------------------------------------------------------
	// helpers

	private static HashMap<GarbageItem, Integer> makeFromGarbageItemToConsistencyPosition(
		final GarbageItemState[][] clawses
	) {
		final HashMap<GarbageItem, Integer> fromGarbageItemToConsistencyPosition = new HashMap<>();
		for(final GarbageItemState[] claws : clawses) {
			for(final GarbageItemState claw : claws) {
				final GarbageItem garbage = claw.garbage;
				final int nextIndex = fromGarbageItemToConsistencyPosition.keySet().size();
				fromGarbageItemToConsistencyPosition.putIfAbsent(garbage, new Integer(nextIndex));
			}
		}
		return fromGarbageItemToConsistencyPosition;
	}

	private static SimpleHashSet<ConsistencyConstraint> makeConstraints(
		final GarbageItemState[][]          clawses,
		final HashMap<GarbageItem, Integer> fromGarbageItemToConsistencyPosition,
		final int                           numberOfDecisionsInProblem
	) {
		final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
		for(final GarbageItemState[] claws : clawses) {
			final SimpleHashSet<Integer> decisions = new SimpleHashSet<>();
			for(final GarbageItemState claw : claws) {
				final Integer pairIndex = fromGarbageItemToConsistencyPosition.get(claw.garbage);
				decisions.add(new Integer(2 * pairIndex.intValue() + (claw.present ? 0 : 1)));
			}
			constraints.add(ConsistencyConstraint.atLeastOne(decisions, numberOfDecisionsInProblem));
		}
		{
			final SimpleHashSet<Integer> decisionIds = SimpleHashSet.makeHashSetRange(2 * fromGarbageItemToConsistencyPosition.size(), numberOfDecisionsInProblem - 2, 2);
			constraints.add(ConsistencyConstraint.exactlyZeroOf(decisionIds, numberOfDecisionsInProblem));
		}
		return constraints;
	}

	// TODO: move to utility
	private static void print(
		final HashMap<GarbageItem, Integer> fromGarbageItemToConsistencyPosition
	) {
		for(final GarbageItem garbage : fromGarbageItemToConsistencyPosition.keySet()) {
			final Integer position = fromGarbageItemToConsistencyPosition.get(garbage);
			System.out.println(" " + garbage + " -> " + position);
		}
	}
}
