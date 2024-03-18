package consistency;

// Notes
// * isRoot and isLeaf are mainly used for debugging and extract, remove eventually
// * hadamard domains can be computed without conditional logic or they can be stored
// * Position node could be 1 32-bit integer: 5 bits for the tier and 27 bits for the term

// TODO: possibly store is root and is leaf???

final class PositionNode {
	final int              nodeTier;
	final int              nodeTerm;
	final HadamardDomain   hadamardDomain;
	final PositionNode[][] positionNodes;

	PositionNode(
		final int                nodeTier,
		final int                nodeTerm,
		final HadamardDomain[][] hadamardDomains,
		final PositionNode[][]   positionNodes
	) {
		this.nodeTier       = nodeTier;
		this.nodeTerm       = nodeTerm;
		this.hadamardDomain = hadamardDomains == null
							? HadamardDomain.newDefault(nodeTier, nodeTerm)
							: hadamardDomains[nodeTier][nodeTerm];
		this.positionNodes  = positionNodes;
	}

	boolean isRoot() {
		return nodeTier == 0;
	}

	boolean isLeaf() {
		return nodeTier == positionNodes.length - 1;
	}

	@Override
	public String toString() {
		return "<PositionNode " + nodeTier + " " + nodeTerm + ">";
	}

	// note that both parents should always have the same domain!!!
	// ERROR: is this the parent domain???
	boolean wouldMakeValidParentHadamards(final int leftChildHadamard, final int rightChildHadamard) {
		return (leftChildHadamard & 1) == (rightChildHadamard & 1)
			&& hadamardDomain.isInDomain((leftChildHadamard + rightChildHadamard) / 2)
			&& hadamardDomain.isInDomain((leftChildHadamard - rightChildHadamard) / 2);
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject     != null
			&& getClass()      == otherObject.getClass()
			&& nodeTier        == ((PositionNode) otherObject).nodeTier
			&& nodeTerm        == ((PositionNode) otherObject).nodeTerm
			&& hadamardDomain  == ((PositionNode) otherObject).hadamardDomain;
	}

	// TODO: rename
	public int[] canonicalPopulations() {
		final int minimumCanonicalPopulation = 0;
		final int maximumCanonicalPopulation = 1 << nodeTier;
		return Utility.enumerateAscending(minimumCanonicalPopulation, maximumCanonicalPopulation);
	}
}
