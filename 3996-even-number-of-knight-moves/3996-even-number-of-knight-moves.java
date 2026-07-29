class Solution {
    public boolean canReach(int[] start, int[] target) {

        int val = (start[0]+start[1])%2;
        int vall = (target[0]+target[1])%2;


        return val==vall;
        
    }
}