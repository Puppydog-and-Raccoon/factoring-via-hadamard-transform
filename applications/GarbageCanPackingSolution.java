package applications;

import consistency.Utility;
import consistency.SimpleHashSet;

/**
 * Describes a garbage can packing solution.
 */
public class GarbageCanPackingSolution {
	/**
	 * The solution.
	 * An array indicates which garbage cans to select.
	 * a null indicates no solution found.
	 */
	public final boolean[]  garbageCanSelections;

	/**
	 * The internal constructor.
	 * 
	 * @param garbageCanSelections the solution, null means no solution found
	 */
	private GarbageCanPackingSolution(final boolean[] garbageCanSelections) {
		this.garbageCanSelections = garbageCanSelections;
	}

	/**
	 * Factory for solution found.
	 * 
	 * @param garbageCanSelections an array indicating the garbage cans to choose.
	 * @return the garbage can packing solution
	 */
	public static GarbageCanPackingSolution makeSolutionFound(final boolean[] garbageCanSelections) {
		return new GarbageCanPackingSolution(garbageCanSelections);
	}

	/**
	 * Factory for no solution found.
	 * 
	 * @return the garbage can packing solution
	 */
	public static GarbageCanPackingSolution makeNoSolutionFound() {
		return new GarbageCanPackingSolution(null);
	}

	@Override
	public String toString() {
		return "GarbageCanPackingSolution ["
			 + "garbageCanSelections=" + Utility.toString(garbageCanSelections)
			 + "]";
	}

	/**
	 * Computes whether this solution solves the given problem.
	 * No solution found is always valid.
	 * Solutions must consider the correct number of garbage cans,
	 * must choose the correct number of garbage cans,
	 * and chosen garbage cans must contains no items in common.
	 * 
	 * @param garbageCanPackingProblem the given problem
	 * @return whether this solution is valid
	 */
	public boolean isValid(GarbageCanPackingProblem garbageCanPackingProblem) {
		return garbageCanSelections == null
			|| garbageCanSelections.length == garbageCanPackingProblem.garbageCansAndTheirItems.length
			&& Utility.population(garbageCanSelections) == garbageCanPackingProblem.numberOfGarbageCansToChoose
			&& selectionsAreIndependent(garbageCanPackingProblem.garbageCansAndTheirItems, garbageCanSelections);
	}

	/**
	 * Compute whether the chosen garbage cans contain any items in common.
	 * 
	 * @param garbageCansAndTheirItems the garbage cans and their items
	 * @param garbageCanSelections the garbage can selections
	 * @return whether any 2 selected garbage cans contain the same item
	 */
	private boolean selectionsAreIndependent(final String[][] garbageCansAndTheirItems, final boolean[] garbageCanSelections) {
		final SimpleHashSet<String> selectedItems = new SimpleHashSet<String>();
		for(int i = 0; i < garbageCansAndTheirItems.length; i++) {
			if(garbageCanSelections[i]) {
				for(int j = 0; j < garbageCansAndTheirItems[i].length; j++) {
					if(!selectedItems.add(garbageCansAndTheirItems[i][j])) {
						System.out.println("* duplicate item " + i + " " + j + " " + garbageCansAndTheirItems[i][j]);
						return false;
					}
				}
			}
		}
		return true;
	}
}
