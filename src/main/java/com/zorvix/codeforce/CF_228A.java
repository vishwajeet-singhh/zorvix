package com.zorvix.codeforce;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CF_228A {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);


        Set<Integer> set = new HashSet<>();

        int n = 4;
        while (n-- > 0) {
            set.add(scan.nextInt());
        }

        System.out.println(4 - set.size());

    }

}
