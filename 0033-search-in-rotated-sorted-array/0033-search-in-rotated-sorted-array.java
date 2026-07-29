class Solution {
    public int search(int[] nums, int target) {

        // [4,5,6,7,0,1,2] = mid - 7 
        //[1,3] - len = 2 mid = 0

        int left = 0;
        int right = nums.length - 1;

        while (left < right) { // 0,1
            int midpoint = left + (right - left) / 2; // 0

            if (nums[midpoint] == target) // 1 
                return midpoint;
            else if (nums[midpoint] > nums[right]) // 
                left = midpoint + 1;
            else
                right = midpoint; // 1 
        }

        int low = 0, high = nums.length - 1;

        if (target >= nums[left] && target <= nums[high])
            low = left;
        else
            high = left-1;

        while (low <= high) {

            int midpoint = low + (high - low) / 2;

            if (nums[midpoint] == target)
                return midpoint;

            else if (nums[midpoint] > target)
                high = midpoint - 1;
            else
                low = midpoint + 1;

        }

        return -1;

    }
}