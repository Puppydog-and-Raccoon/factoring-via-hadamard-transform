package applications;

import consistency.Utility;

public class FactoringProblem {
	public static final String BAD_POWER_OF_TWO = "the number of bits in the product must be a power of 2";
	public static final String BAD_AT_LEAST_TWO = "the number of bits in the product must be at least 2";

	public final FactoringNumber product;

	public FactoringProblem(final FactoringNumber product) {
		this.product = product;
	}

	public boolean isValid() {
		return Utility.isPowerOfTwo(product.bits.length) && product.bits.length >= 2;
	}
}
