class Solution {
    public int countGoodSubstrings(String s) {

        List<Set<Character>> list = new ArrayList();

        int count = 0;

        int left = 0;
        int right = 2;
        int n = s.length();

        while (right < n) {
            Set<Character> set = new HashSet();
            set.add(s.charAt(left));
            set.add(s.charAt(left + 1));
            set.add(s.charAt(left + 2));

            if (3 == set.size())
                count++;
            left++;
            right++;

        }
        return count;
    }
}