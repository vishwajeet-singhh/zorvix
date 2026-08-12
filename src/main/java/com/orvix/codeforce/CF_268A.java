package com.orvix.codeforce;

import java.util.Scanner;

public class CF_268A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        int[] home = new int[n];
        int[] guest = new int[n];

        /*
                    3
                    1 2
                    2 4
                    3 4
        */

        for (int i = 0; i < n; ++i) {
            home[i] = scan.nextInt();
            guest[i] = scan.nextInt();
        }

        int affectGames = 0;


        for (int i = 0; i < n; ++i) {

            for (int j = 0; j < n; ++j) {

                if (i == j) continue;

                if (home[i] == guest[j]) affectGames++;
            }

        }

        System.out.println(affectGames);


    }
}
