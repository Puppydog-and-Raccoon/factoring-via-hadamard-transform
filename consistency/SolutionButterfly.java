package consistency;

import java.util.HashSet;

class SolutionButterfly {
	final PositionButterfly positionButterfly;
	final SolutionBox[][]   solutionBoxes;

	SolutionButterfly(
		final PositionButterfly positionButterfly
	) {
		this.positionButterfly = positionButterfly;
		this.solutionBoxes      = newSupportBoxes(positionButterfly);
	}

	static SolutionBox[][] newSupportBoxes(
		final PositionButterfly positionButterfly
	) {
		final SolutionBox[][] supportBoxes = new SolutionBox[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final PositionBox positionBox = positionButterfly.positionBoxes[boxTier][boxTerm];
				supportBoxes[boxTier][boxTerm] = new SolutionBox(positionBox, supportBoxes);
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

	@Override
	public String toString() {
		return "SolutionButterfly [solutionBoxes=\n" + Utility.toString(solutionBoxes) + "]";
	}
}
