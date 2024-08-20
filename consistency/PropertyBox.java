package consistency;

// TODO: add partial sum domain??? No. That could differ for each equation butterfly

final class PropertyBox {
	final int                 boxTier;
	final int                 boxTerm;
	final int                 populationTreeBoxTerm;
	final int                 spinePartialSumTreeBoxTerm;
	final boolean             isFirstPopulationBoxTerm;
	final boolean             isFirstSpinePartialSumBoxTerm;
	final PropertyNode        leftParentNode;
	final PropertyNode        rightParentNode;
	final PropertyNode        leftChildNode;
	final PropertyNode        rightChildNode;
	final PopulationDelta[][] parentPopulationDeltas;

	PropertyBox(
		final int              boxTier,
		final int              boxTerm,
		final PropertyNode[][] propertyNodes
	) {
		final int lowBitsMask       = Utility.lowBitsMask(boxTier);
		final int lowBitsOfBoxTerm  = boxTerm &  lowBitsMask;
		final int highBitsOfBoxTerm = boxTerm & ~lowBitsMask;
		final int parentNodeTier    = boxTier + 0;
		final int childNodeTier     = boxTier + 1;
		final int leftNodeTerm      = (highBitsOfBoxTerm << 1) | (0 << boxTier) | (lowBitsOfBoxTerm << 0);
		final int rightNodeTerm     = (highBitsOfBoxTerm << 1) | (1 << boxTier) | (lowBitsOfBoxTerm << 0);

		this.boxTier                       = boxTier;
		this.boxTerm                       = boxTerm;
		this.populationTreeBoxTerm         = highBitsOfBoxTerm >> boxTier;
		this.spinePartialSumTreeBoxTerm    = lowBitsOfBoxTerm  >> 0;
		this.isFirstPopulationBoxTerm      = boxTerm == highBitsOfBoxTerm;
		this.isFirstSpinePartialSumBoxTerm = boxTerm == lowBitsOfBoxTerm;
		this.leftParentNode                = propertyNodes[parentNodeTier][leftNodeTerm];
		this.rightParentNode               = propertyNodes[parentNodeTier][rightNodeTerm];
		this.leftChildNode                 = propertyNodes[childNodeTier][leftNodeTerm];
		this.rightChildNode                = propertyNodes[childNodeTier][rightNodeTerm];
		this.parentPopulationDeltas        = makeParentPopulationDeltas(); // TODO: args
	}

	// NOTE: both parents and both children SHOULD produce the same results
	private PopulationDelta[][] makeParentPopulationDeltas() {
		final int minimumChildPopulation = leftChildNode.populationDomain.minimum;
		final int maximumChildPopulation = leftChildNode.populationDomain.maximum;
		final int numberOfChildPopulations = maximumChildPopulation + 1;
		final PopulationDelta[][] parentPopulationDeltas = new PopulationDelta[numberOfChildPopulations][];
		for(int childPopulation = minimumChildPopulation; childPopulation <= maximumChildPopulation; childPopulation++) {
			parentPopulationDeltas[childPopulation] = leftParentNode.populationDomain.parentPopulationDeltas(childPopulation);
		}
		return parentPopulationDeltas;
	}

	boolean wouldMakeValidParentEquationFacts(
		final EquationFact    leftChildEquationFact,
		final EquationFact    rightChildEquationFact,
		final PopulationDelta parentPopulationDelta
	) {
		Utility.insist(leftChildNode.hadamardDomain.isInDomain(leftChildEquationFact.hadamard), "left child hadamard must lie in its domain");
		Utility.insist(rightChildNode.hadamardDomain.isInDomain(rightChildEquationFact.hadamard), "right child hadamard must lie in its domain");
		Utility.insist(leftChildNode.populationDomain.isInDomain(leftChildEquationFact.population), "left child population must lie in its domain");
		Utility.insist(rightChildNode.populationDomain.isInDomain(rightChildEquationFact.population), "right child population must lie in its domain");
		Utility.insist(leftChildNode.spineDomain.isInDomain(leftChildEquationFact.spine), "left child spine must lie in its domain");
		Utility.insist(rightChildNode.spineDomain.isInDomain(rightChildEquationFact.spine), "right child spine must lie in its domain");
		Utility.insist(leftParentNode.populationDomain.isInDomain(parentPopulationDelta.leftParentPopulation), "left parent population must lie in its domain");
		Utility.insist(rightParentNode.populationDomain.isInDomain(parentPopulationDelta.rightParentPopulation), "right parent population must lie in its domain");

		return Utility.haveSameParity(leftChildEquationFact.hadamard, rightChildEquationFact.hadamard)
			&& leftChildEquationFact.population == parentPopulationDelta.childPopulation()
			&& rightChildEquationFact.population == parentPopulationDelta.childPopulation()
			&& Utility.haveSameParity(leftChildEquationFact.spine, rightChildEquationFact.spine)
			&& Utility.haveSameParity(leftChildEquationFact.partialSum, rightChildEquationFact.partialSum)
			&& leftParentNode.hadamardDomain.isInDomain((leftChildEquationFact.hadamard + rightChildEquationFact.hadamard) / 2)
			&& rightParentNode.hadamardDomain.isInDomain((leftChildEquationFact.hadamard - rightChildEquationFact.hadamard) / 2)
			&& leftParentNode.spineDomain.isInDomain((leftChildEquationFact.spine + rightChildEquationFact.spine) / 2)
			&& rightParentNode.spineDomain.isInDomain((leftChildEquationFact.spine + rightChildEquationFact.spine) / 2);
	}

	boolean wouldMakeValidParentSolutionFacts(
		final SolutionFact    leftChildSolutionFact,
		final SolutionFact    rightChildSolutionFact,
		final PopulationDelta parentPopulationDelta
	) {
		Utility.insist(leftChildNode.hadamardDomain.isInDomain(leftChildSolutionFact.hadamard), "left child hadamard must lie in its domain");
		Utility.insist(rightChildNode.hadamardDomain.isInDomain(rightChildSolutionFact.hadamard), "right child hadamard must lie in its domain");
		Utility.insist(leftChildNode.populationDomain.isInDomain(leftChildSolutionFact.population), "left child population must lie in its domain");
		Utility.insist(rightChildNode.populationDomain.isInDomain(rightChildSolutionFact.population), "right child population must lie in its domain");
		Utility.insist(leftChildNode.spineDomain.isInDomain(leftChildSolutionFact.spine), "left child spine must lie in its domain");
		Utility.insist(rightChildNode.spineDomain.isInDomain(rightChildSolutionFact.spine), "right child spine must lie in its domain");
		Utility.insist(leftParentNode.populationDomain.isInDomain(parentPopulationDelta.leftParentPopulation), "left parent population must lie in its domain");
		Utility.insist(rightParentNode.populationDomain.isInDomain(parentPopulationDelta.rightParentPopulation), "right parent population must lie in its domain");

		return Utility.haveSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard)
			&& leftChildSolutionFact.population == parentPopulationDelta.childPopulation()
			&& rightChildSolutionFact.population == parentPopulationDelta.childPopulation()
			&& Utility.haveSameParity(leftChildSolutionFact.spine, rightChildSolutionFact.spine)
			&& leftParentNode.hadamardDomain.isInDomain((leftChildSolutionFact.hadamard + rightChildSolutionFact.hadamard) / 2)
			&& rightParentNode.hadamardDomain.isInDomain((leftChildSolutionFact.hadamard - rightChildSolutionFact.hadamard) / 2)
			&& leftParentNode.spineDomain.isInDomain((leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2)
			&& rightParentNode.spineDomain.isInDomain((leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2);
	}

	PopulationDelta[] parentPopulationDeltas(
		final int childPopulation
	) {
		return parentPopulationDeltas[childPopulation];
	}

	@Override
	public String toString() {
		return "<pb " + boxTier + " " + boxTerm + ">";
	}
}
