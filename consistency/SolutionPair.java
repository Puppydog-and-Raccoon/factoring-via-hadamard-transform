package consistency;

import java.util.Objects;

public class SolutionPair {
	public final int leftHadamard;
	public final int rightHadamard;
	public final int parentHadamard;

	public SolutionPair(Position parentPosition, int leftChildHadamard, int rightChildHadamard) {
		Utility.throwIfFalse(parentPosition.wouldMakeValidParentHadamard(leftChildHadamard, rightChildHadamard), "invalid solution pair");

		this.leftHadamard   = leftChildHadamard;
		this.rightHadamard  = rightChildHadamard;
		this.parentHadamard = parentPosition.parentHadamard(leftChildHadamard, rightChildHadamard);
	}

	public SolutionPair(Position parentPosition, SolutionPair leftChild, SolutionPair rightChild) {
		Utility.throwIfFalse(parentPosition.wouldMakeValidParentHadamard(leftChild.parentHadamard, rightChild.parentHadamard), "invalid solution pair");

		this.leftHadamard   = leftChild.parentHadamard;
		this.rightHadamard  = rightChild.parentHadamard;
		this.parentHadamard = parentPosition.parentHadamard(leftChild.parentHadamard, rightChild.parentHadamard);
	}

	public SolutionPair(Position parentPosition, int parentHadamard) {
		Utility.throwIfFalse(parentPosition.hadamardDomain.contains(parentHadamard), "parent hadamard must be in the parent hadamard domain");

		this.leftHadamard   = 2 * parentHadamard;
		this.rightHadamard  = 0;
		this.parentHadamard = parentHadamard;
	}

	@Override
	public boolean equals(Object other) {
		SolutionPair otherPair = (SolutionPair)other;
		return leftHadamard == otherPair.leftHadamard && rightHadamard == otherPair.rightHadamard;
	}

	@Override
	public int hashCode() {
		return Objects.hash(leftHadamard, rightHadamard);
	}

	@Override
	public String toString() {
		return "<leftHadamard = " + leftHadamard + ", rightHadamard = " + rightHadamard + ">";
	}
}
