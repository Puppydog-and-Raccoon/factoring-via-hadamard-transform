package consistency;

import java.util.Objects;

public class EquationPair {
	final int  hadamard;
	final long partialSum;

	EquationPair(int hadamard, long partialSum) {
		this.hadamard   = hadamard;
		this.partialSum = partialSum;
	}

	EquationPair(Position parentPosition, EquationPair leftChildPair, EquationPair rightChildPair) {
		this.hadamard   = parentPosition.parentHadamard(leftChildPair.hadamard, rightChildPair.hadamard);
		this.partialSum = leftChildPair.partialSum + rightChildPair.partialSum;
	}

	@Override
	public boolean equals(Object other) {
		EquationPair otherPair = (EquationPair)other;
		return hadamard == otherPair.hadamard && partialSum == otherPair.partialSum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hadamard, partialSum);
	}

	@Override
	public String toString() {
		return "<hadamard = " + hadamard + ", partialSum = " + partialSum + ">";
	}
}
