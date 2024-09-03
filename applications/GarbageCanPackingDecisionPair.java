package applications;

import consistency.Utility;

/**
 * Each packing (garbage can and padding) decision will be represented by 2 adjacent consistency decisions.
 * The first is the actual consistency decision and the second is the negated consistency decision.
 * 
 * A garbage can decision is represented by the array of items that it contains.
 * The array may be empty but may not be null.
 * A padding decision is represented by a null array of items.
 */
class GarbageCanPackingDecisionPair {
	/**
	 * non-null means garbage can with array of items, null means padding
	 */
	final GarbageItem[] garbageCanItems;

	/**
	 * index of this decision in decision space
	 */
	final int decisionId;

	/**
	 * Internal constructor.
	 * 
	 * @param garbageCanItems non-null represents a garbage can with the specified items and null represents padding
	 * @param decisionId the index of the first decision in the consistency vector
	 */
	private GarbageCanPackingDecisionPair(
		final GarbageItem[] garbageCanItems,
		final int       decisionId
	) {
		this.garbageCanItems = garbageCanItems;
		this.decisionId      = decisionId;
	}

	/**
	 * Determines whether this decision represents a garbage can containing the specified item.
	 * 
	 * @param item the item of interest
	 * @return whether the condition is true
	 */
	boolean concernsGarbageCanContaining(final GarbageItem item) {
		return Utility.contains(item, garbageCanItems);
	}

	/**
	 * Determines whether this decision represent a garbage can decision.
	 * 
	 * @return whether the condition is true
	 */
	boolean concernsGarbageCan() {
		return garbageCanItems != null;
	}

	/**
	 * Determines whether this decision represents a padding decision.
	 * 
	 * @return whether the condition is true
	 */
	boolean concernsPadding() {
		return garbageCanItems == null;
	}

	/**
	 * Create a decision pair that embodies a garbage can containing the specified items.
	 * 
	 * @param items
	 * @param packingDecisionIndex
	 * @return the new decision pair
	 */
	static GarbageCanPackingDecisionPair makeGarbageCan(final GarbageItem[] items, final int packingDecisionIndex) {
		Utility.insist(items != null, "items must not be null");
		return new GarbageCanPackingDecisionPair(items, 2 * packingDecisionIndex);
	}

	/**
	 * Create a decision pair that embodies padding.
	 * 
	 * @param packingDecisionIndex the index of the pair in packing space
	 * @return the new decision pair
	 */
	static GarbageCanPackingDecisionPair makePadding(final int packingDecisionIndex) {
		return new GarbageCanPackingDecisionPair(null, 2 * packingDecisionIndex);
	}
}
