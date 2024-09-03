package applications;

import consistency.ConsistencyProblem;
import consistency.ConsistencySolution;
import consistency.Utility;

/**
 * This class describes a satisfied-critter problem.
 */
public class SatisfiedCritterProblem {
	final GarbageItemState[][] clawses;
	final int[][]              testSolutions;

	/**
	 * The constructor.
	 * 
	 * @param clawses the clawses that describe the problem
	 * @param testSolutions solutions for testing, optional
	 */
	SatisfiedCritterProblem(
		final GarbageItemState[][] clawses,
		final int[][]              testSolutions
	) {
		this.clawses       = clawses;
		this.testSolutions = testSolutions;
	}

	/**
	 * Requires that there is at least one claws and
	 * that all claws have at least one claw.
	 *
	 * @return whether the problem is valid
	 */
	boolean isValid() {
		if(clawses == null || clawses.length == 0) {
			return false;
		}
		for(final GarbageItemState[] claws : clawses) {
			if(claws == null || claws.length == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the solution to the problem.
	 * 
	 * @return the solution
	 */
	SatisfiedCritterSolution solve() {
		Utility.insist(isValid(), "invalid satisfied critter problem");

		final SatisfiedCritterInternals satisfiedCritterInternals = new SatisfiedCritterInternals(this);

		final ConsistencyProblem consistencyProblem = satisfiedCritterInternals.makeConsistencyProblem();
		Utility.insist(consistencyProblem.isValid(), "invalid consistency problem");

		final ConsistencySolution consistencySolution = consistencyProblem.solve();
		Utility.insist(consistencySolution.isValid(), "invalid consistency solution");

		final SatisfiedCritterSolution satisfiedCritterSolution = satisfiedCritterInternals.makeSatisfiedCritterSolution(consistencySolution);
		Utility.insist(satisfiedCritterSolution.isValid(this), "invalid satisfied critter solution");

		return satisfiedCritterSolution;
	}

	@Override
	public String toString() {
		return "SatisfiedCritterProblem [clawses=" + Utility.toString(clawses) + "]";
	}
}
