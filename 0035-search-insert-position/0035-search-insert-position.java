class Solution {
    public int searchInsert(int[] nums, int target) {
        int result = nums.length, low = 0, high = result - 1;
        // 1,3,5,6 target =5
        // result = 3

        /*

        It 1 -> 
        l-0 (1)
        h-3 (6)
        m-1(3)


        It2 ->
        l - 1(3), h - 3(6)
        m - 2(5)


        It3 ->
        l - 2(5), h - 3(6)
        m - 2(5)


        it3 -> 
        l - 3(6), h-3(6)
        m = 3 (6)

        it4 l-3(6), h-2. --- terminate


        
        
        */
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (target == nums[mid])
                return mid;

            if (target < nums[mid]) {
                result = mid;
                high = mid - 1;
            } else
                low = mid + 1;
        }

        return result;

    }
}