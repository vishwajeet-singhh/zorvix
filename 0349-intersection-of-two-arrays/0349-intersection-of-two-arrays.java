class Solution {
    public int[] intersection(int[] nums1, int[] nums2) {

        Set<Integer> set = new HashSet();

        List<Integer> list = new ArrayList();

        for (int i : nums1)
            set.add(i);

        for (int i : nums2) {

            if (set.contains(i)) {
                list.add(i);
                set.remove(i);
            }

        }

        int[] res = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }
}