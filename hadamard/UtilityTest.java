package hadamard;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilityTest {

	@Test
	public void testIsAPowerOfTwo() {
		assertEquals(Utility.isAPowerOfTwo(-5), false);
		assertEquals(Utility.isAPowerOfTwo(-4), false);
		assertEquals(Utility.isAPowerOfTwo(-3), false);
		assertEquals(Utility.isAPowerOfTwo(-2), false);
		assertEquals(Utility.isAPowerOfTwo(-1), false);
		assertEquals(Utility.isAPowerOfTwo( 0), false);
		assertEquals(Utility.isAPowerOfTwo( 1), true);
		assertEquals(Utility.isAPowerOfTwo( 2), true);
		assertEquals(Utility.isAPowerOfTwo( 3), false);
		assertEquals(Utility.isAPowerOfTwo( 4), true);
		assertEquals(Utility.isAPowerOfTwo( 5), false);
		assertEquals(Utility.isAPowerOfTwo( 6), false);
		assertEquals(Utility.isAPowerOfTwo( 7), false);
		assertEquals(Utility.isAPowerOfTwo( 8), true);
		assertEquals(Utility.isAPowerOfTwo( 9), false);
		assertEquals(Utility.isAPowerOfTwo(10), false);
		assertEquals(Utility.isAPowerOfTwo(11), false);
		assertEquals(Utility.isAPowerOfTwo(12), false);
		assertEquals(Utility.isAPowerOfTwo(13), false);
		assertEquals(Utility.isAPowerOfTwo(14), false);
		assertEquals(Utility.isAPowerOfTwo(15), false);
		assertEquals(Utility.isAPowerOfTwo(16), true);
		assertEquals(Utility.isAPowerOfTwo(17), false);
		assertEquals(Utility.isAPowerOfTwo(18), false);
		assertEquals(Utility.isAPowerOfTwo(19), false);
		assertEquals(Utility.isAPowerOfTwo(20), false);
		assertEquals(Utility.isAPowerOfTwo(21), false);
		assertEquals(Utility.isAPowerOfTwo(22), false);
		assertEquals(Utility.isAPowerOfTwo(23), false);
		assertEquals(Utility.isAPowerOfTwo(24), false);
		assertEquals(Utility.isAPowerOfTwo(25), false);
		assertEquals(Utility.isAPowerOfTwo(26), false);
		assertEquals(Utility.isAPowerOfTwo(27), false);
		assertEquals(Utility.isAPowerOfTwo(28), false);
		assertEquals(Utility.isAPowerOfTwo(29), false);
		assertEquals(Utility.isAPowerOfTwo(30), false);
		assertEquals(Utility.isAPowerOfTwo(31), false);
		assertEquals(Utility.isAPowerOfTwo(32), true);
		assertEquals(Utility.isAPowerOfTwo(33), false);
	}

	@Test
	public void testLog2() {
		assertEquals(Utility.log2(   1),  0);
		assertEquals(Utility.log2(   2),  1);
		assertEquals(Utility.log2(   3),  2);
		assertEquals(Utility.log2(   4),  2);
		assertEquals(Utility.log2(   5),  3);
		assertEquals(Utility.log2(   6),  3);
		assertEquals(Utility.log2(   7),  3);
		assertEquals(Utility.log2(   8),  3);
		assertEquals(Utility.log2(   9),  4);
		assertEquals(Utility.log2(  10),  4);
		assertEquals(Utility.log2(  11),  4);
		assertEquals(Utility.log2(  12),  4);
		assertEquals(Utility.log2(  13),  4);
		assertEquals(Utility.log2(  14),  4);
		assertEquals(Utility.log2(  15),  4);
		assertEquals(Utility.log2(  16),  4);
		assertEquals(Utility.log2(  17),  5);

		assertEquals(Utility.log2(  31),  5);
		assertEquals(Utility.log2(  32),  5);
		assertEquals(Utility.log2(  33),  6);
		assertEquals(Utility.log2(  63),  6);
		assertEquals(Utility.log2(  64),  6);
		assertEquals(Utility.log2(  65),  7);
		assertEquals(Utility.log2( 127),  7);
		assertEquals(Utility.log2( 128),  7);
		assertEquals(Utility.log2( 129),  8);
		assertEquals(Utility.log2( 255),  8);
		assertEquals(Utility.log2( 256),  8);
		assertEquals(Utility.log2( 257),  9);
		assertEquals(Utility.log2( 511),  9);
		assertEquals(Utility.log2( 512),  9);
		assertEquals(Utility.log2( 513), 10);
		assertEquals(Utility.log2(1023), 10);
		assertEquals(Utility.log2(1024), 10);
		assertEquals(Utility.log2(1025), 11);
		assertEquals(Utility.log2(2047), 11);
		assertEquals(Utility.log2(2048), 11);
		assertEquals(Utility.log2(2049), 12);
	}

	@Test
	public void testSequency() {
		assertEquals(Utility.sequency(new int[]{1,  1,  1,  1,  1,  1,  1,  1}), 0);
		assertEquals(Utility.sequency(new int[]{1,  1,  1,  1, -1, -1, -1, -1}), 1);
		assertEquals(Utility.sequency(new int[]{1,  1, -1, -1, -1, -1,  1,  1}), 2);
		assertEquals(Utility.sequency(new int[]{1,  1, -1, -1,  1,  1, -1, -1}), 3);
		assertEquals(Utility.sequency(new int[]{1, -1, -1,  1,  1, -1, -1,  1}), 4);
		assertEquals(Utility.sequency(new int[]{1, -1, -1,  1, -1,  1,  1, -1}), 5);
		assertEquals(Utility.sequency(new int[]{1, -1,  1, -1, -1,  1, -1,  1}), 6);
		assertEquals(Utility.sequency(new int[]{1, -1,  1, -1,  1, -1,  1, -1}), 7);
	}

	@Test
	public void testPowers() {
		assertArrayEquals(Utility.powers(1), new int[]{1});
		assertArrayEquals(Utility.powers(2), new int[]{1, 2});
		assertArrayEquals(Utility.powers(4), new int[]{1, 2, 4, 8});
		assertArrayEquals(Utility.powers(8), new int[]{1, 2, 4, 8, 16, 32, 64, 128});
	}
}
