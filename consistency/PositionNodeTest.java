package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionNodeTest {
	void check(
		final int     tier,
		final int     term,
		final int     n,

		final boolean isRoot,
		final boolean isLeaf,
		final boolean isPopulation,
		final int     minimumHadamard,
		final int     maximumHadamard
	) {
		final PositionButterfly positionButterfly = new PositionButterfly(n, null);
		final PositionNode node = positionButterfly.positionNodes[tier][term];

		assertEquals(node.nodeTier,            tier);
		assertEquals(node.nodeTerm,            term);
		assertEquals(node.isRoot(),          isRoot);
		assertEquals(node.isLeaf(),          isLeaf);
		assertEquals(node.hadamardDomain.minimum, minimumHadamard);
		assertEquals(node.hadamardDomain.maximum, maximumHadamard);

		assertFalse(node.hadamardDomain.isInDomain(minimumHadamard - 1));
		assertTrue(node.hadamardDomain.isInDomain(minimumHadamard));
		assertTrue(node.hadamardDomain.isInDomain(maximumHadamard));
		assertFalse(node.hadamardDomain.isInDomain(maximumHadamard + 1));
	}

	@Test
	public void test() {
		check(0, 0, 2, true,  false, true,   0, 1);
		check(0, 1, 2, true,  false, true,   0, 1);
		check(1, 0, 2, false, true,  true,   0, 2);
		check(1, 1, 2, false, true,  false, -1, 1);

		check(0, 0, 4, true,  false, true,   0, 1);
		check(0, 1, 4, true,  false, true,   0, 1);
		check(0, 2, 4, true,  false, true,   0, 1);
		check(0, 3, 4, true,  false, true,   0, 1);
		check(1, 0, 4, false, false, true,   0, 2);
		check(1, 1, 4, false, false, false, -1, 1);
		check(1, 2, 4, false, false, true,   0, 2);
		check(1, 3, 4, false, false, false, -1, 1);
		check(2, 0, 4, false, true,  true,   0, 4);
		check(2, 1, 4, false, true,  false, -2, 2);
		check(2, 2, 4, false, true,  false, -2, 2);
		check(2, 3, 4, false, true,  false, -2, 2);

		check(0, 0, 8, true,  false, true,   0, 1);
		check(0, 1, 8, true,  false, true,   0, 1);
		check(0, 2, 8, true,  false, true,   0, 1);
		check(0, 3, 8, true,  false, true,   0, 1);
		check(0, 4, 8, true,  false, true,   0, 1);
		check(0, 5, 8, true,  false, true,   0, 1);
		check(0, 6, 8, true,  false, true,   0, 1);
		check(0, 7, 8, true,  false, true,   0, 1);
		check(1, 0, 8, false, false, true,   0, 2);
		check(1, 1, 8, false, false, false, -1, 1);
		check(1, 2, 8, false, false, true,   0, 2);
		check(1, 3, 8, false, false, false, -1, 1);
		check(1, 4, 8, false, false, true,   0, 2);
		check(1, 5, 8, false, false, false, -1, 1);
		check(1, 6, 8, false, false, true,   0, 2);
		check(1, 7, 8, false, false, false, -1, 1);
		check(2, 0, 8, false, false, true,   0, 4);
		check(2, 1, 8, false, false, false, -2, 2);
		check(2, 2, 8, false, false, false, -2, 2);
		check(2, 3, 8, false, false, false, -2, 2);
		check(2, 4, 8, false, false, true,   0, 4);
		check(2, 5, 8, false, false, false, -2, 2);
		check(2, 6, 8, false, false, false, -2, 2);
		check(2, 7, 8, false, false, false, -2, 2);
		check(3, 0, 8, false, true,  true,   0, 8);
		check(3, 1, 8, false, true,  false, -4, 4);
		check(3, 2, 8, false, true,  false, -4, 4);
		check(3, 3, 8, false, true,  false, -4, 4);
		check(3, 4, 8, false, true,  false, -4, 4);
		check(3, 5, 8, false, true,  false, -4, 4);
		check(3, 6, 8, false, true,  false, -4, 4);
		check(3, 7, 8, false, true,  false, -4, 4);
	}
}
