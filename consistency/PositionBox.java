package consistency;

final class PositionBox {
	final int          boxTier;
	final int          boxTerm;
	final int          leafBoxTier;
	final int          populationBoxTerm;
	final int          spineBoxTerm;
	final PositionNode leftParentNode;
	final PositionNode rightParentNode;
	final PositionNode leftChildNode;
	final PositionNode rightChildNode;

	PositionBox(
		final int              boxTier,
		final int              boxTerm,
		final PositionNode[][] positionNodes
	) {
		final int lowBitsMask       = Utility.lowestNBitsSet(boxTier);
		final int highBitsOfBoxTerm = boxTerm & ~lowBitsMask;
		final int lowBitsOfBoxTerm  = boxTerm &  lowBitsMask;
		final int parentNodeTier    = boxTier;
		final int childNodeTier     = boxTier + 1;
		final int leftNodeTerm      = (highBitsOfBoxTerm << 1) | (0 << boxTier) | (lowBitsOfBoxTerm << 0);
		final int rightNodeTerm     = (highBitsOfBoxTerm << 1) | (1 << boxTier) | (lowBitsOfBoxTerm << 0);

		this.boxTier           = boxTier;
		this.boxTerm           = boxTerm;
		this.leafBoxTier       = positionNodes.length - 2;
		this.populationBoxTerm = highBitsOfBoxTerm;
		this.spineBoxTerm      = lowBitsOfBoxTerm;
		this.leftParentNode    = positionNodes[parentNodeTier][leftNodeTerm];
		this.rightParentNode   = positionNodes[parentNodeTier][rightNodeTerm];
		this.leftChildNode     = positionNodes[childNodeTier][leftNodeTerm];
		this.rightChildNode    = positionNodes[childNodeTier][rightNodeTerm];
	}

	boolean wouldMakeValidParentHadamards(
		final int leftChildHadamard,
		final int rightChildHadamard
	) {
		Utility.insist(leftChildNode.hadamardDomain.isInDomain(leftChildHadamard), "leftChildHadamard must lie in its domain");
		Utility.insist(rightChildNode.hadamardDomain.isInDomain(rightChildHadamard), "rightChildHadamard must lie in its domain");

		return (leftChildHadamard & 1) == (rightChildHadamard & 1)
			&& leftParentNode.hadamardDomain.isInDomain((leftChildHadamard + rightChildHadamard) / 2)
			&& rightParentNode.hadamardDomain.isInDomain((leftChildHadamard - rightChildHadamard) / 2);
	}

	boolean isLeafTierBox() {
		return boxTier == leafBoxTier;
	}

	boolean isSpineTermBox() {
		return boxTerm == spineBoxTerm;
	}

	boolean isPopulationTermBox() {
		return boxTerm == populationBoxTerm;
	}

	@Override
	public String toString() {
		return "<PositionBox boxTier=" + boxTier + " boxTerm=" + boxTerm + ">";
	}
}
