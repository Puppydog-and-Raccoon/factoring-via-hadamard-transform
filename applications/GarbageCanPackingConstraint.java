package applications;

import consistency.ConsistencyConstraint;
import consistency.Unique;

public class GarbageCanPackingConstraint {
	public final int decisionIdA;
	public final int decisionIdB;

	private static Unique<GarbageCanPackingConstraint> unique = new Unique<GarbageCanPackingConstraint>();

	private GarbageCanPackingConstraint(
		final int decisionIdA,
		final int decisionIdB
	) {
		this.decisionIdA = Math.min(decisionIdA, decisionIdB);
		this.decisionIdB = Math.max(decisionIdA, decisionIdB);
	}

	public ConsistencyConstraint toExactlyOneOfThree(
		final int decisionIdC,
		final int numberOfDecisionsInProblem
	) {
		return ConsistencyConstraint.exactlyOneOfThree(decisionIdA, decisionIdB, decisionIdC, numberOfDecisionsInProblem);
	}

	public static GarbageCanPackingConstraint makeAtMostOneOf(
		final int decisionIdA,
		final int decisionIdB
	) {
		return unique.unique(new GarbageCanPackingConstraint(decisionIdA, decisionIdB));
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(decisionIdA,  0)
			 ^ Integer.rotateLeft(decisionIdB, 16);
	}

	@Override
	public boolean equals(final Object otherObject) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& decisionIdA == ((GarbageCanPackingConstraint) otherObject).decisionIdA
			&& decisionIdB == ((GarbageCanPackingConstraint) otherObject).decisionIdB;
	}

	@Override
	public String toString() {
		return "GarbageCanPackingConstraint ["
			 + "decisionIdA=" + decisionIdA + ", "
			 + "decisionIdB=" + decisionIdB
			 + "]";
	}
}
