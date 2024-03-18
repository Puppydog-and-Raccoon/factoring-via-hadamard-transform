package consistency;

// NOTE: hadamards can have same or different parities from spines and/or populations, consider (3+1)/2 and (3-1)/2

class SolutionFact {
	final byte hadamard;
	final byte spine;
	final byte population;

	private static final Unique<SolutionFact> unique = new Unique<SolutionFact>();

	private SolutionFact(
		final int hadamard,
		final int spine,
		final int population
	) {
		Utility.insist(hadamard   >= Byte.MIN_VALUE  && hadamard   <= Byte.MAX_VALUE, "fix types");
		Utility.insist(spine      >= Byte.MIN_VALUE  && spine      <= Byte.MAX_VALUE, "fix types");
		Utility.insist(population >= Byte.MIN_VALUE  && population <= Byte.MAX_VALUE, "fix types");

		this.hadamard   = (byte)hadamard;
		this.spine      = (byte)spine;
		this.population = (byte)population;
	}

	// NOTE: this does not check domain
	static boolean couldMakeValidParents(
		final SolutionFact leftChildFact,
		final SolutionFact rightChildFact
	) {
		return (leftChildFact.hadamard & 1) == (rightChildFact.hadamard & 1)
			&& (leftChildFact.spine & 1) == (rightChildFact.spine & 1)
			&& leftChildFact.population == rightChildFact.population;
	}

	static SolutionFact newLeaf(
		final EquationFact equationFact,
		final int          population
	) {
		return unique.unique(new SolutionFact(
			equationFact.hadamard,
			equationFact.hadamard,
			population
		));
	}

	static SolutionFact newLeftParent(
		final SolutionFact           leftChildSolutionFact,
		final SolutionFact           rightChildSolutionFact,
		final SolutionPopulationFact parentPopulationFact
	) {
		Utility.insist(leftChildSolutionFact.population == parentPopulationFact.childPopulation(), "left child population mismatch");
		Utility.insist(rightChildSolutionFact.population == parentPopulationFact.childPopulation(), "right child population mismatch");

		return unique.unique(new SolutionFact(
			(leftChildSolutionFact.hadamard + rightChildSolutionFact.hadamard) / 2,
			(leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2,
			parentPopulationFact.leftParentPopulation
		));
	}

	static SolutionFact newRightParent(
		final SolutionFact           leftChildSolutionFact,
		final SolutionFact           rightChildSolutionFact,
		final SolutionPopulationFact parentPopulationFact
	) {
		Utility.insist(leftChildSolutionFact.population == parentPopulationFact.childPopulation(), "left child population mismatch");
		Utility.insist(rightChildSolutionFact.population == parentPopulationFact.childPopulation(), "right child population mismatch");

		return unique.unique(new SolutionFact(
			(leftChildSolutionFact.hadamard - rightChildSolutionFact.hadamard) / 2,
			(leftChildSolutionFact.spine + rightChildSolutionFact.spine) / 2,
			parentPopulationFact.rightParentPopulation
		));
	}

	static SolutionFact newFact(
		final int hadamard,
		final int spine,
		final int population
	) {
		return unique.unique(new SolutionFact(
			hadamard,
			spine,
			population
		));
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(hadamard,    0)
			 ^ Integer.rotateLeft(spine,      18)
			 ^ Integer.rotateLeft(population, 24);
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& hadamard    == ((SolutionFact) otherObject).hadamard
			&& spine       == ((SolutionFact) otherObject).spine
			&& population  == ((SolutionFact) otherObject).population;
	}

	@Override
	public String toString() {
		return "["
			 + "h=" + hadamard   + " "
			 + "s=" + spine      + " "
			 + "p=" + population
			 + "]";
	}

	public boolean isValidRootFact() {
		return hadamard == population;
	}

	public boolean isValidLeafFact() {
		return hadamard == spine;
	}
}
