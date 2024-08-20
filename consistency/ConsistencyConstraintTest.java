package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

// TODO: update tests

public class ConsistencyConstraintTest {
	@Test
	public void testEquals0() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyOneOfTwo(0, 1, 4);
		ConsistencyConstraint d = ConsistencyConstraint.exactlyOneOfTwo(1, 0, 4);
		ConsistencyConstraint e = ConsistencyConstraint.exactlyOneOfTwo(0, 2, 4);
		assertEquals(c, d);
		assertNotEquals(c, e);
		assertNotEquals(d, e);
	}

	@Test
	public void testEquals1() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyZeroOfOne(0, 4);
		ConsistencyConstraint d = ConsistencyConstraint.exactlyZeroOfOne(0, 4);
		ConsistencyConstraint e = ConsistencyConstraint.exactlyZeroOfOne(1, 4);
		assertEquals(c, d);
		assertNotEquals(c, e);
		assertNotEquals(d, e);
	}

	@Test
	public void testConstructor0() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyZeroOfOne(0, 4);
		assertEquals(c.sortedAndUniqueDecisionIds.length,1);
		assertEquals(c.sortedAndUniqueDecisionIds[0], 0);
		assertEquals(c.sortedAndUniqueNumbersOfTrues[0], 0);
//		assertEquals(c.toString(), "ConsistencyConstraint [numberOfTruesInConstraint=0, type=Exactly, decisions=[0]]");
	}

	@Test
	public void testConstructor1() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyOneOfTwo(0, 1, 4);
		assertEquals(c.sortedAndUniqueDecisionIds.length, 2);
		assertEquals(c.sortedAndUniqueDecisionIds[0], 0);
		assertEquals(c.sortedAndUniqueDecisionIds[1], 1);
		assertEquals(c.sortedAndUniqueNumbersOfTrues[0], 1);
//		assertEquals(c.toString(), "ConsistencyConstraint [numberOfTruesInConstraint=1, type=Exactly, decisions=[0, 1]]");
	}

	@Test
	public void testConstructor2() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyOneOfTwo(2, 0, 4);
		assertEquals(c.sortedAndUniqueDecisionIds.length, 2);
		assertEquals(c.sortedAndUniqueDecisionIds[0], 0);
		assertEquals(c.sortedAndUniqueDecisionIds[1], 2);
		assertEquals(c.sortedAndUniqueNumbersOfTrues[0], 1);
//		assertEquals(c.toString(), "ConsistencyConstraint [numberOfTruesInConstraint=1, type=Exactly, decisions=[0, 2]]");
	}

	@Test
	public void testConstructor3() {
		ConsistencyConstraint c = ConsistencyConstraint.exactlyOneOfThree(2, 1, 0, 4);
		assertEquals(c.sortedAndUniqueDecisionIds.length, 3);
		assertEquals(c.sortedAndUniqueDecisionIds[0], 0);
		assertEquals(c.sortedAndUniqueDecisionIds[1], 1);
		assertEquals(c.sortedAndUniqueDecisionIds[2], 2);
		assertEquals(c.sortedAndUniqueNumbersOfTrues[0], 1);
//		assertEquals(c.toString(), "ConsistencyConstraint [numberOfTruesInConstraint=1, type=Exactly, decisions=[0, 1, 2]]");
	}
}
