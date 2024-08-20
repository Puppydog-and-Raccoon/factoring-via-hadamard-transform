package applications;

/**
 * Link the meaning of one state in a factoring matrix with one decision in a consistency vector.
 * Helps programs to translate from factoring problems to consistency problems and back.
 * 
 * <p>
 * Each cell in a multiplying/factoring matrix has sixteen different possible input states,
 * defined by the bits a_in, b_in, sum_in, and carry_in.
 * Each cell has four different possible output states, defined by the bits sum_out and carry_out.
 * The output states are determined by the input states.
 * </p>
 * 
 * <p>
 * This class has package scope, because user applications should never access it.
 * </p>
 */
class FactoringDecision {
	final boolean aIn;
	final boolean bIn;
	final boolean sumIn;
	final boolean carryIn;
	final boolean sumOut;
	final boolean carryOut;
	final int     decisionId;    // position of this decision in the decisions array

	/**
	 * The constructor.
	 * Note that it doesn't matter which state bit means what, as long as they are consistent.
	 * 
	 * @param stateFlags four bits that indicate a_in, b_in, sum_in, and carry_in
	 * @param decisionId position in the decision or consistency vector
	 */
	FactoringDecision(
		final int stateFlags,
		final int decisionId
	) {
		final int aIn      = ithBit(stateFlags, 0);
		final int bIn      = ithBit(stateFlags, 1);
		final int sumIn    = ithBit(stateFlags, 2);
		final int carryIn  = ithBit(stateFlags, 3);
		final int totalIn  = aIn * bIn + sumIn + carryIn;
		final int sumOut   = ithBit(totalIn, 0);
		final int carryOut = ithBit(totalIn, 1);

		this.aIn        = toBoolean(aIn);
		this.bIn        = toBoolean(bIn);
		this.sumIn      = toBoolean(sumIn);
		this.carryIn    = toBoolean(carryIn);
		this.sumOut     = toBoolean(sumOut);
		this.carryOut   = toBoolean(carryOut);
		this.decisionId = decisionId;
	}

	/**
	 * Extract the ith bit from an int.
	 * 
	 * @param bits the int to extract from
	 * @param i which bit
	 * @return the result
	 */
	private static int ithBit(int bits, int i) {
		return (bits >> i) & 0x1;
	}

	/**
	 * Translate a bit to a boolean.
	 * 
	 * @param bit the bit to translate, presume it is a single bit
	 * @return the boolean value
	 */
	private static boolean toBoolean(int bit) {
		return bit != 0;
	}
}
