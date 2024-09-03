package applications;

import static org.junit.Assert.*;

import org.junit.Test;

import consistency.Utility;
import static applications.GarbageItem.*;

public class GarbageCanPackingTest {
	static final GarbageItem[][] THREE_GARBAGE_CANS = new GarbageItem[][]{
		new GarbageItem[]{MOLDY_BREAD,            },
		new GarbageItem[]{FISH_HEAD,              },
		new GarbageItem[]{MOLDY_BREAD, FISH_HEAD, },
	};
	static final int TWO_MAX = 2;
	static final boolean[][] THREE_SOLUTIONS = new boolean[][]{
		new boolean[]{true, true, false}
	};

	static final GarbageItem[][] SIX_GARBAGE_CANS = new GarbageItem[][]{
		new GarbageItem[]{MOLDY_BREAD,   FISH_HEAD,     },
		new GarbageItem[]{FISH_HEAD,     BANANA_PEEL,   },
		new GarbageItem[]{BANANA_PEEL,   EMPTY_BOTTLE,  },
		new GarbageItem[]{EMPTY_BOTTLE,  WEEDS,         },
		new GarbageItem[]{WEEDS,         SNOTTY_TISSUE, },
		new GarbageItem[]{SNOTTY_TISSUE, MOLDY_BREAD,   },
	};
	static final int SIX_MAX = 3;
	static final boolean[][] SIX_SOLUTIONS = new boolean[][]{
		new boolean[]{true,  false, true,  false, true,  false},
		new boolean[]{false, true,  false, true,  false, true},
	};

	static final GarbageItem[][] THIRTEEN_GARBAGE_CANS = new GarbageItem[][]{
		new GarbageItem[]{MOLDY_BREAD,   FISH_HEAD,     },
		new GarbageItem[]{FISH_HEAD,     BANANA_PEEL,   },
		new GarbageItem[]{BANANA_PEEL,   MOLDY_BREAD,   },
		new GarbageItem[]{BANANA_PEEL                   },
		new GarbageItem[]{                              },
		new GarbageItem[]{LOST_FORK                     },
		new GarbageItem[]{WEEDS,         SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS,         SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS,         SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS                         },
		new GarbageItem[]{SNOTTY_TISSUE,                },
		new GarbageItem[]{DOG_DO,                       },
		new GarbageItem[]{BROKEN_TOY,                   },
	};
	static final int THIRTEEN_MAX = 8;
	static final boolean[][] THIRTEEN_SOLUTIONS = new boolean[][]{
		new boolean[]{true, false, false, true, true, true, false, false, false, true, true, true, true}
	};

	static final GarbageItem[][] TWENTY_SIX_GARBAGE_CANS = new GarbageItem[][]{
		new GarbageItem[]{MOLDY_BREAD,     FISH_HEAD,     },
		new GarbageItem[]{FISH_HEAD,       BANANA_PEEL,   },
		new GarbageItem[]{BANANA_PEEL,     MOLDY_BREAD,   },
		new GarbageItem[]{BANANA_PEEL                     },
		new GarbageItem[]{                                },
		new GarbageItem[]{LOST_FORK                       },
		new GarbageItem[]{WEEDS,           SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS,           SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS,           SNOTTY_TISSUE, },
		new GarbageItem[]{WEEDS                           },
		new GarbageItem[]{SNOTTY_TISSUE,                  },
		new GarbageItem[]{DOG_DO,                         },
		new GarbageItem[]{BROKEN_TOY,                     },

		new GarbageItem[]{BURNT_TOAST,     LOST_SPOON,    },
		new GarbageItem[]{LOST_SPOON,      BROKEN_CUP,    },
		new GarbageItem[]{BROKEN_CUP,      BURNT_TOAST,   },
		new GarbageItem[]{BROKEN_CUP                      },
		new GarbageItem[]{                                },
		new GarbageItem[]{EMPTY_BOX                       },
		new GarbageItem[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new GarbageItem[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new GarbageItem[]{GRASS_CLIPPINGS, DRY_PEN,       },
		new GarbageItem[]{GRASS_CLIPPINGS                 },
		new GarbageItem[]{DRY_PEN,                        },
		new GarbageItem[]{GRAPEFRUIT_RIND,                },
		new GarbageItem[]{DUST_BUNNIES,                   },
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

	// takes 8gb and 2.0 hours
	@Test
	public void test_6() {
		run(TWENTY_SIX_GARBAGE_CANS, TWENTY_SIX_MAX, TWENTY_SIX_SOLUTIONS);
	}

	// takes 8gb and 2.5 hours
	@Test
	public void test_7() {
		run(TWENTY_SIX_GARBAGE_CANS, TWENTY_SIX_MAX + 1, null);
	}

	void run(
		final GarbageItem[][] garbageCans,
		final int             numberOfGarbageCansToSelect,
		final boolean[][]     givenSolutions
	) {
		final GarbageCanPackingProblem garbageCanPackingProblem = new GarbageCanPackingProblem(garbageCans, numberOfGarbageCansToSelect, givenSolutions);
		final GarbageCanPackingSolution algorithmSolution = garbageCanPackingProblem.solve();
		System.out.println("solution = " + algorithmSolution);
		assertTrue(Utility.contains(algorithmSolution.garbageCanSelections, givenSolutions));
	}
}
