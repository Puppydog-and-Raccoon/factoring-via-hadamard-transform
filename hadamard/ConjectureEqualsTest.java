package hadamard;

//This tests the conjecture that H(v) dot-product H(w) = N * population(v & w)

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class ConjectureEqualsTest {

	static int scale = 1 << 4; // 1;

	@Test
	public void test_init() {
		// make sure we are using this correctly
		arrayEquals(ConjectureContainsTest.split(4, 0x5), new int[]{1, 0, 1, 0});
		arrayEquals(ConjectureContainsTest.split(4, 0xa), new int[]{0, 1, 0, 1});
	}

	@Test
	public void test_2() {
		testAllPairs(2);
	}

	@Test
	public void test_4() {
		testAllPairs(4);
	}

	@Test
	public void test_8() {
		testAllPairs(8);
	}

	@Test
	public void test_16() {
		testAllPairs(16);
	}

	@Test
	public void test_32() {
		testSomeRandomVectors(32, 1 << 30);
	}

	@Test
	public void test_64() {
		testSomeRandomVectors(1 << 6, 1 << 28);
	}

	@Test
	public void test_256() {
		testSomeRandomVectors(1 << 8, 1 << 26);
	}

	@Test
	public void test_1K() {
		testSomeRandomVectors(1 << 10, 1 << 24);
	}

	@Test
	public void test_4K() {
		testSomeRandomVectors(1 << 12, 1 << 22);
	}

	@Test
	public void test_16K() {
		testSomeRandomVectors(1 << 14, 1 << 20);
	}

	@Test
	public void test_64K() {
		testSomeRandomVectors(1 << 16, 1 << 18);
	}

	@Test
	public void test_256K() {
		testSomeRandomVectors(1 << 18, 1 << 16);
	}

	@Test
	public void test_1M() {
		testSomeRandomVectors(1 << 20, 1 << 14);
	}

	public void testAllPairs(int numberOfBits) {
		final int size = 1 << numberOfBits;
		for(int v = 0; v < size; v++) {
			for(int w = 0; w < size; w++) {
				int[] d0 = ConjectureContainsTest.split(numberOfBits, v);
				int[] d1 = ConjectureContainsTest.split(numberOfBits, w);
				int population = ConjectureContainsTest.population(v & w, numberOfBits);
				assertEquals(dotProductOfHadamardOf(d0, d1), numberOfBits * population);
			}
		}
	}

	public void testSomeRandomVectors(int numberOfBits, int numberOfTests) {
		Random random = new Random();
		for(int testId = 0; testId < numberOfTests / scale; testId++) {
			int[] d0 = ConjectureContainsTest.makeRandomBinaryVector(numberOfBits, random);
			int[] d1 = ConjectureContainsTest.makeRandomBinaryVector(numberOfBits, random);
			int population = ConjectureContainsTest.population(ConjectureContainsTest.intersect(d0, d1));
			assertEquals(dotProductOfHadamardOf(d0, d1), numberOfBits * population);
		}
	}

	public int dotProductOfHadamardOf(int v[], int w[]) {
		int[] h0 = Hadamard.fastSylvesterTransform(v);
		int[] h1 = Hadamard.fastSylvesterTransform(w);
		return ConjectureContainsTest.dotProduct(h0, h1);
	}

	public static void arrayEquals(int[] a, int[] b) {
		assertEquals(a.length, b.length);
		for(int i = 0; i < a.length; i++) {
			assertEquals(a[i], b[i]);
		}
	}
}
