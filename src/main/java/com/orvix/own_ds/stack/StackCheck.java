package com.orvix.own_ds.stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackCheck {

    private static final Logger log = LoggerFactory.getLogger(StackCheck.class);

    public static void main(String[] args) {

        MyStack<String> stack = new MyStack<>();

        log.info("Is Empty: {}", stack.isEmpty());

        stack.push("true");
        stack.push("Java");
        stack.push("Spring");

        log.info("Top: {}", stack.top());
        log.info(stack.toString());

     /*   stack.push("Docker");
        log.info("Top after push: {}", stack.top());

        stack.pop();
        log.info("Top after pop: {}", stack.top());

        stack.push("Kafka");
        log.info("Top after push: {}", stack.top());

        stack.push("Redis");
        stack.push("PostgreSQL");

        log.info("Stack: {}", stack);

        log.info("Size: {}", stack.size());

        log.info("Popped: {}", stack.pop());
        log.info("Top: {}", stack.top());

        log.info("Popped: {}", stack.pop());
        log.info("Top: {}", stack.top());

        log.info("Stack: {}", stack);

        stack.clear();

        log.info("Size after clear: {}", stack.size());
        log.info("Is Empty after clear: {}", stack.isEmpty());*/

        // Uncomment these if your implementation throws exceptions
        // stack.pop();
        // stack.top();
    }
}