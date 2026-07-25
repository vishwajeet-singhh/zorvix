package com.orvix.search;

public class S01_Binary_Search {
    public static void main(String[] args) {
        int[] arr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91, 105, 120, 145, 167, 190};


        int[] target = {56, 120, 12, 145, 202};

        // 56 -> 7
        // 120 -> 11
        // 12 -> 3
        // 145 -> 12
        // 202 -> -1


        for (int i : target) {
            int index = binarySearchInInfineteElemenet(arr, i);
            System.out.println("Element -> " + i + " Index -> " + index);
        }


    }

    static int binarySearchInInfineteElemenet(int[] arr, int target) {

        int low = 0;
        int high = 1;


        while (arr[high] < target) {

            if (high == arr.length - 1) {
                return -1;
            }

            low = high + 1;
            high *= 2;

            if (high >= arr.length) {
                high = arr.length - 1;
            }
        }

        return binarySearch(target, arr, low, high);


    }

    static int binarySearch(int target, int[] arr, int low, int high) {



        if (low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;

        if (arr[mid] == target) return mid;

        if (arr[mid] < target) {
            return binarySearch(target, arr, mid + 1, high);
        } else {
            return binarySearch(target, arr, low, mid - 1);
        }
    }
}
