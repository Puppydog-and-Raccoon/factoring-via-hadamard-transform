package applications;

import consistency.*;

public class FactoringAlgorithm {
	public static FactoringSolution solve(final FactoringProblem factoringProblem) {
		Utility.insist(factoringProblem.isValid(), "invalid factoring problem");

		final FactoringState factoringState = new FactoringState(factoringProblem.product);

		final ConsistencyProblem consistencyProblem = factoringState.makeConsistencyProblem();
		Utility.insist(consistencyProblem.isValid(), "invalid consistency problem");

		final ConsistencySolution consistencySolution = ConsistencyAlgorithm.solve(consistencyProblem);
		Utility.insist(consistencySolution.isValid(), "invalid consistency solution");

		final FactoringSolution factoringSolution = factoringState.makeFactoringSolution(consistencySolution);
		Utility.insist(factoringSolution.isValid(factoringProblem), "invalid factoring solution");

		return factoringSolution;
	}
}
