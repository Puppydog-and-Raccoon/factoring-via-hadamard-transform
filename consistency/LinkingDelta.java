package consistency;

// caching left/right parent/child linking facts helps performance a tiny bit

class LinkingDelta {
	private final short leftChildHadamard;
	private final short rightChildHadamard;
	private final short leftParentPopulation;
	private final short rightParentPopulation;
	private final short leftChildSpine;
	private final short rightChildSpine;
	private final short firstLeftChildPartialSum;
	private final short firstRightChildPartialSum;
	private final short secondLeftChildPartialSum;
	private final short secondRightChildPartialSum;

	private static final Canonicalizer<LinkingDelta> canonicalizer = new Canonicalizer<LinkingDelta>();

	private LinkingDelta(
		final int leftChildHadamard,
		final int rightChildHadamard,
		final int leftParentPopulation,
		final int rightParentPopulation,
		final int leftChildSpine,
		final int rightChildSpine,
		final int firstLeftChildPartialSum,
		final int firstRightChildPartialSum,
		final int secondLeftChildPartialSum,
		final int secondRightChildPartialSum
	) {
		Utility.insistSameParity(leftChildHadamard, rightChildHadamard);
		Utility.insistSameParity(firstLeftChildPartialSum,firstRightChildPartialSum);
		Utility.insistSameParity(secondLeftChildPartialSum, secondRightChildPartialSum);

		this.leftChildHadamard          = Utility.toShort(leftChildHadamard);
		this.rightChildHadamard         = Utility.toShort(rightChildHadamard);
		this.leftParentPopulation       = Utility.toShort(leftParentPopulation);
		this.rightParentPopulation      = Utility.toShort(rightParentPopulation);
		this.leftChildSpine             = Utility.toShort(leftChildSpine);
		this.rightChildSpine            = Utility.toShort(rightChildSpine);
		this.firstLeftChildPartialSum   = Utility.toShort(firstLeftChildPartialSum);
		this.firstRightChildPartialSum  = Utility.toShort(firstRightChildPartialSum);
		this.secondLeftChildPartialSum  = Utility.toShort(secondLeftChildPartialSum);
		this.secondRightChildPartialSum = Utility.toShort(secondRightChildPartialSum);
	}

	// -----------------------------------------------------------------------
	// factories

	static LinkingDelta make(
		final EquationDelta firstEquationDelta,
		final EquationDelta secondEquationDelta
	) {
//		Utility.insistEqual(firstEquationDelta.leftChildHadamard,     secondEquationDelta.leftChildHadamard);
//		Utility.insistEqual(firstEquationDelta.rightChildHadamard,    secondEquationDelta.rightChildHadamard);
//		Utility.insistEqual(firstEquationDelta.leftParentPopulation,  secondEquationDelta.leftParentPopulation);
//		Utility.insistEqual(firstEquationDelta.rightParentPopulation, secondEquationDelta.rightParentPopulation);
//		Utility.insistEqual(firstEquationDelta.leftChildSpine,        secondEquationDelta.leftChildSpine);
//		Utility.insistEqual(firstEquationDelta.rightChildSpine,       secondEquationDelta.rightChildSpine);

		return canonicalizer.canonicalize(new LinkingDelta(
			firstEquationDelta.leftChildHadamard,
			firstEquationDelta.rightChildHadamard,
			firstEquationDelta.leftParentPopulation,
			firstEquationDelta.rightParentPopulation,
			firstEquationDelta.leftChildSpine,
			firstEquationDelta.rightChildSpine,
			firstEquationDelta.leftChildPartialSum,
			firstEquationDelta.rightChildPartialSum,
			secondEquationDelta.leftChildPartialSum,
			secondEquationDelta.rightChildPartialSum
		));
	}

	static LinkingDelta make(
		final LinkingFact     leftChildLinkingFact,
		final LinkingFact     rightChildLinkingFact,
		final PopulationDelta populationDelta
	) {
//		Utility.insistSameParity(leftChildLinkingFact.hadamard, rightChildLinkingFact.hadamard);
//		Utility.insistEqual(leftChildLinkingFact.population, populationDelta.childPopulation());
//		Utility.insistEqual(rightChildLinkingFact.population, populationDelta.childPopulation());
//		Utility.insistSameParity(leftChildLinkingFact.spine, rightChildLinkingFact.spine);
//		Utility.insistSameParity(leftChildLinkingFact.firstPartialSum, rightChildLinkingFact.firstPartialSum);
//		Utility.insistSameParity(leftChildLinkingFact.secondPartialSum, rightChildLinkingFact.secondPartialSum);

		return canonicalizer.canonicalize(new LinkingDelta(
			leftChildLinkingFact.hadamard,
			rightChildLinkingFact.hadamard,
			populationDelta.leftParentPopulation,
			populationDelta.rightParentPopulation,
			leftChildLinkingFact.spine,
			rightChildLinkingFact.spine,
			leftChildLinkingFact.firstPartialSum,
			rightChildLinkingFact.firstPartialSum,
			leftChildLinkingFact.secondPartialSum,
			rightChildLinkingFact.secondPartialSum
		));
	}

	// -----------------------------------------------------------------------
	// accessor helpers

	private int leftParentHadamard() {
		return (leftChildHadamard + rightChildHadamard) / 2;
	}

	private int rightParentHadamard() {
		return (leftChildHadamard - rightChildHadamard) / 2;
	}

	private int childPopulation() {
		return leftParentPopulation + rightParentPopulation;
	}

	private int parentSpine() {
		return (leftChildSpine + rightChildSpine) / 2;
	}

	private int firstParentPartialSum() {
		return (firstLeftChildPartialSum + firstRightChildPartialSum) / 2;
	}

	private int secondParentPartialSum() {
		return (secondLeftChildPartialSum + secondRightChildPartialSum) / 2;
	}

	// -----------------------------------------------------------------------
	// accessors

	LinkingFact leftChildLinkingFact() {
		return LinkingFact.make(leftChildHadamard, childPopulation(), leftChildSpine, firstLeftChildPartialSum, secondLeftChildPartialSum);
	}

	LinkingFact rightChildLinkingFact() {
		return LinkingFact.make(rightChildHadamard, childPopulation(), rightChildSpine, firstRightChildPartialSum, secondRightChildPartialSum);
	}

	LinkingFact leftParentLinkingFact() {
		return LinkingFact.make(leftParentHadamard(), leftParentPopulation, parentSpine(), firstParentPartialSum(), secondParentPartialSum());
	}

	LinkingFact rightParentLinkingFact() {
		return LinkingFact.make(rightParentHadamard(), rightParentPopulation, parentSpine(), firstParentPartialSum(), secondParentPartialSum());
	}

	SolutionDelta solutionDelta() {
		return SolutionDelta.make(leftChildHadamard, rightChildHadamard, leftParentPopulation, rightParentPopulation, leftChildSpine, rightChildSpine);
	}

	SpinePartialSumsDelta linkingSpinePartialSumsDelta() {
		return SpinePartialSumsDelta.make(leftChildSpine, rightChildSpine, firstLeftChildPartialSum, firstRightChildPartialSum, secondLeftChildPartialSum, secondRightChildPartialSum);
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftChildHadamard,          0)
							   ^ DoubleTabulationHashing.primaryHash(rightChildHadamard,         1)
							   ^ DoubleTabulationHashing.primaryHash(leftParentPopulation,       2)
							   ^ DoubleTabulationHashing.primaryHash(rightParentPopulation,      3)
							   ^ DoubleTabulationHashing.primaryHash(leftChildSpine,             4)
							   ^ DoubleTabulationHashing.primaryHash(rightChildSpine,            5)
							   ^ DoubleTabulationHashing.primaryHash(firstLeftChildPartialSum,   6)
							   ^ DoubleTabulationHashing.primaryHash(firstRightChildPartialSum,  7)
							   ^ DoubleTabulationHashing.primaryHash(secondLeftChildPartialSum,  8)
							   ^ DoubleTabulationHashing.primaryHash(secondRightChildPartialSum, 9);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject                != null
			&& getClass()                  == otherObject.getClass()
			&& leftChildHadamard           == ((LinkingDelta) otherObject).leftChildHadamard
			&& rightChildHadamard          == ((LinkingDelta) otherObject).rightChildHadamard
			&& leftParentPopulation        == ((LinkingDelta) otherObject).leftParentPopulation
			&& rightParentPopulation       == ((LinkingDelta) otherObject).rightParentPopulation
			&& leftChildSpine              == ((LinkingDelta) otherObject).leftChildSpine
			&& rightChildSpine             == ((LinkingDelta) otherObject).rightChildSpine
			&& firstLeftChildPartialSum    == ((LinkingDelta) otherObject).firstLeftChildPartialSum
			&& firstRightChildPartialSum   == ((LinkingDelta) otherObject).firstRightChildPartialSum
			&& secondLeftChildPartialSum   == ((LinkingDelta) otherObject).secondLeftChildPartialSum
			&& secondRightChildPartialSum  == ((LinkingDelta) otherObject).secondRightChildPartialSum;
	}

	@Override
	public String toString() {
		return "<LinkingDelta "
			 + "lch=" + leftChildHadamard           + " "
			 + "rch=" + rightChildHadamard          + " "
			 + "lpp=" + leftParentPopulation        + " "
			 + "rpp=" + rightParentPopulation       + " "
			 + "lcs=" + leftChildSpine              + " "
			 + "rcs=" + rightChildSpine             + " "
			 + "1lcp=" + firstLeftChildPartialSum   + " "
			 + "1rcp=" + firstRightChildPartialSum  + " "
			 + "2lcp=" + secondLeftChildPartialSum  + " "
			 + "2rcp=" + secondRightChildPartialSum
			 + ">";
	}
}
