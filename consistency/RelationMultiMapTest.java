package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class RelationMultiMapTest {
	static final SolutionFact S_ONE = SolutionFact.newFact(1, 1, 1);
	static final SolutionFact S_TWO = SolutionFact.newFact(2, 2, 2);

	static final EquationFact E_ONE = EquationFact.newLeaf(1, 1);
	static final EquationFact E_TWO = EquationFact.newLeaf(2, 2);

	@Test
	public void testZeroLinks() {
		RelationMultiMap mm = new RelationMultiMap();
		assertEquals(mm.allEquationFacts().size(), 0);
		assertEquals(mm.getAllSolutionFacts().size(), 0);
	}

	@Test
	public void testOneLink() {
		RelationMultiMap mm = new RelationMultiMap();
		mm.add(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);
		assertEquals(mm.getEquationFactsFor(S_ONE).size(), 1);
		assertEquals(mm.getEquationFactsFor(S_ONE).contains(E_ONE), true);
		assertEquals(mm.getEquationFactsFor(S_ONE).contains(E_TWO), false);
		assertEquals(mm.getEquationFactsFor(S_TWO), null);
		assertEquals(mm.solutionFactsFor(E_ONE).size(), 1);
		assertEquals(mm.solutionFactsFor(E_ONE).contains(S_ONE), true);
		assertEquals(mm.solutionFactsFor(E_ONE).contains(S_TWO), false);
		assertEquals(mm.solutionFactsFor(E_TWO), null);
	}

	@Test
	public void testDeleteLink1() {
		RelationMultiMap mm = new RelationMultiMap();
		mm.add(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);

		mm.remove(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 0);
		assertEquals(mm.getAllSolutionFacts().size(), 0);
	}

	@Test
	public void testDeleteLink2() {
		RelationMultiMap mm = new RelationMultiMap();
		mm.add(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);

		mm.remove(E_TWO, S_TWO);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);

		mm.remove(E_ONE, S_TWO);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);

		mm.remove(E_TWO, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 1);
		assertEquals(mm.getAllSolutionFacts().size(), 1);

		mm.remove(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 0);
		assertEquals(mm.getAllSolutionFacts().size(), 0);
	}

	@Test
	public void testDeleteLink3() {
		RelationMultiMap mm = new RelationMultiMap();
		mm.add(E_ONE, S_ONE);
		mm.add(E_ONE, S_TWO);
		mm.add(E_TWO, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 2);
		assertEquals(mm.getAllSolutionFacts().size(), 2);
		assertEquals(mm.getEquationFactsFor(S_ONE).size(), 2);
		assertEquals(mm.getEquationFactsFor(S_TWO).size(), 1);
		assertEquals(mm.solutionFactsFor(E_ONE).size(), 2);
		assertEquals(mm.solutionFactsFor(E_TWO).size(), 1);

		mm.remove(E_ONE, S_ONE);
		assertEquals(mm.allEquationFacts().size(), 2);
		assertEquals(mm.getAllSolutionFacts().size(), 2);
		assertEquals(mm.getEquationFactsFor(S_ONE).size(), 1);
		assertEquals(mm.getEquationFactsFor(S_TWO).size(), 1);
		assertEquals(mm.solutionFactsFor(E_ONE).size(), 1);
		assertEquals(mm.solutionFactsFor(E_TWO).size(), 1);
	}
}
