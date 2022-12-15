package factoring;

public class Utility {
	static void throwIfFalse(boolean condition, String message) {
		if(!condition) {
			throw new RuntimeException(message);
		}
	}

	static boolean[] toBooleans(String productInHexadecimal) {
		return Utility.toBooleans(4 * productInHexadecimal.length(), productInHexadecimal);
	}

	static boolean[] toBooleans(int length, String hexadecimalDigits) {
		boolean[] results = new boolean[length];
		for(int i = 0; i < hexadecimalDigits.length(); i++) {
			final int offset = 4 * (hexadecimalDigits.length() - i);
			final int value = toNumber(hexadecimalDigits.charAt(i));
			assign(results, offset - 4, value & 0x1);
			assign(results, offset - 3, value & 0x2);
			assign(results, offset - 2, value & 0x4);
			assign(results, offset - 1, value & 0x8);
		}
		return results;
	}

	private static void assign(boolean[] results, final int position, final int value) {
		if(0 <= position && position < results.length) {
			results[position] = value != 0; // fix
		}
	}

	private static int toNumber(char c) {
		if(c >= '0' && c <= '9') {
			return c - '0';
		}
		if(c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		if(c >= 'A' && c <= 'F') {
			return c - 'A' + 10;
		}
		throw new RuntimeException("bad hexadecimal digit (" + (int)c + ")");
	}

	static boolean isHexadecimal(String hexadecimalDigits) {
		for(int i = 0; i < hexadecimalDigits.length(); i++) {
			final char c = hexadecimalDigits.charAt(i);
			if(!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')))
				return false;
		}
		return true;
	}

	static boolean lessThanOrEqualTo(boolean[] numberA, boolean[] numberB) {
		throwIfFalse(numberA.length == numberB.length, "mismatched lengths");

		for(int i = numberA.length - 1; i >= 0; i--) {
			if(!numberA[i] && numberB[i]) {
				return true;
			}
			if(numberA[i] && !numberB[i]) {
				return false;
			}
		}
		return true;
	}

	static boolean isEven(int x) {
		return (x & 1) == 0;
	}

	static boolean[] booleans() {
		return new boolean[] {};
	}

	static boolean[] booleans(boolean a) {
		return new boolean[] {a};
	}

	static boolean[] booleans(boolean a, boolean b) {
		return new boolean[] {b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c) {
		return new boolean[] {c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d) {
		return new boolean[] {d, c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d, boolean e) {
		return new boolean[] {e, d, c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
		return new boolean[] {f, e, d, c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g) {
		return new boolean[] {g, f, e, d, c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h) {
		return new boolean[] {h, g, f, e, d, c, b, a};
	}

	static boolean[] booleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h, boolean i) {
		return new boolean[] {i, h, g, f, e, d, c, b, a};
	}

	static boolean booleanArrayEquals(boolean[] arrayA, boolean[] arrayB) {
		if(arrayA == null && arrayB == null) {
			return true;
		}
		if(arrayA == null || arrayB == null) {
			return false;
		}
		if(arrayA.length != arrayB.length) {
			return false;
		}
		for(int i = 0; i < arrayA.length; i++) {
			if(arrayA[i] != arrayB[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean wouldDropLeadingBit(Integer sizeOfNumberInBits, String numberInHexadecimal) {
		boolean[] number = Utility.toBooleans(numberInHexadecimal);
		for(int i = sizeOfNumberInBits; i < number.length; i++) {
			if(number[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean leadingBitInMostSignificantHalf(Integer sizeOfNumberInBits, String numberInHexadecimal) {
		boolean[] bits = Utility.toBooleans(numberInHexadecimal);
		for(int i = sizeOfNumberInBits / 2; i < sizeOfNumberInBits && i < bits.length; i++) {
			if(bits[i]) {
				return true;
			}
		}
		return false;
	}

	public static String toString(boolean[] array) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		String separator = "";
		for(int i = 0; i < array.length; i++) {
			buffer.append(separator);
			buffer.append(array[i] ? "true" : "false");
			separator = ", ";
		}
		buffer.append("]");
		return buffer.toString();
	}
}
