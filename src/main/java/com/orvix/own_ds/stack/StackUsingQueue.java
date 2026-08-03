package com.orvix.own_ds.stack;

import java.util.LinkedList;
import java.util.Queue;

class StackUsingQueue {

    Queue<Integer> push_queue;
    Queue<Integer> pop_queue;

    public StackUsingQueue() {
        push_queue = new LinkedList<>();
        pop_queue = new LinkedList<>();


    }

    public void push(int x) {
        push_queue.add(x);
    }

    public int pop() {


        while (push_queue.size() > 0) {
            pop_queue.add(push_queue.poll());
        }

        int last = push_queue.poll();

        Queue<Integer> temp = push_queue;
        push_queue = pop_queue;
        pop_queue = temp;

        return last;
    }

    public int top() {

        while (push_queue.size() > 0) {
            pop_queue.add(push_queue.poll());
        }

        int last = push_queue.poll();
        pop_queue.add(last);

        Queue<Integer> temp = push_queue;
        push_queue = pop_queue;
        pop_queue = temp;

        return last;


    }

    public boolean empty() {
        return push_queue.isEmpty() && pop_queue.isEmpty();
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */