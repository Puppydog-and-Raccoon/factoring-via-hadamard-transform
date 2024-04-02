package consistency;

import java.util.HashSet;

class SolutionButterfly {
	final PositionButterfly  positionButterfly;
	final SolutionNode[][]   solutionNodes;
	final SolutionBox[][]    solutionBoxes;
	final ConsistencyProblem consistencyProblem;

	SolutionButterfly(
		final PositionButterfly  positionButterfly,
		final ConsistencyProblem consistencyProblem
	) {
		final SolutionNode[][] solutionNodes = newSolutionNodes(positionButterfly);
		final SolutionBox[][] solutionBoxes  = newSupportBoxes(positionButterfly, solutionNodes);

		this.positionButterfly  = positionButterfly;
		this.solutionNodes      = solutionNodes;
		this.solutionBoxes      = solutionBoxes;
		this.consistencyProblem = consistencyProblem;
	}

	static SolutionNode[][] newSolutionNodes(
		final PositionButterfly positionButterfly
	) {
		final SolutionNode[][] solutionNodes = new SolutionNode[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				final PositionNode positionNode = positionButterfly.positionNodes[nodeTier][nodeTerm];
				solutionNodes[nodeTier][nodeTerm] = new SolutionNode(positionNode);
			}
		}
		return solutionNodes;
	}

	// TOD: rename support -> solution
	static SolutionBox[][] newSupportBoxes(
		final PositionButterfly positionButterfly,
		final SolutionNode[][]  solutionNodes
	) {
		final SolutionBox[][] supportBoxes = new SolutionBox[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final PositionBox positionBox = positionButterfly.positionBoxes[boxTier][boxTerm];
				supportBoxes[boxTier][boxTerm] = new SolutionBox(positionBox, solutionNodes, supportBoxes);
			}
		}
		return supportBoxes;
	}

	int[] leafSpines() {
		final int[] spines = new int[positionButterfly.numberOfNodeTerms];
		boolean allSpinesFound = true;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			final SolutionBox leafSupportBox = solutionBoxes[positionButterfly.leafBoxTier][boxTerm];
			final boolean theseSpinesFound = leafSupportBox.extractLeafSpines(spines);
			allSpinesFound = allSpinesFound && theseSpinesFound;
		}
		return allSpinesFound ? spines : null;
	}

	boolean isValid(int[] leafVector) {
		final ButterflyOfHashSets<SolutionFact> solutionFactButterfly = new ButterflyOfHashSets<SolutionFact>(positionButterfly);

		// do leaves
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			final PositionNode positionNode  = positionButterfly.positionNodes[positionButterfly.leafNodeTier][leafNodeTerm];
			final HashSet<SolutionFact> solutionFacts = solutionFactButterfly.hashSetAt(positionButterfly.leafNodeTier, leafNodeTerm);
			addAllSolutionFacts(positionNode, solutionFacts, leafVector[leafNodeTerm]);
		}

		// do boxes
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final SolutionBox solutionBox = solutionBoxes[boxTier][boxTerm];
				solutionBox.rippleUp(solutionFactButterfly);
			}
		}

		// do roots
		boolean allRootsAreValid = true;
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			final HashSet<SolutionFact> solutionFacts = solutionFactButterfly.hashSetAt(positionButterfly.rootNodeTier, leafNodeTerm);
			allRootsAreValid = allRootsAreValid && !solutionFacts.isEmpty();
		}
		return allRootsAreValid;
	}

	void addAllSolutionFacts(
		final PositionNode          positionNode,
		final HashSet<SolutionFact> solutionFacts,
		final int                   hadamard
	) {
		for(final int population : positionNode.canonicalPopulations()) {
			solutionFacts.add(SolutionFact.newFact(hadamard, hadamard, population));
		}
	}

	void applyConstants() {
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			if(consistencyConstraint.isConstant()) {
				final int value = consistencyConstraint.numberOfTruesInConstraint == 0 ? 0 : 1;
				for(final int decisionId : consistencyConstraint.sortedAndUniqueDecisionIds) {
					solutionBoxes[0][decisionId / 2].assignRoot(decisionId, value);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "SolutionButterfly [solutionBoxes=\n" + Utility.toString(solutionBoxes) + "]";
	}

	public String status() {
		final StringBuffer buffer = new StringBuffer();
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				if(solutionNodes[nodeTier][nodeTerm].solutionFacts.isEmpty()) {
					buffer.append("_");
				} else {
					buffer.append("+");
				}
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	boolean wringTierBothDirections(final int boxTier) {
		boolean anythingChanged = false;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			final SolutionBox solutionBox = solutionBoxes[boxTier][boxTerm];
			final boolean thisBoxChanged = solutionBox.intersectEverythingBothRelationsXXX();
			anythingChanged = anythingChanged || thisBoxChanged;
		}
		return anythingChanged;
	}

	boolean saturateSolutionLeaves() {
		boolean anythingChanged = false;
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			solutionNodes[positionButterfly.leafNodeTier][leafNodeTerm].fillLeaf();
			anythingChanged = true;
		}
		return anythingChanged;
	}

	boolean wringOnce() {
		System.out.println("wring once XXX");
	
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final SolutionBox solutionBox = solutionBoxes[boxTier][boxTerm];
				final boolean thisChanged = solutionBox.intersectEverythingBothRelationsXXX();
				anythingChanged = anythingChanged || thisChanged;
			}
			{
				final boolean transformChanged = wringTierBothDirections(boxTier);
				anythingChanged = anythingChanged || transformChanged;
			}
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final SolutionBox solutionBox = solutionBoxes[boxTier][boxTerm];
				final boolean thisChanged = solutionBox.intersectEverythingBothRelationsXXX();
				anythingChanged = anythingChanged || thisChanged;
			}
			{
				final boolean transformChanged = wringTierBothDirections(boxTier);
				anythingChanged = anythingChanged || transformChanged;
			}
		}
		return anythingChanged;
	}

	void wringUntilNoChange() {
		while(wringOnce()) {
		}
	}
}
