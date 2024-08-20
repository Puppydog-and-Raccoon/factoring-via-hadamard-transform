package applications;

import java.util.Vector;

import consistency.Utility;

/**
 * Represent numbers for factoring.
 */
public class FactoringNumber {
	public static final String BOTH_NUMBERS_MUST_BE_NULL_OR_NOT_NULL = "both numbers must be null or not null";
	public static final String NUMBER_MUST_CONTAIN_0S_AND_1S         = "number must contains 0s and 1s";

	public final boolean[] bits;

	/**
	 * Constructor
	 * 
	 * @param numberOfBits the number of bits to store
	 */
	public FactoringNumber(final int numberOfBits) {
		this.bits = new boolean[numberOfBits]; // all bits default to false/zero
	}

	/**
	 * Constructor from a string.
	 * Mostly used in testing.
	 * 
	 * @param numberAsString the initial value
	 */
	public FactoringNumber(final String numberAsString) {
		Utility.insist(isBinaryString(numberAsString), NUMBER_MUST_CONTAIN_0S_AND_1S);

		this.bits = toBits(numberAsString);
	}

	/**
	 * Constructor. initialize with a given number.
	 * 
	 * @param number the initial value
	 * @param numberOfBits the number of bits to store
	 */
	public FactoringNumber(final int number, final int numberOfBits) {
		Utility.insist(number >= 0, "number (" + number + ") must be zero or positive");
		Utility.insist(numberOfBits >= 0, "number of bits (" + numberOfBits + ") must be zero or positive");
		Utility.insist(number < (1 << numberOfBits), "number (" + number + ") must fit in number of bits (" + numberOfBits + ")");

		this.bits = toBits(number, numberOfBits);
	}

	// -----------------------------------------------------------------------

	/**
	 * Multiply 2 numbers.
	 * 
	 * @param a the first multiplicand
	 * @param b the second multiplicand
	 * @return the product
	 */
	public static FactoringNumber multiply(final FactoringNumber a, final FactoringNumber b) {
		final FactoringNumber product = new FactoringNumber(a.bits.length + b.bits.length);
		for(int i = 0; i < a.bits.length; i++) {
			for(int j = 0; j < b.bits.length; j++) {
				if(a.bits[i] && b.bits[j]) {
					product.addNthBitToNumber(i + j);
				}
			}
		}
		return product;
	}

	/**
	 * Return the minimum of 2 numbers.
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return the minimum
	 */
	public static FactoringNumber minimum(final FactoringNumber a, final FactoringNumber b) {
		return compare(a, b) <= 0 ? a : b;
	}

	/**
	 * Return the maximum of 2 numbers.
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return the maximum
	 */
	public static FactoringNumber maximum(final FactoringNumber a, final FactoringNumber b) {
		return compare(a, b) >= 0 ? a : b;
	}

	/**
	 * Compare 2 numbers.
	 * Return 1 if the first number is larger.
	 * Return 0 if the numbers are equal.
	 * Return -1 if the second number is larger.
	 * Nulls only equal nulls.
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return 1, 0, or -1 depending on the order of the numbers
	 */
	public static int compare(final FactoringNumber a, final FactoringNumber b) {
		Utility.insist((a == null) == (b == null), BOTH_NUMBERS_MUST_BE_NULL_OR_NOT_NULL);
	
		if(a == null && b == null) {
			return 0;
		}

		final int commonLength = Math.min(a.bits.length, b.bits.length);
		for(int i = commonLength; i < a.bits.length; i++) {
			if(a.bits[i]) {
				return 1;
			}
		}
		for(int i = commonLength; i < b.bits.length; i++) {
			if(b.bits[i]) {
				return -1;
			}
		}
		for(int i = commonLength - 1; i >= 0; i--) {
			if(a.bits[i] && !b.bits[i]) {
				return 1;
			}
			if(!a.bits[i] && b.bits[i]) {
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Return whether 2 numbers are equal
	 * 
	 * @param a the first number
	 * @param b the second number
	 * @return whether they are equal
	 */
	public static boolean isEqual(final FactoringNumber a, final FactoringNumber b) {
		return (a == null) == (b == null) && compare(a, b) == 0;
	}

	/**
	 * Compute all pairs of factors for a given product.
	 * This is used for testing.
	 * 
	 * @param product the product
	 * @param numberOfBitsPerFactor limits the pairs of factors
	 * @return an array of all pairs of factors
	 */
	public static FactoringSolution[] pairsOfFactors(final int product, final int numberOfBitsPerFactor) {
		final int maximumFactor = (1 << numberOfBitsPerFactor) - 1;

		Utility.insist(product > 0, "product must be positive");
		Utility.insist(numberOfBitsPerFactor > 0, "number of bits per factor must be positive");

		final Vector<FactoringSolution> solutions = new Vector<FactoringSolution>();
		for(int i = 1; i * i <= product; i++) {
			if(product % i == 0) {
				final int j = product / i;
				if(i <= maximumFactor && j <= maximumFactor) {
					final FactoringSolution solution = FactoringSolution.makeSolutionFound(
						new FactoringNumber(i, numberOfBitsPerFactor),
						new FactoringNumber(j, numberOfBitsPerFactor)
					);
					solutions.add(solution);
				}
			}
		}
		return solutions.toArray(new FactoringSolution[0]);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(bits.length);
		for(int i = bits.length - 1; i >= 0; i--) {
			buffer.append(bits[i] ? '1' : '0');
		}
		return buffer.toString();
	}

	// -----------------------------------------------------------------------

	private static boolean isBinaryString(final String numberAsString) {
		final int length = numberAsString.length();
		for(int i = 0; i < length; i++) {
			final char c = numberAsString.charAt(i);
			if(c != '0' && c != '1') {
				return false;
			}
		}
		return true;
	}

	private static boolean[] toBits(final String numberAsBinaryString) {
		final int numberOfBits = numberAsBinaryString.length();
		final boolean[] numberAsBooleans = new boolean[numberOfBits];
		for(int i = 0; i < numberOfBits; i++) {
			final char c = numberAsBinaryString.charAt(numberOfBits - 1 - i);
			numberAsBooleans[i] = Utility.toBoolean(c);
		}
		return numberAsBooleans;
	}

	private boolean[] toBits(int number, int numberOfBits) {
		final boolean[] bits = new boolean[numberOfBits];
		for(int i = 0; i < numberOfBits; i++) {
			if(i < Integer.SIZE) {
				bits[i] = (number & (1 << i)) != 0;
			}
		}
		return bits;
	}

	private void addNthBitToNumber(final int n) {
		for(int m = n; !(bits[m] = !bits[m]); m++) {
		}
	}
}
