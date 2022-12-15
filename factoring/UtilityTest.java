package factoring;

import static org.junit.Assert.*;
import org.junit.Test;

public class UtilityTest {

	@Test
	public void testThrowIfFalseSuccess() {
		String message = null;
		try {
			Utility.throwIfFalse(true, "message");
		} catch (Exception e) {
			message = e.getMessage();
		}
		assertNull(message);
	}

	@Test
	public void testThrowIfFalse() {
		String message = null;
		try {
			Utility.throwIfFalse(false, "message");
		} catch (Exception e) {
			message = e.getMessage();
		}
		assertEquals(message, "message");
	}


	@Test
	public void testToBooleans() {
		class TestData {
			public final int       length;
			public final String    input;
			public final boolean[] output;

			public TestData(int length, String input, boolean[] output) {
				this.length = length;
				this.input  = input;
				this.output = output;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(0, "0",  Utility.booleans()),
			new TestData(0, "FF", Utility.booleans()),

			new TestData(1, "",   Utility.booleans(false)),
			new TestData(1, "0",  Utility.booleans(false)),
			new TestData(1, "1",  Utility.booleans(true)),
			new TestData(1, "FF", Utility.booleans(true)),

			new TestData(2, "",   Utility.booleans(false, false)),
			new TestData(2, "0",  Utility.booleans(false, false)),
			new TestData(2, "1",  Utility.booleans(false, true)),
			new TestData(2, "2",  Utility.booleans(true,  false)),
			new TestData(2, "3",  Utility.booleans(true,  true)),
			new TestData(2, "4",  Utility.booleans(false, false)),
			new TestData(2, "5",  Utility.booleans(false, true)),
			new TestData(2, "6",  Utility.booleans(true,  false)),
			new TestData(2, "FF", Utility.booleans(true,  true)),

			new TestData(3, "",   Utility.booleans(false, false, false)),
			new TestData(3, "0",  Utility.booleans(false, false, false)),
			new TestData(3, "1",  Utility.booleans(false, false, true)),
			new TestData(3, "2",  Utility.booleans(false, true,  false)),
			new TestData(3, "3",  Utility.booleans(false, true,  true)),
			new TestData(3, "4",  Utility.booleans(true,  false, false)),
			new TestData(3, "5",  Utility.booleans(true,  false, true)),
			new TestData(3, "6",  Utility.booleans(true,  true,  false)),
			new TestData(3, "7",  Utility.booleans(true,  true,  true)),
			new TestData(3, "F",  Utility.booleans(true,  true,  true)),
			new TestData(3, "FF", Utility.booleans(true,  true,  true)),

			new TestData(4, "",   Utility.booleans(false, false, false, false)),
			new TestData(4, "0",  Utility.booleans(false, false, false, false)),
			new TestData(4, "1",  Utility.booleans(false, false, false, true)),
			new TestData(4, "2",  Utility.booleans(false, false, true,  false)),
			new TestData(4, "3",  Utility.booleans(false, false, true,  true)),
			new TestData(4, "4",  Utility.booleans(false, true,  false, false)),
			new TestData(4, "5",  Utility.booleans(false, true,  false, true)),
			new TestData(4, "6",  Utility.booleans(false, true,  true,  false)),
			new TestData(4, "7",  Utility.booleans(false, true,  true,  true)),
			new TestData(4, "8",  Utility.booleans(true,  false, false, false)),
			new TestData(4, "9",  Utility.booleans(true,  false, false, true)),
			new TestData(4, "A",  Utility.booleans(true,  false, true,  false)),
			new TestData(4, "B",  Utility.booleans(true,  false, true,  true)),
			new TestData(4, "C",  Utility.booleans(true,  true,  false, false)),
			new TestData(4, "D",  Utility.booleans(true,  true,  false, true)),
			new TestData(4, "E",  Utility.booleans(true,  true,  true,  false)),
			new TestData(4, "F",  Utility.booleans(true,  true,  true,  true)),
			new TestData(4, "FF", Utility.booleans(true,  true,  true,  true)),

			new TestData(5, "555", Utility.booleans(true,  false, true,  false, true)),
			new TestData(5, "AAA", Utility.booleans(false, true,  false, true,  false)),
			new TestData(6, "555", Utility.booleans(false, true,  false, true,  false, true)),
			new TestData(6, "AAA", Utility.booleans(true,  false, true,  false, true,  false)),
			new TestData(7, "555", Utility.booleans(true,  false, true,  false, true,  false, true)),
			new TestData(7, "AAA", Utility.booleans(false, true,  false, true,  false, true,  false)),
			new TestData(8, "555", Utility.booleans(false, true,  false, true,  false, true,  false, true)),
			new TestData(8, "AAA", Utility.booleans(true,  false, true,  false, true,  false, true,  false)),
			new TestData(9, "555", Utility.booleans(true,  false, true,  false, true,  false, true,  false, true)),
			new TestData(9, "AAA", Utility.booleans(false, true,  false, true,  false, true,  false, true,  false)),
		};

		for(TestData test : tests) {
			boolean[] results = Utility.toBooleans(test.length, test.input);
			assertTrue(Utility.booleanArrayEquals(results, test.output));
		}
	}

	@Test
	public void testIsHexadecimal() {
		assertTrue( Utility.isHexadecimal(""));
		assertTrue( Utility.isHexadecimal("0123456789abcdefABCDEF"));
		assertFalse(Utility.isHexadecimal("#123456789abcdefABCDEF"));
		assertFalse(Utility.isHexadecimal("0123456789ab#defABCDEF"));
		assertFalse(Utility.isHexadecimal("0123456789abcdefABCDE#"));
		assertFalse(Utility.isHexadecimal("/"));
		assertFalse(Utility.isHexadecimal(":"));
		assertFalse(Utility.isHexadecimal("`"));
		assertFalse(Utility.isHexadecimal("g"));
		assertFalse(Utility.isHexadecimal("@"));
		assertFalse(Utility.isHexadecimal("G"));
	}

	@Test
	public void testLessThanOrEqual() {
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false), Utility.booleans(false)), true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false), Utility.booleans(true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true),  Utility.booleans(false)), false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true),  Utility.booleans(true)),  true);

		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, false), Utility.booleans(false, false)), true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, false), Utility.booleans(false, true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, false), Utility.booleans(true,  false)), true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, false), Utility.booleans(true,  true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, true),  Utility.booleans(false, false)), false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, true),  Utility.booleans(false, true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, true),  Utility.booleans(true,  false)), true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(false, true),  Utility.booleans(true,  true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  false), Utility.booleans(false, false)), false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  false), Utility.booleans(false, true)),  false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  false), Utility.booleans(true,  false)), true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  false), Utility.booleans(true,  true)),  true);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  true),  Utility.booleans(false, false)), false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  true),  Utility.booleans(false, true)),  false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  true),  Utility.booleans(true,  false)), false);
		assertEquals(Utility.lessThanOrEqualTo(Utility.booleans(true,  true),  Utility.booleans(true,  true)),  true);
	}

	@Test
	public void testIsEven() {
		assertEquals(Utility.isEven(-2), true);
		assertEquals(Utility.isEven(-1), false);
		assertEquals(Utility.isEven( 0), true);
		assertEquals(Utility.isEven( 1), false);
		assertEquals(Utility.isEven( 2), true);
		assertEquals(Utility.isEven( 3), false);
		assertEquals(Utility.isEven( 4), true);
	}

	@Test
	public void testLeadingBitInMostSignificantHalf() {
		assertEquals(Utility.leadingBitInMostSignificantHalf(2, "0"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(2, "1"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(2, "2"), true);

		assertEquals(Utility.leadingBitInMostSignificantHalf(4, "0"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(4, "1"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(4, "2"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(4, "4"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(4, "8"), true);

		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "00"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "01"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "02"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "04"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "08"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "10"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(6, "20"), true);

		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "00"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "01"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "02"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "04"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "08"), false);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "10"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "20"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "40"), true);
		assertEquals(Utility.leadingBitInMostSignificantHalf(8, "80"), true);
	}

	@Test
	public void testToString() {
		assertEquals(Utility.toString(new boolean[]{}), "[]");
		assertEquals(Utility.toString(new boolean[]{false}), "[false]");
		assertEquals(Utility.toString(new boolean[]{true}),  "[true]");
		assertEquals(Utility.toString(new boolean[]{false, false}), "[false, false]");
		assertEquals(Utility.toString(new boolean[]{false, true}),  "[false, true]");
		assertEquals(Utility.toString(new boolean[]{true,  false}), "[true, false]");
		assertEquals(Utility.toString(new boolean[]{true,  true}),  "[true, true]");
	}
}
