class Solution {
    public int calPoints(String[] operations) {

        Stack<Integer> stack = new Stack();

        for (String str : operations) {
            // if (stack.isEmpty())
            //     stack.push(str);

            if (str.equals("C"))
                stack.pop();
            else if (str.equals("D")) {
                int val = stack.peek();
                int double_score = val * 2;
                stack.push(double_score);
            } else if (str.equals("+")) {
                int top = stack.pop();
                int second_top = stack.peek();
                stack.push(top);
                stack.push(top + second_top);
            }else{
                int val = Integer.parseInt(str);
                stack.push(val);
            }

        }


        int sum = 0;

        while(!stack.isEmpty()){
            sum += stack.pop();
        }

        return sum;
            
        

    }
}