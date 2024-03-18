package consistency;

import java.util.HashMap;

public class Unique<ElementType> {
	private final HashMap<ElementType, ElementType> elements = new HashMap<ElementType, ElementType>();

	public ElementType unique(final ElementType newElement) {
		Utility.insist(newElement != null, "element must be non-null");

		final ElementType previousElement = elements.putIfAbsent(newElement, newElement);
		return previousElement == null ? newElement : previousElement;
	}
}
