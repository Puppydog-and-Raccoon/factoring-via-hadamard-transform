package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProblemTest {

	@Test
	public void testSuccess() {
		Constraint[] testConstraints = new Constraint[]{
			new Constraint(1, 2, 4)
		};
		Problem p = new Problem(4, testConstraints, 3);
		assertEquals(p.numberOfDecisions, 4);
		assertArrayEquals(p.constraints, testConstraints);
		assertEquals(p.numberOfTrues, 3);
		assertEquals(p.errors.size(), 0);
	}

	@Test
	public void testNumberOfDecisionsError() {
		Constraint[] testConstraints = new Constraint[]{
			new Constraint(1, 2, 4)
		};
		Problem p = new Problem(0, testConstraints, 3);
		assertEquals(p.numberOfDecisions, 0);
		assertArrayEquals(p.constraints, testConstraints);
		assertEquals(p.numberOfTrues, 3);
		assertEquals(p.errors.size(), 1);
		assertTrue(p.errors.toString().contains(Problem.NUMBER_OF_DECISIONS_IS_NOT_POSITIVE));
	}

	@Test
	public void testConstraintsError() {
		Problem p = new Problem(4, null, 3);
		assertEquals(p.numberOfDecisions, 4);
		assertArrayEquals(p.constraints, null);
		assertEquals(p.numberOfTrues, 3);
		assertEquals(p.errors.size(), 1);
		assertTrue(p.errors.toString().contains(Problem.CONSTRAINTS_IS_NULL));
	}

	@Test
	public void testConstraintError() {
		Constraint[] testConstraints = new Constraint[]{
			new Constraint(7, 2, 8)
		};
		Problem p = new Problem(4, testConstraints, 3);
		assertEquals(p.numberOfDecisions, 4);
		assertArrayEquals(p.constraints, testConstraints);
		assertEquals(p.numberOfTrues, 3);
		assertEquals(p.errors.size(), 1);
		assertTrue(p.errors.toString().contains(Problem.CONSTRAINT_VALUE_IS_OUT_OF_RANGE));
	}

	@Test
	public void testNumberOfTruesError() {
		Constraint[] testConstraints = new Constraint[]{
			new Constraint(1, 2, 8)
		};
		Problem p = new Problem(4, testConstraints, 7);
		assertEquals(p.numberOfDecisions, 4);
		assertArrayEquals(p.constraints, testConstraints);
		assertEquals(p.numberOfTrues, 7);
		assertEquals(p.errors.size(), 1);
		assertTrue(p.errors.toString().contains(Problem.NUMBER_OF_TRUES_IS_OUT_OF_RANGE));
	}
}
