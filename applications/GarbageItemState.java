package applications;

import static applications.GarbageItem.*;

/**
 * These enums embody the presence or absence of specific garbage items
 * within a specific context.
 * <p>
 * For no good reason,
 * a garbage item state is referred to as a "claw",
 * a group of garbage item states (usually ored together) is referred to as a "claws",
 * and a group of groups of garbage item states (usually anded together)  is referred to as a "clawses".
 * </p>
 */
public enum GarbageItemState {
	PRESENT_APPLE_CORE       (true,  APPLE_CORE),
	ABSENT_APPLE_CORE        (false, APPLE_CORE), 

	PRESENT_BANANA_PEEL      (true,  BANANA_PEEL),
	ABSENT_BANANA_PEEL       (false, BANANA_PEEL),

	PRESENT_BROKEN_CUP       (true,  BROKEN_CUP),
	ABSENT_BROKEN_CUP        (false, BROKEN_CUP),

	PRESENT_BROKEN_PLATE     (true,  BROKEN_PLATE),
	ABSENT_BROKEN_PLATE      (false, BROKEN_PLATE),

	PRESENT_BROKEN_TOY       (true,  BROKEN_TOY),
	ABSENT_BROKEN_TOY        (false, BROKEN_TOY),

	PRESENT_BURNT_TOAST      (true,  BURNT_TOAST),
	ABSENT_BURNT_TOAST       (false, BURNT_TOAST),

	PRESENT_DRIED_FLOWERS    (true,  DRIED_FLOWERS),
	ABSENT_DRIED_FLOWERS     (false, DRIED_FLOWERS),

	PRESENT_DUST_BUNNIES     (true,  DUST_BUNNIES),
	ABSENT_DUST_BUNNIES      (false, DUST_BUNNIES),

	PRESENT_CHERRY_PITS      (true,  CHERRY_PITS),
	ABSENT_CHERRY_PITS       (false, CHERRY_PITS),

	PRESENT_DOG_DO           (true,  DOG_DO),
	ABSENT_DOG_DO            (false, DOG_DO),

	PRESENT_DRY_PEN          (true,  DRY_PEN),
	ABSENT_DRY_PEN           (false, DRY_PEN),

	PRESENT_EMPTY_BOTTLE     (true,  EMPTY_BOTTLE),
	ABSENT_EMPTY_BOTTLE      (false, EMPTY_BOTTLE),

	PRESENT_EMPTY_BOX        (true,  EMPTY_BOX),
	ABSENT_EMPTY_BOX         (false, EMPTY_BOX),

	PRESENT_EMPTY_CAN        (true,  EMPTY_CAN),
	ABSENT_EMPTY_CAN         (false, EMPTY_CAN),

	PRESENT_FISH_HEAD        (true,  FISH_HEAD),
	ABSENT_FISH_HEAD         (false, FISH_HEAD),

	PRESENT_GRAPEFRUIT_RIND  (true,  GRAPEFRUIT_RIND),
	ABSENT_GRAPEFRUIT_RIND   (false, GRAPEFRUIT_RIND),

	PRESENT_GRASS_CLIPPINGS  (true,  GRASS_CLIPPINGS),
	ABSENT_GRASS_CLIPPINGS   (false, GRASS_CLIPPINGS),

	PRESENT_LOST_FORK        (true,  LOST_FORK),
	ABSENT_LOST_FORK         (false, LOST_FORK),

	PRESENT_LOST_KNIFE       (true,  LOST_KNIFE),
	ABSENT_LOST_KNIFE        (false, LOST_KNIFE),

	PRESENT_LOST_SPOON       (true,  LOST_SPOON),
	ABSENT_LOST_SPOON        (false, LOST_SPOON),

	PRESENT_MOLDY_BREAD      (true,  MOLDY_BREAD),
	ABSENT_MOLDY_BREAD       (false, MOLDY_BREAD),

	PRESENT_POTATO_PEEL      (true,  POTATO_PEEL),
	ABSENT_POTATO_PEEL       (false, POTATO_PEEL),

	PRESENT_RANCID_BUTTER    (true,  RANCID_BUTTER),
	ABSENT_RANCID_BUTTER     (false, RANCID_BUTTER),

	PRESENT_SNOTTY_TISSUE    (true,  SNOTTY_TISSUE),
	ABSENT_SNOTTY_TISSUE     (false, SNOTTY_TISSUE),

	PRESENT_SOILED_DIAPER    (true,  SOILED_DIAPER),
	ABSENT_SOILED_DIAPER     (false, SOILED_DIAPER),

	PRESENT_SPOILED_LEFTOVERS(true,  SPOILED_LEFTOVERS),
	ABSENT_SPOILED_LEFTOVERS (false, SPOILED_LEFTOVERS),

	PRESENT_SPOILED_MILK     (true,  SPOILED_MILK),
	ABSENT_SPOILED_MILK      (false, SPOILED_MILK),

	PRESENT_WEEDS            (true,  WEEDS),
	ABSENT_WEEDS             (false, WEEDS);

	public final boolean present;
	public final GarbageItem garbage;

	private GarbageItemState(
		final boolean present,
		final GarbageItem garbage
	) {
		this.present = present;
		this.garbage = garbage;
	}
}
