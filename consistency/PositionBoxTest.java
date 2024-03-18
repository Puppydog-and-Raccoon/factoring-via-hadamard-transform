package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionBoxTest {
	void checkPositions(
		int boxTier,
		int boxTerm,
		int n,

		int parentNodeTier,
		int childNodeTier,
		int leftNodeTerm,
		int rightNodeTerm
	) {
		final PositionButterfly positionButterfly = new PositionButterfly(n, null);
		final PositionBox box = positionButterfly.positionBoxes[boxTier][boxTerm];

		assertEquals(box.leftParentNode.nodeTier,  parentNodeTier);
		assertEquals(box.leftParentNode.nodeTerm,  leftNodeTerm);
		assertEquals(box.rightParentNode.nodeTier, parentNodeTier);
		assertEquals(box.rightParentNode.nodeTerm, rightNodeTerm);
		assertEquals(box.leftChildNode.nodeTier,   childNodeTier);
		assertEquals(box.leftChildNode.nodeTerm,   leftNodeTerm);
		assertEquals(box.rightChildNode.nodeTier,  childNodeTier);
		assertEquals(box.rightChildNode.nodeTerm,  rightNodeTerm);
	}

	@Test
	public void testPositions() {
		checkPositions(0, 0, 2, 0, 1, 0, 1);

		checkPositions(0, 0, 4, 0, 1, 0, 1);
		checkPositions(0, 1, 4, 0, 1, 2, 3);
		checkPositions(1, 0, 4, 1, 2, 0, 2);
		checkPositions(1, 1, 4, 1, 2, 1, 3);

		checkPositions(0, 0, 8, 0, 1, 0, 1);
		checkPositions(0, 1, 8, 0, 1, 2, 3);
		checkPositions(0, 2, 8, 0, 1, 4, 5);
		checkPositions(0, 3, 8, 0, 1, 6, 7);
		checkPositions(1, 0, 8, 1, 2, 0, 2);
		checkPositions(1, 1, 8, 1, 2, 1, 3);
		checkPositions(1, 2, 8, 1, 2, 4, 6);
		checkPositions(1, 3, 8, 1, 2, 5, 7);
		checkPositions(2, 0, 8, 2, 3, 0, 4);
		checkPositions(2, 1, 8, 2, 3, 1, 5);
		checkPositions(2, 2, 8, 2, 3, 2, 6);
		checkPositions(2, 3, 8, 2, 3, 3, 7);
	}

	void checkCanonicals(
		int boxTier,
		int boxTerm,
		int n,

		boolean isSpineBox,
		boolean isPopulationBox,
		int     spineTerm,
		int     populationTerm
	) {
		final PositionButterfly positionButterfly = new PositionButterfly(n, null);
		final PositionBox box = positionButterfly.positionBoxes[boxTier][boxTerm];

		assertEquals(box.isSpineTermBox(),      isSpineBox);
		assertEquals(box.isPopulationTermBox(), isPopulationBox);
	}

	@Test
	public void testCanonicals() {
		checkCanonicals(0, 0, 2,    true,  true,  0, 0);

		checkCanonicals(0, 0, 4,    true,  true,  0, 0);
		checkCanonicals(0, 1, 4,    false, true,  0, 1);
		checkCanonicals(1, 0, 4,    true,  true,  0, 0);
		checkCanonicals(1, 1, 4,    true,  false, 1, 0);

		checkCanonicals(0, 0, 8,    true,  true,  0, 0);
		checkCanonicals(0, 1, 8,    false, true,  0, 1);
		checkCanonicals(0, 2, 8,    false, true,  0, 2);
		checkCanonicals(0, 3, 8,    false, true,  0, 3);
		checkCanonicals(1, 0, 8,    true,  true,  0, 0);
		checkCanonicals(1, 1, 8,    true,  false, 1, 0);
		checkCanonicals(1, 2, 8,    false, true,  0, 2);
		checkCanonicals(1, 3, 8,    false, false, 1, 2);
		checkCanonicals(2, 0, 8,    true,  true,  0, 0);
		checkCanonicals(2, 1, 8,    true,  false, 1, 0);
		checkCanonicals(2, 2, 8,    true,  false, 2, 0);
		checkCanonicals(2, 3, 8,    true,  false, 3, 0);
	}
}
