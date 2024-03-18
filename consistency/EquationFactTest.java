package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class EquationFactTest {

	@Test
	public void testEquals() {
		final EquationFact p = EquationFact.newLeaf(1, 2);
		final EquationFact q = EquationFact.newLeaf(1, 2);
		final EquationFact r = EquationFact.newLeaf(2, 1);

		assertTrue(p == q);
		assertEquals(p, q);
		assertNotEquals(p, r);
		assertNotEquals(p, r);
	}

	@Test
	public void testLeafConstructor() {
		final EquationFact p = EquationFact.newLeaf(0, 0);
		final EquationFact q = EquationFact.newLeaf(2, 4);

		assertEquals(p.hadamard, 0);
		assertEquals(p.partialSum, 0);
		assertEquals(q.hadamard, 2);
		assertEquals(q.partialSum, 4);
	}

	@Test
	public void testParentConstructors() {
		final EquationFact leftChildFact = EquationFact.newLeaf(3, 6);
		final EquationFact rightChildFact = EquationFact.newLeaf(1, 2);
		final EquationFact leftParentFact = EquationFact.newLeftParent(leftChildFact, rightChildFact);
		final EquationFact rightParentFact = EquationFact.newRightParent(leftChildFact, rightChildFact);

		assertEquals(leftChildFact.hadamard, 3);
		assertEquals(leftChildFact.partialSum, 6);
		assertEquals(rightChildFact.hadamard, 1);
		assertEquals(rightChildFact.partialSum, 2);
		assertEquals(leftParentFact.hadamard, 2);
		assertEquals(leftParentFact.partialSum, 8);
		assertEquals(rightParentFact.hadamard, 1);
		assertEquals(rightParentFact.partialSum, 8);
	}
}
