class Solution {
    public int findGCD(int[] nums) {

        int small = Integer.MAX_VALUE, large = Integer.MIN_VALUE;

        for (int num : nums) {

            if (num < small)
                small = num;
            if (num > large)
                large = num;

        }
        return gcd(small, large);

    }

    private int gcd(int small, int large) {
        if (0 == large)
            return small;
        return gcd(large, small % large);
    }
}