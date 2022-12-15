package factoring;

public class Solution {
	public final boolean[] smallerFactor;
	public final boolean[] largerFactor;

	public Solution(boolean[] factorA, boolean[] factorB) {
		Utility.throwIfFalse(factorA == null || factorB == null || factorA.length == factorB.length, "vectors not the same length");

		if(factorA == null || factorB == null) {
			this.smallerFactor = null;
			this.largerFactor  = null;
		} else if(Utility.lessThanOrEqualTo(factorA, factorB)) {
			this.smallerFactor = factorA;
			this.largerFactor  = factorB;
		} else {
			this.smallerFactor = factorB;
			this.largerFactor  = factorA;
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		appendNumber(buffer, smallerFactor);
		buffer.append(",");
		appendNumber(buffer, largerFactor);
		buffer.append(">");
		return buffer.toString();
	}

	private static void appendNumber(StringBuffer buffer, boolean[] factor) {
		if(factor == null) {
			buffer.append("null");
		} else {
			for(int i = factor.length - 1; i >= 0; i--) {
				buffer.append(factor[i] ? "1" : "0");
			}
		}
	}
}
