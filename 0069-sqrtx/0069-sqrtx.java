class Solution {
    public int mySqrt(int x) {

        // 1 2 3 -> 4 -> 5 6 7 8 

        int low = 0, result = x, high = x;

        while (low <= high) {

            int mid = low + (high - low) / 2;
            long sq = (long) mid * mid;

            if (sq == x)
                return mid;

            if (sq < x) {
                result = mid;
                low = mid + 1;
            } else
                high = mid - 1;

        }

        return result;
    }
}