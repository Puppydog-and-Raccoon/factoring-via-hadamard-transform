package consistency;

// caching values in wring equation box seems to hurt a bit

final class EquationBox {
	final PropertyBox            propertyBox;
	final EquationNode           leftParentNode;
	final EquationNode           rightParentNode;
	final EquationNode           leftChildNode;
	final EquationNode           rightChildNode;
	final SimpleHashSet<EquationDelta> equationDeltas;

	EquationBox(
		final PropertyBox      propertyBox,
		final EquationNode[][] equationNodes
	) {
		this.propertyBox     = propertyBox;
		this.leftParentNode  = equationNodes[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
		this.rightParentNode = equationNodes[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
		this.leftChildNode   = equationNodes[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
		this.rightChildNode  = equationNodes[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];
		this.equationDeltas  = new SimpleHashSet<>();
	}

	// -------------------------------------------------------------------------------------
	// fill and wring equation butterflies

	// TODO: rename
	void addAllPossibleParentFacts() {
		for(final EquationFact leftChildEquationFact : leftChildNode.equationFacts) {
			for(final EquationFact rightChildEquationFact : rightChildNode.equationFacts) {
				if(leftChildEquationFact.population == rightChildEquationFact.population) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildEquationFact.population)) {
						if(propertyBox.wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta)) {
							final EquationDelta equationDelta = EquationDelta.make(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
							equationDeltas.add(equationDelta);
							leftParentNode.equationFacts.add(equationDelta.leftParentEquationFact());
							rightParentNode.equationFacts.add(equationDelta.rightParentEquationFact());
						}
					}
				}
			}
		}
	}

	boolean wringEquationBox() {
		final SimpleHashSet<EquationDelta> verifiedEquationDeltas           = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedLeftParentEquationFacts  = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedRightParentEquationFacts = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedLeftChildEquationFacts   = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedRightChildEquationFacts  = new SimpleHashSet<>();

		for(final EquationDelta equationDelta : equationDeltas) {
			if(leftParentNode.equationFacts.contains(equationDelta.leftParentEquationFact())
			&& rightParentNode.equationFacts.contains(equationDelta.rightParentEquationFact())
			&& leftChildNode.equationFacts.contains(equationDelta.leftChildEquationFact())
			&& rightChildNode.equationFacts.contains(equationDelta.rightChildEquationFact())) {
				verifiedEquationDeltas.add(equationDelta);
				verifiedLeftParentEquationFacts.add(equationDelta.leftParentEquationFact());
				verifiedRightParentEquationFacts.add(equationDelta.rightParentEquationFact());
				verifiedLeftChildEquationFacts.add(equationDelta.leftChildEquationFact());
				verifiedRightChildEquationFacts.add(equationDelta.rightChildEquationFact());
			}
		}

		final boolean deltasChanged      = equationDeltas.retainAll(verifiedEquationDeltas);
		final boolean leftParentChanged  = leftParentNode.equationFacts.retainAll(verifiedLeftParentEquationFacts);
		final boolean rightParentChanged = rightParentNode.equationFacts.retainAll(verifiedRightParentEquationFacts);
		final boolean leftChildChanged   = leftChildNode.equationFacts.retainAll(verifiedLeftChildEquationFacts);
		final boolean rightChildChanged  = rightChildNode.equationFacts.retainAll(verifiedRightChildEquationFacts);
		return deltasChanged || leftParentChanged || rightParentChanged || leftChildChanged || rightChildChanged;
	}

	// -------------------------------------------------------------------------------------

	boolean rippleUpBoxFacts(
		final DebugButterfly<EquationFact> equationFactsButterfly
	) {
		final SimpleHashSet<EquationFact> leftChildEquationFacts = equationFactsButterfly.hashSetAt(leftChildNode.propertyNode);
		final SimpleHashSet<EquationFact> rightChildEquationFacts = equationFactsButterfly.hashSetAt(rightChildNode.propertyNode);
		final SimpleHashSet<EquationFact> leftParentEquationFacts = equationFactsButterfly.hashSetAt(leftParentNode.propertyNode);
		final SimpleHashSet<EquationFact> rightParentEquationFacts = equationFactsButterfly.hashSetAt(rightParentNode.propertyNode);

		for(EquationFact leftChildEquationFact : leftChildEquationFacts) {
			for(EquationFact rightChildEquationFact : rightChildEquationFacts) {
				if(leftChildEquationFact.population == rightChildEquationFact.population) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildEquationFact.population)) {
						if(wouldAndDoMakeActualParents(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta)) {
							final EquationDelta equationDelta = EquationDelta.make(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
							leftParentEquationFacts.add(equationDelta.leftParentEquationFact());
							rightParentEquationFacts.add(equationDelta.rightParentEquationFact());
						}
					}
				}
			}
		}

		return !leftParentEquationFacts.isEmpty() && !rightParentEquationFacts.isEmpty();
	}

	// ---------------------------------------------------------------------------------------------
	// helpers

	boolean wouldAndDoMakeActualParents(
		final EquationFact    leftChildEquationFact,
		final EquationFact    rightChildEquationFact,
		final PopulationDelta parentPopulationDelta
	) {
		return propertyBox.wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta)
			&& doMakeActualParentEquationFacts(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
	}

	// ERROR: we must keep the equation facts checks
	// ERROR: the equation deltas check is too generous, but we don't know why
	private boolean doMakeActualParentEquationFacts(
		final EquationFact    leftChildEquationFact,
		final EquationFact    rightChildEquationFact,
		final PopulationDelta parentPopulationDelta
	) {
		final EquationFact  leftParentEquationFact  = EquationFact.makeLeftParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
		final EquationFact  rightParentEquationFact = EquationFact.makeRightParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
		final EquationDelta equationDelta           = EquationDelta.make(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
		return leftParentNode.equationFacts.contains(leftParentEquationFact)
			&& rightParentNode.equationFacts.contains(rightParentEquationFact)
			&& equationDeltas.contains(equationDelta);
	}

	// ---------------------------------------------------------------------------------------------
	// boilerplate

	@Override
	public String toString() {
		return "EquationBox [equationDeltas=" + Utility.toStringFromSet(equationDeltas) + "]";
	}
}
