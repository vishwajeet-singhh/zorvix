class Solution {
    public int evalRPN(String[] tokens) {

        Stack<Integer> stack = new Stack();

        for (String str : tokens) {

            if (str.equals("+")) {

                int second = stack.pop();
                int first = stack.pop();

                int cal = first + second;
                stack.push(cal);

            } else if (str.equals("-")) {
                int second = stack.pop();
                int first = stack.pop();

                int cal = first - second;
                stack.push(cal);

            } else if (str.equals("*")) {
                int second = stack.pop();
                int first = stack.pop();

                int cal = first * second;
                stack.push(cal);

            } else if (str.equals("/")) {
                int second = stack.pop();
                int first = stack.pop();

                int cal = first / second;
                stack.push(cal);

            } else {
                int val = Integer.parseInt(str);
                stack.push(val);
            }

        }

        return stack.peek();

    }
}