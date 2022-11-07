package hadamard;

public class Hadamard {
	public static final String errorMessage_SizeNotPowerOfTwo         = "size is not a power of two";
	public static final String errorMessage_VectorLengthNotPowerOfTwo = "vector length is not a power of two";

	public static int[][] sylvesterMatrix(int size) {
		Utility.throwIfFalse(Utility.isAPowerOfTwo(size), errorMessage_SizeNotPowerOfTwo);

		int[][] result = new int[size][size];
		result[0][0] = 1;

		for(int scale = 1; scale < size; scale *= 2) {
			for(int i = 0; i < scale; i++) {
				for(int j = 0; j < scale; j++) {
					result[i + scale][j]         =  result[i][j];
					result[i]        [j + scale] =  result[i][j];
					result[i + scale][j + scale] = -result[i][j];
				}
			}
		}
		return result;
	}

	public static int[][] sequencyMatrix(int size) {
		final int[][] sylvesterMatrix = sylvesterMatrix(size);
		final int[] sequencyOrder = sequencyOrder(size);
		return reorder(sylvesterMatrix, sequencyOrder);
	}

	public static int[][] dyadicMatrix(int size) {
		final int[][] sylvesterMatrix = sylvesterMatrix(size);
		final int[] dyadicOrder = dyadicOrder(size);
		return reorder(sylvesterMatrix, dyadicOrder);
	}

	public static int[] apply(int[][] matrix, int[] vector) {
		Utility.throwIfFalse(Utility.isAPowerOfTwo(vector.length), errorMessage_VectorLengthNotPowerOfTwo);
		Utility.throwIfFalse(matrix.length == vector.length, "mismatch lengths");

		int[] result = new int[vector.length];
		for(int i = 0; i < vector.length; i++) {
			result[i] = 0;
			for(int j = 0; j < vector.length; j++) {
				result[i] += matrix[i][j] * vector[j];
			}
		}
		return result;
	}

	public static int[] fastSylvesterTransform(int[] vector) {
		Utility.throwIfFalse(Utility.isAPowerOfTwo(vector.length), errorMessage_VectorLengthNotPowerOfTwo);

		int[] result = vector.clone();
		for(int scale = 1; scale < vector.length; scale *= 2) {
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					int x = result[j];
					int y = result[j + scale];
					result[j]         = x + y;
					result[j + scale] = x - y;
				}
			}
		}
		return result;
	}

	public static int[] fastSequencyTransform(int[] vector) {
		final int[] sylvesterTransform = fastSylvesterTransform(vector);
		final int[] sequencyOrder = sequencyOrder(vector.length);
		return reorder(sylvesterTransform, sequencyOrder);
	}

	public static int[] fastDyadicTransform(int[] vector) {
		final int[] sylvesterTransform = fastSylvesterTransform(vector);
		final int[] dyadicOrder = dyadicOrder(vector.length);
		return reorder(sylvesterTransform, dyadicOrder);
	}

	@SuppressWarnings("unchecked")
	public static <T extends HadamardValue> T[] fastSylvesterTransform(T[] vector) {
		Utility.throwIfFalse(Utility.isAPowerOfTwo(vector.length), errorMessage_VectorLengthNotPowerOfTwo);

		T[] result = vector.clone();
		for(int scale = 1; scale < vector.length; scale *= 2) {
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					T x = result[j];
					T y = result[j + scale];
					result[j]         = (T)x.add(y);
					result[j + scale] = (T)x.subtract(y);
				}
			}
		}
		return result;
	}

	public static <T extends HadamardValue> T[] fastSequencyTransform(T[] vector) {
		final T[] sylvesterTransform = fastSylvesterTransform(vector);
		final int[] sequencyOrder = sequencyOrder(vector.length);
		return reorder(sylvesterTransform, sequencyOrder);
	}

	public static <T extends HadamardValue> T[] fastDyadicTransform(T[] vector) {
		final T[] sylvesterTransform = fastSylvesterTransform(vector);
		final int[] dyadicOrder = dyadicOrder(vector.length);
		return reorder(sylvesterTransform, dyadicOrder);
	}

	public static int[][] sylvesterButterfly(int[] vector) {
		Utility.throwIfFalse(Utility.isAPowerOfTwo(vector.length), errorMessage_VectorLengthNotPowerOfTwo);

		final int numberOfColumns = Utility.log2(vector.length) + 1;
		final int numberOfRows = vector.length;
		final int[][] result = new int[numberOfColumns][numberOfRows];

		int column = 0;
		for(int i = 0; i < numberOfRows; i++) {
			result[column][i] = vector[i];
		}
		for(int scale = 1; scale < vector.length; scale *= 2) {
			column++;
			for(int i = 0; i < vector.length; i += 2 * scale) {
				for(int j = i; j < i + scale; j++) {
					int x = result[column - 1][j];
					int y = result[column - 1][j + scale];
					result[column][j]         = x + y;
					result[column][j + scale] = x - y;
				}
			}
		}
		return result;
	}

	private static int[] sequencyOrder(int size) {
		int[] result = new int[size];
		result[0] = 0;
		if(size > 1) {
			result[1] = 1;
		}
		for(int scale = 2; scale < size; scale *= 2) {
			for(int i = scale - 2; i >= 0; i -= 2) {
				result[2 * i + 3] = result[i + 1];
				result[2 * i + 2] = result[i + 1] + scale;
				result[2 * i + 1] = result[i + 0] + scale;
				result[2 * i + 0] = result[i + 0];
			}
		}
		return result;
	}

	private static int[] dyadicOrder(int size) {
		int[] result = new int[size];
		result[0] = 0;
		for(int scale = 1; scale < size; scale *= 2) {
			for(int i = 0; i < scale; i++) {
				result[i]         = result[i] * 2;
				result[i + scale] = result[i] + 1;
			}
		}
		return result;
	}

	private static int[][] reorder(int[][] vector, int[] order) {
		int[][] result = new int[vector.length][];
		for(int i = 0; i < vector.length; i++) {
			result[i] = vector[order[i]];
		}
		return result;
	}

	private static int[] reorder(int[] vector, int[] order) {
		int[] result = new int[vector.length];
		for(int i = 0; i < vector.length; i++) {
			result[i] = vector[order[i]];
		}
		return result;
	}

	private static <T extends HadamardValue> T[] reorder(T[] vector, int[] order) {
		T[] result = vector.clone();
		for(int i = 0; i < vector.length; i++) {
			result[i] = vector[order[i]];
		}
		return result;
	}
}
