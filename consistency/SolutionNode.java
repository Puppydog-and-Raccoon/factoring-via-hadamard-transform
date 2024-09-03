package consistency;

// ERROR: public

class SolutionNode {
	final PropertyNode                propertyNode;
	final SimpleHashSet<SolutionFact> solutionFacts;

	SolutionNode(final PropertyNode propertyNode) {
		this.propertyNode  = propertyNode;
		this.solutionFacts = new SimpleHashSet<>();
	}

	// NOTE: in leaf facts, spine values equal hadamard values
	public void fillAllPossibleLeafStates() {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		for(final int hadamard : propertyNode.hadamardDomain.enumerate()) {
			for(final int population : propertyNode.populationDomain.enumerate()) {
				final SolutionFact solutionFact = SolutionFact.make(hadamard, population, hadamard);
				solutionFacts.add(solutionFact);
			}
		}
	}

	public void rippleUp(
		final DebugButterfly<SolutionFact> solutionFactButterfly,
		final int[] leafHadamards
	) {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		final SimpleHashSet<SolutionFact> leafSolutionFacts = solutionFactButterfly.hashSetAt(propertyNode);
		final int hadamard = leafHadamards[propertyNode.nodeTerm];
		for(final SolutionFact solutionFact : solutionFacts) {
			if(solutionFact.hadamard == hadamard) {
				leafSolutionFacts.add(solutionFact);
			}
		}
	}

	public void rippleUp(
		final DebugButterfly<SolutionFact> solutionFactButterfly,
		final int[] leafHadamards,
		final int[][] populationButterfly
	) {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		final SimpleHashSet<SolutionFact> leafSolutionFacts = solutionFactButterfly.hashSetAt(propertyNode);
		final int hadamard = leafHadamards[propertyNode.nodeTerm];
		final int population = populationButterfly[propertyNode.nodeTier][propertyNode.nodeTerm];
		for(final SolutionFact solutionFact : solutionFacts) {
			if(solutionFact.hadamard == hadamard && solutionFact.population == population) {
				leafSolutionFacts.add(solutionFact);
			}
		}
	}

	@Override
	public String toString() {
		return "<SolutionNode "
		 +                    propertyNode                             + " "
		 + "solutionFacts=" + Utility.toStringFromSet(solutionFacts)   + " "
		 + ">";
	}
}
