class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {

        int left = 0, right = 0, max = 0, n = nums.length;

        while (right < n) {

            if (1 != nums[right]) {
                max = Math.max(right - left, max);
                left = right + 1;
            }
            max = Math.max(right - left + 1, max);
            right++;
        }

        return max;
    }
}