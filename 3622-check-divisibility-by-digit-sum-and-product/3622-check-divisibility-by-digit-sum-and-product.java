class Solution {
    public boolean checkDivisibility(int n) {
        long sum = 0;
        long pro = 1;
        long sto = n;

        while (n > 0) {
            int mod = n % 10;
            sum = (long) sum + mod;
            pro = (long) pro * mod;
            n = n / 10;
        }

        long total = sum+pro;

        return sto%total == 0;

    }
}