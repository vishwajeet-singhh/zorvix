class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {

        int left = 0,
                right = 0;

        int maxConsecutiveOnes = 0;

        while (right < nums.length) {

            if (1 == nums[right])
                right++;
            else
                left = right = right + 1;

            maxConsecutiveOnes = Math.max(maxConsecutiveOnes, right - left);

        }

        return maxConsecutiveOnes;

    }
}