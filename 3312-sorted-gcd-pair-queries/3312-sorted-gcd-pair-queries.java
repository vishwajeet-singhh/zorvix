import java.util.*;

class Solution {
    public int[] gcdValues(int[] nums, long[] queries) {

        int max = 0;
        for (int x : nums) {
            max = Math.max(max, x);
        }

        int[] freq = new int[max + 1];
        for (int x : nums) {
            freq[x]++;
        }

        int[] cntDiv = new int[max + 1];
        for (int d = 1; d <= max; d++) {
            for (int multiple = d; multiple <= max; multiple += d) {
                cntDiv[d] += freq[multiple];
            }
        }

        long[] exactPairs = new long[max + 1];

        for (int d = max; d >= 1; d--) {
            long cnt = cntDiv[d];
            long pairs = cnt * (cnt - 1) / 2;

            for (int multiple = d * 2; multiple <= max; multiple += d) {
                pairs -= exactPairs[multiple];
            }

            exactPairs[d] = pairs;
        }

        long[] prefix = new long[max + 1];
        for (int d = 1; d <= max; d++) {
            prefix[d] = prefix[d - 1] + exactPairs[d];
        }

        int[] ans = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            long k = queries[i] + 1; 

            int left = 1, right = max;
            while (left < right) {
                int mid = left + (right - left) / 2;

                if (prefix[mid] >= k)
                    right = mid;
                else
                    left = mid + 1;
            }

            ans[i] = left;
        }

        return ans;
    }
}