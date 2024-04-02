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
		return "<PositionBox boxTier=" + boxTier + " boxTerm=" + boxTerm + " domain=" + leftParentNode.hadamardDomain + ">";
	}

	SolutionPopulationFact[] allParentPopulationFacts(
		final int childPopulation
	) {
		final int lowestParentPopulationForPosition         = leftParentNode.hadamardDomain.populationMinimum;
		final int highestParentPopulationForPosition        = leftParentNode.hadamardDomain.populationMaximum;
		final int lowestParentPopulationForChildPopulation  = Math.max(lowestParentPopulationForPosition,  childPopulation - highestParentPopulationForPosition);
		final int highestParentPopulationForChildPopulation = Math.min(highestParentPopulationForPosition, childPopulation - lowestParentPopulationForPosition);
		final int numberOfParentPopulationFacts             = highestParentPopulationForChildPopulation - lowestParentPopulationForChildPopulation + 1;
//		Utility.insist(numberOfParentPopulationFacts >= 0, "must have positive canonicals (" + numberOfParentPopulationFacts + ") " + positionBox + " " + childPopulation);
	
		if(numberOfParentPopulationFacts <= 0) {
//			System.out.println("### " + this + " " + numberOfParentPopulationFacts);
			return new SolutionPopulationFact[0];
		}
//		System.out.println("### " + positionBox + " " + numberOfParentPopulationFacts);
	
		final SolutionPopulationFact[] parentPopulationFacts = new SolutionPopulationFact[numberOfParentPopulationFacts];
		for(final int i : Utility.enumerateAscending(numberOfParentPopulationFacts)) {
			Utility.insist(lowestParentPopulationForChildPopulation + i >= 0, "must be non-negative");
			Utility.insist(lowestParentPopulationForChildPopulation <= childPopulation, "must be less than child population");
			Utility.insist(highestParentPopulationForChildPopulation - i >= 0, "must be non-negative");
			Utility.insist(highestParentPopulationForChildPopulation <= childPopulation, "must be less than child population");
			parentPopulationFacts[i] = SolutionPopulationFact.newFact(lowestParentPopulationForChildPopulation + i, highestParentPopulationForChildPopulation - i);
			Utility.insist(parentPopulationFacts[i].childPopulation() == childPopulation, "child populations must match");
		}
		return parentPopulationFacts;
	}
}
