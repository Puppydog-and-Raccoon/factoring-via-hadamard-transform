package applications;

import consistency.Utility;

public class FactoringNumber {
	public static final String BOTH_NUMBERS_MUST_BE_NULL_OR_NOT_NULL = "both numbers must be null or not null";
	public static final String NUMBER_MUST_CONTAIN_0S_AND_1S         = "number must contains 0s and 1s";

	public final boolean[] bits;

	public FactoringNumber(final int length) {
		this.bits = new boolean[length]; // all bits default to false/zero
	}

	public FactoringNumber(final String numberAsString) {
		Utility.insist(isBinaryString(numberAsString), NUMBER_MUST_CONTAIN_0S_AND_1S);

		this.bits = toBits(numberAsString);
	}

	// -----------------------------------------------------------------------

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

	public static FactoringNumber minimum(final FactoringNumber a, final FactoringNumber b) {
		return compare(a, b) <= 0 ? a : b;
	}

	public static FactoringNumber maximum(final FactoringNumber a, final FactoringNumber b) {
		return compare(a, b) >= 0 ? a : b;
	}

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

	public static boolean isEqual(final FactoringNumber a, final FactoringNumber b) {
		return (a == null) == (b == null) && compare(a, b) == 0;
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

	private void addNthBitToNumber(final int n) {
		for(int m = n; !(bits[m] = !bits[m]); m++) {
		}
	}
}
