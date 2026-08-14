package com.zorvix.codeforce;

import java.util.*;

public class Recusant {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();

            int[] a = new int[n];
            long total = 0;

            Map<Integer, Integer> freq = new HashMap<>();

            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
                total += a[i];
                freq.put(a[i], freq.getOrDefault(a[i], 0) + 1);
            }

            int mxFreq = 0;
            int mxValue = 0;

            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                if (entry.getValue() > mxFreq) {
                    mxFreq = entry.getValue();
                    mxValue = entry.getKey();
                }
            }

            int others = n - mxFreq;

            if (mxFreq <= others + 1) {
                System.out.println(total);
            } else {
                long otherSum = total - (long) mxFreq * mxValue;
                long ans = otherSum + (long) (others + 2) * mxValue;
                System.out.println(ans);
            }
        }

        sc.close();
    }
}