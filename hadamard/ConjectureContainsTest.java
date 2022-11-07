package hadamard;

// Test the conjecture that w contains v <=> H(v) dot-product H(w) = N * population(v)
//
// Specifically, test_all() checks whether the conjecture holds for all 2 bit vectors for
// v and for all vectors for w.
// Specifically, test_some() finds the intersection of 2 random vectors, then flips a
// random bit, and finally checks whether the conjecture holds for both original vectors.

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class ConjectureContainsTest {
	@Test
	public void test1() {
		test_all(1);
	}

	@Test
	public void test2() {
		test_all(2);
	}

	@Test
	public void test4() {
		test_all(4);
	}

	@Test
	public void test8() {
		test_all(8);
	}

	@Test
	public void test16() {
		test_all(16);
	}

	@Test
	public void test32() {
		test_some(32, 10000000);
	}

	@Test
	public void test64() {
		test_some(64, 10000000);
	}

	@Test
	public void test2_8() {
		test_some(2 << 8, 1000000);
	}

	@Test
	public void test2_10() {
		test_some(1 << 10, 1000000);
	}

	@Test
	public void test2_12() {
		test_some(1 << 12, 100000);
	}

	@Test
	public void test2_14() {
		test_some(1 << 14, 10000);
	}

	@Test
	public void test2_16() {
		test_some(1 << 16, 1000);
	}

	@Test
	public void test2_18() {
		test_some(1 << 18, 100);
	}

	@Test
	public void test2_20() {
		test_some(1 << 20, 10);
	}

	private void test_all(int size) {
		for(int value = 0; value < 1 << size; value++) {
			int[] input = split(size, value);
			int[] output = Hadamard.fastSylvesterTransform(input);
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					int[] input2 = make_pair(size, i, j);
					int[] output2 = Hadamard.fastSylvesterTransform(input2);
					int target = i == j ? size : 2 * size;
					if(input[i] == 1 && input[j] == 1)
						assertEquals(target, dotProduct(output, output2));
					else
						assertNotEquals(target, dotProduct(output, output2));
				}
			}
		}
	}

	public static int[] split(int size, int value) {
		int[] result = new int[size];
		for(int i = 0; i < size; i++) {
			result[i] = (value >> i) & 0x1;
		}
		return result;
	}

	public static int[] make_pair(int size, int i, int j) {
		int[] result = new int[size];
		for(int k = 0; k < size; k++) {
			result[k] = 0;
		}
		result[i] = 1;
		result[j] = 1;
		return result;
	}

	public static int dotProduct(int[] a, int[] b) {
		assert a.length == b.length;
		int sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i] * b[i];
		}
		return sum;
	}

	private void test_some(int size, int n_tries) {
		Random random = new Random();
		for(int i = 0; i < n_tries; i++) {
			int[] input0 = makeRandomBinaryVector(size, random);
			int[] input1 = makeRandomBinaryVector(size, random);
			int[] input2 = intersect(input0, input1);
			int p = population(input2);

			int bit_to_flip = random.nextInt(size);
			int old_bit = input2[bit_to_flip];
			input2[bit_to_flip] = 1 - input2[bit_to_flip];

			int[] output0 = Hadamard.fastSylvesterTransform(input0);
			int[] output1 = Hadamard.fastSylvesterTransform(input1);
			int[] output2 = Hadamard.fastSylvesterTransform(input2);
			if(old_bit == 1) {
				assertEquals((p - 1) * size, dotProduct(output0, output2));
				assertEquals((p - 1) * size, dotProduct(output1, output2));
			} else {
				int dp0 = dotProduct(output0, output2);
				int dp1 = dotProduct(output1, output2);
				assertTrue((p + 1) * size != dp0 || (p + 1) * size != dp1);
//				assertNotEquals((p) * size, dot_product(output0, output2));
			}
		}
	}

	public static int[] makeRandomBinaryVector(int size, Random random) {
		int[] results = new int[size];
		for(int i = 0; i < size; i++) {
			results[i] = random.nextBoolean() ? 1 : 0;
		}
		return results;
	}

	public static int[] intersect(int[] a, int[] b) {
		assert a.length == b.length;

		int[] results = new int[a.length];
		for(int i = 0; i < a.length; i++) {
			results[i] = a[i] * b[i];
		}
		return results;
	}

	public static int population(int a, int number_of_bits) {
		int sum = 0;
		for(int i = 0; i < number_of_bits; i++) {
			sum += (a >> i) & 1;
		}
		return sum;
	}

	public static int population(int[] a) {
		int sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}
}
