package com.orvix.recursion;

public class R01_Rope_Cutting {

    public static void main(String[] args) {

        System.out.println(maxCut(37, 11, 2, 13));

    }

    static int maxCut(int n, int a, int b, int c) {

        if (n == 0) return 0;
        if (n < 0) return -1;

        int na = maxCut(n - a, a, b, c);
        int nb = maxCut(n - b, a, b, c);
        int nc = maxCut(n - c, a, b, c);

        int res = Math.max(na, Math.max(nb, nc));

        if (res == -1) return -1;

        return res + 1;

    }
}
