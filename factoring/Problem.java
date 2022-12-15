package factoring;

import java.util.ArrayList;
import java.util.List;

// sizeOfFactorsInBits sets the size of the matrix
// when size = 0, then factors = 0
// should this automatically detect the size?

public class Problem {
	public static final String PRODUCT_LENGTH_IS_NOT_POSITIVE = "product length is not positive";
	public static final String PRODUCT_LENGTH_IS_ODD          = "product length is not even";
	public static final String PRODUCT_IS_NOT_HEXADECIMAL     = "product is not hexadecimal";
	public static final String LEADING_BIT_GETS_DROPPED       = "leading bits get dropped";
	public static final String LEADING_BIT_IS_INSIGNIFICANT   = "leading bit is not in the most significant half";

	public final int          sizeOfFactorsInBits;
	public final boolean[]    product;
	public final List<String> errors;

	public Problem(Integer sizeOfProductInBits, String productInHexadecimal) {
		List<String> errors = validate(sizeOfProductInBits, productInHexadecimal);
		if(errors.isEmpty()) {
			this.sizeOfFactorsInBits = sizeOfProductInBits / 2;
			this.product             = Utility.toBooleans(sizeOfProductInBits, productInHexadecimal);
			this.errors              = errors;
		} else {
			this.sizeOfFactorsInBits = 0;
			this.product             = null;
			this.errors              = errors;
		}
	}

	public static List<String> validate(Integer sizeOfProductInBits, String productInHexadecimal) {
		List<String> result = new ArrayList<String>();
		if(sizeOfProductInBits <= 0) {
			result.add(PRODUCT_LENGTH_IS_NOT_POSITIVE);
		}
		if(!Utility.isEven(sizeOfProductInBits)) {
			result.add(PRODUCT_LENGTH_IS_ODD);
		}
		if(!Utility.isHexadecimal(productInHexadecimal)) {
			result.add(PRODUCT_IS_NOT_HEXADECIMAL);
		}
		if(result.isEmpty()) {
			if(Utility.wouldDropLeadingBit(sizeOfProductInBits, productInHexadecimal)) {
				result.add(LEADING_BIT_GETS_DROPPED);
			}
			if(sizeOfProductInBits != 2 && !Utility.leadingBitInMostSignificantHalf(sizeOfProductInBits, productInHexadecimal)) {
				result.add(LEADING_BIT_IS_INSIGNIFICANT);
			}
		}
		return result;
	}
}
