package com.orvix.math;

import java.util.Scanner;

public class M04_SW_GCD {

    /**
     * Find the GCD and LCM of the subarray whose sum equals the target.
     * If no such subarray exists, return (0, 0).
     */
    public static void main(String[] args) {

        int[] numbers = {
                16, 21,  40, 32, 14, 28,
                35, 49, 56, 24, 36, 45, 27, 54, 12, 60,
                15, 75, 20, 50, 10, 70, 84, 48, 33, 66,
                22, 44, 55, 77, 88, 26, 39, 52, 65, 78,
                91, 13, 17, 19, 23, 29, 31, 37, 41, 47
        };

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter target sum: ");
        int targetSum = scanner.nextInt();
        scanner.close();

        Result result = findSubarrayGcdAndLcm(numbers, targetSum);

        System.out.println("LCM -> " + result.lcm);
        System.out.println("GCD -> " + result.gcd);

    }

    static Result findSubarrayGcdAndLcm(int[] numbers, int targetSum) {

        int start = 0;
        int currentSum = 0;

        for (int end = 0; end < numbers.length; end++) {

            currentSum += numbers[end];

            while (currentSum > targetSum) {
                currentSum -= numbers[start];
                start++;
            }

            if (currentSum == targetSum) {

                System.out.println("Found from index " + start + " to " + end);

                for (int i = start; i <= end; i++) {
                    System.out.print(numbers[i] + " ");
                }
                System.out.println();

                return computeGcdAndLcm(numbers, start, end);
            }
        }

        return new Result(0, 0);
    }

    static Result computeGcdAndLcm(int[] numbers, int start, int end) {

        int currentGcd = numbers[start];
        int currentLcm = numbers[start];

        for (int i = start + 1; i <= end; i++) {
            currentGcd = gcd(currentGcd, numbers[i]);
            currentLcm = lcm(currentLcm, numbers[i]);
        }

        return new Result(currentLcm, currentGcd);
    }

    static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    static int lcm(int a, int b) {
        return (a / gcd(a, b)) * b;
    }

    static class Result {
        int lcm;
        int gcd;

        Result(int lcm, int gcd) {
            this.lcm = lcm;
            this.gcd = gcd;
        }
    }
}