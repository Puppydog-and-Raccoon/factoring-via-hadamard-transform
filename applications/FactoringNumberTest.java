package applications;

import static org.junit.Assert.*;

import org.junit.Test;

public class FactoringNumberTest {
	@Test
	public void testIsEqual() {
		testIsEqual("",   "",   true);

		testIsEqual("0",   "", true);
		testIsEqual( "",  "0", true);
		testIsEqual("1",   "", false);
		testIsEqual( "",  "1", false);

		testIsEqual("0",  "0", true);
		testIsEqual("1",  "1", true);
		testIsEqual("0",  "1", false);
		testIsEqual("1",  "0", false);

		testIsEqual("00",  "0", true);
		testIsEqual( "0", "00", true);
		testIsEqual("10",  "0", false);
		testIsEqual( "0", "10", false);

		testIsEqual("01",  "1", true);
		testIsEqual( "1", "01", true);
		testIsEqual("11",  "1", false);
		testIsEqual( "1", "11", false);
	}

	private void testIsEqual(final String a, final String b, final boolean result) {
		assertEquals(FactoringNumber.isEqual(new FactoringNumber(a), new FactoringNumber(b)), result);
	}

	@Test
	public void testCompare() {
		testCompare("",   "",   0);

		testCompare("0",   "",  0);
		testCompare( "",  "0",  0);
		testCompare("1",   "",  1);
		testCompare( "",  "1", -1);

		testCompare("0",  "0",  0);
		testCompare("1",  "1",  0);
		testCompare("0",  "1", -1);
		testCompare("1",  "0",  1);

		testCompare("00",  "0",  0);
		testCompare( "0", "00",  0);
		testCompare("10",  "0",  1);
		testCompare( "0", "10", -1);

		testCompare("01",  "1",  0);
		testCompare( "1", "01",  0);
		testCompare("11",  "1",  1);
		testCompare( "1", "11", -1);
	}

	private void testCompare(final String a, final String b, final int result) {
		assertEquals(FactoringNumber.compare(new FactoringNumber(a), new FactoringNumber(b)), result);
	}

	@Test
	public void testMultiply() {
		testMultiply("",   "",   "");

		testMultiply("0",  "",  "0");
		testMultiply("1",  "",  "0");
		testMultiply("",  "0",  "0");
		testMultiply("",  "1",  "0");

		testMultiply("0", "0", "00");
		testMultiply("0", "1", "00");
		testMultiply("1", "0", "00");
		testMultiply("1", "1", "01");

		testMultiply("100", "100", "010000");
		testMultiply("111", "111", "110001");
	}

	private void testMultiply(String number0AsString, String number1AsString, String productAsString) {
		final FactoringNumber number0 = new FactoringNumber(number0AsString);
		final FactoringNumber number1 = new FactoringNumber(number1AsString);
		final FactoringNumber product = new FactoringNumber(productAsString);
		final FactoringNumber testProduct = FactoringNumber.multiply(number0, number1);
		assertTrue(FactoringNumber.isEqual(testProduct, product));
	}

	@Test
	public void testConstructor() {
		testConstructor(0, 0);
		testConstructor(0, 1);
		testConstructor(1, 1);
		testConstructor(0, 2);
		testConstructor(1, 2);
		testConstructor(2, 2);
		testConstructor(3, 2);
		testConstructor(0, 3);
		testConstructor(1, 3);
		testConstructor(2, 3);
		testConstructor(3, 3);
		testConstructor(4, 3);
		testConstructor(5, 3);
		testConstructor(6, 3);
		testConstructor(7, 3);
	}

	private void testConstructor(final int number, final int numberOfBits) {
		final FactoringNumber factoringNumber = new FactoringNumber(number, numberOfBits);
		assertEquals(factoringNumber.bits.length, numberOfBits);
		for(int i = 0; i < numberOfBits; i++) {
			assertEquals(" " + i, factoringNumber.bits[i], ((number >> i) & 1) != 0);
		}
	}

	@Test
	public void testFactors() {
		testFactors(1, 1, new FactoringNumber[][]{
			new FactoringNumber[]{new FactoringNumber(1, 1), new FactoringNumber(1, 1)},
		});
		testFactors(2, 2, new FactoringNumber[][]{
			new FactoringNumber[]{new FactoringNumber(1, 2), new FactoringNumber(2, 2)},
		});
		testFactors(3, 2, new FactoringNumber[][]{
			new FactoringNumber[]{new FactoringNumber(1, 2), new FactoringNumber(3, 2)},
		});
		testFactors(4, 3, new FactoringNumber[][]{
			new FactoringNumber[]{new FactoringNumber(1, 3), new FactoringNumber(4, 3)},
			new FactoringNumber[]{new FactoringNumber(2, 3), new FactoringNumber(2, 3)},
		});
		testFactors(4, 2, new FactoringNumber[][]{
			new FactoringNumber[]{new FactoringNumber(2, 2), new FactoringNumber(2, 2)},
		});
	}

	private void testFactors(
		final int product,
		final int numberOfBitsPerFactor,
		final FactoringNumber[][] testFactors
	) {
		final FactoringSolution[] algorithmFactors = FactoringNumber.pairsOfFactors(product, numberOfBitsPerFactor);
		assertEquals(algorithmFactors.length, testFactors.length);
		for(int i = 0; i < algorithmFactors.length; i++) {
			assertTrue(FactoringNumber.isEqual(algorithmFactors[i].factor0, testFactors[i][0]));
			assertTrue(FactoringNumber.isEqual(algorithmFactors[i].factor1, testFactors[i][1]));
		}
	}
}
