class Solution {
    public int[] dailyTemperatures(int[] temperatures) {

        int n = temperatures.length;

        int[] arr = new int[n];

        // 73,74,75,71,69,72,76,73

        Stack<int[]> stack = new Stack();

        for (int i = 0; i < n; i++) {

            int t = temperatures[i];

            while (!stack.isEmpty() && stack.peek()[0] < t) {

                int[] val = stack.pop();
                arr[val[1]] = i - val[1];

            }
            stack.push(new int[] { t, i });

        }

        return arr;

    }
}