package consistency;

// TODO: rename

class SupportAnswer {
	final PositionButterfly          positionButterfly;
	final int[]                      rootVector;
	final int[][]                    hadamardButterfly;
	final int[][]                    spineButterfly;
	final int[][]                    populationButterfly;
	final SolutionFact[][]           solutionButterfly;
	final SolutionSpineFact[][]      supportSpineButterfly;
	final SolutionPopulationFact[][] supportPopulationButterfly;

	SupportAnswer(final PositionButterfly positionButterfly, final int[] rootVector) {
		Utility.insist(positionButterfly.numberOfNodeTerms == rootVector.length, "must be the same size");

		final int[][]                    hadamardButterfly          = Utility.sylvesterButterfly(rootVector);
		final int[][]                    spineButterfly             = newSpineButterfly(positionButterfly, hadamardButterfly);
		final int[][]                    populationButterfly        = newPopulationButterfly(positionButterfly, hadamardButterfly);
		final SolutionFact[][]           solutionButterfly          = newSolutionButterfly(positionButterfly, hadamardButterfly, spineButterfly, populationButterfly);
		final SolutionSpineFact[][]      supportSpineButterfly      = newSupportSpineButterfly(positionButterfly, spineButterfly);
		final SolutionPopulationFact[][] supportPopulationButterfly = newSupportPopulationButterfly(positionButterfly, populationButterfly);

		this.positionButterfly          = positionButterfly;
		this.rootVector                 = rootVector;
		this.hadamardButterfly          = hadamardButterfly;
		this.spineButterfly             = spineButterfly;
		this.populationButterfly        = populationButterfly;
		this.solutionButterfly          = solutionButterfly;
		this.supportSpineButterfly      = supportSpineButterfly;
		this.supportPopulationButterfly = supportPopulationButterfly;
	}

	static int[][] newSpineButterfly(
		final PositionButterfly positionButterfly,
		final int[][]           hadamardButterfly
	) {
		final int[][] spineButterfly = new int[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTerm : positionButterfly.nodeTermIndices) {
			assignValue(spineButterfly, hadamardButterfly, positionButterfly.leafPositionNodes[nodeTerm]);
		}
		for(final int boxTier : positionButterfly.boxTierIndicesBottomUp) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				assignSpineParents(spineButterfly, positionButterfly.positionBoxes[boxTier][boxTerm]);
			}
		}
		return spineButterfly;
	}

	static void assignSpineParents(
		final int[][]     spineButterfly,
		final PositionBox positionBox
	) {
		final int leftChildSpine  = getFromButterfly(spineButterfly, positionBox.leftChildNode);
		final int rightChildSpine = getFromButterfly(spineButterfly, positionBox.rightChildNode);
		final int parentSpine     = (leftChildSpine + rightChildSpine) / 2;
		setToButterfly(spineButterfly, positionBox.leftParentNode, parentSpine);
		setToButterfly(spineButterfly, positionBox.rightParentNode, parentSpine);
		Utility.insistSameParity(leftChildSpine, rightChildSpine);
	}

	static int[][] newPopulationButterfly(
		final PositionButterfly positionButterfly,
		final int[][]           hadamardButterfly
	) {
		final int[][] populationButterfly = new int[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTerm : positionButterfly.nodeTermIndices) {
			assignValue(populationButterfly, hadamardButterfly, positionButterfly.rootPositionNodes[nodeTerm]);
		}
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				assignPopulationChildren(populationButterfly, positionButterfly.positionBoxes[boxTier][boxTerm]);
			}
		}
		return populationButterfly;
	}

	static void assignPopulationChildren(final int[][] populationButterfly, final PositionBox positionBox) {
		final int leftParentPopulation  = getFromButterfly(populationButterfly, positionBox.leftParentNode);
		final int rightParentPopulation = getFromButterfly(populationButterfly, positionBox.rightParentNode);
		final int childPopulation       = leftParentPopulation + rightParentPopulation;
		setToButterfly(populationButterfly, positionBox.leftChildNode, childPopulation);
		setToButterfly(populationButterfly, positionBox.rightChildNode, childPopulation);
	}

	static void assignValue(int[][] toButterfly, int[][] fromButterfly, PositionNode positionNode) {
		toButterfly[positionNode.nodeTier][positionNode.nodeTerm] = fromButterfly[positionNode.nodeTier][positionNode.nodeTerm];
	}

	static int getFromButterfly(final int[][] butterfly, final PositionNode positionNode) {
		return butterfly[positionNode.nodeTier][positionNode.nodeTerm];
	}

	static int setToButterfly(final int[][] butterfly, final PositionNode positionNode, final int value) {
		return butterfly[positionNode.nodeTier][positionNode.nodeTerm] = value;
	}

	static SolutionFact[][] newSolutionButterfly(
		final PositionButterfly positionButterfly,
		final int[][]           hadamardButterfly,
		final int[][]           spineButterfly,
		final int[][]           populationButterfly
	) {
		final SolutionFact[][] solutionFacts = new SolutionFact[positionButterfly.numberOfNodeTiers][positionButterfly.numberOfNodeTerms];
		for(final int nodeTier : positionButterfly.nodeTierIndicesTopDown) {
			for(final int nodeTerm : positionButterfly.nodeTermIndices) {
				final int hadamard   = hadamardButterfly[nodeTier][nodeTerm];
				final int spine      = spineButterfly[nodeTier][nodeTerm];
				final int population = populationButterfly[nodeTier][nodeTerm];
				solutionFacts[nodeTier][nodeTerm] = SolutionFact.newFact(hadamard, spine, population);
			}
		}
		return solutionFacts;
	}

	static SolutionSpineFact[][] newSupportSpineButterfly(
		final PositionButterfly positionButterfly,
		final int[][]           spineButterfly
	) {
		final SolutionSpineFact[][] results = new SolutionSpineFact[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final PositionBox positionBox     = positionButterfly.positionBoxes[boxTier][boxTerm];
				final int         leftChildSpine  = getFromButterfly(spineButterfly, positionBox.leftChildNode);
				final int         rightChildSpine = getFromButterfly(spineButterfly, positionBox.rightChildNode);
				results[positionBox.boxTier][positionBox.boxTerm] = SolutionSpineFact.newFact(leftChildSpine, rightChildSpine);
			}
		}
		return results;
	}

	static SolutionPopulationFact[][] newSupportPopulationButterfly(
		final PositionButterfly positionButterfly,
		final int[][]           populationButterfly
	) {
		final SolutionPopulationFact[][] results = new SolutionPopulationFact[positionButterfly.numberOfBoxTiers][positionButterfly.numberOfBoxTerms];
		for(final int boxTier : positionButterfly.boxTierIndicesTopDown) {
			for(final int boxTerm : positionButterfly.boxTermIndices) {
				final PositionBox positionBox           = positionButterfly.positionBoxes[boxTier][boxTerm];
				final int         leftParentPopulation  = getFromButterfly(populationButterfly, positionBox.leftParentNode);
				final int         rightParentPopulation = getFromButterfly(populationButterfly, positionBox.rightParentNode);
				results[positionBox.boxTier][positionBox.boxTerm] = SolutionPopulationFact.newFact(leftParentPopulation, rightParentPopulation);
			}
		}
		return results;
	}

	@Override
	public String toString() {
		return "SupportAnswer ["
			 + "positionButterfly="          + positionButterfly          + ", "
			 + "rootVector="                 + rootVector                 + ", "
			 + "hadamardButterfly="          + hadamardButterfly          + ", "
			 + "spineButterfly="             + spineButterfly             + ", "
			 + "populationButterfly="        + populationButterfly        + ", "
			 + "supportSpineButterfly="      + supportSpineButterfly      + ", "
			 + "supportPopulationButterfly=" + supportPopulationButterfly
			 + "]";
	}
}
