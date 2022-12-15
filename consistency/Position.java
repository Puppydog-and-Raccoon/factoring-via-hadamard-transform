package consistency;

import java.util.Iterator;

// Most fields are only used for debugging
// Only the isLeftNode and hadamardDomain fields are essential
// Both can be derived from tier and term

public class Position {
	final int            tier;             // could be deleted
	final int            term;             // could be deleted
	final boolean        isLeftNode;       // could be derived from tier and term
	final boolean        isInTopTier;      // could be deleted - used for debugging
	final boolean        isInBottomTier;   // could be deleted - used for debugging
	final HadamardDomain hadamardDomain;   // could be derived from tier and term

	Position(int tier, int term, int numberOfDecisions) {
		final int topTier    = 0;
		final int bottomTier = Utility.log2(numberOfDecisions);

		this.tier           = tier;
		this.term           = term;
		this.isLeftNode     = ((1 << tier) & term) == 0;
		this.isInTopTier    = tier == topTier;
		this.isInBottomTier = tier == bottomTier;
		this.hadamardDomain = new HadamardDomain(tier, term);
	}

	// Note: Both parents are population nodes or both parents are non-population nodes, so we check both ???????????????? No!!!!
	boolean wouldMakeValidParentHadamard(int leftChildHadamard, int rightChildHadamard) {
		return Utility.haveSameParity(leftChildHadamard, rightChildHadamard)
			&& hadamardDomain.contains(parentHadamard(leftChildHadamard, rightChildHadamard));
	}

	boolean wouldMakeValidParentHadamard(EquationPair leftChildPair, EquationPair rightChildPair) {
		return wouldMakeValidParentHadamard(leftChildPair.hadamard, rightChildPair.hadamard);
	}

	boolean wouldMakeValidParentHadamard(SolutionPair leftChildPair, SolutionPair rightChildPair) {
		return wouldMakeValidParentHadamard(leftChildPair.parentHadamard, rightChildPair.parentHadamard);
	}

	int parentHadamard(int leftChildHadamard, int rightChildHadamard) {
		return isLeftNode
			 ? (leftChildHadamard + rightChildHadamard) / 2
			 : (leftChildHadamard - rightChildHadamard) / 2;
	}

	EquationPair parentPair(EquationPair leftChildPair, EquationPair rightChildPair) {
		final int  parentHadamard   = parentHadamard(leftChildPair.hadamard, rightChildPair.hadamard);
		final long parentPartialSum = leftChildPair.partialSum + rightChildPair.partialSum;
		return new EquationPair(parentHadamard, parentPartialSum);
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<");
		stringBuffer.append("tier = "           + tier             + ", ");
		stringBuffer.append("term = "           + term             + ", ");
		stringBuffer.append("isLeftNode = "     + isLeftNode       + ", ");
		stringBuffer.append("isInTopTier = "    + isInTopTier      + ", ");
		stringBuffer.append("isInBottomTier = " + isInBottomTier   + ", ");
		stringBuffer.append("hadamardDomain = " + hadamardDomain);
		stringBuffer.append(">");
		return stringBuffer.toString();
	}

	class HadamardDomain implements Iterable<Integer> {
		final boolean isPopulationNode;   // could be deleted
		final int     minimum;            // could be private
		final int     maximum;            // could be private

		HadamardDomain(int tier, int term) {
			final boolean isPopulationNode = (((1 << tier) - 1) & term) == 0;
			this.isPopulationNode = isPopulationNode;
			this.minimum          = isPopulationNode ? 0           : -(1 << (tier - 1));
			this.maximum          = isPopulationNode ? (1 << tier) : +(1 << (tier - 1));
		}

		boolean contains(int value) {
			return minimum <= value && value <= maximum;
		}

		@Override
		public Iterator<Integer> iterator() {
			return new HadamardDomainIterator(minimum, maximum);
		}

		@Override
		public String toString() {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("<");
			stringBuffer.append("isPopulationNode = " + isPopulationNode + ", ");
			stringBuffer.append("minimum = "          + minimum          + ", ");
			stringBuffer.append("maximum = "          + maximum);
			stringBuffer.append(">");
			return stringBuffer.toString();
		}
	}

	class HadamardDomainIterator implements Iterator<Integer> {
		private       int next;
		private final int last;

		public HadamardDomainIterator(int minimum, int maximum) {
			this.next = minimum;
			this.last = maximum;
		}

		@Override
		public boolean hasNext() {
			return next <= last;
		}

		@Override
		public Integer next() {
			return next++;
		}
	}
}
