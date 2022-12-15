package consistency;

public class NaiveConsistency implements Consistency {
	public boolean[] solve(Problem problem) {
		boolean[] vector = new boolean[problem.numberOfDecisions]; // defaults to all false
		while(true) {
			if(isSolution(vector, problem)) {
				return vector;
			}
			if(increment(vector)) {
				return null;
			}
		}
	}

	public boolean isSolution(boolean[] vector, Problem problem) {
		return population(vector) == problem.numberOfTrues && matches(vector, problem.constraints);
	}

	int population(boolean[] vector) {
		int sum = 0;
		for(boolean b : vector) {
			sum += b ? 1 : 0;
		}
		return sum;
	}

	boolean matches(boolean[] vector, Constraint[] constraints) {
		for(Constraint constraint : constraints) {
			if(vector[constraint.i] && vector[constraint.j]) {
				return false;
			}
		}
		return true;
	}

	// this works differently than the video, this returns the carry out
	boolean increment(boolean[] vector) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = !vector[i];
			if(vector[i]) {
				return false;
			}
		}
		return true;
	}
}
