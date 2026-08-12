package com.orvix.codeforce;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CF_546C {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();

        Deque<Integer> first = new ArrayDeque<>();
        Deque<Integer> second = new ArrayDeque<>();

        int k = scan.nextInt();

        for (int i = 0; i < k; i++) {
            first.addLast(scan.nextInt());
        }
        int j = scan.nextInt();
        for (int i = 0; i < j; i++) {
            second.addLast(scan.nextInt());
        }

        int step = 0;
        int winner = 0;

        Set<String> set = new HashSet<>();


        while (true) {

            if (first.isEmpty()) {
                winner = 2;
                break;
            }
            if (second.isEmpty()) {
                winner = 1;
                break;
            }

            String str = first + "/" + second;

            if (set.contains(str)) {
                System.out.println(-1);
                return;
            }


            int firstTop = first.removeFirst();
            int secondTop = second.removeFirst();


            if (firstTop > secondTop) {
                first.addLast(secondTop);
                first.addLast(firstTop);
            } else {
                second.addLast(firstTop);
                second.addLast(secondTop);
            }
            set.add(str);
            step++;
        }


        System.out.println(step + " " + winner);


    }


}
