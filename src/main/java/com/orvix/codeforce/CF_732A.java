package com.orvix.codeforce;

import java.util.Scanner;

public class CF_732A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();
        int b = scan.nextInt();


        for (int i = 1; i < 10; ++i) {

            int mod = (n * i) % 10;

            if (0 == mod) {
                System.out.println(i);
                break;
            }

            if (b ==mod) {
                System.out.println(i);
                break;
            }


        }

    }
}
