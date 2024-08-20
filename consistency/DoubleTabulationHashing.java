package consistency;

import java.util.Random;

// disallow 0 hash values, so each hash value distinguishes something at all
// disallow duplicate hash values, so each hash value distinguishes something different
// disallow the nots of hash values, so the probability that a bit changes does not change
// these are already low probability events, which don't really affect quality
// do no more, to avoid getting totally carried away

// NOTE: single tabulation hashing works much faster and may be good enough

public class DoubleTabulationHashing {
	private static final int PRIMARY_POSITIONS                =  10;
	private static final int HASH_TABLES_PER_PRIMARY_POSITION =   4;
	private static final int PRIMARY_HASH_TABLES              = PRIMARY_POSITIONS * HASH_TABLES_PER_PRIMARY_POSITION;
	private static final int SECONDARY_HASH_TABLES            =   8;
	private static final int ENTRIES_PER_HASH_TABLE           = 256;

	private static final int ARRAY_HASHING_INITIAL_VALUE      =   1;
	private static final int ARRAY_HASHING_MULTIPLIER         =  31;

	private static final Random                 randomNumberGenerator    = new Random();
	private static final SimpleHashSet<Long>    forbiddenPrimaryHashes   = SimpleHashSet.makeHashSet(new Long(0L), new Long(~0L));
	private static final SimpleHashSet<Integer> forbiddenSecondaryHashes = SimpleHashSet.makeHashSet(new Integer(0), new Integer(~0));
	private static final long[][]               primaryHashes            = makePrimaryHashes();
	private static final int[][]                secondaryHashes          = makeSecondaryHashes();

	public static long primaryHash(final byte value, final int position) {
		return primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 0][(value >>> 0) & 0xFF];
	}

	public static long primaryHash(final short value, final int position) {
		return primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 0][(value >>> 0) & 0xFF]
			 ^ primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 1][(value >>> 8) & 0xFF];
	}

	public static long primaryHash(final int value, final int position) {
		return primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 0][(value >>>  0) & 0xFF]
			 ^ primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 1][(value >>>  8) & 0xFF]
			 ^ primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 2][(value >>> 16) & 0xFF]
			 ^ primaryHashes[HASH_TABLES_PER_PRIMARY_POSITION * position + 3][(value >>> 24) & 0xFF];
	}

	public static long primaryHash(final int[] values, final int position) {
		return primaryHash(hashArray(values), position);
	}

	public static int secondaryHash(final long primaryHash) {
		return secondaryHashes[0][((int) (primaryHash >>>  0)) & 0xFF]
			 ^ secondaryHashes[1][((int) (primaryHash >>>  8)) & 0xFF]
			 ^ secondaryHashes[2][((int) (primaryHash >>> 16)) & 0xFF]
			 ^ secondaryHashes[3][((int) (primaryHash >>> 24)) & 0xFF]
			 ^ secondaryHashes[4][((int) (primaryHash >>> 32)) & 0xFF]
			 ^ secondaryHashes[5][((int) (primaryHash >>> 40)) & 0xFF]
			 ^ secondaryHashes[6][((int) (primaryHash >>> 48)) & 0xFF]
			 ^ secondaryHashes[7][((int) (primaryHash >>> 56)) & 0xFF];
	}

	// -----------------------------------------------------------------------
	// helpers

	private static long[][] makePrimaryHashes() {
		final long[][] primaryHashTables = new long[PRIMARY_HASH_TABLES][ENTRIES_PER_HASH_TABLE];
		for(final int table : Utility.enumerateAscending(PRIMARY_HASH_TABLES)) {
			for(final int entry : Utility.enumerateAscending(ENTRIES_PER_HASH_TABLE)) {
				primaryHashTables[table][entry] = nextValidPrimaryHash();
			}
		}
		return primaryHashTables;
	}

	private static int[][] makeSecondaryHashes() {
		final int[][] secondaryHashTables = new int[SECONDARY_HASH_TABLES][ENTRIES_PER_HASH_TABLE];
		for(final int table : Utility.enumerateAscending(SECONDARY_HASH_TABLES)) {
			for(final int entry : Utility.enumerateAscending(ENTRIES_PER_HASH_TABLE)) {
				secondaryHashTables[table][entry] = nextValidSecondaryHash();
			}
		}
		return secondaryHashTables;
	}

	private static long nextValidPrimaryHash() {
		while(true) {
			final long nextRandomLongPrimitive = randomNumberGenerator.nextLong();
			final Long nextRandomLongObject    = new Long(nextRandomLongPrimitive);
			final Long notNextRandomLongObject = new Long(~nextRandomLongPrimitive);
			if(!forbiddenPrimaryHashes.contains(nextRandomLongObject)) {
				forbiddenPrimaryHashes.add(nextRandomLongObject);
				forbiddenPrimaryHashes.add(notNextRandomLongObject);
				return nextRandomLongPrimitive;
			}
		}
	}

	private static int nextValidSecondaryHash() {
		while(true) {
			final int     nextRandomIntPrimitive = randomNumberGenerator.nextInt();
			final Integer nextRandomIntObject    = new Integer(nextRandomIntPrimitive);
			final Integer notNextRandomIntObject = new Integer(~nextRandomIntPrimitive);
			if(!forbiddenSecondaryHashes.contains(nextRandomIntObject)) {
				forbiddenSecondaryHashes.add(nextRandomIntObject);
				forbiddenSecondaryHashes.add(notNextRandomIntObject);
				return nextRandomIntPrimitive;
			}
		}
	}

	// TODO: should be better
	private static int hashArray(final int[] values) {
		int hash = ARRAY_HASHING_INITIAL_VALUE;
		for(final int value : values) {
			hash = ARRAY_HASHING_MULTIPLIER * hash + value;
		}
		return hash;
	}
}
