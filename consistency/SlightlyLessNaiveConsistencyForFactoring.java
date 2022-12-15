package consistency;

// 1 true will be chosen from every group of 16 decisions

public class SlightlyLessNaiveConsistencyForFactoring implements Consistency {
	public boolean[] solve(Problem problem) {
		Utility.throwIfFalse(problem.numberOfDecisions % 16 == 0, "number_of_decisions must be a multiple of 16");
		Utility.throwIfFalse(problem.numberOfDecisions == 16 * problem.numberOfTrues, "number_of_decisions must equal 16 * number_of_trues");

		byte[] vector = initialize(problem);
		int[] constraints = toArrayFromConstraints(problem.constraints);
		int i = 0;
		while(true) {
			if(matches(vector, constraints)) {
				return toBooleansFromBytes(vector);
			}
			if(increment(vector)) {
				return null;
			}
			if(i % (256 * 256) == 0) {
				// System.out.println(i);
			}
			i++;
		}
	}

	boolean matches(byte[] vector, int[] constraints) {
		for(int constraint : constraints) {
			final int constraintI = constraint >> 16;
			final int constraintJ = constraint & 0xffff;
			final int constraintIUpper = constraintI >> 4;
			final int constraintILower = constraintI & 0xf;
			final int constraintJUpper = constraintJ >> 4;
			final int constraintJLower = constraintJ & 0xf;
			if(vector[constraintIUpper] == constraintILower && vector[constraintJUpper] == constraintJLower) {
				return false;
			}
		}
		return true;
	}

	byte[] initialize(Problem problem) {
		byte[] vector = new byte[problem.numberOfDecisions / 16];
		for(int i = 0; i < vector.length; i++) {
			vector[i] = 0;
		}
		return vector;
	}

	// return carry out
	boolean increment(byte[] vector) {
		for(int i = 0; i < vector.length; i++) {
			vector[i] = (byte)((vector[i] + 1) % 16);
			if(vector[i] != 0) {
				return false;
			}
		}
		return true;
	}

	int[] toArrayFromConstraints(Constraint[] constraints) {
		int[] result = new int[constraints.length];
		int next = 0;
		for(Constraint constraint : constraints) {
			result[next++] = (constraint.i << 16) + constraint.j;
		}
		return result;
	}

	boolean[] toBooleansFromBytes(byte[] vector) {
		boolean[] result = new boolean[vector.length * 16];
		for(int i = 0; i < vector.length; i++) {
			result[16 * i + vector[i]] = true;
		}
		return result;
	}
}
