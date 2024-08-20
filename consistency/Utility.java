package consistency;

import java.util.Arrays;
import java.util.Random;

// TODO: verify public used correctly

/**
 * Miscellaneous static functions that are used by consistency its applications.
 */
public final class Utility {
	public static final String FIX_TYPE                             = "fix type";
	public static final String NUMBER_MUST_BE_AT_MOST_UPPER_BOUND   = "number must be at most upper bound";
	public static final String NUMBER_MUST_BE_AT_LEAST_LOWER_BOUND  = "number must be at least lower bound";
	public static final String NUMBERS_MUST_BE_EQUAL                = "numbers must be equal";
	public static final String NUMBERS_MUST_HAVE_THE_SAME_PARITY    = "numbers must have the same parity";
	public static final String NUMBER_MUST_BE_A_POWER_OF_TWO        = "number must be a power of two";
	public static final String NUMBER_MUST_BE_POSITIVE              = "number must be positive";

	private static final Random randomNumberGenerator = new Random();

	// -----------------------------------------------------------------------
	// insist functions

    /**
     * Throw an exception if condition is false.
     * 
     * @param condition whether the condition is met
     * @param message the string to throw
     */
	public static void insist(final boolean condition, final String message) {
		if(!condition) {
			throw new RuntimeException(message);
		}
	}

    /**
     * Throw an exception if two numbers differ.
     * 
     * @param a the first number to test
     * @param b the second number to test
     */
	public static void insistEqual(final int a, final int b) {
		insist(a == b, NUMBERS_MUST_BE_EQUAL);
	}

    /**
     * Throw an exception if two numbers have different parities.
     * 
     * @param a the first number to test
     * @param b the second number to test
     */
	public static void insistSameParity(final int a, final int b) {
		insist(haveSameParity(a, b), NUMBERS_MUST_HAVE_THE_SAME_PARITY);
	}

    /**
     * Throw an exception if the number lies outside of the given range.
     * 
     * @param lowerBound the lower bound of the range
     * @param n the number to test
     * @param upperBount the upper bound of the range
     */
	public static void insistInRange(final int lowerBound, final int n, final int upperBound) {
		insist(lowerBound <= n, NUMBER_MUST_BE_AT_LEAST_LOWER_BOUND);
		insist(n <= upperBound, NUMBER_MUST_BE_AT_MOST_UPPER_BOUND);
	}

    /**
     * Throw an exception if the number is not a power of 2.
     * 
     * @param n the number to test
     */
	public static void insistIsPowerOfTwo(final int n) {
		insist(isPowerOfTwo(n), NUMBER_MUST_BE_A_POWER_OF_TWO);
	}

	// -----------------------------------------------------------------------
	// integer functions

    /**
     * Determines whether a number is a power of 2.
     * 
     * @param n the number to test
     * @return the boolean result
     */
	public static boolean isPowerOfTwo(final int n) {
		return n > 0 && Integer.lowestOneBit(n) == n;
	}

    /**
     * Computes the first power of 2 that is greater or equal to a number.
     * 
     * @param n the number to test
     * @return the integer result
     */
	public static int roundUpToPowerOfTwo(final int n) {
		int m = n - 1;
		m |= m >>>  1;
		m |= m >>>  2;
		m |= m >>>  4;
		m |= m >>>  8;
		m |= m >>> 16;
		return m + 1;
	}

    /**
     * Computes the smallest logarithm that is greater or equal to the logarithm of a number.
     * Logarithms are base 2.
     * 
     * @param n the number
     * @return the integer result
     */
	public static int log2RoundedUp(final int n) {
		insist(n > 0, NUMBER_MUST_BE_POSITIVE);

		int mostSignificantBit = 0, m = n - 1;
		if((m >>> 16) != 0) { mostSignificantBit += 16; m = m >>> 16; }
		if((m >>>  8) != 0) { mostSignificantBit +=  8; m = m >>>  8; }
		if((m >>>  4) != 0) { mostSignificantBit +=  4; m = m >>>  4; }
		if((m >>>  2) != 0) { mostSignificantBit +=  2; m = m >>>  2; }
		if((m >>>  1) != 0) { mostSignificantBit +=  1; m = m >>>  1; }
		return n == 1 ? 0 : mostSignificantBit + 1;
	}

    /**
     * Creates a bit mask with the lowest n bits set to true.
     * 
     * @param n the number of bits to set to true
     * @return the mask
     */
	public static int lowBitsMask(final int n) {
		return (1 << n) - 1;
	}

    /**
     * Determines whether two integers have the same parity.
     * 
     * @param a an integer
     * @param b another integer
     * @return true or false
     */
	public static boolean haveSameParity(final int a, final int b) {
		return (a & 1) == (b & 1);
	}

	/**
	 * Returns value, bounded by the range from minimum to maximum.
	 * 
	 * @param minimum the minimum value to return
	 * @param value the value to return, if it lies within the range
	 * @param maximum the maximum value to return
	 * @return the bounded value
	 */
	public static int bound(int minimum, int value, int maximum) {
		Utility.insist(minimum <= maximum, "bad");

		return value < minimum ? minimum
			 : value > maximum ? maximum
			 :                   value;
	}

    /**
     * Converts a boolean into an int. True becomes 1 and false becomes 0.
     * 
     * @param a the boolean
     * @return the int
     */
	public static int toInt(final boolean b) {
		return b ? 1 : 0;
	}

    /**
     * Converts an int into a boolean. Non-zeros become true and zero becomes false.
     * 
     * @param i the int
     * @return the boolean
     */
	public static boolean toBoolean(final int i) {
		return i != 0;
	}

    /**
     * Converts a char into a boolean. Non-'0's become true and '0' becomes false.
     * 
     * @param c the char
     * @return the boolean
     */
	public static boolean toBoolean(final char c) {
		return c != '0';
	}

    /**
     * Converts an int into a byte. Throws an exception if value is out of range.
     * 
     * @param i the int
     * @return the byte
     */
	public static byte toByte(final int i) {
		insist(Byte.MIN_VALUE <= i, FIX_TYPE);
		insist(i <= Byte.MAX_VALUE, FIX_TYPE);
		return (byte) i;
	}

    /**
     * Converts an int into a short. Throws an exception if value is out of range.
     * 
     * @param i the int
     * @return the short
     */
	public static short toShort(final int i) {
		insist(Short.MIN_VALUE <= i, FIX_TYPE);
		insist(i <= Short.MAX_VALUE, FIX_TYPE);
		return (short) i;
	}

	// -----------------------------------------------------------------------
	// array functions

    /**
     * Computes the population of an int array, specifically the number of non-zeros.
     * 
     * @param ints the array
     * @return the population
     */
	public static int population(final int[] ints) {
		int population = 0;
		for(final int i : ints) {
			population += i != 0 ? 1 : 0;
		}
		return population;
	}

    /**
     * Computes the population of a boolean array, specifically the number of non-falses.
     * 
     * @param booleans the array
     * @return the population
     */
	public static int population(final boolean[] booleans) {
		int population = 0;
		for(final boolean b : booleans) {
			population += b ? 1 : 0;
		}
		return population;
	}

    /**
     * Randomize an array of integers in place.
     * 
     * @param ints the array
     */
	public static void randomize(final int[] ints) {
		for(int i = ints.length - 1; i > 0; i--) {
			swap(ints, i, randomNumberGenerator.nextInt(i + 1));
		}
	}

    /**
     * Swap two elements of an array, in place.
     * 
     * @param array the array
     * @param i the index of the first element
     * @param j the index of the second element
     */
	private static void swap(final int[] array, final int i, final int j) {
		final int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

    /**
     * Converts an array of booleans into an array of ints. Trues become ones and falses becomes zeros.
     * 
     * @param booleans the array of booleans
     * @return the array of ints
     */
	public static int[] toInts(final boolean[] booleans) {
		final int[] ints = new int[booleans.length];
		for(int i = 0; i < booleans.length; i++) {
			ints[i] = toInt(booleans[i]);
		}
		return ints;
	}

    /**
     * Converts an array of ints into an array of booleans. Non-zeros become trues and zeros becomes falses.
     * 
     * @param ints the array of ints
     * @return the array of booleans
     */
	public static boolean[] toBooleans(final int[] ints) {
		final boolean[] booleans = new boolean[ints.length];
		for(int i = 0; i < ints.length; i++) {
			booleans[i] = ints[i] != 0;
		}
		return booleans;
	}

    /**
     * Converts a long into an array of ints. Bits[0] becomes ints[0] and so on.
     * 
     * @param bits a long that holds the bits
     * @param length the number of bits to convert
     * @return the array of ints
     */
	public static int[] toInts(final long bits, final int  length) {
		final int[] ints = new int[length];
		for(int i = 0; i < length; i++) {
			ints[i] = (int) ((bits >> i) & 0x1);
		}
		return ints;
	}

    /**
     * Make a deep copy of a two-dimensional array of strings.
     * Null elements are allowed.
     * 
     * @param array the array to copy
     * @return the copy
     */
	public static String[][] deepCopy(final String[][] arrays) {
		if(arrays == null) {
			return null;
		} else {
			final String[][] copy = new String[arrays.length][];
			for(int i = 0; i < arrays.length; i++) {
				copy[i] = arrays[i] == null
						? null
						: Arrays.copyOf(arrays[i], arrays[i].length);
			}
			return copy;
		}
	}

    /**
     * Determines whether an array of ints is sorted and all values are unique.
     * 
     * @param ints the input array
     * @return true or false
     */
	public static boolean sortedAndUnique(int[] ints) {
		for(int i = 1; i < ints.length; i++) {
			if(ints[i - 1] >= ints[i]) {
				return false;
			}
		}
		return true;
	}

    /**
     * Determines whether an array of ints contains a particular value.
     * Treats the array as a set of values.
     * 
     * @param intToFind the value to find
     * @param ints the set of values to search through
     * @return true or false
     */
	public static boolean contains(final int intToFind, final int[] ints) {
		if(ints == null) {
			return false;
		}
		for(final int intInArray : ints) {
			if(intToFind == intInArray) {
				return true;
			}
		}
		return false;
	}

    /**
     * Determines whether an array of strings contains a particular value.
     * Treats the array as a set of values.
     * Null values are handled clumsily.
     * 
     * @param stringToFind the value to find
     * @param strings the set of values to search through
     * @return true or false
     */
	public static boolean contains(final String stringToFind, final String[] strings) {
		if(stringToFind == null && strings == null) {
			return true;
		}
		if(stringToFind == null || strings == null) {
			return false;
		}
		for(final String stringInArray : strings) {
			if(stringToFind == stringInArray) {
				return true;
			}
		}
		return false;
	}

    /**
     * Determines whether an array of arrays contains a particular value.
     * Treats the array as a set of values.
     * Null values are handled clumsily.
     * 
     * @param arrayToFind the value to find
     * @param arrays the set of values to search through
     * @return true or false
     */
	public static boolean contains(final boolean[] arrayToFind, final boolean[][] arrays) {
		if(arrayToFind == null && arrays == null) {
			return true;
		}
		if(arrayToFind == null || arrays == null) {
			return false;
		}
		for(boolean[] arrayInArrays : arrays) {
			if(Arrays.equals(arrayToFind, arrayInArrays)) {
				return true;
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------
	// sylvester transform functions

    /**
     * Compute the number of node tiers for a butterfly given a number of decisions.
     * 
     * @param numberOfDecisions the number of decisions in the problem
     * @return the number of node tiers in the butterfly
     */
	public static int numberOfNodeTiers(final int numberOfDecisions) {
		return log2RoundedUp(numberOfDecisions) + 1;
	}

    /**
     * Compute the number of node terms for a butterfly given a number of decisions.
     * 
     * @param numberOfDecisions the number of decisions in the problem
     * @return the number of node terms in the butterfly
     */
	public static int numberOfNodeTerms(final int numberOfDecisions) {
		return roundUpToPowerOfTwo(numberOfDecisions);
	}

    /**
     * Compute the fast Hadamard transform of a vector, using the Sylvester matrix.
     * 
     * @param vector the input vector, usually binary
     * @return the Sylvester transform of the input vector
     */
	public static int[] fastSylvesterTransform(final int[] vector) {
		insistIsPowerOfTwo(vector.length);

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

    /**
     * Compute the inverse fast Hadamard transform of a vector, using the Sylvester matrix.
     * Throws exception if the resulting vector is not binary.
     * 
     * @param vector the Sylvester transform vector
     * @return the original input vector, must be binary
     */
	public static int[] inverseFastSylvesterTransform(final int[] vector) {
		insistIsPowerOfTwo(vector.length);

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

    /**
     * Compute the whole butterfly of the fast Hadamard transform of a vector, using the Sylvester matrix.
     * 
     * @param vector the input vector, usually binary
     * @return the whole butterfly of the fast Hadamard transform
     */
	public static int[][] sylvesterButterfly(final int[] vector) {
		insistIsPowerOfTwo(vector.length);

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

	// -----------------------------------------------------------------------
	// enumeration

    /**
     * Create an array of the integers from zero to number - 1.
     * Used to make simple iterators.
     * 
     * @param number the number of values in the array
     * @return the array of ascending values
     */
	public static int[] enumerateAscending(final int number) {
		final int[] enumeration = new int[number];
		for(int i = 0; i < number; i++) {
			enumeration[i] = i;
		}
		return enumeration;
	}

    /**
     * Create an array of the integers from small up to large.
     * Used to make simple iterators.
     * 
     * @param small the smallest value in the array
     * @param large the largest value in the array
     * @return the array of ascending values
     */
	public static int[] enumerateAscending(final int small, final int large) {
		final int size = large - small + 1;
		final int[] enumeration = new int[size];
		for(int i = small; i <= large; i++) {
			enumeration[i - small] = i;
		}
		return enumeration;
	}

    /**
     * Create an array of the integers from small up to large by step.
     * Used to make simple iterators.
     * 
     * @param small the smallest value in the array
     * @param large the largest value in the array
     * @param step difference between adjacent values
     * @return the array of ascending values
     */
	public static int[] enumerateAscending(final int small, final int large, final int step) {
		insist((large - small) % step == 0, "small (" + small + ") and large (" + large + ") must be separated by a multiple of step (" + step + ")");

		final int size = (large - small) / step + 1;
		final int[] enumeration = new int[size];
		for(int i = 0; i < size; i++) {
			enumeration[i] = small + step * i;
		}
		return enumeration;
	}

    /**
     * Create an array of the integers from number - 1 down to 0.
     * Used to make simple iterators.
     * 
     * @param number the number of values in the array
     * @return the array of descending values
     */
	public static int[] enumerateDescending(final int number) {
		final int[] enumeration = new int[number];
		for(int i = 0; i < number; i++) {
			enumeration[number - i - 1] = i;
		}
		return enumeration;
	}

    /**
     * Create an array of the integers from large down to small.
     * Used to make simple iterators.
     * 
     * @param small the smallest value in the array
     * @param large the largest value in the array
     * @return the array of descending values
     */
	public static int[] enumerateDescending(final int small, final int large) {
		final int size = large - small + 1;
		final int[] enumeration = new int[size];
		for(int i = small; i <= large; i++) {
			enumeration[large - i] = i;
		}
		return enumeration;
	}

    /**
     * Create an array that contains all of the values from zero to number - 1, in random order.
     * Used to make simple iterators. This also signals that this loop could be parallelized.
     * 
     * @param number the number of values in the array
     * @return the array of random values
     */
	public static int[] enumerateRandomly(final int number) {
		final int[] enumeration = enumerateAscending(number);
		randomize(enumeration);
		return enumeration;
	}

    /**
     * Create an array that contains all of the values from 1 to 2 ^ n.
     * Used to make simple iterators.
     * This is used for testing.
     * 
     * @param n the number of values in the array
     * @return the array of values
     */
	public static int[] enumeratePowersAscending(final int n) {
		final int[] result = new int[n];
		for(int i = 0; i < n; i++) {
			result[i] = 1 << i;
		}
		return result;
	}

	// -----------------------------------------------------------------------
	// to string functions

    /**
     * Converts an array of booleans into a string.
     * Boolean values are represented using 't' and 'f'.
     * 
     * @param array the array of booleans to represent
     * @return the resulting string
     */
	public static String toString(final boolean[] array) {
		if(array == null) {
			return "null";
		}

		final StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		String separator = "";
		for(boolean b : array) {
			buffer.append(separator);
			buffer.append(b ? "t" : "f");
			separator = " ";
		}
		buffer.append("]");
		return buffer.toString();
	}

    /**
     * Converts an array of ints into a string, as if it were a set.
     * 
     * @param arrayAsSet the array of ints to represent
     * @return the resulting string
     */
	public static String toStringAsSet(final int[] arrayAsSet) {
		final StringBuffer buffer = new StringBuffer();
		String separator = "";
		buffer.append('{');
		for(final int i : arrayAsSet) {
			buffer.append(separator);
			buffer.append(i);
			separator = " ";
		}
		buffer.append('}');
		return buffer.toString();
	}

    /**
     * Converts a butterfly of ints into a string.
     * 
     * @param butterfly the butterfly of ints to represent
     * @return the resulting string
     */
	public static String toString(final int[][] butterfly) {
		final StringBuffer buffer = new StringBuffer();
		for(final int[] ints : butterfly) {
			String separator = "";
			for(final int i : ints) {
				buffer.append(separator);
				buffer.append(i);
				separator = " ";
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

    /**
     * Converts a set of objects into a string.
     * Strings will have quotes added.
     * 
     * @param set the set to represent
     * @return the resulting string
     */
	public static <ElementType> String toStringFromSet(final SimpleHashSet<ElementType> set) {
		final StringBuffer buffer = new StringBuffer();
		if(set == null) {
			buffer.append("null");
		} else {
			String separator = "";
			buffer.append("{");
			for(ElementType element : set) {
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

	// -----------------------------------------------------------------------
	// odds and ends functions

    /**
     * Computes a character to represent the size of the argument.
     * Essentially, it is log n.
     * It maps 0 to '0'; 1 to '1'; 2 and 3 to '2'; 4, 5, 6, and 7 to '3'; and so on.
     * All values 512 or larger get mapped to '*'.
     * 
     * @param n the number to represent
     * @return the character that represents
     */
	public static char code(final int n) {
		for(int i = 0; i < 10; i++) {
			if(n < (1 << i)) {
				return Character.forDigit(i, Character.MAX_RADIX);
			}
		}
		return '*';
	}

    /**
     * Computes the population node term for a given node.
     * 
     * @param nodeTier the tier of the given node
     * @param nodeTerm the term of the given node
     * @return the population node term
     */
	public static int populationNodeTerm(final int nodeTier, final int nodeTerm) {
		return nodeTerm & ~lowBitsMask(nodeTier);
	}

    /**
     * Computes the spine node term for a given node.
     * 
     * @param nodeTier the tier of the given node
     * @param nodeTerm the term of the given node
     * @return the spine node term
     */
	public static int spineNodeTerm(final int nodeTier, final int nodeTerm) {
		return nodeTerm & lowBitsMask(nodeTier);
	}
}
