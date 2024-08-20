package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBoxTest {
	void checkProperties(
		int boxTier,
		int boxTerm,
		int n,

		int parentNodeTier,
		int childNodeTier,
		int leftNodeTerm,
		int rightNodeTerm
	) {
		final int numberOfDecisionsInProblem = n;
		final PropertyButterfly propertyButterfly = PropertyButterfly.make(numberOfDecisionsInProblem, DomainGenerator.STANDARD);
		final PropertyBox box = propertyButterfly.propertyBoxes[boxTier][boxTerm];

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
		checkProperties(0, 0, 2, 0, 1, 0, 1);

		checkProperties(0, 0, 4, 0, 1, 0, 1);
		checkProperties(0, 1, 4, 0, 1, 2, 3);
		checkProperties(1, 0, 4, 1, 2, 0, 2);
		checkProperties(1, 1, 4, 1, 2, 1, 3);

		checkProperties(0, 0, 8, 0, 1, 0, 1);
		checkProperties(0, 1, 8, 0, 1, 2, 3);
		checkProperties(0, 2, 8, 0, 1, 4, 5);
		checkProperties(0, 3, 8, 0, 1, 6, 7);
		checkProperties(1, 0, 8, 1, 2, 0, 2);
		checkProperties(1, 1, 8, 1, 2, 1, 3);
		checkProperties(1, 2, 8, 1, 2, 4, 6);
		checkProperties(1, 3, 8, 1, 2, 5, 7);
		checkProperties(2, 0, 8, 2, 3, 0, 4);
		checkProperties(2, 1, 8, 2, 3, 1, 5);
		checkProperties(2, 2, 8, 2, 3, 2, 6);
		checkProperties(2, 3, 8, 2, 3, 3, 7);
	}

	void checkCanonicals(
		int boxTier,
		int boxTerm,
		int populationTreeBoxTerm,
		int spinePartialSumTreeBoxTerm,
		int n
	) {
		final int numberOfDecisionsInProblem = n;
		final PropertyButterfly propertyButterfly = PropertyButterfly.make(numberOfDecisionsInProblem, DomainGenerator.STANDARD);
		final PropertyBox box = propertyButterfly.propertyBoxes[boxTier][boxTerm];

		assertEquals(box.boxTier,                    boxTier);
		assertEquals(box.boxTerm,                    boxTerm);
		assertEquals(box.populationTreeBoxTerm,      populationTreeBoxTerm);
		assertEquals(box.spinePartialSumTreeBoxTerm, spinePartialSumTreeBoxTerm);
	}

	@Test
	public void testCanonicals() {
		checkCanonicals(0, 0, 0, 0, 2);

		checkCanonicals(0, 0, 0, 0, 4);
		checkCanonicals(0, 1, 1, 0, 4);
		checkCanonicals(1, 0, 0, 0, 4);
		checkCanonicals(1, 1, 0, 1, 4);

		checkCanonicals(0, 0, 0, 0, 8);
		checkCanonicals(0, 1, 1, 0, 8);
		checkCanonicals(0, 2, 2, 0, 8);
		checkCanonicals(0, 3, 3, 0, 8);
		checkCanonicals(1, 0, 0, 0, 8);
		checkCanonicals(1, 1, 0, 1, 8);
		checkCanonicals(1, 2, 1, 0, 8);
		checkCanonicals(1, 3, 1, 1, 8);
		checkCanonicals(2, 0, 0, 0, 8);
		checkCanonicals(2, 1, 0, 1, 8);
		checkCanonicals(2, 2, 0, 2, 8);
		checkCanonicals(2, 3, 0, 3, 8);
	}
}
