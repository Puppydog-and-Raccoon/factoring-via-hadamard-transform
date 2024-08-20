package consistency;

// * isRoot and isLeaf are mainly used for debugging and extracting, remove eventually
// * domains are used are mainly used for debugging or where they can be generated
// * domains can be computed without conditional logic or they can be stored
// * property node could be 1 32-bit integer: 5 bits for the tier and 27 bits for the term
// * that would be plenty of precision to factor numbers with 2k bits

final class PropertyNode {
	final int     nodeTier;
	final int     nodeTerm;
	final boolean isRoot;
	final boolean isLeaf;
    final Domain  hadamardDomain;
	final Domain  populationDomain;
	final Domain  spineDomain;

	PropertyNode(
		final int             nodeTier,
		final int             nodeTerm,
		final DomainGenerator domainGenerator,
		final int             numberOfNodeTiers
	) {
		final int hadamardNodeTerm   = nodeTerm;
		final int populationNodeTerm = Utility.populationNodeTerm(nodeTier, nodeTerm);
		final int spineNodeTerm      = Utility.spineNodeTerm(nodeTier, nodeTerm);

		this.nodeTier         = nodeTier;
		this.nodeTerm         = nodeTerm;
		this.isRoot           = nodeTier == 0;
		this.isLeaf           = nodeTier == numberOfNodeTiers - 1;
		this.hadamardDomain   = domainGenerator.domainForNode(nodeTier, hadamardNodeTerm);
		this.populationDomain = domainGenerator.domainForNode(nodeTier, populationNodeTerm);
		this.spineDomain      = domainGenerator.domainForNode(nodeTier, spineNodeTerm);
	}

	@Override
	public String toString() {
		return "<pn " + nodeTier + " " + nodeTerm + ">";
	}
}
