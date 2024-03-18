package consistency;

// TODO: check if domains are valid???

import java.util.Arrays;
import java.util.Set;

public final class ConsistencySolution {
	public final ConsistencyProblem consistencyProblem;
	public final boolean[]          decisions; // null = no solution exists
	public final Set<String>        errors;

	public ConsistencySolution(
		final ConsistencyProblem consistencyProblem,
		final boolean[]          decisions,
		final Set<String>        errors
	) {
		this.consistencyProblem = consistencyProblem;
		this.decisions          = decisions;
		this.errors             = errors;
	}

	public boolean isValid() {
		return decisions == null || numberOfTruesIsValid() && allConstraintsAreValid() && numberOfDecisionsIsValid();
	}

	private boolean numberOfTruesIsValid() {
		final Integer numberOfTrues = consistencyProblem.numberOfTruesInProblem;
		return numberOfTrues == null || numberOfTrues.intValue() == Utility.population(decisions);
	}

	private boolean allConstraintsAreValid() {
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			if(!consistencyConstraint.matches(decisions)) {
				return false;
			}
		}
		return true;
	}

	private boolean numberOfDecisionsIsValid() {
		final int numberOfDecisions = consistencyProblem.numberOfDecisionsInProblem;
		return numberOfDecisions == decisions.length;
	}

	@Override
	public String toString() {
		return "ConsistencySolution ["
//			 + "consistencyProblem="   + consistencyProblem              + ", "
			 + "decisions="            + Arrays.toString(decisions)      + ", "
			 + "errors="               + Utility.toStringFromSet(errors)
			 + "]";
	}
}
