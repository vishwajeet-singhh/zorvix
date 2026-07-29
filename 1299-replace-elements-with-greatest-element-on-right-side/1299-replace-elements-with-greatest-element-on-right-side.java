class Solution {
    public int[] replaceElements(int[] arr) {
        int len = arr.length;
        int[] ouput_array = new int[len];

        int greatest = arr[len - 1];
        ouput_array[len - 1] = -1;

        for (int i = len - 2; i >= 0; i--) {

            ouput_array[i] = greatest;

            greatest = Math.max(greatest, arr[i]);
        }

        return ouput_array;

    }
}