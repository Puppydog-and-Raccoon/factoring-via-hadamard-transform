package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

// TODO: test boxes
// TODO: test roots and leaves
// TODO: iterators (indices)
// TODO: test special domains

public class PropertyButterflyTest {
	@Test
	public void testConstructor2() {
		testConstructor(2);
	}

	@Test
	public void testConstructor4() {
		testConstructor(4);
	}

	@Test
	public void testConstructor8() {
		testConstructor(8);
	}

	@Test
	public void testConstructor16() {
		testConstructor(16);
	}

	@Test
	public void testConstructor32() {
		testConstructor(32);
	}

	private void testConstructor(int numberOfDecisionsInProblem) {
		final PropertyButterfly butterfly = PropertyButterfly.make(numberOfDecisionsInProblem, DomainGenerator.STANDARD);
		testNodes(numberOfDecisionsInProblem, butterfly);
	}

	private void testNodes(int numberOfDecisions, final PropertyButterfly butterfly) {
		final int numberOfTiers = Utility.numberOfNodeTiers(numberOfDecisions);
		assertEquals(butterfly.numberOfNodeTiers, numberOfTiers);
		assertEquals(butterfly.numberOfNodeTerms, numberOfDecisions);
		assertEquals(butterfly.propertyNodes.length, numberOfTiers);
		assertEquals(butterfly.propertyNodes[0].length, numberOfDecisions);
		for(int nodeTier = 0; nodeTier < butterfly.numberOfNodeTiers; nodeTier++) {
			for(int nodeTerm = 0; nodeTerm < butterfly.numberOfNodeTerms; nodeTerm++) {
				assertEquals(butterfly.propertyNodes[nodeTier][nodeTerm].nodeTier, nodeTier);
				assertEquals(butterfly.propertyNodes[nodeTier][nodeTerm].nodeTerm, nodeTerm);
			}
		}
	}
}
