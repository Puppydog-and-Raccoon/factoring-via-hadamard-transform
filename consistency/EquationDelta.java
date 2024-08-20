package consistency;

// internal types are shorts to save space during prototyping
// external types are ints to factor numbers with 2k bits
// this could be 3 pointers

// NOTE: we did cache the left/right parent/child equation facts, but that went slower

class EquationDelta {
	final short leftChildHadamard;
	final short rightChildHadamard;
	final short leftParentPopulation;
	final short rightParentPopulation;
	final short leftChildSpine;
	final short rightChildSpine;
	final short leftChildPartialSum;
	final short rightChildPartialSum;

	private static final Canonicalizer<EquationDelta> canonicalizer = new Canonicalizer<EquationDelta>();

	private EquationDelta(
		final int leftChildHadamard,
		final int rightChildHadamard,
		final int leftParentPopulation,
		final int rightParentPopulation,
		final int leftChildSpine,
		final int rightChildSpine,
		final int leftChildPartialSum,
		final int rightChildPartialSum
	) {
		Utility.insistSameParity(leftChildHadamard, rightChildHadamard);
		Utility.insistSameParity(leftChildPartialSum, rightChildPartialSum);

		this.leftChildHadamard     = Utility.toShort(leftChildHadamard);
		this.rightChildHadamard    = Utility.toShort(rightChildHadamard);
		this.leftParentPopulation  = Utility.toShort(leftParentPopulation);
		this.rightParentPopulation = Utility.toShort(rightParentPopulation);
		this.leftChildSpine        = Utility.toShort(leftChildSpine);
		this.rightChildSpine       = Utility.toShort(rightChildSpine);
		this.leftChildPartialSum   = Utility.toShort(leftChildPartialSum);
		this.rightChildPartialSum  = Utility.toShort(rightChildPartialSum);
	}

	// -----------------------------------------------------------------------
	// factories

	static EquationDelta make(
		final EquationFact leftChildEquationFact,
		final EquationFact rightChildEquationFact,
		final PopulationDelta parentPopulationDelta
	) {
		return canonicalizer.canonicalize(new EquationDelta(
			leftChildEquationFact.hadamard,
			rightChildEquationFact.hadamard,
			parentPopulationDelta.leftParentPopulation,
			parentPopulationDelta.rightParentPopulation,
			leftChildEquationFact.spine,
			rightChildEquationFact.spine,
			leftChildEquationFact.partialSum,
			rightChildEquationFact.partialSum
		));
	}

	// -----------------------------------------------------------------------
	// accessors

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

	private int parentPartialSum() {
		return (leftChildPartialSum + rightChildPartialSum) / 2;
	}

	EquationFact leftParentEquationFact() {
		return EquationFact.make(leftParentHadamard(), leftParentPopulation, parentSpine(), parentPartialSum());
	}

	EquationFact rightParentEquationFact() {
		return EquationFact.make(rightParentHadamard(), rightParentPopulation, parentSpine(), parentPartialSum());
	}

	EquationFact leftChildEquationFact() {
		return EquationFact.make(leftChildHadamard, childPopulation(), leftChildSpine, leftChildPartialSum);
	}

	EquationFact rightChildEquationFact() {
		return EquationFact.make(rightChildHadamard, childPopulation(), rightChildSpine, rightChildPartialSum);
	}
/*
	PopulationDelta populationDelta() {
		return PopulationDelta.make(leftParentPopulation, rightParentPopulation);
	}

	SolutionDelta solutionDelta() {
		return SolutionDelta.make(leftChildHadamard, rightChildHadamard, leftParentPopulation, rightParentPopulation, leftChildSpine, rightChildSpine);
	}

	SpineDelta spineDelta() {
		return SpineDelta.make(leftChildSpine, rightChildSpine);
	}

	SolutionFact leftParentSolutionFact() {
		return SolutionFact.make(leftParentHadamard(), leftParentPopulation, parentSpine());
	}

	SolutionFact rightParentSolutionFact() {
		return SolutionFact.make(rightParentHadamard(), rightParentPopulation, parentSpine());
	}

	SolutionFact leftChildSolutionFact() {
		return SolutionFact.make(leftChildHadamard, childPopulation(), leftChildSpine);
	}

	SolutionFact rightChildSolutionFact() {
		return SolutionFact.make(rightChildHadamard, childPopulation(), rightChildSpine);
	}
*/
	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftChildHadamard,     0)
							   ^ DoubleTabulationHashing.primaryHash(rightChildHadamard,    1)
							   ^ DoubleTabulationHashing.primaryHash(leftParentPopulation,  2)
							   ^ DoubleTabulationHashing.primaryHash(rightParentPopulation, 3)
							   ^ DoubleTabulationHashing.primaryHash(leftChildSpine,        4)
							   ^ DoubleTabulationHashing.primaryHash(rightChildSpine,       5)
							   ^ DoubleTabulationHashing.primaryHash(leftChildPartialSum,   6)
							   ^ DoubleTabulationHashing.primaryHash(rightChildPartialSum,  7);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject           != null
			&& getClass()            == otherObject.getClass()
			&& leftChildHadamard     == ((EquationDelta) otherObject).leftChildHadamard
			&& rightChildHadamard    == ((EquationDelta) otherObject).rightChildHadamard
			&& leftParentPopulation  == ((EquationDelta) otherObject).leftParentPopulation
			&& rightParentPopulation == ((EquationDelta) otherObject).rightParentPopulation
			&& leftChildSpine        == ((EquationDelta) otherObject).leftChildSpine
			&& rightChildSpine       == ((EquationDelta) otherObject).rightChildSpine
			&& leftChildPartialSum   == ((EquationDelta) otherObject).leftChildPartialSum
			&& rightChildPartialSum  == ((EquationDelta) otherObject).rightChildPartialSum;
	}

	@Override
	public String toString() {
		return "<EquationDelta "
			 + "lch=" + leftChildHadamard     + " "
			 + "rch=" + rightChildHadamard    + " "
			 + "lpp=" + leftParentPopulation  + " "
			 + "rpp=" + rightParentPopulation + " "
			 + "lcs=" + leftChildSpine        + " "
			 + "rcs=" + rightChildSpine       + " "
			 + "lcp=" + leftChildPartialSum   + " "
			 + "rcp=" + rightChildPartialSum
			 + ">";
	}
}
