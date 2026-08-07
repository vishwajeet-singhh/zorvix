package com.orvix.codeforce;

import java.util.Scanner;

public class CF_158A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);


        int n = scan.nextInt();
        int k = scan.nextInt();

        int[] arr = new int[n]; // 10 9 8 7 7 7 5 5

        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        int val = arr[k - 1];
        int qualified = 0;


        for (int i = 0; i < n; i++) {
            if (val <= arr[i] && 0 < arr[i]) {
                qualified++;
            }
        }

        System.out.println(qualified);
        scan.close();
    }
}
