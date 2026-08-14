package com.zorvix.math;

public class M03_Prime_Number {

    public static void main(String[] args) {

        System.out.println(isPrime(121));

    }


    /**
     * Time complexity will be O(sqrt(n))
     * <br>
     * Space Complexity will be O(1)
     */
    private static boolean isPrime(int n) {


        if (n == 1) return false;

        if (n % 2 == 0) return false;
        if (n % 3 == 0) return false;


        for (int i = 5; i * i < n; i += 6) { // {5,7},{11,13},{17,19},{23,25},{29,31},{35,37}
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;

    }
}
