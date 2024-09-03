package applications;

import java.util.Vector;

import consistency.*;

/**
 * The heart of the garbage can packing algorithm.
 * It translates garbage can packing problems into consistency problems
 * and consistency solutions into garbage can packing solutions.
 */
public class GarbageCanPackingInternals {
	private final GarbageCanPackingProblem garbageCanPackingProblem;

	/**
	 * The constructor.
	 * 
	 * @param garbageCanPackingProblem the problem to solve
	 */
	public GarbageCanPackingInternals(
		final GarbageCanPackingProblem garbageCanPackingProblem
	) {
		this.garbageCanPackingProblem = garbageCanPackingProblem;
	}

	/**
	 * Translate the garbage can problem into a consistency problem.
	 * 
	 * @return the consistency problem
	 */
	public ConsistencyProblem makeConsistencyProblem() {
		final GarbageCanPackingDecisionPair[]       packingDecisionPairs         = makeDecisionPairs();
		final int                                   numberOfPackingDecisions     = packingDecisionPairs.length;
		final int                                   numberOfConsistencyDecisions = 2 * packingDecisionPairs.length;
		final SimpleHashSet<ConsistencyConstraint>  constraints                  = makeConsistencyConstraints(packingDecisionPairs);
		final DomainGenerator                       domainGenerator              = DomainGenerator.BINARY_GROUP;
		final int[][]                               solutions                    = makeConsistencySolutions(garbageCanPackingProblem.solutions, numberOfPackingDecisions);
		return new ConsistencyProblem(numberOfConsistencyDecisions, constraints, domainGenerator, solutions);
	}

	/**
	 * Make the decision pairs array, which maps garbage can packing decisions to consitency decisons and back.
	 * 
	 * @return the array of decision pairs
	 */
	private GarbageCanPackingDecisionPair[] makeDecisionPairs() {
		final Vector<GarbageCanPackingDecisionPair> decisionPairs = new Vector<GarbageCanPackingDecisionPair>();

		// add one decision pair for each garbage can
		for(GarbageItem[] garbageCanItems : garbageCanPackingProblem.garbageCansAndTheirItems) {
			decisionPairs.add(GarbageCanPackingDecisionPair.makeGarbageCan(garbageCanItems, decisionPairs.size()));
		}

		// add one decision pair for paddng until power of two
		while(!Utility.isPowerOfTwo(decisionPairs.size())) {
			decisionPairs.add(GarbageCanPackingDecisionPair.makePadding(decisionPairs.size()));
		}

		return decisionPairs.toArray(new GarbageCanPackingDecisionPair[0]);
	}

	/**
	 * Translate an array of garbage packing solutions into an array of consistency solutions.
	 * Used for debugging.
	 * 
	 * @param packingSolutions the array of garbage can packing solutions to translate
	 * @param numberOfPackingDecisions the number of packing decisions in the problem
	 * @return an array of consistency solutions
	 */
	private static int[][] makeConsistencySolutions(
		final boolean[][] packingSolutions,
		final int         numberOfPackingDecisions
	) {
		if(packingSolutions == null) {
			return null;
		}

		final Vector<int[]> consistencySolutions = new Vector<int[]>();
		for(final boolean[] packingSolution : packingSolutions) {
			final int[] consistencySolution = toConsistencySolution(packingSolution, numberOfPackingDecisions);
			consistencySolutions.add(consistencySolution);
		}
		return consistencySolutions.toArray(new int[0][]);
	}

	/**
	 * Translate one garbage can packing solution into one consistency solution.
	 * Used for debugging.
	 * 
	 * @param packingSolution the garbage can packing solution to translate
	 * @param numberOfPackingDecisions the number of packing decisions in the problem
	 * @return a consistency solution
	 */
	private static int[] toConsistencySolution(
		final boolean[] packingSolution,
		final int       numberOfPackingDecisions
	) {
		final int[] consistencySolution = new int[2 * numberOfPackingDecisions];

		// translate garbage can decisions
		for(int i = 0; i < packingSolution.length; i++) {
			consistencySolution[2 * i + 0] = Utility.toInt(packingSolution[i]);
			consistencySolution[2 * i + 1] = Utility.toInt(!packingSolution[i]);
		}

		// translate padding decisions
		for(int i = packingSolution.length; i < numberOfPackingDecisions; i++) {
			consistencySolution[2 * i + 0] = Utility.toInt(false);
			consistencySolution[2 * i + 1] = Utility.toInt(true);
		}

		return consistencySolution;
	}

	/**
	 * Make the consistency constraints.
	 * 
	 * @param packingDecisionPairs the decision pairs
	 * @return the consistency constraints
	 */
	private SimpleHashSet<ConsistencyConstraint> makeConsistencyConstraints(
		final GarbageCanPackingDecisionPair[] packingDecisionPairs
	) {
		final int numberOfDecisionsInConsistencyProblem = 2 * packingDecisionPairs.length;
		final SimpleHashSet<ConsistencyConstraint> consistencyConstraints = new SimpleHashSet<ConsistencyConstraint>();

		// add one constraint for each item, ignoring singletons
		for(final GarbageItem uniqueItem : garbageCanPackingProblem.uniqueItems) {
			final SimpleHashSet<Integer> indices = indicesForProperty(packingDecisionPairs, dp -> dp.concernsGarbageCanContaining(uniqueItem));
			final ConsistencyConstraint constraint = ConsistencyConstraint.atMostOneOf(indices, numberOfDecisionsInConsistencyProblem);
			if(indices.size() > 1) {
				consistencyConstraints.add(constraint);
			}
		}

		// add one constraint for the number of garbage cans to choose
		{
			final int numberOfGarbageCansToChoose = garbageCanPackingProblem.numberOfGarbageCansToChoose;
			final SimpleHashSet<Integer> indices = indicesForProperty(packingDecisionPairs, dp -> dp.concernsGarbageCan());
			final ConsistencyConstraint constraint = ConsistencyConstraint.exactlyNOf(numberOfGarbageCansToChoose, indices, numberOfDecisionsInConsistencyProblem);
			consistencyConstraints.add(constraint);
		}

		// add one constraint for padding
		{
			final SimpleHashSet<Integer> indices = indicesForProperty(packingDecisionPairs, dp -> dp.concernsPadding());
			final ConsistencyConstraint connstraint = ConsistencyConstraint.exactlyZeroOf(indices, numberOfDecisionsInConsistencyProblem);
			consistencyConstraints.add(connstraint);
		}

		return consistencyConstraints;
	}

	/**
	 * Translate a consistency solution into a garbage can packing solution.
	 * 
	 * @param consistencySolution the consistency solution
	 * @return the garbage can packing solution
	 */
	public GarbageCanPackingSolution makeGarbageCanPackingSolution(
		final ConsistencySolution consistencySolution
	) {
		return consistencySolution.decisions == null
			 ? GarbageCanPackingSolution.makeNoSolutionFound()
			 : GarbageCanPackingSolution.makeSolutionFound(toPackingSolution(consistencySolution));
	}

	/**
	 * Translate a consistency solution into a boolean vector of decisions.
	 * 
	 * @param consistencySolution the consistency solution
	 * @return the boolean vector of decisions
	 */
	private boolean[] toPackingSolution(
		final ConsistencySolution consistencySolution
	) {
		final int numberOfGarbageCans = garbageCanPackingProblem.garbageCansAndTheirItems.length;
		final boolean[] garbageCanSelections = new boolean[numberOfGarbageCans];
		for(int i = 0; i < numberOfGarbageCans; i++) {
			garbageCanSelections[i] = consistencySolution.decisions[2 * i];
		}
		return garbageCanSelections;
	}

	private interface DecisionPairFilter {
		boolean hasProperty(final GarbageCanPackingDecisionPair decisionPair);
	}

	/**
	 * Compute the set of decision ids that correspond to a particular property.
	 * Properties include contains a particular item, is a garbage can, and is padding.
	 * 
	 * @param decisionPairs the decision pairs
	 * @param filter the property
	 * @return the set of decisions ids
	 */
	private SimpleHashSet<Integer> indicesForProperty(final GarbageCanPackingDecisionPair[] decisionPairs, DecisionPairFilter filter) {
		final SimpleHashSet<Integer> decisions = new SimpleHashSet<Integer>();
		for(final GarbageCanPackingDecisionPair decisionPair : decisionPairs) {
			if(filter.hasProperty(decisionPair)) {
				decisions.add(new Integer(decisionPair.decisionId));
			}
		}
		return decisions;
	}
}
