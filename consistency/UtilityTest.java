package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilityTest {
	@Test
	public void testInsistTrue() {
		boolean caught = false;
		String message = null;
		try {
			Utility.insist(true, "message");
		} catch(Exception exception) {
			caught = true;
			message = exception.getMessage();
		}
		assertEquals(caught, false);
		assertEquals(message, null);
	}

	@Test
	public void testInsistFalse() {
		boolean caught = false;
		String message = null;
		try {
			Utility.insist(false, "message");
		} catch(Exception exception) {
			caught = true;
			message = exception.getMessage();
		}
		assertEquals(caught, true);
		assertEquals(message, "message");
	}

	@Test
	public void testIsPowerOfTwo() {
		assertEquals(Utility.isPowerOfTwo(-1),  false);
		assertEquals(Utility.isPowerOfTwo(0x0), false);
		assertEquals(Utility.isPowerOfTwo(0x1), true);
		assertEquals(Utility.isPowerOfTwo(0x2), true);
		assertEquals(Utility.isPowerOfTwo(0x3), false);
		assertEquals(Utility.isPowerOfTwo(0x4), true);
		assertEquals(Utility.isPowerOfTwo(0x5), false);
		assertEquals(Utility.isPowerOfTwo(0x6), false);
		assertEquals(Utility.isPowerOfTwo(0x7), false);
		assertEquals(Utility.isPowerOfTwo(0x8), true);
		assertEquals(Utility.isPowerOfTwo(0x9), false);
		assertEquals(Utility.isPowerOfTwo(0xA), false);
		assertEquals(Utility.isPowerOfTwo(0xB), false);
		assertEquals(Utility.isPowerOfTwo(0xC), false);
		assertEquals(Utility.isPowerOfTwo(0xD), false);
		assertEquals(Utility.isPowerOfTwo(0xE), false);
		assertEquals(Utility.isPowerOfTwo(0xF), false);
	}

	@Test
	public void testToBooleans() {
		assertBooleanArrayEquals(Utility.toBooleans(new int[]{}),        new boolean[]{});
		assertBooleanArrayEquals(Utility.toBooleans(new int[]{1}),       new boolean[]{true});
		assertBooleanArrayEquals(Utility.toBooleans(new int[]{2, 0}),    new boolean[]{true, false});
		assertBooleanArrayEquals(Utility.toBooleans(new int[]{0, 4, 0}), new boolean[]{false, true, false});
	}

	@Test
	public void testToStringAsSet() {
		assertEquals(Utility.toStringAsSet(new int[]{}),        "{}");
		assertEquals(Utility.toStringAsSet(new int[]{1}),       "{1}");
		assertEquals(Utility.toStringAsSet(new int[]{2, 3}),    "{2 3}");
		assertEquals(Utility.toStringAsSet(new int[]{3, 4, 5}), "{3 4 5}");
	}

	@Test
	public void testToInts() {
		assertArrayEquals(Utility.toInts(0, 0), new int[]{});
		assertArrayEquals(Utility.toInts(0, 1), new int[]{0});
		assertArrayEquals(Utility.toInts(1, 1), new int[]{1});
		assertArrayEquals(Utility.toInts(0, 2), new int[]{0, 0});
		assertArrayEquals(Utility.toInts(1, 2), new int[]{1, 0});
		assertArrayEquals(Utility.toInts(2, 2), new int[]{0, 1});
		assertArrayEquals(Utility.toInts(3, 2), new int[]{1, 1});
		assertArrayEquals(Utility.toInts(0, 3), new int[]{0, 0, 0});
		assertArrayEquals(Utility.toInts(1, 3), new int[]{1, 0, 0});
		assertArrayEquals(Utility.toInts(2, 3), new int[]{0, 1, 0});
		assertArrayEquals(Utility.toInts(3, 3), new int[]{1, 1, 0});
		assertArrayEquals(Utility.toInts(4, 3), new int[]{0, 0, 1});
		assertArrayEquals(Utility.toInts(5, 3), new int[]{1, 0, 1});
		assertArrayEquals(Utility.toInts(6, 3), new int[]{0, 1, 1});
		assertArrayEquals(Utility.toInts(7, 3), new int[]{1, 1, 1});
	}

	void assertBooleanArrayEquals(boolean[] booleans0, boolean[] booleans1) {
		assertEquals(booleans0.length, booleans1.length);
		for(int i = 0; i < booleans0.length; i++) {
			assertEquals(booleans0[i], booleans1[i]);
		}
	}

	@Test
	public void testLog2() {
		assertEquals(Utility.log2RoundedUp(   1),  0);
		assertEquals(Utility.log2RoundedUp(   2),  1);
		assertEquals(Utility.log2RoundedUp(   3),  2);
		assertEquals(Utility.log2RoundedUp(   4),  2);
		assertEquals(Utility.log2RoundedUp(   5),  3);
		assertEquals(Utility.log2RoundedUp(   6),  3);
		assertEquals(Utility.log2RoundedUp(   7),  3);
		assertEquals(Utility.log2RoundedUp(   8),  3);
		assertEquals(Utility.log2RoundedUp(   9),  4);
		assertEquals(Utility.log2RoundedUp(  10),  4);
		assertEquals(Utility.log2RoundedUp(  11),  4);
		assertEquals(Utility.log2RoundedUp(  12),  4);
		assertEquals(Utility.log2RoundedUp(  13),  4);
		assertEquals(Utility.log2RoundedUp(  14),  4);
		assertEquals(Utility.log2RoundedUp(  15),  4);
		assertEquals(Utility.log2RoundedUp(  16),  4);
		assertEquals(Utility.log2RoundedUp(  17),  5);

		assertEquals(Utility.log2RoundedUp(  31),  5);
		assertEquals(Utility.log2RoundedUp(  32),  5);
		assertEquals(Utility.log2RoundedUp(  33),  6);
		assertEquals(Utility.log2RoundedUp(  63),  6);
		assertEquals(Utility.log2RoundedUp(  64),  6);
		assertEquals(Utility.log2RoundedUp(  65),  7);
		assertEquals(Utility.log2RoundedUp( 127),  7);
		assertEquals(Utility.log2RoundedUp( 128),  7);
		assertEquals(Utility.log2RoundedUp( 129),  8);
		assertEquals(Utility.log2RoundedUp( 255),  8);
		assertEquals(Utility.log2RoundedUp( 256),  8);
		assertEquals(Utility.log2RoundedUp( 257),  9);
		assertEquals(Utility.log2RoundedUp( 511),  9);
		assertEquals(Utility.log2RoundedUp( 512),  9);
		assertEquals(Utility.log2RoundedUp( 513), 10);
		assertEquals(Utility.log2RoundedUp(1023), 10);
		assertEquals(Utility.log2RoundedUp(1024), 10);
		assertEquals(Utility.log2RoundedUp(1025), 11);
		assertEquals(Utility.log2RoundedUp(2047), 11);
		assertEquals(Utility.log2RoundedUp(2048), 11);
		assertEquals(Utility.log2RoundedUp(2049), 12);
	}

	@Test
	public void testRoundUpToAPowerOf2() {
		assertEquals(Utility.roundUpToPowerOfTwo(   1),  1);
		assertEquals(Utility.roundUpToPowerOfTwo(   2),  2);
		assertEquals(Utility.roundUpToPowerOfTwo(   3),  4);
		assertEquals(Utility.roundUpToPowerOfTwo(   4),  4);
		assertEquals(Utility.roundUpToPowerOfTwo(   5),  8);
		assertEquals(Utility.roundUpToPowerOfTwo(   6),  8);
		assertEquals(Utility.roundUpToPowerOfTwo(   7),  8);
		assertEquals(Utility.roundUpToPowerOfTwo(   8),  8);
		assertEquals(Utility.roundUpToPowerOfTwo(   9),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  10),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  11),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  12),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  13),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  14),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  15),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  16),  16);
		assertEquals(Utility.roundUpToPowerOfTwo(  17),  32);

		assertEquals(Utility.roundUpToPowerOfTwo(  31),  32);
		assertEquals(Utility.roundUpToPowerOfTwo(  32),  32);
		assertEquals(Utility.roundUpToPowerOfTwo(  33),  64);
		assertEquals(Utility.roundUpToPowerOfTwo(  63),  64);
		assertEquals(Utility.roundUpToPowerOfTwo(  64),  64);
		assertEquals(Utility.roundUpToPowerOfTwo(  65),  128);
		assertEquals(Utility.roundUpToPowerOfTwo( 127),  128);
		assertEquals(Utility.roundUpToPowerOfTwo( 128),  128);
		assertEquals(Utility.roundUpToPowerOfTwo( 129),  256);
		assertEquals(Utility.roundUpToPowerOfTwo( 255),  256);
		assertEquals(Utility.roundUpToPowerOfTwo( 256),  256);
		assertEquals(Utility.roundUpToPowerOfTwo( 257),  512);
		assertEquals(Utility.roundUpToPowerOfTwo( 511),  512);
		assertEquals(Utility.roundUpToPowerOfTwo( 512),  512);
		assertEquals(Utility.roundUpToPowerOfTwo( 513), 1024);
		assertEquals(Utility.roundUpToPowerOfTwo(1023), 1024);
		assertEquals(Utility.roundUpToPowerOfTwo(1024), 1024);
		assertEquals(Utility.roundUpToPowerOfTwo(1025), 2048);
		assertEquals(Utility.roundUpToPowerOfTwo(2047), 2048);
		assertEquals(Utility.roundUpToPowerOfTwo(2048), 2048);
		assertEquals(Utility.roundUpToPowerOfTwo(2049), 4096);
	}

	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_1  = new int[]{1};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_2  = new int[]{3, -1};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_4  = new int[]{15, -5, -9, 3};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_8  = new int[]{255, -85, -153, 51, -225, 75, 135, -45};
	private static final int[] EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_16 = new int[]{65535, -21845, -39321, 13107, -57825, 19275, 34695, -11565, -65025, 21675, 39015, -13005, 57375, -19125, -34425, 11475};

	@Test
	public void testFastTransforms() {
		assertArrayEquals(Utility.fastSylvesterTransform(Utility.enumeratePowersAscending(1)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_1);
		assertArrayEquals(Utility.fastSylvesterTransform(Utility.enumeratePowersAscending(2)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_2);
		assertArrayEquals(Utility.fastSylvesterTransform(Utility.enumeratePowersAscending(4)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_4);
		assertArrayEquals(Utility.fastSylvesterTransform(Utility.enumeratePowersAscending(8)),  EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_8);
		assertArrayEquals(Utility.fastSylvesterTransform(Utility.enumeratePowersAscending(16)), EXPECTED_SYLVESTER_TRANSFORM_OUTPUT_16);
	}

	@Test
	public void testPowers() {
		assertArrayEquals(Utility.enumeratePowersAscending(1), new int[]{1});
		assertArrayEquals(Utility.enumeratePowersAscending(2), new int[]{1, 2});
		assertArrayEquals(Utility.enumeratePowersAscending(4), new int[]{1, 2, 4, 8});
		assertArrayEquals(Utility.enumeratePowersAscending(8), new int[]{1, 2, 4, 8, 16, 32, 64, 128});
	}

	@Test
	public void testEnumerateForward0() {
		final int[] enumeration = Utility.enumerateAscending(0, 2);
		assertEquals(enumeration.length, 3);
		assertEquals(enumeration[0], 0);
		assertEquals(enumeration[1], 1);
		assertEquals(enumeration[2], 2);
	}

	@Test
	public void testEnumerateForward1() {
		final int[] enumeration = Utility.enumerateAscending(-3, 2);
		assertEquals(enumeration.length, 6);
		assertEquals(enumeration[0], -3);
		assertEquals(enumeration[1], -2);
		assertEquals(enumeration[2], -1);
		assertEquals(enumeration[3],  0);
		assertEquals(enumeration[4],  1);
		assertEquals(enumeration[5],  2);
	}

	@Test
	public void testEnumerateReverse0() {
		final int[] enumeration = Utility.enumerateDescending(0, 2);
		assertEquals(enumeration.length, 3);
		assertEquals(enumeration[0], 2);
		assertEquals(enumeration[1], 1);
		assertEquals(enumeration[2], 0);
	}

	@Test
	public void testEnumerateReverse1() {
		final int[] enumeration = Utility.enumerateDescending(-3, 2);
		assertEquals(enumeration.length, 6);
		assertEquals(enumeration[0],  2);
		assertEquals(enumeration[1],  1);
		assertEquals(enumeration[2],  0);
		assertEquals(enumeration[3], -1);
		assertEquals(enumeration[4], -2);
		assertEquals(enumeration[5], -3);
	}

	@Test
	public void testEnumerateRandomize() {
		final int numberOfTrials = 10000000;
		final int sizeOfTrial = 10;
		final int[][] counts = new int[sizeOfTrial][sizeOfTrial]; // defaults to zeros
		for(int trial = 0; trial < numberOfTrials; trial++) {
			final int[] randomArray = Utility.enumerateRandomly(sizeOfTrial);
			for(int i = 0; i < sizeOfTrial; i++ ) {
				counts[i][randomArray[i]]++;
			}
		}
		for(int i = 0; i < sizeOfTrial; i++) {
			for(int j = 0; j < sizeOfTrial; j++) {
				assertWithinOnePercent(counts[i][j], numberOfTrials / sizeOfTrial);
//				System.out.print(" " + counts[i][j]);
			}
//			System.out.println();
		}
	}

	// should be 2 * sigma or something
	private void assertWithinOnePercent(int i, int j) {
		assertTrue(1.01 * i > j);
		assertTrue(1.01 * j > i);
	}
}
