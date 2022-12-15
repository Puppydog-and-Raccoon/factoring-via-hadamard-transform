package factoring;

import static org.junit.Assert.*;
import org.junit.Test;

public class FactoringTest {
	interface FactoringTestCase {
		public void run();
	}

	class FactoringTestCaseSolution implements FactoringTestCase {
		public final consistency.Consistency consistencySolver;
		public final int                     size;
		public final String                  product;
		public final String                  smallerFactor;
		public final String                  largerFactor;

		public FactoringTestCaseSolution(consistency.Consistency consistencySolver, int size, String product, String smallerFactor, String largerFactor) {
			this.consistencySolver = consistencySolver;
			this.size              = size;
			this.product           = product;
			this.smallerFactor     = smallerFactor;
			this.largerFactor      = largerFactor;
		}

		public void run() {
			Problem problem = new Problem(size, product);
			assertTrue(problem.errors.isEmpty());
			Factoring factoring = new Factoring(consistencySolver);
			Solution solution = factoring.solve(problem);

			assertTrue(solution != null);
			assertTrue(arrayEqualsString(solution.smallerFactor, smallerFactor));
			assertTrue(arrayEqualsString(solution.largerFactor, largerFactor));
		}
	}

	class FactoringTestCaseNull implements FactoringTestCase {
		public final consistency.Consistency consistencySolver;
		public final int                     size;
		public final String                  product;

		public FactoringTestCaseNull(consistency.Consistency consistencySolver, int size, String product) {
			this.consistencySolver = consistencySolver;
			this.size              = size;
			this.product           = product;
		}

		public void run() {
			Problem problem = new Problem(size, product);
			assertTrue(problem.errors.isEmpty());
			Factoring factoring = new Factoring(consistencySolver);
			Solution solution = factoring.solve(problem);
			assertNull(solution);
		}
	}

	class FactoringTestCaseException implements FactoringTestCase {
		public final consistency.Consistency consistencySolver;
		public final int                     size;
		public final String                  product;
		public final String                  exceptionMessage;

		public FactoringTestCaseException(consistency.Consistency consistencySolver, int size, String product, String exceptionMessage) {
			this.consistencySolver = consistencySolver;
			this.size              = size;
			this.product           = product;
			this.exceptionMessage  = exceptionMessage;
		}

		public void run() {
			Exception caughtException = null;
			try {
				Problem problem = new Problem(size, product);
				Factoring factoring = new Factoring(consistencySolver);
				Solution solution = factoring.solve(problem);
			} catch(Exception e) {
				caughtException = e;
			}
			assertNotNull(caughtException);
			assertTrue(exceptionMessage, caughtException.getMessage().contains(exceptionMessage));
		}
	}

	public boolean arrayEqualsString(boolean[] a, String s) {
		boolean[] b = Utility.toBooleans(s);
		int i = 0;
		for(; i < Math.min(a.length, b.length); i++) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		for(; i < a.length; i++) {
			if(a[i] != false) {
				return false;
			}
		}
		for(; i < b.length; i++) {
			if(b[i] != false) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void test() {
		consistency.NaiveConsistency naiveConsistencySolver = new consistency.NaiveConsistency();
		consistency.SlightlyLessNaiveConsistencyForFactoring lessNaiveConsistencySolver = new consistency.SlightlyLessNaiveConsistencyForFactoring();
		consistency.HadamardConsistency hadamardConsistencySolver = new consistency.HadamardConsistency();

		final FactoringTestCase[] testCases = new FactoringTestCase[] {
			new FactoringTestCaseException(naiveConsistencySolver, 0, "0", Problem.PRODUCT_LENGTH_IS_NOT_POSITIVE),

			new FactoringTestCaseException(naiveConsistencySolver, 1, "0", Problem.PRODUCT_LENGTH_IS_ODD),
	//		new FactoringTestCaseException(naiveConsistencySolver, 1, "1", Problem.PRODUCT_LENGTH_IS_ODD),

//			new FactoringTestCaseException(naiveConsistencySolver, 2, "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
//			new FactoringTestCaseException(naiveConsistencySolver, 2, "1", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new FactoringTestCaseNull(naiveConsistencySolver, 2, "2"),
			new FactoringTestCaseNull(naiveConsistencySolver, 2, "3"),

			new FactoringTestCaseException(naiveConsistencySolver, 3, "0", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "1", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "2", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "3", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "4", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "5", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "6", Problem.PRODUCT_LENGTH_IS_ODD),
			new FactoringTestCaseException(naiveConsistencySolver, 3, "7", Problem.PRODUCT_LENGTH_IS_ODD),

//			new FactoringTestCaseException(lessNaiveConsistencySolver, 2, "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
//			new FactoringTestCaseException(lessNaiveConsistencySolver, 2, "1", Problem.LEADING_BIT_IS_INSIGNIFICANT),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 2, "2"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 2, "3"),

			new FactoringTestCaseSolution(lessNaiveConsistencySolver, 4, "4", "2", "2"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "5"),
			new FactoringTestCaseSolution(lessNaiveConsistencySolver, 4, "6", "2", "3"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "7"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "8"),
			new FactoringTestCaseSolution(lessNaiveConsistencySolver, 4, "9", "3", "3"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "a"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "b"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "c"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "d"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "e"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 4, "f"),

			new FactoringTestCaseSolution(hadamardConsistencySolver, 2, "1", "1", "1"),
	//		new FactoringTestCaseException(hadamardConsistencySolver, 2, "0", Problem.LEADING_BIT_IS_INSIGNIFICANT),
	//		new FactoringTestCaseException(hadamardConsistencySolver, 2, "1", Problem.LEADING_BIT_IS_INSIGNIFICANT),
	//		new FactoringTestCaseNull(hadamardConsistencySolver, 2, "2"),
	//		new FactoringTestCaseNull(hadamardConsistencySolver, 2, "3"),

//			new FactoringTestCaseSolution(hadamardConsistencySolver, 4, "4", "2", "2"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "5"),
//			new FactoringTestCaseSolution(hadamardConsistencySolver, 4, "6", "2", "3"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "7"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "8"),
//			new FactoringTestCaseSolution(hadamardConsistencySolver, 4, "9", "3", "3"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "a"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "b"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "c"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "d"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "e"),
//			new FactoringTestCaseNull(hadamardConsistencySolver, 4, "f"),

	//		new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "20"), // 4 * 8
//			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "21"), // 3 * b,
//			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "22"), // 2 * 17
	//		new FactoringTestCaseUnique(lessNaiveConsistencySolver, 6, "23", "5", "7"), // successful
	//		new FactoringTestCaseUnique(lessNaiveConsistencySolver, 6, "24", "6", "6"), // successful
	//		new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "25"),             // successful
/*			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "26"), // 2 * 19
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "27"), // 3 * 13
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "28"), // 5 * 8
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "29"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2a", "6", "7"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2b"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2c"), // 4 * 0xb,
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2d"), // 5 * 0x9,
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2e"), // 2 * 23
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "2f"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "30"), // 6 * 8 // multiple
*/	//		new FactoringTestCaseUnique(lessNaiveConsistencySolver, 6, "31", "7", "7"), // successful
	//		new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "32"), // successful // 5 * 0xa
/*			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "33"), // 3 * 17
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "34"), // 4 * 13
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "35"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "36"), // 2 * 0x17
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "37"), // 5 * 0xa
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "38"), // 7 * 0x8
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "39"), // 3 * 0x19
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3a"), // 2 * 0x29
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3b"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3c"), // 60 = 6 * 10 = 
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3d"),
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3e"), // 62 = 2 * 31
			new FactoringTestCaseNull(lessNaiveConsistencySolver, 6, "3f"), // 63 = 7 * 9
*/		};

		for(FactoringTestCase testCase : testCases) {
			testCase.run();
		}
	}
}
