package applications;

import static org.junit.Assert.*;
import static applications.GarbageItemState.*;
import consistency.Utility;

import org.junit.Test;

public class SatisfiedCritterTest {

//	@Test
	public void test_6() {
		final GarbageItemState[][] clawses = new GarbageItemState[][] {
			new GarbageItemState[]{PRESENT_LOST_FORK,    ABSENT_LOST_SPOON,   },
			new GarbageItemState[]{PRESENT_LOST_SPOON,   ABSENT_LOST_KNIFE,   },
			new GarbageItemState[]{PRESENT_LOST_KNIFE,   ABSENT_EMPTY_BOX,    },
			new GarbageItemState[]{PRESENT_EMPTY_BOX,    ABSENT_EMPTY_CAN,    },
			new GarbageItemState[]{PRESENT_EMPTY_CAN,    ABSENT_EMPTY_BOTTLE, },
			new GarbageItemState[]{PRESENT_EMPTY_BOTTLE, ABSENT_LOST_FORK,    },
		};
		final boolean[][] testSolutions = new boolean[][]{
			new boolean[]{true,  true,  true,  true,  true,  true,  },
			new boolean[]{false, false, false, false, false, false, },
		};
		run(clawses, testSolutions);
	}

//	@Test
	public void test_13() {
		final GarbageItemState[][] clawses = new GarbageItemState[][] {
			new GarbageItemState[]{PRESENT_LOST_FORK,       ABSENT_LOST_SPOON,      },
			new GarbageItemState[]{PRESENT_LOST_SPOON,      ABSENT_LOST_KNIFE,      },
			new GarbageItemState[]{PRESENT_LOST_KNIFE,      ABSENT_EMPTY_BOX,       },
			new GarbageItemState[]{PRESENT_EMPTY_BOX,       ABSENT_EMPTY_CAN,       },
			new GarbageItemState[]{PRESENT_EMPTY_CAN,       ABSENT_EMPTY_BOTTLE,    },
			new GarbageItemState[]{PRESENT_EMPTY_BOTTLE,    ABSENT_LOST_FORK,       },
			new GarbageItemState[]{PRESENT_LOST_FORK,       ABSENT_GRAPEFRUIT_RIND, },
			new GarbageItemState[]{PRESENT_GRAPEFRUIT_RIND, ABSENT_POTATO_PEEL,     },
			new GarbageItemState[]{PRESENT_POTATO_PEEL,     ABSENT_APPLE_CORE,      },
			new GarbageItemState[]{PRESENT_APPLE_CORE,      ABSENT_BANANA_PEEL,     },
			new GarbageItemState[]{PRESENT_BANANA_PEEL,     ABSENT_GRASS_CLIPPINGS, },
			new GarbageItemState[]{PRESENT_GRASS_CLIPPINGS, ABSENT_WEEDS,           },
			new GarbageItemState[]{PRESENT_WEEDS,           ABSENT_LOST_FORK,       },
		};
		final boolean[][] testSolutions = new boolean[][]{
			new boolean[]{true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  },
			new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, },
		};
		run(clawses, testSolutions);
	}

	// take 4gb and 6 minutes
//	@Test
	public void test_17() {
		final GarbageItemState[][] clawses = new GarbageItemState[][] {
			new GarbageItemState[]{PRESENT_APPLE_CORE,      ABSENT_BANANA_PEEL,     },
			new GarbageItemState[]{PRESENT_BANANA_PEEL,     ABSENT_BROKEN_CUP,      },
			new GarbageItemState[]{PRESENT_BROKEN_CUP,      ABSENT_BROKEN_PLATE,    },
			new GarbageItemState[]{PRESENT_BROKEN_PLATE,    ABSENT_BROKEN_TOY,      },
			new GarbageItemState[]{PRESENT_BROKEN_TOY,      ABSENT_BURNT_TOAST,     },
			new GarbageItemState[]{PRESENT_BURNT_TOAST,     ABSENT_DRIED_FLOWERS,   },
			new GarbageItemState[]{PRESENT_DRIED_FLOWERS,   ABSENT_DUST_BUNNIES,    },
			new GarbageItemState[]{PRESENT_DUST_BUNNIES,    ABSENT_CHERRY_PITS,     },
			new GarbageItemState[]{PRESENT_CHERRY_PITS,     ABSENT_DOG_DO,          },
			new GarbageItemState[]{PRESENT_DOG_DO,          ABSENT_DRY_PEN,         },
			new GarbageItemState[]{PRESENT_DRY_PEN,         ABSENT_EMPTY_BOTTLE,    },
			new GarbageItemState[]{PRESENT_EMPTY_BOTTLE,    ABSENT_EMPTY_BOX,       },
			new GarbageItemState[]{PRESENT_EMPTY_BOX,       ABSENT_EMPTY_CAN,       },
			new GarbageItemState[]{PRESENT_EMPTY_CAN,       ABSENT_FISH_HEAD,       },
			new GarbageItemState[]{PRESENT_FISH_HEAD,       ABSENT_GRAPEFRUIT_RIND, },
			new GarbageItemState[]{PRESENT_GRAPEFRUIT_RIND, ABSENT_GRASS_CLIPPINGS, },
			new GarbageItemState[]{PRESENT_GRASS_CLIPPINGS, ABSENT_APPLE_CORE,      },
		};
		final boolean[][] testSolutions = new boolean[][]{
			new boolean[]{true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  },
			new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, },
		};
		run(clawses, testSolutions);
	}

	// takes 6gb and 25 minutes
	@Test
	public void test_21() {
		final GarbageItemState[][] clawses = new GarbageItemState[][] {
			new GarbageItemState[]{PRESENT_APPLE_CORE,        ABSENT_BANANA_PEEL,       },
			new GarbageItemState[]{PRESENT_BANANA_PEEL,       ABSENT_BROKEN_CUP,        },
			new GarbageItemState[]{PRESENT_BROKEN_CUP,        ABSENT_BROKEN_PLATE,      },
			new GarbageItemState[]{PRESENT_BROKEN_PLATE,      ABSENT_BROKEN_TOY,        },
			new GarbageItemState[]{PRESENT_BROKEN_TOY,        ABSENT_BURNT_TOAST,       },
			new GarbageItemState[]{PRESENT_BURNT_TOAST,       ABSENT_DRIED_FLOWERS,     },
			new GarbageItemState[]{PRESENT_DRIED_FLOWERS,     ABSENT_DUST_BUNNIES,      },
			new GarbageItemState[]{PRESENT_DUST_BUNNIES,      ABSENT_CHERRY_PITS,       },
			new GarbageItemState[]{PRESENT_CHERRY_PITS,       ABSENT_DOG_DO,            },
			new GarbageItemState[]{PRESENT_DOG_DO,            ABSENT_DRY_PEN,           },
			new GarbageItemState[]{PRESENT_DRY_PEN,           ABSENT_EMPTY_BOTTLE,      },
			new GarbageItemState[]{PRESENT_EMPTY_BOTTLE,      ABSENT_EMPTY_BOX,         },
			new GarbageItemState[]{PRESENT_EMPTY_BOX,         ABSENT_EMPTY_CAN,         },
			new GarbageItemState[]{PRESENT_EMPTY_CAN,         ABSENT_FISH_HEAD,         },
			new GarbageItemState[]{PRESENT_FISH_HEAD,         ABSENT_GRAPEFRUIT_RIND,   },
			new GarbageItemState[]{PRESENT_GRAPEFRUIT_RIND,   ABSENT_GRASS_CLIPPINGS,   },
			new GarbageItemState[]{PRESENT_GRASS_CLIPPINGS,   ABSENT_LOST_FORK,         },
			new GarbageItemState[]{PRESENT_LOST_FORK,         ABSENT_LOST_KNIFE,        },
			new GarbageItemState[]{PRESENT_LOST_KNIFE,        ABSENT_LOST_SPOON,        },
			new GarbageItemState[]{PRESENT_LOST_SPOON,        ABSENT_MOLDY_BREAD,       },
			new GarbageItemState[]{PRESENT_MOLDY_BREAD,       ABSENT_APPLE_CORE,        },
		};
		final boolean[][] testSolutions = new boolean[][]{
			new boolean[]{true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  },
			new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, },
		};
		run(clawses, testSolutions);
	}

//	@Test
	public void test_28() {
		final GarbageItemState[][] clawses = new GarbageItemState[][] {
			new GarbageItemState[]{PRESENT_APPLE_CORE,        ABSENT_BANANA_PEEL,       },
			new GarbageItemState[]{PRESENT_BANANA_PEEL,       ABSENT_BROKEN_CUP,        },
			new GarbageItemState[]{PRESENT_BROKEN_CUP,        ABSENT_BROKEN_PLATE,      },
			new GarbageItemState[]{PRESENT_BROKEN_PLATE,      ABSENT_BROKEN_TOY,        },
			new GarbageItemState[]{PRESENT_BROKEN_TOY,        ABSENT_BURNT_TOAST,       },
			new GarbageItemState[]{PRESENT_BURNT_TOAST,       ABSENT_DRIED_FLOWERS,     },
			new GarbageItemState[]{PRESENT_DRIED_FLOWERS,     ABSENT_DUST_BUNNIES,      },
			new GarbageItemState[]{PRESENT_DUST_BUNNIES,      ABSENT_CHERRY_PITS,       },
			new GarbageItemState[]{PRESENT_CHERRY_PITS,       ABSENT_DOG_DO,            },
			new GarbageItemState[]{PRESENT_DOG_DO,            ABSENT_DRY_PEN,           },
			new GarbageItemState[]{PRESENT_DRY_PEN,           ABSENT_EMPTY_BOTTLE,      },
			new GarbageItemState[]{PRESENT_EMPTY_BOTTLE,      ABSENT_EMPTY_BOX,         },
			new GarbageItemState[]{PRESENT_EMPTY_BOX,         ABSENT_EMPTY_CAN,         },
			new GarbageItemState[]{PRESENT_EMPTY_CAN,         ABSENT_FISH_HEAD,         },
			new GarbageItemState[]{PRESENT_FISH_HEAD,         ABSENT_GRAPEFRUIT_RIND,   },
			new GarbageItemState[]{PRESENT_GRAPEFRUIT_RIND,   ABSENT_GRASS_CLIPPINGS,   },
			new GarbageItemState[]{PRESENT_GRASS_CLIPPINGS,   ABSENT_LOST_FORK,         },
			new GarbageItemState[]{PRESENT_LOST_FORK,         ABSENT_LOST_KNIFE,        },
			new GarbageItemState[]{PRESENT_LOST_KNIFE,        ABSENT_LOST_SPOON,        },
			new GarbageItemState[]{PRESENT_LOST_SPOON,        ABSENT_MOLDY_BREAD,       },
			new GarbageItemState[]{PRESENT_MOLDY_BREAD,       ABSENT_POTATO_PEEL,       },
			new GarbageItemState[]{PRESENT_POTATO_PEEL,       ABSENT_RANCID_BUTTER,     },
			new GarbageItemState[]{PRESENT_RANCID_BUTTER,     ABSENT_SNOTTY_TISSUE,     },
			new GarbageItemState[]{PRESENT_SNOTTY_TISSUE,     ABSENT_SOILED_DIAPER,     },
			new GarbageItemState[]{PRESENT_SOILED_DIAPER,     ABSENT_SPOILED_LEFTOVERS, },
			new GarbageItemState[]{PRESENT_SPOILED_LEFTOVERS, ABSENT_SPOILED_MILK,      },
			new GarbageItemState[]{PRESENT_SPOILED_MILK,      ABSENT_WEEDS,             },
			new GarbageItemState[]{PRESENT_WEEDS,             ABSENT_APPLE_CORE,        },
		};
		final boolean[][] testSolutions = new boolean[][]{
			new boolean[]{true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  },
			new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, },
		};
		run(clawses, testSolutions);
	}

	void run(
		final GarbageItemState[][] clawses,
		final boolean[][]         testSolutions
	) {
		final SatisfiedCritterProblem satisfiedCritterProblem = new SatisfiedCritterProblem(clawses, pad(testSolutions));
		final SatisfiedCritterSolution satisfiedCritterSolution = satisfiedCritterProblem.solve();
		System.out.println("solution = " + satisfiedCritterSolution);
		assertTrue(satisfiedCritterSolution.isValid(satisfiedCritterProblem));
		assertTrue(satisfiedCritterSolution.fromGarbageToPresent != null);
	}

	int[][] pad(boolean[][] input) {
		int[][] output = new int[input.length][];
		for(int i = 0; i < input.length; i++) {
			output[i] = pad(input[i]);
		}
		return output;
	}

	private int[] pad(boolean[] input) {
		final int outputPairs = Utility.roundUpToPowerOfTwo(input.length);
		final int[] output = new int[2 * outputPairs];
		for(int i = 0; i < input.length; i++) {
			output[2 * i + 0] = Utility.toInt(input[i]);
			output[2 * i + 1] = Utility.toInt(!input[i]);
		}
		for(int i = input.length; i < outputPairs; i++) {
			output[2 * i + 0] = Utility.toInt(false);
			output[2 * i + 1] = Utility.toInt(true);
		}
		return output;
	}
}
