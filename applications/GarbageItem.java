package applications;

/**
 * Garbage items
 */
public enum GarbageItem {
	APPLE_CORE        ("apple core"),
	BANANA_PEEL       ("banana peel"),
	BROKEN_CUP        ("broken cup"),
	BROKEN_PLATE      ("broken plate"),
	BROKEN_TOY        ("broken toy"),
	BURNT_TOAST       ("burnt toast"),
	DRIED_FLOWERS     ("dried flowers"),
	DUST_BUNNIES      ("dust bunnies"),
	CHERRY_PITS       ("cherry pits"),
	DOG_DO            ("dog do"),
	DRY_PEN           ("dry pen"),
	EMPTY_BOTTLE      ("empty bottle"),
	EMPTY_BOX         ("empty box"),
	EMPTY_CAN         ("empty can"),
	FISH_HEAD         ("fish head"),
	GRAPEFRUIT_RIND   ("grapefruit rind"),
	GRASS_CLIPPINGS   ("grass clippngs"),
	LOST_FORK         ("lost fork"),
	LOST_KNIFE        ("lost knife"),
	LOST_SPOON        ("lost spoon"),
	MOLDY_BREAD       ("moldy bread"),
	POTATO_PEEL       ("potato peel"),
	RANCID_BUTTER     ("rancid butter"),
	SNOTTY_TISSUE     ("snotty tissue"),
	SOILED_DIAPER     ("soiled diaper"),
	SPOILED_LEFTOVERS ("spoiled leftovers"),
	SPOILED_MILK      ("spoiled milk"),
	WEEDS             ("weeds");

	public final String name;

	private GarbageItem(
		final String name
	) {
		this.name = name;
	}
}
