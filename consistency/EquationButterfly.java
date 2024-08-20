package consistency;

final class EquationButterfly {
	final PropertyButterfly     propertyButterfly;
	final ConsistencyConstraint consistencyConstraint;
	final EquationNode[][]      equationNodes;
	final EquationBox[][]       equationBoxes;

	EquationButterfly(
		final PropertyButterfly     propertyButterfly,
		final ConsistencyConstraint consistencyConstraint
	) {
		final EquationNode[][] equationNodes = makeNodes(consistencyConstraint, propertyButterfly);
		final EquationBox[][]  equationBoxes = makeBoxes(propertyButterfly, equationNodes);

		this.propertyButterfly     = propertyButterfly;
		this.consistencyConstraint = consistencyConstraint;
		this.equationNodes         = equationNodes;
		this.equationBoxes         = equationBoxes;
	}

	void fill() {
		fill(null);
	}

	void fill(
		final SimpleHashSet<ConsistencyConstraint> consistencyConstraints
	) {
		fillLeafNodes();
		fillParentBoxes();
		stripTotalSums();
		stripConstantConstraints(consistencyConstraints);
		wringUntilNoChange();
	}

	boolean wringUntilNoChange() {
		boolean anythingChanged = false;
		while(wringOnce()) {
			anythingChanged = true;
		}
		return anythingChanged;
	}

	boolean wringOnce() {
		boolean anythingChanged = false;
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				final boolean thisBoxChanged = equationBoxes[boxTier][boxTerm].wringEquationBox();
				anythingChanged = anythingChanged || thisBoxChanged;
			}
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				final boolean thisBoxChanged = equationBoxes[boxTier][boxTerm].wringEquationBox();
				anythingChanged = anythingChanged || thisBoxChanged;
			}
		}
		return anythingChanged;
	}

	boolean stripTotalSums() {
		boolean anythingChanged = false;
		for(final int rootNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			final boolean thisChanged = equationNodes[propertyButterfly.rootNodeTier][rootNodeTerm].removeInvalidPartialSums();
			anythingChanged = anythingChanged || thisChanged;
		}
		return anythingChanged;
	}

	void fillLeafNodes() {
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			equationNodes[propertyButterfly.leafNodeTier][leafNodeTerm].fillLeaf();
		}
	}

	void fillParentBoxes() {
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				equationBoxes[boxTier][boxTerm].addAllPossibleParentFacts();
			}
		}
	}

	boolean areValidRootHadamards(final int[] rootHadamards) {
		Utility.insistEqual(rootHadamards.length, equationNodes[0].length);

		final int[] leafHadamards = Utility.fastSylvesterTransform(rootHadamards);
		return areValidLeafHadamards(leafHadamards);
	}

	boolean areValidLeafHadamards(final int[] leafHadamards) {
		Utility.insistEqual(leafHadamards.length, equationNodes[0].length);

		final DebugButterfly<EquationFact> equationFactsButterfly = new DebugButterfly<EquationFact>(propertyButterfly);

		boolean valid = true;
		for(final int leafTerm : propertyButterfly.nodeTermIndicesRandomly()) {
			final boolean nodeIsValid = equationNodes[propertyButterfly.leafNodeTier][leafTerm].rippleUpLeafFacts(equationFactsButterfly, leafHadamards);
			valid = valid && nodeIsValid;
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesRandomly()) {
				final boolean boxIsValid = equationBoxes[boxTier][boxTerm].rippleUpBoxFacts(equationFactsButterfly);
				valid = valid && boxIsValid;
			}
		}
		return valid;
	}

	// ERROR: do array processing (numbers of trues)
	// TODO: convert to separate part of problem
	void stripConstantConstraints(
		final SimpleHashSet<ConsistencyConstraint> consistencyConstraints
	) {
		if(consistencyConstraints != null) {
			for(final ConsistencyConstraint consistencyConstraint : consistencyConstraints) {
				if(consistencyConstraint.isConstant()) {
					final int hadamard = consistencyConstraint.sortedAndUniqueNumbersOfTrues[0] == 0 ? 0 : 1;
					for(final int rootNodeTerm : consistencyConstraint.sortedAndUniqueDecisionIds) {
						equationNodes[propertyButterfly.rootNodeTier][rootNodeTerm].removeInvalidHadamards(hadamard);
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------------
	// constructor functions

	private static EquationNode[][] makeNodes(
		final ConsistencyConstraint constraint,
		final PropertyButterfly     propertyButterfly
	) {
		final EquationNode[][] equationNodes = new EquationNode[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final PropertyNode propertyNode = propertyButterfly.propertyNodes[nodeTier][nodeTerm];
				equationNodes[nodeTier][nodeTerm] = new EquationNode(propertyNode, constraint);
			}
		}
		return equationNodes;
	}

	private static EquationBox[][] makeBoxes(
		final PropertyButterfly propertyButterfly,
		final EquationNode[][]  equationNodes
	) {
		final EquationBox[][] boxes = new EquationBox[propertyButterfly.numberOfBoxTiers][propertyButterfly.numberOfBoxTerms];
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				boxes[boxTier][boxTerm] = new EquationBox(propertyBox, equationNodes);
			}
		}
		return boxes;
	}

	// -----------------------------------------------------------------------
	// status functions

	String nodeStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("equation butterfly: # node facts\n");
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final int numberOfEquationFacts = equationNodes[nodeTier][nodeTerm].equationFacts.size();
				buffer.append(Utility.code(numberOfEquationFacts));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	String boxStatuses() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("equation butterfly: # box deltas\n");
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final int numberOfEquationDeltas = equationBoxes[boxTier][boxTerm].equationDeltas.size();
				buffer.append(Utility.code(numberOfEquationDeltas));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

	// -----------------------------------------------------------------------
	// boilerplate functions

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(consistencyConstraint);
		buffer.append("\n");
		for(int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				buffer.append(equationNodes[nodeTier][nodeTerm]);
				buffer.append("\n");
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
