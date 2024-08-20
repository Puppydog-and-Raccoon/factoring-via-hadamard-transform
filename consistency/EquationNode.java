package consistency;

final class EquationNode {
	final PropertyNode                propertyNode;
	final ConsistencyConstraint       consistencyConstraint;
	final SimpleHashSet<EquationFact> equationFacts;

	EquationNode(
		final PropertyNode          propertyNode,
		final ConsistencyConstraint consistencyConstraint
	) {
		this.propertyNode          = propertyNode;
		this.consistencyConstraint = consistencyConstraint;
		this.equationFacts         = new SimpleHashSet<EquationFact>();
	}

	boolean removeInvalidPartialSums() {
		Utility.insist(propertyNode.isRoot, "must be a root node");

		return equationFacts.removeIf(equationFact -> !Utility.contains(equationFact.partialSum, consistencyConstraint.sortedAndUniqueNumbersOfTrues));
	}

	boolean removeInvalidHadamards(int hadamard) {
		Utility.insist(propertyNode.isRoot, "must be a root node");

		return equationFacts.removeIf(equationFact -> equationFact.hadamard != hadamard);
	}

	void fillLeaf() {
		Utility.insist(propertyNode.isLeaf, "must be a leaf node");

		final int multiplier = consistencyConstraint.constraintVector[propertyNode.nodeTerm];
		for(final int hadamard : propertyNode.hadamardDomain.enumerate()) {
			for(final int population : propertyNode.populationDomain.enumerate()) {
				final EquationFact equationFact = EquationFact.make(hadamard, population, hadamard, multiplier * hadamard);
				equationFacts.add(equationFact);
			}
		}
	}

	int size() {
		return equationFacts.size();
	}

	// -----------------------------------------------------------------------------------------

	boolean rippleUpLeafFacts(
		final DebugButterfly<EquationFact> equationFactsButterfly,
		final int[] leafHadamards
	) {
		final int leafHadamard = leafHadamards[propertyNode.nodeTerm];
		final SimpleHashSet<EquationFact> resultEquationFacts = equationFactsButterfly.hashSetAt(propertyNode);
		for(final EquationFact equationFact : equationFacts) {
			if(equationFact.hadamard == leafHadamard) {
				resultEquationFacts.add(equationFact);
			}
		}
		return !resultEquationFacts.isEmpty();
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public String toString() {
		return "<en"                                             + " "
			 + propertyNode                                      + " "
			 + "facts=" + Utility.toStringFromSet(equationFacts)
			 + ">";
	}
}
