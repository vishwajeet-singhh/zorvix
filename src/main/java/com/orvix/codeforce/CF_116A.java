package com.orvix.codeforce;

import java.util.Scanner;

public class CF_116A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int t = scan.nextInt();
        int max_capacity = 0;
        int current_passensar = 0;
        while (t-- > 0) {
            int out = scan.nextInt(); // 0, 2, 4, 4
            int in = scan.nextInt(); // 3 , 5, 2, 0

            current_passensar = current_passensar - out + in; // 0-0+3 = 3, 3-2+5 = 6, 6-4+2 = 4, 4-4+0 = 0

            max_capacity = Math.max(max_capacity, current_passensar); // 3, 6, 6, 6


        }
        System.out.println(max_capacity);

    }
}
