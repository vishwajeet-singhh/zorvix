class Solution {
    public int[] resultArray(int[] nums) {
        int n = nums.length;
        int[] arr1 = new int[n];
        int[] arr2 = new int[n];

        int p1 = 0;
        int p2 = 0;

        arr1[0] = nums[0];
        arr2[0] = nums[1];

        for (int i = 2; i < n; i++) {
            if (arr1[p1] > arr2[p2])
                arr1[++p1] = nums[i];
            else
                arr2[++p2] = nums[i];
        }

        System.out.println(p1);
        System.out.println(Arrays.toString(arr1));
        System.out.println(p2);
        System.out.println(Arrays.toString(arr2));

        int[] result = new int[p1 + p2 + 2];

        for (int i = 0; i <= p1; i++) {
            result[i] = arr1[i];
              System.out.println(Arrays.toString(result));
        }

        for (int i = 0; i <=p2; i++) {

            if (arr2[i] > 0)
                result[i+p1+1] = arr2[i];
              System.out.println(Arrays.toString(result));
        }

        return result;

    }
}