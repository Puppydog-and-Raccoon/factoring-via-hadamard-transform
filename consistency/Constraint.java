package consistency;

import java.util.Objects;

public class Constraint {
	public final int i;
	public final int j;
	public final int N;

	public Constraint(int i, int j, int N) {
		this.i = Math.min(i, j);
		this.j = Math.max(i, j);
		this.N = N;
	}

	public int[] fhtOfConstraintVector() {
		int[] result = new int[N];   // defaults to 0
		result[i] = 1;
		result[j] = 1;
		return hadamard.Hadamard.fastSylvesterTransform(result);
	}

	public long[] validPartialSums() {
		return i == j
			 ? new long[] {0}
			 : new long[] {0, N};
	}

	@Override
	public int hashCode() {
		return Objects.hash(i, j, N);
	}

	@Override
	public boolean equals(Object otherObject) {
		if(otherObject == null) {
			return false;
		}
		if(otherObject.getClass() != Constraint.class) {
			return false;
		}
		Constraint otherConstraint = (Constraint)otherObject;
		return i == otherConstraint.i && j == otherConstraint.j && N == otherConstraint.N;
	}

	@Override
	public String toString() {
		return "<i = " + i + ", j = " + j + ", N = " + N + ">";
	}
}
