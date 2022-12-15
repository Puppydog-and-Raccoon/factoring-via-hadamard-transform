package factoring;

import java.util.HashSet;
import java.util.Vector;

public class Factoring {
	private final consistency.Consistency consistencySolver;

	public Factoring(consistency.Consistency consistencySolver) {
		this.consistencySolver = consistencySolver;
	}

	public Solution solve(Problem factoringProblem) {
		Utility.throwIfFalse(factoringProblem.errors.isEmpty(), factoringProblem.errors.toString());
		final Meaning[] meanings = makeMeanings(factoringProblem.sizeOfFactorsInBits);
		final consistency.Problem consistencyProblem = new consistency.Problem(
			meanings.length,
			makeConstraints(meanings, factoringProblem),
			factoringProblem.sizeOfFactorsInBits * factoringProblem.sizeOfFactorsInBits
		);
		final boolean[] decisions = consistencySolver.solve(consistencyProblem);
		return decisions == null ? null : extractSolution(decisions, meanings, factoringProblem);
	}

	Meaning[] makeMeanings(int factorSizeInBits) {
		final boolean[] booleans = new boolean[]{false, true};
		final Vector<Meaning> meanings = new Vector<Meaning>();
		for(int aId = 0; aId < factorSizeInBits; aId++) {
			for(int bId = 0; bId < factorSizeInBits; bId++) {
				for(boolean aIn : booleans) {
					for(boolean bIn : booleans) {
						for(boolean sumIn : booleans) {
							for(boolean carryIn : booleans) {
								final Meaning meaning = new Meaning(aId, bId, aIn, bIn, carryIn, sumIn, factorSizeInBits);
								meanings.add(meaning);
							}
						}
					}
				}
			}
		}
		return meanings.toArray(new Meaning[0]);
	}

	consistency.Constraint[] makeConstraints(Meaning[] meanings, Problem problem) {
		final HashSet<consistency.Constraint> constraints = new HashSet<consistency.Constraint>(); // to remove duplicates
		for(int i = 0; i < meanings.length; i++) {
			for(int j = 0; j < meanings.length; j++) {
				if(!Meaning.isConsistent(meanings[i], meanings[j], problem)) {
					constraints.add(new consistency.Constraint(i, j, meanings.length));
				}
			}
		}
		return constraints.toArray(new consistency.Constraint[0]);
	}

	Solution extractSolution(boolean[] decisions, Meaning[] meanings, Problem problem) {
		final boolean[] factorA = new boolean[problem.sizeOfFactorsInBits];
		final boolean[] factorB = new boolean[problem.sizeOfFactorsInBits];
		for(int i = 0; i < decisions.length; i++) {
			if(decisions[i]) {
				final Meaning meaning = meanings[i];
				factorA[meaning.aId] = meaning.aIn;
				factorB[meaning.bId] = meaning.bIn;
			}
		}
		return new Solution(factorA, factorB);
	}
}
