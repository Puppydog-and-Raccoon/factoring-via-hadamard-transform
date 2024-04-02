package applications;

import java.util.HashSet;

import consistency.ConsistencyConstraint;
import consistency.Utility;

public class GarbageCanPackingConstraint {
	public final String           garbageCanItem;
	public final HashSet<Integer> actualGarbageCanIds;
	public final Integer          virtualGarbageCanId;

	public GarbageCanPackingConstraint(
		final String garbageCanItem,
		final int    virtualGarbageCanId
	) {
		this.garbageCanItem      = garbageCanItem;
		this.actualGarbageCanIds = new HashSet<Integer>();
		this.virtualGarbageCanId = new Integer(virtualGarbageCanId);
	}

	public void addActualGarbageCanId(final int actualGarbageCanId) {
		actualGarbageCanIds.add(new Integer(actualGarbageCanId));
	}

	public ConsistencyConstraint toConsistencyConstraint(
		final int numberOfDecisionsInProblem
	) {
		final HashSet<Integer> allGarbageCanIds = new HashSet<Integer>();
		allGarbageCanIds.addAll(actualGarbageCanIds);
		allGarbageCanIds.add(virtualGarbageCanId);
		return ConsistencyConstraint.exactlyOneOf(allGarbageCanIds, numberOfDecisionsInProblem);
	}

	@Override
	public String toString() {
		return "GarbageCanPackingConstraint ["
			 + "garbageCanItem="      + garbageCanItem                               + ", "
			 + "actualGarbageCanIds=" + Utility.toStringFromSet(actualGarbageCanIds) + ", "
			 + "virtualGarbageCanId"  + virtualGarbageCanId
			 + "]";
	}
}
