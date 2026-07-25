package com.orvix.math;


public class M02_LCM {

    public static void main(String[] args) {
        int output = lcm(10, 20);
        System.out.println(output);
    }

    private static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}
