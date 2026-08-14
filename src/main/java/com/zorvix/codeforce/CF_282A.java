package com.zorvix.codeforce;

import java.util.Scanner;

public class CF_282A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();
        int pos = 0;
        int neg = 0;
        while (n-- > 0) {

            String ops = scan.next();

            if (ops.contains("++")) pos += 1;
            else neg += 1;

        }
        scan.close();

        System.out.println(pos - neg);

    }

}
