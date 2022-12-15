package factoring;

import static org.junit.Assert.*;
import org.junit.Test;

public class MeaningTest {

	@Test
	public void testConstructorAdderMultiplier() {
		class TestData {
			public final boolean aIn;
			public final boolean bIn;
			public final boolean sumIn;
			public final boolean carryIn;
			public final boolean sumOut;
			public final boolean carryOut;

			public TestData(boolean aIn, boolean bIn, boolean sumIn, boolean carryIn, boolean sumOut, boolean carryOut) {
				this.aIn      = aIn;
				this.bIn      = bIn;
				this.sumIn    = sumIn;
				this.carryIn  = carryIn;
				this.sumOut   = sumOut;
				this.carryOut = carryOut;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(false, false, false, false, false, false),
			new TestData(false, false, false, true,  true,  false),
			new TestData(false, false, true,  false, true,  false),
			new TestData(false, false, true,  true,  false, true),
			new TestData(false, true,  false, false, false, false),
			new TestData(false, true,  false, true,  true,  false),
			new TestData(false, true,  true,  false, true,  false),
			new TestData(false, true,  true,  true,  false, true),

			new TestData(true,  false, false, false, false, false),
			new TestData(true,  false, false, true,  true,  false),
			new TestData(true,  false, true,  false, true,  false),
			new TestData(true,  false, true,  true,  false, true),
			new TestData(true,  true,  false, false, true,  false),
			new TestData(true,  true,  false, true,  false, true),
			new TestData(true,  true,  true,  false, false, true),
			new TestData(true,  true,  true,  true,  true,  true),
		};

		for(TestData test : tests) {
			Meaning d = new Meaning(0, 0, test.aIn, test.bIn, test.sumIn, test.carryIn, 1);
			assertEquals(d.aIn,      test.aIn);
			assertEquals(d.bIn,      test.bIn);
			assertEquals(d.sumIn,    test.sumIn);
			assertEquals(d.carryIn,  test.carryIn);
			assertEquals(d.sumOut,   test.sumOut);
			assertEquals(d.carryOut, test.carryOut);
		}
	}

	@Test
	public void testIsConsistent() {
		// this is tested implicitly by the factoring tests
		// fail("isConsistent");
	}

	@Test
	public void testImplies() {
		assertEquals(Meaning.implies(false, false), true);
		assertEquals(Meaning.implies(false, true),  true);
		assertEquals(Meaning.implies(true,  false), false);
		assertEquals(Meaning.implies(true,  true),  true);
	}

	@Test
	public void testUnaryBoxFunctions() {
		class TestData {
			public final int     aId;
			public final int     bId;
			public final int     N;
			public final boolean right;
			public final boolean left;
			public final boolean top;
			public final boolean bottom;

			public TestData(int aId, int bId, int N, boolean right, boolean left, boolean top, boolean bottom) {
				this.aId      = aId;
				this.bId      = bId;
				this.N        = N;
				this.right    = right;
				this.left     = left;
				this.top      = top;
				this.bottom   = bottom;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(0, 0, 1, true, true, true, true),

			new TestData(0, 0, 2, true,  false, true,  false),
			new TestData(0, 1, 2, false, true,  true,  false),
			new TestData(1, 0, 2, true,  false, false, true),
			new TestData(1, 1, 2, false, true,  false, true),

			new TestData(0, 0, 4, true,  false, true,  false),
			new TestData(0, 1, 4, false, false, true,  false),
			new TestData(0, 2, 4, false, false, true,  false),
			new TestData(0, 3, 4, false, true,  true,  false),
			new TestData(1, 0, 4, true,  false, false, false),
			new TestData(1, 1, 4, false, false, false, false),
			new TestData(1, 2, 4, false, false, false, false),
			new TestData(1, 3, 4, false, true,  false, false),
			new TestData(2, 0, 4, true,  false, false, false),
			new TestData(2, 1, 4, false, false, false, false),
			new TestData(2, 2, 4, false, false, false, false),
			new TestData(2, 3, 4, false, true,  false, false),
			new TestData(3, 0, 4, true,  false, false, true),
			new TestData(3, 1, 4, false, false, false, true),
			new TestData(3, 2, 4, false, false, false, true),
			new TestData(3, 3, 4, false, true,  false, true),
		};

		for(TestData test : tests) {
			Meaning d = new Meaning(test.aId, test.bId, false, false, false, false, test.N);
			assertEquals(Meaning.boxIsInRightColumn(d), test.right);
			assertEquals(Meaning.boxIsInLeftColumn(d),  test.left);
			assertEquals(Meaning.boxIsInTopRow(d),      test.top);
			assertEquals(Meaning.boxIsInBottomRow(d),   test.bottom);
		}
	}

	@Test
	public void testBinaryBoxFunctions() {
		class TestData {
			public final int     aId;
			public final int     bId;
			public final boolean leftOf;
			public final boolean above;
			public final boolean diagonal;
			public final boolean same;

			public TestData(int aId, int bId, boolean leftOf, boolean above, boolean diagonal, boolean same) {
				this.aId      = aId;
				this.bId      = bId;
				this.leftOf   = leftOf;
				this.above    = above;
				this.diagonal = diagonal;
				this.same     = same;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(0, 0, false, false, false, false),
			new TestData(0, 1, false, false, false, false),
			new TestData(0, 2, false, false, false, false),
			new TestData(1, 0, true,  false, false, false),
			new TestData(1, 1, false, false, false, true),
			new TestData(1, 2, false, false, false, false),
			new TestData(2, 0, false, false, true,  false),
			new TestData(2, 1, false, true,  false, false),
			new TestData(2, 2, false, false, false, false),
		};

		for(TestData test : tests) {
			Meaning center = new Meaning(1,        1,        false, false, false, false, 3);
			Meaning spoke  = new Meaning(test.aId, test.bId, false, false, false, false, 3);

			assertEquals(Meaning.boxIsImmediatelyLeftOf(center, spoke),         test.leftOf);
			assertEquals(Meaning.boxIsImmediatelyAbove(center, spoke),          test.above);
			assertEquals(Meaning.boxIsImmediatelyAboveAndLeftOf(center, spoke), test.diagonal);
			assertEquals(Meaning.boxIsSameAs(center,  spoke),                   test.same);
		}
	}
}
