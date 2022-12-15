package consistency;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class EPTest {
	@Test
	public void testEP0() {
		HashSet<EquationPair> nodes = new HashSet<EquationPair>();

		assertEquals(nodes, EP.expand(new EP[0]));
	}

	@Test
	public void testEP1() {
		HashSet<EquationPair> pairs = new HashSet<EquationPair>();
		pairs.add(new EquationPair(0, 1));

		assertEquals(pairs, EP.expand(new EP[]{new EP(0, 1)}));
	}

	@Test
	public void testEP2() {
		HashSet<EquationPair> pairs = new HashSet<EquationPair>();
		pairs.add(new EquationPair(0, 1));
		pairs.add(new EquationPair(0, 4));
		pairs.add(new EquationPair(0, 7));

		assertEquals(pairs, EP.expand(new EP[]{new EP(0, 1, 7, 3)}));
	}

	@Test
	public void testEP3() {
		HashSet<EquationPair> pairs = new HashSet<EquationPair>();
		pairs.add(new EquationPair(0, 1));
		pairs.add(new EquationPair(0, 4));
		pairs.add(new EquationPair(0, 7));
		pairs.add(new EquationPair(1, 2));

		assertEquals(pairs, EP.expand(new EP[]{new EP(0, 1, 7, 3), new EP(1, 2)}));
	}
}
