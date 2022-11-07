package hadamard;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class HadamardTest {

	interface CreateMatrixLambda {
		int[][] createMatrix(int size);
	}

	interface HadamardTransformLambda {
		int[] hadamardTransform(int[] input);
	}

	private static final int[][] EXPECTED_SYLVESTER_MATRIX_OUTPUT_1 = new int[][] {
		new int[]{1},
	};
	private static final int[][] EXPECTED_SYLVESTER_MATRIX_OUTPUT_2 = new int[][] {
		new int[]{1,  1},
		new int[]{1, -1},
	};
	private static final int[][] EXPECTED_SYLVESTER_MATRIX_OUTPUT_4 = new int[][] {
		new int[]{1,  1,  1,  1},
		new int[]{1, -1,  1, -1},
		new int[]{1,  1, -1, -1},
		new int[]{1, -1, -1,  1},
	};
	private static final int[][] EXPECTED_SYLVESTER_MATRIX_OUTPUT_8 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1},
	};
	private static final int[][] EXPECTED_SYLVESTER_MATRIX_OUTPUT_16 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1,  1, -1},
		new int[]{1,  1,  1,  1,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  1,  1,  1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1,  1, -1,  1, -1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1,  1,  1, -1, -1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1,  1, -1, -1,  1},
	};

	private static final int[][] EXPECTED_SEQUENCY_MATRIX_OUTPUT_1 = new int[][] {
		new int[]{1},
	};
	private static final int[][] EXPECTED_SEQUENCY_MATRIX_OUTPUT_2 = new int[][] {
		new int[]{1,  1},
		new int[]{1, -1},
	};
	private static final int[][] EXPECTED_SEQUENCY_MATRIX_OUTPUT_4 = new int[][] {
		new int[]{1,  1,  1,  1},
		new int[]{1,  1, -1, -1},
		new int[]{1, -1, -1,  1},
		new int[]{1, -1,  1, -1},
	};
	private static final int[][] EXPECTED_SEQUENCY_MATRIX_OUTPUT_8 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1},
	};
	private static final int[][] EXPECTED_SEQUENCY_MATRIX_OUTPUT_16 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1,  1,  1,  1,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  1,  1,  1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1,  1,  1, -1, -1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1,  1, -1, -1,  1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1,  1, -1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1,  1, -1,  1, -1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1},
	};

	private static final int[][] EXPECTED_DYADIC_MATRIX_OUTPUT_1 = new int[][] {
		new int[]{1},
	};
	private static final int[][] EXPECTED_DYADIC_MATRIX_OUTPUT_2 = new int[][] {
		new int[]{1,  1},
		new int[]{1, -1},
	};
	private static final int[][] EXPECTED_DYADIC_MATRIX_OUTPUT_4 = new int[][] {
		new int[]{1,  1,  1,  1},
		new int[]{1,  1, -1, -1},
		new int[]{1, -1,  1, -1},
		new int[]{1, -1, -1,  1},
	};
	private static final int[][] EXPECTED_DYADIC_MATRIX_OUTPUT_8 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1},
	};
	private static final int[][] EXPECTED_DYADIC_MATRIX_OUTPUT_16 = new int[][] {
		new int[]{1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1},
		new int[]{1,  1,  1,  1,  1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1},
		new int[]{1,  1,  1,  1, -1, -1, -1, -1, -1, -1, -1, -1,  1,  1,  1,  1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1},
		new int[]{1,  1, -1, -1,  1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1,  1,  1, -1, -1, -1, -1,  1,  1},
		new int[]{1,  1, -1, -1, -1, -1,  1,  1, -1, -1,  1,  1,  1,  1, -1, -1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1,  1, -1},
		new int[]{1, -1,  1, -1,  1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1},
		new int[]{1, -1,  1, -1, -1,  1, -1,  1, -1,  1, -1,  1,  1, -1,  1, -1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,  1},
		new int[]{1, -1, -1,  1,  1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1,  1, -1, -1,  1, -1,  1,  1, -1},
		new int[]{1, -1, -1,  1, -1,  1,  1, -1, -1,  1,  1, -1,  1, -1, -1,  1},
	};

	@Test
	public void testMatrixFunctionResults() {
		checkResults( 1, (i) -> Hadamard.sylvesterMatrix(i), EXPECTED_SYLVESTER_MATRIX_OUTPUT_1);
		checkResults( 2, (i) -> Hadamard.sylvesterMatrix(i), EXPECTED_SYLVESTER_MATRIX_OUTPUT_2);
		checkResults( 4, (i) -> Hadamard.sylvesterMatrix(i), EXPECTED_SYLVESTER_MATRIX_OUTPUT_4);
		checkResults( 8, (i) -> Hadamard.sylvesterMatrix(i), EXPECTED_SYLVESTER_MATRIX_OUTPUT_8);
		checkResults(16, (i) -> Hadamard.sylvesterMatrix(i), EXPECTED_SYLVESTER_MATRIX_OUTPUT_16);

		checkResults( 1, (i) -> Hadamard.sequencyMatrix(i), EXPECTED_SEQUENCY_MATRIX_OUTPUT_1);
		checkResults( 2, (i) -> Hadamard.sequencyMatrix(i), EXPECTED_SEQUENCY_MATRIX_OUTPUT_2);
		checkResults( 4, (i) -> Hadamard.sequencyMatrix(i), EXPECTED_SEQUENCY_MATRIX_OUTPUT_4);
		checkResults( 8, (i) -> Hadamard.sequencyMatrix(i), EXPECTED_SEQUENCY_MATRIX_OUTPUT_8);
		checkResults(16, (i) -> Hadamard.sequencyMatrix(i), EXPECTED_SEQUENCY_MATRIX_OUTPUT_16);

		checkResults( 1, (i) -> Hadamard.dyadicMatrix(i), EXPECTED_DYADIC_MATRIX_OUTPUT_1);
		checkResults( 2, (i) -> Hadamard.dyadicMatrix(i), EXPECTED_DYADIC_MATRIX_OUTPUT_2);
		checkResults( 4, (i) -> Hadamard.dyadicMatrix(i), EXPECTED_DYADIC_MATRIX_OUTPUT_4);
		checkResults( 8, (i) -> Hadamard.dyadicMatrix(i), EXPECTED_DYADIC_MATRIX_OUTPUT_8);
		checkResults(16, (i) -> Hadamard.dyadicMatrix(i), EXPECTED_DYADIC_MATRIX_OUTPUT_16);
	}

	@Test
	public void testMatrixFunctionThrows() {
		checkThrow(0, (i) -> Hadamard.sylvesterMatrix(i));
		checkThrow(3, (i) -> Hadamard.sylvesterMatrix(i));
		checkThrow(5, (i) -> Hadamard.sylvesterMatrix(i));
		checkThrow(6, (i) -> Hadamard.sylvesterMatrix(i));
		checkThrow(7, (i) -> Hadamard.sylvesterMatrix(i));
		checkThrow(9, (i) -> Hadamard.sylvesterMatrix(i));

		checkThrow(0, (i) -> Hadamard.sequencyMatrix(i));
		checkThrow(3, (i) -> Hadamard.sequencyMatrix(i));
		checkThrow(5, (i) -> Hadamard.sequencyMatrix(i));
		checkThrow(6, (i) -> Hadamard.sequencyMatrix(i));
		checkThrow(7, (i) -> Hadamard.sequencyMatrix(i));
		checkThrow(9, (i) -> Hadamard.sequencyMatrix(i));

		checkThrow(0, (i) -> Hadamard.dyadicMatrix(i));
		checkThrow(3, (i) -> Hadamard.dyadicMatrix(i));
		checkThrow(5, (i) -> Hadamard.dyadicMatrix(i));
		checkThrow(6, (i) -> Hadamard.dyadicMatrix(i));
		checkThrow(7, (i) -> Hadamard.dyadicMatrix(i));
		checkThrow(9, (i) -> Hadamard.dyadicMatrix(i));
	}

	private void checkResults(int size, CreateMatrixLambda lambda, int[][] hadamardMatrix) {
		assertArrayEquals(lambda.createMatrix(size), hadamardMatrix);
	}

	private void checkThrow(int size, CreateMatrixLambda lambda) {
		Exception exception = null;
		try {
			lambda.createMatrix(size);
		} catch(Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals(exception.getMessage(), Hadamard.errorMessage_SizeNotPowerOfTwo);
	}

	// these test vectors came from octave online
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_1  = new int[]{1};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_2  = new int[]{3, -1};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_4  = new int[]{15, -5, -9, 3};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_8  = new int[]{255, -85, -153, 51, -225, 75, 135, -45};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_16 = new int[]{65535, -21845, -39321, 13107, -57825, 19275, 34695, -11565, -65025, 21675, 39015, -13005, 57375, -19125, -34425, 11475};

	private static final int[] EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_1  = new int[]{1};
	private static final int[] EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_2  = new int[]{3, -1};
	private static final int[] EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_4  = new int[]{15, -9, 3, -5};
	private static final int[] EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_8  = new int[]{255, -225, 135, -153, 51, -45, 75, -85};
	private static final int[] EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_16 = new int[]{65535, -65025, 57375, -57825, 34695, -34425, 39015, -39321, 13107, -13005, 11475, -11565, 19275, -19125, 21675, -21845};

	private static final int[] EXPECTED_DYADIC_TRANSFORM_OUTPUT_1  = new int[]{1};
	private static final int[] EXPECTED_DYADIC_TRANSFORM_OUTPUT_2  = new int[]{3, -1};
	private static final int[] EXPECTED_DYADIC_TRANSFORM_OUTPUT_4  = new int[]{15, -9, -5, 3};
	private static final int[] EXPECTED_DYADIC_TRANSFORM_OUTPUT_8  = new int[]{255, -225, -153, 135, -85, 75, 51, -45};
	private static final int[] EXPECTED_DYADIC_TRANSFORM_OUTPUT_16 = new int[]{65535, -65025, -57825, 57375, -39321, 39015, 34695, -34425, -21845, 21675, 19275, -19125, 13107, -13005, -11565, 11475};

	@Test
	public void testFastTransforms() {
		assertArrayEquals(Hadamard.fastSylvesterTransform(Utility.powers(1)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_1);
		assertArrayEquals(Hadamard.fastSylvesterTransform(Utility.powers(2)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_2);
		assertArrayEquals(Hadamard.fastSylvesterTransform(Utility.powers(4)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_4);
		assertArrayEquals(Hadamard.fastSylvesterTransform(Utility.powers(8)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_8);
		assertArrayEquals(Hadamard.fastSylvesterTransform(Utility.powers(16)), EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_16);

		assertArrayEquals(Hadamard.fastSequencyTransform(Utility.powers(1)),  EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_1);
		assertArrayEquals(Hadamard.fastSequencyTransform(Utility.powers(2)),  EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_2);
		assertArrayEquals(Hadamard.fastSequencyTransform(Utility.powers(4)),  EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_4);
		assertArrayEquals(Hadamard.fastSequencyTransform(Utility.powers(8)),  EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_8);
		assertArrayEquals(Hadamard.fastSequencyTransform(Utility.powers(16)), EXPECTED_SEQUENCY_TRANSFORM_OUTPUT_16);

		assertArrayEquals(Hadamard.fastDyadicTransform(Utility.powers(1)),  EXPECTED_DYADIC_TRANSFORM_OUTPUT_1);
		assertArrayEquals(Hadamard.fastDyadicTransform(Utility.powers(2)),  EXPECTED_DYADIC_TRANSFORM_OUTPUT_2);
		assertArrayEquals(Hadamard.fastDyadicTransform(Utility.powers(4)),  EXPECTED_DYADIC_TRANSFORM_OUTPUT_4);
		assertArrayEquals(Hadamard.fastDyadicTransform(Utility.powers(8)),  EXPECTED_DYADIC_TRANSFORM_OUTPUT_8);
		assertArrayEquals(Hadamard.fastDyadicTransform(Utility.powers(16)), EXPECTED_DYADIC_TRANSFORM_OUTPUT_16);
	}

	@Test
	public void compareAllFastAndSlowTransforms() {
		final Random random = new Random();
		final int numberOfAttempts = 50;

		for(int attempt = 0; attempt < numberOfAttempts; attempt++) {
			int[] input = Utility.randomVector(32, random);
			compareOneSlowAndFastTransform((i) -> Hadamard.sylvesterMatrix(i), (v) -> Hadamard.fastSylvesterTransform(v), input);
			compareOneSlowAndFastTransform((i) -> Hadamard.sequencyMatrix(i),  (v) -> Hadamard.fastSequencyTransform(v),  input);
			compareOneSlowAndFastTransform((i) -> Hadamard.dyadicMatrix(i),    (v) -> Hadamard.fastDyadicTransform(v),    input);
		}

		for(int attempt = 0; attempt < numberOfAttempts; attempt++) {
			int[] input = Utility.randomVector(256, random);
			compareOneSlowAndFastTransform((i) -> Hadamard.sylvesterMatrix(i), (v) -> Hadamard.fastSylvesterTransform(v), input);
			compareOneSlowAndFastTransform((i) -> Hadamard.sequencyMatrix(i),  (v) -> Hadamard.fastSequencyTransform(v),  input);
			compareOneSlowAndFastTransform((i) -> Hadamard.dyadicMatrix(i),    (v) -> Hadamard.fastDyadicTransform(v),    input);
		}

		for(int attempt = 0; attempt < numberOfAttempts; attempt++) {
			int[] input = Utility.randomVector(2048, random);
			compareOneSlowAndFastTransform((i) -> Hadamard.sylvesterMatrix(i), (v) -> Hadamard.fastSylvesterTransform(v), input);
			compareOneSlowAndFastTransform((i) -> Hadamard.sequencyMatrix(i),  (v) -> Hadamard.fastSequencyTransform(v),  input);
			compareOneSlowAndFastTransform((i) -> Hadamard.dyadicMatrix(i),    (v) -> Hadamard.fastDyadicTransform(v),    input);
		}
	}

	private void compareOneSlowAndFastTransform(CreateMatrixLambda createHadamardMatrix, HadamardTransformLambda fastHadamardTransform, int[] input) {
		int[][] matrix = createHadamardMatrix.createMatrix(input.length);
		int[] slowOutput = Hadamard.apply(matrix, input);
		int[] fastOutput = fastHadamardTransform.hadamardTransform(input);
		assertArrayEquals(slowOutput, fastOutput);
	}

	private static final int[][] EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_1 = new int[][]{
		new int[]{1},
	};
	private static final int[][] EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_2 = new int[][]{
		new int[]{1,  2},
		new int[]{3, -1},
	};
	private static final int[][] EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_4 = new int[][]{
		new int[]{ 1,  2,  4,  8},
		new int[]{ 3, -1, 12, -4},
		new int[]{15, -5, -9,  3},
	};
	private static final int[][] EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_8 = new int[][]{
		new int[]{  1,   2,    4,  8,   16,  32,   64, 128},
		new int[]{  3,  -1,   12, -4,   48, -16,  192, -64},
		new int[]{ 15,  -5,   -9,  3,  240, -80, -144,  48},
		new int[]{255, -85, -153, 51, -225,  75,  135, -45},
	};
	private static final int[][] EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_16 = new int[][]{
		new int[]{    1,      2,      4,     8,     16,    32,    64,    128,    256,    512,   1024,   2048,   4096,   8192,  16384,  32768},
		new int[]{    3,     -1,     12,    -4,     48,   -16,   192,    -64,    768,   -256,   3072,  -1024,  12288,  -4096,  49152, -16384},
		new int[]{   15,     -5,     -9,     3,    240,   -80,  -144,     48,   3840,  -1280,  -2304,    768,  61440, -20480, -36864,  12288},
		new int[]{  255,    -85,   -153,    51,   -225,    75,   135,    -45,  65280, -21760, -39168,  13056, -57600,  19200,  34560, -11520},
		new int[]{65535, -21845, -39321, 13107, -57825, 19275, 34695, -11565, -65025,  21675,  39015, -13005,  57375, -19125, -34425,  11475},
	};

	@Test
	public void testSylvesterButterfly() {
		assertArrayEquals(Hadamard.sylvesterButterfly(Utility.powers(1)),  EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_1);
		assertArrayEquals(Hadamard.sylvesterButterfly(Utility.powers(2)),  EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_2);
		assertArrayEquals(Hadamard.sylvesterButterfly(Utility.powers(4)),  EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_4);
		assertArrayEquals(Hadamard.sylvesterButterfly(Utility.powers(8)),  EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_8);
		assertArrayEquals(Hadamard.sylvesterButterfly(Utility.powers(16)), EXPECTED_SYLVESTER_BUTTERFLY_OUTPUT_16);
	}
}
