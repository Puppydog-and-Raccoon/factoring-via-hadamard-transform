package consistency;

class EquationButterfly {
	final Problem          problem;
	final Constraint       constraint;
	final int              numberOfTiers;
	final int              numberOfTerms;
	final EquationNode[][] nodes;

	private EquationButterfly(Problem problem, PositionButterfly positionButterfly, Constraint constraint) {
		this.problem       = problem;
		this.constraint    = constraint;
		this.numberOfTiers = positionButterfly.numberOfTiers;
		this.numberOfTerms = positionButterfly.numberOfTerms;
		this.nodes         = makeNodes(positionButterfly);
	}

	void fill() {
		fillUpLeaves();
		fillUpParents();
	}

	void fillUpLeaves() {
		final int[] fhtOfConstraintVector = constraint.fhtOfConstraintVector();
		final int tier = numberOfTiers - 1;
		for(int term = 0; term < numberOfTerms; term++) {
			nodes[tier][term].fillUpLeaf(fhtOfConstraintVector[term]);
		}
	}

	void fillUpParents() {
		for(int tier = numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].fillUpParent();
			}
		}
	}

	void limit() {
		limitRootNodes();
		limitPopulationNode();
	}

	void limitRootNodes() {
		final long[] validPartialSums = constraint.validPartialSums();
		for(int term = 0; term < numberOfTerms; term++) {
			nodes[0][term].setPartialSums(validPartialSums);
		}
	}

	void limitPopulationNode() {
		final int population = problem.numberOfTrues;
		nodes[numberOfTiers - 1][0].setHadamard(population);
	}

	void wring() {
		for(int loopCount = 0; loopCount <= 2 * numberOfTiers; loopCount++) {
			boolean unchanged = wringOnceReturnUnchanged();
			if(unchanged) {
				return;
			}
		}
		Utility.throwAlways("too many loops in wring()");
	}

	boolean wringOnceReturnUnchanged() {
		long populationBeforeStrips = numberOfPairs();
		stripDownChildren();
		stripUpParent();
		long populationAfterStrips = numberOfPairs();
		return populationBeforeStrips == populationAfterStrips;
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

	void stripDownChildren() {
		for(int tier = 0; tier < numberOfTiers - 1; tier++) {
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].stripDownChildren();
			}
		}
	}

	void stripUpParent() {
		for(int tier = numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < numberOfTerms; term++) {
				nodes[tier][term].stripUpParent();
			}
		}
	}

	static EquationButterfly makeButterfly(Problem problem, PositionButterfly positionButterfly, Constraint constraint) {
		EquationButterfly butterfly = new EquationButterfly(problem, positionButterfly, constraint);
		butterfly.fill();
		butterfly.limit();
		butterfly.wring();
		return butterfly;
	}

	static EquationButterfly makeButterflyForTest(Problem problem, PositionButterfly positionButterfly, Constraint constraint) {
		return new EquationButterfly(problem, positionButterfly, constraint);
	};

	static EquationNode[][] makeNodes(PositionButterfly positionButterfly) {
		final EquationNode[][] nodes = new EquationNode[positionButterfly.numberOfTiers][positionButterfly.numberOfTerms];
		makeLeafNodes(positionButterfly, nodes);
		makeParentNodes(positionButterfly, nodes);
		return nodes;
	}

	private static void makeParentNodes(PositionButterfly positionButterfly, EquationNode[][] nodes) {
		for(int tier = positionButterfly.numberOfTiers - 2; tier >= 0; tier--) {
			for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
				EquationNode leftChild = nodes[tier + 1][Utility.leftChildTerm(tier, term)];
				EquationNode rightChild = nodes[tier + 1][Utility.rightChildTerm(tier, term)];
				nodes[tier][term] = new EquationNode(positionButterfly.positions[tier][term], leftChild, rightChild);
			}
		}
	}

	private static void makeLeafNodes(PositionButterfly positionButterfly, EquationNode[][] nodes) {
		final int tier = positionButterfly.numberOfTiers - 1;
		for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
			nodes[tier][term] = new EquationNode(positionButterfly.positions[tier][term]);
		}
	}
}
