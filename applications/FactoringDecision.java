package applications;

// This class describes the meaning of each decision.
// Directions are based on the square multiplication matrix, where the upper right
// is position 0, 0; vector a runs down the right; and vector b runs across the top.
// This class has package scope, because user applications should never access it.

class FactoringDecision {
	final boolean aIn;
	final boolean bIn;
	final boolean sumIn;
	final boolean carryIn;
	final boolean sumOut;
	final boolean carryOut;
	final int     index;    // position of this decision in the decisions array

	FactoringDecision(
		final int flags,
		final int index
	) {
		final int aIn      = ithBit(flags, 0);
		final int bIn      = ithBit(flags, 1);
		final int sumIn    = ithBit(flags, 2);
		final int carryIn  = ithBit(flags, 3);
		final int totalIn  = aIn * bIn + sumIn + carryIn;
		final int sumOut   = ithBit(totalIn, 0);
		final int carryOut = ithBit(totalIn, 1);

		this.aIn      = toBoolean(aIn);
		this.bIn      = toBoolean(bIn);
		this.sumIn    = toBoolean(sumIn);
		this.carryIn  = toBoolean(carryIn);
		this.sumOut   = toBoolean(sumOut);
		this.carryOut = toBoolean(carryOut);
		this.index    = index;
	}

	private static int ithBit(int bits, int i) {
		return (bits >> i) & 0x1;
	}

	private static boolean toBoolean(int i) {
		return i != 0;
	}
}
