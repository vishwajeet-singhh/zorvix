class Solution {
    public int[] sortedSquares(int[] nums) {

        int length = nums.length;
        if (length == 1) {
            nums[0] = nums[0] * nums[0];
            return nums;
        }

        int[] output_array = new int[length];

        int left = 0;
        int right = length - 1;

        int candidate = right;

        while (left <= right) {

            int left_value = Math.abs(nums[left]);
            int right_value = Math.abs(nums[right]);

            if (left_value >= right_value) {
                output_array[candidate] = left_value * left_value;
                left++;
            } else {
                output_array[candidate] = right_value * right_value;
                right--;
            }
            candidate--;
        }

        return output_array;

    }
}