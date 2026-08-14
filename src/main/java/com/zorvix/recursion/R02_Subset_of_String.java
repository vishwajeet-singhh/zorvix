package com.zorvix.recursion;

public class R02_Subset_of_String {

    public static void main(String[] args) {

        String people = "ABCDE";
        int minGroupSize = 3;

        subset(people, "", 0, minGroupSize);
    }

    static void subset(String str, String curr, int index, int minSize) {

        if (index == str.length()) {
            if (curr.length() == minSize) {
                System.out.println(curr);
            }
            return;
        }

        // Include current person


        // Exclude current person
        subset(str, curr, index + 1, minSize);
        subset(str, curr + str.charAt(index), index + 1, minSize);
    }
}