package consistency;

public class Utility {
	public static void throwIfFalse(boolean condition, String message) {
		if(!condition) {
			throwAlways(message);
		}
	}

	public static void throwAlways(String message) {
		throw new RuntimeException(message);
	}

	// log2() rounds up
	// TODO: this should be optimized ala Hacker's Delight
	public static int log2(int x) {
		for(int i = 0; i < Integer.SIZE - 1; i++) {
			if(x <= (1 << i)) {
				return i;
			}
		}

		throw new RuntimeException("this should never occur");
	}

	public static boolean haveSameParity(int x, int y) {
		return (x & 1) == (y & 1);
	}

	public static boolean isPowerOfTwo(int x) {
		return x > 0 && (x & (x - 1)) == 0;
	}

	public static int leftChildTerm(int parentTier, int parentTerm) {
		return parentTerm & ~(1 << parentTier);
	}

	public static int rightChildTerm(int parentTier, int parentTerm) {
		return parentTerm | (1 << parentTier);
	}

	public static boolean contains(long value, long[] array) {
		for(int i = 0; i < array.length; i++) {
			if(value == array[i]) {
				return true;
			}
		}
		return false;
	}

	public static boolean arraysAreEqual(boolean[] a, boolean[] b) {
		if(a == null && b == null) {
			return true;
		}
		if(a == null || b == null) {
			return false;
		}
		if(a.length != b.length) {
			return false;
		}
		for(int i = 0; i < a.length; i++) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
}
