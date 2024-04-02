package consistency;

import java.util.Arrays;
import java.util.Vector;

public class ConsistencyState {
	private final ConsistencyProblem        consistencyProblem;
	private final PositionButterfly         positionButterfly;
	private final SolutionButterfly         solutionButterfly;
	private final Vector<EquationButterfly> equationButterflies;
	private final Vector<RelationButterfly> relationButterflies;

	ConsistencyState(
		final ConsistencyProblem consistencyProblem
	) {
		final PositionButterfly         positionButterfly   = new PositionButterfly(consistencyProblem.numberOfDecisionsInProblem, consistencyProblem.hadamardDomains);
		final SolutionButterfly         solutionButterfly   = new SolutionButterfly(positionButterfly, consistencyProblem);
		final Vector<EquationButterfly> equationButterflies = new Vector<EquationButterfly>();
		final Vector<RelationButterfly> relationButterflies = new Vector<RelationButterfly>();

		this.consistencyProblem  = consistencyProblem;
		this.positionButterfly   = positionButterfly;
		this.solutionButterfly   = solutionButterfly;
		this.equationButterflies = equationButterflies;
		this.relationButterflies = relationButterflies;

		fillEquationButterflies();
		fillRelationButterflies();
	}

	private void fillEquationButterflies() {
		int i = 0;
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			if(!consistencyConstraint.isConstant()) {
				System.out.println("making equation butterfly for " + consistencyConstraint + " " + i++ + "/" + consistencyProblem.consistencyConstraints.size());
				final EquationButterfly newEquationButterfly = new EquationButterfly(positionButterfly, consistencyConstraint, consistencyProblem.numberOfTruesInProblem);
				newEquationButterfly.fill(consistencyProblem.consistencyConstraints);
				equationButterflies.add(newEquationButterfly);
			}
		}
	}

	private void fillRelationButterflies() {
		int i = 0;
		for(final EquationButterfly equationButterfly : equationButterflies) {
			System.out.println("making relation butterfly for " + equationButterfly.consistencyConstraint + " " + i++ + "/" + equationButterflies.size());
			final RelationButterfly newRelationButterfly = new RelationButterfly(equationButterfly, solutionButterfly);
			newRelationButterfly.fillRelationUp();
			newRelationButterfly.wringRelationDown();
			relationButterflies.add(newRelationButterfly);
			if(i == 1) {
				fillSolutionButterfly();
			} else {
				wringUntilNoChange();
			}
		}
	}

	boolean fillSolutionButterfly() {
		boolean anythingChanged = false;
		{
			boolean saturateChanged = solutionButterfly.saturateSolutionLeaves();
			anythingChanged = anythingChanged || saturateChanged;
		}
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			final boolean fillUpChanged    = applyBoxTierFirstRelationButterflyOnce(boxTier);
			anythingChanged = anythingChanged || fillUpChanged;
		}
		System.out.println("aaa " + solutionButterfly.status());
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			final boolean stripUpChanged   = intersectBoxTierAllButterfliesUntilNoChange(boxTier);
			final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
			anythingChanged = anythingChanged || stripUpChanged || transformChanged;
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			final boolean stripDownChanged = intersectBoxTierAllButterfliesUntilNoChange(boxTier);
			final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
			anythingChanged = anythingChanged || stripDownChanged || transformChanged;
		}
		System.out.println("bbb " + solutionButterfly.status());
		return anythingChanged;
	}

	void wringUntilNoChange() {
		while(wringOnce()) {
		}
	}

	boolean wringOnce() {
		System.out.println("wring");
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			final boolean thisChanged = intersectBoxTierAllButterfliesUntilNoChange(boxTier);
			final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
			anythingChanged = anythingChanged || thisChanged || transformChanged;
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			final boolean thisChanged = intersectBoxTierAllButterfliesUntilNoChange(boxTier);
			final boolean transformChanged = solutionButterfly.wringTierBothDirections(boxTier);
			anythingChanged = anythingChanged || thisChanged || transformChanged;
		}
		return anythingChanged;
	}

	void chooseSpineSolutionsDown() {
		for(ConsistencyConstraint constraint : consistencyProblem.consistencyConstraints) {
			System.out.println(" ccc " + constraint);
		}
		chooseSpineSolutionsDown(0, 0);
	}

	void chooseSpineSolutionsDown(
		final int boxTier,
		final int boxTerm
	) {
		final RelationBox relationBox = relationButterflies.get(0).relationBoxes[boxTier][boxTerm];
		final boolean changed = relationBox.assignRandomSpineSupport();
		if(changed) {
			wringUntilNoChange();
		}
		if(boxTier < positionButterfly.numberOfBoxTiers - 1) {
			chooseSpineSolutionsDown(boxTier + 1, boxTerm);
			chooseSpineSolutionsDown(boxTier + 1, boxTerm + (1 << boxTier));
		}
	}

	boolean[] extractDecisions() {
		final int[] leafHadamards = solutionButterfly.leafSpines();
		if(leafHadamards == null) {
			return null;
		} else {
			final int[]     rootHadamards = Utility.inverseFastSylvesterTransform(leafHadamards);
			final boolean[] decisions     = Utility.toBooleans(rootHadamards);
			return decisions;
		}
	}

	boolean isValidSolution(final int[] rootHadamards) {
		Utility.insist(rootHadamards.length == positionButterfly.numberOfNodeTerms, "vector is wrong size: " + rootHadamards.length + " != " + positionButterfly.numberOfBoxTerms);

		final SupportAnswer supportAnswer = new SupportAnswer(positionButterfly, rootHadamards);
		final Vector<ButterflyOfHashSets<EquationFact>> equationAnswers = ButterflyOfHashSets.vectorOfButterflies(positionButterfly, equationButterflies.size());

		// fill up leaves
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(equationButterflies.size())) {
				final RelationNode relationNode = relationButterflies.get(relationButterflyIndex).relationNodes[positionButterfly.leafNodeTier][leafNodeTerm];
				boolean xxx = relationNode.rippleUpEquation(supportAnswer, equationAnswers.get(relationButterflyIndex));
			}
		}

		// fill up tiers
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(equationButterflies.size())) {
				for(final int boxTerm : positionButterfly.boxTermIndices) {
					final RelationBox relationBox = relationButterflies.get(relationButterflyIndex).relationBoxes[boxTier][boxTerm];
					boolean xxx = relationBox.rippleUpEquation(supportAnswer, equationAnswers.get(relationButterflyIndex));
				}
			}
		}

		// compute all are valid
		boolean allAreValid = true;
		for(final int equationAnswerIndex : Utility.enumerateAscending(equationAnswers.size())) {
			allAreValid = allAreValid && equationAnswers.get(equationAnswerIndex).isValid();
		}
		return allAreValid;
	}

	private boolean applyBoxTierFirstRelationButterflyOnce(
		final int boxTier
	) {
		boolean anythingChanged = false;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			if(equationButterflies.size() > 0) {
				final RelationBox relationBox = relationButterflies.get(0).relationBoxes[boxTier][boxTerm];
				final boolean thisChanged = relationBox.fillSolutionBoxesFromEquationBoxes();
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	private boolean intersectBoxTierAllButterfliesUntilNoChange(
		final int boxTier
	) {
		boolean anythingChanged = false;
		while(true) {
			final boolean thisChanged = intersectBoxTierAllButterfliesOnce(boxTier);
			anythingChanged = anythingChanged || thisChanged;
			if(!thisChanged) {
				break;
			}
		}
		return anythingChanged;
	}

	// return true if and only if any box changed
	private boolean intersectBoxTierAllButterfliesOnce(
		final int boxTier
	) {
		boolean anyBoxChanged = false;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(relationButterflies.size())) {
				final RelationBox relationBox = relationButterflies.get(relationButterflyIndex).relationBoxes[boxTier][boxTerm];
				final boolean thisBoxChanged = relationBox.intersectEverythingBothRelation();
				anyBoxChanged = anyBoxChanged || thisBoxChanged;
			}
		}
		return anyBoxChanged;
	}

	void validateAllAnswersAllEquationButterflies() {
		final boolean[] vector = new boolean[positionButterfly.numberOfNodeTerms];
		initVector(vector);
		for(;;) {
			if(Arrays.equals(vector, new boolean[]{true, true, false, false, false, false, false, false})) {
				final ConsistencySolution consistencySolution = new ConsistencySolution(consistencyProblem, vector, null);
				final boolean shouldBeValid = consistencySolution.isValid();
				final boolean isValid = isValidEquationSolution(Utility.toInts(vector));
				if(shouldBeValid != isValid) {
					final String message = "is " + isValid + " but should be " + shouldBeValid + ": " + Arrays.toString(vector);
					throw new RuntimeException(message);
				}
			}
			if(!nextVector(vector)) {
				break;
			}
		}
	}

	private boolean isValidEquationSolution(int[] rootHadamards) {
		Utility.insist(rootHadamards.length == positionButterfly.numberOfNodeTerms, "vector is wrong size: " + rootHadamards.length + " != " + positionButterfly.numberOfBoxTerms);

		boolean allAreValid = true;
		for(final EquationButterfly equationButterfly : equationButterflies) {
			allAreValid = allAreValid && equationButterfly.isValid(rootHadamards);
		}
		return allAreValid;
	}

	void validateAllAnswers() {
		final boolean[] vector = new boolean[positionButterfly.numberOfNodeTerms];
		initVector(vector);
		for(;;) {
			if(Arrays.equals(vector, new boolean[]{true, true, false, false, false, false, false, false})) {
				final ConsistencySolution consistencySolution = new ConsistencySolution(consistencyProblem, vector, null);
				final boolean shouldBeValid = consistencySolution.isValid();
				final boolean isValid = isValidSolution(Utility.toInts(vector));
				if(shouldBeValid != isValid) {
					final String message = "is " + isValid + " but should be " + shouldBeValid + ": " + Arrays.toString(vector);
					throw new RuntimeException(message);
				}
			}
			if(!nextVector(vector)) {
				break;
			}
		}
	}

	private void initVector(final boolean[] vector) {
		Arrays.fill(vector, false);
	}

	private boolean nextVector(final boolean[] vector) {
		for(int i = 0; i < consistencyProblem.coreSize; i++) {
			vector[i] = !vector[i];
			if(vector[i]) {
				fillNonCoreDecisions(vector);
				return true;
			}
		}
		return false;
	}

	private void fillNonCoreDecisions(boolean[] vector) {
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			if(consistencyConstraint.numberOfTruesInConstraint == 1) {
				final int   numberOfTruesInConstraint = consistencyConstraint.numberOfTruesInConstraint;
				final int[] decisionIds               = consistencyConstraint.sortedAndUniqueDecisionIds;
				final int   coreSize                  = consistencyProblem.coreSize;
				if(decisionIds.length == 3 && decisionIds[0] < coreSize && decisionIds[1] < coreSize && decisionIds[2] >= coreSize) {
					final int coreSum = Utility.toInt(vector[decisionIds[0]]) + Utility.toInt(vector[decisionIds[1]]);
					vector[decisionIds[2]] = Utility.toBoolean(numberOfTruesInConstraint - coreSum);
				}
				if(decisionIds.length == 1 && decisionIds[0] >= coreSize) {
					vector[decisionIds[0]] = Utility.toBoolean(numberOfTruesInConstraint);
				}
			}
		}
	}

	void solve() {
//		validateAllAnswersAllEquationButterflies(); // debug
		fillSolutionButterfly();
//		validateAllAnswers(); // debug
		wringUntilNoChange();
//		System.out.println("rrr " + solutionButterfly.status()); // debug
	}

	@Override
	public String toString() {
		return "ConsistencyAlgorithm ["
			 + "solutionButterfly="   + solutionButterfly   + ", "
			 + "relationButterflies=" + relationButterflies
			 + "]";
	}
}
