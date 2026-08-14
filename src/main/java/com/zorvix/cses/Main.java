package com.zorvix.cses;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        int[] nums = {0, 3, 7, 2, 5 , 8, 4, 6, 0, 1};

        HashSet<Integer> set = new HashSet();

        for (int i : nums) set.add(i); // 100 4 200 1 3 2

        int max = 0; // 1 ,4

        for (int i : nums) {
            if (!set.contains(i - 1)) {
                int curr = 1; // 2,3,4
                while (set.contains(i + curr)) {
                    curr++;
                }
                max = Math.max(max, curr);
            }
        }

        System.out.println(max);


    }
}
