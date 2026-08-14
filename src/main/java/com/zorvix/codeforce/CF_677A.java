package com.zorvix.codeforce;

import java.util.Scanner;

public class CF_677A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);


        int t = scan.nextInt(); //3
        int h = scan.nextInt();//7

        int output = 0; // 1, 2, 4
        while (t-- > 0) {
            int p = scan.nextInt();// 4, 5, 7
            if (p > h) output += 2;
            else output++;
        }

        System.out.println(output);


    }
}
