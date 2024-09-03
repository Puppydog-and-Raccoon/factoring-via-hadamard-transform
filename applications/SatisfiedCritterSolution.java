package applications;

import java.util.HashMap;

/**
 * This class describes a satisfied-critter solution.
 */
public class SatisfiedCritterSolution {
	final HashMap<GarbageItem, Boolean> fromGarbageToPresent;

	/**
	 * This constructor.
	 * 
	 * @param fromGarbageToPresent map containing the solution
	 */
	public SatisfiedCritterSolution(
		final HashMap<GarbageItem, Boolean> fromGarbageToPresent
	) {
		this.fromGarbageToPresent = fromGarbageToPresent;
	}

	/**
	 * Determine whether the solution is valid for the problem.
	 * 
	 * @param satisfiedCritterProblem the problem to compare
	 * @return whether the solution is valid
	 */
	public boolean isValid(SatisfiedCritterProblem satisfiedCritterProblem) {
		return fromGarbageToPresent == null || works(satisfiedCritterProblem.clawses);
	}

	private boolean works(
		final GarbageItemState[][] clawses
	) {
		for(final GarbageItemState[] claws : clawses) {
			boolean found = false;
			for(final GarbageItemState claw : claws) {
				final Boolean isPresent = fromGarbageToPresent.get(claw.garbage);
				if(isPresent.booleanValue() == claw.present) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "<SatisfiedCritterSolution " + toString(fromGarbageToPresent) + ">";
	}

	// TODO: move to utility?
	private static String toString(HashMap<GarbageItem, Boolean> fromGarbageToPresent) {
		if(fromGarbageToPresent == null) {
			return "null";
		} else {
			final StringBuffer buffer = new StringBuffer();
			String separator = "";
			for(final GarbageItem garbage : fromGarbageToPresent.keySet()) {
				final Boolean value = fromGarbageToPresent.get(garbage);
				buffer.append(separator);
				buffer.append(garbage);
				buffer.append("->");
				buffer.append(value.booleanValue() ? "present" : "absent");
				separator = " ";
			}
			return buffer.toString();
		}
	}
}
