package consistency;

class SolutionBox {
	final PropertyBox                  propertyBox;
	final SolutionNode                 leftParentNode;
	final SolutionNode                 rightParentNode;
	final SolutionNode                 leftChildNode;
	final SolutionNode                 rightChildNode;
	final SimpleHashSet<SolutionDelta> solutionDeltaSet;
	final SimpleHashSet<SpineDelta>    spineDeltaSet;

	@SuppressWarnings("unchecked")
	SolutionBox(
		final PropertyBox      propertyBox,
		final SolutionNode[][] solutionNodes,
		final Object[][]       spineDeltaSets   // SimpleHashSet<SpineDelta>[][]
	) {
		this.propertyBox      = propertyBox;
		this.leftParentNode   = solutionNodes[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
		this.rightParentNode  = solutionNodes[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
		this.leftChildNode    = solutionNodes[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
		this.rightChildNode   = solutionNodes[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];
		this.solutionDeltaSet = new SimpleHashSet<>();
		this.spineDeltaSet    = (SimpleHashSet<SpineDelta>) spineDeltaSets[propertyBox.boxTier][propertyBox.spinePartialSumTreeBoxTerm];
	}

	@Override
	public String toString() {
		return "<SolutionBox "
			 +                       propertyBox                               + " "
			 + "solutionDeltas="   + Utility.toStringFromSet(solutionDeltaSet)   + " "
			 + ">";
	}

	void rippleUp(
		final DebugButterfly<SolutionFact> solutionFactButterfly
	) {
		for(final SolutionFact leftChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.leftChildNode)) {
			for(final SolutionFact rightChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.rightChildNode)) {
				if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						if(solutionDeltaSet.contains(SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta))) {
							solutionFactButterfly.hashSetAt(propertyBox.leftParentNode).add(SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
							solutionFactButterfly.hashSetAt(propertyBox.rightParentNode).add(SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
						}
					}
				}
			}			
		}
	}

	void rippleUp(
		final DebugButterfly<SolutionFact> solutionFactButterfly,
		final int[][]                      populationButterfly
	) {
		for(final SolutionFact leftChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.leftChildNode)) {
			for(final SolutionFact rightChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.rightChildNode)) {
				if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						if(solutionDeltaSet.contains(SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta))) {
							solutionFactButterfly.hashSetAt(propertyBox.leftParentNode).add(SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
							solutionFactButterfly.hashSetAt(propertyBox.rightParentNode).add(SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
						}
					}
				}
			}			
		}
	}

	void fillAllPossibleBoxStates() {
		for(final SolutionFact leftChildSolutionFact : leftChildNode.solutionFacts) {
			for(final SolutionFact rightChildSolutionFact : rightChildNode.solutionFacts) {
				if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						solutionDeltaSet.add(SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
						leftParentNode.solutionFacts.add(SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
						rightParentNode.solutionFacts.add(SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta));
					}
				}
			}
		}
	}

	boolean wouldMakeValidParentSolutionFacts(
		final SolutionFact leftChildSolutionFact,
		final SolutionFact rightChildSolutionFact
	) {
		return Utility.haveSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard)
			&& leftChildSolutionFact.population == rightChildSolutionFact.population
			&& Utility.haveSameParity(leftChildSolutionFact.spine, rightChildSolutionFact.spine)
			&& propertyBox.leftParentNode.hadamardDomain.isInDomain((leftChildSolutionFact.hadamard + rightChildSolutionFact.hadamard) / 2)
			&& propertyBox.rightParentNode.hadamardDomain.isInDomain((leftChildSolutionFact.hadamard - rightChildSolutionFact.hadamard) / 2)
			&& propertyBox.leftParentNode.spineDomain.isInDomain((leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2)
			&& propertyBox.rightParentNode.spineDomain.isInDomain((leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2);
	}
}
