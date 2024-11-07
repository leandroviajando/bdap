import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * This class implements a bloom filter with k hash functions and m bits.
 * The bloom filter is used to check if an element is in a set or not.
 * The bloom filter can have false positives but no false negatives.
 * 
 * That is, if for at least one hash function h_i, B[h_i(e)] = 0, then the
 * element is definitely not in the set. However, if B[h_i(e)] = 1 for all h_i,
 * there is still a chance that the element may in fact not be in the set.
 */
public class BloomFilter {

    /** Total number of bits in the bloom filter */
    private int numBits;

    /** Number of hash functions */
    private int k;

    /** Vector to hold the bits */
    private Vector<Integer> bits;

    /**
     * Constructs a BloomFilter object with the specified number of bits and hash
     * functions.
     *
     * @param logNumBits the log of the number of bits in the bloom filter
     * @param k          the number of hash functions to be used
     */
    public BloomFilter(int logNumBits, int k) {
        this.numBits = 1 << logNumBits; // Bitwise left shift: 2^logNumBits - 1
        this.k = k;

        System.out.printf("%s = %s, %s = %s, %s = %s%n", "logNumBits", logNumBits, "numBits", numBits, "k", k);

        this.bits = new Vector<>(numBits);
        for (int i = 0; i < numBits; i++)
            bits.add(0);
    }

    /**
     * Adds the specified element to the bloom filter.
     *
     * @param s the element to be added
     */
    public void add(String s) {
        for (int i = 0; i < k; i++) {
            int index = getIndex(s, i);
            bits.set(index, 1);
        }
    }

    /**
     * Checks if the specified element is present in the bloom filter.
     *
     * @param s the element to be checked
     * @return false if any bit is not set, else true
     */
    public boolean check(String s) {
        for (int i = 0; i < k; i++) {
            int index = getIndex(s, i);
            if (bits.get(index) == 0)
                return false;
        }
        return true;
    }

    /**
     * Returns the index of the specified element in the bloom filter.
     *
     * @param element the element to get the index for
     * @param seed    the seed value for the hash function
     * @return the index of the element in the bloom filter
     */
    private int getIndex(String element, int seed) {
        byte[] bytes = element.getBytes(StandardCharsets.UTF_8);
        int hash = MurmurHash.hash32(bytes, bytes.length, seed);

        /**
         * Bitwise ANDs to ensure hash
         * 1. is non-negative (by clearing the sign-bit), and
         * 2. fits within bits vector size
         */
        return hash & 0x7FFFFFFF & (numBits - 1);
    }
}
