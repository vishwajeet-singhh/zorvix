package com.zorvix.codeforce;

import java.util.Scanner;

public class CF_467A {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int t = scan.nextInt();

        /*
             3
            1 10
            0 10
            10 10
         */

        int outout = 0;
        while (t-- > 0) {
            int p = scan.nextInt(); // 1  0  10
            int q = scan.nextInt();//  10 10 10
            if (Math.abs(p - q) >= 2) outout++;//1,2,0
        }

        System.out.println(outout);


    }
}
