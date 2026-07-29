class Solution {
    public long maximumSubarraySum(int[] nums, int k) {
        long sum = 0, ans = 0;
        Map<Integer, Integer> freq = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            freq.put(nums[i], freq.getOrDefault(nums[i], 0) + 1);

            if (i >= k) {
                sum -= nums[i - k];
                int cnt = freq.get(nums[i - k]) - 1;
                if (cnt == 0)
                    freq.remove(nums[i - k]);
                else
                    freq.put(nums[i - k], cnt);
            }

            if (i >= k - 1 && freq.size() == k)
                ans = Math.max(ans, sum);
        }

        return ans;
    }
}