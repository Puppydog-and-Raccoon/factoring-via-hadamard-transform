package consistency;

class LinkingButterfly {
	final PropertyButterfly            propertyButterfly;
	final EquationButterfly            firstEquationButterfly;
	final EquationButterfly            secondEquationButterfly;
	final SolutionButterfly            solutionButterfly; // sharedAcrossLinkingButterflies
	final SharedWithinLinkingButterfly sharedWithinLinkingButterfly;

	final LinkingNode[][]              linkingNodes;
	final LinkingBox[][]               linkingBoxes;
	final boolean                      isFirstButterfly;

	// -----------------------------------------------------------------------
	// methods used by consistency internals

	LinkingButterfly(
		final EquationButterfly firstEquationButterfly,
		final EquationButterfly secondEquationButterfly,
		final SolutionButterfly solutionButterfly,
		final boolean           isFirstButterfly
	) {
		final PropertyButterfly            propertyButterfly            = firstEquationButterfly.propertyButterfly;
		final SharedWithinLinkingButterfly sharedWithinLinkingButterfly = new SharedWithinLinkingButterfly(propertyButterfly);
		final LinkingNode[][]              linkingNodes                 = makeLinkingNodes(propertyButterfly, firstEquationButterfly, secondEquationButterfly, solutionButterfly, isFirstButterfly);
		final LinkingBox[][]               linkingBoxes                 = makeLinkingBoxes(propertyButterfly, firstEquationButterfly, secondEquationButterfly, solutionButterfly, linkingNodes, sharedWithinLinkingButterfly, isFirstButterfly);

		this.propertyButterfly              = propertyButterfly;
		this.firstEquationButterfly         = firstEquationButterfly;
		this.secondEquationButterfly        = secondEquationButterfly;
		this.solutionButterfly = solutionButterfly;
		this.sharedWithinLinkingButterfly   = sharedWithinLinkingButterfly;
		this.linkingNodes                   = linkingNodes;
		this.linkingBoxes                   = linkingBoxes;
		this.isFirstButterfly               = isFirstButterfly;
	}

	void fill() {
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			linkingNodes[propertyButterfly.leafNodeTier][leafNodeTerm].fillLeaf();
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				linkingBoxes[boxTier][boxTerm].fillBottomUp();
			}
		}
	}

	boolean wringUntilNoChange() {
		boolean anythingChanged = false;
		while(wringOnce()) {
			anythingChanged = true;
		}
		return anythingChanged;
	}

	int[] leafHadamards() {
		final int[] leafHadamards = new int[propertyButterfly.numberOfNodeTerms];
		boolean allLeafHadamardsFound = true;
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			final LinkingNode leafLinkingNode = linkingNodes[propertyButterfly.leafNodeTier][leafNodeTerm];
			final boolean leafHadamardFound = leafLinkingNode.extractLeafHadamard(leafHadamards);
			allLeafHadamardsFound = allLeafHadamardsFound && leafHadamardFound;
		}
		return allLeafHadamardsFound ? leafHadamards : null;
	}

	// this is used for testing
	void projectSolutionButterfly() {
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final LinkingNode linkingNode = linkingNodes[nodeTier][nodeTerm];
				linkingNode.projectSolutionFactsIntoSolutionNode();
			}
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final LinkingBox linkingBox = linkingBoxes[boxTier][boxTerm];
				linkingBox.projectSolutionDeltasIntoSolutionBox();
			}
		}
	}

	// -----------------------------------------------------------------------
	// helper functions

	private static LinkingNode[][] makeLinkingNodes(
		final PropertyButterfly propertyButterfly,
		final EquationButterfly firstEquationButterfly,
		final EquationButterfly secondEquationButterfly,
		final SolutionButterfly solutionButterfly,
		final boolean           isFirstButterfly
	) {
		final LinkingNode[][] linkingNodes = new LinkingNode[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final EquationNode firstEquationNode  = firstEquationButterfly.equationNodes[nodeTier][nodeTerm];
				final EquationNode secondEquationNode = secondEquationButterfly.equationNodes[nodeTier][nodeTerm];
				final SolutionNode solutionNode       = solutionButterfly.solutionNodes[nodeTier][nodeTerm];
				linkingNodes[nodeTier][nodeTerm] = new LinkingNode(firstEquationNode, secondEquationNode, solutionNode, isFirstButterfly);
			}
		}
		return linkingNodes;
	}

	private static LinkingBox[][] makeLinkingBoxes(
		final PropertyButterfly            propertyButterfly,
		final EquationButterfly            firstEquationButterfly,
		final EquationButterfly            secondEquationButterfly,
		final SolutionButterfly            solutionButterfly,
		final LinkingNode[][]              linkingNodes,
		final SharedWithinLinkingButterfly sharedWithinLinkingButterfly,
		final boolean                      isFirstButterfly
	) {
		final LinkingBox[][] linkingBoxes = new LinkingBox[propertyButterfly.numberOfBoxTiers][propertyButterfly.numberOfBoxTerms];
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final EquationBox firstEquationBox  = firstEquationButterfly.equationBoxes[boxTier][boxTerm];
				final EquationBox secondEquationBox = secondEquationButterfly.equationBoxes[boxTier][boxTerm];
				final SolutionBox solutionBox       = solutionButterfly.solutionBoxes[boxTier][boxTerm];
				linkingBoxes[boxTier][boxTerm] = new LinkingBox(
					firstEquationBox,
					secondEquationBox,
					linkingNodes,
					solutionBox,
					sharedWithinLinkingButterfly,
					isFirstButterfly
				);
			}
		}
		return linkingBoxes;
	}

	boolean wringOnce() {
		boolean anythingChanged = false;
		{
			final boolean butterflyChanged  = firstEquationButterfly.wringUntilNoChange();
			anythingChanged = anythingChanged || butterflyChanged;
		}
		{
			final boolean butterflyChanged = secondEquationButterfly.wringUntilNoChange();
			anythingChanged = anythingChanged || butterflyChanged;
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final boolean boxChanged = linkingBoxes[boxTier][boxTerm].wringOnceBottomUp();
				anythingChanged = anythingChanged || boxChanged;
			}
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final boolean boxChanged = linkingBoxes[boxTier][boxTerm].wringOnceTopDown();
				anythingChanged = anythingChanged || boxChanged;
			}
		}
		return anythingChanged;
	}

	// -----------------------------------------------------------------------

	// TODO: use debug butterfly
	boolean isValidRootHadamards(final int[] rootHadamards) {
		Utility.insist(rootHadamards.length == propertyButterfly.numberOfNodeTerms, "vector is wrong size: " + rootHadamards.length + " != " + linkingNodes[0].length);

		final int[]      leafHadamards  = Utility.fastSylvesterTransform(rootHadamards);
		final int        leafPopulation = Utility.population(rootHadamards);
		final Object[][] links          = makeLinks(propertyButterfly);

		boolean valid = true;
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			valid = valid && linkingNodes[propertyButterfly.leafNodeTier][leafNodeTerm].rippleUpValidLeafFacts(links, leafHadamards, leafPopulation);
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				valid = valid && linkingBoxes[boxTier][boxTerm].rippleUpValidParentFacts(links);
			}
		}
		return valid;
	}

	// convert to debug butterfly
	boolean isValidLeafHadamards(final int[] leafHadamards) {
		Utility.insist(leafHadamards.length == propertyButterfly.numberOfNodeTerms, "vector is wrong size: " + leafHadamards.length + " != " + linkingNodes[0].length);

		final int        leafPopulation = leafHadamards.length / 2;
		final Object[][] links          = makeLinks(propertyButterfly);

		boolean valid = true;
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			valid = valid && linkingNodes[propertyButterfly.leafNodeTier][leafNodeTerm].rippleUpValidLeafFacts(links, leafHadamards, leafPopulation);
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				valid = valid && linkingBoxes[boxTier][boxTerm].rippleUpValidParentFacts(links);
			}
		}
		return valid;
	}

	// TODO: factor out links into separate class?
	private static Object[][] makeLinks(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][] links = new Object[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				links[nodeTier][nodeTerm] = new SimpleHashSet<>();
			}
		}
		return links;
	}

	// -----------------------------------------------------------------------
	// debug functions

	String nodeStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("linking butterfly: # node facts\n");
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final int numberOfLinkingFacts = linkingNodes[nodeTier][nodeTerm].linkingFacts.size();
				buffer.append(Utility.code(numberOfLinkingFacts));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	String boxStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("linking butterfly: # box spine deltas\n");
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final int numberOfLinkingDeltas = linkingBoxes[boxTier][boxTerm].linkingDeltas.size();
				buffer.append(Utility.code(numberOfLinkingDeltas));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				buffer.append(linkingNodes[nodeTier][nodeTerm].toString());
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
}
