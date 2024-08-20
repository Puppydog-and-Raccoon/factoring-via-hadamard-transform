package consistency;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;

// differences from Java HashSet
// * null is forbidden as a member
// * choose()
// * as elements are removed, memory usage shrinks
// * requires good hash functions (such as double tabulation hashing)
// * insert and remove are faster and smaller than Java HashSet (by a factor of ~2.0)
// * iteration is faster (by a factor of ~1.5)

// 3 states
// elements[i] == null    -> means open
// elements[i] == DELETED -> means deleted
// elements[i] != DELETED -> elements[i] is element of set

// ops
// * constructor    **
// * add            **
// * add all        **
// * remove         **
// * remove all     **
// * remove if      **
// * retain all     **
// * empty          **
// * size           **
// * enumerate      **
// * contains       **
// * contains all   **
// * clear          **
// * choose         **
// * equals         **
// * instrument     **

// TODO: a variety of optimizations around add all, retain all, remove all

// when you get 3/4 of way to next size, shift
// if capacity = 1024, so range is 256 to 768
// when it shrinks to 256, then resize capacity = 512 with range 128 to 384
// when it grows to 768, then resize capacity = 2048 with range 512 to 1536
//    next smaller = 512
//    next larger = 2048
// central capacity = 768
//    when capacity becomes less than (256+512)/2, shrink
//    when capacity becomes more than (512+1024)/2, grow

// no, when it gets to 1/4 capacity, shrink (could be 3/8)
//     when it gets to 3/4 capacity, grow
// capacity = 2 * size

/**
 * SimpleHashSet replaces java.util.HashSet,
 * tailored to solving consistency.
 * SimpleHashSet emphasizes space efficiency, using about 1/4 the space of HashSet.
 * Time efficiency is less important.
 * The tradeoff is that it requires excellent hash functions.
 * 
 * <p>
 * Update methods are as consistent as possible, always
 * returning a boolean indicating whether the set changed.
 * Various new methods simplify consistency,
 * such as <tt>choose_random_element()</tt> and <tt>assign_all_or_retain_all()</tt>.
 * </p>
 * 
 * @param <ElementType> the type of the elements
 */
public class SimpleHashSet<ElementType> implements Iterable<ElementType> {
	private static final int    MINIMUM_CAPACITY      = 1 <<  3;
	private static final int    MAXIMUM_CAPACITY      = 1 << 30;
	private static final int    SLOT_NOT_FOUND        = -1;
	private static final Object DELETED               = new Object();
	private static final Random randomNumberGenerator = new Random();

	private int      capacity;           // MINIMUM_CAPACITY to MAXIMUM_CAPACITY by powers of 2
	private int      numberOfElements;
	private int      numberOfDeletes;
	private Object[] elements;           // TODO: renaame slots

	/**
	 * The constructor.
	 */
	public SimpleHashSet() {
		initializeEverythingInternal(MINIMUM_CAPACITY);
	}

	/**
	 * Add the specified element to the set.
	 * If the element was already present, using equals(), do nothing.
	 * 
	 * @param elementToAdd the element to add
	 * @return whether the element was added
	 */
	public boolean add(
		final Object elementToAdd
	) {
		int firstDeletedSlot = SLOT_NOT_FOUND;

		for(int slot = elementToAdd.hashCode() & wrapMask(); true; slot = (slot + 1) & wrapMask()) {
			if(elements[slot] == elementToAdd) {
				// element is already present
				return false;
			}
			if(elements[slot] == null) {
				// finding an empty slot implies the element is not present
				if(firstDeletedSlot == SLOT_NOT_FOUND) {
					setEmptySlotToElement(slot, elementToAdd);
				} else {
					setDeletedSlotToElement(firstDeletedSlot, elementToAdd);
				}
				increaseCapacityIfAppropriate();
				return true;
			}
			if(elements[slot] == DELETED) {
				// remember the first deleted slot
				if(firstDeletedSlot == SLOT_NOT_FOUND) {
					firstDeletedSlot = slot;
				}
			}
		}
	}

	public boolean remove(
		final Object elementToRemove
	) {
		for(int slot = elementToRemove.hashCode() & wrapMask(); true; slot = (slot + 1) & wrapMask()) {
			if(elements[slot] == elementToRemove) {
				// found
				setElementSlotToDeleted(slot);
				decreaseCapacityIfAppropriate();
				return true;
			}
			if(elements[slot] == null) {
				// not found
				return false;
			}
		}
	}

	public int size() {
		return numberOfElements;
	}

	public boolean isEmpty() {
		return numberOfElements == 0;
	}

	public boolean contains(
		final Object elementToFind
	) {
		for(int slot = elementToFind.hashCode() & wrapMask(); true; slot = (slot + 1) & wrapMask()) {
			if(elements[slot] == elementToFind) {
				return true;
			}
			if(elements[slot] == null) {
				return false;
			}
		}
	}

	public boolean clear() {
		final int old_size = numberOfElements;
		initializeEverythingInternal(MINIMUM_CAPACITY);
		return old_size > 0;
	}

	public boolean addAll(final SimpleHashSet<?> otherHashSet) {
		boolean anythingChanged = false;
		for(final Object otherElement : otherHashSet.elements) {
			if(otherElement != null && otherElement != DELETED) {
				final boolean thisChanged = add(otherElement);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	public boolean removeAll(final SimpleHashSet<?> otherHashSet) {
		boolean anythingChanged = false;
		for(final Object otherElement : otherHashSet.elements) {
			if(otherElement != null && otherElement != DELETED) {
				final boolean thisChanged = remove(otherElement);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	@SuppressWarnings("unchecked")
	public ElementType chooseRandomElement() {
		if(numberOfElements == 0) {
			return null;
		}

		int numberToSkip = randomNumberGenerator.nextInt(numberOfElements);
		for(final Object element : elements) {
			if(element != null && element != DELETED) {
				if(numberToSkip-- == 0) {
					return (ElementType) element;
				}
			}
		}

		while(true) {
			int slot = randomNumberGenerator.nextInt() & wrapMask();
			if(elements[slot] != null) {
				return (ElementType) elements[slot];
			}
		}
	}

	// NOTE: this is used in functions to fill equation butterflies and consistency,
	// so performancy matters little
	@SuppressWarnings("unchecked")
	public boolean removeIf(final Predicate<ElementType> filter) {
		boolean anythingChanged = false;
		for(int slot = 0; slot < capacity; slot++) {
			final Object element = elements[slot];
			if(element != null && element != DELETED && filter.test((ElementType) element)) {
				setElementSlotToDeleted(slot);
				anythingChanged = true;
			}
		}
		decreaseCapacityIfAppropriate();
		return anythingChanged;
	}

	public boolean retainAll(final SimpleHashSet<?> otherHashMap) {
		boolean anythingChanged = false;
		for(int slot = 0; slot < capacity; slot++) {
			final Object element = elements[slot];
			if(element != null && element != DELETED) {
				if(!otherHashMap.contains(element)) {
					setElementSlotToDeleted(slot);
					anythingChanged = true;
				}
			}
		}
		decreaseCapacityIfAppropriate();
		return anythingChanged;
	}

	public boolean containsAll(final SimpleHashSet<?> otherHashMap) {
		for(final Object otherElement : otherHashMap.elements) {
			if(otherElement!= null && otherElement != DELETED && !contains(otherElement)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Iterator<ElementType> iterator() {
		return new Iterator<ElementType>() {
			private int currentSlot = SLOT_NOT_FOUND;
			private int nextSlot    = SLOT_NOT_FOUND;

			@Override
			public boolean hasNext() {
				nextSlot = indexOfNextElement();
				return nextSlot < capacity;
			}

			@SuppressWarnings("unchecked")
			@Override
			public ElementType next() {
				currentSlot = nextSlot;
				return (ElementType) elements[currentSlot];
			}

			@Override
			public void remove() {
				setElementSlotToDeleted(currentSlot);
			}

			private int indexOfNextElement() {
				int indexOfNextElement = currentSlot + 1;
				while(indexOfNextElement < capacity && (elements[indexOfNextElement] == null || elements[indexOfNextElement] == DELETED)) {
					indexOfNextElement += 1;
				}
				return indexOfNextElement;
			}
		};
	}
/*
	public void dumpStats() {
		System.out.println("number of contains = " + numberOfContains);
		System.out.println("number of probes   = " + numberOfContainsProbes);
		System.out.println("probes / contain   = " + (double) numberOfContainsProbes / (double) numberOfContains);

		System.out.println("number of inserts  = " + numberOfInserts);
		System.out.println("number of probes   = " + numberOfInsertsProbes);
		System.out.println("probes / contain   = " + (double) numberOfInsertsProbes / (double) numberOfInserts);
	}
*/
	// used for testing delete
	public int capacity() {
		return capacity;
	}

	// -----------------------------------------------------------------------
	// internal helpers

	private void initializeEverythingInternal(final int newCapacity) {
		Utility.insistInRange(MINIMUM_CAPACITY, newCapacity, MAXIMUM_CAPACITY);

		capacity         = newCapacity;
		numberOfElements = 0;
		numberOfDeletes  = 0;
		elements         = new Object[newCapacity];
	}

	private int minimumNumberOfValidElements() {
		return capacity == MINIMUM_CAPACITY ? 0 : capacity * 1 / 4;
	}

	private int maximumNumberOfValidElements() {
		return capacity * 3 / 4;
	}

	private int maximumNumberOfDeletedElements() {
		return capacity / 8;
	}

	private int wrapMask() {
		return capacity - 1;
	}

	private void increaseCapacityIfAppropriate() {
		if(numberOfElements > maximumNumberOfValidElements()) {
			refillEverythingInternal(capacity * 2);
		} else if(numberOfDeletes > maximumNumberOfDeletedElements()) {
			refillEverythingInternal(capacity);
		}
	}

	private void decreaseCapacityIfAppropriate() {
		if(numberOfElements < minimumNumberOfValidElements()) {
			do {
				refillEverythingInternal(capacity / 2);
			} while(numberOfElements < minimumNumberOfValidElements());
		} else if(numberOfDeletes > maximumNumberOfDeletedElements()) {
			refillEverythingInternal(capacity);
		}
	}

	private void refillEverythingInternal(
		final int newCapacity
	) {
		Utility.insistInRange(MINIMUM_CAPACITY, newCapacity, MAXIMUM_CAPACITY);

		final Object[] previousElements = elements;

		capacity        = newCapacity;
		numberOfDeletes = 0;
		elements        = new Object[newCapacity];

		for(final Object previousElement : previousElements) {
			if(previousElement != null && previousElement != DELETED) {
				int slot = previousElement.hashCode() & wrapMask();
				while(elements[slot] != null) {
					slot = (slot + 1) & wrapMask();
				}
				elements[slot] = previousElement;
			}
		}
	}

	private void setEmptySlotToElement(
		final int    emptySlot,
		final Object element
	) {
//		Utility.insist(elements[emptySlot] == null, "bad");

		elements[emptySlot] = element;
		numberOfElements += 1;
	}

	private void setDeletedSlotToElement(
		final int    deletedSlot,
		final Object elementToAdd
	) {
//		Utility.insist(elements[deletedSlot] == DELETED, "bad");

		elements[deletedSlot] = elementToAdd;
		numberOfElements      += 1;
		numberOfDeletes       -= 1;
	}

	private void setElementSlotToDeleted(int slot) {
//		Utility.insist(elements[slot] != null && elements[slot] != DELETED, "bad");

		elements[slot]   = DELETED;
		numberOfElements -= 1;
		numberOfDeletes  += 1;
	}

	// -----------------------------------------------------------------------
	// boilerplate functions

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		SimpleHashSet<?> other = (SimpleHashSet<?>) obj;
		if (numberOfElements != other.numberOfElements)
			return false;
		for(final Object object : other) {
			if(!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ElementType[] toArray(ElementType[] original) {
		final ElementType[] array = Arrays.copyOf(original, numberOfElements);
		int i = 0;
		for(final Object element : elements) {
			if(element != null) {
				array[i++] = (ElementType) element;
			}
		}
		return array;
	}

	/**
	 * Either assign or intersect the second hash set into the first hash set.
	 * Used to find the largest intersection of a group of hash sets.
	 * 
	 * <p>
	 * This is only used in consistency, so preformance matters little.
	 * </p>
	 * 
	 * @param isTrueAssignAllIsFalseRetainAll whether to assign or intersect
	 * @param toHashSet the hash set to modify
	 * @param fromHashSet the other hash set
	 * @return whether anything changed
	 */
	public boolean assignAllOrRetainAll(
		final boolean                    isTrueAssignAllIsFalseRetainAll,
		final SimpleHashSet<ElementType> fromHashSet
	) {
		return isTrueAssignAllIsFalseRetainAll
			 ? assignDestructive(fromHashSet)
			 : retainAll(fromHashSet);
	}

	/**
	 * Remove everything from a hash set except for a single random element.
	 * 
	 * @param hashSet the hash set to choose from
	 * @return whether anything changed
	 */
	public boolean removeAllButRandomSingleton() {
		if(isEmpty()) {
			return false;
		} else {
			final ElementType elementToKeep = chooseRandomElement();
			return removeIf(element -> element != elementToKeep);
		}
	}

	/**
	 * Create a hash set with a given element.
	 * 
	 * @param element the element to add
	 * @return the new hash set
	 */
	public static <ElementType> SimpleHashSet<ElementType> makeHashSet(
		final ElementType element
	) {
		final SimpleHashSet<ElementType> hashSet = new SimpleHashSet<>();
		hashSet.add(element);
		return hashSet;
	}

	/**
	 * Create a hash set with two given elements.
	 * 
	 * @param elementA the first element to add
	 * @param elementB the second element to add
	 * @return the new hash set
	 */
	public static <ElementType> SimpleHashSet<ElementType> makeHashSet(
		final ElementType elementA,
		final ElementType elementB
	) {
		final SimpleHashSet<ElementType> hashSet = new SimpleHashSet<>();
		hashSet.add(elementA);
		hashSet.add(elementB);
		return hashSet;
	}

	/**
	 * Create a hash set with three given elements.
	 * 
	 * @param elementA the first element to add
	 * @param elementB the second element to add
	 * @param elementC the third element to add
	 * @return the new hash set
	 */
	public static <ElementType> SimpleHashSet<ElementType> makeHashSet(
		final ElementType elementA,
		final ElementType elementB,
		final ElementType elementC
	) {
		final SimpleHashSet<ElementType> hashSet = new SimpleHashSet<>();
		hashSet.add(elementA);
		hashSet.add(elementB);
		hashSet.add(elementC);
		return hashSet;
	}

	/**
	 * Create an Integer hash set with the given elements.
	 * 
	 * @param elements the array of element to add
	 * @return the new hash set
	 */
	public static SimpleHashSet<Integer> makeHashSet(
		final int[] elements
	) {
		final SimpleHashSet<Integer> hashSet = new SimpleHashSet<>();
		for(final int element : elements) {
			hashSet.add(new Integer(element));
		}
		return hashSet;
	}

	public int[] toSortedIntArray() {
		final int[] ints = new int[numberOfElements];
		int next = 0;
		for(final Object element : elements) {
			if(element instanceof Integer) {
				Integer integer = (Integer) element;
				ints[next++] = integer.intValue();
			} else if(element != null) {
//				System.out.println(element.getClass().getName());
				throw new RuntimeException("this only works for Integer objects");
			}
		}

		Arrays.sort(ints);
		return ints;
	}

	public boolean assignSubset(
		final SimpleHashSet<ElementType> otherHashSet
	) {
		return numberOfElements == otherHashSet.numberOfElements
			 ? false
			 : assignDestructive(otherHashSet);
	}

	private boolean assignDestructive(
		final SimpleHashSet<ElementType> otherHashSet
	) {
		capacity         = otherHashSet.capacity;
		numberOfElements = otherHashSet.numberOfElements;
		numberOfDeletes  = otherHashSet.numberOfDeletes;
		elements         = otherHashSet.elements;
		otherHashSet.capacity         = 0;
		otherHashSet.numberOfElements = 0;
		otherHashSet.numberOfDeletes  = 0;
		otherHashSet.elements         = null;
		return true;
	}
}
