package consistency;

final class PropertyButterfly {
	final int              numberOfNodeTiers;
	final int              numberOfNodeTerms;
	final int              numberOfBoxTiers;
	final int              numberOfBoxTerms;
	final int              rootNodeTier;
	final int              leafNodeTier;
	final int              rootBoxTier;
	final int              leafBoxTier;
	final PropertyNode[][] propertyNodes;
	final PropertyBox[][]  propertyBoxes;
	final int[]            nodeTierIndicesTopDown;
	final int[]            nodeTierIndicesBottomUp;
	final int[]            nodeTermIndicesForward;
	final int[]            nodeTermIndicesReverse;
	final int[]            boxTierIndicesTopDown;
	final int[]            boxTierIndicesBottomUp;
	final int[]            boxTermIndicesForward;
	final int[]            boxTermIndicesReverse;

	private PropertyButterfly(
		final int             numberOfDecisionsInProblem,
		final DomainGenerator domainGenerator
	) {
		Utility.insistIsPowerOfTwo(numberOfDecisionsInProblem);
		Utility.insist(numberOfDecisionsInProblem >= 2, "numberOfDecisions must be at least 2");

		final int              numberOfNodeTerms = Utility.numberOfNodeTerms(numberOfDecisionsInProblem);
		final int              numberOfNodeTiers = Utility.numberOfNodeTiers(numberOfDecisionsInProblem);
		final int              numberOfBoxTiers  = numberOfNodeTiers - 1;
		final int              numberOfBoxTerms  = numberOfNodeTerms / 2;
		final PropertyNode[][] propertyNodes     = newNodes(numberOfNodeTiers, numberOfNodeTerms, domainGenerator);
		final PropertyBox[][]  propertyBoxes     = newBoxes(numberOfBoxTiers, numberOfBoxTerms, propertyNodes);

		this.numberOfNodeTiers       = numberOfNodeTiers;
		this.numberOfNodeTerms       = numberOfNodeTerms;
		this.numberOfBoxTiers        = numberOfBoxTiers;
		this.numberOfBoxTerms        = numberOfBoxTerms;
		this.rootNodeTier            = 0;
		this.leafNodeTier            = numberOfNodeTiers - 1;
		this.rootBoxTier             = 0;
		this.leafBoxTier             = numberOfBoxTiers - 1;
		this.propertyNodes           = propertyNodes;
		this.propertyBoxes           = propertyBoxes;
		this.nodeTierIndicesTopDown  = Utility.enumerateAscending(numberOfNodeTiers);
		this.nodeTierIndicesBottomUp = Utility.enumerateDescending(numberOfNodeTiers);
		this.nodeTermIndicesForward  = Utility.enumerateAscending(numberOfNodeTerms);
		this.nodeTermIndicesReverse  = Utility.enumerateDescending(numberOfNodeTerms);
		this.boxTierIndicesTopDown   = Utility.enumerateAscending(numberOfBoxTiers);
		this.boxTierIndicesBottomUp  = Utility.enumerateDescending(numberOfBoxTiers);
		this.boxTermIndicesForward   = Utility.enumerateAscending(numberOfBoxTerms);
		this.boxTermIndicesReverse   = Utility.enumerateDescending(numberOfBoxTerms);
	}

	static PropertyButterfly make(
		final ConsistencyProblem consistencyProblem
	) {
		final int             numberOfDecisionsInProblem = consistencyProblem.numberOfDecisionsInProblem;
		final DomainGenerator domainGenerator            = consistencyProblem.domainGenerator;
		return new PropertyButterfly(numberOfDecisionsInProblem, domainGenerator);
	}

	static PropertyButterfly make(
		final int             numberOfDecisionsInProblem,
		final DomainGenerator domainGenerator
	) {
		return new PropertyButterfly(numberOfDecisionsInProblem, domainGenerator);
	}

	// ERROR: restore
	int[] nodeTermIndicesRandomly() {
		return Utility.enumerateAscending(numberOfNodeTerms);
	}

	// ERROR: restore
	int[] boxTermIndicesRandomly() {
		return Utility.enumerateAscending(numberOfBoxTerms);		
	}

	private static PropertyNode[][] newNodes(
		final int             numberOfNodeTiers,
		final int             numberOfNodeTerms,
		final DomainGenerator actualDomainGenerator
	) {
		final PropertyNode[][] propertyNodes = new PropertyNode[numberOfNodeTiers][numberOfNodeTerms];
		for(final int nodeTier : Utility.enumerateAscending(numberOfNodeTiers)) {
			for(final int nodeTerm : Utility.enumerateAscending(numberOfNodeTerms)) {
				propertyNodes[nodeTier][nodeTerm] = new PropertyNode(nodeTier, nodeTerm, actualDomainGenerator, numberOfNodeTiers);
			}
		}
		return propertyNodes;
	}

	private static PropertyBox[][] newBoxes(
		final int              numberOfBoxTiers,
		final int              numberOfBoxTerms,
		final PropertyNode[][] propertyNodes
	) {
		final PropertyBox[][] propertyBoxes = new PropertyBox[numberOfBoxTiers][numberOfBoxTerms];
		for(final int boxTier : Utility.enumerateAscending(numberOfBoxTiers)) {
			for(final int boxTerm : Utility.enumerateAscending(numberOfBoxTerms)) {
				propertyBoxes[boxTier][boxTerm] = new PropertyBox(boxTier, boxTerm, propertyNodes);
			}
		}
		return propertyBoxes;
	}

	@Override
	public String toString() {
		return "<PropertyButterfly"                     + " "
			 + "numberOfNodeTiers=" + numberOfNodeTiers + " "
			 + "numberOfNodeTerms=" + numberOfNodeTerms + " "
			 + "numberOfBoxTiers="  + numberOfBoxTiers  + " "
			 + "numberOfBoxTerms="  + numberOfBoxTerms  + " "
			 + "rootNodeTier="      + rootNodeTier      + " "
			 + "leafNodeTier="      + leafNodeTier      + " "
			 + "rootBoxTier="       + rootBoxTier       + " "
			 + "leafBoxTier="       + leafBoxTier
//				+ ", " + "propertyNodes="           + propertyNodes
//				+ ", " + "propertyBoxes="           + propertyBoxes
//				+ ", " + "nodeTierIndicesTopDown="  + nodeTierIndicesTopDown
//				+ ", " + "nodeTierIndicesBottomUp=" + nodeTierIndicesBottomUp
//				+ ", " + "nodeTermIndices="         + nodeTermIndices
//				+ ", " + "boxTierIndicesTopDown="   + boxTierIndicesTopDown
//				+ ", " + "boxTierIndicesBottomUp="  + boxTierIndicesBottomUp
//				+ ", " + "boxTermIndicesForward="   + boxTermIndicesForward
//				+ ", " + "boxTermIndicesReverse="   + boxTermIndicesReverse
			+ ">";
	}
}
