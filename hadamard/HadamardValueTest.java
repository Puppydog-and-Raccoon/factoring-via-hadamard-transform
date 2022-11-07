package hadamard;

import static org.junit.Assert.*;
import org.junit.Test;

public class HadamardValueTest {

	class DoubleWithHadamardInterface implements HadamardValue {
		public final double value;

		public DoubleWithHadamardInterface(double value) {
			this.value = value;
		}

		@Override
		public HadamardValue add(HadamardValue otherValue) {
			DoubleWithHadamardInterface otherDouble = (DoubleWithHadamardInterface)otherValue;
			return new DoubleWithHadamardInterface(value + otherDouble.value);
		}

		@Override
		public HadamardValue subtract(HadamardValue otherValue) {
			DoubleWithHadamardInterface otherDouble = (DoubleWithHadamardInterface)otherValue;
			return new DoubleWithHadamardInterface(value - otherDouble.value);
		}

		@Override
		public boolean equals(HadamardValue otherValue) {
			if(otherValue == null) {
				return false;
			}
			if(otherValue.getClass() != getClass()) {
				return false;
			}
			DoubleWithHadamardInterface otherDouble = (DoubleWithHadamardInterface)otherValue;
			return value == otherDouble.value;
		}
	}

	@Test
	public void testSylvester() {
		final DoubleWithHadamardInterface[] input = new DoubleWithHadamardInterface[] {
			new DoubleWithHadamardInterface(  1.0),
			new DoubleWithHadamardInterface(  2.0),
			new DoubleWithHadamardInterface(  4.0),
			new DoubleWithHadamardInterface(  8.0),
			new DoubleWithHadamardInterface( 16.0),
			new DoubleWithHadamardInterface( 32.0),
			new DoubleWithHadamardInterface( 64.0),
			new DoubleWithHadamardInterface(128.0),
		};
		final DoubleWithHadamardInterface[] expectedSylvesterOutput = new DoubleWithHadamardInterface[]{
			new DoubleWithHadamardInterface( 255.0),
			new DoubleWithHadamardInterface( -85.0),
			new DoubleWithHadamardInterface(-153.0),
			new DoubleWithHadamardInterface(  51.0),
			new DoubleWithHadamardInterface(-225.0),
			new DoubleWithHadamardInterface(  75.0),
			new DoubleWithHadamardInterface( 135.0),
			new DoubleWithHadamardInterface( -45.0)
		};
		final DoubleWithHadamardInterface[] expectedSequencyOutput = new DoubleWithHadamardInterface[]{
			new DoubleWithHadamardInterface( 255.0),
			new DoubleWithHadamardInterface(-225.0),
			new DoubleWithHadamardInterface( 135.0),
			new DoubleWithHadamardInterface(-153.0),
			new DoubleWithHadamardInterface(  51.0),
			new DoubleWithHadamardInterface( -45.0),
			new DoubleWithHadamardInterface(  75.0),
			new DoubleWithHadamardInterface( -85.0)
		};
		final DoubleWithHadamardInterface[] expectedDyadicOutput = new DoubleWithHadamardInterface[]{
			new DoubleWithHadamardInterface( 255.0),
			new DoubleWithHadamardInterface(-225.0),
			new DoubleWithHadamardInterface(-153.0),
			new DoubleWithHadamardInterface( 135.0),
			new DoubleWithHadamardInterface( -85.0),
			new DoubleWithHadamardInterface(  75.0),
			new DoubleWithHadamardInterface(  51.0),
			new DoubleWithHadamardInterface( -45.0)
		};

		assertTrue(arrayEquals(Hadamard.fastSylvesterTransform(input), expectedSylvesterOutput));
		assertTrue(arrayEquals(Hadamard.fastSequencyTransform(input), expectedSequencyOutput));
		assertTrue(arrayEquals(Hadamard.fastDyadicTransform(input), expectedDyadicOutput));
	}

	private boolean arrayEquals(DoubleWithHadamardInterface[] a, DoubleWithHadamardInterface[] b) {
		if(a.length != b.length) {
			return false;
		}
		for(int i = 0; i < a.length; i++) {
			if(!a[i].equals(b[i])) {
				return false;
			}
		}
		return true;
	}
}
