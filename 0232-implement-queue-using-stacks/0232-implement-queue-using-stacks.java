class MyQueue {

    Stack<Integer> add_stack;
    Stack<Integer> remove_stack;

    public MyQueue() {
        add_stack = new Stack();
        remove_stack = new Stack();
    }

    public void push(int x) {
        add_stack.push(x);

    }

    public int pop() {
        while (add_stack.size() > 1) {
            remove_stack.push(add_stack.pop());
        }

        int last_element = add_stack.pop();

        while (remove_stack.size() > 0) {
            add_stack.push(remove_stack.pop());
        }
        return last_element;

    }

    public int peek() {
        while (add_stack.size() > 1) {
            remove_stack.push(add_stack.pop());
        }

        int last_element = add_stack.peek();

        while (remove_stack.size() > 0) {
            add_stack.push(remove_stack.pop());
        }

        return last_element;

    }

    public boolean empty() {

        return add_stack.isEmpty();

    }
}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */