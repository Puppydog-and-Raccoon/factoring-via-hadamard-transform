package applications;

import static org.junit.Assert.*;

import org.junit.Test;

import consistency.Utility;

public class GarbageCanPackingTest {
	static final String BANANA_PEEL   = "banana peel";
	static final String BROKEN_TOY    = "broken toy";
	static final String DOG_DO        = "dog do";
	static final String EMPTY_BOTTLE  = "empty bottle";
	static final String FISH_HEAD     = "fish head";
	static final String LOST_FORK     = "lost fork";
	static final String MOLDY_BREAD   = "moldy bread";
	static final String SNOTTY_TISSUE = "snotty tissue";
	static final String WEEDS         = "weeds";

	static final String[][] THREE_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD           },
		new String[]{FISH_HEAD             },
		new String[]{MOLDY_BREAD, FISH_HEAD},
	};
	static final boolean[] THREE_SOLUTION = new boolean[]{true, true, false};

	static final String[][] SIX_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,   FISH_HEAD    },
		new String[]{FISH_HEAD,     BANANA_PEEL  },
		new String[]{BANANA_PEEL,   EMPTY_BOTTLE },
		new String[]{EMPTY_BOTTLE,  WEEDS        },
		new String[]{WEEDS,         SNOTTY_TISSUE},
		new String[]{SNOTTY_TISSUE, MOLDY_BREAD  },
	};
	static final boolean[][] SIX_SOLUTIONS = new boolean[][]{
		new boolean[]{true,  false, true,  false, true,  false},
		new boolean[]{false, true,  false, true,  false, true},
	};

	static final String[][] THIRTEEN_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,  FISH_HEAD    },
		new String[]{FISH_HEAD,    BANANA_PEEL  },
		new String[]{BANANA_PEEL,  MOLDY_BREAD  },
		new String[]{BANANA_PEEL                },
		new String[]{                           },
		new String[]{LOST_FORK                  },
		new String[]{WEEDS,        SNOTTY_TISSUE},
		new String[]{WEEDS,        SNOTTY_TISSUE},
		new String[]{WEEDS,        SNOTTY_TISSUE},
		new String[]{WEEDS                      },
		new String[]{SNOTTY_TISSUE              },
		new String[]{DOG_DO                     },
		new String[]{BROKEN_TOY                 },
	};
	static final boolean[] THIRTEEN_SOLUTION = new boolean[]{true, false, false, true, true, true, false, false, false, true, true, true, true};

	@Test
	public void test_0() {
		run(THREE_GARBAGE_CANS, 2, THREE_SOLUTION);
	}

	@Test
	public void test_1() {
		run(THREE_GARBAGE_CANS, 3, (boolean[]) null);
	}

	@Test
	public void test_2() {
		run(SIX_GARBAGE_CANS, 3, SIX_SOLUTIONS);
	}

	@Test
	public void test_3() {
		run(SIX_GARBAGE_CANS, 4, (boolean[]) null);
	}

//	@Test
	public void test_4() {
		run(THIRTEEN_GARBAGE_CANS, 8, THIRTEEN_SOLUTION);
	}

//	@Test
	public void test_5() {
		run(THIRTEEN_GARBAGE_CANS, 9, (boolean[]) null);
	}

	void run(final String[][] garbageCans, final int numberOfGarbageCansToSelect, final boolean[] givenSolution) {
		final GarbageCanPackingProblem problem = new GarbageCanPackingProblem(garbageCans, numberOfGarbageCansToSelect);
		final GarbageCanPackingSolution algorithmSolution = GarbageCanPackingAlgorithm.solve(problem);
		System.out.println(algorithmSolution);
		assertTrue(Utility.isEqual(algorithmSolution.garbageCanSelections, givenSolution));
	}

	void run(final String[][] garbageCans, final int numberOfGarbageCansToSelect, final boolean[][] givenSolutions) {
		final GarbageCanPackingProblem problem = new GarbageCanPackingProblem(garbageCans, numberOfGarbageCansToSelect);
		final GarbageCanPackingSolution algorithmSolution = GarbageCanPackingAlgorithm.solve(problem);
		System.out.println(algorithmSolution);
		assertTrue(Utility.isEqualToAny(algorithmSolution.garbageCanSelections, givenSolutions));
	}
}
