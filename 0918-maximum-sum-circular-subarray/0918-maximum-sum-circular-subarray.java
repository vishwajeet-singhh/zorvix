class Solution {
    public int maxSubarraySumCircular(int[] nums) {

        int max = maxSum(nums);
        if (0 > max)
            return max;

        int array_sum = 0;

        for (int i = 0; i < nums.length; i++) {
            array_sum += nums[i];
            nums[i] = -nums[i];
        }

        int max_circular = array_sum + maxSum(nums);

        return Math.max(max_circular, max);

    }

    int maxSum(int[] nums) {

        int res = nums[0], max = nums[0];

        for (int i = 1; i < nums.length; i++) {

            max = Math.max(nums[i], max + nums[i]);
            res = Math.max(res, max);

        }

        return res;

    }
}