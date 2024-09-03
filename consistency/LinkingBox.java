package consistency;

import java.util.HashMap;
import java.util.Iterator;

class LinkingBox {
	final PropertyBox                  propertyBox;
	final EquationBox                  firstEquationBox;
	final EquationBox                  secondEquationBox;
	final LinkingNode                  leftParentNode;
	final LinkingNode                  rightParentNode;
	final LinkingNode                  leftChildNode;
	final LinkingNode                  rightChildNode;
	final SimpleHashSet<LinkingDelta>  linkingDeltas;

	final SolutionBox                  solutionBox;
	final boolean                      isFirstButterfly;
	final SimpleHashSet<SpinePartialSumsDelta> sharedSpinePartialSumsDeltasHashSet;
	
	LinkingBox(
		final EquationBox                  firstEquationBox,
		final EquationBox                  secondEquationBox,
		final LinkingNode[][]              linkingNodes,
		final SolutionBox                  solutionBox,
		final SharedWithinLinkingButterfly sharedWithinLinkingButterfly,
		final boolean                      isFirstButterfly
	) {
		final PropertyBox propertyBox = firstEquationBox.propertyBox;

		this.propertyBox                  = propertyBox;
		this.firstEquationBox             = firstEquationBox;
		this.secondEquationBox            = secondEquationBox;
		this.leftParentNode               = linkingNodes[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
		this.rightParentNode              = linkingNodes[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
		this.leftChildNode                = linkingNodes[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
		this.rightChildNode               = linkingNodes[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];
		this.linkingDeltas                = new SimpleHashSet<LinkingDelta>();

		this.solutionBox                  = solutionBox;
		this.isFirstButterfly             = isFirstButterfly;
		this.sharedSpinePartialSumsDeltasHashSet = sharedWithinLinkingButterfly.getSpinePartialSumsDeltasHashSet(propertyBox);

	}

	void fillBottomUp() {
		for(final LinkingFact leftChildLinkingFact : leftChildNode.linkingFacts) {
			for(final LinkingFact rightChildLinkingFact : rightChildNode.linkingFacts) {
				// this test is false about 7/8
				if(wouldMakeValidParentLinkingFacts(leftChildLinkingFact, rightChildLinkingFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildLinkingFact.population)) {
						// this test is false about 3/4
						if(doMakeActualParentEquationFacts(leftChildLinkingFact, rightChildLinkingFact, parentPopulationDelta)) {
							leftParentNode.linkingFacts.add(LinkingFact.makeLeftParent(leftChildLinkingFact, rightChildLinkingFact, parentPopulationDelta));
							rightParentNode.linkingFacts.add(LinkingFact.makeRightParent(leftChildLinkingFact, rightChildLinkingFact, parentPopulationDelta));
							linkingDeltas.add(LinkingDelta.make(leftChildLinkingFact, rightChildLinkingFact, parentPopulationDelta));
						}
					}
				}
			}
		}
	}

	private static void doStats(final SimpleHashSet<LinkingDelta> linkingDeltas) {
		final SimpleHashSet<SolutionDelta> solutionDeltas = new SimpleHashSet<>();
		for(final LinkingDelta linkingDelta : linkingDeltas) {
			solutionDeltas.add(linkingDelta.solutionDelta());
		}
		System.out.println(" ** " + linkingDeltas.size() + " / " + solutionDeltas.size() + " = " + ((double) linkingDeltas.size() / (double) solutionDeltas.size()));
	}

	private static void characterize(final SimpleHashSet<LinkingDelta> linkingDeltas) {
		final HashMap<SolutionDelta, Integer> minimumFirstLeft = new HashMap<>();
		final HashMap<SolutionDelta, Integer> minimumFirstRight = new HashMap<>();
		final HashMap<SolutionDelta, Integer> maximumFirstLeft = new HashMap<>();
		final HashMap<SolutionDelta, Integer> maximumFirstRight = new HashMap<>();
		final HashMap<SolutionDelta, Integer> minimumSecondLeft = new HashMap<>();
		final HashMap<SolutionDelta, Integer> minimumSecondRight = new HashMap<>();
		final HashMap<SolutionDelta, Integer> maximumSecondLeft = new HashMap<>();
		final HashMap<SolutionDelta, Integer> maximumSecondRight = new HashMap<>();

		for(final LinkingDelta linkingDelta : linkingDeltas) {
			final SolutionDelta solutionDelta = linkingDelta.solutionDelta();

			final Integer previousMinimumFirstLeft = minimumFirstLeft.get(solutionDelta);
			if(previousMinimumFirstLeft == null || previousMinimumFirstLeft.intValue() > linkingDelta.firstLeftChildPartialSum) {
				minimumFirstLeft.put(solutionDelta, new Integer(linkingDelta.firstLeftChildPartialSum));
			}
			final Integer previousMaximumFirstLeft = maximumFirstLeft.get(solutionDelta);
			if(previousMaximumFirstLeft == null || previousMaximumFirstLeft.intValue() < linkingDelta.firstLeftChildPartialSum) {
				maximumFirstLeft.put(solutionDelta, new Integer(linkingDelta.firstLeftChildPartialSum));
			}
			final Integer previousMinimumFirstRight = minimumFirstRight.get(solutionDelta);
			if(previousMinimumFirstRight == null || previousMinimumFirstRight.intValue() > linkingDelta.firstRightChildPartialSum) {
				minimumFirstRight.put(solutionDelta, new Integer(linkingDelta.firstRightChildPartialSum));
			}
			final Integer previousMaximumFirstRight = maximumFirstRight.get(solutionDelta);
			if(previousMaximumFirstRight == null || previousMaximumFirstRight.intValue() < linkingDelta.firstRightChildPartialSum) {
				maximumFirstRight.put(solutionDelta, new Integer(linkingDelta.firstRightChildPartialSum));
			}
			final Integer previousMinimumSecondLeft = minimumSecondLeft.get(solutionDelta);
			if(previousMinimumSecondLeft == null || previousMinimumSecondLeft.intValue() < linkingDelta.secondLeftChildPartialSum) {
				maximumSecondLeft.put(solutionDelta, new Integer(linkingDelta.secondLeftChildPartialSum));
			}
			final Integer previousMaximumSecondLeft = maximumSecondLeft.get(solutionDelta);
			if(previousMaximumSecondLeft == null || previousMaximumSecondLeft.intValue() < linkingDelta.secondLeftChildPartialSum) {
				maximumSecondLeft.put(solutionDelta, new Integer(linkingDelta.secondLeftChildPartialSum));
			}
			final Integer previousMinimumSecondRight = minimumSecondRight.get(solutionDelta);
			if(previousMinimumSecondRight == null || previousMinimumSecondRight.intValue() < linkingDelta.secondRightChildPartialSum) {
				maximumSecondRight.put(solutionDelta, new Integer(linkingDelta.secondRightChildPartialSum));
			}
			final Integer previousMaximumSecondRight = maximumSecondRight.get(solutionDelta);
			if(previousMaximumSecondRight == null || previousMaximumSecondRight.intValue() < linkingDelta.secondRightChildPartialSum) {
				maximumSecondRight.put(solutionDelta, new Integer(linkingDelta.secondRightChildPartialSum));
			}
		}

		int strideFirstLeft;
		int strideFirstRight;
		int strideSecondLeft;
		int strideSecondRight;
		for(final SolutionDelta solutionDelta : minimumSecondLeft.keySet()) {
		}
	}

	boolean wringOnceTopDown() {
		final SimpleHashSet<LinkingFact> verifiedLeftChildLinkingFacts  = new SimpleHashSet<>();
		final SimpleHashSet<LinkingFact> verifiedRightChildLinkingFacts = new SimpleHashSet<>();

		boolean linkingDeltasChanged = false;
		final Iterator<LinkingDelta> iterator = linkingDeltas.iterator();
		while(iterator.hasNext()) {
			final LinkingDelta linkingDelta = iterator.next();
			if(leftParentNode.linkingFacts.contains(linkingDelta.leftParentLinkingFact())
			&& rightParentNode.linkingFacts.contains(linkingDelta.rightParentLinkingFact())) {
				verifiedLeftChildLinkingFacts.add(linkingDelta.leftChildLinkingFact());
				verifiedRightChildLinkingFacts.add(linkingDelta.rightChildLinkingFact());
			} else {
				iterator.remove();
				linkingDeltasChanged = true;
			}
		}
		linkingDeltas.decreaseCapacityIfAppropriate();

		final boolean leftChildChanged = leftChildNode.linkingFacts.retainAll(verifiedLeftChildLinkingFacts);
		final boolean rightChildChanged = rightChildNode.linkingFacts.retainAll(verifiedRightChildLinkingFacts);
		return leftChildChanged || rightChildChanged || linkingDeltasChanged;
	}

	boolean wringOnceBottomUp() {
		final SimpleHashSet<LinkingFact> verifiedLeftParentLinkingFacts  = new SimpleHashSet<>();
		final SimpleHashSet<LinkingFact> verifiedRightParentLinkingFacts = new SimpleHashSet<>();

		boolean linkingDeltasChanged = false;
		final Iterator<LinkingDelta> iterator = linkingDeltas.iterator();
		while(iterator.hasNext()) {
			final LinkingDelta linkingDelta = iterator.next();
			if(leftChildNode.linkingFacts.contains(linkingDelta.leftChildLinkingFact())
			&& rightChildNode.linkingFacts.contains(linkingDelta.rightChildLinkingFact())) {
				verifiedLeftParentLinkingFacts.add(linkingDelta.leftParentLinkingFact());
				verifiedRightParentLinkingFacts.add(linkingDelta.rightParentLinkingFact());
			} else {
				iterator.remove();
				linkingDeltasChanged = true;
			}
		}
		linkingDeltas.decreaseCapacityIfAppropriate();

		final boolean leftParentChanged = leftParentNode.linkingFacts.retainAll(verifiedLeftParentLinkingFacts);
		final boolean rightParentChanged = rightParentNode.linkingFacts.retainAll(verifiedRightParentLinkingFacts);
		return leftParentChanged || rightParentChanged || linkingDeltasChanged;
	}

	// TODO: redo this
	@SuppressWarnings("unchecked")
	boolean rippleUpValidParentFacts(
		final Object[][] linkingFactsButterfly
	) {
		final SimpleHashSet<LinkingFact> leftParentLinkingFacts  = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
		final SimpleHashSet<LinkingFact> rightParentLinkingFacts = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
		final SimpleHashSet<LinkingFact> leftChildLinkingFacts   = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
		final SimpleHashSet<LinkingFact> rightChildLinkingFacts  = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];

		for(final LinkingDelta linkingDelta : linkingDeltas) {
			if(leftChildLinkingFacts.contains(linkingDelta.leftChildLinkingFact())
			&& rightChildLinkingFacts.contains(linkingDelta.rightChildLinkingFact())) {
				leftParentLinkingFacts.add(linkingDelta.leftParentLinkingFact());
				rightParentLinkingFacts.add(linkingDelta.rightParentLinkingFact());
			}
		}

		return !leftParentLinkingFacts.isEmpty() && !rightParentLinkingFacts.isEmpty();
	}

	private boolean wouldMakeValidParentLinkingFacts(
		final LinkingFact leftChildLinkingFact,
		final LinkingFact rightChildLinkingFact
	) {
		return Utility.haveSameParity(leftChildLinkingFact.hadamard, rightChildLinkingFact.hadamard)
			&& leftChildLinkingFact.population == rightChildLinkingFact.population
			&& Utility.haveSameParity(leftChildLinkingFact.spine, rightChildLinkingFact.spine)
			&& Utility.haveSameParity(leftChildLinkingFact.firstPartialSum, rightChildLinkingFact.firstPartialSum)
			&& Utility.haveSameParity(leftChildLinkingFact.secondPartialSum, rightChildLinkingFact.secondPartialSum)
			&& propertyBox.leftParentNode.hadamardDomain.isInDomain((leftChildLinkingFact.hadamard + rightChildLinkingFact.hadamard) / 2)
			&& propertyBox.rightParentNode.hadamardDomain.isInDomain((leftChildLinkingFact.hadamard - rightChildLinkingFact.hadamard) / 2)
			&& propertyBox.leftParentNode.spineDomain.isInDomain((leftChildLinkingFact.spine + rightChildLinkingFact.spine) / 2)
			&& propertyBox.rightParentNode.spineDomain.isInDomain((leftChildLinkingFact.spine + rightChildLinkingFact.spine) / 2);
	}

	private boolean doMakeActualParentEquationFacts(
		final LinkingFact leftChildLinkingFact,
		final LinkingFact rightChildLinkingFact,
		final PopulationDelta parentPopulationDelta
	) {
		return firstEquationBox.doMakeActualParentEquationFacts(leftChildLinkingFact.firstEquationFact(), rightChildLinkingFact.firstEquationFact(), parentPopulationDelta)
			&& secondEquationBox.doMakeActualParentEquationFacts(leftChildLinkingFact.secondEquationFact(), rightChildLinkingFact.secondEquationFact(), parentPopulationDelta);
	}

	// ---------------------------------------------------------------------------------------------
	// consistency functions

	boolean intersectToSharedState() {
		return sharedSpinePartialSumsDeltasHashSet.assignAllOrRetainAll(propertyBox.isFirstSpinePartialSumBoxTerm, projectLinkingSpinePartialSumsDeltas());
	}

	boolean intersectFromSharedState() {
		return linkingDeltas.removeIf(linkingDelta -> !sharedSpinePartialSumsDeltasHashSet.contains(linkingDelta.linkingSpinePartialSumsDelta()));
	}

	boolean intersectSpinePartialSumDeltasIntoSolutionDeltas() {
		return solutionBox.solutionDeltaSet.assignAllOrRetainAll(isFirstButterfly, projectSolutionDeltas());
	}

	// !sharedSpinePartialSumsDeltasHashSet.contains(linkingDelta.linkingSpinePartialSumsDelta()
	boolean intersectFromSharedStateToSpinePartialSumsDelta() {
		return linkingDeltas.removeIf(linkingDelta -> !solutionBox.solutionDeltaSet.contains(linkingDelta.solutionDelta()));
	}

	// TO SOLUTION BOX???
	boolean intersectToSpineDeltas() {
		return solutionBox.spineDeltaSet.assignAllOrRetainAll(isFirstButterfly && propertyBox.isFirstSpinePartialSumBoxTerm, projectSpineDeltas());
	}

	// TO SOLUTION BOX???
	boolean intersectFromSpineDeltas() {
		return sharedSpinePartialSumsDeltasHashSet.removeIf(spinePartialSumsDelta -> !solutionBox.spineDeltaSet.contains(spinePartialSumsDelta.spineDelta()));
	}

	private SimpleHashSet<SpinePartialSumsDelta> projectLinkingSpinePartialSumsDeltas() {
		final SimpleHashSet<SpinePartialSumsDelta> linkingSpinePartialSumsDeltas = new SimpleHashSet<>();
		for(final LinkingDelta linkingDelta : linkingDeltas) {
			linkingSpinePartialSumsDeltas.add(linkingDelta.linkingSpinePartialSumsDelta());
		}
		return linkingSpinePartialSumsDeltas;
	}

	private SimpleHashSet<SolutionDelta> projectSolutionDeltas() {
		final SimpleHashSet<SolutionDelta> solutionDeltas = new SimpleHashSet<>();
		for(final LinkingDelta linkingDelta : linkingDeltas) {
			solutionDeltas.add(linkingDelta.solutionDelta());
		}
		return solutionDeltas;
	}

	private SimpleHashSet<SpineDelta> projectSpineDeltas() {
		final SimpleHashSet<SpineDelta> spineDeltas = new SimpleHashSet<>();
		for(final SpinePartialSumsDelta spinePartialSumsDelta : sharedSpinePartialSumsDeltasHashSet) {
			spineDeltas.add(spinePartialSumsDelta.spineDelta());
		}
		return spineDeltas;
	}

	// this is used for testing
	void projectSolutionDeltasIntoSolutionBox() {
		solutionBox.solutionDeltaSet.clear();
		for(final LinkingDelta linkingDelta : linkingDeltas) {
			solutionBox.solutionDeltaSet.add(linkingDelta.solutionDelta());
		}
	}
}
