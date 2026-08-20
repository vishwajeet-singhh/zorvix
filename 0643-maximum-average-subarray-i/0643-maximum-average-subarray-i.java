class Solution {
    public double findMaxAverage(int[] nums, int k) {
        double max = Integer.MIN_VALUE, sum = 0;

        for (int i = 0; i < k; i++)
            sum += nums[i];

        if (nums.length <= k) {
            return (double) sum / nums.length;
        }

        double avg = sum / k;
        max = Math.max(avg, max);

        int left = 0;
        int right = k;

        while (right < nums.length) {
            sum -= nums[left];
            sum += nums[right];

            avg = sum / k;
            max = Math.max(avg, max);
            left++;
            right++;
        }

        return max;

    }
}