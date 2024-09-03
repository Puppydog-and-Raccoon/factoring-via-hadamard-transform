package consistency;

// TODO: rename shared?

/**
 * Contains the information that gets shared across all linking butterflies.
 * This should be a singleton.
 *
 */

class SolutionButterfly {
	final PropertyButterfly propertyButterfly;
	final SolutionNode[][]  solutionNodes;
	final SolutionBox[][]   solutionBoxes;
	final Object[][]        spineDeltasSets;   // SimpleHashSet<SpineDelta>

	SolutionButterfly(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][]       spineDeltaSets = makeSpineDeltas(propertyButterfly);
		final SolutionNode[][] solutionNodes  = makeSolutionNodes(propertyButterfly);
		final SolutionBox[][]  solutionBoxes  = makeSolutionBoxes(propertyButterfly, solutionNodes, spineDeltaSets);

		this.propertyButterfly = propertyButterfly;
		this.solutionNodes     = solutionNodes;
		this.solutionBoxes     = solutionBoxes;
		this.spineDeltasSets   = spineDeltaSets;
	}

	// -----------------------------------------------------------------------
	// constructor helpers

	static SolutionNode[][] makeSolutionNodes(
		final PropertyButterfly propertyButterfly
	) {
		final SolutionNode[][] solutionNodes = new SolutionNode[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
				final PropertyNode propertyNode = propertyButterfly.propertyNodes[nodeTier][nodeTerm];
				solutionNodes[nodeTier][nodeTerm] = new SolutionNode(propertyNode);
			}
		}
		return solutionNodes;
	}

	static SolutionBox[][] makeSolutionBoxes(
		final PropertyButterfly propertyButterfly,
		final SolutionNode[][]  solutionNodes,
		final Object[][]        spineDeltaSets
	) {
		final SolutionBox[][] solutionBoxes = new SolutionBox[propertyButterfly.numberOfBoxTiers][propertyButterfly.numberOfBoxTerms];
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				solutionBoxes[boxTier][boxTerm] = new SolutionBox(propertyBox, solutionNodes, spineDeltaSets);
			}
		}
		return solutionBoxes;
	}

	// -----------------------------------------------------------------------

	// TODO: delete
	boolean areValidRootHadamards(
		final int[] rootHadamards
	) {
		final int[]   leafHadamards       = Utility.fastSylvesterTransform(rootHadamards);
		final int[][] populationButterfly = populationButterfly(rootHadamards);
		return areValidLeafHadamards(leafHadamards, populationButterfly);
	}

	// TODO: get rid of one of these
	boolean areValidLeafHadamards(
		final int[] leafHadamards
	) {
		final DebugButterfly<SolutionFact> solutionFactButterfly = new DebugButterfly<>(propertyButterfly);
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			solutionNodes[propertyButterfly.leafNodeTier][leafNodeTerm].rippleUp(solutionFactButterfly, leafHadamards);
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				solutionBoxes[boxTier][boxTerm].rippleUp(solutionFactButterfly);
			}
		}
		return solutionFactButterfly.allRootsAreValid();
	}

	boolean areValidLeafHadamards(
		final int[]   leafHadamards,
		final int[][] populationButterfly
	) {
		final DebugButterfly<SolutionFact> solutionFactButterfly = new DebugButterfly<SolutionFact>(propertyButterfly);
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			solutionNodes[propertyButterfly.leafNodeTier][leafNodeTerm].rippleUp(solutionFactButterfly, leafHadamards, populationButterfly);
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				solutionBoxes[boxTier][boxTerm].rippleUp(solutionFactButterfly, populationButterfly);
			}
		}
		return solutionFactButterfly.allRootsAreValid();
	}

	public String nodeStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("solution butterfly: # node facts\n");
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final int numberOfSolutionFacts = solutionNodes[nodeTier][nodeTerm].solutionFacts.size();
				buffer.append(Utility.code(numberOfSolutionFacts));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	public String boxStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("solution butterfly: # box spine deltas\n");
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final int numberOfSpineDeltas = SolutionButterfly.numberOfSpineDeltas(solutionBoxes[boxTier][boxTerm].solutionDeltaSet);
				buffer.append(Utility.code(numberOfSpineDeltas));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	void fillAllFactsAndDeltas() {
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			solutionNodes[propertyButterfly.leafNodeTier][leafNodeTerm].fillAllPossibleLeafStates();
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				solutionBoxes[boxTier][boxTerm].fillAllPossibleBoxStates();
			}
		}
	}

	// -----------------------------------------------------------------------

	int[][] populationButterfly(
		final int[] rootHadamards
	) {
		final int[][] populationTree = new int[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int rootNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			populationTree[propertyButterfly.rootNodeTier][rootNodeTerm] = rootHadamards[rootNodeTerm];
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyNode leftParentNode = propertyButterfly.propertyBoxes[boxTier][boxTerm].leftParentNode;
				final PropertyNode rightChildNode = propertyButterfly.propertyBoxes[boxTier][boxTerm].rightChildNode;
				final int childPopulation = populationTree[leftParentNode.nodeTier][leftParentNode.nodeTerm]
										  + populationTree[leftParentNode.nodeTier][rightChildNode.nodeTerm];
				populationTree[rightChildNode.nodeTier][leftParentNode.nodeTerm] = childPopulation;
				populationTree[rightChildNode.nodeTier][rightChildNode.nodeTerm] = childPopulation;
			}
		}
		return populationTree;
	}

	static Object[][] makeSpineDeltas(final PropertyButterfly propertyButterfly) {
		Object[][] spineDeltas = new Object[propertyButterfly.numberOfBoxTiers][];
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			final int numberOfHashSetsInTier = 1 << boxTier;
			spineDeltas[boxTier] = new Object[numberOfHashSetsInTier];
			for(final int boxTerm : Utility.enumerateAscending(numberOfHashSetsInTier)) {
				spineDeltas[boxTier][boxTerm] = new SimpleHashSet<>();
			}
		}
		return spineDeltas;
	}

	static int numberOfSpineDeltas(
		final SimpleHashSet<SolutionDelta> solutionDeltas
	) {
		final SimpleHashSet<SpineDelta> spineDeltas = new SimpleHashSet<>();
		for(final SolutionDelta solutionDelta : solutionDeltas) {
			final SpineDelta spineDelta = solutionDelta.spineDelta();
			spineDeltas.add(spineDelta);
		}
		return solutionDeltas.size();
	}

	@SuppressWarnings("unchecked")
	void chooseSpineDeltasForTier(int boxTier) {
		for(int treeTerm = 0; treeTerm < spineDeltasSets[boxTier].length; treeTerm++) {
			final SimpleHashSet<SpineDelta> spineDeltaSet = (SimpleHashSet<SpineDelta>) spineDeltasSets[boxTier][treeTerm];
			spineDeltaSet.removeAllButRandomSingleton();
		}
	}

	// -----------------------------------------------------------------------
	// boilerplate functions

	@Override
	public String toString() {
		return "SolutionButterfly [solutionNodes=\n" /* + Utility.toString(solutionNodes) */ + "]";
	}
}
