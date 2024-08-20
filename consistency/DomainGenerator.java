package consistency;

/**
 * Define the interface for a function that creates all of the domains for a propery butterfly.
 * Also, define three standard domain generators and two domain generator factories.
 * 
 * <p>
 * The GROUP generators are the only ones we recommend using.
 * All other generators are used for experimenting and testing.
 */
public interface DomainGenerator {
    /**
     * Generate a domain for the given node.
     * 
     * @param nodeTier the tier of the given butterfly node
     * @param nodeTerm the term of the given butterfly node
     * @return the domain for the given butterfly node
     */
	public Domain domainForNode(final int nodeTier, final int nodeTerm);

    /**
     * The standard domain generator for any binary vector.
     * This was originally used by all applications.
     * This is mainly used for testing.
     */
	public static DomainGenerator STANDARD = (nodeTier, nodeTerm) -> Domain.makeStandardDomain(nodeTier, nodeTerm);

    /**
     * One of every pair of decisions must be true.
     * This is used by garbage can packing.
     */
	public static DomainGenerator BINARY_GROUP = (nodeTier, nodeTerm) -> Domain.makeGroupDomain(nodeTier, nodeTerm, 1);

	/**
     * One of every sixteen decisions must be true.
     * This is used by factoring.
     */
	public static DomainGenerator HEXADECIMAL_GROUP = (nodeTier, nodeTerm) -> Domain.makeGroupDomain(nodeTier, nodeTerm, 4);

    /**
     * Make a bounded domain generator.
     * Exactly n of the decisions in the problem will be set to true.
     * 
	 * <p>
     * Hadamard values must lie in the domain -n to n, as long as
     * n is less than or equal to half the number of decisions in the problem.
     * 
     * @param numberOfTruesInProblem the number of trues in the problem
     * @param numberOfDecisionsInProblem the number of decisions in the problem, to determine the leaf tier
     * @return the domain generator
     */
	public static DomainGenerator boundedDomainGenerator(
		final int numberOfTruesInProblem,
		final int numberOfDecisionsInProblem
	) {
		Utility.insistIsPowerOfTwo(numberOfDecisionsInProblem);
		Utility.insist(0 <= numberOfTruesInProblem, "number of trues in problem must be zero or positive");
		Utility.insist(numberOfTruesInProblem <= numberOfDecisionsInProblem / 2, "number of trues must be less than or equal to half the number of decisions");

		return (nodeTier, nodeTerm) -> Domain.makeBoundedDomain(nodeTier, nodeTerm, numberOfTruesInProblem, numberOfDecisionsInProblem);
	}

    /**
     * Make a group domain generator.
	 * Exactly one decision in each group will be set to true.
	 * 
	 * <p>
	 * The top log2(n) tiers only contain hadamard values related to 0 and 1.
	 * The remaining tiers only contain hadamard values related to the scale of the tier.
     * 
     * @param numberOfDecisionsInEachGroup the size of each group, must be a power of two greater than one
     * @return the domain generator
     */
	public static DomainGenerator groupDomainGenerator(
		final int numberOfDecisionsInEachGroup
	) {
		Utility.insistIsPowerOfTwo(numberOfDecisionsInEachGroup);
		Utility.insist(numberOfDecisionsInEachGroup > 1, "groups must contain more than one decision");

		final int numberOfTiersInEachGroup = Utility.log2RoundedUp(numberOfDecisionsInEachGroup);
		return (nodeTier, nodeTerm) -> Domain.makeGroupDomain(nodeTier, nodeTerm, numberOfTiersInEachGroup);
	}
}
