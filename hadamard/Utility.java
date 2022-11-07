package hadamard;

import java.util.Random;

public class Utility {
	public static boolean isAPowerOfTwo(int x) {
		return x != 0 && (x & (x - 1)) == 0;
	}

	// this rounds up
	public static int log2(int x) {
		throwIfFalse(x > 0, "x must be greater than zero");

		for(int i = 0; i < Integer.SIZE; i++) {
			if(x <= (1 << i)) {
				return i;
			}
		}
		throw new RuntimeException("this should never occur");
	}

	public static int sequency(int[] vector) {
		int changes = 0;
		for(int i = 0; i < vector.length - 1; i++) {
			changes += vector[i] == vector[i+1] ? 0 : 1;
		}
		return changes;
	}

	public static void throwIfFalse(boolean check, String message) {
		if(!check) {
			throw new RuntimeException(message);
		}
	}

	public static void print(int[] vector) {
		for(int i = 0; i < vector.length; i++) {
			System.out.print(" " + vector[i]);
		}
		System.out.println();
	}

	public static void print(int[][] array) {
		for(int i = 0; i < array.length; i++) {
			print(array[i]);
		}
	}

	public static int[] randomVector(int size, Random random) {
		int[] result = new int[size];
		for(int i = 0; i < size; i++) {
			result[i] = random.nextInt(size);
		}
		return result;
	}

	public static int[] powers(int n) {
		int[] result = new int[n];
		for(int i = 0; i < n; i++) {
			result[i] = 1 << i;
		}
		return result;
	}
}
