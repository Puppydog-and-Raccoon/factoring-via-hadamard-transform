package applications;

import consistency.*;

public class GarbageCanPackingAlgorithm {
	public static GarbageCanPackingSolution solve(final GarbageCanPackingProblem garbageCanPackingProblem) {
		Utility.insist(garbageCanPackingProblem.isValid(), "invalid garbage can packing problem");

		final GarbageCanPackingState garbageCanPackingState = new GarbageCanPackingState(garbageCanPackingProblem);

		final ConsistencyProblem consistencyProblem = garbageCanPackingState.makeConsistencyProblem();
		Utility.insist(consistencyProblem.isValid(), "invalid consistency problem");

		final ConsistencySolution consistencySolution = ConsistencyAlgorithm.solve(consistencyProblem);
		Utility.insist(consistencySolution.isValid(), "invalid consistency solution");

		final GarbageCanPackingSolution garbageCanPackingSolution = garbageCanPackingState.makeGarbageCanPackingSolution(consistencySolution);
		Utility.insist(garbageCanPackingSolution.isValid(garbageCanPackingProblem), "invalid garbage can packing solution");

		return garbageCanPackingSolution;
	}
}
