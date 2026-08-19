class Solution {
    public int[] sortArrayByParity(int[] nums) {

        int n = nums.length;

        int insert = 0;
        int point = 0;

        while (point < n) {

            if (nums[point] % 2 == 0) {
                int temp = nums[insert];
                nums[insert] = nums[point];
                nums[point] = temp;
                insert++;
                point++;
            } else {
                point++;

            }

        }

        return nums;

    }
}