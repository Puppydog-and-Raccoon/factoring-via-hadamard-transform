package applications;

import static org.junit.Assert.*;

import org.junit.Test;

import consistency.Utility;

public class GarbageCanPackingTest {
	static final String APPLE_CORE        = "apple core";
	static final String BANANA_PEEL       = "banana peel";
	static final String BROKEN_CUP        = "broken cup";
	static final String BROKEN_PLATE      = "broken plate";
	static final String BROKEN_TOY        = "broken toy";
	static final String BURNT_TOAST       = "burnt toast";
	static final String DEAD_FLOWERS      = "dead flowers";
	static final String DUST_BUNNIES      = "dust bunnies";
	static final String CHERRY_PITS       = "cherry pits";
	static final String DOG_DO            = "dog do";
	static final String DRY_PEN           = "dry pen";
	static final String EMPTY_BOTTLE      = "empty bottle";
	static final String EMPTY_BOX         = "empty box";
	static final String EMPTY_CAN         = "empty can";
	static final String FISH_HEAD         = "fish head";
	static final String GRAPEFRUIT_RIND   = "grapefruit rind";
	static final String GRASS_CLIPPINGS   = "grass clippngs";
	static final String LOST_FORK         = "lost fork";
	static final String LOST_KNIFE        = "lost knife";
	static final String LOST_SPOON        = "lost spoon";
	static final String MOLDY_BREAD       = "moldy bread";
	static final String POTATO_PEEL       = "potato peel";
	static final String RANCID_BUTTER     = "rancid butter";
	static final String SNOTTY_TISSUE     = "snotty tissue";
	static final String SOILED_DIAPER     = "soiled diaper";
	static final String SPOILED_LEFTOVERS = "spoiled leftovers";
	static final String SPOILED_MILK      = "spoiled milk";
	static final String WEEDS             = "weeds";

	static final String[][] THREE_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,            },
		new String[]{FISH_HEAD,              },
		new String[]{MOLDY_BREAD, FISH_HEAD, },
	};
	static final int TWO_MAX = 2;
	static final boolean[][] THREE_SOLUTIONS = new boolean[][]{
		new boolean[]{true, true, false}
	};

	static final String[][] SIX_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,   FISH_HEAD,     },
		new String[]{FISH_HEAD,     BANANA_PEEL,   },
		new String[]{BANANA_PEEL,   EMPTY_BOTTLE,  },
		new String[]{EMPTY_BOTTLE,  WEEDS,         },
		new String[]{WEEDS,         SNOTTY_TISSUE, },
		new String[]{SNOTTY_TISSUE, MOLDY_BREAD,   },
	};
	static final int SIX_MAX = 3;
	static final boolean[][] SIX_SOLUTIONS = new boolean[][]{
		new boolean[]{true,  false, true,  false, true,  false},
		new boolean[]{false, true,  false, true,  false, true},
	};

	static final String[][] THIRTEEN_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,   FISH_HEAD,     },
		new String[]{FISH_HEAD,     BANANA_PEEL,   },
		new String[]{BANANA_PEEL,   MOLDY_BREAD,   },
		new String[]{BANANA_PEEL                   },
		new String[]{                              },
		new String[]{LOST_FORK                     },
		new String[]{WEEDS,         SNOTTY_TISSUE, },
		new String[]{WEEDS,         SNOTTY_TISSUE, },
		new String[]{WEEDS,         SNOTTY_TISSUE, },
		new String[]{WEEDS                         },
		new String[]{SNOTTY_TISSUE,                },
		new String[]{DOG_DO,                       },
		new String[]{BROKEN_TOY,                   },
	};
	static final int THIRTEEN_MAX = 8;
	static final boolean[][] THIRTEEN_SOLUTIONS = new boolean[][]{
		new boolean[]{true, false, false, true, true, true, false, false, false, true, true, true, true}
	};

	static final String[][] TWENTY_SIX_GARBAGE_CANS = new String[][]{
		new String[]{MOLDY_BREAD,     FISH_HEAD,     },
		new String[]{FISH_HEAD,       BANANA_PEEL,   },
		new String[]{BANANA_PEEL,     MOLDY_BREAD,   },
		new String[]{BANANA_PEEL                     },
		new String[]{                                },
		new String[]{LOST_FORK                       },
		new String[]{WEEDS,           SNOTTY_TISSUE, },
		new String[]{WEEDS,           SNOTTY_TISSUE, },
		new String[]{WEEDS,           SNOTTY_TISSUE, },
		new String[]{WEEDS                           },
		new String[]{SNOTTY_TISSUE,                  },
		new String[]{DOG_DO,                         },
		new String[]{BROKEN_TOY,                     },

		new String[]{BURNT_TOAST,     LOST_SPOON,    },
		new String[]{LOST_SPOON,      BROKEN_CUP,    },
		new String[]{BROKEN_CUP,      BURNT_TOAST,   },
		new String[]{BROKEN_CUP                      },
		new String[]{                                },
		new String[]{EMPTY_BOX                       },
		new String[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new String[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new String[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new String[]{GRASS_CLIPPINGS                 },
		new String[]{DRY_PEN,                        },
		new String[]{GRAPEFRUIT_RIND,                },
		new String[]{DUST_BUNNIES,                   },
	};
	static final int TWENTY_SIX_MAX = 16;
	static final boolean[][] TWENTY_SIX_SOLUTIONS = new boolean[][]{
		new boolean[]{true, false, false, true, true, true, false, false, false, true, true, true, true,
					  true, false, false, true, true, true, false, false, false, true, true, true, true}
	};

	@Test
	public void test_0() {
		run(THREE_GARBAGE_CANS, TWO_MAX, THREE_SOLUTIONS);
	}

	@Test
	public void test_1() {
		run(THREE_GARBAGE_CANS, TWO_MAX + 1, null);
	}

	@Test
	public void test_2() {
		run(SIX_GARBAGE_CANS, SIX_MAX, SIX_SOLUTIONS);
	}

	@Test
	public void test_3() {
		run(SIX_GARBAGE_CANS, SIX_MAX + 1, null);
	}

	@Test
	public void test_4() {
		run(THIRTEEN_GARBAGE_CANS, THIRTEEN_MAX, THIRTEEN_SOLUTIONS);
	}

	@Test
	public void test_5() {
		run(THIRTEEN_GARBAGE_CANS, THIRTEEN_MAX + 1, null);
	}

	// takes 8gb and 4.5 hours
	@Test
	public void test_6() {
		run(TWENTY_SIX_GARBAGE_CANS, TWENTY_SIX_MAX, TWENTY_SIX_SOLUTIONS);
	}

	// takes 8gb and 4.5 hours
	@Test
	public void test_7() {
		run(TWENTY_SIX_GARBAGE_CANS, TWENTY_SIX_MAX + 1, null);
	}

	void run(
		final String[][]  garbageCans,
		final int         numberOfGarbageCansToSelect,
		final boolean[][] givenSolutions
	) {
		final GarbageCanPackingProblem garbageCanPackingProblem = new GarbageCanPackingProblem(garbageCans, numberOfGarbageCansToSelect, givenSolutions);
		final GarbageCanPackingSolution algorithmSolution = garbageCanPackingProblem.solve();
		System.out.println("solution = " + algorithmSolution);
		assertTrue(Utility.contains(algorithmSolution.garbageCanSelections, givenSolutions));
	}
}
