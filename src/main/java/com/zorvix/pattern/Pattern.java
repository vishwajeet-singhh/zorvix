package com.zorvix.pattern;

public class Pattern {
    public static void main(String[] args) {
        pattern22(2);

    }


    private static void pattern7(int n) {

        int max_len = 2 * (n - 1) + 1;

        for (int i = 0; i < n; i++) {
            int no_of_star = 2 * i + 1;
            int space_print = (max_len - no_of_star) / 2;

            // print space
            for (int j = 0; j < space_print; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= no_of_star; j++) {
                System.out.print("*");
            }
            System.out.println();

        }
    }

    private static void pattern8(int n) {
        int max_len = 2 * (n - 1) + 1;

        for (int i = n - 1; i >= 0; i--) {
            int no_of_star = 2 * i + 1;
            int space_print = (max_len - no_of_star) / 2;

            // print space
            for (int j = 0; j < space_print; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= no_of_star; j++) {
                System.out.print("*");
            }
            System.out.println();

        }
    }

    private static void pattern9(int n) {

        // upper part
        int max_len = 2 * (n - 1) + 1;

        for (int i = 0; i < n; i++) {
            int no_of_star = 2 * i + 1;
            int space_print = (max_len - no_of_star) / 2;

            // print space
            for (int j = 0; j < space_print; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= no_of_star; j++) {
                System.out.print("*");
            }
            System.out.println();

        }


        // lower part
        for (int i = n - 1; i >= 0; i--) {
            int no_of_star = 2 * i + 1;
            int space_print = (max_len - no_of_star) / 2;

            // print space
            for (int j = 0; j < space_print; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= no_of_star; j++) {
                System.out.print("*");
            }
            System.out.println();

        }

    }

    private static void pattern10(int n) {

        for (int i = 1; i <= n; ++i) {

            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println();

        }
        for (int i = n; i >= 1; --i) {
            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println();


        }

    }

    private static void pattern11(int n) {

        int i, j;
        for (i = 1; i <= n; i++) {

            if (i % 2 == 0) {
                for (j = 0; j < i; ++j) {
                    if (j % 2 == 0)
                        System.out.print(0);
                    else
                        System.out.print(1);

                }
            } else {
                for (j = 0; j < i; ++j) {
                    if (j % 2 == 1)
                        System.out.print(0);
                    else
                        System.out.print(1);

                }

            }

            System.out.println();
        }

    }

    private static void pattern12(int n) {

        int max = n * 2;


        for (int i = 1; i <= n; i++) {
            // left

            int space = max - 2 * i;

            for (int j = 1; j <= i; j++) {
                System.out.print(j);
            }

            // mid

            for (int j = 1; j <= space; j++) {
                System.out.print(" ");
            }

            // right
            for (int j = i; j >= 1; j--) {
                System.out.print(j);
            }


            System.out.println();
        }


    }

    private static void pattern13(int n) {

        int curr = 1;
        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= i; j++) {
                System.out.print(curr++ + " ");
            }


            System.out.println();
        }

    }

    private static void pattern14(int n) {

        for (int i = 1; i <= n; i++) {
            char ch = 'A';

            for (int j = 1; j <= i; j++) {

                System.out.print(ch++ + " ");
            }


            System.out.println();
        }


    }

    private static void pattern15(int n) {
        for (int i = n; i >= 1; i--) {
            char ch = 'A';

            for (int j = 1; j <= i; j++) {

                System.out.print(ch++ + " ");
            }


            System.out.println();
        }

    }

    private static void pattern16(int n) {
        char ch = 'A';
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {

                System.out.print(ch + " ");
            }

            ch++;
            System.out.println();
        }

    }

    private static void pattern17(int n) {

        for (int i = 1; i <= n; i++) {

            // spaces
            for (int j = 1; j <= n - i; j++) {
                System.out.print(" ");
            }

            // A -> current character
            for (int j = 1; j <= i; j++) {
                System.out.print((char) ('A' + j - 1));
            }

            // current character - 1 -> A
            for (int j = i - 1; j >= 1; j--) {
                System.out.print((char) ('A' + j - 1));
            }

            System.out.println();
        }
    }

    private static void pattern18(int n) {

        char first = (char) ('A' + n);
        for (int i = 1; i <= n; i++) {


            char start = (char) (first - i);
            for (int j = 1; j <= i; j++) {
                System.out.print(start++ + " ");

            }
            System.out.println();

        }

    }

    private static void pattern19(int n) {


        for (int i = 0; i < n; i++) {

            for (int j = n - i; j >= 1; j--) {
                System.out.print("*");
            }
            for (int j = 0; j < i * 2; j++) {
                System.out.print(" ");
            }
            for (int j = 1; j <= n - i; j++) {
                System.out.print("*");
            }

            System.out.println();

        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 1; j <= n - i; j++) {
                System.out.print("*");
            }

            for (int j = 0; j < i * 2; j++) {
                System.out.print(" ");
            }
            for (int j = n - i; j >= 1; j--) {
                System.out.print("*");
            }
            System.out.println();

        }


    }

    private static void pattern20(int n) {

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            for (int j = 1; j <= 2 * (n - i); j++) {
                System.out.print(" ");
            }

            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println();
        }


        for (int i = n - 1; i >= 1; i--) {
            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            for (int j = 1; j <= 2 * (n - i); j++) {
                System.out.print(" ");
            }

            for (int j = 1; j <= i; j++) {
                System.out.print("*");
            }
            System.out.println();

        }


    }


    private static void pattern21(int n) {

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {

                if (i == 1 || i == n || j == 1 || j == n) {
                    System.out.print("*");
                } else if (j != 1 || j != n) {
                    System.out.print(" ");
                }

            }
            System.out.println();
        }

    }

    /*
5 5 5 5 5 5 5 5 5
5 4 4 4 4 4 4 4 5
5 4 3 3 3 3 3 4 5
5 4 3 2 2 2 3 4 5
5 4 3 2 1 2 3 4 5
5 4 3 2 2 2 3 4 5
5 4 3 3 3 3 3 4 5
5 4 4 4 4 4 4 4 5
5 5 5 5 5 5 5 5 5
     */
    private static void pattern22(int n) {

        int max = 2 * n - 1;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                int min = Math.min(
                        Math.min(i, j),
                        Math.min(max - 1 - i, max - 1 - j)
                );
                System.out.print((n - min) + " ");

            }

            System.out.println();


        }

    }


}
