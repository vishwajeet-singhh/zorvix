package com.orvix.own_ds.queue;

import java.util.Arrays;

public class LinearQueue {

    private static final int DEFAULT_CAPACITY = 8;

    private int[] arr;
    private int size;
    private int front;

    public LinearQueue() {
        arr = new int[DEFAULT_CAPACITY];
        size = 0;
        front = -1;
    }

    public void enqueue(int data) {

        if (front == -1) {
            front = 0;
        }

        if (size >= arr.length * 0.75) {
            grow();
        }

        arr[size] = data;
        size++;
    }

    public void dequeue() {

        if (front == -1) {
            return;
        }

        for (int i = 1; i < size; i++) {
            arr[i - 1] = arr[i];
        }

        size--;
        arr[size] = 0;

        if (size == 0) {
            front = -1;
        }
    }

    private void grow() {
        arr = Arrays.copyOf(arr, arr.length + DEFAULT_CAPACITY);
    }

    @Override
    public String toString() {

        if (size == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            sb.append(arr[i]);

            if (i != size - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }
}