package consistency;

// name is reversed to group support classes alphabetically
// make everything parent-based? no.

// TODO: consider removing population pair

// This only works WITHIN a butterfly.
//  * even though nodes must share population values, they generally have different hadamard values
//  * from butterfly to butterfly, nodes must share hadamard, population, and spine values

class SolutionPopulationFact {
	final short leftParentPopulation;
	final short rightParentPopulation;

	private static final Unique<SolutionPopulationFact> unique = new Unique<SolutionPopulationFact>();

	private SolutionPopulationFact(
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		Utility.insist(leftParentPopulation  >= Short.MIN_VALUE && leftParentPopulation  <= Short.MAX_VALUE, "fix types");
		Utility.insist(rightParentPopulation >= Short.MIN_VALUE && rightParentPopulation <= Short.MAX_VALUE, "fix types");

		this.leftParentPopulation  = (short) leftParentPopulation;
		this.rightParentPopulation = (short) rightParentPopulation;
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
