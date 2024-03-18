package consistency;

import java.util.HashSet;

public class RelationNode {
	final PositionNode     positionNode;
	final EquationNode     equationNode;
	final RelationMultiMap relationMultiMap;

	RelationNode(final EquationNode equationNode) {
		this.positionNode     = equationNode.positionNode;
		this.equationNode     = equationNode;
		this.relationMultiMap = new RelationMultiMap();
	}

	public void fillLeafUp() {
		Utility.insist(positionNode.isLeaf(), "node must be a leaf");

		for(EquationFact equationFact : equationNode.equationFacts) {
			for(int canonicalPopulation : positionNode.canonicalPopulations()) {
				final SolutionFact solutionFact = SolutionFact.newLeaf(equationFact, canonicalPopulation);
				relationMultiMap.add(equationFact, solutionFact);
			}
		}
	}

	// ERROR: fix this
	public void rippleUp(SupportAnswer answer, SolutionFact[][] solutionFactButterfly) {
		solutionFactButterfly[positionNode.nodeTier][positionNode.nodeTerm] = null;
		for(final SolutionFact fact : relationMultiMap.getAllSolutionFacts()) {
			if(fact.hadamard   == answer.hadamardButterfly[positionNode.nodeTier][positionNode.nodeTerm]
			&& fact.spine      == answer.spineButterfly[positionNode.nodeTier][positionNode.nodeTerm]
			&& fact.population == answer.populationButterfly[positionNode.nodeTier][positionNode.nodeTerm]) {
				if(solutionFactButterfly[positionNode.nodeTier][positionNode.nodeTerm] != null) {
					throw new RuntimeException("multiple solutions should never occur");
				}
				solutionFactButterfly[positionNode.nodeTier][positionNode.nodeTerm] = fact;
			}
		}
	}

	// ERROR: fix this
	// this is used for testing
	private boolean isValid(int[][] hadamardButterfly) {
		final int hadamard = hadamardButterfly[positionNode.nodeTier][positionNode.nodeTerm];
		return false; // solutionHadamards.contains(new Integer(hadamard));
	}

	// ERROR: fix this
	@Override
	public String toString() {
		return "SolutionNode ["
			 + "positionNode="           + positionNode           + ", "
			 + "fromEquationToSolution=" + relationMultiMap
			 + "]";
	}

	public boolean rippleUpEquation(
		final SupportAnswer                     answer,
		final ButterflyOfHashSets<EquationFact> equationAnswer
	) {
		final SolutionFact solutionFact           = answer.solutionButterfly[positionNode.nodeTier][positionNode.nodeTerm];
		final HashSet<EquationFact> equationFacts = equationAnswer.hashSetAt(positionNode);

		// ERROR: rename
		final HashSet<EquationFact> xxx = relationMultiMap.getEquationFactsFor(solutionFact);
		if(xxx != null) {
			equationFacts.addAll(xxx);
		}
		return xxx != null && !xxx.isEmpty();
	}
}
