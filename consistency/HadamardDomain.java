package consistency;

public class HadamardDomain {
    public final int minimum;
	public final int maximum;
	public final int stride;

	private static Unique<HadamardDomain> unique = new Unique<HadamardDomain>();

	private HadamardDomain(
		final int minimum,
		final int maximum,
		final int stride
	) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.stride  = stride;
	}

	public static HadamardDomain newDefault(
		final int tier,
		final int term
	) {
		return unique.unique(new HadamardDomain(
			defaultMinimumForNode(tier, term),
			defaultMaximumForNode(tier, term),
			1
		));
	}

	public static HadamardDomain newSpecific(
		final int minimum,
		final int maximum
	) {
		return unique.unique(new HadamardDomain(
			minimum,
			maximum,
			1
		));
	}

	public static HadamardDomain newSpecific(
		final int minimum,
		final int maximum,
		final int stride
	) {
		return unique.unique(new HadamardDomain(
			minimum,
			maximum,
			stride
		));
	}

	public boolean isInDomain(
		final int hadamard
	) {
		return minimum <= hadamard && hadamard <= maximum;
	}

	public int[] enumerate() {
		return Utility.enumerateAscending(minimum, maximum, stride);
	}

	private static int defaultMinimumForNode(
		final int nodeTier,
		final int nodeTerm
	) {
		return isPopulation(nodeTier, nodeTerm)
			 ? 0
			 : -(1 << (nodeTier - 1));
	}

	private static int defaultMaximumForNode(
		final int nodeTier,
		final int nodeTerm
	) {
		return isPopulation(nodeTier, nodeTerm)
			 ? 1 << nodeTier
			 : (1 << (nodeTier - 1));
	}

	private static boolean isPopulation(
		final int nodeTier,
		final int nodeTerm
	) {
		final int mask = Utility.lowestNBitsSet(nodeTier);
		return (nodeTerm & mask) == 0;
	}

	public static HadamardDomain[][] newHadamardDomains(
		final int numberOfTruesInProblem,
		final int numberOfDecisionsInProblem
	) {
		final int numberOfTerms = Utility.numberOfNodeTerms(numberOfDecisionsInProblem);
		final int numberOfTiers = Utility.numberOfNodeTiers(numberOfDecisionsInProblem);

		final HadamardDomain[][] hadamardDomains = new HadamardDomain[numberOfTiers][numberOfTerms];
		for(int nodeTier = 0; nodeTier < numberOfTiers; nodeTier++) {
			for(int nodeTerm = 0; nodeTerm < numberOfTerms; nodeTerm++) {
				hadamardDomains[nodeTier][nodeTerm] = newSpecific(
					Math.max(defaultMinimumForNode(nodeTier, nodeTerm), -numberOfTruesInProblem),
					Math.min(defaultMaximumForNode(nodeTier, nodeTerm),  numberOfTruesInProblem)
				);
			}
		}
		return hadamardDomains;
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(maximum,  0)
			 ^ Integer.rotateLeft(minimum, 16);
	}

	@Override
	public boolean equals(
		Object otherObject
	) {
		return otherObject != null
			&& getClass()  == otherObject.getClass()
			&& maximum     == ((HadamardDomain) otherObject).maximum
			&& minimum     == ((HadamardDomain) otherObject).minimum;
	}
}
