package consistency;

import java.util.HashSet;
import java.util.Vector;

class ButterflyOfHashSets<ElementType> {
	private final PositionButterfly positionButterfly;
	private final Object[][]        hashSets;

	ButterflyOfHashSets(final PositionButterfly positionButterfly) {
		this.positionButterfly = positionButterfly;
		this.hashSets          = makeElementTypeButterfly(positionButterfly);
	}

	@SuppressWarnings("unchecked")
	HashSet<ElementType> hashSetAt(
		final PositionNode positionNode
	) {
		return (HashSet<ElementType>) hashSets[positionNode.nodeTier][positionNode.nodeTerm];
	}

	@SuppressWarnings("unchecked")
	HashSet<ElementType> hashSetAt(
		final int nodeTier,
		final int nodeTerm
	) {
		return (HashSet<ElementType>) hashSets[nodeTier][nodeTerm];
	}

	boolean isValid() {
		boolean valid = true;
		for(final int rootNodeTerm : positionButterfly.nodeTermIndices) {
			final HashSet<ElementType> elements = hashSetAt(positionButterfly.rootNodeTier, rootNodeTerm);
			valid = valid && !elements.isEmpty();
		}
		return valid;
	}

	private Object[][] makeElementTypeButterfly(
		final PositionButterfly positionButterfly
	) {
		final Object[][] facts = new Object[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				facts[nodeTier][nodeTerm] = new HashSet<ElementType>();
			}
		}
		return facts;
	}

	// used in consistency algorithm
	static <ElementType2> Vector<ButterflyOfHashSets<ElementType2>> vectorOfButterflies(
		final PositionButterfly positionButterfly,
		final int               count
	) {
		final Vector<ButterflyOfHashSets<ElementType2>> butterflies = new Vector<ButterflyOfHashSets<ElementType2>>(count);
		for(int i = 0; i < count; i++) {
			butterflies.add(new ButterflyOfHashSets<ElementType2>(positionButterfly));
		}
		return butterflies;
	}

	void print() {
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			System.out.print("tier " + nodeTier + ": ");
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				System.out.print(Utility.toStringFromSet(hashSetAt(nodeTier, nodeTerm)) + ", ");
			}
			System.out.println();
		}
	}
}
