package com.zorvix.codeforce;

import java.util.Scanner;

public class Cf_731A {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        char[] ch = scan.next().toCharArray(); // z e u s


        int curr = 1;
        int rot = 0;

        for (char c : ch) { // z
            int num = c - 'a' + 1; // 26

            int diff = Math.abs(curr - num);
            int min = Math.min(diff, 26 - diff);


            curr = num;

            rot += min;


        }
        System.out.println(rot);

    }
}
