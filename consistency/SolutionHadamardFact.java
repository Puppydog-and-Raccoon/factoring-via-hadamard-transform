package consistency;

// TODO: rename this SupportCanonicalFact or something

public class SolutionHadamardFact {
	final byte leftChildHadamard;
	final byte rightChildHadamard;
	final byte leftChildSpine;
	final byte rightChildSpine;
	final byte leftParentPopulation;
	final byte rightParentPopulation;

	private static final Unique<SolutionHadamardFact> unique = new Unique<SolutionHadamardFact>();

	private SolutionHadamardFact(
		final int leftChildHadamard,
		final int rightChildHadamard,
		final int leftChildSpine,
		final int rightChildSpine,
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		Utility.insist(leftChildHadamard     >= Byte.MIN_VALUE && leftChildHadamard     <= Byte.MAX_VALUE, "fix types");
		Utility.insist(rightChildHadamard    >= Byte.MIN_VALUE && rightChildHadamard    <= Byte.MAX_VALUE, "fix types");
		Utility.insist(leftChildSpine        >= Byte.MIN_VALUE && leftChildSpine        <= Byte.MAX_VALUE, "fix types");
		Utility.insist(rightChildSpine       >= Byte.MIN_VALUE && rightChildSpine       <= Byte.MAX_VALUE, "fix types");
		Utility.insist(leftParentPopulation  >= Byte.MIN_VALUE && leftParentPopulation  <= Byte.MAX_VALUE, "fix types");
		Utility.insist(rightParentPopulation >= Byte.MIN_VALUE && rightParentPopulation <= Byte.MAX_VALUE, "fix types");

		this.leftChildHadamard     = (byte) leftChildHadamard;
		this.rightChildHadamard    = (byte) rightChildHadamard;
		this.leftChildSpine        = (byte) leftChildSpine;
		this.rightChildSpine       = (byte) rightChildSpine;
		this.leftParentPopulation  = (byte) leftParentPopulation;
		this.rightParentPopulation = (byte) rightParentPopulation;
	}

	int leftParentHadamard() {
		return (leftChildHadamard + rightChildHadamard) / 2;
	}

	int rightParentHadamard() {
		return (leftChildHadamard - rightChildHadamard) / 2;
	}

	static SolutionHadamardFact newFact(
		final SolutionFact           leftChildSolutionFact,
		final SolutionFact           rightChildSolutionFact,
		final SolutionPopulationFact parentPopulationFact
	) {
		Utility.insistSameParity(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard);
		Utility.insist(leftChildSolutionFact.population == parentPopulationFact.childPopulation(), "left child population must match");
		Utility.insist(rightChildSolutionFact.population == parentPopulationFact.childPopulation(), "right child population must match");
		// TODO: verify that parent spines are equal

		return unique.unique(new SolutionHadamardFact(
			leftChildSolutionFact.hadamard,
			rightChildSolutionFact.hadamard,
			leftChildSolutionFact.spine,
			rightChildSolutionFact.spine,
			parentPopulationFact.leftParentPopulation,
			parentPopulationFact.rightParentPopulation
		));
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(leftChildHadamard,      0)
			 ^ Integer.rotateLeft(rightChildHadamard,     5)
			 ^ Integer.rotateLeft(leftChildSpine,        10)
			 ^ Integer.rotateLeft(rightChildSpine,       15)
			 ^ Integer.rotateLeft(leftParentPopulation,  20)
			 ^ Integer.rotateLeft(rightParentPopulation, 25);
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject           != null
			&& getClass()            == otherObject.getClass()
			&& leftChildHadamard     == ((SolutionHadamardFact) otherObject).leftChildHadamard
			&& rightChildHadamard    == ((SolutionHadamardFact) otherObject).rightChildHadamard
			&& leftChildSpine        == ((SolutionHadamardFact) otherObject).leftChildSpine
			&& rightChildSpine       == ((SolutionHadamardFact) otherObject).rightChildSpine
			&& leftParentPopulation  == ((SolutionHadamardFact) otherObject).leftParentPopulation
			&& rightParentPopulation == ((SolutionHadamardFact) otherObject).rightParentPopulation;
	}

	@Override
	public String toString() {
		return "<shf "
			 + "lch=" + leftChildHadamard     + " "
			 + "rch=" + rightChildHadamard    + " "
			 + "lcs=" + leftChildSpine        + " "
			 + "rcs=" + rightChildSpine       + " "
			 + "lpp=" + leftParentPopulation  + " "
			 + "rpp=" + rightParentPopulation
			 + ">";
	}
}
