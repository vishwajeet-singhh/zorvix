class Solution {
    public int search(int[] nums, int target) {
        return binary(0, nums.length - 1, target, nums);

    }

    int binary(int low, int high, int tar, int[] arr) {

        int mid = low + (high - low) / 2;

        if (low > high)
            return -1;

        if (arr[mid] == tar)
            return mid;

        if (arr[mid] < tar) {
            return binary(mid + 1, high, tar, arr);
        } else {
            return binary(low, mid - 1, tar, arr);
        }

    }
}