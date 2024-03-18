package consistency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationMultiMap {
	private final HashMap<EquationFact, HashSet<SolutionFact>> fromEquationFactToSolutionFacts;
	private final HashMap<SolutionFact, HashSet<EquationFact>> fromSolutionFactToEquationFacts;

	public RelationMultiMap() {
		this.fromEquationFactToSolutionFacts = new HashMap<EquationFact, HashSet<SolutionFact>>();
		this.fromSolutionFactToEquationFacts = new HashMap<SolutionFact, HashSet<EquationFact>>();
	}

	public void clear() {
		fromEquationFactToSolutionFacts.clear();
		fromSolutionFactToEquationFacts.clear();
	}

	public void add(final EquationFact equationFact, final SolutionFact solutionFact) {
		addBIntoA(fromEquationFactToSolutionFacts, equationFact, solutionFact);
		addBIntoA(fromSolutionFactToEquationFacts, solutionFact, equationFact);
	}

	public void remove(final EquationFact equationFact, final SolutionFact solutionFact) {
		removeBFromA(fromEquationFactToSolutionFacts, equationFact, solutionFact);
		removeBFromA(fromSolutionFactToEquationFacts, solutionFact, equationFact);
	}

	public Set<EquationFact> allEquationFacts() {
		return fromEquationFactToSolutionFacts.keySet();
	}

	public Set<SolutionFact> getAllSolutionFacts() {
		return fromSolutionFactToEquationFacts.keySet();
	}

	public HashSet<SolutionFact> solutionFactsFor(final EquationFact equationFact) {
		return fromEquationFactToSolutionFacts.get(equationFact);
	}

	public HashSet<EquationFact> getEquationFactsFor(final SolutionFact solutionFact) {
		return fromSolutionFactToEquationFacts.get(solutionFact);
	}

	public boolean contains(EquationFact equationFact, SolutionFact solutionFact) {
		final HashSet<SolutionFact> Solutions = fromEquationFactToSolutionFacts.get(equationFact);
		return Solutions != null && Solutions.contains(solutionFact);
	}

	public boolean addAll(final RelationMultiMap otherMultiMap) {
		final boolean a = addAllOthersIntoThis(fromEquationFactToSolutionFacts, otherMultiMap.fromEquationFactToSolutionFacts);
		final boolean b = addAllOthersIntoThis(fromSolutionFactToEquationFacts, otherMultiMap.fromSolutionFactToEquationFacts);
		return a || b;
	}

	public boolean retainAll(final RelationMultiMap otherMultiMap) {
		final boolean a = retainAllOthersInThis(fromEquationFactToSolutionFacts, otherMultiMap.fromEquationFactToSolutionFacts);
		final boolean b = retainAllOthersInThis(fromSolutionFactToEquationFacts, otherMultiMap.fromSolutionFactToEquationFacts);
		return a || b;
	}

	public boolean retainSolutions(final HashSet<SolutionFact> Solutions) {
		final boolean a = fromEquationFactToSolutionFacts.keySet().retainAll(Solutions);
		final boolean b = retainAllOthersInThis(fromEquationFactToSolutionFacts, Solutions);
		return a || b;
	}

	private static <AType, BType> void addBIntoA(final HashMap<AType, HashSet<BType>> fromAToBs, final AType a, final BType b) {
		final HashSet<BType> bs = fromAToBs.get(a);
		if(bs != null) {
			bs.add(b);
		} else {
			final HashSet<BType> newBs = Utility.createHashSetWith(b);
			fromAToBs.put(a, newBs);
		}
	}

	private static <AType, BType> void removeBFromA(final HashMap<AType, HashSet<BType>> fromAToBs, final AType a, final BType b) {
		final HashSet<BType> bs = fromAToBs.get(a);
		if(bs != null) {
			bs.remove(b);
			if(bs.isEmpty()) {
				fromAToBs.remove(a);
			}
		}
	}

	private static <AType, BType> boolean addAllOthersIntoThis(
		final HashMap<AType, HashSet<BType>> thisMap,
		final HashMap<AType, HashSet<BType>> otherMap
	) {
		boolean changed = false;
		for(final Map.Entry<AType, HashSet<BType>> keyPair : otherMap.entrySet()) {
			final AType          otherA  = keyPair.getKey();
			final HashSet<BType> otherBs = keyPair.getValue();

			final HashSet<BType> thisBs = thisMap.get(otherA);
			if(thisBs != null) {
				final boolean a = thisBs.addAll(otherBs);
				changed = changed || a;
			} else {
				final HashSet<BType> newThisBs = new HashSet<BType>(otherBs);
				thisMap.put(otherA, newThisBs);
				changed = true;
			}
		}
		return changed;
	}

	private static <AType, BType> boolean retainAllOthersInThis(
		final HashMap<AType, HashSet<BType>> thisMap,
		final HashMap<AType, HashSet<BType>> otherMap
	) {
		boolean changed = false;
		final boolean a = thisMap.keySet().retainAll(otherMap.keySet());
		changed = changed || a;
		for(final Map.Entry<AType, HashSet<BType>> keyPair : otherMap.entrySet()) {
			final AType          otherA  = keyPair.getKey();
			final HashSet<BType> otherBs = keyPair.getValue();

			final HashSet<BType> thisBs  = thisMap.get(otherA);
			if(thisBs != null) {
				final boolean b = thisBs.retainAll(otherBs);
				changed = changed || b;
			}
		}
		return changed;
	}

	private static <AType, BType> boolean retainAllOthersInThis(
		final HashMap<AType, HashSet<BType>> thisMap,
		final HashSet<BType>                 otherBs
	) {
		boolean changed = false;
		for(final Map.Entry<AType, HashSet<BType>> keyPair : thisMap.entrySet()) {
			final HashSet<BType> thisBs = keyPair.getValue();
			final boolean b = thisBs.retainAll(otherBs);
			changed = changed || b;
		}
		return changed;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		String EquationSeparator = "";
		for(EquationFact equationFact : fromEquationFactToSolutionFacts.keySet()) {
			buffer.append(EquationSeparator);
			buffer.append(equationFact);
			buffer.append("<->{");

			String SolutionSeparator = "";
			for(SolutionFact solutionFact : fromEquationFactToSolutionFacts.get(equationFact)) {
				buffer.append(SolutionSeparator);
				buffer.append(solutionFact);
				SolutionSeparator = " ";
			}

			buffer.append("}");
			EquationSeparator = " ";
		}
		buffer.append("}");
		return buffer.toString();
	}
}
