package applications;

import java.util.HashSet;

import consistency.Utility;

public class GarbageCanPackingSolution {
	public final boolean[]  garbageCanSelections;

	private GarbageCanPackingSolution(final boolean[] garbageCanSelections) {
		this.garbageCanSelections = garbageCanSelections;
	}

	public static GarbageCanPackingSolution makeSolutionFound(final boolean[] garbageCanSelections) {
		return new GarbageCanPackingSolution(garbageCanSelections);
	}

	public static GarbageCanPackingSolution makeNoSolutionFound() {
		return new GarbageCanPackingSolution(null);
	}

	@Override
	public String toString() {
		return "GarbageCanPackingSolution ["
			 + "garbageCanSelections=" + Utility.toString(garbageCanSelections)
			 + "]";
	}

	public boolean isValid(GarbageCanPackingProblem garbageCanPackingProblem) {
		return garbageCanSelections == null
			|| garbageCanSelections.length == garbageCanPackingProblem.garbageCansAndTheirItems.length
			&& Utility.population(garbageCanSelections) == garbageCanPackingProblem.numberOfGarbageCansToChoose
			&& selectionsAreIndependent(garbageCanPackingProblem.garbageCansAndTheirItems, garbageCanSelections);
	}

	private boolean selectionsAreIndependent(final String[][] garbageCansAndTheirItems, final boolean[] garbageCanSelections) {
		final HashSet<String> selectedItems = new HashSet<String>();
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
