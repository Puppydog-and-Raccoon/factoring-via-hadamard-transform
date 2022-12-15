package factoring;

import static org.junit.Assert.*;
import org.junit.Test;

public class SolutionTest {

	@Test
	public void testConstructor() {
		class TestData {
			public final boolean[] factorA;
			public final boolean[] factorB;
			public final boolean[] smallerFactor;
			public final boolean[] largerFactor;
			public final String    string;

			public TestData(boolean[] factorA, boolean[] factorB, boolean[] smallerFactor, boolean[] largerFactor, String string) {
				this.factorA       = factorA;
				this.factorB       = factorB;
				this.smallerFactor = smallerFactor;
				this.largerFactor  = largerFactor;
				this.string        = string;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(null,                    null,                    null, null, "<null,null>"),
			new TestData(Utility.booleans(false), null,                    null, null, "<null,null>"),
			new TestData(null,                    Utility.booleans(false), null, null, "<null,null>"),

			new TestData(Utility.booleans(false), Utility.booleans(false), Utility.booleans(false), Utility.booleans(false), "<0,0>"),
			new TestData(Utility.booleans(false), Utility.booleans(true),  Utility.booleans(false), Utility.booleans(true),  "<0,1>"),
			new TestData(Utility.booleans(true),  Utility.booleans(false), Utility.booleans(false), Utility.booleans(true),  "<0,1>"),
			new TestData(Utility.booleans(true),  Utility.booleans(true),  Utility.booleans(true),  Utility.booleans(true),  "<1,1>"),

			new TestData(Utility.booleans(false, false), Utility.booleans(false, false),  Utility.booleans(false, false), Utility.booleans(false, false), "<00,00>"),
			new TestData(Utility.booleans(false, false), Utility.booleans(false, true),   Utility.booleans(false, false), Utility.booleans(false, true),  "<00,01>"),
			new TestData(Utility.booleans(false, false), Utility.booleans(true,  false),  Utility.booleans(false, false), Utility.booleans(true,  false), "<00,10>"),
			new TestData(Utility.booleans(false, false), Utility.booleans(true,  true),   Utility.booleans(false, false), Utility.booleans(true,  true),  "<00,11>"),
			new TestData(Utility.booleans(false, true),  Utility.booleans(false, false),  Utility.booleans(false, false), Utility.booleans(false, true),  "<00,01>"),
			new TestData(Utility.booleans(false, true),  Utility.booleans(false, true),   Utility.booleans(false, true),  Utility.booleans(false, true),  "<01,01>"),
			new TestData(Utility.booleans(false, true),  Utility.booleans(true,  false),  Utility.booleans(false, true),  Utility.booleans(true,  false), "<01,10>"),
			new TestData(Utility.booleans(false, true),  Utility.booleans(true,  true),   Utility.booleans(false, true),  Utility.booleans(true,  true),  "<01,11>"),
			new TestData(Utility.booleans(true,  false), Utility.booleans(false, false),  Utility.booleans(false, false), Utility.booleans(true,  false), "<00,10>"),
			new TestData(Utility.booleans(true,  false), Utility.booleans(false, true),   Utility.booleans(false, true),  Utility.booleans(true,  false), "<01,10>"),
			new TestData(Utility.booleans(true,  false), Utility.booleans(true,  false),  Utility.booleans(true,  false), Utility.booleans(true,  false), "<10,10>"),
			new TestData(Utility.booleans(true,  false), Utility.booleans(true,  true),   Utility.booleans(true,  false), Utility.booleans(true,  true),  "<10,11>"),
			new TestData(Utility.booleans(true,  true),  Utility.booleans(false, false),  Utility.booleans(false, false), Utility.booleans(true,  true),  "<00,11>"),
			new TestData(Utility.booleans(true,  true),  Utility.booleans(false, true),   Utility.booleans(false, true),  Utility.booleans(true,  true),  "<01,11>"),
			new TestData(Utility.booleans(true,  true),  Utility.booleans(true,  false),  Utility.booleans(true,  false), Utility.booleans(true,  true),  "<10,11>"),
			new TestData(Utility.booleans(true,  true),  Utility.booleans(true,  true),   Utility.booleans(true,  true),  Utility.booleans(true,  true),  "<11,11>"),
		};

		for(TestData test : tests) {
			Solution solution = new Solution(test.factorA, test.factorB);
			assertTrue(Utility.booleanArrayEquals(solution.smallerFactor, test.smallerFactor));
			assertTrue(Utility.booleanArrayEquals(solution.largerFactor,  test.largerFactor));
			assertEquals(solution.toString(), test.string);
		}
	}
}
