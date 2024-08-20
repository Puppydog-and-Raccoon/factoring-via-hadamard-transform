package consistency;

/**
 * Objects contain the hash sets that are shared among the boxes within each linking butterfly.
 */
class SharedWithinLinkingButterfly {
	private final Object[][] spinePartialSumsDeltasHashSets;
	private final Object[][] populationDeltasHashSets;

	// -----------------------------------------------------------------------
	// visible methods

	SharedWithinLinkingButterfly(
		final PropertyButterfly propertyButterfly
	) {
		this.spinePartialSumsDeltasHashSets = makeSpinePartialSumsDeltasHashSets(propertyButterfly);
		this.populationDeltasHashSets       = makePopulationDeltasHashSets(propertyButterfly);
	}

	@SuppressWarnings("unchecked")
	SimpleHashSet<SpinePartialSumsDelta> getSpinePartialSumsDeltasHashSet(final PropertyBox propertyBox) {
		return (SimpleHashSet<SpinePartialSumsDelta>) spinePartialSumsDeltasHashSets[propertyBox.boxTier][propertyBox.spinePartialSumTreeBoxTerm];
	}

	@SuppressWarnings("unchecked")
	SimpleHashSet<PopulationDelta> getPopulationDeltasHashSet(final PropertyBox propertyBox) {
		return (SimpleHashSet<PopulationDelta>) populationDeltasHashSets[propertyBox.boxTier][propertyBox.populationTreeBoxTerm];
	}

	// -----------------------------------------------------------------------
	// helpers

	private static Object[][] makeSpinePartialSumsDeltasHashSets(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][] hashSetTree = new Object[propertyButterfly.numberOfBoxTiers][];
		for(final int hashSetTier : propertyButterfly.boxTierIndicesTopDown) {
			final int numberOfHashSetsInTier = 1 << hashSetTier;
			hashSetTree[hashSetTier] = new Object[numberOfHashSetsInTier];
			for(final int hashSetTerm : Utility.enumerateAscending(numberOfHashSetsInTier)) {
				hashSetTree[hashSetTier][hashSetTerm] = new SimpleHashSet<>();
			}
		}
		return hashSetTree;
	}

	private static Object[][] makePopulationDeltasHashSets(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][] hashSetTree = new Object[propertyButterfly.numberOfBoxTiers][];
		for(final int hashSetTier : propertyButterfly.boxTierIndicesTopDown) {
			final int numberOfHashSetsInTier = propertyButterfly.numberOfBoxTerms >> hashSetTier;
			hashSetTree[hashSetTier] = new Object[numberOfHashSetsInTier];
			for(final int hashSetTerm : Utility.enumerateAscending(numberOfHashSetsInTier)) {
				hashSetTree[hashSetTier][hashSetTerm] = new SimpleHashSet<>();
			}
		}
		return hashSetTree;
	}
}
