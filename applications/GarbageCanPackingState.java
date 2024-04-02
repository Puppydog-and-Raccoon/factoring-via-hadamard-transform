package applications;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import consistency.*;

public class GarbageCanPackingState {
	final GarbageCanPackingProblem garbageCanPackingProblem;

	GarbageCanPackingState(final GarbageCanPackingProblem garbageCanPackingProblem) {
		this.garbageCanPackingProblem = garbageCanPackingProblem;
	}

	ConsistencyProblem makeConsistencyProblem() {
		final HashSet<ConsistencyConstraint> consistencyConstraints     = makeConsistencyConstraints();
		final int                            numberOfCoreDecisions      = garbageCanPackingProblem.garbageCansAndTheirItems.length + consistencyConstraints.size();
		final int                            numberOfDecisionsInProblem = Utility.roundUpToPowerOfTwo(numberOfCoreDecisions);

		// add constraints that force all remaining decisions to false. not needed but makes debugging more predictable
		for(int decisionId = numberOfCoreDecisions; decisionId < numberOfDecisionsInProblem; decisionId++) {
			consistencyConstraints.add(ConsistencyConstraint.exactlyZeroOfOne(decisionId, numberOfDecisionsInProblem));
		}

		// add constraint for for the number of garbage cans to choose
		consistencyConstraints.add(ConsistencyConstraint.exactlyMOfFirstN(garbageCanPackingProblem.numberOfGarbageCansToChoose, garbageCanPackingProblem.garbageCansAndTheirItems.length, numberOfDecisionsInProblem));

		System.out.println("bbb\n" + Utility.toStringFromSet(consistencyConstraints));

		// make consistency problem
		return new ConsistencyProblem(null, consistencyConstraints, numberOfDecisionsInProblem);
	}

	private HadamardDomain[][] makeHadamardDomains(final int numberOfDecisionsInProblem) {
		final int numberOfTiers = Utility.numberOfNodeTiers(numberOfDecisionsInProblem);
		final int numberOfTerms = Utility.numberOfNodeTerms(numberOfDecisionsInProblem);
		final HadamardDomain[][] hadamardDomains = new HadamardDomain[numberOfTiers][numberOfTerms];
		for(int tier = 0; tier < numberOfTiers; tier++) {
			for(int term = 0; term < numberOfTerms; term++) {
				hadamardDomains[tier][term] = makeHadamardDomain(tier, term);
			}
		}
		return hadamardDomains;
	}

	private HadamardDomain makeHadamardDomain(int tier, int term) {
		final int minimumHadamard   = HadamardDomain.defaultMinimumForNode(tier, term);
		final int maximumHadamard   = HadamardDomain.defaultMaximumForNode(tier, term);
		final int minimumPopulation = tier == 0 ? 0 : 1 << (tier - 1);
		final int maximumPopulation = tier == 0 ? 1 : 1 << (tier - 1); 
		return HadamardDomain.newSpecific(minimumHadamard, maximumHadamard, 1, minimumPopulation, maximumPopulation, 1);
	}

	HashSet<ConsistencyConstraint> makeConsistencyConstraints() {
		final String[][] garbageCansAndTheirItems = garbageCanPackingProblem.garbageCansAndTheirItems;
		final HashMap<String, GarbageCanPackingConstraint> fromItemToConstraint = new HashMap<String, GarbageCanPackingConstraint>();
		int nextVirtualGarbageCanId = garbageCansAndTheirItems.length;
		for(final int garbageCanIndex : Utility.enumerateAscending(garbageCansAndTheirItems.length)) {
			for(final String item : garbageCansAndTheirItems[garbageCanIndex]) {
				if(!fromItemToConstraint.containsKey(item)) {
					fromItemToConstraint.put(item, new GarbageCanPackingConstraint(item, nextVirtualGarbageCanId++));
				}
				fromItemToConstraint.get(item).addActualGarbageCanId(garbageCanIndex);
			}
		}

		final int numberOfDecisionsInProblem = Utility.roundUpToPowerOfTwo(garbageCansAndTheirItems.length + fromItemToConstraint.size());
		final HashSet<ConsistencyConstraint> consistencyConstraints = new HashSet<ConsistencyConstraint>();
		for(final GarbageCanPackingConstraint garbageCanPackingConstraint : fromItemToConstraint.values()) {
			final ConsistencyConstraint consistencyConstraint = garbageCanPackingConstraint.toConsistencyConstraint(numberOfDecisionsInProblem);
			consistencyConstraints.add(consistencyConstraint);
		}
		return consistencyConstraints;
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
			 : GarbageCanPackingSolution.makeSolutionFound(makeSelections(consistencySolution));
	}

	private boolean[] makeSelections(
		final ConsistencySolution consistencySolution
	) {
		final int numberOfSelections = garbageCanPackingProblem.garbageCansAndTheirItems.length;
		final boolean[] selections = new boolean[numberOfSelections];
		for(int i = 0; i < numberOfSelections; i++) {
			selections[i] = consistencySolution.decisions[i];
		}
		return selections;
	}
}
