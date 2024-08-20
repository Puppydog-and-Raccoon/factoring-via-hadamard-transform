package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class EquationFactTest {

	@Test
	public void testEquals() {
		final EquationFact p = EquationFact.make(1, 2, 1, 0);
		final EquationFact q = EquationFact.make(1, 2, 1, 0);
		final EquationFact r = EquationFact.make(2, 1, 2, 0);

		assertTrue(p == q);
		assertEquals(p, q);
		assertNotEquals(p, r);
		assertNotEquals(p, r);
	}

	@Test
	public void testLeafConstructor() {
		final EquationFact p = EquationFact.make(0, 0, 0, 0);
		final EquationFact q = EquationFact.make(2, 4, 6, 8);

		assertEquals(p.hadamard,   0);
		assertEquals(p.population, 0);
		assertEquals(p.spine,      0);
		assertEquals(p.partialSum, 0);

		assertEquals(q.hadamard,   2);
		assertEquals(q.population, 4);
		assertEquals(q.spine,      6);
		assertEquals(q.partialSum, 8);
	}

	@Test
	public void testParentConstructors() {
		final EquationFact    leftChildFact         = EquationFact.make(3, 22, 3, 13);
		final EquationFact    rightChildFact        = EquationFact.make(1, 22, 1, 17);
		final PopulationDelta parentPopulationDelta = PopulationDelta.make(10, 12);
		final EquationFact    leftParentFact        = EquationFact.makeLeftParent(leftChildFact, rightChildFact, parentPopulationDelta);
		final EquationFact    rightParentFact       = EquationFact.makeRightParent(leftChildFact, rightChildFact, parentPopulationDelta);

		assertEquals(leftChildFact.hadamard,      3);
		assertEquals(leftChildFact.population,   22);
		assertEquals(leftChildFact.spine,         3);
		assertEquals(leftChildFact.partialSum,   13);
		assertEquals(rightChildFact.hadamard,     1);
		assertEquals(rightChildFact.population,  22);
		assertEquals(rightChildFact.spine,        1);
		assertEquals(rightChildFact.partialSum,  17);
		assertEquals(leftParentFact.hadamard,     2);
		assertEquals(leftParentFact.population,  10);
		assertEquals(leftParentFact.spine,        2);
		assertEquals(leftParentFact.partialSum,  15);
		assertEquals(rightParentFact.hadamard,    1);
		assertEquals(rightParentFact.population, 12);
		assertEquals(rightParentFact.spine,       2);
		assertEquals(rightParentFact.partialSum, 15);
	}
}
