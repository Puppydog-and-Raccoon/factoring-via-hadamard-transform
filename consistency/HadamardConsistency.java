package consistency;

public class HadamardConsistency implements Consistency {
	public boolean[] solve(Problem problem) {
		final PositionButterfly positionButterfly = new PositionButterfly(problem.numberOfDecisions);
		final EquationButterfly[] equationButterflies = makeEquationButterflies(problem, positionButterfly);
		final boolean[] solution = new boolean[problem.numberOfDecisions];

		for(int root = 0; root < problem.numberOfDecisions; root++) {
			System.out.println("root = " + root + " of " + problem.numberOfDecisions);
			final SolutionButterfly solutionButterfly = SolutionButterfly.makeButterfly(positionButterfly);

			//merge leaves
			{
				int tier = positionButterfly.numberOfTiers - 1;
				for(EquationButterfly equationButterfly : equationButterflies) {
					for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
						solutionButterfly.nodes[tier][term].intersectSolutionPairs(equationButterfly.nodes[tier][term].validSolutionPairsLeaf());
						equationButterfly.nodes[tier][term].reverseIntersectLeaf(solutionButterfly.nodes[tier][term].validSolutionPairs);
					}
				}
			}

			//merge parents
			for(int tier = positionButterfly.numberOfTiers - 2; tier >= 0; tier--) {
				for(EquationButterfly equationButterfly : equationButterflies) {
					for(int term = 0; term < positionButterfly.numberOfTerms; term++) {
						solutionButterfly.nodes[tier][term].intersectSolutionPairs(equationButterfly.nodes[tier][term].validSolutionPairsParent());
						equationButterfly.nodes[tier][term].reverseIntersectParent(solutionButterfly.nodes[tier][term].validSolutionPairs);
					}
				}
			}

			Integer hadamard = solutionButterfly.nodes[0][root].chooseHadamard();
			if(hadamard == null) {
				return null;
			}
			solution[root] = hadamard.intValue() != 0;
			for(EquationButterfly equationButterfly : equationButterflies) {
				equationButterfly.nodes[0][root].setHadamard(hadamard.intValue());
				equationButterfly.wring();
			}
		}

		return solution;
	}

	EquationButterfly[] makeEquationButterflies(Problem problem, final PositionButterfly positionButterfly) {
		final EquationButterfly[] equationButterflies = new EquationButterfly[problem.constraints.length];
		for(int i = 0; i < problem.constraints.length; i++) {
			equationButterflies[i] = EquationButterfly.makeButterfly(problem, positionButterfly, problem.constraints[i]);
		}
		return equationButterflies;
	}
}
