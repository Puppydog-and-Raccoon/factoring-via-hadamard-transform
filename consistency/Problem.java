package consistency;

import java.util.ArrayList;
import java.util.List;

public class Problem {
	public static final String NUMBER_OF_DECISIONS_IS_NOT_POSITIVE = "number of decisions is not positive";
	public static final String CONSTRAINTS_IS_NULL                 = "constraints is null";
	public static final String CONSTRAINT_VALUE_IS_OUT_OF_RANGE    = "constraint value is out of range";
	public static final String NUMBER_OF_TRUES_IS_OUT_OF_RANGE     = "number of trues is out of range";

	public final int          numberOfDecisions;
	public final Constraint[] constraints;
	public final int          numberOfTrues;
	public final List<String> errors;

	public Problem(int numberOfDecisions, Constraint[] constraints, int numberOfTrues) {
		this.numberOfDecisions = numberOfDecisions;
		this.constraints       = constraints;
		this.numberOfTrues     = numberOfTrues;
		this.errors            = validate(numberOfDecisions, constraints, numberOfTrues);
	}

	static List<String> validate(int numberOfDecisions, Constraint[] constraints, int numberOfTrues) {
		List<String> errors = new ArrayList<String>();
		if(numberOfDecisions <= 0) {
			errors.add(NUMBER_OF_DECISIONS_IS_NOT_POSITIVE);
		}
		if(constraints == null) {
			errors.add(CONSTRAINTS_IS_NULL);
		}
		if(errors.isEmpty()) {
			for(Constraint constraint : constraints) {
				if(0 > constraint.i || constraint.i >= numberOfDecisions) {
					errors.add(CONSTRAINT_VALUE_IS_OUT_OF_RANGE);
					break;
				}
				if(0 > constraint.j || constraint.j >= numberOfDecisions) {
					errors.add(CONSTRAINT_VALUE_IS_OUT_OF_RANGE);
					break;
				}
			}
			if(0 > numberOfTrues || numberOfTrues > numberOfDecisions) {
				errors.add(NUMBER_OF_TRUES_IS_OUT_OF_RANGE);
			}
		}
		return errors;
	}
}
