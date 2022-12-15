package consistency;

import static org.junit.Assert.*;
import org.junit.Test;

public class HadamardConsistencyTest {

	static final int numberOfDecisions4 = 4;
	static final Constraint[] constraints4 = new Constraint[] {
		new Constraint(0, 0, numberOfDecisions4),
		new Constraint(0, 1, numberOfDecisions4),
		new Constraint(0, 2, numberOfDecisions4),
		new Constraint(0, 3, numberOfDecisions4),
		new Constraint(3, 3, numberOfDecisions4)
	};

//	@Test
	public void testSuccess4() {
		final int     numberOfTrues = 2;
		final Problem problem       = new Problem(numberOfDecisions4, constraints4, numberOfTrues);

		HadamardConsistency solver = new HadamardConsistency();
		final boolean[] solution = solver.solve(problem);
		assertTrue(Utility.arraysAreEqual(solution, new boolean[]{false, true, true, false}));
	}

//	@Test
	public void testFail4() {
		final int     numberOfTrues = 3;
		final Problem problem       = new Problem(numberOfDecisions4, constraints4, numberOfTrues);

		HadamardConsistency solver = new HadamardConsistency();
		final boolean[] solution = solver.solve(problem);
		assertTrue(Utility.arraysAreEqual(solution, null));
	}

	static final int numberOfDecisions8 = 8;
	static final Constraint[] constraints8 = new Constraint[] {
		new Constraint(0, 1, numberOfDecisions8),
		new Constraint(0, 2, numberOfDecisions8),
		new Constraint(0, 3, numberOfDecisions8),
		new Constraint(3, 3, numberOfDecisions8),
		new Constraint(4, 5, numberOfDecisions8),
		new Constraint(4, 6, numberOfDecisions8),
		new Constraint(4, 7, numberOfDecisions8),
		new Constraint(7, 7, numberOfDecisions8)
	};

	@Test
	public void testSuccess8() {
		final int     numberOfTrues = 4;
		final Problem problem       = new Problem(numberOfDecisions8, constraints8, numberOfTrues);

		HadamardConsistency solver = new HadamardConsistency();
		final boolean[] solution = solver.solve(problem);
		System.out.println("solution = " + solution);
		assertNotNull(solution);
//		assertTrue(Utility.arraysAreEqual(solution, new boolean[]{false, true, true, false, false, true, true, false}));
	}

//	@Test
	public void testFail8() {
		final int     numberOfTrues = 8;
		final Problem problem       = new Problem(numberOfDecisions8, constraints8, numberOfTrues);

		HadamardConsistency solver = new HadamardConsistency();
		final boolean[] solution = solver.solve(problem);
		assertTrue(Utility.arraysAreEqual(solution, null));
	}
}
