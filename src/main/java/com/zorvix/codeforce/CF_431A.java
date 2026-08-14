package com.zorvix.codeforce;

import java.util.Scanner;

public class CF_431A {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int[] arr = new int[5];

        for (int i = 1; i < 5; i++) {
            arr[i] = scan.nextInt();
        }


        char[] ch = scan.next().toCharArray();
        int energy = 0;

        for (char c : ch) {
            int val = c - '0';
            energy += arr[val];
        }

        System.out.println(energy);
    }
}
