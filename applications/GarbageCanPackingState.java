package applications;

import java.util.Arrays;
import java.util.HashSet;

import consistency.*;

public class GarbageCanPackingState {
	final GarbageCanPackingProblem garbageCanPackingProblem;

	GarbageCanPackingState(final GarbageCanPackingProblem garbageCanPackingProblem) {
		this.garbageCanPackingProblem = garbageCanPackingProblem;
	}

	ConsistencyProblem makeConsistencyProblem() {
		final int                                  numberOfGarbageCansToChoose  = garbageCanPackingProblem.numberOfGarbageCansToChoose;
		final HashSet<GarbageCanPackingConstraint> garbageCanPackingConstraints = GarbageCanPackingState.garbageCanPackingConstraints(garbageCanPackingProblem.garbageCansAndTheirItems);
		final int                                  numberOfGarbageCans          = garbageCanPackingProblem.garbageCansAndTheirItems.length;
		final int                                  numberOfDecisionsInProblem   = Utility.roundUpToPowerOfTwo(numberOfGarbageCans + garbageCanPackingConstraints.size());

		// convert "at most 1 of 2" constraints into "exactly 1 of 3" constraints
		final HashSet<ConsistencyConstraint> consistencyConstraints = new HashSet<ConsistencyConstraint>();
		int nextFreeConsistencyDecision = numberOfGarbageCans;
		for(GarbageCanPackingConstraint garbageCanPackingConstraint : garbageCanPackingConstraints) {
			consistencyConstraints.add(garbageCanPackingConstraint.toExactlyOneOfThree(nextFreeConsistencyDecision++, numberOfDecisionsInProblem));
		}

		// add constraints that force all remaining decisions to false. not needed, but makes debugging more predictable
		for(; nextFreeConsistencyDecision < numberOfDecisionsInProblem; nextFreeConsistencyDecision++) {
			consistencyConstraints.add(ConsistencyConstraint.exactlyZeroOfOne(nextFreeConsistencyDecision, numberOfDecisionsInProblem));
		}

		// add constraint for for the number of garbage cans to choose
		// make this into domains? could be canonical population? no, this must remain a constraint
		consistencyConstraints.add(ConsistencyConstraint.exactlyMOfFirstN(numberOfGarbageCansToChoose, numberOfGarbageCans, numberOfDecisionsInProblem));

		// make consistency problem
		// RETHINK: should the first arg be null???
		return new ConsistencyProblem(null, consistencyConstraints, numberOfDecisionsInProblem);
	}

	static HashSet<GarbageCanPackingConstraint> garbageCanPackingConstraints(
		final String[][] garbageCansAndTheirItems
	) {
		final HashSet<GarbageCanPackingConstraint> constraints = new HashSet<GarbageCanPackingConstraint>();
		for(int garbageCanIdA = 0; garbageCanIdA < garbageCansAndTheirItems.length; garbageCanIdA++) {
			for(int garbageCanIdB = garbageCanIdA + 1; garbageCanIdB < garbageCansAndTheirItems.length; garbageCanIdB++) {
				if(!GarbageCanPackingState.isConsistent(garbageCansAndTheirItems[garbageCanIdA], garbageCansAndTheirItems[garbageCanIdB])) {
					constraints.add(GarbageCanPackingConstraint.makeAtMostOneOf(garbageCanIdA, garbageCanIdB));
				}
			}
		}
		return constraints;
	}

	// two garbage cans are consistent when they contain no items in common
	// efficiency is less important in this context
	static boolean isConsistent(String[] garbageCanAItems, String[] garbageCanBItems) {
		for(String garbageCanAItem : garbageCanAItems) {
			for(String garbageCanBItem : garbageCanBItems) {
				if(garbageCanAItem.equals(garbageCanBItem)) {
					return false;
				}
			}
		}
		return true;
	}

	GarbageCanPackingSolution makeGarbageCanPackingSolution(
		final ConsistencySolution consistencySolution
	) {
		return consistencySolution.decisions == null
			 ? GarbageCanPackingSolution.makeNoSolutionFound()
			 : GarbageCanPackingSolution.makeSolutionFound(Arrays.copyOf(consistencySolution.decisions, garbageCanPackingProblem.garbageCansAndTheirItems.length));
	}
}
