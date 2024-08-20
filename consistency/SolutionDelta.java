package consistency;

class SolutionDelta {
	final short leftChildHadamard;
	final short rightChildHadamard;
	final short leftParentPopulation;
	final short rightParentPopulation;
	final short leftChildSpine;
	final short rightChildSpine;

	private static final Canonicalizer<SolutionDelta> canonicalizer = new Canonicalizer<SolutionDelta>();

	private SolutionDelta(
		final int leftChildHadamard,
		final int rightChildHadamard,
		final int leftParentPopulation,
		final int rightParentPopulation,
		final int leftChildSpine,
		final int rightChildSpine
	) {
		Utility.insistSameParity(leftChildHadamard, rightChildHadamard);
		Utility.insistSameParity(leftChildSpine, rightChildSpine);

		this.leftChildHadamard     = Utility.toShort(leftChildHadamard);
		this.rightChildHadamard    = Utility.toShort(rightChildHadamard);
		this.leftParentPopulation  = Utility.toShort(leftParentPopulation);
		this.rightParentPopulation = Utility.toShort(rightParentPopulation);
		this.leftChildSpine        = Utility.toShort(leftChildSpine);
		this.rightChildSpine       = Utility.toShort(rightChildSpine);
	}

	// -----------------------------------------------------------------------
	// factories

	static SolutionDelta make(
		final SolutionFact    leftChildSolutionFact,
		final SolutionFact    rightChildSolutionFact,
		final PopulationDelta parentPopulationDelta
	) {
		Utility.insistSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard);
		Utility.insistEqual(leftChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistEqual(rightChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistSameParity(leftChildSolutionFact.spine, rightChildSolutionFact.spine);

		return canonicalizer.canonicalize(new SolutionDelta(
			leftChildSolutionFact.hadamard,
			rightChildSolutionFact.hadamard,
			parentPopulationDelta.leftParentPopulation,
			parentPopulationDelta.rightParentPopulation,
			leftChildSolutionFact.spine,
			rightChildSolutionFact.spine
		));
	}

	static SolutionDelta make(
		final int leftChildHadamard,
		final int rightChildHadamard,
		final int leftParentPopulation,
		final int rightParentPopulation,
		final int leftChildSpine,
		final int rightChildSpine
	) {
		return canonicalizer.canonicalize(new SolutionDelta(
			leftChildHadamard,
			rightChildHadamard,
			leftParentPopulation,
			rightParentPopulation,
			leftChildSpine,
			rightChildSpine
		));
	}

	// -----------------------------------------------------------------------
	// accessors

	int leftParentHadamard() {
		return (leftChildHadamard + rightChildHadamard) / 2;
	}

	int rightParentHadamard() {
		return (leftChildHadamard - rightChildHadamard) / 2;
	}

	int childPopulation() {
		return leftParentPopulation + rightParentPopulation;
	}

	int parentSpine() {
		return (leftChildSpine + rightChildSpine) / 2;
	}

	SolutionFact leftChildSolutionFact() {
		return SolutionFact.make(leftChildHadamard, childPopulation(), leftChildSpine);
	}

	SolutionFact rightChildSolutionFact() {
		return SolutionFact.make(rightChildHadamard, childPopulation(), rightChildSpine);
	}

	SolutionFact leftParentSolutionFact() {
		return SolutionFact.make(leftParentHadamard(), leftParentPopulation, parentSpine());
	}

	SolutionFact rightParentSolutionFact() {
		return SolutionFact.make(rightParentHadamard(), rightParentPopulation, parentSpine());
	}

	PopulationDelta populationDelta() {
		return PopulationDelta.make(leftParentPopulation, rightParentPopulation);
	}

	SpineDelta spineDelta() {
		return SpineDelta.make(leftChildSpine, rightChildSpine);
	}

	public static boolean sameSolutionDelta(
		final EquationDelta firstEquationDelta,
		final EquationDelta secondEquationDelta
	) {
		return firstEquationDelta.leftChildHadamard     == secondEquationDelta.leftChildHadamard
			&& firstEquationDelta.rightChildHadamard    == secondEquationDelta.rightChildHadamard
			&& firstEquationDelta.leftParentPopulation  == secondEquationDelta.leftParentPopulation
			&& firstEquationDelta.rightParentPopulation == secondEquationDelta.rightParentPopulation
			&& firstEquationDelta.leftChildSpine        == secondEquationDelta.leftChildSpine
			&& firstEquationDelta.rightChildSpine       == secondEquationDelta.rightChildSpine;
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftChildHadamard,     0)
							   ^ DoubleTabulationHashing.primaryHash(rightChildHadamard,    1)
							   ^ DoubleTabulationHashing.primaryHash(leftParentPopulation,  2)
							   ^ DoubleTabulationHashing.primaryHash(rightParentPopulation, 3)
							   ^ DoubleTabulationHashing.primaryHash(leftChildSpine,        4)
							   ^ DoubleTabulationHashing.primaryHash(rightChildSpine,       5);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject           != null
			&& getClass()            == otherObject.getClass()
			&& leftChildHadamard     == ((SolutionDelta) otherObject).leftChildHadamard
			&& rightChildHadamard    == ((SolutionDelta) otherObject).rightChildHadamard
			&& leftParentPopulation  == ((SolutionDelta) otherObject).leftParentPopulation
			&& rightParentPopulation == ((SolutionDelta) otherObject).rightParentPopulation
			&& leftChildSpine        == ((SolutionDelta) otherObject).leftChildSpine
			&& rightChildSpine       == ((SolutionDelta) otherObject).rightChildSpine;
	}

	@Override
	public String toString() {
		return "<shf "
			 + "lch=" + leftChildHadamard     + " "
			 + "rch=" + rightChildHadamard    + " "
			 + "lpp=" + leftParentPopulation  + " "
			 + "rpp=" + rightParentPopulation + " "
			 + "lcs=" + leftChildSpine        + " "
			 + "rcs=" + rightChildSpine
			 + ">";
	}
}
