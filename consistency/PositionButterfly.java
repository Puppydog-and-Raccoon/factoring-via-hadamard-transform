package consistency;

final class PositionButterfly {
	final int              numberOfNodeTiers;
	final int              numberOfNodeTerms;
	final int              numberOfBoxTiers;
	final int              numberOfBoxTerms;
	final int              rootNodeTier;
	final int              leafNodeTier;
	final int              rootBoxTier;
	final int              leafBoxTier;
	final PositionNode[][] positionNodes;
	final PositionNode[]   rootPositionNodes;
	final PositionNode[]   leafPositionNodes;
	final PositionBox[][]  positionBoxes;
	final PositionBox[]    rootPositionBoxes;
	final PositionBox[]    leafPositionBoxes;
	final int[]            nodeTierIndicesTopDown;
	final int[]            nodeTierIndicesBottomUp;
	final int[]            nodeTermIndices;
	final int[]            boxTierIndicesTopDown;
	final int[]            boxTierIndicesBottomUp;
	final int[]            boxTermIndices;

	PositionButterfly(
		final int                numberOfDecisionsInProblem,
		final HadamardDomain[][] hadamardDomains
	) {
		Utility.insist(Utility.isPowerOfTwo(numberOfDecisionsInProblem), "numberOfDecisions must be a power of 2");
		Utility.insist(numberOfDecisionsInProblem >= 2, "numberOfDecisions must be at least 2");

		final int              numberOfNodeTerms = Utility.numberOfNodeTerms(numberOfDecisionsInProblem);
		final int              numberOfNodeTiers = Utility.numberOfNodeTiers(numberOfDecisionsInProblem);
		final int              numberOfBoxTiers  = numberOfNodeTiers - 1;
		final int              numberOfBoxTerms  = numberOfNodeTerms / 2;
		final int              rootNodeTier      = 0;
		final int              leafNodeTier      = numberOfNodeTiers - 1;
		final int              rootBoxTier       = 0;
		final int              leafBoxTier       = numberOfBoxTiers - 1;
		final PositionNode[][] positionNodes     = newNodes(numberOfNodeTiers, numberOfNodeTerms, hadamardDomains);
		final PositionBox[][]  positionBoxes     = newBoxes(numberOfBoxTiers, numberOfBoxTerms, positionNodes);

		this.numberOfNodeTiers       = numberOfNodeTiers;
		this.numberOfNodeTerms       = numberOfNodeTerms;
		this.numberOfBoxTiers        = numberOfBoxTiers;
		this.numberOfBoxTerms        = numberOfBoxTerms;
		this.rootNodeTier            = rootNodeTier;
		this.leafNodeTier            = leafNodeTier;
		this.rootBoxTier             = rootBoxTier;
		this.leafBoxTier             = leafBoxTier;
		this.positionNodes           = positionNodes;
		this.positionBoxes           = positionBoxes;
		this.rootPositionNodes       = positionNodes[rootNodeTier];
		this.leafPositionNodes       = positionNodes[leafNodeTier];
		this.rootPositionBoxes       = positionBoxes[rootBoxTier];
		this.leafPositionBoxes       = positionBoxes[leafBoxTier];
		this.nodeTierIndicesTopDown  = Utility.enumerateAscending(numberOfNodeTiers);
		this.nodeTierIndicesBottomUp = Utility.enumerateDescending(numberOfNodeTiers);
		this.nodeTermIndices         = Utility.enumerateAscending(numberOfNodeTerms);
		this.boxTierIndicesTopDown   = Utility.enumerateAscending(numberOfBoxTiers);
		this.boxTierIndicesBottomUp  = Utility.enumerateDescending(numberOfBoxTiers);
		this.boxTermIndices          = Utility.enumerateAscending(numberOfBoxTerms);
	}

	static PositionNode[][] newNodes(
		final int                numberOfNodeTiers,
		final int                numberOfNodeTerms,
		final HadamardDomain[][] hadamardDomains
	) {
		final PositionNode[][] positionNodes = new PositionNode[numberOfNodeTiers][numberOfNodeTerms];
		for(int nodeTier = 0; nodeTier < numberOfNodeTiers; nodeTier++) {
			for(int nodeTerm = 0; nodeTerm < numberOfNodeTerms; nodeTerm++) {
				positionNodes[nodeTier][nodeTerm] = new PositionNode(nodeTier, nodeTerm, hadamardDomains, positionNodes);
			}
		}
		return positionNodes;
	}

	static PositionBox[][] newBoxes(
		final int              numberOfBoxTiers,
		final int              numberOfBoxTerms,
		final PositionNode[][] positionNodes
	) {
		final PositionBox[][] positionBoxes = new PositionBox[numberOfBoxTiers][numberOfBoxTerms];
		for(int boxTier = 0; boxTier < numberOfBoxTiers; boxTier++) {
			for(int boxTerm = 0; boxTerm < numberOfBoxTerms; boxTerm++) {
				positionBoxes[boxTier][boxTerm] = new PositionBox(boxTier, boxTerm, positionNodes);
			}
		}
		return positionBoxes;
	}
}
