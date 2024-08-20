package consistency;

// caching first/second equation fact seems to hurt a small amount

class LinkingFact {
	final short hadamard;
	final short population;
	final short spine;
	final short firstPartialSum;
	final short secondPartialSum;

	private static final Canonicalizer<LinkingFact> canonicalizer = new Canonicalizer<LinkingFact>();

	private LinkingFact(
		final int hadamard,
		final int population,
		final int spine,
		final int firstPartialSum,
		final int secondPartialSum
	) {
		this.hadamard         = Utility.toShort(hadamard);
		this.population       = Utility.toShort(population);
		this.spine            = Utility.toShort(spine);
		this.firstPartialSum  = Utility.toShort(firstPartialSum);
		this.secondPartialSum = Utility.toShort(secondPartialSum);
	}

	// -----------------------------------------------------------------------
	// accessors

	SolutionFact solutionFact() {
		return SolutionFact.make(hadamard, population, spine);
	}

	// -----------------------------------------------------------------------
	// factories

	static LinkingFact make(
		final EquationFact firstEquationFact,
		final EquationFact secondEquationFact
	) {
//		Utility.insistEqual(firstEquationFact.hadamard,   secondEquationFact.hadamard);
//		Utility.insistEqual(firstEquationFact.population, secondEquationFact.population);
//		Utility.insistEqual(firstEquationFact.spine,      secondEquationFact.spine);

		return canonicalizer.canonicalize(new LinkingFact(
			firstEquationFact.hadamard,
			firstEquationFact.population,
			firstEquationFact.spine,
			firstEquationFact.partialSum,
			secondEquationFact.partialSum
		));
	}

	static LinkingFact make(
		final int hadamard,
		final int population,
		final int spine,
		final int firstPartialSum,
		final int secondPartialSum
	) {
		return canonicalizer.canonicalize(new LinkingFact(
			hadamard,
			population,
			spine,
			firstPartialSum,
			secondPartialSum
		));
	}

	static LinkingFact makeLeftParent(
		final LinkingFact leftChild,
		final LinkingFact rightChild,
		final PopulationDelta populationDelta
	) {
		Utility.insistSameParity(leftChild.hadamard, rightChild.hadamard);
		Utility.insistEqual(leftChild.population, populationDelta.childPopulation());
		Utility.insistEqual(rightChild.population, populationDelta.childPopulation());
		Utility.insistSameParity(leftChild.spine, rightChild.spine);
		Utility.insistSameParity(leftChild.firstPartialSum, rightChild.firstPartialSum);
		Utility.insistSameParity(leftChild.secondPartialSum, rightChild.secondPartialSum);
		
		return canonicalizer.canonicalize(new LinkingFact(
			(leftChild.hadamard + rightChild.hadamard) / 2,
			populationDelta.leftParentPopulation,
			(leftChild.spine + rightChild.spine) / 2,
			(leftChild.firstPartialSum + rightChild.firstPartialSum) / 2,
			(leftChild.secondPartialSum + rightChild.secondPartialSum) / 2
		));
	}

	static LinkingFact makeRightParent(
		final LinkingFact leftChild,
		final LinkingFact rightChild,
		final PopulationDelta populationDelta
	) {
		Utility.insistSameParity(leftChild.hadamard, rightChild.hadamard);
		Utility.insistEqual(leftChild.population, populationDelta.childPopulation());
		Utility.insistEqual(rightChild.population, populationDelta.childPopulation());
		Utility.insistSameParity(leftChild.spine, rightChild.spine);
		Utility.insistSameParity(leftChild.firstPartialSum, rightChild.firstPartialSum);
		Utility.insistSameParity(leftChild.secondPartialSum, rightChild.secondPartialSum);
		
		return canonicalizer.canonicalize(new LinkingFact(
			(leftChild.hadamard - rightChild.hadamard) / 2,
			populationDelta.leftParentPopulation,
			(leftChild.spine + rightChild.spine) / 2,
			(leftChild.firstPartialSum + rightChild.firstPartialSum) / 2,
			(leftChild.secondPartialSum + rightChild.secondPartialSum) / 2
		));
	}

	EquationFact firstEquationFact() {
		return EquationFact.make(hadamard, population, spine, firstPartialSum);
	}

	EquationFact secondEquationFact() {
		return EquationFact.make(hadamard, population, spine, secondPartialSum);
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(hadamard,         0)
							   ^ DoubleTabulationHashing.primaryHash(population,       1)
							   ^ DoubleTabulationHashing.primaryHash(spine,            2)
							   ^ DoubleTabulationHashing.primaryHash(firstPartialSum,  3)
							   ^ DoubleTabulationHashing.primaryHash(secondPartialSum, 4);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject       != null
			&& getClass()        == otherObject.getClass()
			&& hadamard          == ((LinkingFact) otherObject).hadamard
			&& population        == ((LinkingFact) otherObject).population
			&& spine             == ((LinkingFact) otherObject).spine
			&& firstPartialSum   == ((LinkingFact) otherObject).firstPartialSum
			&& secondPartialSum  == ((LinkingFact) otherObject).secondPartialSum;
	}

	@Override
	public String toString() {
		return "<lf " + hadamard + " " + population + " " + spine + " " + firstPartialSum + " " + secondPartialSum + ">";
	}
}
