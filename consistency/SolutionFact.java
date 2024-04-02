package consistency;

// NOTE: hadamards can have same or different parities from spines and/or populations, consider (3+1)/2 and (3-1)/2

class SolutionFact {
	final short hadamard;
	final short spine;
	final short population;

	private static final Unique<SolutionFact> unique = new Unique<SolutionFact>();

	private SolutionFact(
		final int hadamard,
		final int spine,
		final int population
	) {
		Utility.insist(hadamard   >= Short.MIN_VALUE  && hadamard   <= Short.MAX_VALUE, "fix types");
		Utility.insist(spine      >= Short.MIN_VALUE  && spine      <= Short.MAX_VALUE, "fix types");
		Utility.insist(population >= Short.MIN_VALUE  && population <= Short.MAX_VALUE, "fix types");

		this.hadamard   = (short)hadamard;
		this.spine      = (short)spine;
		this.population = (short)population;
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
