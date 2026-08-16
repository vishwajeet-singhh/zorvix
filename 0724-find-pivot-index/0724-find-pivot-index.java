class Solution {
    public int pivotIndex(int[] nums) {

        // 1 7 3 6 5 6
        // 1  8  11 17 22 28
        // 28 27 20 17 11 6 

        int n = nums.length;

        int[] pre = new int[n];
        int[] suf = new int[n];

        pre[0] = nums[0];
        for (int i = 1; i < n; i++) {
            pre[i] = nums[i] + pre[i - 1];
        }

        suf[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suf[i] = nums[i] + suf[i + 1];
        }

        for (int i = 0; i < n; i++) {
            if (pre[i] == suf[i])
                return i;
        }

        return -1;
    }
}