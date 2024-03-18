package consistency;

// TODO: use "index" suffix
// TODO: public

final class EquationButterfly {
	final PositionButterfly     positionButterfly;
	final ConsistencyConstraint consistencyConstraint;
	final EquationNode[][]      equationNodes;
	final EquationBox[][]       equationBoxes;
	final Integer               numberOfTruesInProblem;

	EquationButterfly(
		final PositionButterfly     positionButterfly,
		final ConsistencyConstraint consistencyConstraint,
		final Integer               numberOfTruesInProblem
	) {
		final EquationNode[][] equationNodes = newNodes(consistencyConstraint, positionButterfly);
		final EquationBox[][]  equationBoxes = newBoxes(equationNodes, positionButterfly);

		this.positionButterfly      = positionButterfly;
		this.consistencyConstraint  = consistencyConstraint;
		this.equationNodes          = equationNodes;
		this.equationBoxes          = equationBoxes;
		this.numberOfTruesInProblem = numberOfTruesInProblem;
//		System.out.println("**** number of trues = " + numberOfTruesInProblem);
	}

	void fill() {
		fillLeaves();
		fillParents();
		stripRootNodes();
		stripPopulationNode();
//		System.out.println(this);
		wringBoxesUntilNoChange();
	}

	void wringBoxesUntilNoChange() {
		while(wringBoxesOnce()) {
//			System.out.println("**** wring up and down\n" + this);
		}
	}

	boolean wringBoxesOnce() {
		final boolean topDownChanged  = wringBoxesTopDown();
		final boolean bottomUpChanged = wringBoxesBottomUp();
		return topDownChanged || bottomUpChanged;
	}

	boolean wringBoxesBottomUp() {
		boolean anyBoxChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final boolean thisBoxChanged = equationBoxes[boxTier][boxTerm].removeInvalidParentFacts();
				anyBoxChanged = anyBoxChanged || thisBoxChanged;
			}
		}
		return anyBoxChanged;
	}

	boolean wringBoxesTopDown() {
		boolean anyBoxChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final boolean thisBoxChanged = equationBoxes[boxTier][boxTerm].removeInvalidChildFacts();
				anyBoxChanged = anyBoxChanged || thisBoxChanged;
			}
		}
		return anyBoxChanged;
	}

	void stripRootNodes() {
		final EquationNode[] rootNodes = equationNodes[0];
		for(final int rootTerm : positionButterfly.nodeTermIndices) {
			rootNodes[rootTerm].removeInvalidPartialSums(consistencyConstraint.totalSum());
		}
	}

	void stripPopulationNode() {
		if(numberOfTruesInProblem != null) {
			final int populationNodeTier = positionButterfly.leafNodeTier;
			final int populationNodeTerm = 0;
			equationNodes[populationNodeTier][populationNodeTerm].removeInvalidNumbersOfTrues(numberOfTruesInProblem.intValue());
		}
	}

	void fillLeaves() {
		final int[] constraintVector = consistencyConstraint.constraintVector();
		final int   totalSum         = consistencyConstraint.totalSum();
//		System.out.println("totalSums = " + Arrays.toString(totalSums));
		final EquationNode[] leafNodes = equationNodes[equationNodes.length - 1];
		for(final int leafTerm : positionButterfly.nodeTermIndices) {
			leafNodes[leafTerm].fillLeaf(constraintVector[leafTerm], totalSum);
		}
	}

	void fillParents() {
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				equationBoxes[boxTier][boxTerm].addPossibleParentFacts();
			}
		}
	}

	boolean isValid(final int[] rootHadamards) {
		Utility.insist(rootHadamards.length == equationNodes[0].length, "vector is wrong size: " + rootHadamards.length + " != " + equationNodes[0].length);

		final int[][]    hadamards     = Utility.sylvesterButterfly(rootHadamards);
		final Object[][] equationFacts = new Object[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];

		boolean valid = true;
		for(final int leafTerm : positionButterfly.nodeTermIndices) {
			equationNodes[positionButterfly.leafNodeTier][leafTerm].computeValidLeafFacts(equationFacts, hadamards);
			valid = valid && equationNodes[positionButterfly.leafNodeTier][leafTerm].leafIsValid(equationFacts);
		}
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				equationBoxes[boxTier][boxTerm].computeValidParentFacts(equationFacts);
				valid = valid && equationBoxes[boxTier][boxTerm].parentsAreValid(equationFacts);
			}
		}
		return valid;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(consistencyConstraint);
		buffer.append("\n");
		for(int tier = 0; tier < equationNodes.length; tier++) {
			for(int term = 0; term < equationNodes[tier].length; term++) {
				buffer.append(equationNodes[tier][term]);
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	static EquationNode[][] newNodes(final ConsistencyConstraint constraint, final PositionButterfly positionButterfly) {
		final EquationNode[][] equationNodes = new EquationNode[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int tier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int term : positionButterfly.nodeTermIndices) {
				equationNodes[tier][term] = new EquationNode(positionButterfly.positionNodes[tier][term]);
			}
		}
		return equationNodes;
	}

	static EquationBox[][] newBoxes(final EquationNode[][] equationNodes, final PositionButterfly positionButterfly) {
		final EquationBox[][] boxes = new EquationBox[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				boxes[boxTier][boxTerm] = new EquationBox(equationNodes, positionButterfly.positionBoxes[boxTier][boxTerm], boxes);
			}
		}
		return boxes;
	}
}
