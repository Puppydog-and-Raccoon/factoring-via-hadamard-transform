package consistency;

public class SolutionButterfly {
	public final int numberOfTiers;
	public final int numberOfTerms;
	public final SolutionNode[][] nodes;

	SolutionButterfly(PositionButterfly positionButterfly) {
		this.numberOfTiers = positionButterfly.numberOfTiers;
		this.numberOfTerms = positionButterfly.numberOfTerms;
		this.nodes         = makeNodes(positionButterfly);
	}

	void fill() {
		fillLeaves();
		fillParents();
	}

	void fillLeaves() {
		final int tier = numberOfTiers - 1;
		for(int term = 0; term < numberOfTerms; term++) {
			nodes[tier][term].fillUpLeaf();
		}
	}

	void fillParents() {
		for(int tier = numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].fillUpParent();
			}
		}
	}

	long numberOfPairs() {
		long result = 0;
		for(int tier = 0; tier < numberOfTiers; tier++) {
			for(int term = 0; term < numberOfTerms; term++) {
				result += nodes[tier][term].numberOfPairs();
			}
		}
		return result;
	}

	void mergeInto(EquationButterfly equationButterfly) {
		{
			int tier = numberOfTiers - 1;
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].intersectSolutionPairs(equationButterfly.nodes[tier][term].validSolutionPairsLeaf());
				equationButterfly.nodes[tier][term].reverseIntersectLeaf(nodes[tier][term].validSolutionPairs);
			}
		}
		for(int tier = numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].intersectSolutionPairs(equationButterfly.nodes[tier][term].validSolutionPairsParent());
				equationButterfly.nodes[tier][term].reverseIntersectParent(nodes[tier][term].validSolutionPairs);
			}
		}
	}

	static SolutionButterfly makeButterfly(PositionButterfly positionButterfly) {
		SolutionButterfly mergeButterfly = new SolutionButterfly(positionButterfly);
		mergeButterfly.fill();
		return mergeButterfly;
	}

	static SolutionNode[][] makeNodes(PositionButterfly positionButterfly) {
		SolutionNode[][] results = new SolutionNode[positionButterfly.numberOfTiers][positionButterfly.numberOfTerms];
		for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
			results[positionButterfly.numberOfTiers - 1][term] = new SolutionNode(positionButterfly.positions[positionButterfly.numberOfTiers - 1][term]);
		}
		for(int tier = positionButterfly.numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
				final SolutionNode leftChild = results[tier + 1][Utility.leftChildTerm(tier, term)];
				final SolutionNode rightChild = results[tier + 1][Utility.rightChildTerm(tier, term)];
				results[tier][term] = new SolutionNode(positionButterfly.positions[tier][term], leftChild, rightChild);
			}
		}
		return results;
	}
}
