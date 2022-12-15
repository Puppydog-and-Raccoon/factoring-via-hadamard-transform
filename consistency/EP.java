package consistency;

import java.util.HashSet;

// shorthand for sets of EquationPairs

public class EP {
	int hadamard;
	int firstPartialSum;
	int lastPartialSum;
	int step;

	EP(int hadamard, int firstPartialSum, int lastPartialSum, int step) {
		Utility.throwIfFalse(step > 0, "step must be positive");
		this.hadamard        = hadamard;
		this.firstPartialSum = firstPartialSum;
		this.lastPartialSum  = lastPartialSum;
		this.step            = step;
	}

	EP(int hadamard, int partialSum) {
		this.hadamard        = hadamard;
		this.firstPartialSum = partialSum;
		this.lastPartialSum  = partialSum;
		this.step            = 1;
	}

	HashSet<EquationPair> expand() {
		HashSet<EquationPair> results = new HashSet<EquationPair>();
		for(int ps = firstPartialSum; ps <= lastPartialSum; ps += step) {
			results.add(new EquationPair(hadamard, ps));
		}
		return results;
	}

	static HashSet<EquationPair> expand(EP[] eps) {
		HashSet<EquationPair> results = new HashSet<EquationPair>();
		for(EP ep : eps) {
			results.addAll(ep.expand());
		}
		return results;
	}
}
