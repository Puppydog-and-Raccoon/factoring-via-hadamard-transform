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
	public String toString() {
		return "FactoringSolution ["
			 + "factor0=" + factor0 + ", "
			 + "factor1=" + factor1
			 + "]";
	}
}
