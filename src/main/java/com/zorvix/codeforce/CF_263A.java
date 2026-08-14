package com.zorvix.codeforce;

import java.util.Scanner;

public class CF_263A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int output = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int val = scan.nextInt();
                if (1 == val) {
                    output = Math.abs(2 - i) + Math.abs(2 - j);
                    break;
                }
            }

        }
        System.out.println(output);

    }
}
