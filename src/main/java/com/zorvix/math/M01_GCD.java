package com.zorvix.math;

public class M01_GCD {

    public static void main(String[] args) {

        int output = gcd(25, 15);
        System.out.println(output);


    }


    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);


    }
}
