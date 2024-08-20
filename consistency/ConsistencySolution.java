package consistency;

// TODO: check if domains are valid???

import java.util.Arrays;
import java.util.Set;

/**
 * Describes the result of attempting to solve a consistency problem.
 */
public final class ConsistencySolution {
	/**
	 * Describes the consistency problem that was attempted.
	 */
	public final ConsistencyProblem consistencyProblem;

	/**
	 * The solution of successfully running the algorithm.
	 * Either an array of booleans that describes a valid solution or
	 * a null indicating that no solution is possible.
	 */
	public final boolean[] decisions;

	/**
	 * If the problem was fine, this variable is null.
	 * If the problem was badly formed, this variable contains a set of strings describing the errors.
	 */
	public final Set<String> errors;

    /**
     * Construct an object that indicates success.
     * Success means that the algorithm completed
     * and either found a solution or
     * found that no solution is possible.
     * 
     * @param consistencyProblem the problem that was solved
     * @param decisions either a vector of booleans that describes a solution or a null indicating that no solution is possible
     */
	public ConsistencySolution(
		final ConsistencyProblem consistencyProblem,
		final boolean[]          decisions
	) {
		Utility.insist(consistencyProblem.isValid(), "must be a valid problem");

		this.consistencyProblem = consistencyProblem;
		this.decisions          = decisions;
		this.errors             = null;
	}

    /**
     * Construct an object that indicates that the problem was badly formed.
     * 
     * @param consistencyProblem the problem that was to be solved
     */
	public ConsistencySolution(
		final ConsistencyProblem consistencyProblem
	) {
		Utility.insist(!consistencyProblem.isValid(), "must be an invalid problem");

		this.consistencyProblem = consistencyProblem;
		this.decisions          = null;
		this.errors             = consistencyProblem.errors();
	}

    /**
     * Check whether the decisions member describes a valid solution.
     * A vector of booleans must must match all constraints
     * and must have the correct number of decisions.
     * A null is trivially valid.
     * 
     * @return whether the given solution is valid for the given problem
     */
	public boolean isValid() {
		return decisions == null || allConstraintsAreValid() && numberOfDecisionsIsValid();
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
		return consistencyProblem.numberOfDecisionsInProblem == decisions.length;
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
