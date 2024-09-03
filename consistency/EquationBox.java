package consistency;

import java.util.Iterator;

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
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildEquationFact.population)) {
						equationDeltas.add(EquationDelta.make(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta));
						leftParentNode.equationFacts.add(EquationFact.makeLeftParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta));
						rightParentNode.equationFacts.add(EquationFact.makeRightParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta));
					}
				}
			}
		}
	}

	boolean wringEquationBoxDown() {
		final SimpleHashSet<EquationFact>  verifiedLeftChildEquationFacts   = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedRightChildEquationFacts  = new SimpleHashSet<>();

		boolean equationDeltasChanged = false;
		final Iterator<EquationDelta> iterator = equationDeltas.iterator();
		while(iterator.hasNext()) {
			final EquationDelta equationDelta = iterator.next();
			if(leftParentNode.equationFacts.contains(equationDelta.leftParentEquationFact())
			&& rightParentNode.equationFacts.contains(equationDelta.rightParentEquationFact())) {
				verifiedLeftChildEquationFacts.add(equationDelta.leftChildEquationFact());
				verifiedRightChildEquationFacts.add(equationDelta.rightChildEquationFact());
			} else {
				iterator.remove();
				equationDeltasChanged = true;
			}
		}
		equationDeltas.decreaseCapacityIfAppropriate();

		final boolean leftChildChanged   = leftChildNode.equationFacts.retainAll(verifiedLeftChildEquationFacts);
		final boolean rightChildChanged  = rightChildNode.equationFacts.retainAll(verifiedRightChildEquationFacts);
		return equationDeltasChanged || leftChildChanged || rightChildChanged;
	}

	boolean wringEquationBoxUp() {
		final SimpleHashSet<EquationFact>  verifiedLeftParentEquationFacts  = new SimpleHashSet<>();
		final SimpleHashSet<EquationFact>  verifiedRightParentEquationFacts = new SimpleHashSet<>();

		boolean equationDeltasChanged = false;
		final Iterator<EquationDelta> iterator = equationDeltas.iterator();
		while(iterator.hasNext()) {
			final EquationDelta equationDelta = iterator.next();
			if(leftChildNode.equationFacts.contains(equationDelta.leftChildEquationFact())
			&& rightChildNode.equationFacts.contains(equationDelta.rightChildEquationFact())) {
				verifiedLeftParentEquationFacts.add(equationDelta.leftParentEquationFact());
				verifiedRightParentEquationFacts.add(equationDelta.rightParentEquationFact());
			} else {
				iterator.remove();
				equationDeltasChanged = true;
			}
		}
		equationDeltas.decreaseCapacityIfAppropriate();

		final boolean leftParentChanged  = leftParentNode.equationFacts.retainAll(verifiedLeftParentEquationFacts);
		final boolean rightParentChanged = rightParentNode.equationFacts.retainAll(verifiedRightParentEquationFacts);
		return equationDeltasChanged || leftParentChanged || rightParentChanged;
	}

	// -------------------------------------------------------------------------------------

	// TODO: optimize away the would and do make
	boolean rippleUpBoxFacts(
		final DebugButterfly<EquationFact> equationFactsButterfly
	) {
		final SimpleHashSet<EquationFact> leftChildEquationFacts = equationFactsButterfly.hashSetAt(leftChildNode.propertyNode);
		final SimpleHashSet<EquationFact> rightChildEquationFacts = equationFactsButterfly.hashSetAt(rightChildNode.propertyNode);
		final SimpleHashSet<EquationFact> leftParentEquationFacts = equationFactsButterfly.hashSetAt(leftParentNode.propertyNode);
		final SimpleHashSet<EquationFact> rightParentEquationFacts = equationFactsButterfly.hashSetAt(rightParentNode.propertyNode);

		for(EquationFact leftChildEquationFact : leftChildEquationFacts) {
			for(EquationFact rightChildEquationFact : rightChildEquationFacts) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					for(final PopulationDelta parentPopulationDelta : propertyBox.parentPopulationDeltas(leftChildEquationFact.population)) {
						if(doMakeActualParentEquationFacts(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta)) {
							final EquationFact leftParentEquationFact  = EquationFact.makeLeftParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
							final EquationFact rightParentEquationFact = EquationFact.makeRightParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
							leftParentEquationFacts.add(leftParentEquationFact);
							rightParentEquationFacts.add(rightParentEquationFact);
						}
					}
				}
			}
		}

		return !leftParentEquationFacts.isEmpty() && !rightParentEquationFacts.isEmpty();
	}

	// ---------------------------------------------------------------------------------------------
	// helpers

	private boolean wouldMakeValidParentEquationFacts(
		final EquationFact leftChildEquationFact,
		final EquationFact rightChildEquationFact
	) {
		return Utility.haveSameParity(leftChildEquationFact.hadamard, rightChildEquationFact.hadamard)
		&& leftChildEquationFact.population == rightChildEquationFact.population
		&& Utility.haveSameParity(leftChildEquationFact.spine, rightChildEquationFact.spine)
		&& Utility.haveSameParity(leftChildEquationFact.partialSum, rightChildEquationFact.partialSum)
		&& propertyBox.leftParentNode.hadamardDomain.isInDomain((leftChildEquationFact.hadamard + rightChildEquationFact.hadamard) / 2)
		&& propertyBox.rightParentNode.hadamardDomain.isInDomain((leftChildEquationFact.hadamard - rightChildEquationFact.hadamard) / 2)
		&& propertyBox.leftParentNode.spineDomain.isInDomain((leftChildEquationFact.spine + rightChildEquationFact.spine) / 2)
		&& propertyBox.rightParentNode.spineDomain.isInDomain((leftChildEquationFact.spine + rightChildEquationFact.spine) / 2);
	}

	boolean doMakeActualParentEquationFacts(
		final EquationFact    leftChildEquationFact,
		final EquationFact    rightChildEquationFact,
		final PopulationDelta parentPopulationDelta
	) {
		final EquationFact leftParentEquationFact  = EquationFact.makeLeftParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
		final EquationFact rightParentEquationFact = EquationFact.makeRightParent(leftChildEquationFact, rightChildEquationFact, parentPopulationDelta);
		return leftParentNode.equationFacts.contains(leftParentEquationFact)
			&& rightParentNode.equationFacts.contains(rightParentEquationFact);
	}

	// ---------------------------------------------------------------------------------------------
	// boilerplate

	@Override
	public String toString() {
		return "EquationBox [equationDeltas=" + Utility.toStringFromSet(equationDeltas) + "]";
	}
}
