package com.orvix.cses;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Weird_Algo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);


        int n = scan.nextInt();
        scan.close();


        while (n != 1) {
            System.out.print(n + " ");
            if (n % 2 == 0) n = n / 2;
            else n = n * 3 + 1;
        }
        System.out.print(1);


    }
}
