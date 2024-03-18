package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

// TODO: test boxes
// TODO: test roots and leaves
// TODO: iterators (indices)
// TODO: test special domains

public class PositionButterflyTest {
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

	private void testConstructor(int n) {
		final PositionButterfly butterfly = new PositionButterfly(n, null);
		testNodes(n, butterfly);
	}

	private void testNodes(int numberOfDecisions, final PositionButterfly butterfly) {
		final int numberOfTiers = Utility.numberOfNodeTiers(numberOfDecisions);
		assertEquals(butterfly.numberOfNodeTiers, numberOfTiers);
		assertEquals(butterfly.numberOfNodeTerms, numberOfDecisions);
		assertEquals(butterfly.positionNodes.length, numberOfTiers);
		assertEquals(butterfly.positionNodes[0].length, numberOfDecisions);
		for(int tier = 0; tier < butterfly.numberOfNodeTiers; tier++) {
			for(int term = 0; term < butterfly.numberOfNodeTerms; term++) {
				assertEquals(butterfly.positionNodes[tier][term].nodeTier, tier);
				assertEquals(butterfly.positionNodes[tier][term].nodeTerm, term);
			}
		}
	}

	// ERROR: BROKEN
	@Test
	public void testGroups2() {
		final PositionButterfly butterfly = new PositionButterfly(2, null);
		final PositionNode[][] positionNodes = new PositionNode[2][2];

		final PositionNode[][][] populationGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 1, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes),
					new PositionNode(1, 1, null, positionNodes)
				}
			}
		};

		final PositionNode[][][] hadamardGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes),
					new PositionNode(0, 1, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 1, null, positionNodes)
				}
			}
		};

//		assertGroupEquals(butterfly.populationGroups, populationGroups);
//		assertGroupEquals(butterfly.hadamardGroups, hadamardGroups);
	}

	// ERROR: BROKEN
	@Test
	public void testGroups4() {
		final PositionButterfly butterfly = new PositionButterfly(4, null);
		final PositionNode[][] positionNodes = new PositionNode[3][4];

		final PositionNode[][][] populationGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 2, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 3, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes),
					new PositionNode(1, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 2, null, positionNodes),
					new PositionNode(1, 3, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(2, 0, null, positionNodes),
					new PositionNode(2, 1, null, positionNodes),
					new PositionNode(2, 2, null, positionNodes),
					new PositionNode(2, 3, null, positionNodes)
				}
			}
		};

		final PositionNode[][][] hadamardGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes),
					new PositionNode(0, 1, null, positionNodes),
					new PositionNode(0, 2, null, positionNodes),
					new PositionNode(0, 3, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes),
					new PositionNode(1, 2, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 1, null, positionNodes),
					new PositionNode(1, 3, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(2, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 2, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 3, null, positionNodes)
				}
			}
		};

//		assertGroupEquals(butterfly.populationGroups, populationGroups);
//		assertGroupEquals(butterfly.hadamardGroups, hadamardGroups);
	}

	// ERROR: BROKEN
	@Test
	public void testGroups8() {
		final PositionButterfly butterfly = new PositionButterfly(8, null);
		final PositionNode[][] positionNodes = new PositionNode[4][8];

		final PositionNode[][][] populationGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 2, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 3, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 4, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 5, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 6, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(0, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes),
					new PositionNode(1, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 2, null, positionNodes),
					new PositionNode(1, 3, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 4, null, positionNodes),
					new PositionNode(1, 5, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 6, null, positionNodes),
					new PositionNode(1, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(2, 0, null, positionNodes),
					new PositionNode(2, 1, null, positionNodes),
					new PositionNode(2, 2, null, positionNodes),
					new PositionNode(2, 3, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 4, null, positionNodes),
					new PositionNode(2, 5, null, positionNodes),
					new PositionNode(2, 6, null, positionNodes),
					new PositionNode(2, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(3, 0, null, positionNodes),
					new PositionNode(3, 1, null, positionNodes),
					new PositionNode(3, 2, null, positionNodes),
					new PositionNode(3, 3, null, positionNodes),
					new PositionNode(3, 4, null, positionNodes),
					new PositionNode(3, 5, null, positionNodes),
					new PositionNode(3, 6, null, positionNodes),
					new PositionNode(3, 7, null, positionNodes)
				}
			}
		};

		final PositionNode[][][] hadamardGroups = new PositionNode[][][] {
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(0, 0, null, positionNodes),
					new PositionNode(0, 1, null, positionNodes),
					new PositionNode(0, 2, null, positionNodes),
					new PositionNode(0, 3, null, positionNodes),
					new PositionNode(0, 4, null, positionNodes),
					new PositionNode(0, 5, null, positionNodes),
					new PositionNode(0, 6, null, positionNodes),
					new PositionNode(0, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(1, 0, null, positionNodes),
					new PositionNode(1, 2, null, positionNodes),
					new PositionNode(1, 4, null, positionNodes),
					new PositionNode(1, 6, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(1, 1, null, positionNodes),
					new PositionNode(1, 3, null, positionNodes),
					new PositionNode(1, 5, null, positionNodes),
					new PositionNode(1, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(2, 0, null, positionNodes),
					new PositionNode(2, 4, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 1, null, positionNodes),
					new PositionNode(2, 5, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 2, null, positionNodes),
					new PositionNode(2, 6, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(2, 3, null, positionNodes),
					new PositionNode(2, 7, null, positionNodes)
				}
			},
			new PositionNode[][] {
				new PositionNode[] {
					new PositionNode(3, 0, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 1, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 2, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 3, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 4, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 5, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 6, null, positionNodes)
				},
				new PositionNode[] {
					new PositionNode(3, 7, null, positionNodes)
				}
			}
		};

//		assertGroupEquals(butterfly.populationGroups, populationGroups);
//		assertGroupEquals(butterfly.hadamardGroups, hadamardGroups);
	}

	private void assertGroupEquals(PositionNode[][][] groupsToTest, PositionNode[][][] groupsToCompare) {
		assertEquals(groupsToTest.length, groupsToCompare.length);
		for(int tier = 0; tier < groupsToTest.length; tier++) {
			assertEquals(groupsToTest[tier].length, groupsToCompare[tier].length);
			for(int group = 0; group < groupsToTest[tier].length; group++) {
				assertEquals(groupsToTest[tier][group].length, groupsToCompare[tier][group].length);
				for(int member = 0; member < groupsToTest[tier][group].length; member++) {
					assertEquals(groupsToTest[tier][group][member], groupsToCompare[tier][group][member]);
				}
			}
		}
	}
}
