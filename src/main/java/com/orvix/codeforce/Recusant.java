package com.orvix.codeforce;


import java.util.*;

public class Recusant {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();
            int q = sc.nextInt();

            String s = sc.next();
            String tt = sc.next();

            int[] preS0 = new int[n + 1];
            int[] preT0 = new int[n + 1];
            int[] preA = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                char cs = s.charAt(i - 1);
                char ct = tt.charAt(i - 1);

                preS0[i] = preS0[i - 1] + (cs == '0' ? 1 : 0);
                preT0[i] = preT0[i - 1] + (ct == '0' ? 1 : 0);
                preA[i] = preA[i - 1] + ((cs == '0' && ct == '0') ? 1 : 0);
            }

            StringBuilder out = new StringBuilder();

            while (q-- > 0) {
                int l = sc.nextInt();
                int r = sc.nextInt();

                int S0 = preS0[r] - preS0[l - 1];
                int T0 = preT0[r] - preT0[l - 1];
                int A = preA[r] - preA[l - 1];

                int m = r - l + 1;
                int S1 = m - S0;
                int T1 = m - T0;

                int Tx = Math.max(0, S0 - S1);
                int Ty = Math.max(0, T0 - T1);

                if (Tx + Ty <= 2 * A)
                    out.append("YES\n");
                else
                    out.append("NO\n");
            }

            System.out.print(out);
        }

        sc.close();
    }
}