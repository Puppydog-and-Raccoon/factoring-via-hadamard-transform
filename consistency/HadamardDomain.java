package consistency;

// TODO: rename fields like hadamardMinimum and populationMinimum
// TODO: add fields like spineMinimum

public class HadamardDomain {
    public final int minimum;
	public final int maximum;
	public final int stride;
	public final int populationMinimum;
	public final int populationMaximum;
	public final int populationStride;

	private static Unique<HadamardDomain> unique = new Unique<HadamardDomain>();

	private HadamardDomain(
		final int hadamardMinimum,
		final int hadamardMaximum,
		final int hadamardStride,
		final int populationMinimum,
		final int populationMaximum,
		final int populationStride
	) {
		this.minimum           = hadamardMinimum;
		this.maximum           = hadamardMaximum;
		this.stride            = hadamardStride;
		this.populationMinimum = populationMinimum;
		this.populationMaximum = populationMaximum;
		this.populationStride  = populationStride;
	}

	public static HadamardDomain newDefault(
		final int tier,
		final int term
	) {
		return unique.unique(new HadamardDomain(
			defaultMinimumForNode(tier, term),
			defaultMaximumForNode(tier, term),
			1,
			0,
			1 << tier,
			1
		));
	}

	public static HadamardDomain newSpecific(
		final int minimum,
		final int maximum,
		final int stride,
		final int canonicalMinimum,
		final int canonicalMaximum,
		final int canonicalStride
	) {
		return unique.unique(new HadamardDomain(
			minimum,
			maximum,
			stride,
			canonicalMinimum,
			canonicalMaximum,
			canonicalStride
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

	public static int defaultMinimumForNode(
		final int nodeTier,
		final int nodeTerm
	) {
		return isPopulation(nodeTier, nodeTerm)
			 ? 0
			 : -(1 << (nodeTier - 1));
	}

	public static int defaultMaximumForNode(
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
					Math.min(defaultMaximumForNode(nodeTier, nodeTerm),  numberOfTruesInProblem),
					1,
					0,
					Math.min(1 << nodeTier, numberOfTruesInProblem),
					1
				);
			}
		}
		return hadamardDomains;
	}

	@Override
	public int hashCode() {
		return Integer.rotateLeft(minimum,            0)
			 ^ Integer.rotateLeft(maximum,            5)
			 ^ Integer.rotateLeft(stride,            10)
			 ^ Integer.rotateLeft(populationMinimum, 15)
			 ^ Integer.rotateLeft(populationMaximum, 20)
			 ^ Integer.rotateLeft(populationStride,  25);
	}

	@Override
	public boolean equals(
		Object otherObject
	) {
		return otherObject != null
			&& getClass()        == otherObject.getClass()
			&& minimum           == ((HadamardDomain) otherObject).minimum
			&& maximum           == ((HadamardDomain) otherObject).maximum
			&& stride            == ((HadamardDomain) otherObject).stride
			&& populationMinimum == ((HadamardDomain) otherObject).populationMinimum
			&& populationMaximum == ((HadamardDomain) otherObject).populationMaximum
			&& populationStride  == ((HadamardDomain) otherObject).populationStride;
	}

	@Override
	public String toString() {
		return "HadamardDomain ["
				+ "minimum="           + minimum           + ", "
				+ "maximum="           + maximum           + ", "
				+ "stride="            + stride            + ", "
				+ "populationMinimum=" + populationMinimum + ", "
				+ "populationMaximum=" + populationMaximum + ", "
				+ "populationStride="  + populationStride
				+ "]";
	}
}
