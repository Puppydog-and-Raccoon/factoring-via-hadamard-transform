package consistency;

public class PositionButterfly {
	final int          numberOfTiers;
	final int          numberOfTerms;
	final Position[][] positions;

	PositionButterfly(int numberOfDecisions) {
		this.numberOfTiers = Utility.log2(numberOfDecisions) + 1;
		this.numberOfTerms = numberOfDecisions;
		this.positions     = new Position[numberOfTiers][numberOfTerms];
		for(int tier = 0; tier < numberOfTiers; tier++) {
			for(int term = 0; term < numberOfTerms; term++) {
				this.positions[tier][term] = new Position(tier, term, numberOfTerms);
			}
		}
	}
}
