class Solution {
    public int[] getConcatenation(int[] nums) {

        int n = nums.length;

        int[] arr = Arrays.copyOf(nums, 2 * n);

        for (int i = n ; i < 2 * n; i++) {
            arr[i] = arr[i-n];
        }

        return arr;

    }
}