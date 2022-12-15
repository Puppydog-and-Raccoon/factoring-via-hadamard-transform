package consistency;

import java.util.HashSet;

public class EquationNode {
	final Position              position;
	final HashSet<EquationPair> validEquationPairs;
	final EquationNode          leftChild;
	final EquationNode          rightChild;

	EquationNode(Position position, EquationNode leftChild, EquationNode rightChild) {
		Utility.throwIfFalse(!position.isInBottomTier, "must be in a parent tier");

		this.position           = position;
		this.validEquationPairs = new HashSet<EquationPair>();
		this.leftChild          = leftChild;
		this.rightChild         = rightChild;
	}

	EquationNode(Position position) {
		Utility.throwIfFalse(position.isInBottomTier, "must be in the bottom tier");

		this.position           = position;
		this.validEquationPairs = new HashSet<EquationPair>();
		this.leftChild          = null;
		this.rightChild         = null;
	}

	void fillUpLeaf(int constraintFactor) {
		Utility.throwIfFalse(position.isInBottomTier, "must be in the bottom tier");

		for(Integer hadamard : position.hadamardDomain) {
			validEquationPairs.add(new EquationPair(hadamard, constraintFactor * hadamard));
		}
	}

	void fillUpParent() {
		Utility.throwIfFalse(!position.isInBottomTier, "must be in a parent tier");

		for(EquationPair leftChildPair : leftChild.validEquationPairs) {
			for(EquationPair rightChildPair : rightChild.validEquationPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildPair, rightChildPair)) {
					EquationPair parentPair = new EquationPair(position, leftChildPair, rightChildPair);
					validEquationPairs.add(parentPair);
				}
			}
		}
	}

	void setPartialSums(long[] validPartialSums) {
		Utility.throwIfFalse(position.isInTopTier, "must be in the top tier");

		validEquationPairs.removeIf(equationPair -> !Utility.contains(equationPair.partialSum, validPartialSums));
	}

	void setHadamard(int hadamard) {
		validEquationPairs.removeIf(equationPair -> equationPair.hadamard != hadamard);
	}

	long numberOfPairs() {
		return validEquationPairs.size();
	}

	void stripDownChildren() {
		Utility.throwIfFalse(!position.isInBottomTier, "must not be in the bottom tier");

		HashSet<EquationPair> validLeftChildEquationPairs = new HashSet<EquationPair>();
		HashSet<EquationPair> validRightChildEquationPairs = new HashSet<EquationPair>();
		for(EquationPair leftChildEquationPair : leftChild.validEquationPairs) {
			for(EquationPair rightChildEquationPair : rightChild.validEquationPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildEquationPair, rightChildEquationPair)) {
					EquationPair parentEquationPair = new EquationPair(position, leftChildEquationPair, rightChildEquationPair);
					if(validEquationPairs.contains(parentEquationPair)) {
						validLeftChildEquationPairs.add(leftChildEquationPair);
						validRightChildEquationPairs.add(rightChildEquationPair);
					}
				}
			}
		}
		leftChild.validEquationPairs.retainAll(validLeftChildEquationPairs);
		rightChild.validEquationPairs.retainAll(validRightChildEquationPairs);
	}

	void stripUpParent() {
		Utility.throwIfFalse(!position.isInBottomTier, "must not be in the bottom tier");

		HashSet<EquationPair> validParentEquationPairs = new HashSet<EquationPair>();
		for(EquationPair leftChildPair : leftChild.validEquationPairs) {
			for(EquationPair rightChildPair : rightChild.validEquationPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildPair, rightChildPair)) {
					validParentEquationPairs.add(new EquationPair(position, leftChildPair, rightChildPair));
				}
			}
		}
		validEquationPairs.retainAll(validParentEquationPairs);
	}

	HashSet<SolutionPair> validSolutionPairsLeaf() {
		Utility.throwIfFalse(position.isInBottomTier, "must not be in the bottom tier");

		HashSet<SolutionPair> solutionPairs = new HashSet<SolutionPair>();
		for(EquationPair equationPair : validEquationPairs) {
			solutionPairs.add(new SolutionPair(position, equationPair.hadamard));
		}
		return solutionPairs;
	}

	HashSet<SolutionPair> validSolutionPairsParent() {
		Utility.throwIfFalse(!position.isInBottomTier, "must not be in the bottom tier");

		HashSet<SolutionPair> solutionPairs = new HashSet<SolutionPair>();
		for(EquationPair leftChildEquationPair : leftChild.validEquationPairs) {
			for(EquationPair rightChildEquationPair : rightChild.validEquationPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildEquationPair, rightChildEquationPair)) {
					EquationPair parentEquationPair = new EquationPair(position, leftChildEquationPair, rightChildEquationPair);
					if(validEquationPairs.contains(parentEquationPair)) {
						solutionPairs.add(new SolutionPair(position, leftChildEquationPair.hadamard, rightChildEquationPair.hadamard));
					}
				}
			}
		}
		return solutionPairs;
	}

	void reverseIntersectLeaf(HashSet<SolutionPair> solutionPairs) {
		Utility.throwIfFalse(position.isInBottomTier, "must not be in the bottom tier");

		HashSet<Integer> validHadamards = new HashSet<Integer>();
		for(SolutionPair solutionPair : solutionPairs) {
			validHadamards.add(new Integer(solutionPair.parentHadamard));
		}
		validEquationPairs.removeIf(equationPair -> !validHadamards.contains(new Integer(equationPair.hadamard)));
	}

	void reverseIntersectParent(HashSet<SolutionPair> solutionPairs) {
		Utility.throwIfFalse(!position.isInBottomTier, "must not be in the bottom tier");

		HashSet<EquationPair> validParentPairs = new HashSet<EquationPair>();
		for(EquationPair leftChildEquationPair : leftChild.validEquationPairs) {
			for(EquationPair rightChildEquationPair : rightChild.validEquationPairs) {
				if(position.wouldMakeValidParentHadamard(leftChildEquationPair, rightChildEquationPair)) {
					EquationPair parentEquationPair = new EquationPair(position, leftChildEquationPair, rightChildEquationPair);
					if(validEquationPairs.contains(parentEquationPair)) {
						SolutionPair solutionPair = new SolutionPair(position, leftChildEquationPair.hadamard, rightChildEquationPair.hadamard);
						if(solutionPairs.contains(solutionPair)) {
							validParentPairs.add(parentEquationPair);
						}
					}
				}
			}
		}
		validEquationPairs.retainAll(validParentPairs);
	}
}
