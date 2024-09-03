package consistency;

// This only works WITHIN a butterfly.
// From butterfly to butterfly, nodes must share hadamard and spine values.

public class SpineDelta {
	final short leftChildSpine;
	final short rightChildSpine;

	private static final Canonicalizer<SpineDelta> canonicalizer = new Canonicalizer<SpineDelta>();

	private SpineDelta(
		final int leftChildSpine,
		final int rightChildSpine
	) {
		this.leftChildSpine  = Utility.toShort(leftChildSpine);
		this.rightChildSpine = Utility.toShort(rightChildSpine);
	}

	// -----------------------------------------------------------------------
	// factories

	static SpineDelta make(
		final int leftChildSpine,
		final int rightChildSpine
	) {
		Utility.insistSameParity(leftChildSpine, rightChildSpine);

		return canonicalizer.canonicalize(new SpineDelta(
			leftChildSpine,
			rightChildSpine
		));
	}

	// -----------------------------------------------------------------------
	// accessors

	// remove? this is never used
	int parentSpine() {
		return (leftChildSpine + rightChildSpine) / 2;
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftChildSpine,  0)
							   ^ DoubleTabulationHashing.primaryHash(rightChildSpine, 1);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject     != null
			&& getClass()      == otherObject.getClass()
			&& leftChildSpine  == ((SpineDelta) otherObject).leftChildSpine
			&& rightChildSpine == ((SpineDelta) otherObject).rightChildSpine;
	}

	@Override
	public String toString() {
		return "<sd "
			 + "lcs=" + leftChildSpine  + " "
			 + "rcs=" + rightChildSpine
			 + ">";
	}
}
