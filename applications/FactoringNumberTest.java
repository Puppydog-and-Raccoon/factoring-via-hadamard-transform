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
}
