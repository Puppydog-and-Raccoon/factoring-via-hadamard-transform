package applications;

/**
 * Describes a factoring solution.
 */
public class FactoringSolution {
	public final FactoringNumber factor0;
	public final FactoringNumber factor1;

	/**
	 * Constructor.
	 * Both factors must be valid numbers or must both be null.
	 * 
	 * @param factor0
	 * @param factor1
	 */
	private FactoringSolution(
		final FactoringNumber factor0,
		final FactoringNumber factor1
	) {
		this.factor0 = factor0;
		this.factor1 = factor1;
	}

	/**
	 * Make a factoring solution for valid factors.
	 * Order the factors smallest to largest.
	 * 
	 * @param factor0
	 * @param factor1
	 * @return the factoring solution
	 */
	public static FactoringSolution makeSolutionFound(
		final FactoringNumber factor0,
		final FactoringNumber factor1
	) {
		final FactoringNumber minimumFactor = FactoringNumber.minimum(factor0, factor1);
		final FactoringNumber maximumFactor = FactoringNumber.maximum(factor0, factor1);
		return new FactoringSolution(minimumFactor, maximumFactor);
	}

	/**
	 * Make a factoring solution when no factors are found.
	 * 
	 * @return the factoring solution
	 */
	public static FactoringSolution makeNoSolutionFound() {
		return new FactoringSolution(null, null);
	}

	/**
	 * Determine whether this solution is valid for a given problem.
	 * No solution (factors = null) is valid.
	 * Otherwise, the factors must multiply to the correct number.
	 * 
	 * @param factoringProblem the given problem
	 * @return whether this solution is valid
	 */
	public boolean isValid(FactoringProblem factoringProblem) {
		return (factor0 == null) == (factor1 == null)
			&& (factor0 == null && factor1 == null
				|| FactoringNumber.isEqual(FactoringNumber.multiply(factor0, factor1), factoringProblem.product));
	}

	@Override
	public boolean equals(Object otherObject) {
		return otherObject != null
			&& getClass() == otherObject.getClass()
			&& FactoringNumber.isEqual(factor0, ((FactoringSolution) otherObject).factor0)
			&& FactoringNumber.isEqual(factor1, ((FactoringSolution) otherObject).factor1);
	}

	@Override
	public String toString() {
		return "<FactoringSolution "
			 + "factor0=" + factor0 + " "
			 + "factor1=" + factor1
			 + ">";
	}

	/**
	 * Determines whether this solution equals any of the given solutions.
	 * Used in testing.
	 * 
	 * @param factoringSolutions the given solutions
	 * @return whether this solution equals any
	 */
	public boolean equalsAny(FactoringSolution[] factoringSolutions) {
		if(factoringSolutions == null || factoringSolutions.length == 0) {
			return factor0 == null || factor1 == null;
		}

		for(FactoringSolution pairOfFactors : factoringSolutions) {
			if(this.equals(pairOfFactors)) {
				return true;
			}
		}
		return false;
	}
}
