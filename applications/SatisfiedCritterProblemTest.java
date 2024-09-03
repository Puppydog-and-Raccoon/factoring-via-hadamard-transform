package applications;

import static org.junit.Assert.*;

import org.junit.Test;

public class SatisfiedCritterProblemTest {

	@Test
	public void testSellFormed() {
		assertTrue(SatisfiedCritterProblem.wellFormed("a"));
		assertTrue(SatisfiedCritterProblem.wellFormed("M"));
		assertTrue(SatisfiedCritterProblem.wellFormed("9"));
		assertTrue(SatisfiedCritterProblem.wellFormed("~a"));
		assertTrue(SatisfiedCritterProblem.wellFormed("!M"));
		assertTrue(SatisfiedCritterProblem.wellFormed("^M"));
		assertTrue(SatisfiedCritterProblem.wellFormed("/M"));
		assertTrue(SatisfiedCritterProblem.wellFormed("-M"));
		assertTrue(SatisfiedCritterProblem.wellFormed("thisIsVeryLong99"));
		assertTrue(SatisfiedCritterProblem.wellFormed("-thisIsVeryLong99"));

		assertFalse(SatisfiedCritterProblem.wellFormed("_"));
		assertFalse(SatisfiedCritterProblem.wellFormed(" "));
		assertFalse(SatisfiedCritterProblem.wellFormed("&a"));
		assertFalse(SatisfiedCritterProblem.wellFormed(null));
		assertFalse(SatisfiedCritterProblem.wellFormed(""));
		assertFalse(SatisfiedCritterProblem.wellFormed("~"));
		assertFalse(SatisfiedCritterProblem.wellFormed("!"));
		assertFalse(SatisfiedCritterProblem.wellFormed("^"));
		assertFalse(SatisfiedCritterProblem.wellFormed("/"));
		assertFalse(SatisfiedCritterProblem.wellFormed("-"));
		assertFalse(SatisfiedCritterProblem.wellFormed("~~armadillo"));
		assertFalse(SatisfiedCritterProblem.wellFormed("an armadillo"));
	}
}
