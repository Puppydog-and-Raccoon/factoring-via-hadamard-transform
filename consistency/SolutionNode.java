package consistency;

import java.util.HashSet;

public class SolutionNode {
	final Position              position;
	final HashSet<SolutionPair> validSolutionPairs;
	final SolutionNode          leftChild;
	final SolutionNode          rightChild;

	SolutionNode(Position position, SolutionNode leftChild, SolutionNode rightChild) {
		Utility.throwIfFalse(!position.isInBottomTier, "must be in a parent tier");

		this.position           = position;
		this.validSolutionPairs = new HashSet<SolutionPair>();
		this.leftChild          = leftChild;
		this.rightChild         = rightChild;
	}

	SolutionNode(Position position) {
		Utility.throwIfFalse(position.isInBottomTier, "must be in the bottom tier");

		this.position           = position;
		this.validSolutionPairs = new HashSet<SolutionPair>();
		this.leftChild          = null;
		this.rightChild         = null;
	}

	public void fillUpLeaf() {
		for(int hadamard : position.hadamardDomain) {
			validSolutionPairs.add(new SolutionPair(position, hadamard));
		}
	}

	public void fillUpParent() {
		for(SolutionPair leftChildSolutionPair : leftChild.validSolutionPairs) {
			for(SolutionPair rightChildSolutionPair : rightChild.validSolutionPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildSolutionPair, rightChildSolutionPair)) {
					validSolutionPairs.add(new SolutionPair(position, leftChildSolutionPair, rightChildSolutionPair));
				}
			}
		}
	}

	long numberOfPairs() {
		return validSolutionPairs.size();
	}

	Integer chooseHadamard() {
		Utility.throwIfFalse(position.isInTopTier, "must be in the top tier");

		final boolean[] found = new boolean[] {false, false};
		for(SolutionPair solutionPair : validSolutionPairs) {
			found[solutionPair.parentHadamard] = true;
		}
		final Integer choice = found[0] ? new Integer(0) : found[1] ? new Integer(1) : null;
		// System.out.println("in choose hadamard, term = " + position.term + ": found = {" + found[0] + ", " + found[1] + "} and choice = " + choice);
		return choice;
	}

	void assignHadamard(int hadamard) {
		validSolutionPairs.removeIf(solutionPair -> solutionPair.parentHadamard != hadamard);
	}

	void intersectSolutionPairs(HashSet<SolutionPair> otherSolutionPairs) {
		validSolutionPairs.retainAll(otherSolutionPairs);
	}
}
