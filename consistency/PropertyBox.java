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
