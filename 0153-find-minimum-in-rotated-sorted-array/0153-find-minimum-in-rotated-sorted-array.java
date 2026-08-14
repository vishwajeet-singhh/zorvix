class Solution {
    public int findMin(int[] nums) {

        // 3,4,5,1,2

        /*
        l = 0(3) , 3(1)
        r = 4(2) , 4(2)
        m = 2(5)
        
        3, 4, 5, 1, 2
        0. 1  2. 3  4



        l = 0(4)  4(0)
        r = 6(2)  6(2) 5(1)
        m = 3(7)  5(1)

        4, 5, 6, 7, 0, 1, 2
        0  1  2. 3. 4. 5. 6

        [11,13,15,17]

               */

            int left  =0, right = nums.length-1;

            while(left<right){
                int mid = left +(right-left)/2;
                if(nums[mid]>nums[right]) left = mid+1;
                else right = mid;
            }

            return nums[left];



    }
}