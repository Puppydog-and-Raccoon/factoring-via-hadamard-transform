package consistency;

import java.util.HashSet;

// TODO: validate domains???

public final class ConsistencyProblem {
	public static final String BAD_NUMBER_OF_TRUES                   = "0 <= numberOfTrues <= numberOfDecisions must hold";
	public static final String BAD_NUMBER_OF_CONSTRAINTS             = "problem must have at least one constraint";
	public static final String BAD_NUMBER_OF_DECISIONS               = "numberOfDecisions must be a power of 2 and at least 2";
	public static final String BAD_NUMBER_OF_DECISIONS_IN_CONSTRAINT = "constraint number of decisions must equal problem number of decisions";

	public final Integer                        numberOfTruesInProblem; // null means could be any
	public final HadamardDomain[][]             hadamardDomains;
	public final HashSet<ConsistencyConstraint> consistencyConstraints;
	public final int                            numberOfDecisionsInProblem;
	public final int                            coreSize;

	public ConsistencyProblem(
		final int                            numberOfTruesInProblem,
		final HashSet<ConsistencyConstraint> consistencyConstraints,
		final int                            numberOfDecisionsInProblem
	) {
		this.numberOfTruesInProblem     = new Integer(numberOfTruesInProblem);
		this.hadamardDomains            = HadamardDomain.newHadamardDomains(numberOfTruesInProblem, numberOfDecisionsInProblem);
		this.consistencyConstraints     = consistencyConstraints;
		this.numberOfDecisionsInProblem = numberOfDecisionsInProblem;
		this.coreSize                   = numberOfDecisionsInProblem;
	}

	public ConsistencyProblem(
		final HadamardDomain[][]             hadamardDomains,
		final HashSet<ConsistencyConstraint> consistencyConstraints,
		final int                            numberOfDecisionsInProblem
	) {
		this.numberOfTruesInProblem     = null;
		this.hadamardDomains            = hadamardDomains;
		this.consistencyConstraints     = consistencyConstraints;
		this.numberOfDecisionsInProblem = numberOfDecisionsInProblem;
		this.coreSize                   = numberOfDecisionsInProblem;
	}

	public boolean isValid() {
		return errors().isEmpty();
	}

	public HashSet<String> errors() {
		final HashSet<String> errors = new HashSet<String>();
		if(numberOfTruesInProblem != null) {
			if(!(0 <= numberOfTruesInProblem.intValue() && numberOfTruesInProblem.intValue() <= numberOfDecisionsInProblem)) {
				errors.add(BAD_NUMBER_OF_TRUES);
			}
		}
		if(consistencyConstraints == null || consistencyConstraints.size() == 0) {
			errors.add(BAD_NUMBER_OF_CONSTRAINTS);
		}
		if(!Utility.isPowerOfTwo(numberOfDecisionsInProblem) || numberOfDecisionsInProblem < 2) {
			errors.add(BAD_NUMBER_OF_DECISIONS);
		}
		for(ConsistencyConstraint constraint : consistencyConstraints) {
			if(constraint.numberOfDecisionsInProblem != numberOfDecisionsInProblem) {
				errors.add(BAD_NUMBER_OF_DECISIONS_IN_CONSTRAINT);
			}
		}
		return errors;
	}

	@Override
	public String toString() {
		return "ConsistencyProblem ["
			 + "numberOfTruesInProblem="     + numberOfTruesInProblem     + ",\n"
			 + "hadamardDomains="            + hadamardDomains            + ",\n"
			 + "consistencyConstraints="     + consistencyConstraints     + ",\n"
			 + "numberOfDecisionsInProblem=" + numberOfDecisionsInProblem
			 + "]";
	}
}
