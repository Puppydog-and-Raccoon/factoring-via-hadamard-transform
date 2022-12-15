package factoring;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProblemTest {

	@Test
	public void testSuccess() {
		class TestCase {
			public final int       sizeOfProductInBits;
			public final String    productInHexadecimal;
			public final int       sizeOfFactorsInBits;
			public final boolean[] product;

			TestCase(int sizeOfProductInBits, String productInHexadecimal, int sizeOfFactorsInBits, boolean[] product) {
				this.sizeOfProductInBits  = sizeOfProductInBits;
				this.productInHexadecimal = productInHexadecimal;
				this.sizeOfFactorsInBits  = sizeOfFactorsInBits;
				this.product              = product;
			}
		}

		TestCase[] tests = new TestCase[] {
			new TestCase( 2,    "2", 1, new boolean[]{false, true}),
			new TestCase( 2, "0002", 1, new boolean[]{false, true}),

			new TestCase( 4,    "4", 2, new boolean[]{false, false, true,  false}),
			new TestCase( 4,    "f", 2, new boolean[]{true,  true,  true,  true}),
			new TestCase( 4, "0004", 2, new boolean[]{false, false, true,  false}),
			new TestCase( 4, "000f", 2, new boolean[]{true,  true,  true,  true}),

			new TestCase( 6,    "8", 3, new boolean[]{false, false, false, true,  false, false}),
			new TestCase( 6,   "3F", 3, new boolean[]{true,  true,  true,  true,  true,  true}),
			new TestCase( 6, "0008", 3, new boolean[]{false, false, false, true,  false, false}),
			new TestCase( 6, "003F", 3, new boolean[]{true,  true,  true,  true,  true,  true}),

			new TestCase( 8,   "10", 4, new boolean[]{false, false, false, false, true,  false, false, false}),
			new TestCase( 8,   "5a", 4, new boolean[]{false, true,  false, true,  true,  false, true,  false}),
			new TestCase( 8,   "a5", 4, new boolean[]{true,  false, true,  false, false, true,  false, true}),
			new TestCase( 8,   "FF", 4, new boolean[]{true,  true,  true,  true,  true,  true,  true,  true}),
			new TestCase( 8, "0010", 4, new boolean[]{false, false, false, false, true,  false, false, false}),
			new TestCase( 8, "005a", 4, new boolean[]{false, true,  false, true,  true,  false, true,  false}),
			new TestCase( 8, "00a5", 4, new boolean[]{true,  false, true,  false, false, true,  false, true}),
			new TestCase( 8, "00FF", 4, new boolean[]{true,  true,  true,  true,  true,  true,  true,  true}),

			new TestCase(16,  "100", 8, new boolean[]{false, false, false, false, false, false, false, false, true,  false, false, false, false, false, false, false}),
			new TestCase(16, "137f", 8, new boolean[]{true,  true,  true,  true,  true,  true,  true,  false, true,  true,  false, false, true,  false, false, false}),
			new TestCase(16, "f731", 8, new boolean[]{true,  false, false, false, true,  true,  false, false, true,  true,  true,  false, true,  true,  true,  true}),
			new TestCase(16, "ffff", 8, new boolean[]{true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true}),
		};

		for(TestCase test : tests) {
			Problem fp = new Problem(test.sizeOfProductInBits, test.productInHexadecimal);
			assertEquals(fp.sizeOfFactorsInBits, test.sizeOfFactorsInBits);
			assertTrue(Utility.booleanArrayEquals(fp.product, test.product));
		}
	}

	@Test
	public void testErrors() {
		class TestCase {
			public final int    sizeOfProductInBits;
			public final String productInHexadecimal;
			public final String errorMessage;

			TestCase(int sizeOfProductInBits, String productInHexadecimal, String errorMessage) {
				this.sizeOfProductInBits  = sizeOfProductInBits;
				this.productInHexadecimal = productInHexadecimal;
				this.errorMessage         = errorMessage;
			}
		}

		TestCase[] tests = new TestCase[] {
			new TestCase(0,   "0", Problem.PRODUCT_LENGTH_IS_NOT_POSITIVE),

			new TestCase(1,   "0", Problem.PRODUCT_LENGTH_IS_ODD),
			new TestCase(3,   "0", Problem.PRODUCT_LENGTH_IS_ODD),
			new TestCase(5,   "0", Problem.PRODUCT_LENGTH_IS_ODD),
			new TestCase(7,   "0", Problem.PRODUCT_LENGTH_IS_ODD),

			new TestCase(2,   "g", Problem.PRODUCT_IS_NOT_HEXADECIMAL),
			new TestCase(2,   "?", Problem.PRODUCT_IS_NOT_HEXADECIMAL),
			new TestCase(2,   " ", Problem.PRODUCT_IS_NOT_HEXADECIMAL),

			new TestCase(2,   "4", Problem.LEADING_BIT_GETS_DROPPED),
			new TestCase(4,  "10", Problem.LEADING_BIT_GETS_DROPPED),
			new TestCase(6,  "40", Problem.LEADING_BIT_GETS_DROPPED),
			new TestCase(8, "100", Problem.LEADING_BIT_GETS_DROPPED),

			new TestCase(2,    "", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(2,   "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(2,   "1", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(4,   "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(4,   "3", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(6,   "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(6,   "7", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(8,   "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new TestCase(8,   "F", Problem.LEADING_BIT_IS_INSIGNIFICANT),
		};

		for(TestCase test : tests) {
			Problem fp = new Problem(test.sizeOfProductInBits, test.productInHexadecimal);
			assertTrue(fp.errors.contains(test.errorMessage));
		}
	}
}
