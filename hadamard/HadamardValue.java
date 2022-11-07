package hadamard;

public interface HadamardValue {
	public HadamardValue add(HadamardValue b);
	public HadamardValue subtract(HadamardValue b);
	public boolean equals(HadamardValue other);
}
