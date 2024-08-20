package consistency;

// internal types are shorts to save space during prototyping
// external types are ints to factor numbers with 2k bits

final class EquationFact {
	final short hadamard;
	final short population;
	final short spine;
	final short partialSum;

	private static final Canonicalizer<EquationFact> canonicalizer = new Canonicalizer<EquationFact>();

	private EquationFact(
		final int hadamard,
		final int population,
		final int spine,
		final int partialSum
	) {
		this.hadamard   = Utility.toShort(hadamard);
		this.population = Utility.toShort(population);
		this.spine      = Utility.toShort(spine);
		this.partialSum = Utility.toShort(partialSum);
	}

	// -----------------------------------------------------------------------
	// accessors

	SolutionFact solutionFact() {
		return SolutionFact.make(hadamard, population, spine);
	}

	static boolean sameSolutionFact(
		final EquationFact firstEquationFact,
		final EquationFact secondEquationFact
	) {
		return firstEquationFact.hadamard   == secondEquationFact.hadamard
			&& firstEquationFact.population == secondEquationFact.population
			&& firstEquationFact.spine      == secondEquationFact.spine;
	}

	// -----------------------------------------------------------------------
	// factories

	static EquationFact make(
		final int hadamard,
		final int population,
		final int spine,
		final int partialSum
	) {
		return canonicalizer.canonicalize(new EquationFact(
			hadamard,
			population,
			spine,
			partialSum
		));
	}

	static EquationFact makeLeftParent(
		final EquationFact leftChildFact,
		final EquationFact rightChildFact,
		final PopulationDelta populationDelta
	) {
		Utility.insistSameParity(leftChildFact.hadamard, rightChildFact.hadamard);
		Utility.insistSameParity(leftChildFact.spine, rightChildFact.spine);
		Utility.insistEqual(leftChildFact.population, populationDelta.childPopulation());
		Utility.insistEqual(rightChildFact.population, populationDelta.childPopulation());
		Utility.insistSameParity(leftChildFact.partialSum, rightChildFact.partialSum);

		return canonicalizer.canonicalize(new EquationFact(
			(leftChildFact.hadamard + rightChildFact.hadamard) / 2,
			populationDelta.leftParentPopulation,
			(leftChildFact.spine + rightChildFact.spine) / 2,
			(leftChildFact.partialSum + rightChildFact.partialSum) / 2
		));
	}

	static EquationFact makeRightParent(
		final EquationFact leftChildFact,
		final EquationFact rightChildFact,
		final PopulationDelta populationDelta
	) {
		Utility.insistSameParity(leftChildFact.hadamard, rightChildFact.hadamard);
		Utility.insistSameParity(leftChildFact.spine, rightChildFact.spine);
		Utility.insistEqual(leftChildFact.population, populationDelta.childPopulation());
		Utility.insistEqual(rightChildFact.population, populationDelta.childPopulation());
		Utility.insistSameParity(leftChildFact.partialSum, rightChildFact.partialSum);

		return canonicalizer.canonicalize(new EquationFact(
			(leftChildFact.hadamard - rightChildFact.hadamard) / 2,
			populationDelta.rightParentPopulation,
			(leftChildFact.spine + rightChildFact.spine) / 2,
			(leftChildFact.partialSum + rightChildFact.partialSum) / 2
		));
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(hadamard,   0)
							   ^ DoubleTabulationHashing.primaryHash(population, 1)
							   ^ DoubleTabulationHashing.primaryHash(spine,      2)
							   ^ DoubleTabulationHashing.primaryHash(partialSum, 3);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& hadamard    == ((EquationFact) otherObject).hadamard
			&& population  == ((EquationFact) otherObject).population
			&& spine       == ((EquationFact) otherObject).spine
			&& partialSum  == ((EquationFact) otherObject).partialSum;
	}

	@Override
	public String toString() {
		return "<" + hadamard + " " + population + " " + spine + " " + partialSum + ">";
	}
}
