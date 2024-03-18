package consistency;

import java.util.HashSet;
import java.util.Random;

// error: public

class SolutionBox {
	final PositionBox                     positionBox;
	final HashSet<SolutionHadamardFact>   solutionHadamardFacts;
	final HashSet<SolutionPopulationFact> solutionPopulationFacts;
	final HashSet<SolutionSpineFact>      solutionSpineFacts;

	static final Random random = new Random();

	// this is the main case - the solution butterfly connected to all equation butterflies
	// boxes are created left to right, so a support box will already exist if this box is not a population or spine box
	SolutionBox(
		final PositionBox     positionBox,
		final SolutionBox[][] supportBoxes
	) {
		this.positionBox             = positionBox;
		this.solutionHadamardFacts   = new HashSet<SolutionHadamardFact>();
		this.solutionPopulationFacts = positionBox.isPopulationTermBox()
									 ? new HashSet<SolutionPopulationFact>()
									 : supportBoxes[positionBox.boxTier][positionBox.populationBoxTerm].solutionPopulationFacts;
		this.solutionSpineFacts      = positionBox.isSpineTermBox()
									 ? new HashSet<SolutionSpineFact>()
									 : supportBoxes[positionBox.boxTier][positionBox.spineBoxTerm].solutionSpineFacts;
	}

	// this is the temporary case during dynamic programming
	SolutionBox(
		final PositionBox positionBox
	) {
		this.positionBox             = positionBox;
		this.solutionHadamardFacts   = new HashSet<SolutionHadamardFact>();
		this.solutionPopulationFacts = new HashSet<SolutionPopulationFact>();
		this.solutionSpineFacts      = new HashSet<SolutionSpineFact>();
	}

	boolean add(
		final SolutionHadamardFact   solutionHadamardFact,
		final SolutionSpineFact      solutionSpineFact,
		final SolutionPopulationFact solutionPopulationFact
	) {
		final boolean hadamardSupportsChanged   = solutionHadamardFacts.add(solutionHadamardFact);
		final boolean spineSupportsChanged      = solutionSpineFacts.add(solutionSpineFact);
		final boolean populationSupportsChanged = solutionPopulationFacts.add(solutionPopulationFact);
		return hadamardSupportsChanged || spineSupportsChanged || populationSupportsChanged;
	}

	boolean addAll(
		final SolutionBox otherSupportBox
	) {
		final boolean hadamardSupportsChanged   = solutionHadamardFacts.addAll(otherSupportBox.solutionHadamardFacts);
		final boolean spineSupportsChanged      = solutionSpineFacts.addAll(otherSupportBox.solutionSpineFacts);
		final boolean populationSupportsChanged = solutionPopulationFacts.addAll(otherSupportBox.solutionPopulationFacts);
		return hadamardSupportsChanged || spineSupportsChanged || populationSupportsChanged;
	}

	boolean retainAll(
		final SolutionBox otherSupportBox
	) {
		final boolean hadamardSupportsChanged   = solutionHadamardFacts.retainAll(otherSupportBox.solutionHadamardFacts);
		final boolean spineSupportsChanged      = solutionSpineFacts.retainAll(otherSupportBox.solutionSpineFacts);
		final boolean populationSupportsChanged = solutionPopulationFacts.retainAll(otherSupportBox.solutionPopulationFacts);
		return hadamardSupportsChanged || spineSupportsChanged || populationSupportsChanged;
	}

	boolean contains(
		final SolutionHadamardFact   solutionHadamardFact,
		final SolutionSpineFact      solutionSpineFact,
		final SolutionPopulationFact parentPopulationFact
	) {
		return solutionHadamardFacts.contains(solutionHadamardFact)
			&& solutionSpineFacts.contains(solutionSpineFact)
			&& solutionPopulationFacts.contains(parentPopulationFact);
	}

	boolean assignRandomSpineSupport() {
		final SolutionSpineFact solutionSpineFactToKeep = chooseRandomSolutionSpineFact();
		return removeAllBut(solutionSpineFactToKeep);
	}

	boolean assignSpecificSpineSupport(final SolutionSpineFact specificSpineSupport) {
		final SolutionSpineFact solutionSpineFactToKeep = solutionSpineFacts.contains(specificSpineSupport) ? specificSpineSupport : null;
		return removeAllBut(solutionSpineFactToKeep);
	}

	SolutionSpineFact chooseFirstSpineSupport() {
		for(final SolutionSpineFact solutionSpineFact : solutionSpineFacts) {
			return solutionSpineFact;
		}
		return null;
	}

	SolutionSpineFact chooseRandomSolutionSpineFact() {
		if(solutionSpineFacts.size() == 0) {
			return null;
		} else {
			final int chosenIndex = random.nextInt(solutionSpineFacts.size());
			int currentIndex = 0;
			for(final SolutionSpineFact solutionSpineFact : solutionSpineFacts) {
				if(currentIndex == chosenIndex) {
					return solutionSpineFact;
				}
				currentIndex++;
			}
			throw new RuntimeException("should never occur");
		}
	}

	// return changed
	boolean removeAllBut(final SolutionSpineFact spineSupportToKeep) {
		if(spineSupportToKeep == null) { // null means remove everything
			if(solutionSpineFacts.isEmpty()) {
				return false; // unchanged
			} else {
				solutionSpineFacts.clear();
				return true; // changed
			}
		} else {
			return solutionSpineFacts.removeIf(spineSupportFromSet -> spineSupportFromSet != spineSupportToKeep);
		}
	}

	boolean extractLeafSpines(final int[] leafSpines) {
		Utility.insist(positionBox.isLeafTierBox(), "must be a leaf box");
		Utility.insist(solutionSpineFacts.size() <= 1, "there must be at most one spine support");

		for(final SolutionSpineFact leafSpineFact : solutionSpineFacts) {
			leafSpines[positionBox.leftChildNode.nodeTerm]  = leafSpineFact.leftChildSpine;
			leafSpines[positionBox.rightChildNode.nodeTerm] = leafSpineFact.rightChildSpine;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "<SolutionBox "
			 +                         positionBox                                      + " "
//			 + "hadamardSupports="   + Utility.toStringFromSet(solutionHadamardFacts)   + " "
			 + "populationSupports=" + Utility.toStringFromSet(solutionPopulationFacts) + " "
			 + "spineSupports="      + Utility.toStringFromSet(solutionSpineFacts)
			 + ">";
	}

	public void rippleUp(
		final ButterflyOfHashSets<SolutionFact> solutionFactButterfly
	) {
		for(final SolutionFact leftChildSolutionFact : solutionFactButterfly.hashSetAt(positionBox.leftChildNode)) {
			for(final SolutionFact rightChildSolutionFact : solutionFactButterfly.hashSetAt(positionBox.rightChildNode)) {
				if(SolutionFact.couldMakeValidParents(leftChildSolutionFact, rightChildSolutionFact)
				&& positionBox.leftParentNode.wouldMakeValidParentHadamards(leftChildSolutionFact.hadamard, rightChildSolutionFact.hadamard)) {
					for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)/*positionBox.populationFacts()*/) {
						final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
						final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
						final SolutionSpineFact solutionSpineFact = SolutionSpineFact.newFact(leftChildSolutionFact, rightChildSolutionFact);
						final SolutionHadamardFact solutionHadamardFact = SolutionHadamardFact.newFact(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
						solutionFactButterfly.hashSetAt(positionBox.leftParentNode).add(leftParentSolutionFact);
						solutionFactButterfly.hashSetAt(positionBox.rightParentNode).add(rightParentSolutionFact);
					}
				}
			}			
		}
//		System.out.println();
	}
}
