package consistency;

import java.util.HashSet;

class RelationBox {
	final PositionBox  positionBox;
	final EquationBox  equationBox;
	final SolutionBox  solutionBox;
	final RelationNode leftParentNode;
	final RelationNode rightParentNode;
	final RelationNode leftChildNode;
	final RelationNode rightChildNode;

	RelationBox(
		final EquationBox      equationBox,
		final SolutionBox      solutionBox,
		final RelationNode[][] solutionNodes
	) {
		final PositionBox positionBox = equationBox.positionBox;

		this.positionBox     = positionBox;
		this.equationBox     = equationBox;
		this.solutionBox     = solutionBox;
		this.leftParentNode  = solutionNodes[positionBox.leftParentNode.nodeTier][positionBox.leftParentNode.nodeTerm];
		this.rightParentNode = solutionNodes[positionBox.rightParentNode.nodeTier][positionBox.rightParentNode.nodeTerm];
		this.leftChildNode   = solutionNodes[positionBox.leftChildNode.nodeTier][positionBox.leftChildNode.nodeTerm];
		this.rightChildNode  = solutionNodes[positionBox.rightChildNode.nodeTier][positionBox.rightChildNode.nodeTerm];
	}

	// -----------------------------------------------------------------------
	// for relation butterflies

	// TODO: improve name
	void unionParentsUpRelation() {
		final RelationMultiMap validLeftParentRelationMultiMap  = new RelationMultiMap();
		final RelationMultiMap validRightParentRelationMultiMap = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										validLeftParentRelationMultiMap.add(leftParentEquationFact, leftParentSolutionFact);
										validRightParentRelationMultiMap.add(rightParentEquationFact, rightParentSolutionFact);
									}
								}
							}
						}
					}
				}
			}
		}

		leftParentNode.relationMultiMap.addAll(validLeftParentRelationMultiMap);
		rightParentNode.relationMultiMap.addAll(validRightParentRelationMultiMap);
	}

	// TODO: compare "could make valid parent functions"
	boolean intersectParentsUpRelation() {
		final RelationMultiMap validLeftParentRelationMultiMap = new RelationMultiMap();
		final RelationMultiMap validRightParentRelationMultiMap = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact  = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionFact leftParentSolutionFact  = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										if(leftParentNode.relationMultiMap.contains(leftParentEquationFact, leftParentSolutionFact)
										&& rightParentNode.relationMultiMap.contains(rightParentEquationFact, rightParentSolutionFact)) {
											validLeftParentRelationMultiMap.add(leftParentEquationFact, leftParentSolutionFact);
											validRightParentRelationMultiMap.add(rightParentEquationFact, rightParentSolutionFact);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean leftParentChanged  = leftParentNode.relationMultiMap.retainAll(validLeftParentRelationMultiMap);
		final boolean rightParentChanged = rightParentNode.relationMultiMap.retainAll(validRightParentRelationMultiMap);
		return leftParentChanged || rightParentChanged;
	}

	boolean intersectChildrenDownRelation() {
		final RelationMultiMap validLeftChildRelationMultiMap = new RelationMultiMap();
		final RelationMultiMap validRightChildRelationMultiMap = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact  = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionFact leftParentSolutionFact  = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										if(leftParentNode.relationMultiMap.contains(leftParentEquationFact, leftParentSolutionFact)
										&& rightParentNode.relationMultiMap.contains(rightParentEquationFact, rightParentSolutionFact)) {
											validLeftChildRelationMultiMap.add(leftChildEquationFact, leftChildSolutionFact);
											validRightChildRelationMultiMap.add(rightChildEquationFact, rightChildSolutionFact);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean leftChildChanged  = leftChildNode.relationMultiMap.retainAll(validLeftChildRelationMultiMap);
		final boolean rightChildChanged = rightChildNode.relationMultiMap.retainAll(validRightChildRelationMultiMap);
		return leftChildChanged || rightChildChanged;
	}

	// TODO: rename isValid()???
	void rippleUpRelation(
		final SupportAnswer    answer,
		final SolutionFact[][] solutionFactButterfly
	) {
		solutionFactButterfly[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm] = null;
		solutionFactButterfly[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm] = null;
		final SolutionFact leftChildSolutionFact = solutionFactButterfly[leftChildNode.positionNode.nodeTier][leftChildNode.positionNode.nodeTerm];
		final SolutionFact rightChildSolutionFact = solutionFactButterfly[rightChildNode.positionNode.nodeTier][rightChildNode.positionNode.nodeTerm];
		if(leftChildSolutionFact != null && rightChildSolutionFact != null) {
			final int leftParentPopulation = answer.populationButterfly[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm];
			final int rightParentPopulation = answer.populationButterfly[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm];
			final SolutionPopulationFact parentPopulationFact = SolutionPopulationFact.newFact(leftParentPopulation, rightParentPopulation);
			final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
			final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
			if(leftParentNode.relationMultiMap.getAllSolutionFacts().contains(leftParentSolutionFact)
			&& rightParentNode.relationMultiMap.getAllSolutionFacts().contains(rightParentSolutionFact)) {
				solutionFactButterfly[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm] = leftParentSolutionFact;
				solutionFactButterfly[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm] = rightParentSolutionFact;
			}
		}
	}

	// -----------------------------------------------------------------------
	// for solution butterflies

	// IMPORTANT
	boolean fillSolutionBoxesFromEquationBoxes() {
		final SolutionBox validSolutionBox = new SolutionBox(positionBox);

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionHadamardFact hadamardRelationFact = SolutionHadamardFact.newFact(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionSpineFact spineRelationFact = SolutionSpineFact.newFact(leftChildSolutionFact, rightChildSolutionFact);
										validSolutionBox.add(hadamardRelationFact, spineRelationFact, parentPopulationFact);
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean solutionBoxChanged = solutionBox.addAll(validSolutionBox);
		return solutionBoxChanged;
	}

	// IMPORTANT
	public boolean stripEverythingUpSolution() {
		final SolutionBox      validSolutionBox                 = new SolutionBox(positionBox);
		final RelationMultiMap validLeftParentRelationMultiMap  = new RelationMultiMap();
		final RelationMultiMap validRightParentRelationMultiMap = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionHadamardFact solutionHadamardFact = SolutionHadamardFact.newFact(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionSpineFact spineRelationFact = SolutionSpineFact.newFact(leftChildSolutionFact, rightChildSolutionFact);
										final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										if(solutionBox.contains(solutionHadamardFact, spineRelationFact, parentPopulationFact)
										&& parentNodesContain(leftParentSolutionFact, rightParentSolutionFact)) {
											validSolutionBox.add(solutionHadamardFact, spineRelationFact, parentPopulationFact);
											validLeftParentRelationMultiMap.add(leftParentEquationFact, leftParentSolutionFact);
											validRightParentRelationMultiMap.add(rightParentEquationFact, rightParentSolutionFact);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean solutionBoxChanged = solutionBox.retainAll(validSolutionBox);
		final boolean leftParentChanged  = leftParentNode.relationMultiMap.retainAll(validLeftParentRelationMultiMap);
		final boolean rightParentChanged = rightParentNode.relationMultiMap.retainAll(validRightParentRelationMultiMap);
		return solutionBoxChanged || leftParentChanged || rightParentChanged;
	}

	// IMPORTANT
	public boolean stripEverythingDownSolution() {
		final SolutionBox      validSolutionBox                = new SolutionBox(positionBox);
		final RelationMultiMap validLeftChildRelationMultiMap  = new RelationMultiMap();
		final RelationMultiMap validRightChildRelationMultiMap = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionHadamardFact solutionHadamardFact = SolutionHadamardFact.newFact(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionSpineFact solutionSpineFact = SolutionSpineFact.newFact(leftChildSolutionFact, rightChildSolutionFact);
										final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										if(solutionBox.contains(solutionHadamardFact, solutionSpineFact, parentPopulationFact)
										&& parentNodesContain(leftParentSolutionFact, rightParentSolutionFact)) {
											validSolutionBox.add(solutionHadamardFact, solutionSpineFact, parentPopulationFact);
											validLeftChildRelationMultiMap.add(leftChildEquationFact, leftChildSolutionFact);
											validRightChildRelationMultiMap.add(rightChildEquationFact, rightChildSolutionFact);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean solutionBoxChanged = solutionBox.retainAll(validSolutionBox);
		final boolean leftChildChanged   = leftChildNode.relationMultiMap.retainAll(validLeftChildRelationMultiMap);
		final boolean rightChildChanged  = rightChildNode.relationMultiMap.retainAll(validRightChildRelationMultiMap);
		return solutionBoxChanged || leftChildChanged || rightChildChanged;
	}

	public boolean intersectEverythingBothRelation() {
		final SolutionBox      validSolutionBox                 = new SolutionBox(positionBox);
		final RelationMultiMap validLeftParentRelationMultiMap  = new RelationMultiMap();
		final RelationMultiMap validRightParentRelationMultiMap = new RelationMultiMap();
		final RelationMultiMap validLeftChildRelationMultiMap   = new RelationMultiMap();
		final RelationMultiMap validRightChildRelationMultiMap  = new RelationMultiMap();

		for(final EquationFact leftChildEquationFact : leftChildNode.relationMultiMap.allEquationFacts()) {
			for(final EquationFact rightChildEquationFact : rightChildNode.relationMultiMap.allEquationFacts()) {
				if(wouldMakeValidParentEquationFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentEquationFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentEquationFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(equationBox.arePresentInParentNodes(leftParentEquationFact, rightParentEquationFact)) {
						for(final SolutionFact leftChildSolutionFact : leftChildNode.relationMultiMap.solutionFactsFor(leftChildEquationFact)) {
							for(final SolutionFact rightChildSolutionFact : rightChildNode.relationMultiMap.solutionFactsFor(rightChildEquationFact)) {
								if(wouldMakeValidParentSolutionFacts(leftChildSolutionFact, rightChildSolutionFact)) {
									for(final SolutionPopulationFact parentPopulationFact : SolutionPopulationFact.allParentPopulationFacts(positionBox.boxTier, leftChildSolutionFact.population)) {
										final SolutionHadamardFact solutionHadamardFact = SolutionHadamardFact.newFact(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionSpineFact solutionSpineFact = SolutionSpineFact.newFact(leftChildSolutionFact, rightChildSolutionFact);
										final SolutionFact leftParentSolutionFact = SolutionFact.newLeftParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										final SolutionFact rightParentSolutionFact = SolutionFact.newRightParent(leftChildSolutionFact, rightChildSolutionFact, parentPopulationFact);
										if(solutionBox.contains(solutionHadamardFact, solutionSpineFact, parentPopulationFact)
										&& parentNodesContain(leftParentSolutionFact, rightParentSolutionFact)) {
											validSolutionBox.add(solutionHadamardFact, solutionSpineFact, parentPopulationFact);
											validLeftParentRelationMultiMap.add(leftParentEquationFact, leftParentSolutionFact);
											validRightParentRelationMultiMap.add(rightParentEquationFact, rightParentSolutionFact);
											validLeftChildRelationMultiMap.add(leftChildEquationFact, leftChildSolutionFact);
											validRightChildRelationMultiMap.add(rightChildEquationFact, rightChildSolutionFact);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final boolean solutionBoxChanged = solutionBox.retainAll(validSolutionBox);
		final boolean leftParentChanged  = leftParentNode.relationMultiMap.retainAll(validLeftParentRelationMultiMap);
		final boolean rightParentChanged = rightParentNode.relationMultiMap.retainAll(validRightParentRelationMultiMap);
		final boolean leftChildChanged   = leftChildNode.relationMultiMap.retainAll(validLeftChildRelationMultiMap);
		final boolean rightChildChanged  = rightChildNode.relationMultiMap.retainAll(validRightChildRelationMultiMap);
		return solutionBoxChanged || leftParentChanged || rightParentChanged || leftChildChanged || rightChildChanged;
	}

	// -----------------------------------------------------------------------
	// helpers

	boolean parentNodesContain(
		final SolutionFact leftParentSolutionFact,
		final SolutionFact rightParentSolutionFact
	) {
		return leftParentNode.relationMultiMap.getAllSolutionFacts().contains(leftParentSolutionFact)
			&& rightParentNode.relationMultiMap.getAllSolutionFacts().contains(rightParentSolutionFact);
	}

	// Note: "would make valid parent" does not check domains, which are checked by parents being present
	boolean wouldMakeValidParentEquationFacts(
		final EquationFact leftChildEquationFact,
		final EquationFact rightChildEquationFact
	) {
		return (leftChildEquationFact.hadamard & 1) == (rightChildEquationFact.hadamard & 1);
	}

	boolean wouldMakeValidParentSolutionFacts(
		final SolutionFact leftChildSolutionFact,
		final SolutionFact rightChildSolutionFact
	) {
		return (leftChildSolutionFact.hadamard & 1) == (rightChildSolutionFact.hadamard & 1)
			&& (leftChildSolutionFact.spine & 1) == (rightChildSolutionFact.spine & 1)
			&& leftChildSolutionFact.population == rightChildSolutionFact.population;
	}

	public boolean rippleUpEquation(
		final SupportAnswer                     supportAnswer,
		final ButterflyOfHashSets<EquationFact> equationAnswer
	) {
		final SolutionFact leftParentSolutionFact  = supportAnswer.solutionButterfly[leftParentNode.positionNode.nodeTier][leftParentNode.positionNode.nodeTerm];
		final SolutionFact rightParentSolutionFact = supportAnswer.solutionButterfly[rightParentNode.positionNode.nodeTier][rightParentNode.positionNode.nodeTerm];
		final HashSet<EquationFact> leftChildEquationFacts   = equationAnswer.hashSetAt(leftChildNode.positionNode);
		final HashSet<EquationFact> rightChildEquationFacts  = equationAnswer.hashSetAt(rightChildNode.positionNode);
		final HashSet<EquationFact> leftParentEquationFacts  = equationAnswer.hashSetAt(leftParentNode.positionNode);
		final HashSet<EquationFact> rightParentEquationFacts = equationAnswer.hashSetAt(rightParentNode.positionNode);

		for(final EquationFact leftChildEquationFact : leftChildEquationFacts) {
			for(final EquationFact rightChildEquationFact : rightChildEquationFacts) {
				if(equationBox.wouldMakeValidParentFacts(leftChildEquationFact, rightChildEquationFact)) {
					final EquationFact leftParentFact = EquationFact.newLeftParent(leftChildEquationFact, rightChildEquationFact);
					final EquationFact rightParentFact = EquationFact.newRightParent(leftChildEquationFact, rightChildEquationFact);
					if(leftParentNode.relationMultiMap.contains(leftParentFact, leftParentSolutionFact)
					&& rightParentNode.relationMultiMap.contains(rightParentFact, rightParentSolutionFact)) {
						leftParentEquationFacts.add(leftParentFact);
						rightParentEquationFacts.add(rightParentFact);
					}
				}
			}
		}
		return !leftParentEquationFacts.isEmpty() && !rightParentEquationFacts.isEmpty();
	}

	// TODO: rename
	// IMPORTANT
	public boolean assignRandomSpineSupport() {
		return solutionBox.assignRandomSpineSupport();
	}

	@Override
	public String toString() {
		return "SolutionBox ["
			 + "positionBox="     + positionBox     + ", "
			 + "equationBox="     + equationBox     + ", "
//			 + "leftParentNode="  + leftParentNode  + ", "
//			 + "rightParentNode=" + rightParentNode + ", "
//			 + "leftChildNode="   + leftChildNode   + ", "
//			 + "rightChildNode="  + rightChildNode
			 + "]";
	}
}
