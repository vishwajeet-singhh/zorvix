import java.util.Arrays;

class MinStack {

    private static final int DEFAULT = 8;

    private int[] arr;
    private int[] minArr;
    private int length;

    public MinStack() {
        arr = new int[DEFAULT];
        minArr = new int[DEFAULT];
        length = 0;
    }

    public void push(int val) {

        if (length == arr.length) {
            grow();
        }

        arr[length] = val;

        if (length == 0) {
            minArr[length] = val;
        } else {
            minArr[length] = Math.min(val, minArr[length - 1]);
        }

        length++;
    }

    public void pop() {
        length--;
    }

    public int top() {
        return arr[length - 1];
    }

    public int getMin() {
        return minArr[length - 1];
    }

    private void grow() {
        arr = Arrays.copyOf(arr, arr.length * 2);
        minArr = Arrays.copyOf(minArr, minArr.length * 2);
    }
}
/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(value);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(value);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */