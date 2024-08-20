package applications;

import consistency.ConsistencyProblem;
import consistency.ConsistencySolution;
import consistency.Utility;

/**
 * Describes a factoring problem,
 */
public class FactoringProblem {
	public static final String BAD_POWER_OF_TWO = "the number of bits in the product must be a power of 2";
	public static final String BAD_AT_LEAST_TWO = "the number of bits in the product must be at least 2";

	public final FactoringNumber product;

	/**
	 * Constructor.
	 * 
	 * @param product the number to factor
	 */
	public FactoringProblem(final FactoringNumber product) {
		this.product = product;
	}

	/**
	 * Determines whether this number can be factored.
	 * The product must have a power of 2 number of bits.
	 * The product msst have at least 2 bits.
	 * 
	 * @return whether the problem is valid
	 */
	public boolean isValid() {
		return Utility.isPowerOfTwo(product.bits.length) && product.bits.length >= 2;
	}

	/**
	 * Solve the factoring problem using consistency.
	 * 
	 * @return the solution
	 */
	public FactoringSolution solve() {
		Utility.insist(isValid(), "invalid factoring problem");
	
		final FactoringInternals factoringInternals = new FactoringInternals(product);
	
		final ConsistencyProblem consistencyProblem = factoringInternals.makeConsistencyProblem();
		Utility.insist(consistencyProblem.isValid(), "invalid consistency problem");
	
		final ConsistencySolution consistencySolution = consistencyProblem.solve();
		Utility.insist(consistencySolution.isValid(), "invalid consistency solution");
	
		final FactoringSolution factoringSolution = factoringInternals.makeFactoringSolution(consistencySolution);
		Utility.insist(factoringSolution.isValid(this), "invalid factoring solution");
	
		return factoringSolution;
	}
}
