package consistency;

// internal types are shorts to save space during prototyping
// external types are ints to factor numbers with 2k bits
// NOTE: hadamards can have same or different parities from spines and/or populations, consider (3+1)/2=2 and (3-1)/2=1

class SolutionFact {
	final short hadamard;
	final short population;
	final short spine;

	private static final Canonicalizer<SolutionFact> canonicalizer = new Canonicalizer<SolutionFact>();

	private SolutionFact(
		final int hadamard,
		final int population,
		final int spine
	) {
		this.hadamard   = Utility.toShort(hadamard);
		this.population = Utility.toShort(population);
		this.spine      = Utility.toShort(spine);
	}

	// -----------------------------------------------------------------------
	// factories

	static SolutionFact make(
		final int hadamard,
		final int population,
		final int spine
	) {
		return canonicalizer.canonicalize(new SolutionFact(
			hadamard,
			population,
			spine
		));
	}

	static SolutionFact makeLeftParent(
		final SolutionFact    leftChildSolutionFact,
		final SolutionFact    rightChildSolutionFact,
		final PopulationDelta parentPopulationDelta
	) {
		Utility.insistSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard);
		Utility.insistEqual(leftChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistEqual(rightChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistSameParity(leftChildSolutionFact.spine, rightChildSolutionFact.spine);

		return canonicalizer.canonicalize(new SolutionFact(
			(leftChildSolutionFact.hadamard + rightChildSolutionFact.hadamard) / 2,
			parentPopulationDelta.leftParentPopulation,
			(leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2
		));
	}

	static SolutionFact makeRightParent(
		final SolutionFact    leftChildSolutionFact,
		final SolutionFact    rightChildSolutionFact,
		final PopulationDelta parentPopulationDelta
	) {
		Utility.insistSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard);
		Utility.insistEqual(leftChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistEqual(rightChildSolutionFact.population, parentPopulationDelta.childPopulation());
		Utility.insistSameParity(leftChildSolutionFact.spine, rightChildSolutionFact.spine);

		return canonicalizer.canonicalize(new SolutionFact(
			(leftChildSolutionFact.hadamard - rightChildSolutionFact.hadamard) / 2,
			parentPopulationDelta.rightParentPopulation,
			(leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2
		));
	}

	// -----------------------------------------------------------------------
	// accessors - not used

	boolean isValidRootFact() {
		return hadamard == population;
	}

	boolean isValidLeafFact() {
		return hadamard == spine;
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(hadamard,   0)
							   ^ DoubleTabulationHashing.primaryHash(population, 1)
							   ^ DoubleTabulationHashing.primaryHash(spine,      2);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& hadamard    == ((SolutionFact) otherObject).hadamard
			&& population  == ((SolutionFact) otherObject).population
			&& spine       == ((SolutionFact) otherObject).spine;
	}

	@Override
	public String toString() {
		return "<sf "
			 + "h=" + hadamard   + " "
			 + "p=" + population + " "
			 + "s=" + spine
			 + ">";
	}
}
