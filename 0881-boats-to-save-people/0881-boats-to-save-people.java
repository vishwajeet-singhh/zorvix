class Solution {
    public int numRescueBoats(int[] people, int limit) {
        int left = 0, right = people.length - 1, result = 0;
        Arrays.sort(people);

        while (left <= right) {

            if (people[left] + people[right] <= limit)
                left++;

            right--;
            result++;

        }

        return result;

        /*
        //[3,2,2,1] limit = 3
        int left = 0, right = people.length - 1, result = 0;
        
        Arrays.sort(people);
        
        //[1,2,2,3] limit = 3
        // left = 0, right = 3
        
        while (left <= right) {
        
            // I- 1:
            // left = 0 (1), right = 3 (3)
            // 1 + 3 = 4 > 3 -> can't pair
        
            // Iteration 2:
            // left = 0 (1), right = 2 (2)
            // 1 + 2 = 3 <= 3 -> pair them
            // left = 1
        
            // Iteration 3:
            // left = 1 (2), right = 1 (2)
            // 2 + 2 = 4 > 3 -> can't pair
        
            if (people[left] + people[right] <= limit) {
                left++;   // l = 1 after iteration 2
            }
        
            right--;      // r = 2, 1, 0
            result++;     // boats = 1, 2, 3
        }
        
        return result;
        
        */
    }
}