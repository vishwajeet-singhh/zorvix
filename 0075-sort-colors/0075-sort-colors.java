class Solution {
    public void sortColors(int[] nums) {

        int low = 0;
        int mid = 0;
        int high = nums.length - 1;

        while (mid <= high) {

            if (0 == nums[mid]) {
                swap(low, mid, nums);
                low++;
                mid++;
            } else if (1 == nums[mid])
                mid++;
            else if (2 == nums[mid]) {
                swap(mid, high, nums);
                high--;

            }

        }

    }

    private void swap(int index1, int index2, int[] arr) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;

    }
}