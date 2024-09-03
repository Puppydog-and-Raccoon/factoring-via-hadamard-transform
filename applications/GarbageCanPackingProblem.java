package applications;

import consistency.ConsistencyProblem;
import consistency.ConsistencySolution;
import consistency.Utility;
import consistency.SimpleHashSet;

/**
 * Describes a garbage can packing problem.
 */
public class GarbageCanPackingProblem {
	public static final String BAD_NUMBER_OF_GARBAGE_CANS_TO_CHOOSE = "bad number of garbage cans to choose";

	public final GarbageItem[][] garbageCansAndTheirItems;
	public final int         numberOfGarbageCansToChoose;
	public final boolean[][] solutions;   // used for debugging. can be null
	public final GarbageItem[]   uniqueItems;

	/**
	 * The constructor
	 * 
	 * @param garbageCansAndTheirItems an array of garbage cans, each entry is an array of items
	 * @param numberOfGarbageCansToChoose the number of garbage cans to choose
	 * @param testSolutions solutions, used for testing, generally will be null
	 */
	public GarbageCanPackingProblem(
		final GarbageItem[][] garbageCansAndTheirItems,
		final int         numberOfGarbageCansToChoose,
		final boolean[][] testSolutions
	) {
		this.garbageCansAndTheirItems    = Utility.deepCopy(garbageCansAndTheirItems);
		this.numberOfGarbageCansToChoose = numberOfGarbageCansToChoose;
		this.solutions                   = testSolutions;
		this.uniqueItems                 = uniqueItems(garbageCansAndTheirItems);
	}

	/**
	 * Computes whether this problem is valid.
	 * THe number of garbage cans to choose must be zero or positive and no larger than the number of garbage cans.
	 * 
	 * @return whether the problem is valid
	 */
	public boolean isValid() {
		return 0 <= numberOfGarbageCansToChoose && numberOfGarbageCansToChoose <= garbageCansAndTheirItems.length;
	}

	/**
	 * Convert the garbage can packing problem into a garbage can packing solution.
	 * 
	 * @return the garbage can packing solution.
	 */
	public GarbageCanPackingSolution solve() {
		Utility.insist(isValid(), "invalid garbage can packing problem");
	
		final GarbageCanPackingInternals garbageCanPackingInternals = new GarbageCanPackingInternals(this);
	
		final ConsistencyProblem consistencyProblem = garbageCanPackingInternals.makeConsistencyProblem();
		Utility.insist(consistencyProblem.isValid(), "invalid consistency problem");
	
		final ConsistencySolution consistencySolution = consistencyProblem.solve();
		Utility.insist(consistencySolution.isValid(), "invalid consistency solution");
	
		final GarbageCanPackingSolution garbageCanPackingSolution = garbageCanPackingInternals.makeGarbageCanPackingSolution(consistencySolution);
		Utility.insist(garbageCanPackingSolution.isValid(this), "invalid garbage can packing solution");
	
		return garbageCanPackingSolution;
	}

	/**
	 * Extract an array of the unique items in all garbage cans.
	 * 
	 * @param garbageCansAndTheirItems the items in each garbage can
	 * @return the array of unique items
	 */
	private static GarbageItem[] uniqueItems(GarbageItem[][] garbageCansAndTheirItems) {
		final SimpleHashSet<GarbageItem> uniqueItems = new SimpleHashSet<GarbageItem>();
		for(final GarbageItem[] garbageCan : garbageCansAndTheirItems) {
			for(final GarbageItem item : garbageCan) {
				uniqueItems.add(item);
			}
		}
		return uniqueItems.toArray(new GarbageItem[0]);
	}
}
