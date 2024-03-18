package consistency;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UtilityTest {
	@Test
	public void testInsist0() {
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
	public void testInsist1() {
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
	public void testToString() {
		assertEquals(Utility.toString(new int[]{}),        "[]");
		assertEquals(Utility.toString(new int[]{1}),       "[1]");
		assertEquals(Utility.toString(new int[]{2, 3}),    "[2, 3]");
		assertEquals(Utility.toString(new int[]{3, 4, 5}), "[3, 4, 5]");
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
}
