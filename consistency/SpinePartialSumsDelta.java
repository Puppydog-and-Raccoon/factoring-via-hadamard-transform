package consistency;

public class SpinePartialSumsDelta {
	final short leftChildSpine;
	final short rightChildSpine;
	final short firstLeftChildPartialSum;
	final short firstRightChildPartialSum;
	final short secondLeftChildPartialSum;
	final short secondRightChildPartialSum;

	private static final Canonicalizer<SpinePartialSumsDelta> canonicalizer = new Canonicalizer<>();

	private SpinePartialSumsDelta(
		final int leftChildSpine,
		final int rightChildSpine,
		final int firstLeftChildPartialSum,
		final int firstRightChildPartialSum,
		final int secondLeftChildPartialSum,
		final int secondRightChildPartialSum
	) {
		Utility.insistSameParity(leftChildSpine, rightChildSpine);
		Utility.insistSameParity(firstLeftChildPartialSum, firstRightChildPartialSum);
		Utility.insistSameParity(secondLeftChildPartialSum, secondRightChildPartialSum);

		this.leftChildSpine             = Utility.toShort(leftChildSpine);
		this.rightChildSpine            = Utility.toShort(rightChildSpine);
		this.firstLeftChildPartialSum   = Utility.toShort(firstLeftChildPartialSum);
		this.firstRightChildPartialSum  = Utility.toShort(firstRightChildPartialSum);
		this.secondLeftChildPartialSum  = Utility.toShort(secondLeftChildPartialSum);
		this.secondRightChildPartialSum = Utility.toShort(secondRightChildPartialSum);
	}

	// -----------------------------------------------------------------------
	// factories

	static SpinePartialSumsDelta make(
		final int leftChildSpine,
		final int rightChildSpine,
		final int firstLeftChildPartialSum,
		final int firstRightChildPartialSum,
		final int secondLeftChildPartialSum,
		final int secondRightChildPartialSum
	) {
		return canonicalizer.canonicalize(new SpinePartialSumsDelta(
			leftChildSpine,
			rightChildSpine,
			firstLeftChildPartialSum,
			firstRightChildPartialSum,
			secondLeftChildPartialSum,
			secondRightChildPartialSum
		));
	}

	// -----------------------------------------------------------------------
	// accessors

	SpineDelta spineDelta() {
		return SpineDelta.make(leftChildSpine, rightChildSpine);
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftChildSpine,             0)
							   ^ DoubleTabulationHashing.primaryHash(rightChildSpine,            1)
							   ^ DoubleTabulationHashing.primaryHash(firstLeftChildPartialSum,   2)
							   ^ DoubleTabulationHashing.primaryHash(firstRightChildPartialSum,  3)
							   ^ DoubleTabulationHashing.primaryHash(secondLeftChildPartialSum,  4)
							   ^ DoubleTabulationHashing.primaryHash(secondRightChildPartialSum, 5);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject                != null
			&& getClass()                 == otherObject.getClass()
			&& leftChildSpine             == ((SpinePartialSumsDelta) otherObject).leftChildSpine
			&& rightChildSpine            == ((SpinePartialSumsDelta) otherObject).rightChildSpine
			&& firstLeftChildPartialSum   == ((SpinePartialSumsDelta) otherObject).firstLeftChildPartialSum
			&& firstRightChildPartialSum  == ((SpinePartialSumsDelta) otherObject).firstRightChildPartialSum
			&& secondLeftChildPartialSum  == ((SpinePartialSumsDelta) otherObject).secondLeftChildPartialSum
			&& secondRightChildPartialSum == ((SpinePartialSumsDelta) otherObject).secondRightChildPartialSum;
	}

	@Override
	public String toString() {
		return "<spsd "
			 + "lcs="   + leftChildSpine             + " "
			 + "rcs="   + rightChildSpine            + " "
			 + "1lcps=" + firstLeftChildPartialSum   + " "
			 + "1rcps=" + firstRightChildPartialSum  + " "
			 + "2lcps=" + secondLeftChildPartialSum  + " "
			 + "2rcps=" + secondRightChildPartialSum
			 + ">";
	}
}
