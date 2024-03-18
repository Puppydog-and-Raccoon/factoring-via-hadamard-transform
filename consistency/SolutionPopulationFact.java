package consistency;

// name is reversed to group support classes alphabetically
// make everything parent-based? no.

// TODO: consider removing population pair

// This only works WITHIN a butterfly.
//  * even though nodes must share population values, they generally have different hadamard values
//  * from butterfly to butterfly, nodes must share hadamard, population, and spine values

class SolutionPopulationFact {
	final byte leftParentPopulation;
	final byte rightParentPopulation;

	private static final Unique<SolutionPopulationFact> unique = new Unique<SolutionPopulationFact>();

	private SolutionPopulationFact(
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		Utility.insist(leftParentPopulation  >= Byte.MIN_VALUE && leftParentPopulation  <= Byte.MAX_VALUE, "fix types");
		Utility.insist(rightParentPopulation >= Byte.MIN_VALUE && rightParentPopulation <= Byte.MAX_VALUE, "fix types");

		this.leftParentPopulation  = (byte) leftParentPopulation;
		this.rightParentPopulation = (byte) rightParentPopulation;
	}

	int childPopulation() {
		return leftParentPopulation + rightParentPopulation;
	}

	static SolutionPopulationFact newFact(
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		return unique.unique(new SolutionPopulationFact(
			leftParentPopulation,
			rightParentPopulation
		));
	}

	public static SolutionPopulationFact[] allParentPopulationFacts(
		final int boxTier,
		final int childPopulation
	) {
		final int lowestParentPopulationForPosition         = 0;
		final int highestParentPopulationForPosition        = 1 << boxTier;
		final int lowestParentPopulationForChildPopulation  = Math.max(lowestParentPopulationForPosition,  childPopulation - highestParentPopulationForPosition);
		final int highestParentPopulationForChildPopulation = Math.min(highestParentPopulationForPosition, childPopulation - lowestParentPopulationForPosition);
		final int numberOfParentPopulationFacts             = highestParentPopulationForChildPopulation - lowestParentPopulationForChildPopulation + 1;

		final SolutionPopulationFact[] parentPopulationFacts = new SolutionPopulationFact[numberOfParentPopulationFacts];
		for(final int i : Utility.enumerateAscending(numberOfParentPopulationFacts)) {
			Utility.insist(lowestParentPopulationForChildPopulation + i >= 0, "must be non-negative");
			Utility.insist(lowestParentPopulationForChildPopulation <= childPopulation, "must be less than child population");
			Utility.insist(highestParentPopulationForChildPopulation - i >= 0, "must be non-negative");
			Utility.insist(highestParentPopulationForChildPopulation <= childPopulation, "must be less than child population");
			parentPopulationFacts[i] = new SolutionPopulationFact(lowestParentPopulationForChildPopulation + i, highestParentPopulationForChildPopulation - i);
		}
		return parentPopulationFacts;
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(leftParentPopulation,   0)
			 ^ Integer.rotateLeft(rightParentPopulation, 16);
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject           != null
			&& getClass()            == otherObject.getClass()
			&& leftParentPopulation  == ((SolutionPopulationFact) otherObject).leftParentPopulation
			&& rightParentPopulation == ((SolutionPopulationFact) otherObject).rightParentPopulation;
	}

	@Override
	public String toString() {
		return "<spf "
			 + "lpp=" + leftParentPopulation  + " "
			 + "rpp=" + rightParentPopulation
			 + ">";
	}
}
