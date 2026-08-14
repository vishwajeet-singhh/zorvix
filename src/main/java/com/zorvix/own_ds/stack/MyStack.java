package com.zorvix.own_ds.stack;

import java.util.Arrays;
import java.util.EmptyStackException;

public class MyStack<T> {

    private static final int DEFAULT_CAPACITY = 8;

    private Object[] arr;
    private int size;

    public MyStack() {
        arr = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void push(T element) {
        if (size * 0.75 >= arr.length) {
            grow();
        }

        arr[size++] = element;
    }

    public Object pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        Object value = arr[--size];
        arr[size] = null;

        return value;
    }

    public Object top() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        return arr[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(arr, 0, size, null);
        arr = new Integer[DEFAULT_CAPACITY];
        size = 0;
    }

    private void grow() {
        arr = Arrays.copyOf(arr, arr.length * 2);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(arr,size));
    }
}