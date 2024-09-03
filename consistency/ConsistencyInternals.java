package consistency;

import java.util.Arrays;
import java.util.Vector;

/**
 * This class implements the heart of the consistency algorithm.
 * The three main functions (constructor, merge bottom up, and extract top down)
 * were separated for debugging purposes.
 */
class ConsistencyInternals {
	/**
	 * Set this to false for factoring 2-bit numbers and true for everything else
	 */
	final static boolean OPTIMIZE_CONSTANTS = true;

	final ConsistencyProblem  consistencyProblem;
	final PropertyButterfly   propertyButterfly;
	final EquationButterfly[] equationButterflies;
	final SolutionButterfly   solutionButterfly;
	final LinkingButterfly[]  linkingButterflies;

	/**
	 * The constructor builds all of the data structures
	 * used to solve consistency problems.
	 * Most of the time is building the linking butterflies.
	 * 
	 * @param consistencyProblem the problem to solve
	 */
	ConsistencyInternals(
		final ConsistencyProblem consistencyProblem
	) {
		final PropertyButterfly   propertyButterfly   = PropertyButterfly.make(consistencyProblem);
		final EquationButterfly[] equationButterflies = makeEquationButterflies(consistencyProblem, propertyButterfly);
		final SolutionButterfly   solutionButterfly   = new SolutionButterfly(propertyButterfly);
		final LinkingButterfly[]  linkingButterflies  = makeLinkingButterflies(equationButterflies, solutionButterfly);

		this.consistencyProblem  = consistencyProblem;
		this.propertyButterfly   = propertyButterfly;
		this.equationButterflies = equationButterflies;
		this.solutionButterfly   = solutionButterfly;
		this.linkingButterflies  = linkingButterflies;
	}

	// this function works on multiple scales and scopes
	// leaf facts are simple, there is one fact per hadamard/spine value
	// leaf facts must have the same population value, but spine values differ for all nodes
	// there is no need for simultaneous within butterflies
	// there only needs to be agreement that the spine/hadamard value is the same for all butterflies
	// question: do we need to transfer linking butterfly changes to equation butterflies?
	void mergeBottomUp() {
		applyOpToLeafNodeTierInAllLinkingButterflies(linkingNode -> linkingNode.intersectToSharedState());
		applyOpToLeafNodeTierInAllLinkingButterflies(linkingNode -> linkingNode.intersectFromSharedState());

		for(final int boxTier : propertyButterfly.boxTierIndicesBottomUp) {
			System.out.println("merging box tier " + boxTier);
//			testSolutions("puppydog 1 - tier " + boxTier);
//			System.out.println("linking butterfly[0]");
//			System.out.println(linkingButterflies[0].nodeStatuses());
//			System.out.println("solution butterfly");
//			System.out.println(solutionButterfly.nodeStatuses());

			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceBottomUp());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectToSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectSpinePartialSumDeltasIntoSolutionDeltas());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectToSpineDeltas());
//			solutionButterfly.simultaneousForTier(boxTier);
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSpineDeltas());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedStateToSpinePartialSumsDelta());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceBottomUp());
		}
	}

	// return any solution, if at least one exists
	// return null, if no solution exists
	// this function works on multiple scales and scopes
	// question: should we transfer linking butterfly changes to equation butterflies?
	boolean[] extractTopDown() {
		for(final int boxTier : propertyButterfly.boxTierIndicesTopDown) {
			System.out.println("extracting box tier " + boxTier);
//			testSolutions("puppydog 2 - tier " + boxTier);
//			System.out.println("linking butterfly[0]");
//			System.out.println(linkingButterflies[0].boxStatuses());
//			System.out.println("solution butterfly");
//			System.out.println(solutionButterfly.boxStatuses());

			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceTopDown());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectToSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectSpinePartialSumDeltasIntoSolutionDeltas());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectToSpineDeltas());
			solutionButterfly.chooseSpineDeltasForTier(boxTier);
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSpineDeltas());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedStateToSpinePartialSumsDelta());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.intersectFromSharedState());
			applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceTopDown());
			wringBoxTiersBelowUntilNoChange(boxTier);
		}

		final int[] leafHadamards = linkingButterflies[0].leafHadamards();  // TODO: move to solution butterfly!!!
		return leafHadamards == null
			 ? null
			 : Utility.toBooleans(Utility.inverseFastSylvesterTransform(leafHadamards));
	}

	private boolean wringBoxTiersBelowUntilNoChange(final int topBoxTier) {
		boolean anythingChanged = false;
		while(wringBoxTiersBelowOnce(topBoxTier)) {
			anythingChanged = true;
		}
		return anythingChanged;
	}

	private boolean wringBoxTiersBelowOnce(final int topBoxTier) {
		boolean anythingChanged = false;
		for(int boxTier = topBoxTier + 1; boxTier <= propertyButterfly.leafBoxTier; boxTier++) {
			final boolean thisChanged = applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceTopDown());
			anythingChanged = anythingChanged || thisChanged;
		}
		for(int boxTier = propertyButterfly.leafBoxTier; boxTier >= topBoxTier + 1; boxTier--) {
			final boolean thisChanged = applyOpToBoxTierInAllLinkingButterflies(boxTier, linkingBox -> linkingBox.wringOnceBottomUp());
			anythingChanged = anythingChanged || thisChanged;
		}
		return anythingChanged;
	}

	// visible for testing and debugging
	boolean isValidEquationSolution(int[] rootHadamards) {
		Utility.insistEqual(rootHadamards.length, propertyButterfly.numberOfNodeTerms);

		boolean allAreValid = true;
		for(final EquationButterfly equationButterfly : equationButterflies) {
			final boolean equationButterflyIsValid  = equationButterfly.areValidRootHadamards(rootHadamards);
			allAreValid = allAreValid && equationButterflyIsValid;
		}
		return allAreValid;
	}

	// -----------------------------------------------------------------------
	// constructor helpers

	// TODO: do constant processing in a better way
	private static EquationButterfly[] makeEquationButterflies(
		final ConsistencyProblem consistencyProblem,
		final PropertyButterfly  propertyButterfly
	) {
		final Vector<EquationButterfly> equationButterflies = new Vector<EquationButterfly>();
		for(final ConsistencyConstraint consistencyConstraint : consistencyProblem.consistencyConstraints) {
			if(OPTIMIZE_CONSTANTS && consistencyConstraint.isConstant()) {
				System.out.println("skipping constant " + consistencyConstraint);
			} else {
				System.out.println("making equation butterfly for " + consistencyConstraint + " " + equationButterflies.size() + "/" + consistencyProblem.consistencyConstraints.size());
				final EquationButterfly equationButterfly = new EquationButterfly(propertyButterfly, consistencyConstraint);
				equationButterfly.fill(consistencyProblem.consistencyConstraints);
				equationButterflies.add(equationButterfly);
			}
		}
		return equationButterflies.toArray(new EquationButterfly[0]);
	}

	private static LinkingButterfly[] makeLinkingButterflies(
		final EquationButterfly[] equationButterflies,
		final SolutionButterfly   solutionButterfly
	) {
		Utility.insist(equationButterflies.length >= 2, "must have at least 2 equation butterfliies");

		final Vector<LinkingButterfly> linkingButterflies = new Vector<>();
		for(final int i : Utility.enumerateAscending(equationButterflies.length)) {
			for(final int j : Utility.enumerateAscending(equationButterflies.length)) {
				if(i < j) {
					System.out.println("linking butterfly " + i + " to butterfly " + j + " of " + equationButterflies.length);
					final boolean isFirstButterfly = linkingButterflies.size() == 0;
					final LinkingButterfly linkingButterfly = new LinkingButterfly(equationButterflies[i], equationButterflies[j], solutionButterfly, isFirstButterfly);
					linkingButterfly.fill();
					// doing wring once cuts time by 1/3, memory mostly same
					// removing wring cuts time by ~1/2, memory mostly same, but satisfied critter 21 failed
					// remove wring and doing equation butterfly wring once, memory mostly same
					// remove wring in both places blows up
//					linkingButterfly.wringUntilNoChange();
					linkingButterfly.wringOnce();
					linkingButterflies.add(linkingButterfly);
				}
			}
		}
		return linkingButterflies.toArray(new LinkingButterfly[0]);
	}

	// -----------------------------------------------------------------------
	// merge up and extract down helpers

	private interface LinkingNodeFunction {
		boolean apply(final LinkingNode linkingNode);
	}

	private boolean applyOpToLeafNodeTierInAllLinkingButterflies(
		final LinkingNodeFunction linkingNodeFunction
	) {
		boolean anythingChanged = false;
		for(final LinkingButterfly linkingButterfly : linkingButterflies) {
			for(final int nodeTerm : propertyButterfly.nodeTermIndicesForward) {
				final LinkingNode linkingNode = linkingButterfly.linkingNodes[propertyButterfly.leafNodeTier][nodeTerm];
				final boolean thisChanged = linkingNodeFunction.apply(linkingNode);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	private interface LinkingBoxFunction {
		boolean apply(final LinkingBox linkingBox);
	}

	private boolean applyOpToBoxTierInAllLinkingButterflies(
		final int                boxTier,
		final LinkingBoxFunction linkingBoxFunction
	) {
		boolean anythingChanged = false;
		for(final LinkingButterfly linkingButterfly : linkingButterflies) {
			for(final int boxTerm : propertyButterfly.boxTermIndicesForward) {
				final LinkingBox linkingBox = linkingButterfly.linkingBoxes[boxTier][boxTerm];
				final boolean thisChanged = linkingBoxFunction.apply(linkingBox);
				anythingChanged = anythingChanged || thisChanged;
			}
		}
		return anythingChanged;
	}

	// -----------------------------------------------------------------------
	// debugging helpers

	void testSolutions(
		final String context
	) {
		System.out.println(context);
		if(consistencyProblem.testSolutions == null) {
			System.out.println("test solutions == null");
		} else {
			System.out.println("test solutions");
			for(final int[] testSolution : consistencyProblem.testSolutions) {
				System.out.println(Arrays.toString(testSolution));
//				for(final EquationButterfly equationButterfly : equationButterflies) {
//					final boolean relationIsValid = equationButterfly.areValidRootHadamards(testSolution);
//					System.out.println(" sss " + equationButterfly.consistencyConstraint + " " + relationIsValid);
//				}
				for(final LinkingButterfly linkingButterfly : linkingButterflies) {
					final boolean solutionIsValid = linkingButterfly.isValidRootHadamards(testSolution);
					System.out.println(" sss " + linkingButterfly.firstEquationButterfly.consistencyConstraint + " " + linkingButterfly.secondEquationButterfly.consistencyConstraint + " " + solutionIsValid);
				}
			}
		}
	}

	void dumpSolutions() {
		System.out.println("\ndump solutions");
		if(consistencyProblem.testSolutions != null) {
			for(final int[] testSolution : consistencyProblem.testSolutions) {
//				System.out.println(solutionButterfly.nodeStatuses());
				final DebugButterfly<EquationFact> debugButterfly = new DebugButterfly<>(propertyButterfly, testSolution, equationButterflies[0].consistencyConstraint);
				System.out.println(debugButterfly.dump());
			}
		}
	}

	// -----------------------------------------------------------------------
	// boilerplate

	@Override
	public String toString() {
		return solutionButterfly.toString();
/*		return "ConsistencyAlgorithm ["
			 + "consistencyProblem=" + consistencyProblem + " "
			 + "propertyButterfly="  + propertyButterfly  + " "
			 + "]";
*/	}
}
