package com.orvix.codeforce;

import java.util.Scanner;

public class CF_427A {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        int output = 0;
        int police = 0;

        while (n-- > 0) {

            int event = scan.nextInt();

            if (0 > event) {


                if (0 == police) {
                    output++;
                } else {
                    police--;
                }
            } else {
                police += event;
            }


        }

        System.out.println(output);
    }
}
