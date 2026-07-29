class Solution {
    public int totalFruit(int[] fruits) {

        int left = 0, ans = 0;
        Map<Integer, Integer> map = new HashMap();

        for (int right = 0; right < fruits.length; right++) {

            map.put(fruits[right], map.getOrDefault(fruits[right], 0) + 1);

            while (2 < map.size()) {

                map.put(fruits[left], map.get(fruits[left]) - 1);

                if (0 == map.get(fruits[left]))
                    map.remove(fruits[left]);
                    left++;

            }

            ans = Math.max(ans,right-left+1);

        }

        return ans;

    }
}