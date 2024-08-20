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

	// ERROR: refactor
	// ERROR: merge "would makes()"???
	// ERROR: names
	void rippleUp(
		final DebugButterfly<SolutionFact> solutionFactButterfly
	) {
		for(final SolutionFact leftChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.leftChildNode)) {
			for(final SolutionFact rightChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.rightChildNode)) {
				if(leftChildSolutionFact.population == rightChildSolutionFact.population) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						if(propertyBox.wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta)) {
							final SolutionFact leftParentSolutionFact = SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionFact rightParentSolutionFact = SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionDelta solutionDelta = SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							if(solutionDeltaSet.contains(solutionDelta)) {
								solutionFactButterfly.hashSetAt(propertyBox.leftParentNode).add(leftParentSolutionFact);
								solutionFactButterfly.hashSetAt(propertyBox.rightParentNode).add(rightParentSolutionFact);
							}
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
		final int leftParentPopulation  = populationButterfly[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
		final int rightParentPopulation = populationButterfly[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];

		for(final SolutionFact leftChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.leftChildNode)) {
			for(final SolutionFact rightChildSolutionFact : solutionFactButterfly.hashSetAt(propertyBox.rightChildNode)) {
				if(leftChildSolutionFact.population == rightChildSolutionFact.population) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						final boolean aaa = propertyBox.wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
//							final boolean bbb = aaa && propertyBox.leftParentNode.wouldMakeValidParentHadamards(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard, parentPopulationDelta);
		//				System.out.println("888 " + aaa + " " + bbb);
						if(aaa) {
		//					System.out.println("999");
							final SolutionFact leftParentSolutionFact = SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionFact rightParentSolutionFact = SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionDelta solutionDelta = SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							if(solutionDeltaSet.contains(solutionDelta)
							&& solutionDelta.leftParentPopulation == leftParentPopulation
							&& solutionDelta.rightParentPopulation == rightParentPopulation) {
		//						System.out.println("000");
								solutionFactButterfly.hashSetAt(propertyBox.leftParentNode).add(leftParentSolutionFact);
								solutionFactButterfly.hashSetAt(propertyBox.rightParentNode).add(rightParentSolutionFact);
							}
						}
					}
				}
			}			
		}
	}

	boolean fillAllPossibleBoxStates() {
		final SimpleHashSet<SolutionFact>  verifiedLeftParentSolutionFacts  = new SimpleHashSet<>();
		final SimpleHashSet<SolutionFact>  verifiedRightParentSolutionFacts = new SimpleHashSet<>();
		final SimpleHashSet<SolutionDelta> verifiedSolutionDeltas           = new SimpleHashSet<>();

		System.out.println("uuu " + propertyBox);
		for(final SolutionFact leftChildSolutionFact : leftChildNode.solutionFacts) {
			for(final SolutionFact rightChildSolutionFact : rightChildNode.solutionFacts) {
				if(leftChildSolutionFact.population == rightChildSolutionFact.population) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildSolutionFact.population)) {
						if(propertyBox.wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta)
						/*&& propertyBox.leftParentNode.wouldMakeValidParentHadamards(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard)*/) {
							final SolutionDelta solutionDelta = SolutionDelta.make(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionFact leftParentSolutionFact = SolutionFact.makeLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							final SolutionFact rightParentSolutionFact = SolutionFact.makeRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationDelta);
							verifiedLeftParentSolutionFacts.add(leftParentSolutionFact);
							verifiedRightParentSolutionFacts.add(rightParentSolutionFact);
							verifiedSolutionDeltas.add(solutionDelta);
						}
					}
				}
			}
		}

		final boolean a = leftParentNode.solutionFacts.addAll(verifiedLeftParentSolutionFacts);
		final boolean b = rightParentNode.solutionFacts.addAll(verifiedRightParentSolutionFacts);
		final boolean c = solutionDeltaSet.addAll(verifiedSolutionDeltas);
		return a || b || c;
	}
}
