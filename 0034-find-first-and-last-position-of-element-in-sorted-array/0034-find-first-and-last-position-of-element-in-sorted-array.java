class Solution {
    public int[] searchRange(int[] nums, int target) {

        int left = 0, right = 0, low = 0, high = nums.length - 1;

        int firstPosition = binarySearch(low, high, nums, target);

        if (-1 == firstPosition)
            return new int[] { -1, -1 };

        left = searchLeftSide(low, firstPosition - 1, nums, target);
        right = searchRightSide(firstPosition + 1, high, nums, target);

        if (-1 == left)
            left = firstPosition;
        if (-1 == right)
            right = firstPosition;

        return new int[] { left, right };

    }

    private int binarySearch(int low, int high, int[] arr, int target) {
        int mid = low + (high - low) / 2;

        if (low > high)
            return -1;

        if (target == arr[mid])
            return mid;

        if (target > arr[mid])
            return binarySearch(mid + 1, high, arr, target);

        return binarySearch(low, mid - 1, arr, target);
    }

    private int searchLeftSide(int left, int right, int[] arr, int target) {

        int indexOfLatestElement = binarySearch(left, right, arr, target);

        if (-1 == indexOfLatestElement)
            return -1;

        int earlistElement = searchLeftSide(left, indexOfLatestElement - 1, arr, target);

        return -1 == earlistElement ? indexOfLatestElement : earlistElement;

    }

    private int searchRightSide(int left, int right, int[] arr, int target) {

        int indexOfLatestElement = binarySearch(left, right, arr, target);

        if (-1 == indexOfLatestElement)
            return -1;

        int earlistElement = searchRightSide(indexOfLatestElement + 1, right, arr, target);

        return -1 == earlistElement ? indexOfLatestElement : earlistElement;

    }

}