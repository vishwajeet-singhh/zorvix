class Solution {
    public int minSubArrayLen(int target, int[] nums) {

        int left = 0, len = nums.length, sum = 0, min = Integer.MAX_VALUE;

        /* left = 0
           right= 1
        sum =  10
         min = 
        */
        for (int right = 0; right < len; right++) {
            sum += nums[right];

            while (sum >= target) {
                min = Math.min(min, right - left + 1);
                sum -= nums[left];
                left++;
            }

        }

        return min == Integer.MAX_VALUE ? 0 : min;

    }
}