package consistency;

import java.util.HashSet;

public class ConsistencyAlgorithm {
	public static ConsistencySolution solve(
		final ConsistencyProblem consistencyProblem
	) {
		final HashSet<String> validationErrors = consistencyProblem.errors();
		if(validationErrors.isEmpty()) {
			final ConsistencyState consistencyState = new ConsistencyState(consistencyProblem);
			consistencyState.solve();
			consistencyState.chooseSpineSolutionsDown();
			final boolean[] decisions = consistencyState.extractDecisions();
			return new ConsistencySolution(consistencyProblem, decisions, null);
		} else {
			return new ConsistencySolution(consistencyProblem, null, validationErrors);
		}
	}
}
