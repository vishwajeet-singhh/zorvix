class Solution {
    public int[] findErrorNums(int[] nums) {

        int n = nums.length;

        int[] arr = new int[n+1];

        int[] out = new int[2];

        for (int i : nums) {
            arr[i]++;
        }

        for (int i = 1; i <= n; i++) {

            int cnt = arr[i];

            if (cnt == 2) {
                out[0] = i;

            }
            if (cnt == 0) {
                out[1] = i;

            }
        }

        return out;

    }
}