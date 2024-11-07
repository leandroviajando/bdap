import java.util.Arrays;

public class RadixSort {

    /**
     * Sorts an array of strings using the Radix Sort algorithm.
     *
     * @param arr the array of strings to be sorted
     * @return the sorted array of strings
     */
    public static String[] radixSort(String[] arr) {
        int maxLength = 0;
        for (String str : arr)
            if (str.length() > maxLength)
                maxLength = str.length();

        for (int pos = maxLength - 1; pos >= 0; pos--)
            countingSort(arr, pos);

        return arr;
    }

    /**
     * A stable counting sort algorithm that sorts the strings based on the
     * character at the given position.
     *
     * @param arr the array of strings to be sorted
     * @param pos the position of the character to sort by (0 is the rightmost)
     */
    private static void countingSort(String[] arr, int pos) {
        final int ASCII_RANGE = 256; // Extended ASCII

        int[] count = new int[ASCII_RANGE]; // Count of occurrences of characters at given pos
        Arrays.fill(count, 0);
        for (String str : arr) {

            char ch = '\0'; // Default character: for shorter strings
            if (pos < str.length())
                ch = str.charAt(pos); // Character in str at pos

            count[ch]++;
        }
        for (int i = 1; i < ASCII_RANGE; i++)
            count[i] += count[i - 1]; // Transform to cumulative count

        String[] output = new String[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {

            char ch = '\0'; // Default character: for shorter strings
            if (pos < arr[i].length())
                ch = arr[i].charAt(pos);

            output[count[ch] - 1] = arr[i];
            count[ch]--;
        }

        // Copy the output array back to arr
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    public static void main(String[] args) {
        String[] arr = { "apple", "banana", "pear", "kiwi", "avocado" };

        System.out.println("\nOriginal array: " + Arrays.toString(arr));
        String[] sorted = radixSort(arr);
        System.out.println("Sorted array: " + Arrays.toString(sorted));

        String[] arr2 = { "c3e", "bfr", "aAD", "Abb", "Rtr", "Ast", "ASb", "Arv", "ASf", "rr", "gtz", "tgk", "mdf",
                "wfrf", "rfrx", "nrfr", "ffro", "srf", "ufrf", "frry" };

        System.out.println("\nOriginal array: " + Arrays.toString(arr2));
        String[] sorted2 = radixSort(arr2);
        System.out.println("Sorted array: " + Arrays.toString(sorted2));
    }
}
