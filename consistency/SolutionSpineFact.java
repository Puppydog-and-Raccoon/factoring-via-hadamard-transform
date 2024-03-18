package consistency;

// name is reversed to group support classes alphabetically

// This only works WITHIN a butterfly.
// * even though nodes must share population values, they generally have different hadamard values
// * from butterfly to butterfly, nodes must share hadamard, population, and spine values

public class SolutionSpineFact {
	final byte leftChildSpine;
	final byte rightChildSpine;

	private static final Unique<SolutionSpineFact> unique = new Unique<SolutionSpineFact>();

	private SolutionSpineFact(
		final int leftChildSpine,
		final int rightChildSpine
	) {
		Utility.insist(leftChildSpine  >= Byte.MIN_VALUE && leftChildSpine  <= Byte.MAX_VALUE, "fix types");
		Utility.insist(rightChildSpine >= Byte.MIN_VALUE && rightChildSpine <= Byte.MAX_VALUE, "fix types");

		this.leftChildSpine  = (byte) leftChildSpine;
		this.rightChildSpine = (byte) rightChildSpine;
	}

	int parentSpine() {
		return (leftChildSpine + rightChildSpine) / 2;
	}

	static SolutionSpineFact newFact(
		final SolutionFact leftChildSolutionFact,
		final SolutionFact rightChildSolutionFact
	) {
		return unique.unique(new SolutionSpineFact(
			leftChildSolutionFact.spine,
			rightChildSolutionFact.spine
		));
	}

	static SolutionSpineFact newFact(
		final int leftChildSpine,
		final int rightChildSpine
	) {
		Utility.insistSameParity(leftChildSpine, rightChildSpine);

		return unique.unique(new SolutionSpineFact(
			leftChildSpine,
			rightChildSpine
		));
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(leftChildSpine,   0)
			 ^ Integer.rotateLeft(rightChildSpine, 16);
	}

	@Override
	public boolean equals(final Object otherObject) {
		return otherObject     != null
			&& getClass()      == otherObject.getClass()
			&& leftChildSpine  == ((SolutionSpineFact) otherObject).leftChildSpine
			&& rightChildSpine == ((SolutionSpineFact) otherObject).rightChildSpine;
	}

	@Override
	public String toString() {
		return "<ssf "
			 + "lcs=" + leftChildSpine  + " "
			 + "rcs=" + rightChildSpine
			 + ">";
	}
}
