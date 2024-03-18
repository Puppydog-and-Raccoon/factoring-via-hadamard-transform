package consistency;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SupportAnswerTest {
	@Test
	public void test_2() {
		final PositionButterfly positionButterfly = new PositionButterfly(2, null);
		final SupportAnswer answer = new SupportAnswer(positionButterfly, new int[]{0, 1});
		assertTrue(Arrays.equals(answer.hadamardButterfly[0], new int[]{0,  1}));
		assertTrue(Arrays.equals(answer.hadamardButterfly[1], new int[]{1, -1}));
		assertTrue(Arrays.equals(answer.spineButterfly[0], new int[]{0,  0}));
		assertTrue(Arrays.equals(answer.spineButterfly[1], new int[]{1, -1}));
		assertTrue(Arrays.equals(answer.populationButterfly[0], new int[]{0, 1}));
		assertTrue(Arrays.equals(answer.populationButterfly[1], new int[]{1, 1}));
		assertEquals(answer.solutionButterfly[0][0], SolutionFact.newFact( 0,  0, 0));
		assertEquals(answer.solutionButterfly[0][1], SolutionFact.newFact( 1,  0, 1));
		assertEquals(answer.solutionButterfly[1][0], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[1][1], SolutionFact.newFact(-1, -1, 1));
		assertEquals(answer.supportSpineButterfly[0][0], SolutionSpineFact.newFact(1, -1));
		assertEquals(answer.supportPopulationButterfly[0][0], SolutionPopulationFact.newFact(0, 1));
	}

	@Test
	public void test_4() {
		final PositionButterfly positionButterfly = new PositionButterfly(4, null);
		final SupportAnswer answer = new SupportAnswer(positionButterfly, new int[]{1, 0, 0, 1});
		assertArrayEquals(answer.hadamardButterfly[0], new int[]{1, 0, 0,  1});
		assertArrayEquals(answer.hadamardButterfly[1], new int[]{1, 1, 1, -1});
		assertArrayEquals(answer.hadamardButterfly[2], new int[]{2, 0, 0,  2});
		assertArrayEquals(answer.spineButterfly[0], new int[]{1, 1, 1, 1});
		assertArrayEquals(answer.spineButterfly[1], new int[]{1, 1, 1, 1});
		assertArrayEquals(answer.spineButterfly[2], new int[]{2, 0, 0, 2});
		assertArrayEquals(answer.populationButterfly[0], new int[]{1, 0, 0, 1});
		assertArrayEquals(answer.populationButterfly[1], new int[]{1, 1, 1, 1});
		assertArrayEquals(answer.populationButterfly[2], new int[]{2, 2, 2, 2});
		assertEquals(answer.solutionButterfly[0][0], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[0][1], SolutionFact.newFact( 0,  1, 0));
		assertEquals(answer.solutionButterfly[0][2], SolutionFact.newFact( 0,  1, 0));
		assertEquals(answer.solutionButterfly[0][3], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[1][0], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[1][1], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[1][2], SolutionFact.newFact( 1,  1, 1));
		assertEquals(answer.solutionButterfly[1][3], SolutionFact.newFact(-1,  1, 1));
		assertEquals(answer.solutionButterfly[2][0], SolutionFact.newFact( 2,  2, 2));
		assertEquals(answer.solutionButterfly[2][1], SolutionFact.newFact( 0,  0, 2));
		assertEquals(answer.solutionButterfly[2][2], SolutionFact.newFact( 0,  0, 2));
		assertEquals(answer.solutionButterfly[2][3], SolutionFact.newFact( 2,  2, 2));
		assertEquals(answer.supportSpineButterfly[0][0], SolutionSpineFact.newFact(1, 1));
		assertEquals(answer.supportSpineButterfly[0][1], SolutionSpineFact.newFact(1, 1));
		assertEquals(answer.supportSpineButterfly[1][0], SolutionSpineFact.newFact(2, 0));
		assertEquals(answer.supportSpineButterfly[1][1], SolutionSpineFact.newFact(0, 2));
		assertEquals(answer.supportPopulationButterfly[0][0], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[0][1], SolutionPopulationFact.newFact(0, 1));
		assertEquals(answer.supportPopulationButterfly[1][0], SolutionPopulationFact.newFact(1, 1));
		assertEquals(answer.supportPopulationButterfly[1][1], SolutionPopulationFact.newFact(1, 1));
	}

	@Test
	public void test_8() {
		final PositionButterfly positionButterfly = new PositionButterfly(8, null);
		final SupportAnswer answer = new SupportAnswer(positionButterfly, new int[]{1, 0, 0, 0, 0, 1, 0, 0});
		assertTrue(Arrays.equals(answer.hadamardButterfly[0], new int[]{1, 0, 0, 0, 0,  1, 0, 0}));
		assertTrue(Arrays.equals(answer.hadamardButterfly[1], new int[]{1, 1, 0, 0, 1, -1, 0, 0}));
		assertTrue(Arrays.equals(answer.hadamardButterfly[2], new int[]{1, 1, 1, 1, 1, -1, 1, -1}));
		assertTrue(Arrays.equals(answer.hadamardButterfly[3], new int[]{2, 0, 2, 0, 0, 2, 0, 2}));
		assertTrue(Arrays.equals(answer.spineButterfly[0], new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
		assertTrue(Arrays.equals(answer.spineButterfly[1], new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
		assertTrue(Arrays.equals(answer.spineButterfly[2], new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
		assertTrue(Arrays.equals(answer.spineButterfly[3], new int[]{2, 0, 2, 0, 0, 2, 0, 2}));
		assertTrue(Arrays.equals(answer.populationButterfly[0], new int[]{1, 0, 0, 0, 0, 1, 0, 0}));
		assertTrue(Arrays.equals(answer.populationButterfly[1], new int[]{1, 1, 0, 0, 1, 1, 0, 0}));
		assertTrue(Arrays.equals(answer.populationButterfly[2], new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
		assertTrue(Arrays.equals(answer.populationButterfly[3], new int[]{2, 2, 2, 2, 2, 2, 2, 2}));
		// TODO: add solution fact
		assertEquals(answer.supportSpineButterfly[0][0], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[0][1], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[0][2], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[0][3], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[1][0], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[1][1], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[1][2], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[1][3], SolutionSpineFact.newFact(1,  1));
		assertEquals(answer.supportSpineButterfly[2][0], SolutionSpineFact.newFact(2,  0));
		assertEquals(answer.supportSpineButterfly[2][1], SolutionSpineFact.newFact(0,  2));
		assertEquals(answer.supportSpineButterfly[2][2], SolutionSpineFact.newFact(2,  0));
		assertEquals(answer.supportSpineButterfly[2][3], SolutionSpineFact.newFact(0,  2));
		assertEquals(answer.supportPopulationButterfly[0][0], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[0][1], SolutionPopulationFact.newFact(0, 0));
		assertEquals(answer.supportPopulationButterfly[0][2], SolutionPopulationFact.newFact(0, 1));
		assertEquals(answer.supportPopulationButterfly[0][3], SolutionPopulationFact.newFact(0, 0));
		assertEquals(answer.supportPopulationButterfly[1][0], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[1][1], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[1][2], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[1][3], SolutionPopulationFact.newFact(1, 0));
		assertEquals(answer.supportPopulationButterfly[2][0], SolutionPopulationFact.newFact(1, 1));
		assertEquals(answer.supportPopulationButterfly[2][1], SolutionPopulationFact.newFact(1, 1));
		assertEquals(answer.supportPopulationButterfly[2][2], SolutionPopulationFact.newFact(1, 1));
		assertEquals(answer.supportPopulationButterfly[2][3], SolutionPopulationFact.newFact(1, 1));
	}
}
