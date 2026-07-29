class Solution {
    public int findMin(int[] nums) {

        int len = nums.length;

        if (len == 0)
            return -1;

        if (len == 1)
            return nums[0];

        int left = 0;
        int right = len - 1;

        while (left < right) {

            int midpoint = left + (right - left) / 2;

            if (midpoint > 0 && nums[midpoint] < nums[midpoint - 1])
                return nums[midpoint];

            else if (nums[left] <= nums[midpoint] && nums[midpoint] > nums[right])
                left = midpoint + 1;
            else
                right = midpoint - 1;

        }

        return nums[left];

    }
}