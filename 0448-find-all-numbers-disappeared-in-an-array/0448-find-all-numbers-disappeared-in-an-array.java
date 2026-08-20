class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> list = new ArrayList();

        for (int i : nums) {
            int index = Math.abs(i) - 1;

            if (nums[index] > 0) {
                nums[index] = -nums[index];
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (0 < nums[i])
                list.add(i + 1);
        }

        return list;

    }
}