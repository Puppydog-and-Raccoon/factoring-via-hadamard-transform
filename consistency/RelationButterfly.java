package consistency;

public class RelationButterfly {
	final PositionButterfly positionButterfly;
	final EquationButterfly equationButterfly;
	final SolutionButterfly solutionButterfly;
	final RelationNode[][]  relationNodes;
	final RelationBox[][]   relationBoxes;

	RelationButterfly(
		final EquationButterfly equationButterfly,
		final SolutionButterfly solutionButterfly
	) {
		final PositionButterfly positionButterfly = equationButterfly.positionButterfly;
		final RelationNode[][]  relationNodes     = newNodes(positionButterfly, equationButterfly);
		final RelationBox[][]   relationBoxes     = newBoxes(positionButterfly, equationButterfly, solutionButterfly, relationNodes);

		this.positionButterfly = positionButterfly;
		this.equationButterfly = equationButterfly;
		this.solutionButterfly = solutionButterfly;
		this.relationNodes     = relationNodes;
		this.relationBoxes     = relationBoxes;
	}

	private static RelationNode[][] newNodes(
		final PositionButterfly positionButterfly,
		final EquationButterfly equationButterfly
	) {
		final RelationNode[][] relationNodes = new RelationNode[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				final EquationNode equationNode = equationButterfly.equationNodes[nodeTier][nodeTerm];
				relationNodes[nodeTier][nodeTerm] = new RelationNode(equationNode);
			}
		}
		return relationNodes;
	}

	private static RelationBox[][] newBoxes(
		final PositionButterfly positionButterfly,
		final EquationButterfly equationButterfly,
		final SolutionButterfly solutionButterfly,
		final RelationNode[][]  relationNodes
	) {
		final RelationBox[][] relationBoxes = new RelationBox[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final EquationBox equationBox = equationButterfly.equationBoxes[boxTier][boxTerm];
				final SolutionBox solutionBox = solutionButterfly.solutionBoxes[boxTier][boxTerm];
				relationBoxes[boxTier][boxTerm] = new RelationBox(equationBox, solutionBox, relationNodes);
			}
		}
		return relationBoxes;
	}

	void fillRelationUp() {
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			final RelationNode relationNode = relationNodes[positionButterfly.leafNodeTier][leafNodeTerm];
			relationNode.fillLeafUp();
		}
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				relationBox.unionParentsUpRelation();
			}
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				relationBox.intersectParentsUpRelation();
			}
		}
	}

	// RETHINK: is this necessary? we should measure how much it changes anything.
	void wringRelationDown() {
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				relationBox.intersectChildrenDownRelation();
			}
		}
	}

	RelationBox getBox(PositionBox positionBox) {
		return relationBoxes[positionBox.boxTier][positionBox.boxTerm];
	}

	boolean isValid(final int[] rootHadamards) {
		Utility.insist(rootHadamards.length == relationNodes[0].length, "vector is wrong size: " + rootHadamards.length + " != " + relationNodes[0].length);

		final SupportAnswer supportAnswer = new SupportAnswer(positionButterfly, rootHadamards);
		final ButterflyOfHashSets<EquationFact> equationAnswer = new ButterflyOfHashSets<EquationFact>(positionButterfly);

		// fill up leaves
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			final RelationNode relationNode = relationNodes[positionButterfly.leafNodeTier][leafNodeTerm];
			relationNode.rippleUpEquation(supportAnswer, equationAnswer);
		}

		// fill up tiers
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				relationBox.rippleUpEquation(supportAnswer, equationAnswer);
			}
		}

		return equationAnswer.isValid();
	}

	// this is used in testing
	// If butterflies filled as part of construction, this would be unnecessary
	static RelationButterfly newRelationButterfly(
		final PositionButterfly     positionButterfly,
		final ConsistencyConstraint consistencyConstraint,
		final Integer               numberOfTruesInProblem,
		final SolutionButterfly     solutionButterfly
	) {
		final EquationButterfly equationButterfly = new EquationButterfly(positionButterfly, consistencyConstraint, numberOfTruesInProblem);
		equationButterfly.fill();
		final RelationButterfly relationButterfly = new RelationButterfly(equationButterfly, solutionButterfly);
		relationButterfly.fillRelationUp();
		relationButterfly.wringRelationDown();
		return relationButterfly;
	}

	void assignSolutionFacts() {
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				final SolutionNode solutionNode = solutionButterfly.solutionNodes[nodeTier][nodeTerm];
				final RelationNode relationNode = relationNodes[nodeTier][nodeTerm];
				solutionNode.solutionFacts.clear();
				solutionNode.solutionFacts.addAll(relationNode.relationMultiMap.getAllSolutionFacts());
			}
		}
	}

	@Override
	public String toString() {
		return "RelationButterfly ["
			 + "positionButterfly=" + positionButterfly               + ",\n"
			 + "equationButterfly=" + equationButterfly               + ",\n"
			 + "relationNodes="     + Utility.toString(relationNodes) + ",\n"
			 + "relationBoxes="     + Utility.toString(relationBoxes)
			 + "]";
	}

	private boolean wringRelationOnceXXX() {
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				final boolean boxChanged = relationBox.intersectEverythingBothRelation();
				anythingChanged = anythingChanged || boxChanged;
			}
			{
				final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
				anythingChanged = anythingChanged || transformChanged;
			}
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				final boolean boxChanged = relationBox.intersectEverythingBothRelation();
				anythingChanged = anythingChanged || boxChanged;
			}
			{
				final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
				anythingChanged = anythingChanged || transformChanged;
			}
		}
		return anythingChanged;
	}

	void wringRelationUntilNoChangeXXX() {
		while(wringRelationOnceXXX()) {
		}
	}

	boolean unionRelationXXX() {
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationBoxes[boxTier][boxTerm];
				final boolean boxChanged = relationBox.fillSolutionBoxesFromEquationBoxes();
				anythingChanged = anythingChanged || boxChanged;
			}
		}
		return anythingChanged;
	}
}
