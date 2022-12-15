package consistency;

import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Test;

public class ConstraintTest {
	@Test
	public void testConstructor() {
		Constraint c0 = new Constraint(0, 0, 4);
		assertEquals(c0.i, 0);
		assertEquals(c0.j, 0);

		Constraint c1 = new Constraint(1, 2, 4);
		assertEquals(c1.i, 1);
		assertEquals(c1.j, 2);
	}

	@Test
	public void testEquals() {
		Constraint c0 = new Constraint(0, 0, 4);
		Constraint c1 = new Constraint(0, 0, 4);
		assertTrue(c0 != c1);
		assertEquals(c0, c1);

		Constraint c2 = new Constraint(1, 2, 4);
		Constraint c3 = new Constraint(2, 1, 4);
		assertTrue(c2 != c3);
		assertEquals(c2, c3);
	}

	@Test
	public void testHashSetMembers() {
		HashSet<Constraint> constraints = new HashSet<Constraint>();
		constraints.add(new Constraint(0, 1, 4));
		constraints.add(new Constraint(1, 0, 4));
		assertEquals(constraints.size(), 1);

		constraints.add(new Constraint(2, 3, 4));
		constraints.add(new Constraint(3, 2, 4));
		assertEquals(constraints.size(), 2);

		constraints.remove(new Constraint(0, 1, 4));
		constraints.remove(new Constraint(3, 2, 4));
		assertEquals(constraints.size(), 0);
	}
}
