package consistency;

import java.util.Arrays;
import java.util.Vector;

public class ConsistencyState {
	private final ConsistencyProblem  consistencyProblem;
	private final PositionButterfly   positionButterfly;
	private final SolutionButterfly   solutionButterfly;
	private final EquationButterfly[] equationButterflies;
	private final RelationButterfly[] relationButterflies;

	ConsistencyState(
		final ConsistencyProblem consistencyProblem
	) {
		final PositionButterfly   positionButterfly   = new PositionButterfly(consistencyProblem.numberOfDecisionsInProblem, consistencyProblem.hadamardDomains);
		final SolutionButterfly   solutionButterfly   = new SolutionButterfly(positionButterfly);
		final EquationButterfly[] equationButterflies = newEquationButterflies(consistencyProblem, positionButterfly);
		final RelationButterfly[] relationButterflies = newRelationButterflies(equationButterflies, solutionButterfly);

		this.consistencyProblem  = consistencyProblem;
		this.positionButterfly   = positionButterfly;
		this.solutionButterfly   = solutionButterfly;
		this.equationButterflies = equationButterflies;
		this.relationButterflies = relationButterflies;
	}

	private static EquationButterfly[] newEquationButterflies(
		final ConsistencyProblem consistencyProblem,
		final PositionButterfly  positionButterfly
	) {
		final Vector<EquationButterfly> equationButterflies = new Vector<EquationButterfly>();
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			final EquationButterfly newEquationButterfly = new EquationButterfly(positionButterfly, consistencyConstraint, consistencyProblem.numberOfTruesInProblem);
			newEquationButterfly.fill();
			equationButterflies.add(newEquationButterfly);
		}		
		return equationButterflies.toArray(new EquationButterfly[0]);
	}

	private static RelationButterfly[] newRelationButterflies(
		final EquationButterfly[] equationButterflies,
		final SolutionButterfly   solutionButterfly
	) {
		final Vector<RelationButterfly> relationButterflies = new Vector<RelationButterfly>();
		for(final EquationButterfly equationButterfly : equationButterflies) {
			final RelationButterfly newRelationButterfly = new RelationButterfly(equationButterfly, solutionButterfly);
			newRelationButterfly.fillRelationUp();
			newRelationButterfly.wringRelationDown();
			relationButterflies.add(newRelationButterfly);
		}
		return relationButterflies.toArray(new RelationButterfly[0]);
	}

	// TODO: rename variables
	boolean fillAllRelationsUp() {
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			final boolean fillUpChanged    = applyBoxTierFirstRelationButterflyOnce(         boxTier, relationBox -> relationBox.fillSolutionBoxesFromEquationBoxes());
			final boolean stripUpChanged   = applyBoxTierAllRelationButterfliesUntilNoChange(boxTier, relationBox -> relationBox.stripEverythingUpSolution());
			final boolean transformChanged = transform(boxTier);
			anythingChanged = anythingChanged || fillUpChanged || stripUpChanged || transformChanged;
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			final boolean stripDownChanged = applyBoxTierAllRelationButterfliesUntilNoChange(boxTier, relationBox -> relationBox.stripEverythingDownSolution());
			final boolean transformChanged = transform(boxTier);
			anythingChanged = anythingChanged || stripDownChanged || transformChanged;
		}
		return anythingChanged;
	}

	void wringUntilNoChange() {
		while(wringOnce()) {
		}
	}

	private boolean wringOnce() {
		boolean anythingChanged = false;
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			final boolean thisChanged = applyBoxTierAllRelationButterfliesUntilNoChange(boxTier, relationBox -> relationBox.stripEverythingUpSolution());
			final boolean transformChanged = transform(boxTier);
			anythingChanged = anythingChanged || thisChanged || transformChanged;
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			final boolean thisChanged = applyBoxTierAllRelationButterfliesUntilNoChange(boxTier, relationBox -> relationBox.stripEverythingDownSolution());
			final boolean transformChanged = transform(boxTier);
			anythingChanged = anythingChanged || thisChanged || transformChanged;
		}
		return anythingChanged;
	}

	private boolean transform(final int boxTier) {
		final int spineSize = 1 << boxTier;
		final int populationSize = positionButterfly.numberOfBoxTerms / spineSize;
		return transform(boxTier, 0, spineSize, 0, populationSize);
	}

	// TODO; rename
	// don't need union because already filled - intersect
	private boolean transform(
		final int boxTier,
		final int spineStart,
		final int spineSize,
		final int populationStart,
		final int populationSize
	) {
		boolean anythingChanged = false;
		if(spineSize == 1) {
			// TODO: make area intersect
			// intersect along population - intersect along spine is implicit
			for(int i = 0; i < populationSize; i++) {
				final PositionBox positionBox = getBox(boxTier, spineStart, populationStart + i);
				final boolean thisChanged = doBoxIntersect(positionBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		} else if(populationSize == 1) {
			// TODO: make area intersect
			// intersect along spine - intersect along population is implicit
			for(int i = 0; i < spineSize; i++) {
				final PositionBox positionBox = getBox(boxTier, spineStart + i, populationStart);
				final boolean thisChanged = doBoxIntersect(positionBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		} else {
			transform(boxTier, spineStart,                 spineSize / 2, populationStart,                      populationSize / 2);
			transform(boxTier, spineStart,                 spineSize / 2, populationStart + populationSize / 2, populationSize / 2);
			transform(boxTier, spineStart + spineSize / 2, spineSize / 2, populationStart,                      populationSize / 2);
			transform(boxTier, spineStart + spineSize / 2, spineSize / 2, populationStart + populationSize / 2, populationSize / 2);
			// by spine
			final boolean a = doAreaIntersect(boxTier, spineStart, spineSize / 2, populationStart, populationSize);
			// by population
			final boolean b = doAreaIntersect(boxTier, spineStart, spineSize, populationStart, populationSize);
			// by spine
			final boolean c = doAreaIntersect(boxTier, spineStart + spineSize / 2, spineSize / 2, populationStart, populationSize);
			// by population
			final boolean d = doAreaIntersect(boxTier, spineStart, spineSize, populationStart, populationSize);
			// by spine
			final boolean e = doAreaIntersect(boxTier, spineStart, spineSize / 2, populationStart, populationSize);
			anythingChanged = anythingChanged || a || b || c || d || e;
		}
		return anythingChanged;
	}

	// this works, whether we are doing by spine or by population
	private boolean doAreaIntersect(
		final int boxTier,
		final int spineStart,
		final int spineSize,
		final int populationStart,
		final int populationSize
	) {
		boolean anythingChanged = false;
		// first pass computes the canonical intersections
		for(int s = 0; s < spineSize; s++) {
			for(int p = 0; p < populationSize; p++) {
				final PositionBox positionBox = getBox(boxTier, spineStart + s, populationStart + p);
				final boolean thisChanged = doBoxIntersect(positionBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		// second pass computes the support intersections
		for(int s = 0; s < spineSize; s++) {
			for(int p = 0; p < populationSize; p++) {
				final PositionBox positionBox = getBox(boxTier, spineStart + s, populationStart + p);
				final boolean thisChanged = doBoxIntersect(positionBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	private boolean doBoxIntersect(final PositionBox positionBox) {
		boolean anythingChanged = false;
		for(final int relationButterflyIndex : Utility.enumerateAscending(relationButterflies.length)) {
			final RelationBox relationBox = relationButterflies[relationButterflyIndex].getBox(positionBox);
			final boolean thisBoxChanged = relationBox.intersectEverythingBothRelation();
			anythingChanged = anythingChanged || thisBoxChanged;
		}
		return anythingChanged;
	}

	private PositionBox getBox(int boxTier, int i, int j) {
		final int numberOfSpines = 1 << boxTier;
		final int boxTerm = i + numberOfSpines * j;
		return positionButterfly.positionBoxes[boxTier][boxTerm];
	}

	void chooseSpineSolutionsDown() {
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final RelationBox relationBox = relationButterflies[0].relationBoxes[boxTier][boxTerm];
				if(relationBox.positionBox.isSpineTermBox()) {
					relationBox.assignRandomSpineSupport();
					wringUntilNoChange();
				}
			}
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
		final Vector<ButterflyOfHashSets<EquationFact>> equationAnswers = ButterflyOfHashSets.vectorOfButterflies(positionButterfly, relationButterflies.length);

		// fill up leaves
		for(final int leafNodeTerm : positionButterfly.nodeTermIndices) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(relationButterflies.length)) {
				final RelationNode relationNode = relationButterflies[relationButterflyIndex].relationNodes[positionButterfly.leafNodeTier][leafNodeTerm];
				boolean xxx = relationNode.rippleUpEquation(supportAnswer, equationAnswers.get(relationButterflyIndex));
			}
		}

		// fill up tiers
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(relationButterflies.length)) {
				for(final int boxTerm : positionButterfly.boxTermIndices) {
					final RelationBox relationBox = relationButterflies[relationButterflyIndex].relationBoxes[boxTier][boxTerm];
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

	private interface RelationBoxOperation {
		boolean operation(final RelationBox relationBox);
	}

	private boolean applyBoxTierFirstRelationButterflyOnce(
		final int                  boxTier,
		final RelationBoxOperation relationBoxOperation
	) {
		boolean anythingChanged = false;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			if(relationButterflies.length > 0) {
				final RelationBox relationBox = relationButterflies[0].relationBoxes[boxTier][boxTerm];
				final boolean thisChanged = relationBoxOperation.operation(relationBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	private boolean applyBoxTierAllRelationButterfliesUntilNoChange(
		final int                  boxTier,
		final RelationBoxOperation relationBoxOperation
	) {
		boolean anythingChanged = false;
		while(true) {
			final boolean thisChanged = applyBoxTierAllRelationButterfliesOnce(boxTier, relationBoxOperation);
			anythingChanged = anythingChanged || thisChanged;
			if(!thisChanged) {
				break;
			}
		}
		return anythingChanged;
	}

	// return true if and only if any box changed
	private boolean applyBoxTierAllRelationButterfliesOnce(
		final int                  boxTier,
		final RelationBoxOperation relationBoxOperation
	) {
		boolean anyBoxChanged = false;
		for(final int boxTerm : positionButterfly.boxTermIndices) {
			for(final int relationButterflyIndex : Utility.enumerateAscending(relationButterflies.length)) {
				final RelationBox relationBox = relationButterflies[relationButterflyIndex].relationBoxes[boxTier][boxTerm];
				final boolean thisBoxChanged = relationBoxOperation.operation(relationBox);
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
		fillAllRelationsUp();
//		validateAllAnswers(); // debug
		wringUntilNoChange();
	}

	@Override
	public String toString() {
		return "ConsistencyAlgorithm ["
			 + "solutionButterfly="   + solutionButterfly   + ", "
			 + "relationButterflies=" + relationButterflies
			 + "]";
	}
}
