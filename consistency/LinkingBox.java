package consistency;

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
	final SharedWithinLinkingButterfly sharedWithinLinkingButterfly;
	final boolean                      isFirstButterfly;

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
		this.sharedWithinLinkingButterfly = sharedWithinLinkingButterfly;
		this.isFirstButterfly             = isFirstButterfly;
	}

	boolean fillBottomUp() {
		final SimpleHashSet<LinkingFact>  verifiedLeftParentLinkingFacts  = new SimpleHashSet<>();
		final SimpleHashSet<LinkingFact>  verifiedRightParentLinkingFacts = new SimpleHashSet<>();
		final SimpleHashSet<LinkingDelta> verifiedLinkingDeltas           = new SimpleHashSet<>();

		for(final LinkingFact leftChildLinkingFact : leftChildNode.linkingFacts) {
			final EquationFact leftChildFirstEquationFact = leftChildLinkingFact.firstEquationFact();
			final EquationFact leftChildSecondEquationFact = leftChildLinkingFact.secondEquationFact();
			for(final LinkingFact rightChildLinkingFact : rightChildNode.linkingFacts) {
				if(leftChildLinkingFact.population == rightChildLinkingFact.population) {
					final EquationFact rightChildFirstEquationFact = rightChildLinkingFact.firstEquationFact();
					final EquationFact rightChildsecondEquationFact = rightChildLinkingFact.secondEquationFact();
					for(final PopulationDelta populationDelta : propertyBox.parentPopulationDeltas(leftChildLinkingFact.population)) {
						if(firstEquationBox.wouldAndDoMakeActualParents(leftChildFirstEquationFact, rightChildFirstEquationFact, populationDelta)
						&& secondEquationBox.wouldAndDoMakeActualParents(leftChildSecondEquationFact, rightChildsecondEquationFact, populationDelta)) {
							final LinkingFact leftParentLinkingFact  = LinkingFact.makeLeftParent(leftChildLinkingFact, rightChildLinkingFact, populationDelta);
							final LinkingFact rightParentLinkingFact = LinkingFact.makeRightParent(leftChildLinkingFact, rightChildLinkingFact, populationDelta);
							final LinkingDelta linkingDelta          = LinkingDelta.make(leftChildLinkingFact, rightChildLinkingFact, populationDelta);
							verifiedLeftParentLinkingFacts.add(leftParentLinkingFact);
							verifiedRightParentLinkingFacts.add(rightParentLinkingFact);
							verifiedLinkingDeltas.add(linkingDelta);
						}
					}
				}
			}
		}

		final boolean a = leftParentNode.linkingFacts.addAll(verifiedLeftParentLinkingFacts);
		final boolean b = rightParentNode.linkingFacts.addAll(verifiedRightParentLinkingFacts);
		final boolean c = linkingDeltas.addAll(verifiedLinkingDeltas);
		return a || b || c;
	}

	boolean wringOnceTopDown() {
		final SimpleHashSet<LinkingFact>  verifiedLeftChildLinkingFacts  = new SimpleHashSet<>();
		final SimpleHashSet<LinkingFact>  verifiedRightChildLinkingFacts = new SimpleHashSet<>();
		final SimpleHashSet<LinkingDelta> verifiedLinkingDeltas          = new SimpleHashSet<>();

		for(final LinkingDelta linkingDelta : linkingDeltas) {
			if(leftParentNode.linkingFacts.contains(linkingDelta.leftParentLinkingFact())
			&& rightParentNode.linkingFacts.contains(linkingDelta.rightParentLinkingFact())
			&& leftChildNode.linkingFacts.contains(linkingDelta.leftChildLinkingFact())
			&& rightChildNode.linkingFacts.contains(linkingDelta.rightChildLinkingFact())) {
				verifiedLeftChildLinkingFacts.add(linkingDelta.leftChildLinkingFact());
				verifiedRightChildLinkingFacts.add(linkingDelta.rightChildLinkingFact());
				verifiedLinkingDeltas.add(linkingDelta);
			}
		}

		final boolean a = leftChildNode.linkingFacts.retainAll(verifiedLeftChildLinkingFacts);
		final boolean b = rightChildNode.linkingFacts.retainAll(verifiedRightChildLinkingFacts);
		final boolean c = linkingDeltas.retainAll(verifiedLinkingDeltas);
		return a || b || c;
	}

	boolean wringOnceBottomUp() {
		final SimpleHashSet<LinkingFact>  verifiedLeftParentLinkingFacts  = new SimpleHashSet<>();
		final SimpleHashSet<LinkingFact>  verifiedRightParentLinkingFacts = new SimpleHashSet<>();
		final SimpleHashSet<LinkingDelta> verifiedLinkingDeltas           = new SimpleHashSet<>();

		for(final LinkingDelta linkingDelta : linkingDeltas) {
			if(leftParentNode.linkingFacts.contains(linkingDelta.leftParentLinkingFact())
			&& rightParentNode.linkingFacts.contains(linkingDelta.rightParentLinkingFact())
			&& leftChildNode.linkingFacts.contains(linkingDelta.leftChildLinkingFact())
			&& rightChildNode.linkingFacts.contains(linkingDelta.rightChildLinkingFact())) {
				verifiedLeftParentLinkingFacts.add(linkingDelta.leftParentLinkingFact());
				verifiedRightParentLinkingFacts.add(linkingDelta.rightParentLinkingFact());
				verifiedLinkingDeltas.add(linkingDelta);
			}
		}

		final boolean a = leftParentNode.linkingFacts.retainAll(verifiedLeftParentLinkingFacts);
		final boolean b = rightParentNode.linkingFacts.retainAll(verifiedRightParentLinkingFacts);
		final boolean c = linkingDeltas.retainAll(verifiedLinkingDeltas);
		return a || b || c;
	}

	@SuppressWarnings("unchecked")
	boolean rippleUpValidParentFacts(
		final Object[][] linkingFactsButterfly
	) {
		final SimpleHashSet<LinkingFact> leftParentLinkingFacts  = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[leftParentNode.propertyNode.nodeTier][leftParentNode.propertyNode.nodeTerm];
		final SimpleHashSet<LinkingFact> rightParentLinkingFacts = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[rightParentNode.propertyNode.nodeTier][rightParentNode.propertyNode.nodeTerm];
		final SimpleHashSet<LinkingFact> leftChildLinkingFacts   = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[leftChildNode.propertyNode.nodeTier][leftChildNode.propertyNode.nodeTerm];
		final SimpleHashSet<LinkingFact> rightChildLinkingFacts  = (SimpleHashSet<LinkingFact>) linkingFactsButterfly[rightChildNode.propertyNode.nodeTier][rightChildNode.propertyNode.nodeTerm];

		for(final LinkingDelta linkingDelta : linkingDeltas) {
			if(leftChildLinkingFacts.contains(linkingDelta.leftChildLinkingFact())
			&& rightChildLinkingFacts.contains(linkingDelta.rightChildLinkingFact())) {
				leftParentLinkingFacts.add(linkingDelta.leftParentLinkingFact());
				rightParentLinkingFacts.add(linkingDelta.rightParentLinkingFact());
			}
		}

		return !leftParentLinkingFacts.isEmpty() && !rightParentLinkingFacts.isEmpty();
	}

	// ---------------------------------------------------------------------------------------------
	// consistency functions

	boolean intersectToSharedState() {
		final SimpleHashSet<SpinePartialSumsDelta> sharedSpinePartialSumsDeltasHashSet = sharedWithinLinkingButterfly.getSpinePartialSumsDeltasHashSet(propertyBox);
		final boolean a = sharedSpinePartialSumsDeltasHashSet.assignAllOrRetainAll(propertyBox.isFirstSpinePartialSumBoxTerm, projectLinkingSpinePartialSumsDeltas());
		final boolean b = solutionBox.solutionDeltaSet.assignAllOrRetainAll(isFirstButterfly, projectSolutionDeltas());
		return a || b;
	}

	boolean intersectFromSharedState() {
		final SimpleHashSet<SpinePartialSumsDelta> sharedSpinePartialSumsDeltasHashSet = sharedWithinLinkingButterfly.getSpinePartialSumsDeltasHashSet(propertyBox);
		return linkingDeltas.removeIf(linkingDelta -> !sharedSpinePartialSumsDeltasHashSet.contains(linkingDelta.linkingSpinePartialSumsDelta())
												   || !solutionBox.solutionDeltaSet.contains(linkingDelta.solutionDelta()));
	}

	boolean intersectToSpineDeltas() {
		final boolean a = solutionBox.spineDeltaSet.assignAllOrRetainAll(propertyBox.isFirstSpinePartialSumBoxTerm, projectSpineDeltas());
		return a;
	}

	boolean intersectFromSpineDeltas() {
		final SimpleHashSet<SpinePartialSumsDelta> sharedSpinePartialSumsDeltasHashSet = sharedWithinLinkingButterfly.getSpinePartialSumsDeltasHashSet(propertyBox);
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
		final SimpleHashSet<SpinePartialSumsDelta> sharedSpinePartialSumsDeltasHashSet = sharedWithinLinkingButterfly.getSpinePartialSumsDeltasHashSet(propertyBox);

		final SimpleHashSet<SpineDelta> spineDeltas = new SimpleHashSet<>();
		for(final SpinePartialSumsDelta spinePartialSumsDelta : sharedSpinePartialSumsDeltasHashSet) {
			spineDeltas.add(spinePartialSumsDelta.spineDelta());
		}
		return spineDeltas;
	}
}
