class Solution {
    public int countPrimes(int n) {

        if (2 > n)
            return 0;

        boolean[] arr = new boolean[n];
        Arrays.fill(arr, true);

        arr[0] = false;
        arr[1] = false;

        int count = 0;

        for (int i = 2; i * i < n; i++) {

            if (arr[i]) {
                for (int j = i * i; j < n; j += i) {
                    arr[j] = false;
                }
            }

        }

        for (boolean b : arr) {
            if (b) {
                count++;
            }
        }

        return count;

    }
}