package consistency;

import java.util.Arrays;

/**
 * Describes a consistency problem to solve.
 * 
 * TODO: validate domains???
 */
public final class ConsistencyProblem {
	public static final String BAD_NUMBER_OF_CONSTRAINTS             = "problem must have at least one constraint";
	public static final String BAD_NUMBER_OF_DECISIONS               = "numberOfDecisions must be a power of 2 and at least 2";
	public static final String BAD_NUMBER_OF_DECISIONS_IN_CONSTRAINT = "constraint number of decisions must equal problem number of decisions";

	/**
	 * This function generates the domains for butterfly nodes.
	 * 
	 * Since, we prefer BINARY_GROUP, this should be hardwired.
	 * However, allowing it to be set enables testing.
	 */
	public final DomainGenerator domainGenerator;

	/**
	 * These are the constraints.
	 * 
	 * Grouping with a hash set enables removing duplicates.
	 */
	public final SimpleHashSet<ConsistencyConstraint> consistencyConstraints;

	/**
	 * The size of the problem.
	 */
	public final int numberOfDecisionsInProblem;

	/**
	 * Test vectors.
	 * 
	 * These are used for debugging.
	 */
	public final int[][] testSolutions;

	/**
	 * Constructs a consistency problem. This is the current constructor to use.
	 * 
	 * @param numberOfDecisionsInProblem the size of the problem, must be a power of 2
	 * @param consistencyConstraints the constraints to be met
	 * @param domainGenerator a function that generates the hadamard domain for each butterfly node
	 * @param testSolutions solutions to be tested when debugging, otherwise null
	 */
	public ConsistencyProblem(
		final int                                  numberOfDecisionsInProblem,
		final SimpleHashSet<ConsistencyConstraint> consistencyConstraints,
		final DomainGenerator                      domainGenerator,
		final int[][]                              testSolutions
	) {
		this.numberOfDecisionsInProblem = numberOfDecisionsInProblem;
		this.consistencyConstraints     = consistencyConstraints;
		this.domainGenerator            = domainGenerator;
		this.testSolutions              = testSolutions;
	}

	/**
	 * Determines whether a problem is valid by checking whether any errors can be detected.
	 * 
     * @return whether the problem is valid
     */
	public boolean isValid() {
		return errors().isEmpty();
	}

	/**
	 * Checks whether any errors can be detected.
	 * 
     * @return a hash set of the errors that have been found or else an empty hash set
     */
	public SimpleHashSet<String> errors() {
		final SimpleHashSet<String> errors = new SimpleHashSet<>();
		if(consistencyConstraints == null || consistencyConstraints.isEmpty()) {
			errors.add(BAD_NUMBER_OF_CONSTRAINTS);
		}
		if(!Utility.isPowerOfTwo(numberOfDecisionsInProblem) || numberOfDecisionsInProblem < 2) {
			errors.add(BAD_NUMBER_OF_DECISIONS);
		}
		return errors;
	}

	/**
	 * Solves the problem and returns a solution.
	 * 
     * @return the solution
     */
	public ConsistencySolution solve() {
		if(isValid()) {
			final ConsistencyInternals consistencyInternals = new ConsistencyInternals(this);
			consistencyInternals.mergeBottomUp();
			final boolean[] solution = consistencyInternals.extractTopDown();
			return new ConsistencySolution(this, solution);
		} else {
			return new ConsistencySolution(this);
		}
	}

	@Override
	public String toString() {
		return "<ConsistencyProblem\n"
			 + "\tconsistencyConstraints="     + consistencyConstraints         + ",\n"
			 + "\tnumberOfDecisionsInProblem=" + numberOfDecisionsInProblem     + ",\n"
			 + "\ttestSolutions="              + Arrays.toString(testSolutions)
			 + ">";
	}
}
