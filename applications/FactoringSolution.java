package applications;

public class FactoringSolution {
	public final FactoringNumber factor0;
	public final FactoringNumber factor1;

	private FactoringSolution(
		final FactoringNumber factor0,
		final FactoringNumber factor1
	) {
		this.factor0 = factor0;
		this.factor1 = factor1;
	}

	public static FactoringSolution makeSolutionFound(
		final FactoringNumber factor0,
		final FactoringNumber factor1
	) {
		final FactoringNumber minimumFactor = FactoringNumber.minimum(factor0, factor1);
		final FactoringNumber maximumFactor = FactoringNumber.maximum(factor0, factor1);
		return new FactoringSolution(minimumFactor, maximumFactor);
	}

	public static FactoringSolution makeNoSolutionFound() {
		return new FactoringSolution(null, null);
	}

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
		return "FactoringSolution ["
			 + "factor0=" + factor0 + ", "
			 + "factor1=" + factor1
			 + "]";
	}

	public boolean matchesOne(FactoringSolution[] factoringSolutions) {
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
