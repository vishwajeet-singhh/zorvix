class Solution {
    public int maximumLengthSubstring(String s) {
        HashMap<Character, Integer> mp = new HashMap<>();
        int l = 0, res = 0;

        for (int r = 0; r < s.length(); r++) {

            char ch = s.charAt(r);

            mp.put(ch, mp.getOrDefault(ch, 0) + 1);
            while (mp.get(ch) > 2) {
                char leftChar = s.charAt(l);
                mp.put(leftChar, mp.get(leftChar) - 1);
                l++;
            }

            res = Math.max(res, r - l + 1);
        }
        return res;
    }
}