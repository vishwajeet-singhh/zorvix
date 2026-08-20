class Solution {
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> list = new ArrayList();

        for (int i : nums) {
            int index = Math.abs(i) - 1;

            if (0 > nums[index])
                list.add(Math.abs(i));
            nums[index] = -nums[index];

        }
        return list;

    }
}