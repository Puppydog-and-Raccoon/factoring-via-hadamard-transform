package consistency;

/**
 * Canonicalizer is primarily about saving space.
 * Externally, it saves space by squashing duplicate objects.
 * Internally, it uses about 1/4 the space of java.util.HashMap.
 *
 * <p>
 * Canonicalizer is secondarily about saving time.
 * Externally, it saves time by comparing objects using <tt>==</tt> instead of <tt>equals()</tt>.
 * Internally, it saves time through simplicity.
 * </p>
 * 
 * <p>
 * Canonicalizer is a run-of-the-mill hash table with linear probing.
 * Deletes are forbidden.
 * Note that elements of the domain must have excellent hash functions,
 * such as double tabulation.
 * </p>
 * 
 * <p>
 * Performance testing:
 * When the max utilization is 75%, the average number of probes per canonicalize
 * starts at about 1.55 and ends at about 1.15.
 * When the max utilization if 50%, the average number of probes per canonicalize
 * starts at about 1.16 and ends at about 1.08.
 * So, storing hashes is unnecessary.
 * </p>
 * 
 * @param <ElementType> the type of the elements
 */
public class Canonicalizer<ElementType> {
	private static final int MINIMUM_CAPACITY = 1 <<  3;
	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private int      numberOfElements;
	private int      capacity;           // MINIMUM_CAPACITY to MAXIMUM_CAPACITY by powers of 2
	private Object[] slots;

	/**
	 * The constructor.
	 */
	public Canonicalizer() {
		numberOfElements = 0;
		capacity         = MINIMUM_CAPACITY;
		slots            = new Object[MINIMUM_CAPACITY];
	}

	/**
	 * If the element to canonicalize has been seen before,
	 * return the original element.
	 * Otherwise, return the new element.
	 * Element to canonicalize must be non-null.
	 * 
	 * @param elementToCanonicalize may or may not be canonical, must be non-null
	 * @return the canonical element
	 */
	@SuppressWarnings("unchecked")
	public ElementType canonicalize(
		final ElementType elementToCanonicalize
	) {
		Utility.insist(elementToCanonicalize != null, "element must be non-null");

		final int wrapMask = capacity - 1;
		for(int slotId = elementToCanonicalize.hashCode() & wrapMask; true; slotId = (slotId + 1) & wrapMask) {
			if(slots[slotId] == null) {
				// previous canonical element not found, so place and return the new element
				slots[slotId] = elementToCanonicalize;
				numberOfElements += 1;
				increaseCapacityIfAppropriate();
				return elementToCanonicalize;
			}
			if(elementToCanonicalize.equals(slots[slotId])) {
				// previous canonical element found, so return it
				return (ElementType) slots[slotId];
			}
		}
	}

	private void increaseCapacityIfAppropriate() {
		final int maximumNumberOfElements = capacity * 5 / 8; // could be 1/2, 3/4, or whatever
		if(numberOfElements > maximumNumberOfElements) {
			Utility.insist(capacity < MAXIMUM_CAPACITY, "too many elements");

			final Object[] previousSlots = slots;
			capacity *= 2;
			slots = new Object[capacity];
			refillSlots(previousSlots);
		}
	}

	private void refillSlots(
		final Object[] previousSlots
	) {
		final int wrapMask = capacity - 1;
		for(final Object previousSlot : previousSlots) {
			if(previousSlot != null) {
				for(int slotId = previousSlot.hashCode() & wrapMask; true; slotId = (slotId + 1) & wrapMask) {
					if(slots[slotId] == null) {
						slots[slotId] = previousSlot;
						break;
					}
				}
			}
		}
	}
}
