package applications;

import consistency.Utility;

public class GarbageCanPackingProblem {
	public static final String BAD_NUMBER_OF_GARBAGE_CANS_TO_CHOOSE = "bad number of garbage cans to choose";

	public final String[][] garbageCansAndTheirItems;
	public final int        numberOfGarbageCansToChoose;

	public GarbageCanPackingProblem(final String[][] garbageCansAndTheirItems, final int numberOfGarbageCansToChoose) {
		this.garbageCansAndTheirItems    = Utility.copy(garbageCansAndTheirItems);
		this.numberOfGarbageCansToChoose = numberOfGarbageCansToChoose;
	}

	public boolean isValid() {
		return 0 <= numberOfGarbageCansToChoose && numberOfGarbageCansToChoose <= garbageCansAndTheirItems.length;
	}
}
