package consistency;

import java.util.HashSet;

public class SolutionNode {
	final PositionNode          positionNode;
	final HashSet<SolutionFact> solutionFacts;

	SolutionNode(final PositionNode positionNode) {
		this.positionNode  = positionNode;
		this.solutionFacts = new HashSet<SolutionFact>();
	}

	public void fillLeaf() {
		final HadamardDomain hadamardDomain = positionNode.hadamardDomain;
		for(int hadamard = hadamardDomain.minimum; hadamard <= positionNode.hadamardDomain.maximum; hadamard += positionNode.hadamardDomain.stride) {
			for(int spine = positionNode.hadamardDomain.minimum; spine <= positionNode.hadamardDomain.maximum; spine += positionNode.hadamardDomain.stride) {
				for(int population = positionNode.hadamardDomain.populationMinimum; spine <= positionNode.hadamardDomain.populationMaximum; spine += positionNode.hadamardDomain.populationStride) {
					final SolutionFact solutionFact = SolutionFact.newFact(hadamard, spine, population);
					solutionFacts.add(solutionFact);
				}
			}
		}
	}
}
