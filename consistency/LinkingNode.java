package consistency;

class LinkingNode {
	final PropertyNode               propertyNode;
	final EquationNode               firstEquationNode;
	final EquationNode               secondEquationNode;
	final SimpleHashSet<LinkingFact> linkingFacts;
	final SolutionNode               solutionNode;
	final boolean                    isFirstButterfly;

	LinkingNode(
		final EquationNode firstEquationNode,
		final EquationNode secondEquationNode,
		final SolutionNode solutionNode,
		final boolean      isFirstButterfly
	) {
		this.propertyNode       = firstEquationNode.propertyNode;
		this.firstEquationNode  = firstEquationNode;
		this.secondEquationNode = secondEquationNode;
		this.linkingFacts       = new SimpleHashSet<>();
		this.solutionNode       = solutionNode;
		this.isFirstButterfly   = isFirstButterfly;
	}

	boolean fillLeaf() {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		boolean anythingChanged = false;
		for(final EquationFact firstEquationFact : firstEquationNode.equationFacts) {
			for(final EquationFact secondEquationFact : secondEquationNode.equationFacts) {
				if(EquationFact.sameSolutionFact(firstEquationFact, secondEquationFact)) {
//					betweenFirstEquationFactsAndSecondEquationFacts.add(firstEquationFact, secondEquationFact);
					final LinkingFact linkingFact = LinkingFact.make(firstEquationFact, secondEquationFact);
					linkingFacts.add(linkingFact);
				}
			}
		}
		return anythingChanged;
	}

	// TODO: use debug butterfly
	@SuppressWarnings("unchecked")
	boolean rippleUpValidLeafFacts(
		final Object[][] links,
		final int[]      leafHadamards,
		final int        leafPopulation
	) {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		final SimpleHashSet<LinkingFact> leafLinkingFacts = (SimpleHashSet<LinkingFact>) links[propertyNode.nodeTier][propertyNode.nodeTerm];
		final int leafHadamard = leafHadamards[propertyNode.nodeTerm];

		for(final LinkingFact leafLinkingFact : linkingFacts) {
			if(leafLinkingFact.hadamard   == leafHadamard
			&& leafLinkingFact.population == leafPopulation
			&& leafLinkingFact.spine      == leafHadamard) { // redundant: leaf -> hadamard == spine
				leafLinkingFacts.add(leafLinkingFact);
			}
		}

		return !leafLinkingFacts.isEmpty();
	}

	@Override
	public String toString() {
//		return toStringLinkingNodes();
		return "<LinkingNode"
			 + propertyNode + " "
			 + ">";
	}

	// -----------------------------------------------------------------------
	// consistency functions

	boolean intersectToSharedState() {
		return solutionNode.solutionFacts.assignAllOrRetainAll(isFirstButterfly, projectSolutionFacts());
	}

	boolean intersectFromSharedState() {
		return linkingFacts.removeIf(linkingFact -> !solutionNode.solutionFacts.contains(linkingFact.solutionFact()));
	}

	private SimpleHashSet<SolutionFact> projectSolutionFacts() {
		final SimpleHashSet<SolutionFact> solutionFacts = new SimpleHashSet<>();
		for(final LinkingFact linkingFact : linkingFacts) {
			solutionFacts.add(linkingFact.solutionFact());
		}
		return solutionFacts;
	}

	boolean extractLeafHadamard(final int[] leafHadamards) {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

//		System.out.println(" eqn term " + propertyNode.nodeTerm + " " + betweenFirstEquationFactsAndSecondEquationFacts.allFirsts().size());
		System.out.println("extract leaf hadamard term " + propertyNode.nodeTerm + ": size = " + linkingFacts.size());
		for(final LinkingFact linkingFact : linkingFacts) {
			leafHadamards[propertyNode.nodeTerm] = linkingFact.spine;
			return true;
		}
		return false;
	}

	// this is used for testing
	void projectSolutionFactsIntoSolutionNode() {
		solutionNode.solutionFacts.clear();
		for(final LinkingFact linkingFact : linkingFacts) {
			solutionNode.solutionFacts.add(linkingFact.solutionFact());
		}
	}
}
