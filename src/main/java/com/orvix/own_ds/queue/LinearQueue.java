package com.orvix.own_ds.queue;

import java.util.Arrays;

/**
 * An array-based linear queue implementation.
 *
 * <p>Follows the FIFO (First In, First Out) principle where elements
 * are inserted from the rear and removed from the front.</p>
 *
 * <h2>Implemented Features</h2>
 *
 * <ul>
 *     <li>Insertion
 *         <ul>
 *             <li>{@link #enqueue(int)}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Deletion
 *         <ul>
 *             <li>{@link #dequeue()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Access
 *         <ul>
 *             <li>{@link #peek()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Utilities
 *         <ul>
 *             <li>{@link #isEmpty()}</li>
 *             <li>{@link #isFull()}</li>
 *             <li>{@link #size()}</li>
 *             <li>{@link #clear()}</li>
 *             <li>{@link #toArray()}</li>
 *             <li>{@link #print()}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Limitations</h2>
 *
 * <ul>
 *     <li>Unused spaces at the beginning of the array cannot be reused.</li>
 *     <li>May report full even when empty slots exist.</li>
 * </ul>
 *
 * <h2>Roadmap</h2>
 *
 * <ul>
 *     <li>Dynamic resizing</li>
 *     <li>Generic implementation</li>
 * </ul>
 *
 * @author Vishwajeet Pratap Singh
 * @version 1.0
 */

/**
 * An array-based linear queue implementation.
 *
 * <p>Follows the FIFO (First In, First Out) principle where elements
 * are inserted from the rear and removed from the front.</p>
 *
 * <h2>Implemented Features</h2>
 *
 * <ul>
 *     <li>Insertion
 *         <ul>
 *             <li>{@link #enqueue(int)}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Deletion
 *         <ul>
 *             <li>{@link #dequeue()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Access
 *         <ul>
 *             <li>{@link #peek()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Utilities
 *         <ul>
 *             <li>{@link #isEmpty()}</li>
 *             <li>{@link #isFull()}</li>
 *             <li>{@link #size()}</li>
 *             <li>{@link #clear()}</li>
 *             <li>{@link #toArray()}</li>
 *             <li>{@link #print()}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Limitations</h2>
 *
 * <ul>
 *     <li>Unused spaces at the beginning of the array cannot be reused.</li>
 *     <li>Dequeue operation requires shifting all elements.</li>
 * </ul>
 *
 * <h2>Roadmap</h2>
 *
 * <ul>
 *     <li>Dynamic resizing</li>
 *     <li>Generic implementation</li>
 *     <li>Circular Queue</li>
 * </ul>
 *
 * @author Vishwajeet Pratap Singh
 * @version 1.0
 */
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

    /**
     * Inserts an element at the rear of the queue.
     */
    public void enqueue(int data) {

        if (size == arr.length) grow();
        if (-1 == front) front = 0;
        arr[size++] = data;

    }

    /**
     * Removes the element from the front of the queue.
     */
    public void dequeue() {

    }

    /**
     * Returns the front element without removing it.
     */
    public int peek() {
        return 0;
    }

    /**
     * Returns true if the queue contains no elements.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns true if the queue is full.
     */
    public boolean isFull() {
        return false;
    }

    /**
     * Returns the number of elements in the queue.
     */
    public int size() {
        return 0;
    }

    /**
     * Removes all elements from the queue.
     */
    public void clear() {

    }

    /**
     * Returns the queue as an integer array.
     */
    public int[] toArray() {
        return null;
    }

    /**
     * Prints all elements in FIFO order.
     */
    public void print() {

    }

    /**
     * Increases the capacity of the underlying array.
     */
    private void grow() {
        arr = Arrays.copyOf(arr, 2 * arr.length);
    }

    @Override
    public String toString() {

        if (size == 0) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < size; i++) {
            builder.append(arr[i]);

            if (i != size - 1) {
                builder.append(",");
            }
        }

        builder.append("]");

        return builder.toString();
    }
}