package consistency;

import java.util.HashSet;

// check public
// todo: go meta with pairs???

final class EquationNode {
	final PositionNode          positionNode;
	final HashSet<EquationFact> equationFacts;

	EquationNode(final PositionNode positionNode) {
		this.positionNode  = positionNode;
		this.equationFacts = new HashSet<EquationFact>();
	}
/*
	void removeNonchosenHadamards(final int chosenHadamard) {
		equationFacts.removeIf(p -> p.hadamard != chosenHadamard);
	}
*/
	void removeInvalidPartialSums(final int totalSum) {
		Utility.insist(positionNode.isRoot(), "must be a root node");

		equationFacts.removeIf(p -> p.partialSum != totalSum);
	}

	// TODO: remove
	void removeInvalidNumbersOfTrues(final int validNumberOfTrues) {
		Utility.insist(positionNode.isLeaf() && positionNode.nodeTerm == 0, "must be the population node");

		equationFacts.removeIf(p -> p.hadamard != validNumberOfTrues);
	}

	void fillLeaf(final int multiplier, final int totalSum) {
		Utility.insist(positionNode.isLeaf(), "not a leaf node");

		for(final int hadamard : positionNode.hadamardDomain.enumerate()) {
			final EquationFact equationFact = EquationFact.newLeaf(hadamard, multiplier * hadamard);
//			System.out.println("* in node " + positionNode.nodeTerm + " add " + equationFact);
			equationFacts.add(equationFact);
		}
	}

	long size() {
		return equationFacts.size();
	}

	// TODO: refactor this
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("enode " + positionNode + " facts={");
		for(EquationFact fact : equationFacts) {
			buffer.append(fact);
		}
		buffer.append("}");
		return buffer.toString();
	}

	// -----------------------------------------------------------------------------------------
	// delete most of this??? used in isValid()

	HashSet<EquationFact> factsWithHadamard(final int hadamard) {
		final HashSet<EquationFact> results = new HashSet<EquationFact>();
		for(final EquationFact fact : equationFacts) {
			if(fact.hadamard == hadamard) {
				results.add(fact);
			}
		}
		return results;
	}

	// TODO: move matrix lookup to caller?
	void computeValidLeafFacts(final Object[][] equationFacts, final int[][] hadamards) {
		equationFacts[positionNode.nodeTier][positionNode.nodeTerm] = factsWithHadamard(hadamards[positionNode.nodeTier][positionNode.nodeTerm]);
	}

	// TODO: move matrix lookup to caller?
	@SuppressWarnings("unchecked")
	boolean leafIsValid(final Object[][] equationFacts) {
		final HashSet<EquationFact> leafEquationFacts = (HashSet<EquationFact>)equationFacts[positionNode.nodeTier][positionNode.nodeTerm];
		return !leafEquationFacts.isEmpty();
	}
}
