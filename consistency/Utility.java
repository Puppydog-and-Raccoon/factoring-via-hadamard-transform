package consistency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

// TODO: make public
// TODO: verify public used correctly

final public class Utility {
	static final String ARGUMENTS_MUST_HAVE_THE_SAME_PARITY  = "arguments must have the same parity";
	static final String VECTOR_LENGTH_MUST_BE_A_POWER_OF_TWO = "vector length must be a power of two";
	static final String N_MUST_BE_POSITIVE                   = "n must be positive";

	public static void insist(final boolean condition, final String message) {
		if(!condition) {
			throw new RuntimeException(message);
		}
	}

	public static void insistSameParity(final int a, final int b) {
		insist((a & 1) == (b & 1), ARGUMENTS_MUST_HAVE_THE_SAME_PARITY);
	}

	private static void insistIsPowerOfTwo(final int[] vector) {
		insist(isPowerOfTwo(vector.length), VECTOR_LENGTH_MUST_BE_A_POWER_OF_TWO);
	}

	public static boolean isPowerOfTwo(final int i) {
		return i > 0 && Integer.lowestOneBit(i) == i;
	}

	public static int roundUpToPowerOfTwo(final int i) {
		final int highestOneBit = Integer.highestOneBit(i);
		return highestOneBit == i ? highestOneBit : 2 * highestOneBit;
	}

	static boolean[] toBooleans(final int[] ints) {
		final boolean[] booleans = new boolean[ints.length];
		for(int i = 0; i < ints.length; i++) {
			booleans[i] = ints[i] != 0;
		}
		return booleans;
	}

	// TODO: inline?
	static String toString(final int[] ints) {
		return Arrays.toString(ints);
	}

	static String toString(final int[][] intss) {
		final StringBuffer buffer = new StringBuffer();
		String separator = "";
		for(final int[] ints : intss) {
			buffer.append(separator);
			buffer.append(Arrays.toString(ints));
			separator = "\n";
		}
		return buffer.toString();
	}

	static int[] toInts(final long bits, final int length) {
		final int[] vector = new int[length];
		for(int i = 0; i < length; i++) {
			vector[i] = (int)((bits >> i) & 0x1);
		}
		return vector;
	}

	static int[] fastSylvesterTransform(final int[] vector) {
		insistIsPowerOfTwo(vector);

		final int[] result = vector.clone();
		for(int scale = 1; scale < vector.length; scale *= 2) {
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					final int x = result[j];
					final int y = result[j + scale];
					result[j]         = x + y;
					result[j + scale] = x - y;
				}
			}
		}
		return result;
	}

	static int[] inverseFastSylvesterTransform(final int[] vector) {
		insistIsPowerOfTwo(vector);

		final int[] result = vector.clone();
		for(int scale = vector.length / 2; scale >= 1 ; scale /= 2) {
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					final int x = result[j];
					final int y = result[j + scale];
					Utility.insistSameParity(x, y);
					result[j]         = (x + y) / 2;
					result[j + scale] = (x - y) / 2;
				}
			}
		}
		return result;
	}

	public static int log2RoundedUp(final int n) {
		insist(n > 0, N_MUST_BE_POSITIVE);

		int mostSignificantBit = 0, m = n;
		if((m >> 16) != 0) { mostSignificantBit += 16; m = m >> 16; }
		if((m >>  8) != 0) { mostSignificantBit +=  8; m = m >>  8; }
		if((m >>  4) != 0) { mostSignificantBit +=  4; m = m >>  4; }
		if((m >>  2) != 0) { mostSignificantBit +=  2; m = m >>  2; }
		if((m >>  1) != 0) { mostSignificantBit +=  1; m = m >>  1; }
		return n == (1 << mostSignificantBit) ? mostSignificantBit : mostSignificantBit + 1;
	}

	static int[][] sylvesterButterfly(final int[] vector) {
		insistIsPowerOfTwo(vector);

		final int numberOfTiers = numberOfNodeTiers(vector.length);
		final int numberOfTerms = numberOfNodeTerms(vector.length);
		final int[][] result = new int[numberOfTiers][numberOfTerms];

		int tier = 0;
		for(int i = 0; i < numberOfTerms; i++) {
			result[tier][i] = vector[i];
		}
		for(int scale = 1; scale < vector.length; scale *= 2) {
			tier++;
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					int x = result[tier - 1][j];
					int y = result[tier - 1][j + scale];
					result[tier][j]         = x + y;
					result[tier][j + scale] = x - y;
				}
			}
		}
		return result;
	}

	static int population(final int[] vector) {
		int count = 0;
		for(int i = 0; i < vector.length; i++) {
			count += vector[i] == 0 ? 0 : 1;
		}
		return count;
	}

	static <ElementType> String toStringFromSet(final Set<ElementType> elements) {
		final StringBuffer buffer = new StringBuffer();
		if(elements == null) {
			buffer.append("null");
		} else {
			String separator = "";
			buffer.append("{");
			for(ElementType element : elements) {
				final String quoteForStrings = element instanceof String ? "\"" : "";
				buffer.append(separator);
				buffer.append(quoteForStrings);
				buffer.append(element);
				buffer.append(quoteForStrings);
				separator = ", ";
			}
			buffer.append("}");
		}
		return buffer.toString();
	}

	public static int[] enumerateAscending(final int number) {
		final int[] enumeration = new int[number];
		for(int i = 0; i < number; i++) {
			enumeration[i] = i;
		}
		return enumeration;
	}

	public static int[] enumerateAscending(final int small, final int large) {
		final int size = large - small + 1;
		final int[] enumeration = new int[size];
		for(int i = small; i <= large; i++) {
			enumeration[i - small] = i;
		}
		return enumeration;
	}

	public static int[] enumerateAscending(final int small, final int large, final int step) {
		insist((large - small) % step == 0, "small (" + small + ") and large (" + large + ") must be separated by a multiple of step (" + step + ")");

		final int size = (large - small) / step + 1;
		final int[] enumeration = new int[size];
		for(int i = 0; i < size; i++) {
			enumeration[i] = small + step * i;
		}
		return enumeration;
	}

	public static int[] enumerateDescending(final int number) {
		final int[] enumeration = new int[number];
		for(int i = 0; i < number; i++) {
			enumeration[number - i - 1] = i;
		}
		return enumeration;
	}

	public static int[] enumerateDescending(final int small, final int large) {
		final int size = large - small + 1;
		final int[] enumeration = new int[size];
		for(int i = small; i <= large; i++) {
			enumeration[large - i] = i;
		}
		return enumeration;
	}

	public static int[] enumeratePowersAscending(final int n) {
		final int[] result = new int[n];
		for(int i = 0; i < n; i++) {
			result[i] = 1 << i;
		}
		return result;
	}

	// TODO: use arrays??? delete???
	static String toString(final Object[][] objects) {
		StringBuffer buffer = new StringBuffer();
		for(int tier = 0; tier < objects.length; tier++) {
			for(int term = 0; term < objects[tier].length; term++) {
				if(objects[tier][term] instanceof HashSet<?>) {
					HashSet<?> equationFacts = (HashSet<?>)(objects[tier][term]);
					buffer.append("  tier " + tier + " term " + term + ": " + Utility.toStringFromSet(equationFacts) + "\n");
				} else {
					buffer.append(objects[tier][term] + "\n");
				}
			}
		}
		return buffer.toString();
	}

	static <T> Vector<T> toVector(T[] array) {
		final Vector<T> vector = new Vector<T>();
		for(final T constraint : array) {
			vector.add(constraint);
		}
		return vector;
	}

	public static <T> Vector<T> toVector(HashSet<T> array) {
		final Vector<T> vector = new Vector<T>();
		for(final T constraint : array) {
			vector.add(constraint);
		}
		return vector;
	}

	// NOTE: presumes values is sorted
	private static int numberOfUniqueValues(final int[] values) {
		if(values.length <= 1) {
			return values.length;
		}

		int numberOfUniqueValues = 1;
		for(int i = 1; i < values.length; i++) {
			if(values[i - 1] != values[i]) {
				numberOfUniqueValues += 1;
			}
		}
		return numberOfUniqueValues;
	}

	// NOTE: presumes values is sorted
	private static int[] uniqueValues(int[] values) {
		final int numberOfUniqueValues = numberOfUniqueValues(values);
		final int[] uniqueValues = new int[numberOfUniqueValues];
	
		int to = 0, from = 0;
		uniqueValues[to++] = values[from++];

		while(from < values.length) {
			if(values[from - 1] != values[from]) {
				uniqueValues[to++] = values[from++];
			} else {
				from++;
			}
		}
		return uniqueValues;
	}

	// insist that values has at least 1 element?
	static int[] sortAndRemoveDuplicates(int[] values) {
		final int[] tempValues = Arrays.copyOf(values, values.length);
		Arrays.sort(tempValues);
		return uniqueValues(tempValues);
	}

	public static int lowestNBitsSet(final int n) {
		return (1 << n) - 1;
	}

	public static int sqr(int x) {
		return x * x;
	}

	public static String toString(String[] strings) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		if(strings != null) {
			String separator = "";
			for(String message : strings) {
				buffer.append(separator);
				buffer.append(message);
				separator = ", ";
			}
		}
		buffer.append("}");
		return buffer.toString();
	}

	public static String toString(boolean[] booleans) {
		StringBuffer buffer = new StringBuffer();
		if(booleans == null) {
			buffer.append("null");
		} else {
			buffer.append("[");
			String separator = "";
			for(boolean b : booleans) {
				buffer.append(separator);
				buffer.append(b ? "t" : "f");
				separator = " ";
			}
			buffer.append("]");
		}
		return buffer.toString();
	}

	public static String[][] copy(String[][] arrays) {
		if(arrays == null) {
			return null;
		}
		final String[][] copy = new String[arrays.length][];
		for(int i = 0; i < arrays.length; i++) {
			copy[i] = arrays[i] == null
					? null
					: Arrays.copyOf(arrays[i], arrays[i].length);
		}
		return copy;
	}

	public static int population(boolean[] booleans) {
		int population = 0;
		for(boolean b : booleans) {
			population += b ? 1 : 0;
		}
		return population;
	}

	public static int numberOfNodeTiers(final int numberOfDecisions) {
		return log2RoundedUp(numberOfDecisions) + 1;
	}

	public static int numberOfNodeTerms(final int numberOfDecisions) {
		return roundUpToPowerOfTwo(numberOfDecisions);
	}

	public static boolean isEqualToAny(boolean[] a, boolean[][] bs) {
		for(boolean[] b : bs) {
			if(Arrays.equals(a, b)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEqual(boolean[] a, boolean[] b) {
		return Arrays.equals(a,  b);
	}

	static <BType> HashSet<BType> createHashSetWith(final BType b) {
		final HashSet<BType> newBs = new HashSet<BType>();
		newBs.add(b);
		return newBs;
	}

	static int[] toArrayFromSet(HashSet<Integer> decisionSet) {
		final int[] decisions = new int[decisionSet.size()];
		int i = 0;
		for(Integer decision : decisionSet) {
			decisions[i++] = decision.intValue();
		}
		return decisions;
	}

	public static boolean isEqualToAny(long number, int[] array) {
		for(final int element : array) {
			if(number == element) {
				return true;
			}
		}
		return false;
	}

	public static <ElementType> HashSet<ElementType> toHashSet(
		final ElementType[] elements
	) {
		final HashSet<ElementType> newHashSet = new HashSet<ElementType>(elements.length);
		for(ElementType element : elements) {
			newHashSet.add(element);
		}
		return newHashSet;
	}

	public static int toInt(final boolean b) {
		return b ? 1 : 0;
	}

	public static boolean toBoolean(final int i) {
		return i != 0;
	}

	public static boolean toBoolean(final char c) {
		return c != '0';
	}

	public static int[] toInts(boolean[] booleans) {
		final int[] ints = new int[booleans.length];
		for(int i = 0; i < booleans.length; i++) {
			ints[i] = toInt(booleans[i]);
		}
		return ints;
	}
}
