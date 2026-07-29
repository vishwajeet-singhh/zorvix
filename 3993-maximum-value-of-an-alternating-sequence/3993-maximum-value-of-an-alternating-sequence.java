class Solution {
    public long maximumValue(int n, int s, int m) {

        // n is length
        // seq[0] = s
        
        long val = s;

        if (1 == n)
            return s;

        long max = n / 2l;

        return (long) s + max * m - (max - 1);

    }
}