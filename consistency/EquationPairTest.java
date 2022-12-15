package consistency;

import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Test;

public class EquationPairTest {

	class TestCase {
		public final int      parentHadamard;
		public final Position parentPosition;
		public final int[]    hadamardInfo;

		public TestCase(int parentHadamard, Position parentPosition, int[] hadamardInfo) {
			this.parentHadamard = parentHadamard;
			this.parentPosition = parentPosition;
			this.hadamardInfo   = hadamardInfo;
		}
	}

	@Test
	public void testConstructorAndAccessors() {
		EquationPair pair0 = new EquationPair(1, 2);
		assertEquals(pair0.hadamard, 1);
		assertEquals(pair0.partialSum, 2);
	}

	@Test
	public void testEquals() {
		EquationPair pair0 = new EquationPair(1, 2);
		EquationPair pair1 = new EquationPair(1, 2);
		assertEquals(pair0, pair1);
	}

	@Test
	public void testGetChildHadamardPairs() {
		TestCase[] cases = new TestCase[] {
			// 2
			new TestCase( 0, new Position(0, 0, 2), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 0, 2), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 1, 2), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 1, 2), new int[] {2, 0, 1, -1}),

			// 4
			new TestCase( 0, new Position(0, 0, 2), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 0, 2), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 1, 2), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 1, 2), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(0, 2, 2), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 2, 2), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 3, 2), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 3, 2), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(1, 0, 4), new int[] {2, -2, 1, -1, 0, 0}),
			new TestCase( 1, new Position(1, 0, 4), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2}),
			new TestCase( 2, new Position(1, 0, 4), new int[] {4, 0, 3, 1, 2, 2}),

			new TestCase(-1, new Position(1, 1, 4), new int[] {0, -2, -1, -1, -2, 0}),
			new TestCase( 0, new Position(1, 1, 4), new int[] {2, -2, 1, -1, 0, 0, -1, 1, -2, 2}),
			new TestCase( 1, new Position(1, 1, 4), new int[] {2, 0, 1, 1, 0, 2}),

			new TestCase( 0, new Position(1, 2, 4), new int[] {0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 2, 4), new int[] {0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(1, 2, 4), new int[] {2, -2, 3, -1, 4, 0}),

			new TestCase(-1, new Position(1, 3, 4), new int[] {-2, 0, -1, 1, 0, 2}),
			new TestCase( 0, new Position(1, 3, 4), new int[] {-2, -2, -1, -1, 0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 3, 4), new int[] {0, -2, 1, -1, 2, 0}),

			// 8
			new TestCase( 0, new Position(0, 0, 8), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 0, 8), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 1, 8), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 1, 8), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(0, 2, 8), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 2, 8), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 3, 8), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 3, 8), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(0, 4, 8), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 4, 8), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 5, 8), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 5, 8), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(0, 6, 8), new int[] {0, 0, 1, -1}),
			new TestCase( 1, new Position(0, 6, 8), new int[] {2, 0, 1, 1}),

			new TestCase( 0, new Position(0, 7, 8), new int[] {0, 0, 1, 1}),
			new TestCase( 1, new Position(0, 7, 8), new int[] {2, 0, 1, -1}),

			new TestCase( 0, new Position(1, 0, 8), new int[] {2, -2, 1, -1, 0, 0}),
			new TestCase( 1, new Position(1, 0, 8), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2}),
			new TestCase( 2, new Position(1, 0, 8), new int[] {4, 0, 3, 1, 2, 2}),

			new TestCase(-1, new Position(1, 1, 8), new int[] {0, -2, -1, -1, -2, 0}),
			new TestCase( 0, new Position(1, 1, 8), new int[] {2, -2, 1, -1, 0, 0, -1, 1, -2, 2}),
			new TestCase( 1, new Position(1, 1, 8), new int[] {2, 0, 1, 1, 0, 2}),

			new TestCase( 0, new Position(1, 2, 8), new int[] {0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 2, 8), new int[] {0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(1, 2, 8), new int[] {2, -2, 3, -1, 4, 0}),

			new TestCase(-1, new Position(1, 3, 8), new int[] {-2, 0, -1, 1, 0, 2}),
			new TestCase( 0, new Position(1, 3, 8), new int[] {-2, -2, -1, -1, 0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 3, 8), new int[] {0, -2, 1, -1, 2, 0}),

			new TestCase( 0, new Position(1, 4, 8), new int[] {2, -2, 1, -1, 0, 0}),
			new TestCase( 1, new Position(1, 4, 8), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2}),
			new TestCase( 2, new Position(1, 4, 8), new int[] {4, 0, 3, 1, 2, 2}),

			new TestCase(-1, new Position(1, 5, 8), new int[] {0, -2, -1, -1, -2, 0}),
			new TestCase( 0, new Position(1, 5, 8), new int[] {2, -2, 1, -1, 0, 0, -1, 1, -2, 2}),
			new TestCase( 1, new Position(1, 5, 8), new int[] {2, 0, 1, 1, 0, 2}),

			new TestCase( 0, new Position(1, 6, 8), new int[] {0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 6, 8), new int[] {0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(1, 6, 8), new int[] {2, -2, 3, -1, 4, 0}),

			new TestCase(-1, new Position(1, 7, 8), new int[] {-2, 0, -1, 1, 0, 2}),
			new TestCase( 0, new Position(1, 7, 8), new int[] {-2, -2, -1, -1, 0, 0, 1, 1, 2, 2}),
			new TestCase( 1, new Position(1, 7, 8), new int[] {0, -2, 1, -1, 2, 0}),

			new TestCase( 0, new Position(2, 0, 8), new int[] {4, -4, 3, -3, 2, -2, 1, -1, 0, 0}),
			new TestCase( 1, new Position(2, 0, 8), new int[] {6, -4, 5, -3, 4, -2, 3, -1, 2, 0, 1, 1, 0, 2}),
			new TestCase( 2, new Position(2, 0, 8), new int[] {8, -4, 7, -3, 6, -2, 5, -1, 4, 0, 3, 1, 2, 2, 1, 3, 0, 4}),
			new TestCase( 3, new Position(2, 0, 8), new int[] {8, -2, 7, -1, 6, 0, 5, 1, 4, 2, 3, 3, 2, 4}),
			new TestCase( 4, new Position(2, 0, 8), new int[] {8, 0, 7, 1, 6, 2, 5, 3, 4, 4}),

			new TestCase(-2, new Position(2, 1, 8), new int[] {0, -4, -1, -3, -2, -2, -3, -1, -4, 0}),
			new TestCase(-1, new Position(2, 1, 8), new int[] {2, -4, 1, -3, 0, -2, -1, -1, -2, 0, -3, 1, -4, 2}),
			new TestCase( 0, new Position(2, 1, 8), new int[] {4, -4, 3, -3, 2, -2, 1, -1, 0, 0, -1, 1, -2, 2, -3, 3, -4, 4}),
			new TestCase( 1, new Position(2, 1, 8), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2, -1, 3, -2, 4}),
			new TestCase( 2, new Position(2, 1, 8), new int[] {4, 0, 3, 1, 2, 2, 1, 3, 0, 4}),

			new TestCase(-2, new Position(2, 2, 8), new int[] {0, -4, -1, -3, -2, -2, -3, -1, -4, 0}),
			new TestCase(-1, new Position(2, 2, 8), new int[] {2, -4, 1, -3, 0, -2, -1, -1, -2, 0, -3, 1, -4, 2}),
			new TestCase( 0, new Position(2, 2, 8), new int[] {4, -4, 3, -3, 2, -2, 1, -1, 0, 0, -1, 1, -2, 2, -3, 3, -4, 4}),
			new TestCase( 1, new Position(2, 2, 8), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2, -1, 3, -2, 4}),
			new TestCase( 2, new Position(2, 2, 8), new int[] {4, 0, 3, 1, 2, 2, 1, 3, 0, 4}),

			new TestCase(-2, new Position(2, 3, 8), new int[] {0, -4, -1, -3, -2, -2, -3, -1, -4, 0}),
			new TestCase(-1, new Position(2, 3, 8), new int[] {2, -4, 1, -3, 0, -2, -1, -1, -2, 0, -3, 1, -4, 2}),
			new TestCase( 0, new Position(2, 3, 8), new int[] {4, -4, 3, -3, 2, -2, 1, -1, 0, 0, -1, 1, -2, 2, -3, 3, -4, 4}),
			new TestCase( 1, new Position(2, 3, 8), new int[] {4, -2, 3, -1, 2, 0, 1, 1, 0, 2, -1, 3, -2, 4}),
			new TestCase( 2, new Position(2, 3, 8), new int[] {4, 0, 3, 1, 2, 2, 1, 3, 0, 4}),

			new TestCase( 0, new Position(2, 4, 8), new int[] {0, 0, 1, 1, 2, 2, 3, 3, 4, 4}),
			new TestCase( 1, new Position(2, 4, 8), new int[] {0, -2, 1, -1, 2, 0, 3, 1, 4, 2, 5, 3, 6, 4}),
			new TestCase( 2, new Position(2, 4, 8), new int[] {0, -4, 1, -3, 2, -2, 3, -1, 4, 0, 5, 1, 6, 2, 7, 3, 8, 4}),
			new TestCase( 3, new Position(2, 4, 8), new int[] {2, -4, 3, -3, 4, -2, 5, -1, 6, 0, 7, 1, 8, 2}),
			new TestCase( 4, new Position(2, 4, 8), new int[] {4, -4, 5, -3, 6, -2, 7, -1, 8, 0}),

			new TestCase(-2, new Position(2, 5, 8), new int[] {-4, 0, -3, 1, -2, 2, -1, 3, 0, 4}),
			new TestCase(-1, new Position(2, 5, 8), new int[] {-4, -2, -3, -1, -2, 0, -1, 1, 0, 2, 1, 3, 2, 4}),
			new TestCase( 0, new Position(2, 5, 8), new int[] {-4, -4, -3, -3, -2, -2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4}),
			new TestCase( 1, new Position(2, 5, 8), new int[] {-2, -4, -1, -3, 0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(2, 5, 8), new int[] {0, -4, 1, -3, 2, -2, 3, -1, 4, 0}),

			new TestCase(-2, new Position(2, 6, 8), new int[] {-4, 0, -3, 1, -2, 2, -1, 3, 0, 4}),
			new TestCase(-1, new Position(2, 6, 8), new int[] {-4, -2, -3, -1, -2, 0, -1, 1, 0, 2, 1, 3, 2, 4}),
			new TestCase( 0, new Position(2, 6, 8), new int[] {-4, -4, -3, -3, -2, -2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4}),
			new TestCase( 1, new Position(2, 6, 8), new int[] {-2, -4, -1, -3, 0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(2, 6, 8), new int[] {0, -4, 1, -3, 2, -2, 3, -1, 4, 0}),

			new TestCase(-2, new Position(2, 7, 8), new int[] {-4, 0, -3, 1, -2, 2, -1, 3, 0, 4}),
			new TestCase(-1, new Position(2, 7, 8), new int[] {-4, -2, -3, -1, -2, 0, -1, 1, 0, 2, 1, 3, 2, 4}),
			new TestCase( 0, new Position(2, 7, 8), new int[] {-4, -4, -3, -3, -2, -2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4}),
			new TestCase( 1, new Position(2, 7, 8), new int[] {-2, -4, -1, -3, 0, -2, 1, -1, 2, 0, 3, 1, 4, 2}),
			new TestCase( 2, new Position(2, 7, 8), new int[] {0, -4, 1, -3, 2, -2, 3, -1, 4, 0}),
		};

		for(TestCase testCase : cases) {
//			HashSet<EquationPair> actualPairs = EquationPair.getChildHadamardPairs(testCase.parentHadamard, testCase.parentPosition);
//			HashSet<EquationPair> targetPairs = toHashSet(testCase.hadamardInfo);
//			assertEquals(actualPairs, targetPairs);
		}
	}

	private static HashSet<EquationPair> toHashSet(int[] values) {
		HashSet<EquationPair> result = new HashSet<EquationPair>();
		for(int i = 0; i < values.length; ) {
			final int upperHadamard = values[i++];
			final int lowerHadamard = values[i++];
			result.add(new EquationPair(upperHadamard, lowerHadamard));
		}
		return result;
	}

	private String toString(HashSet<EquationPair> pairs) {
		StringBuffer buffer = new StringBuffer();
		String separator = "";
		for(EquationPair pair : pairs) {
			buffer.append(separator);
			buffer.append(pair);
			separator = ", ";
		}
		return buffer.toString();
	}
}
