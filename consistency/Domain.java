package consistency;

class Domain {
    final short minimum;
	final short maximum;
	final short stride;

	private static Canonicalizer<Domain> canonicalizer = new Canonicalizer<Domain>();

	private Domain(
		final int minimum,
		final int maximum,
		final int stride
	) {
		Utility.insistIsPowerOfTwo(stride);

		this.minimum = Utility.toShort(minimum);
		this.maximum = Utility.toShort(maximum);
		this.stride  = Utility.toShort(stride);
	}

	// -----------------------------------------------------------------------
	// access functions

	boolean isInDomain(
		final int value
	) {
		return minimum <= value && value <= maximum && (value - minimum) % stride == 0;
	}

	int[] enumerate() {
		return Utility.enumerateAscending(minimum, maximum, stride);
	}

	PopulationDelta[] parentPopulationDeltas(
		final int childPopulation
	) {
		final int smallestParentPopulation       = Math.max(minimum, childPopulation - maximum);
		final int largestParentPopulation        = Math.min(maximum, childPopulation - minimum);
		final int numberOfParentPopulationDeltas = Math.max(0, largestParentPopulation - smallestParentPopulation + 1);

		final PopulationDelta[] parentPopulationDeltas = new PopulationDelta[numberOfParentPopulationDeltas];
		for(final int i : Utility.enumerateAscending(numberOfParentPopulationDeltas)) {
			parentPopulationDeltas[i] = PopulationDelta.make(smallestParentPopulation + i, largestParentPopulation - i);
		}
		return parentPopulationDeltas;
	}

	// -----------------------------------------------------------------------
	// factory functions

	static Domain makeStandardDomain(
		final int nodeTier,
		final int nodeTerm
	) {
//		Utility.insist(false, "do not use this domain until better understood");

		if(isAnyPopulationNode(nodeTier, nodeTerm)) {
			final int bound = 1 << nodeTier;
			return make(0, bound, 1);
		} else {
			final int bound = 1 << (nodeTier - 1);
			return make(-bound, bound, 1);
		}
	}

	// ERROR: fix the population node of the whole butterfly
	static Domain makeBoundedDomain(
		final int nodeTier,
		final int nodeTerm,
		final int numberOfTruesInProblem,
		final int numberOfDecisionsInProblem
	) {
//		Utility.insist(false, "do not use this domain until better understood");

		if(isThePopulationNode(nodeTier, nodeTerm, numberOfDecisionsInProblem)) {
			final int bound = numberOfTruesInProblem;
			return make(bound, bound, 1);
		} else if(isAnyPopulationNode(nodeTier, nodeTerm)) {
			final int bound = Math.min(1 << nodeTier,  numberOfTruesInProblem);
			return make(0, bound, 1);
		} else {
			final int bound = Math.min(1 << (nodeTier - 1), numberOfTruesInProblem);
			return make(-bound, bound, 1);
		}
	}

	static Domain makeGroupDomain(
		final int nodeTier,
		final int nodeTerm,
		final int numberOfGroupTiers
	) {
		if(nodeTier < numberOfGroupTiers) {
			return isAnyPopulationNode(nodeTier, nodeTerm)      ? make( 0, 1, 1)
				 :                                                make(-1, 1, 1);
		} else {
			final int bound = 1 << (nodeTier - numberOfGroupTiers);
			return isAnyPopulationNode(nodeTier, nodeTerm)      ? make( bound, bound, 2)
				 : isShadowedNode(nodeTerm, numberOfGroupTiers) ? make(     0,     0, 2)
				 :                                                make(-bound, bound, 2);
		}
	}

	// -----------------------------------------------------------------------
	// helpers

	private static Domain make(
		final int minimum,
		final int maximum,
		final int stride
	) {
		return canonicalizer.canonicalize(new Domain(minimum, maximum, stride));
	}

	// this node is the population node of the butterfly
	private static boolean isThePopulationNode(final int nodeTier, final int nodeTerm, final int numberOfDecisionsInProblem) {
		final int leafNodeTier = Utility.log2RoundedUp(numberOfDecisionsInProblem);
		return nodeTier == leafNodeTier && nodeTerm == 0;
	}

	// this node is any population node in the butterfly
	private static boolean isAnyPopulationNode(final int nodeTier, final int nodeTerm) {
		final int lowBitsMask = Utility.lowBitsMask(nodeTier);
		return (nodeTerm & lowBitsMask) == 0;
	}

	// this node lies in the shadow of a population node, presumes this node is not a population node
	static boolean isShadowedNode(final int nodeTerm, final int numberOfGroupTiers) {
		final int lowBitsMask = Utility.lowBitsMask(numberOfGroupTiers);
		return (nodeTerm & lowBitsMask) == 0;
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(minimum, 0)
							   ^ DoubleTabulationHashing.primaryHash(maximum, 1)
							   ^ DoubleTabulationHashing.primaryHash(stride,  2);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& minimum     == ((Domain) otherObject).minimum
			&& maximum     == ((Domain) otherObject).maximum
			&& stride      == ((Domain) otherObject).stride;
	}

	@Override
	public String toString() {
		return "<d " + minimum + " " + maximum + " " + stride + ">";
	}
}
