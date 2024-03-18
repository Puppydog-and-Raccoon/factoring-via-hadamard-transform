package consistency;

import java.util.HashSet;

// add "verified" to names?

final class EquationBox {
	final PositionBox  positionBox;
	final EquationNode leftParentNode;
	final EquationNode rightParentNode;
	final EquationNode leftChildNode;
	final EquationNode rightChildNode;

	EquationBox(final EquationNode[][] equationNodes, final PositionBox positionBox, final EquationBox[][] equationBoxes) {
		this.positionBox     = positionBox;
		this.leftParentNode  = equationNodes[positionBox.leftParentNode.nodeTier][positionBox.leftParentNode.nodeTerm];
		this.rightParentNode = equationNodes[positionBox.rightParentNode.nodeTier][positionBox.rightParentNode.nodeTerm];
		this.leftChildNode   = equationNodes[positionBox.leftChildNode.nodeTier][positionBox.leftChildNode.nodeTerm];
		this.rightChildNode  = equationNodes[positionBox.rightChildNode.nodeTier][positionBox.rightChildNode.nodeTerm];
	}

	// -------------------------------------------------------------------------------------
	// create equation butterflies

	void addPossibleParentFacts() {
		for(final EquationFact leftChildFact : leftChildNode.equationFacts) {
			for(final EquationFact rightChildFact : rightChildNode.equationFacts) {
				if(wouldMakeValidParentFacts(leftChildFact, rightChildFact)) {
					final EquationFact leftParentFact = EquationFact.newLeftParent(leftChildFact, rightChildFact);
					final EquationFact rightParentFact = EquationFact.newRightParent(leftChildFact, rightChildFact);
//					System.out.println("** in box " + positionBox.boxTerm + " add " + leftParentFact + " and " + rightParentFact);
					leftParentNode.equationFacts.add(leftParentFact);
					rightParentNode.equationFacts.add(rightParentFact);
				}
			}
		}
	}

	boolean removeInvalidChildFacts() {
		final HashSet<EquationFact> verifiedLeftChildFacts  = new HashSet<EquationFact>();
		final HashSet<EquationFact> verifiedRightChildFacts = new HashSet<EquationFact>();

		for(final EquationFact leftChildFact : leftChildNode.equationFacts) {
			for(final EquationFact rightChildFact : rightChildNode.equationFacts) {
				if(wouldMakeValidParentFacts(leftChildFact, rightChildFact)) {
					final EquationFact leftParentFact = EquationFact.newLeftParent(leftChildFact, rightChildFact);
					final EquationFact rightParentFact = EquationFact.newRightParent(leftChildFact, rightChildFact);
					if(arePresentInParentNodes(leftParentFact, rightParentFact)) {
						verifiedLeftChildFacts.add(leftChildFact);
						verifiedRightChildFacts.add(rightChildFact);
					}
				}
			}
		}

		final boolean leftChildChanged  = leftChildNode.equationFacts.retainAll(verifiedLeftChildFacts);
		final boolean rightChildChanged = rightChildNode.equationFacts.retainAll(verifiedRightChildFacts);
		return leftChildChanged || rightChildChanged;
	}

	boolean removeInvalidParentFacts() {
		final HashSet<EquationFact> validLeftParentFacts = new HashSet<EquationFact>();
		final HashSet<EquationFact> validRightParentFacts = new HashSet<EquationFact>();

		for(final EquationFact leftChildFact : leftChildNode.equationFacts) {
			for(final EquationFact rightChildFact : rightChildNode.equationFacts) {
				if(wouldMakeValidParentFacts(leftChildFact, rightChildFact)) {
					final EquationFact leftParentFact = EquationFact.newLeftParent(leftChildFact, rightChildFact);
					final EquationFact rightParentFact = EquationFact.newRightParent(leftChildFact, rightChildFact);
					if(arePresentInParentNodes(leftParentFact, rightParentFact)) {
						validLeftParentFacts.add(leftParentFact);
						validRightParentFacts.add(rightParentFact);
					}
				}
			}
		}

		final boolean leftParentChanged = leftParentNode.equationFacts.retainAll(validLeftParentFacts);
		final boolean rightParentChanged = rightParentNode.equationFacts.retainAll(validRightParentFacts);
		return leftParentChanged || rightParentChanged;
	}

	// -------------------------------------------------------------------------------------
	// used in isValid()
	// TODO: delete?

	@SuppressWarnings("unchecked")
	void computeValidParentFacts(final Object[][] equationFacts) {
		final HashSet<EquationFact> leftChildEquationFacts = (HashSet<EquationFact>)equationFacts[leftChildNode.positionNode.nodeTier][leftChildNode.positionNode.nodeTerm];
		final HashSet<EquationFact> rightChildEquationFacts = (HashSet<EquationFact>)equationFacts[rightChildNode.positionNode.nodeTier][rightChildNode.positionNode.nodeTerm];

		HashSet<EquationFact> leftParentEquationFacts = new HashSet<EquationFact>();
		HashSet<EquationFact> rightParentEquationFacts = new HashSet<EquationFact>();
		for(EquationFact leftChildFact : leftChildEquationFacts) {
			for(EquationFact rightChildFact : rightChildEquationFacts) {
				if(wouldMakeValidParentFacts(leftChildFact, rightChildFact)) {
					EquationFact leftParentFact = EquationFact.newLeftParent(leftChildFact, rightChildFact);
					EquationFact rightParentFact = EquationFact.newRightParent(leftChildFact, rightChildFact);
					if(arePresentInParentNodes(leftParentFact, rightParentFact)) {
						leftParentEquationFacts.add(leftParentFact);
						rightParentEquationFacts.add(rightParentFact);
					}
				}
			}
		}

		equationFacts[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm] = leftParentEquationFacts;
		equationFacts[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm] = rightParentEquationFacts;
	}

	@SuppressWarnings("unchecked")
	boolean parentsAreValid(final Object[][] equationFacts) {
		final HashSet<EquationFact> leftParentEquationFacts = (HashSet<EquationFact>)equationFacts[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm];
		final HashSet<EquationFact> rightParentEquationFacts = (HashSet<EquationFact>)equationFacts[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm];
		return leftParentEquationFacts != null && !leftParentEquationFacts.isEmpty() && rightParentEquationFacts != null && !rightParentEquationFacts.isEmpty();
	}

	// ---------------------------------------------------------------------------------------------
	// helpers

	boolean wouldMakeValidParentFacts(
		final EquationFact leftChildFact,
		final EquationFact rightChildFact
	) {
		return positionBox.wouldMakeValidParentHadamards(leftChildFact.hadamard, rightChildFact.hadamard);
	}

	boolean arePresentInParentNodes(
		final EquationFact leftParentFact,
		final EquationFact rightParentFact
	) {
		return leftParentNode.equationFacts.contains(leftParentFact)
			&& rightParentNode.equationFacts.contains(rightParentFact);
	}

	boolean parentFactsArePresentInParentNodes(
		final EquationFact leftChildEquationFact,
		final EquationFact rightChildEquationFact
	) {
		final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
		final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
		return arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact);
	}
}
