package consistency;

final class EquationFact {
	final byte  hadamard;
	final short partialSum;

	private static final Unique<EquationFact> unique = new Unique<EquationFact>();

	static final boolean terse = true;

	private EquationFact(
		final int  actualHadamard,
		final long partialSum
	) {
		Utility.insist(actualHadamard >= Byte.MIN_VALUE  && actualHadamard <= Byte.MAX_VALUE,  "fix types");
		Utility.insist(partialSum     >= Short.MIN_VALUE && partialSum     <= Short.MAX_VALUE, "fix types");

		this.hadamard   = (byte)actualHadamard;
		this.partialSum = (short)partialSum;
	}

	static EquationFact newLeaf(final int hadamard, final long partialSum) {
		return unique.unique(new EquationFact(
			hadamard,
			partialSum
		));
	}

	static EquationFact newLeftParent(final EquationFact leftChildFact, final EquationFact rightChildFact) {
		Utility.insistSameParity(leftChildFact.hadamard, rightChildFact.hadamard);

		return unique.unique(new EquationFact(
			(leftChildFact.hadamard + rightChildFact.hadamard) / 2,
			leftChildFact.partialSum + rightChildFact.partialSum
		));
	}

	static EquationFact newRightParent(final EquationFact leftChildFact, final EquationFact rightChildFact) {
		Utility.insistSameParity(leftChildFact.hadamard, rightChildFact.hadamard);

		return unique.unique(new EquationFact(
			(leftChildFact.hadamard - rightChildFact.hadamard) / 2,
			leftChildFact.partialSum + rightChildFact.partialSum
		));
	}

	@Override
	public String toString() {
		return "[" + hadamard + " " + partialSum + "]";
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(hadamard,    0)
			 ^ Integer.rotateLeft(partialSum, 16);
	}

	@Override
	public boolean equals(final Object otherObject) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& hadamard    == ((EquationFact)otherObject).hadamard
			&& partialSum  == ((EquationFact)otherObject).partialSum;
	}
}
