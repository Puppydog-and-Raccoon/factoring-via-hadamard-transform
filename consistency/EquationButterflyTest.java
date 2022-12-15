package consistency;

import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Test;

public class EquationButterflyTest {

	static final int               numberOfDecisions = 4;
	static final Constraint        constraint01      = new Constraint(0, 1, numberOfDecisions);
	static final Constraint        constraint02      = new Constraint(0, 2, numberOfDecisions);
	static final Constraint        constraint03      = new Constraint(0, 3, numberOfDecisions);
	static final Constraint        constraint33      = new Constraint(3, 3, numberOfDecisions);
	static final Constraint[]      constraints       = new Constraint[] {constraint01, constraint02, constraint03, constraint33};
	static final int               numberOfTrues     = 2;
	static final Problem           problem           = new Problem(numberOfDecisions, constraints, numberOfTrues);
	static final PositionButterfly positionButterfly = new PositionButterfly(numberOfDecisions);

	// TODO: rename
	class TestCase {
		final int tier;
		final int term;
		final HashSet<EquationPair> pairs;

		TestCase(int tier, int term, HashSet<EquationPair> pairs) {
			this.tier  = tier;
			this.term  = term;
			this.pairs = pairs;
		}
	}

	TestCase[] afterMake03 = new TestCase[] {
		new TestCase(0, 0, new HashSet<EquationPair>()),
		new TestCase(0, 1, new HashSet<EquationPair>()),
		new TestCase(0, 2, new HashSet<EquationPair>()),
		new TestCase(0, 3, new HashSet<EquationPair>()),
		new TestCase(1, 0, new HashSet<EquationPair>()),
		new TestCase(1, 1, new HashSet<EquationPair>()),
		new TestCase(1, 2, new HashSet<EquationPair>()),
		new TestCase(1, 3, new HashSet<EquationPair>()),
		new TestCase(2, 0, new HashSet<EquationPair>()),
		new TestCase(2, 1, new HashSet<EquationPair>()),
		new TestCase(2, 2, new HashSet<EquationPair>()),
		new TestCase(2, 3, new HashSet<EquationPair>()),
	};

	TestCase[] afterFillLeaves03 = new TestCase[] {
		new TestCase(0, 0, new HashSet<EquationPair>()),
		new TestCase(0, 1, new HashSet<EquationPair>()),
		new TestCase(0, 2, new HashSet<EquationPair>()),
		new TestCase(0, 3, new HashSet<EquationPair>()),
		new TestCase(1, 0, new HashSet<EquationPair>()),
		new TestCase(1, 1, new HashSet<EquationPair>()),
		new TestCase(1, 2, new HashSet<EquationPair>()),
		new TestCase(1, 3, new HashSet<EquationPair>()),
		new TestCase(2, 0, EP.expand(new EP[]{new EP(2, 4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2,  0), new EP(-1,  0), new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2,  0), new EP(-1,  0), new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2, -4), new EP(-1, -2), new EP(0, 0), new EP(1, 2), new EP(2, 4)})),
	};

	TestCase[] afterFillParents03 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0,  8, 2), new EP(1,  0,  8, 2)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0,  8, 2), new EP(1,  0,  8, 2)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  0,  8, 2), new EP(1,  0,  8, 2)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  0,  8, 2), new EP(1,  0,  8, 2)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 0,  4),        new EP(1,  4),        new EP(2,  4)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1, -4,  0, 2), new EP(0, -4,  4, 2), new EP(1,  0, 4, 2)})),
		new TestCase(1, 2, EP.expand(new EP[]{new EP( 0,  4),        new EP(1,  4),        new EP(2,  4)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP(-1,  0,  4, 2), new EP(0, -4,  4, 2), new EP(1, -4, 0, 2)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2,  0),        new EP(-1,  0),       new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2,  0),        new EP(-1,  0),       new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2, -4),        new EP(-1, -2),       new EP(0, 0), new EP(1, 2), new EP(2, 4)})),
	};

	TestCase[] afterLimit03 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0,  4, 4), new EP(1,  0,  4, 4)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0,  4, 4), new EP(1,  0,  4, 4)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  0,  4, 4), new EP(1,  0,  4, 4)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  0,  4, 4), new EP(1,  0,  4, 4)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 0,  4),        new EP(1,  4),        new EP(2,  4)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1, -4,  0, 2), new EP(0, -4,  4, 2), new EP(1,  0, 4, 2)})),
		new TestCase(1, 2, EP.expand(new EP[]{new EP( 0,  4),        new EP(1,  4),        new EP(2,  4)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP(-1,  0,  4, 2), new EP(0, -4,  4, 2), new EP(1, -4, 0, 2)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2,  0),        new EP(-1,  0),       new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2,  0),        new EP(-1,  0),       new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2, -4),        new EP(-1, -2),       new EP(0, 0), new EP(1, 2), new EP(2, 4)})),
	};

	TestCase[] afterStripDown03 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 0,  4),       new EP(1,  4),       new EP(2,  4)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1, -4, 0, 4), new EP(0, -4, 0, 4), new EP(1,  0)})),
		new TestCase(1, 2, EP.expand(new EP[]{new EP( 0,  4),       new EP(1,  4),       new EP(2,  4)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP(-1,  0),       new EP(0, -4, 0, 4), new EP(1, -4, 0, 4)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2,  0),       new EP(0, 0),        new EP(2, 0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2,  0),       new EP(0, 0),        new EP(2, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2, -4),       new EP(0, 0)})),
	};

	@Test
	public void testMakeButterflyStepByStep_0_3() {
		EquationButterfly equationButterfly = EquationButterfly.makeButterflyForTest(problem, positionButterfly, constraint03);
		verify("after make", equationButterfly, afterMake03);
		equationButterfly.fillUpLeaves();
		equationButterfly.limitPopulationNode();
		verify("after fill leaves", equationButterfly, afterFillLeaves03);
		equationButterfly.fillUpParents();
		verify("after fill parents", equationButterfly, afterFillParents03);
		equationButterfly.limitRootNodes();
		verify("after limit", equationButterfly, afterLimit03);
		equationButterfly.stripDownChildren();
		verify("after strip down", equationButterfly, afterStripDown03);
		equationButterfly.wring();
		verify("after wring", equationButterfly, afterStripDown03);
	}

	@Test
	public void testMakeButterfly_0_3() {
		EquationButterfly equationButterfly = EquationButterfly.makeButterfly(problem, positionButterfly, constraint03);
		verify("make 0 3", equationButterfly, afterStripDown03);
	}

	TestCase[] afterWring02 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP(1,  0, 4, 4)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 0,  4),       new EP(1,  4),       new EP(2, 4)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1, -4, 0, 4), new EP(0, -4, 0, 4), new EP(1, 0)})),
		new TestCase(1, 2, EP.expand(new EP[]{new EP( 0,  4),       new EP(1,  4),       new EP(2, 4)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP(-1, -4, 0, 4), new EP(0, -4, 0, 4), new EP(1, 0)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2, -4),       new EP(0,  0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2,  0),       new EP(0,  0),       new EP(2, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2,  0),       new EP(0,  0),       new EP(2, 0)})),
	};

	@Test
	public void testMakeButterfly_0_2() {
		EquationButterfly equationButterfly = EquationButterfly.makeButterfly(problem, positionButterfly, constraint02);
		verify("make 0 2", equationButterfly, afterWring02);
	}

	TestCase[] afterWring01 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP( 1, 4)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0, 4, 4), new EP( 1, 4)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  4),       new EP( 1, 0, 4, 4)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  4),       new EP( 1, 0, 4, 4)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 0,  0),       new EP( 1, 4)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1,  0),       new EP( 0, 0), new EP(1,  0)})),
		new TestCase(1, 2, EP.expand(new EP[]{                      new EP( 1, 4), new EP(2,  0)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP(-1,  0),       new EP( 0, 0), new EP(1,  0)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  4)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP(-2,  0),       new EP(-1, 0), new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP(-2, -4),       new EP( 0, 0)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2,  0),       new EP(-1, 0), new EP(0, 0), new EP(1, 0), new EP(2, 0)})),
	};

	@Test
	public void testMakeButterfly_0_1() {
		EquationButterfly equationButterfly = EquationButterfly.makeButterfly(problem, positionButterfly, constraint01);
		verify("make 0 1", equationButterfly, afterWring01);
	}

	TestCase[] afterWring33 = new TestCase[] {
		new TestCase(0, 0, EP.expand(new EP[]{new EP( 0,  0), new EP(1,  0)})),
		new TestCase(0, 1, EP.expand(new EP[]{new EP( 0,  0), new EP(1,  0)})),
		new TestCase(0, 2, EP.expand(new EP[]{new EP( 0,  0), new EP(1,  0)})),
		new TestCase(0, 3, EP.expand(new EP[]{new EP( 0,  0)})),
		new TestCase(1, 0, EP.expand(new EP[]{new EP( 1,  2), new EP(2,  0)})),
		new TestCase(1, 1, EP.expand(new EP[]{new EP(-1, -2), new EP(0,  0), new EP(1, -2)})),
		new TestCase(1, 2, EP.expand(new EP[]{new EP( 0,  0), new EP(1,  2)})),
		new TestCase(1, 3, EP.expand(new EP[]{new EP( 0,  0), new EP(1, -2)})),
		new TestCase(2, 0, EP.expand(new EP[]{new EP( 2,  2)})),
		new TestCase(2, 1, EP.expand(new EP[]{new EP( 0,  0), new EP(2, -2)})),
		new TestCase(2, 2, EP.expand(new EP[]{new EP( 0,  0), new EP(2, -2)})),
		new TestCase(2, 3, EP.expand(new EP[]{new EP(-2, -2), new EP(0,  0)})),
	};

	@Test
	public void testMakeButterfly_3_3() {
		EquationButterfly equationButterfly = EquationButterfly.makeButterfly(problem, positionButterfly, constraint33);
		verify("make 3 3", equationButterfly, afterWring33);
	}

	void verify(String step, EquationButterfly butterfly, TestCase[] tests) {
		for(TestCase test : tests) {
			HashSet<EquationPair> actualPairs = butterfly.nodes[test.tier][test.term].validEquationPairs;
			if(!test.pairs.equals(actualPairs)) {
				System.out.println(step + ", tier = " + test.tier + ", term = " + test.term);
				System.out.println("   actual = " + actualPairs);
				System.out.println("   target = " + test.pairs);
				assertTrue(false);
			}
		}
	}
}
