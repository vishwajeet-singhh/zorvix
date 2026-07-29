class Solution {
    public void moveZeroes(int[] nums) {

        int zeroHold = 0;

        for (int i = 0; i < nums.length; i++) {
            if (0 != nums[i]) {
                int temp = nums[i];
                nums[i] = nums[zeroHold];
                nums[zeroHold] = temp;
                zeroHold++;

            }

        }

    }
}