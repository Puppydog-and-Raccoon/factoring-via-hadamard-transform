package consistency;

class DebugButterfly<ElementType> {
	private final PropertyButterfly propertyButterfly;
	private final Object[][]        hashSetButterfly;
	private final int[][]           hadamardButterfly;
	private final int[][]           populationButterfly;
	private final int[][]           spineButterfly;
	private final int[][]           partialSumButterfly;

	// TODO: remove this
	DebugButterfly(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][] hashSetButterfly = hashSetButterfly(propertyButterfly);

		this.propertyButterfly     = propertyButterfly;
		this.hashSetButterfly      = hashSetButterfly;
		this.hadamardButterfly     = null;
		this.populationButterfly   = null;
		this.spineButterfly        = null;
		this.partialSumButterfly   = null;
	}

	DebugButterfly(
		final PropertyButterfly     propertyButterfly,
		final int[]                 rootHadamards,
		final ConsistencyConstraint consistencyConstraint
	) {
		final Object[][] hashSetButterfly    = hashSetButterfly(propertyButterfly);
		final int[][]    hadamardButterfly   = hadamardButterfly(propertyButterfly, rootHadamards);
		final int[][]    populationButterfly = populationButterfly(propertyButterfly, rootHadamards);
		final int[][]    spineButterfly      = spineButterfly(propertyButterfly, hadamardButterfly[propertyButterfly.leafNodeTier]);
		final int[][]    partialSumButterfly = partialSumButterfly(propertyButterfly, hadamardButterfly[propertyButterfly.leafNodeTier], consistencyConstraint.constraintVector);

		this.propertyButterfly     = propertyButterfly;
		this.hashSetButterfly      = hashSetButterfly;
		this.hadamardButterfly     = hadamardButterfly;
		this.populationButterfly   = populationButterfly;
		this.spineButterfly        = spineButterfly;
		this.partialSumButterfly   = partialSumButterfly;
	}

	@SuppressWarnings("unchecked")
	SimpleHashSet<ElementType> hashSetAt(
		final PropertyNode propertyNode
	) {
		return (SimpleHashSet<ElementType>) hashSetButterfly[propertyNode.nodeTier][propertyNode.nodeTerm];
	}

	@SuppressWarnings("unchecked")
	SimpleHashSet<ElementType> hashSetAt(
		final int nodeTier,
		final int nodeTerm
	) {
		return (SimpleHashSet<ElementType>) hashSetButterfly[nodeTier][nodeTerm];
	}

	// not static because of type parameter
	private Object[][] hashSetButterfly(
		final PropertyButterfly propertyButterfly
	) {
		final Object[][] hashSetButterfly = new Object[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesRandomly()) {
				hashSetButterfly[nodeTier][nodeTerm] = new SimpleHashSet<>();
			}
		}
		return hashSetButterfly;
	}

	@Override
	public String toString() {
		final StringBuffer stringBuffer = new StringBuffer();
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			stringBuffer.append("nodeTier " + nodeTier + ": ");
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				stringBuffer.append(Utility.toStringFromSet(hashSetAt(nodeTier, nodeTerm)) + " ");
			}
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	@SuppressWarnings("unchecked")
	boolean allRootsAreValid() {
//		System.out.println("sln\n" + this);
		boolean allRootsAreValid = true;
//		System.out.print("all roots are valid ");
		for(final Object hashSet : hashSetButterfly[0]) {
//			if(((SimpleHashSet<ElementType>)hashSet).size() > 1) {
//				System.out.println(" ^^^ " + ((SimpleHashSet<ElementType>)hashSet).size());
//				throw new RuntimeException("wrong size");
//			}
			final boolean isEmpty = ((SimpleHashSet<ElementType>)hashSet).isEmpty();
//			System.out.print(isEmpty ? "0" : "1");
			allRootsAreValid = allRootsAreValid && !isEmpty;
		}
//		System.out.println(" " + allRootsAreValid);
		return allRootsAreValid;
	}

	String thumbnail() {
		final StringBuffer buffer = new StringBuffer();
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : propertyButterfly.boxTermIndicesForward) {
				final SimpleHashSet<?> hashSet = (SimpleHashSet<?>) hashSetAt(nodeTier, nodeTerm);
				buffer.append(hashSet.isEmpty() ? '.' : 'X');
			}
			buffer.append('\n');
		}
		return buffer.toString();
	}

	// move to utility?
	static int[][] hadamardButterfly(
		final PropertyButterfly propertyButterfly,
		final int[]             rootHadamards
	) {
		final int[][] result = new int[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int rootNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			result[propertyButterfly.rootNodeTier][rootNodeTerm] = rootHadamards[rootNodeTerm];
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				final int x = result[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
				final int y = result[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
				result[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm]   = x + y;
				result[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm] = x - y;
			}
		}
		return result;
	}

	// move to utility?
	static int[][] populationButterfly(
		final PropertyButterfly propertyButterfly,
		final int[]             rootHadamards
	) {
		final int[][] result = new int[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int rootNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			result[propertyButterfly.rootNodeTier][rootNodeTerm] = rootHadamards[rootNodeTerm];
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				final int x = result[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm];
				final int y = result[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm];
				result[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm]   = x + y;
				result[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm] = x + y;
			}
		}
		return result;
	}

	// move to utility?
	static int[][] spineButterfly(
		final PropertyButterfly propertyButterfly,
		final int[]             leafHadamards
	) {
		final int[][] result = new int[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			result[propertyButterfly.leafNodeTier][leafNodeTerm] = leafHadamards[leafNodeTerm];
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				final int x = result[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
				final int y = result[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];
				Utility.insistSameParity(x, y);
				result[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm]   = (x + y) / 2;
				result[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm] = (x + y) / 2;
			}
		}
		return result;
	}

	// move to utility?
	static int[][] partialSumButterfly(
		final PropertyButterfly propertyButterfly,
		final int[]             leafHadamards,
		final int[]             constraintVector
	) {
		final int[][] result = new int[propertyButterfly.numberOfNodeTiers][propertyButterfly.numberOfNodeTerms];
		for(final int leafNodeTerm : propertyButterfly.nodeTermIndicesForward) {
			result[propertyButterfly.leafNodeTier][leafNodeTerm] = leafHadamards[leafNodeTerm] * constraintVector[leafNodeTerm];
		}
		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final PropertyBox propertyBox = propertyButterfly.propertyBoxes[boxTier][boxTerm];
				final int x = result[propertyBox.leftChildNode.nodeTier][propertyBox.leftChildNode.nodeTerm];
				final int y = result[propertyBox.rightChildNode.nodeTier][propertyBox.rightChildNode.nodeTerm];
				Utility.insistSameParity(x, y);
				result[propertyBox.leftParentNode.nodeTier][propertyBox.leftParentNode.nodeTerm]   = (x + y) / 2;
				result[propertyBox.rightParentNode.nodeTier][propertyBox.rightParentNode.nodeTerm] = (x + y) / 2;
			}
		}
		return result;
	}

	String dump() {
		final StringBuffer stringBuffer = new StringBuffer();
		for(final int nodeTier : propertyButterfly.nodeTierIndicesTopDown) {
			stringBuffer.append("nodeTier " + nodeTier + ": ");
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final EquationFact equationFact = EquationFact.make(hadamardButterfly[nodeTier][nodeTerm], populationButterfly[nodeTier][nodeTerm], spineButterfly[nodeTier][nodeTerm], partialSumButterfly[nodeTier][nodeTerm]);
				stringBuffer.append(equationFact);
				stringBuffer.append(" ");
			}
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}
}
