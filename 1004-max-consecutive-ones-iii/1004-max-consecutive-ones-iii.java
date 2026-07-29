class Solution {
    public int longestOnes(int[] nums, int k) {
        int left = 0,
                right = 0,
                max_num = 0,
                util = 0;

        while (right < nums.length) {

            if (0 == nums[right])
                util++;
            right++;

            while (util > k) {

                if (0 == nums[left])
                    util--;

                left++;

            }

            max_num = Math.max(max_num, right - left);

        }

        return max_num;

    }
}