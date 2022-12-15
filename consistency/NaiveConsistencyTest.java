package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class NaiveConsistencyTest {

	@Test
	public void testValidate() {
		class TestData {
			final int          numberOfDecisions;
			final Constraint[] constraints;
			final int          numberOfTrues;
			final boolean      result;

			TestData(int numberOfDecisions, Constraint[] constraints, int numberOfTrues, boolean result) {
				this.numberOfDecisions = numberOfDecisions;
				this.constraints       = constraints;
				this.numberOfTrues     = numberOfTrues;
				this.result            = result;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(-1, new Constraint[]{},  0, false),
			new TestData( 0, new Constraint[]{},  0, false),

			new TestData( 1, new Constraint[]{}, -1, false),
			new TestData( 1, new Constraint[]{},  0, true),
			new TestData( 1, new Constraint[]{},  1, true),
			new TestData( 1, new Constraint[]{},  2, false),

			new TestData( 2, new Constraint[]{}, -1, false),
			new TestData( 2, new Constraint[]{},  0, true),
			new TestData( 2, new Constraint[]{},  1, true),
			new TestData( 2, new Constraint[]{},  2, true),
			new TestData( 2, new Constraint[]{},  3, false),

			new TestData( 2, new Constraint[]{new Constraint(-1,  0, 4)},  0, false),
			new TestData( 2, new Constraint[]{new Constraint( 0, -1, 4)},  0, false),
			new TestData( 2, new Constraint[]{new Constraint( 0,  1, 4)},  0, true),
			new TestData( 2, new Constraint[]{new Constraint( 1,  0, 4)},  0, true),
			new TestData( 2, new Constraint[]{new Constraint( 1,  2, 4)},  0, false),
			new TestData( 2, new Constraint[]{new Constraint( 2,  1, 4)},  0, false),
		};

		for(TestData test : tests) {
			Problem problem = new Problem(test.numberOfDecisions, test.constraints, test.numberOfTrues);
			assertEquals(problem.errors.isEmpty(), test.result);
		}
	}

	@Test
	public void testSolve() {
		class TestData {
			final int          numberOfDecisions;
			final Constraint[] constraints;
			final int          numberOfTrues;
			final boolean[]    result;

			TestData(int numberOfDecisions, Constraint[] constraints, int numberOfTrues, boolean[] result) {
				this.numberOfDecisions = numberOfDecisions;
				this.constraints       = constraints;
				this.numberOfTrues     = numberOfTrues;
				this.result            = result;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData( 1, new Constraint[]{}, 0, new boolean[]{false}),
			new TestData( 1, new Constraint[]{}, 1, new boolean[]{true}),

			new TestData( 2, new Constraint[]{},                          0, new boolean[]{false, false}),
			new TestData( 2, new Constraint[]{new Constraint( 0,  0, 4)}, 1, new boolean[]{false, true}),
			new TestData( 2, new Constraint[]{new Constraint( 1,  1, 4)}, 1, new boolean[]{true,  false}),
			new TestData( 2, new Constraint[]{},                          2, new boolean[]{true,  true}),

			new TestData( 3, new Constraint[]{new Constraint(0,  1, 4)},                           0, new boolean[]{false, false, false}),
			new TestData( 3, new Constraint[]{new Constraint(1,  1, 4), new Constraint(2,  2, 4)}, 1, new boolean[]{true,  false, false}),
			new TestData( 3, new Constraint[]{new Constraint(0,  0, 4), new Constraint(2,  2, 4)}, 1, new boolean[]{false, true,  false}),
			new TestData( 3, new Constraint[]{new Constraint(0,  0, 4), new Constraint(1,  1, 4)}, 1, new boolean[]{false, false, true}),
			new TestData( 3, new Constraint[]{new Constraint(0,  1, 4), new Constraint(0,  2, 4)}, 2, new boolean[]{false, true,  true}),
			new TestData( 3, new Constraint[]{new Constraint(1,  0, 4), new Constraint(1,  2, 4)}, 2, new boolean[]{true,  false, true}),
			new TestData( 3, new Constraint[]{new Constraint(2,  0, 4), new Constraint(2,  1, 4)}, 2, new boolean[]{true,  true,  false}),
			new TestData( 3, new Constraint[]{},                                                   3, new boolean[]{true,  true,  true}),
		};

		for(TestData test : tests) {
			Problem problem = new Problem(test.numberOfDecisions, test.constraints, test.numberOfTrues);
			boolean[] solution = (new NaiveConsistency()).solve(problem);
			assertBooleanArrayEquals(solution, test.result);
		}
	}

	@Test
	public void testPopulation() {
		class TestData {
			final boolean[] vector;
			final int       population;

			TestData(boolean[] vector, int population) {
				this.vector     = vector;
				this.population = population;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(new boolean[]{false}, 0),
			new TestData(new boolean[]{true},  1),

			new TestData(new boolean[]{false, false}, 0),
			new TestData(new boolean[]{true,  false}, 1),
			new TestData(new boolean[]{false, true},  1),
			new TestData(new boolean[]{true,  true},  2),

			new TestData(new boolean[]{false, false, false}, 0),
			new TestData(new boolean[]{true,  false, false}, 1),
			new TestData(new boolean[]{false, true,  false}, 1),
			new TestData(new boolean[]{true,  true,  false}, 2),
			new TestData(new boolean[]{false, false,  true}, 1),
			new TestData(new boolean[]{true,  false,  true}, 2),
			new TestData(new boolean[]{false, true,   true}, 2),
			new TestData(new boolean[]{true,  true,   true}, 3),

			new TestData(new boolean[]{false, false, false, false}, 0),
			new TestData(new boolean[]{true,  false, false, false}, 1),
			new TestData(new boolean[]{false, true,  false, false}, 1),
			new TestData(new boolean[]{true,  true,  false, false}, 2),
			new TestData(new boolean[]{false, false,  true, false}, 1),
			new TestData(new boolean[]{true,  false,  true, false}, 2),
			new TestData(new boolean[]{false, true,   true, false}, 2),
			new TestData(new boolean[]{true,  true,   true, false}, 3),
			new TestData(new boolean[]{false, false, false, true},  1),
			new TestData(new boolean[]{true,  false, false, true},  2),
			new TestData(new boolean[]{false, true,  false, true},  2),
			new TestData(new boolean[]{true,  true,  false, true},  3),
			new TestData(new boolean[]{false, false,  true, true},  2),
			new TestData(new boolean[]{true,  false,  true, true},  3),
			new TestData(new boolean[]{false, true,   true, true},  3),
			new TestData(new boolean[]{true,  true,   true, true},  4),
		};

		for(TestData test : tests) {
			assertEquals(test.population, (new NaiveConsistency()).population(test.vector));
		}
	}

	@Test
	public void testMatches() {
		class TestData {
			final boolean[]    vector;
			final Constraint[] constraints;
			final boolean      result;

			TestData(boolean[] vector, Constraint[] constraints, boolean result) {
				this.vector      = vector;
				this.constraints = constraints;
				this.result      = result;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(new boolean[]{false}, new Constraint[]{}, true),
			new TestData(new boolean[]{true},  new Constraint[]{}, true),
			new TestData(new boolean[]{false}, new Constraint[]{new Constraint(0, 0, 4)}, true),
			new TestData(new boolean[]{true},  new Constraint[]{new Constraint(0, 0, 4)}, false),

			new TestData(new boolean[]{false, false}, new Constraint[]{}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{}, true),
			new TestData(new boolean[]{false, true},  new Constraint[]{}, true),
			new TestData(new boolean[]{true,  true},  new Constraint[]{}, true),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 0, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 0, 4)}, false),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 0, 4)}, true),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 0, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 1, 4)}, true),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 1, 4)}, true),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 1, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4)}, false),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4)}, true),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{false, false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, true),
			new TestData(new boolean[]{true,  false}, new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{false, true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, false),
			new TestData(new boolean[]{true,  true},  new Constraint[]{new Constraint(0, 0, 4), new Constraint(0, 1, 4), new Constraint(1, 1, 4)}, false),
		};

		for(TestData test : tests) {
			boolean result = (new NaiveConsistency()).matches(test.vector, test.constraints);
			assertEquals(result, test.result);
		}
	};

	@Test
	public void testIncrement() {
		class TestData {
			final boolean[] input;
			final boolean[] output;
			final boolean   result;

			TestData(boolean[] input, boolean[] output, boolean result) {
				this.input  = input;
				this.output = output;
				this.result = result;
			}
		}

		TestData[] tests = new TestData[] {
			new TestData(new boolean[]{false}, new boolean[]{true},  false),
			new TestData(new boolean[]{true},  new boolean[]{false}, true),

			new TestData(new boolean[]{false, false}, new boolean[]{true,  false}, false),
			new TestData(new boolean[]{true,  false}, new boolean[]{false, true},  false),
			new TestData(new boolean[]{false, true},  new boolean[]{true,  true},  false),
			new TestData(new boolean[]{true,  true},  new boolean[]{false, false}, true),

			new TestData(new boolean[]{false, false, false}, new boolean[]{true,  false, false}, false),
			new TestData(new boolean[]{true,  false, false}, new boolean[]{false, true,  false}, false),
			new TestData(new boolean[]{false, true,  false}, new boolean[]{true,  true,  false}, false),
			new TestData(new boolean[]{true,  true,  false}, new boolean[]{false, false, true},  false),
			new TestData(new boolean[]{false, false,  true}, new boolean[]{true,  false, true},  false),
			new TestData(new boolean[]{true,  false,  true}, new boolean[]{false, true,  true},  false),
			new TestData(new boolean[]{false, true,   true}, new boolean[]{true,  true,  true},  false),
			new TestData(new boolean[]{true,  true,   true}, new boolean[]{false, false, false}, true),

			new TestData(new boolean[]{false, false, false, false}, new boolean[]{true,  false, false, false}, false),
			new TestData(new boolean[]{true,  false, false, false}, new boolean[]{false, true,  false, false}, false),
			new TestData(new boolean[]{false, true,  false, false}, new boolean[]{true,  true,  false, false}, false),
			new TestData(new boolean[]{true,  true,  false, false}, new boolean[]{false, false, true,  false}, false),
			new TestData(new boolean[]{false, false,  true, false}, new boolean[]{true,  false, true,  false}, false),
			new TestData(new boolean[]{true,  false,  true, false}, new boolean[]{false, true,  true,  false}, false),
			new TestData(new boolean[]{false, true,   true, false}, new boolean[]{true,  true,  true,  false}, false),
			new TestData(new boolean[]{true,  true,   true, false}, new boolean[]{false, false, false, true},  false),
			new TestData(new boolean[]{false, false, false, true},  new boolean[]{true,  false, false, true},  false),
			new TestData(new boolean[]{true,  false, false, true},  new boolean[]{false, true,  false, true},  false),
			new TestData(new boolean[]{false, true,  false, true},  new boolean[]{true,  true,  false, true},  false),
			new TestData(new boolean[]{true,  true,  false, true},  new boolean[]{false, false, true,  true},  false),
			new TestData(new boolean[]{false, false,  true, true},  new boolean[]{true,  false, true,  true},  false),
			new TestData(new boolean[]{true,  false,  true, true},  new boolean[]{false, true,  true,  true},  false),
			new TestData(new boolean[]{false, true,   true, true},  new boolean[]{true,  true,  true,  true},  false),
			new TestData(new boolean[]{true,  true,   true, true},  new boolean[]{false, false, false, false}, true),
		};

		for(TestData test : tests) {
			boolean[] vector = test.input.clone();
			boolean result = (new NaiveConsistency()).increment(vector);
			assertBooleanArrayEquals(vector, test.output);
			assertEquals(result, test.result);
		}
	}

	private void assertBooleanArrayEquals(boolean[] a, boolean[] b) {
		if(a == null || b == null) {
			assertEquals(a, b);
		} else {
			assertEquals(a.length, b.length);
			for(int i = 0; i < a.length; i++) {
				assertEquals(a[i], b[i]);
			}
		}
	}
}
