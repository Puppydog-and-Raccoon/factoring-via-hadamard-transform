package applications;

import java.util.HashSet;

import consistency.*;

class FactoringState {
	static final int NUMBER_OF_DECISIONS_PER_CELL = 16;

	final FactoringNumber         product;
	final int                     numberOfBitsPerProduct;
	final int                     numberOfBitsPerFactor;
	final int                     numberOfDecisionsPerProblem;
	final FactoringDecision[][][] decisionsInCells; // [cell row = aId][cell column = bId][flags]

	FactoringState(final FactoringNumber product) {
		final int numberOfBitsPerProduct = product.bits.length;
		final int numberOfBitsPerFactor  = numberOfBitsPerProduct / 2;

		this.product                     = product;
		this.numberOfBitsPerProduct      = numberOfBitsPerProduct;
		this.numberOfBitsPerFactor       = numberOfBitsPerFactor;
		this.numberOfDecisionsPerProblem = NUMBER_OF_DECISIONS_PER_CELL * numberOfBitsPerFactor * numberOfBitsPerFactor;
		this.decisionsInCells            = makeDecisionsInCells(numberOfBitsPerFactor);
	}

	ConsistencyProblem makeConsistencyProblem() {
		return new ConsistencyProblem(
			hadamardDomainButterfly(),
			allConsistencyConstraints(),
			numberOfDecisionsPerProblem
		);
	}

	FactoringSolution makeFactoringSolution(final ConsistencySolution consistencySolution) {
		return consistencySolution.decisions == null
			 ? FactoringSolution.makeNoSolutionFound()
			 : makeSolutionFound(consistencySolution);
	}

	// -----------------------------------------------------------------------

	static FactoringDecision[][][] makeDecisionsInCells(final int lengthOfFactorsInBits) {
		final FactoringDecision[][][] decisionsInCells = new FactoringDecision[lengthOfFactorsInBits][lengthOfFactorsInBits][NUMBER_OF_DECISIONS_PER_CELL];
		int nextIndex = 0;
		for(final int aId : Utility.enumerateAscending(lengthOfFactorsInBits)) {
			for(final int bId : Utility.enumerateAscending(lengthOfFactorsInBits)) {
				for(final int flags : Utility.enumerateAscending(NUMBER_OF_DECISIONS_PER_CELL)) {
					final FactoringDecision factoringDecision = new FactoringDecision(flags, nextIndex++);
					decisionsInCells[aId][bId][flags] = factoringDecision;
				}
			}
		}
		return decisionsInCells;
	}

	HashSet<ConsistencyConstraint> allConsistencyConstraints() {
		final HashSet<ConsistencyConstraint> constraints = new HashSet<ConsistencyConstraint>();
		constraints.addAll(unaryConsistencyConstraints());
		constraints.addAll(binaryConsistencyConstraints());
		return constraints;
	}

	HashSet<ConsistencyConstraint> unaryConsistencyConstraints() {
		final int gfb = numberOfBitsPerFactor - 1;  // most significant factor bit
		final int gpb = numberOfBitsPerProduct - 1; // most significant product bit

		final HashSet<ConsistencyConstraint> constraints = new HashSet<ConsistencyConstraint>();
		for(final int i : Utility.enumerateAscending(numberOfBitsPerFactor)) {
			final boolean bottomRowProductBit = product.bits[i + gfb];
			final boolean rightColumnProductBit = product.bits[i];
			addConditionally(constraints, decisionsInCells[0][i],   d -> d.sumIn   == false);                 // top row      -> sum in must equal 0
			addConditionally(constraints, decisionsInCells[gfb][i], d -> d.sumOut  == bottomRowProductBit);   // bottom row   -> sum out must equal product bit
			addConditionally(constraints, decisionsInCells[i][0],   d -> d.carryIn == false);                 // right column -> carry in must equal 0
			addConditionally(constraints, decisionsInCells[i][0],   d -> d.sumOut  == rightColumnProductBit); // right column -> sum out must equal product bit
		}
		{
			final boolean bottomLeftCornerProductBit = product.bits[gpb];
			addConditionally(constraints, decisionsInCells[gfb][gfb], d -> d.carryOut == bottomLeftCornerProductBit); // bottom left corner -> carry out must equal product bit
		}
		return constraints;
	}

	// add constraint to forbid this decision, if the property is false
	void addConditionally(
		final HashSet<ConsistencyConstraint> constraints,
		final FactoringDecision[]            cell,
		final FactoringDecisionProperty      property
	) {
		for(final FactoringDecision decision : cell){
			if(!property.hasProperty(decision)) {
				constraints.add(ConsistencyConstraint.exactlyZeroOfOne(decision.index, numberOfDecisionsPerProblem));
			}
		}
	}

	HashSet<ConsistencyConstraint> binaryConsistencyConstraints() {
		final HashSet<ConsistencyConstraint> constraints = new HashSet<ConsistencyConstraint>();
		final int[] ids = Utility.enumerateAscending(numberOfBitsPerFactor);
		for(final int aId : ids) {
			for(final int bId : ids) {
				// the carry_in of the left cell must equal the carry_out of the right cell
				if(!isInRightColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						decisionsInCells[aId][bId    ], d -> d.carryIn,
						decisionsInCells[aId][bId - 1], d -> d.carryOut
					));
				}
				// the sum_in of the below right cell must equal the sum_out of the above left cell
				if(!isInTopRow(aId) && !isInLeftColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						decisionsInCells[aId    ][bId    ], d -> d.sumIn,
						decisionsInCells[aId - 1][bId + 1], d -> d.sumOut
					));
				}
				// the a_in of the left cell must equal the a_in of the right cell
				if(true) {
					constraints.add(makeEqualsConstraint(
						decisionsInCells[aId][(bId + 1) % numberOfBitsPerFactor], d -> d.aIn,
						decisionsInCells[aId][ bId                             ], d -> d.aIn
					));
				}
				// the b_in of the below cell must equal the b_in of the above cell
				if(true) {
					constraints.add(makeEqualsConstraint(
						decisionsInCells[(aId + 1) % numberOfBitsPerFactor][bId], d -> d.bIn,
						decisionsInCells[ aId                             ][bId], d -> d.bIn
					));
				}
				// in the left column, the sum_in of the below call must equals the carry_out of the above cell
				if(!isInTopRow(aId) && isInLeftColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						decisionsInCells[aId    ][bId], d -> d.sumIn,
						decisionsInCells[aId - 1][bId], d -> d.carryOut
					));
				}
			}
		}
		return constraints;
	}

	// Make a constraint that ensures that property_p in cell_p equals property_q in cell_q.
	// Each cell will have exactly one decision set to true.
	// So, the number of decisions in cell_p with property_p plus the number of decisions
	// in cell_q without property_q must equal 1. Or vice versa.
	private ConsistencyConstraint makeEqualsConstraint(
		final FactoringDecision[]       cellP,
		final FactoringDecisionProperty propertyP,
		final FactoringDecision[]       cellQ,
		final FactoringDecisionProperty propertyQ
	) {
		final HashSet<Integer> decisionsInConstraint = new HashSet<Integer>();
		for(final FactoringDecision toDecision : cellP) {
			if(!propertyP.hasProperty(toDecision)) {
				decisionsInConstraint.add(new Integer(toDecision.index));
			}
		}
		for(final FactoringDecision fromDecision : cellQ) {
			if(propertyQ.hasProperty(fromDecision)) {
				decisionsInConstraint.add(new Integer(fromDecision.index));
			}
		}
		return ConsistencyConstraint.exactlyOneOf(decisionsInConstraint, numberOfDecisionsPerProblem);
	}

	// Each cell contains 16 adjacent decisions, of which exactly 1 is set to true.
	// So, the top 4 tiers can only contain hadamard values related to 0 and 1.
	// The remaining tiers can only contain hadamard values related to the scale of the tier.
	HadamardDomain[][] hadamardDomainButterfly() {
		final int numberOfNodeTiers = Utility.numberOfNodeTiers(numberOfDecisionsPerProblem);
		final int numberOfNodeTerms = Utility.numberOfNodeTerms(numberOfDecisionsPerProblem);

		final HadamardDomain[][] hadamardDomainButterfly = new HadamardDomain[numberOfNodeTiers][numberOfNodeTerms];
		for(final int nodeTier : Utility.enumerateAscending(numberOfNodeTiers)) {
			for(final int nodeTerm : Utility.enumerateAscending(numberOfNodeTerms)) {
				hadamardDomainButterfly[nodeTier][nodeTerm] = hadamardDomainFor(nodeTier, nodeTerm);
			}
		}
		return hadamardDomainButterfly;
	}

	static HadamardDomain hadamardDomainFor(int tier, int term) {
		if(tier < 4) {
			return isPopulationNode(tier, term) ? HadamardDomain.newSpecific( 0, 1, 1, 0, 1, 1)
				 :                                HadamardDomain.newSpecific(-1, 1, 1, 0, 1, 1);
		} else {
			final int scale = 1 << (tier - 4);
			return isPopulationNode(tier, term) ? HadamardDomain.newSpecific( scale, scale, 2, scale, scale, 1)
				 : isShadowedNode(tier, term)   ? HadamardDomain.newSpecific(     0,     0, 2, scale, scale, 1)
				 :                                HadamardDomain.newSpecific(-scale, scale, 2, scale, scale, 1);
		}
	}

	static boolean isPopulationNode(final int tier, final int term) {
		final int mask = Utility.lowestNBitsSet(tier);
		return (term & mask) == 0;
	}

	// this node lies in the shadow of a population node
	static boolean isShadowedNode(final int tier, final int term) {
		final int mask = Utility.lowestNBitsSet(4);
		return (term & mask) == 0;
	}

	FactoringSolution makeSolutionFound(final ConsistencySolution consistencySolution) {
		final FactoringNumber factorA = new FactoringNumber(numberOfBitsPerFactor);
		final FactoringNumber factorB = new FactoringNumber(numberOfBitsPerFactor);
		for(final int bitId : Utility.enumerateAscending(numberOfBitsPerFactor)) {
			for(final int flags : Utility.enumerateAscending(NUMBER_OF_DECISIONS_PER_CELL)) {
				final FactoringDecision decision = decisionsInCells[bitId][bitId][flags];
				if(consistencySolution.decisions[decision.index]) {
					factorA.bits[bitId] = decision.aIn;
					factorB.bits[bitId] = decision.bIn;
				}
			}
		}
		return FactoringSolution.makeSolutionFound(factorA, factorB);
	}

	boolean isInBottomRow(final int aId) {
		return aId == numberOfBitsPerFactor - 1;
	}

	boolean isInTopRow(final int aId) {
		return aId == 0;
	}

	boolean isInLeftColumn(final int bId) {
		return bId == numberOfBitsPerFactor - 1;
	}

	boolean isInRightColumn(final int bId) {
		return bId == 0;
	}

	interface FactoringDecisionProperty {
		boolean hasProperty(FactoringDecision d);
	}
}
