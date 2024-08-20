package applications;

import consistency.*;
import consistency.SimpleHashSet;

/**
 * The heart of the factoring algorithm.
 * This algorithm factors square numbers by translating
 * factoring problems into consistency problems and consistency solutions into factoring solutions.
 * 
 * <p>
 * Directions are based on the square multiplication matrix, where the upper right corner
 * is position 0, 0; vector a runs down the right; and vector b runs across the top.
 * </p>
 */
class FactoringInternals {
	static final int NUMBER_OF_STATES_PER_CELL     = 16;
	static final int NUMBER_OF_DECISIONS_PER_STATE =  2;

	final FactoringNumber         product;
	final int                     numberOfBitsPerProduct;
	final int                     numberOfBitsPerFactor;
	final FactoringDecision[][][] factoringDecisionsByCell; // [cell row = aId][cell column = bId][flags]
	final int                     numberOfConsistencyDecisions;

	/**
	 * Constructor. Factoring problems are defined by the number to factor.
	 * 
	 * <p>
	 * The length of the product should be 2 times a power of 2.
	 * The most significant bit of the product should be in the upper half of the bits.
	 * </p>
	 * 
	 * @param product the number to factor
	 */
	FactoringInternals(
		final FactoringNumber product
	) {
		final int numberOfBitsPerProduct = product.bits.length;
		final int numberOfBitsPerFactor  = numberOfBitsPerProduct / 2;

		this.product                      = product;
		this.numberOfBitsPerProduct       = numberOfBitsPerProduct;
		this.numberOfBitsPerFactor        = numberOfBitsPerFactor;
		this.factoringDecisionsByCell     = makeFactoringDecisionsByCell(numberOfBitsPerFactor);
		this.numberOfConsistencyDecisions = NUMBER_OF_STATES_PER_CELL * NUMBER_OF_DECISIONS_PER_STATE * numberOfBitsPerFactor * numberOfBitsPerFactor;
	}

	/**
	 * Translate the factoring problem into a consistency problem.
	 * 
	 * @return the corresponding consistency problem
	 */
	ConsistencyProblem makeConsistencyProblem() {
		final SimpleHashSet<ConsistencyConstraint> consistencyConstraints = allConsistencyConstraints();
		final DomainGenerator                      domainGenerator        = DomainGenerator.BINARY_GROUP;
		final int[][]                              consistencySolutions   = null; // TODO: compute this
		return new ConsistencyProblem(numberOfConsistencyDecisions, consistencyConstraints, domainGenerator, consistencySolutions);
	}

	/**
	 * Translate a consistency solution into a factoring solution.
	 * 
	 * @param consistencySolution the consistency solution to translate
	 * @return the corresponding factoring solution
	 */
	FactoringSolution makeFactoringSolution(final ConsistencySolution consistencySolution) {
		return consistencySolution.decisions == null
			 ? FactoringSolution.makeNoSolutionFound()
			 : makeSolutionFound(consistencySolution);
	}

	// -----------------------------------------------------------------------

	/**
	 * Build a matrix of factoring decisions, that maps matrix multiplication states to consistency decisions and back.
	 * 
	 * @param lengthOfFactorsInBits defines the sizes of the multiplication matrix and the consistency vector
	 * @return the factoring decisions that defines the mappings
	 */
	static FactoringDecision[][][] makeFactoringDecisionsByCell(final int lengthOfFactorsInBits) {
		final FactoringDecision[][][] factoringDecisionsByCell = new FactoringDecision[lengthOfFactorsInBits][lengthOfFactorsInBits][NUMBER_OF_STATES_PER_CELL];
		int nextIndex = 0;
		for(final int aId : Utility.enumerateAscending(lengthOfFactorsInBits)) {
			for(final int bId : Utility.enumerateAscending(lengthOfFactorsInBits)) {
				for(final int flags : Utility.enumerateAscending(NUMBER_OF_STATES_PER_CELL)) {
					final FactoringDecision factoringDecision = new FactoringDecision(flags, NUMBER_OF_DECISIONS_PER_STATE * (nextIndex++));
					factoringDecisionsByCell[aId][bId][flags] = factoringDecision;
				}
			}
		}
		return factoringDecisionsByCell;
	}

	/**
	 * Gather all consistency constraints, based on the factoring decision mappings
	 * 
	 * @return all consistency constraints
	 */
	SimpleHashSet<ConsistencyConstraint> allConsistencyConstraints() {
		final SimpleHashSet<ConsistencyConstraint> allConsistencyConstraints = new SimpleHashSet<>();
		allConsistencyConstraints.addAll(unaryConsistencyConstraints());
		allConsistencyConstraints.addAll(binaryConsistencyConstraints());
		allConsistencyConstraints.addAll(cellConsistencyConstraints());
		return allConsistencyConstraints;
	}

	/**
	 * Gather the unary constraints.
	 * The top sum in and right carry in must be 0.
	 * The right and bottom sum outs must equal the product bits.
	 * The bottom left carry out must equal the product bit.
	 *
	 * @return all unary constraints.
	 */
	SimpleHashSet<ConsistencyConstraint> unaryConsistencyConstraints() {
		final int msfb = numberOfBitsPerFactor - 1;  // index of the most significant factor bit
		final int mspb = numberOfBitsPerProduct - 1; // index of the most significant product bit

		final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
		for(final int i : Utility.enumerateAscending(numberOfBitsPerFactor)) {
			final boolean bottomRowProductBit   = product.bits[i + msfb];
			final boolean rightColumnProductBit = product.bits[i];
			addCellConstraintsConditionally(constraints, factoringDecisionsByCell[0][i],    d -> d.sumIn   == false);                 // top row      -> sum in must equal 0
			addCellConstraintsConditionally(constraints, factoringDecisionsByCell[msfb][i], d -> d.sumOut  == bottomRowProductBit);   // bottom row   -> sum out must equal product bit
			addCellConstraintsConditionally(constraints, factoringDecisionsByCell[i][0],    d -> d.carryIn == false);                 // right column -> carry in must equal 0
			addCellConstraintsConditionally(constraints, factoringDecisionsByCell[i][0],    d -> d.sumOut  == rightColumnProductBit); // right column -> sum out must equal product bit
		}
		{
			final boolean bottomLeftCornerProductBit = product.bits[mspb];
			addCellConstraintsConditionally(constraints, factoringDecisionsByCell[msfb][msfb], d -> d.carryOut == bottomLeftCornerProductBit); // bottom left corner -> carry out must equal product bit
		}
		return constraints;
	}

	/**
	 * Require that a property for a cell is true.
	 * Add a new constraint to the set of constraints for each decision where the property is false for the cell.
	 * 
	 * @param constraints the set of constraints to add to
	 * @param cell the cell
	 * @param property the property
	 */
	// add constraints that only allow decisions where the property is true
	void addCellConstraintsConditionally(
		final SimpleHashSet<ConsistencyConstraint> constraints,
		final FactoringDecision[]                  cell,
		final Predicate                            property
	) {
		for(final FactoringDecision decision : cell){
			if(!property.isTrue(decision)) {
				constraints.add(ConsistencyConstraint.exactlyZeroOfOne(decision.decisionId, numberOfConsistencyDecisions));
			}
		}
	}

	/**
	 * Gather the binary constraints between neighboring cells.
	 * The carry out of the right cell must equal the carry in of the left cell.
	 * The sum out of the above left cell must equal the sum in of the below right cell.
	 * The a in of the right cell must equal the a in of the left cell.
	 * The b in of the below cell must equal the b in of the above cell.
	 * In the left column, the carry out of the above cell must equal the sum in of the below cell. 
	 * 
	 * @return all binary constraints
	 */
	SimpleHashSet<ConsistencyConstraint> binaryConsistencyConstraints() {
		final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
		final int[] ids = Utility.enumerateAscending(numberOfBitsPerFactor);
		for(final int aId : ids) {
			for(final int bId : ids) {
				// the carry_in of the left cell must equal the carry_out of the right cell
				if(!isInRightColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						factoringDecisionsByCell[aId][bId    ], d -> d.carryIn,
						factoringDecisionsByCell[aId][bId - 1], d -> d.carryOut
					));
				}

				// the sum_in of the below right cell must equal the sum_out of the above left cell
				if(!isInTopRow(aId) && !isInLeftColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						factoringDecisionsByCell[aId    ][bId    ], d -> d.sumIn,
						factoringDecisionsByCell[aId - 1][bId + 1], d -> d.sumOut
					));
				}

				// the a_in of the left cell must equal the a_in of the right cell
				if(!isInLeftColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						factoringDecisionsByCell[aId][bId + 1], d -> d.aIn,
						factoringDecisionsByCell[aId][bId    ], d -> d.aIn
					));
				}

				// the b_in of the below cell must equal the b_in of the above cell
				if(!isInBottomRow(aId)) {
					constraints.add(makeEqualsConstraint(
						factoringDecisionsByCell[aId + 1][bId], d -> d.bIn,
						factoringDecisionsByCell[aId    ][bId], d -> d.bIn
					));
				}

				// in the left column, the sum_in of the below call must equals the carry_out of the above cell
				if(!isInTopRow(aId) && isInLeftColumn(bId)) {
					constraints.add(makeEqualsConstraint(
						factoringDecisionsByCell[aId    ][bId], d -> d.sumIn,
						factoringDecisionsByCell[aId - 1][bId], d -> d.carryOut
					));
				}
			}
		}
		return constraints;
	}

	/**
	 * Make a constraint that ensures that property_p in cell_p equals property_q in cell_q.
	 * Each cell will have exactly one decision set to true.
	 * So, the number of decisions in cell_p with property_p plus the number of decisions
	 * in cell_q without property_q must equal 1. Or vice versa.
	 * 
	 * @param cellP cell p
	 * @param propertyP property p
	 * @param cellQ cell 1
	 * @param propertyQ property q
	 * @return the constraint
	 */
	private ConsistencyConstraint makeEqualsConstraint(
		final FactoringDecision[] cellP,
		final Predicate           propertyP,
		final FactoringDecision[] cellQ,
		final Predicate           propertyQ
	) {
		final SimpleHashSet<Integer> decisionsInConstraint = new SimpleHashSet<>();
		for(final FactoringDecision toDecision : cellP) {
			if(!propertyP.isTrue(toDecision)) {
				decisionsInConstraint.add(new Integer(toDecision.decisionId));
			}
		}
		for(final FactoringDecision fromDecision : cellQ) {
			if(propertyQ.isTrue(fromDecision)) {
				decisionsInConstraint.add(new Integer(fromDecision.decisionId));
			}
		}
		return ConsistencyConstraint.exactlyOneOf(decisionsInConstraint, numberOfConsistencyDecisions);
	}

	/**
	 * Gather the cell constraints.
	 * Exactly one decision in each cell must be true.
	 *
	 * @return all cell constraints
	 */
	SimpleHashSet<ConsistencyConstraint> cellConsistencyConstraints() {
		final SimpleHashSet<ConsistencyConstraint> constraints = new SimpleHashSet<>();
		final int[] ids = Utility.enumerateAscending(numberOfBitsPerFactor);
		for(final int aId : ids) {
			for(final int bId : ids) {
				constraints.add(cellConsistencyConstraint(factoringDecisionsByCell[aId][bId]));
			}
		}
		return constraints;
	}

	/**
	 * Make one cell constraint.
	 * Exactly one decision in this cell must be true.
	 * 
	 * @param cell the cell
	 * @return the constraint
	 */
	ConsistencyConstraint cellConsistencyConstraint(
		final FactoringDecision[] cell
	) {
		final SimpleHashSet<Integer> decisionsInConstraint = new SimpleHashSet<>();
		for(final FactoringDecision factoringDecision : cell) {
			decisionsInConstraint.add(new Integer(factoringDecision.decisionId));
		}
		return ConsistencyConstraint.exactlyOneOf(decisionsInConstraint, numberOfConsistencyDecisions);
	}

	/**
	 * Convert a consistency solution into a factoring solution.
	 * 
	 * @param consistencySolution the consistency solution
	 * @return the factoring solution
	 */
	FactoringSolution makeSolutionFound(final ConsistencySolution consistencySolution) {
		final FactoringNumber factorA = new FactoringNumber(numberOfBitsPerFactor);
		final FactoringNumber factorB = new FactoringNumber(numberOfBitsPerFactor);
		for(final int bitId : Utility.enumerateAscending(numberOfBitsPerFactor)) {
			for(final int flags : Utility.enumerateAscending(NUMBER_OF_STATES_PER_CELL)) {
				final FactoringDecision decision = factoringDecisionsByCell[bitId][bitId][flags];
				if(consistencySolution.decisions[decision.decisionId]) {
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

	interface Predicate {
		boolean isTrue(FactoringDecision d);
	}
}
