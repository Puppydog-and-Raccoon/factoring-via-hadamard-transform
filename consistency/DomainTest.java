package consistency;

import static org.junit.Assert.*;

import org.junit.Test;

public class DomainTest {
	@Test
	public void testStandardSize2() {
		final Domains[][] domainsButterfly = makeDomains(2, DomainGenerator.STANDARD);
		check(domainsButterfly[0][0],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][1], -1, 1, 1, 0, 2, 1, -1, 1, 1);
	}

	@Test
	public void testStandardSize4() {
		final Domains[][] domainsButterfly = makeDomains(4, DomainGenerator.STANDARD);
		check(domainsButterfly[0][0],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][1], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[1][2],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][3], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[2][0],  0, 4, 1, 0, 4, 1,  0, 4, 1);
		check(domainsButterfly[2][1], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][2], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][3], -2, 2, 1, 0, 4, 1, -2, 2, 1);
	}

	@Test
	public void testStandardSize8() {
		final Domains[][] domainsButterfly = makeDomains(8, DomainGenerator.STANDARD);
		check(domainsButterfly[0][0],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][4],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][5],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][6],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][7],  0, 1, 1, 0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][1], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[1][2],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][3], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[1][4],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][5], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[1][6],  0, 2, 1, 0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][7], -1, 1, 1, 0, 2, 1, -1, 1, 1);
		check(domainsButterfly[2][0],  0, 4, 1, 0, 4, 1,  0, 4, 1);
		check(domainsButterfly[2][1], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][2], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][3], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][4],  0, 4, 1, 0, 4, 1,  0, 4, 1);
		check(domainsButterfly[2][5], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][6], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[2][7], -2, 2, 1, 0, 4, 1, -2, 2, 1);
		check(domainsButterfly[3][0],  0, 8, 1, 0, 8, 1,  0, 8, 1);
		check(domainsButterfly[3][1], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][2], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][3], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][4], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][5], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][6], -4, 4, 1, 0, 8, 1, -4, 4, 1);
		check(domainsButterfly[3][7], -4, 4, 1, 0, 8, 1, -4, 4, 1);
	}

	@Test
	public void testBinaryGroupSize2() {
		final Domains[][] domainsButterfly = makeDomains(2, DomainGenerator.BINARY_GROUP);
		check(domainsButterfly[0][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][1], -1, 1, 2,  1, 1, 2, -1, 1, 2);
	}

	@Test
	public void testBinaryGroupSize4() {
		final Domains[][] domainsButterfly = makeDomains(4, DomainGenerator.BINARY_GROUP);
		check(domainsButterfly[0][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][1], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[1][2],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][3], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][0],  2, 2, 2,  2, 2, 2,  2, 2, 2);
		check(domainsButterfly[2][1], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[2][2],  0, 0, 2,  2, 2, 2,  0, 0, 2);
		check(domainsButterfly[2][3], -2, 2, 2,  2, 2, 2, -2, 2, 2);
	}

	@Test
	public void testBinaryGroupSize8() {
		final Domains[][] domainsButterfly = makeDomains(8, DomainGenerator.BINARY_GROUP);
		check(domainsButterfly[0][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][4],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][5],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][6],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][7],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][1], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[1][2],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][3], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[1][4],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][5], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[1][6],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[1][7], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][0],  2, 2, 2,  2, 2, 2,  2, 2, 2);
		check(domainsButterfly[2][1], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[2][2],  0, 0, 2,  2, 2, 2,  0, 0, 2);
		check(domainsButterfly[2][3], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[2][4],  2, 2, 2,  2, 2, 2,  2, 2, 2);
		check(domainsButterfly[2][5], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[2][6],  0, 0, 2,  2, 2, 2,  0, 0, 2);
		check(domainsButterfly[2][7], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][0],  4, 4, 2,  4, 4, 2,  4, 4, 2);
		check(domainsButterfly[3][1], -4, 4, 2,  4, 4, 2, -4, 4, 2);
		check(domainsButterfly[3][2],  0, 0, 2,  4, 4, 2,  0, 0, 2);
		check(domainsButterfly[3][3], -4, 4, 2,  4, 4, 2, -4, 4, 2);
		check(domainsButterfly[3][4],  0, 0, 2,  4, 4, 2,  0, 0, 2);
		check(domainsButterfly[3][5], -4, 4, 2,  4, 4, 2, -4, 4, 2);
		check(domainsButterfly[3][6],  0, 0, 2,  4, 4, 2,  0, 0, 2);
		check(domainsButterfly[3][7], -4, 4, 2,  4, 4, 2, -4, 4, 2);
	}

	@Test
	public void testGroupsOf4Size8() {
		final Domains[][] domainsButterfly = makeDomains(8, DomainGenerator.groupDomainGenerator(4));
		check(domainsButterfly[0][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][4],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][5],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][6],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][7],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][1], -1, 1, 1,  0, 1, 1, -1, 1, 1);
		check(domainsButterfly[1][2],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][3], -1, 1, 1,  0, 1, 1, -1, 1, 1);
		check(domainsButterfly[1][4],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][5], -1, 1, 1,  0, 1, 1, -1, 1, 1);
		check(domainsButterfly[1][6],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][7], -1, 1, 1,  0, 1, 1, -1, 1, 1);
		check(domainsButterfly[2][0],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[2][1], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][2], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][3], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][4],  1, 1, 2,  1, 1, 2,  1, 1, 2);
		check(domainsButterfly[2][5], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][6], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[2][7], -1, 1, 2,  1, 1, 2, -1, 1, 2);
		check(domainsButterfly[3][0],  2, 2, 2,  2, 2, 2,  2, 2, 2);
		check(domainsButterfly[3][1], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][2], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][3], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][4],  0, 0, 2,  2, 2, 2,  0, 0, 2);
		check(domainsButterfly[3][5], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][6], -2, 2, 2,  2, 2, 2, -2, 2, 2);
		check(domainsButterfly[3][7], -2, 2, 2,  2, 2, 2, -2, 2, 2);
	}

	@Test
	public void testBoundedGroup3Size4() {
		final Domains[][] domainsButterfly = makeDomains(4, DomainGenerator.boundedDomainGenerator(3, 4));
		check(domainsButterfly[0][0],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][1],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][2],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[0][3],  0, 1, 1,  0, 1, 1,  0, 1, 1);
		check(domainsButterfly[1][0],  0, 2, 1,  0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][1], -1, 1, 1,  0, 2, 1, -1, 1, 1);
		check(domainsButterfly[1][2],  0, 2, 1,  0, 2, 1,  0, 2, 1);
		check(domainsButterfly[1][3], -1, 1, 1,  0, 2, 1, -1, 1, 1);
		check(domainsButterfly[2][0],  3, 3, 1,  3, 3, 1,  3, 3, 1);
		check(domainsButterfly[2][1], -2, 2, 1,  3, 3, 1, -2, 2, 1);
		check(domainsButterfly[2][2], -2, 2, 1,  3, 3, 1, -2, 2, 1);
		check(domainsButterfly[2][3], -2, 2, 1,  3, 3, 1, -2, 2, 1);
	}

	private void check(
		final Domains domains,
		final int     hadamardMinimum,
		final int     hadamardMaximum,
		final int     hadamardStride,
		final int     populationMinimum,
		final int     populationMaximum,
		final int     populationStride,
		final int     spineMinimum,
		final int     spineMaximum,
		final int     spineStride
	) {
		assertEquals(domains.hadamard.minimum,   hadamardMinimum);
		assertEquals(domains.hadamard.maximum,   hadamardMaximum);
		assertEquals(domains.hadamard.stride,    hadamardStride);
		assertEquals(domains.population.minimum, populationMinimum);
		assertEquals(domains.population.maximum, populationMaximum);
		assertEquals(domains.population.stride,  populationStride);
		assertEquals(domains.spine.minimum,      spineMinimum);
		assertEquals(domains.spine.maximum,      spineMaximum);
		assertEquals(domains.spine.stride,       spineStride);
	}

	class Domains {
		final Domain hadamard;
		final Domain population;
		final Domain spine;

		Domains(final int nodeTier, final int nodeTerm, final DomainGenerator domainGenerator) {
			this.hadamard   = domainGenerator.domainForNode(nodeTier, nodeTerm);
			this.population = domainGenerator.domainForNode(nodeTier, Utility.populationNodeTerm(nodeTier, nodeTerm));
			this.spine      = domainGenerator.domainForNode(nodeTier, Utility.spineNodeTerm(nodeTier, nodeTerm));
		}
	}

	Domains[][] makeDomains(final int numberOfDecisions, final DomainGenerator domainGenerator) {
		final int numberOfNodeTiers = Utility.numberOfNodeTiers(numberOfDecisions);
		final int numberOfNodeTerms = Utility.numberOfNodeTerms(numberOfDecisions);

		final Domains[][] domains = new Domains[numberOfNodeTiers][numberOfNodeTerms];
		for(int nodeTier = 0; nodeTier < numberOfNodeTiers; nodeTier++) {
			for(int nodeTerm = 0; nodeTerm < numberOfNodeTerms; nodeTerm++) {
				domains[nodeTier][nodeTerm] = new Domains(nodeTier, nodeTerm, domainGenerator);
			}
		}
		return domains;
	}
}
