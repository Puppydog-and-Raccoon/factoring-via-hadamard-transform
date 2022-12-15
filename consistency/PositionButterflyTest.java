package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class PositionButterflyTest {

	class PositionTestCase {
		final int     tier;
		final int     term;
		final boolean isLeftNode;
		final boolean isInTopTier;
		final boolean isInBottomTier;
		final int     hadamardMinimum;
		final int     hadamardMaximum;
		final boolean hadamardIsPopulationNode;

		PositionTestCase(
			int     tier,
			int     term,
			boolean isLeftNode,
			boolean isInTopTier,
			boolean isInBottomTier,
			int     hadamardMinimum,
			int     hadamardMaximum,
			boolean hadamardIsPopulationNode
		) {
			this.tier                     = tier;
			this.term                     = term;
			this.isLeftNode               = isLeftNode;
			this.isInTopTier              = isInTopTier;
			this.isInBottomTier           = isInBottomTier;
			this.hadamardMinimum          = hadamardMinimum;
			this.hadamardMaximum          = hadamardMaximum;
			this.hadamardIsPopulationNode = hadamardIsPopulationNode;
		}

		void run(PositionButterfly positionButterfly) {
			Position position = positionButterfly.positions[tier][term];
			assertEquals(position.tier, tier);
			assertEquals(position.term, term);
			assertEquals(position.isLeftNode, isLeftNode);
			assertEquals(position.isInTopTier, isInTopTier);
			assertEquals(position.isInBottomTier, isInBottomTier);
			assertEquals(position.hadamardDomain.minimum, hadamardMinimum);
			assertEquals(position.hadamardDomain.maximum, hadamardMaximum);
			assertEquals(position.hadamardDomain.isPopulationNode, hadamardIsPopulationNode);
		}
	}

	// ERROR: move
	@Test
	public void testNewPositionButterfly1() {
		PositionTestCase[] testCases = new PositionTestCase[]{
			new PositionTestCase(0, 0, true, true, true, 0, 1, true),
		};
		PositionButterfly positionButterfly = new PositionButterfly(1);
		for(PositionTestCase testCase : testCases) {
			testCase.run(positionButterfly);
		}
	}

	@Test
	public void testNewPositionButterfly2() {
		PositionTestCase[] testCases = new PositionTestCase[]{
			new PositionTestCase(0, 0, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 1, false, true,  false,  0, 1, true),
			new PositionTestCase(1, 0, true,  false, true,   0, 2, true),
			new PositionTestCase(1, 1, true,  false, true,  -1, 1, false),
		};
		PositionButterfly positionButterfly = new PositionButterfly(2);
		for(PositionTestCase testCase : testCases) {
			testCase.run(positionButterfly);
		}
	}

	@Test
	public void testNewPositionButterfly4() {
		PositionTestCase[] testCases = new PositionTestCase[]{
			new PositionTestCase(0, 0, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 1, false, true,  false,  0, 1, true),
			new PositionTestCase(0, 2, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 3, false, true,  false,  0, 1, true),
			new PositionTestCase(1, 0, true,  false, false,  0, 2, true),
			new PositionTestCase(1, 1, true,  false, false, -1, 1, false),
			new PositionTestCase(1, 2, false, false, false,  0, 2, true),
			new PositionTestCase(1, 3, false, false, false, -1, 1, false),
			new PositionTestCase(2, 0, true,  false, true,   0, 4, true),
			new PositionTestCase(2, 1, true,  false, true,  -2, 2, false),
			new PositionTestCase(2, 2, true,  false, true,  -2, 2, false),
			new PositionTestCase(2, 3, true,  false, true,  -2, 2, false),
		};
		PositionButterfly positionButterfly = new PositionButterfly(4);
		for(PositionTestCase testCase : testCases) {
			testCase.run(positionButterfly);
		}
	}

	@Test
	public void testNewPositionButterfly8() {
		PositionTestCase[] testCases = new PositionTestCase[]{
			new PositionTestCase(0, 0, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 1, false, true,  false,  0, 1, true),
			new PositionTestCase(0, 2, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 3, false, true,  false,  0, 1, true),
			new PositionTestCase(0, 4, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 5, false, true,  false,  0, 1, true),
			new PositionTestCase(0, 6, true,  true,  false,  0, 1, true),
			new PositionTestCase(0, 7, false, true,  false,  0, 1, true),
			new PositionTestCase(1, 0, true,  false, false,  0, 2, true),
			new PositionTestCase(1, 1, true,  false, false, -1, 1, false),
			new PositionTestCase(1, 2, false, false, false,  0, 2, true),
			new PositionTestCase(1, 3, false, false, false, -1, 1, false),
			new PositionTestCase(1, 4, true,  false, false,  0, 2, true),
			new PositionTestCase(1, 5, true,  false, false, -1, 1, false),
			new PositionTestCase(1, 6, false, false, false,  0, 2, true),
			new PositionTestCase(1, 7, false, false, false, -1, 1, false),
			new PositionTestCase(2, 0, true,  false, false,  0, 4, true),
			new PositionTestCase(2, 1, true,  false, false, -2, 2, false),
			new PositionTestCase(2, 2, true,  false, false, -2, 2, false),
			new PositionTestCase(2, 3, true,  false, false, -2, 2, false),
			new PositionTestCase(2, 4, false, false, false,  0, 4, true),
			new PositionTestCase(2, 5, false, false, false, -2, 2, false),
			new PositionTestCase(2, 6, false, false, false, -2, 2, false),
			new PositionTestCase(2, 7, false, false, false, -2, 2, false),
			new PositionTestCase(3, 0, true,  false, true,   0, 8, true),
			new PositionTestCase(3, 1, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 2, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 3, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 4, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 5, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 6, true,  false, true,  -4, 4, false),
			new PositionTestCase(3, 7, true,  false, true,  -4, 4, false),
		};
		PositionButterfly positionButterfly = new PositionButterfly(8);
		for(PositionTestCase testCase : testCases) {
			testCase.run(positionButterfly);
		}
	}
}
