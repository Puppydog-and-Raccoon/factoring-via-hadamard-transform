package consistency;

// This only works WITHIN a butterfly.
// From butterfly to butterfly, nodes must share hadamard, population, and spine values.

class PopulationDelta {
	final short leftParentPopulation;
	final short rightParentPopulation;

	private static final Canonicalizer<PopulationDelta> canonicalizer = new Canonicalizer<PopulationDelta>();

	private PopulationDelta(
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		this.leftParentPopulation  = Utility.toShort(leftParentPopulation);
		this.rightParentPopulation = Utility.toShort(rightParentPopulation);
	}

	// -----------------------------------------------------------------------
	// factories

	static PopulationDelta make(
		final int leftParentPopulation,
		final int rightParentPopulation
	) {
		return canonicalizer.canonicalize(new PopulationDelta(
			leftParentPopulation,
			rightParentPopulation
		));
	}

	// -----------------------------------------------------------------------
	// accessors

	int childPopulation() {
		return leftParentPopulation + rightParentPopulation;
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public int hashCode() {
		final long primaryHash = DoubleTabulationHashing.primaryHash(leftParentPopulation,  0)
							   ^ DoubleTabulationHashing.primaryHash(rightParentPopulation, 1);
		return DoubleTabulationHashing.secondaryHash(primaryHash);
	}

	@Override
	public boolean equals(
		final Object otherObject
	) {
		return otherObject           != null
			&& getClass()            == otherObject.getClass()
			&& leftParentPopulation  == ((PopulationDelta) otherObject).leftParentPopulation
			&& rightParentPopulation == ((PopulationDelta) otherObject).rightParentPopulation;
	}

	@Override
	public String toString() {
		return "<spd "
			 + "lpp=" + leftParentPopulation  + " "
			 + "rpp=" + rightParentPopulation
			 + ">";
	}
}
