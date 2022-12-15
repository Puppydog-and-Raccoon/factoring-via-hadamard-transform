package factoring;

// This class describes the meaning of each decision in terms of factoring.
// Directions are based on the square multiplication matrix, where the upper right
// is position 0, 0; vector a runs down the right; and vector b runs across the top.
// This class has package scope, because user applications should never need to access it.

class Meaning {
	final int     aId;   // the bit of factor a or the box row that this decision links to
	final int     bId;   // the bit of factor b or the box column that this decision links to
	final int     sizeOfFactorsInBits;
	final boolean aIn;
	final boolean bIn;
	final boolean sumIn;
	final boolean carryIn;
	final boolean sumOut;
	final boolean carryOut;

	Meaning(int aId, int bId, boolean aIn, boolean bIn, boolean sumIn, boolean carryIn, int sizeOfFactorsInBits) {
		final int totalIn = (aIn && bIn ? 1 : 0) + (sumIn ? 1 : 0) + (carryIn ? 1 : 0);
		this.aId                 = aId;
		this.bId                 = bId;
		this.sizeOfFactorsInBits = sizeOfFactorsInBits;
		this.aIn                 = aIn;
		this.bIn                 = bIn;
		this.sumIn               = sumIn;
		this.carryIn             = carryIn;
		this.sumOut              = (totalIn & 0x1) != 0;
		this.carryOut            = (totalIn & 0x2) != 0;
	}

	static boolean isConsistent(Meaning d, Meaning e, Problem p) {
		return implies(boxIsInRightColumn(d),                               d.carryIn  == false)
			&& implies(boxIsInTopRow(d),                                    d.sumIn    == false)
			&& implies(boxIsInRightColumn(d),                               d.sumOut   == p.product[d.aId])
			&& implies(boxIsInBottomRow(d),                                 d.sumOut   == p.product[d.bId + p.sizeOfFactorsInBits - 1])
			&& implies(boxIsInBottomRow(d) && boxIsInLeftColumn(d),         d.carryOut == p.product[2 * p.sizeOfFactorsInBits - 1])
			&& implies(boxIsImmediatelyLeftOf(d, e),                        d.aIn      == e.aIn)
			&& implies(boxIsImmediatelyAbove(d, e),                         d.bIn      == e.bIn)
			&& implies(boxIsImmediatelyLeftOf(d, e),                        d.carryIn  == e.carryOut)
			&& implies(boxIsImmediatelyAboveAndLeftOf(d, e),                e.sumIn    == d.sumOut)
			&& implies(boxIsImmediatelyAbove(d, e) && boxIsInLeftColumn(d), e.sumIn    == d.carryOut)
			&& implies(boxIsSameAs(d, e),                                   d          == e);
	}

	static boolean implies(boolean b, boolean c) {
		return !b || c;
	}

	static boolean boxIsInRightColumn(Meaning d) {
		return d.bId == 0;
	}

	static boolean boxIsInLeftColumn(Meaning d) {
		return d.bId == d.sizeOfFactorsInBits - 1;
	}

	static boolean boxIsInTopRow(Meaning d) {
		return d.aId == 0;
	}

	static boolean boxIsInBottomRow(Meaning d) {
		return d.aId == d.sizeOfFactorsInBits - 1;
	}

	static boolean boxIsImmediatelyLeftOf(Meaning d, Meaning e) {
		return d.aId == e.aId && d.bId == e.bId + 1;
	}

	static boolean boxIsImmediatelyAbove(Meaning d, Meaning e) {
		return d.aId + 1 == e.aId && d.bId == e.bId;
	}

	static boolean boxIsImmediatelyAboveAndLeftOf(Meaning d, Meaning e) {
		return d.aId + 1 == e.aId && d.bId == e.bId + 1;
	}

	static boolean boxIsSameAs(Meaning d, Meaning e) {
		return d.aId == e.aId && d.bId == e.bId;
	}
}
