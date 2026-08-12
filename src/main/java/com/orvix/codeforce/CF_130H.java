package com.orvix.codeforce;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class CF_130H {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String str = scan.next();
        scan.close();


        char[] ch = str.toCharArray();
        boolean out = true;

        Deque<Character> deque = new ArrayDeque<>();
        for (char c : ch) {
            if (c == '(') {
                deque.addLast(c);
            } else {
                if (!deque.isEmpty()) {
                    out = deque.removeLast() == '(';
                } else {
                    out = false;
                }
            }
        }
        System.out.println(out);
    }
}
