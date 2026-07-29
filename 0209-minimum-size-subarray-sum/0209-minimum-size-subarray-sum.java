class Solution {
    public int minSubArrayLen(int target, int[] nums) {

        int lenOfSubArray = Integer.MAX_VALUE, sum = 0, left = 0, right = 0, len = nums.length;

        while (right < len) {
            sum += nums[right++];

            while (sum >= target) {
                lenOfSubArray = Math.min(lenOfSubArray, right - left);
                sum -= nums[left++];
            }

        }

        return lenOfSubArray == Integer.MAX_VALUE ? 0 : lenOfSubArray;
    }
}