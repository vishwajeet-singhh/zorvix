class Solution {
    public String decodeString(String s) {

        Stack<Character> stack = new Stack<>();

        for (char ch : s.toCharArray()) {

            if (ch != ']') {
                stack.push(ch);
            } else {

                StringBuilder str = new StringBuilder();

                while (stack.peek() != '[') {
                    str.insert(0, stack.pop());
                }

                stack.pop();

                StringBuilder num = new StringBuilder();

                while (!stack.isEmpty() && Character.isDigit(stack.peek())) {
                    num.insert(0, stack.pop());
                }

                int repeat = Integer.parseInt(num.toString());

                String repeated = str.toString().repeat(repeat);

                for (char c : repeated.toCharArray()) {
                    stack.push(c);
                }
            }
        }

        StringBuilder ans = new StringBuilder();

        while (!stack.isEmpty()) {
            ans.insert(0, stack.pop());
        }

        return ans.toString();
    }
}