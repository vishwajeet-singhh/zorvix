package com.orvix.own_ds.linkedlist;


import java.util.Stack;

/**
/**
 * A singly linked list implementation.
 *
 * <p>Supports common linked list operations including insertion, deletion,
 * searching, traversal, reversal, cycle detection, and conversion utilities.</p>
 *
 * <h2>Implemented Features</h2>
 *
 * <ul>
 *     <li>Insertion
 *         <ul>
 *             <li>{@link #addFirst(int)}</li>
 *             <li>{@link #addLast(int)}</li>
 *             <li>{@link #insertAt(int, int)}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Deletion
 *         <ul>
 *             <li>{@link #removeFirst()}</li>
 *             <li>{@link #removeLast()}</li>
 *             <li>{@link #deleteAt(int)}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Searching
 *         <ul>
 *             <li>{@link #contains(int)}</li>
 *             <li>{@link #indexOf(int)}</li>
 *             <li>{@link #get(int)}</li>
 *             <li>{@link #findMiddle()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Utilities
 *         <ul>
 *             <li>{@link #reverse()}</li>
 *             <li>{@link #clear()}</li>
 *             <li>{@link #isEmpty()}</li>
 *             <li>{@link #toArray()}</li>
 *             <li>{@link #print()}</li>
 *         </ul>
 *     </li>
 *
 *     <li>Cycle
 *         <ul>
 *             <li>{@link #detectCycle()}</li>
 *             <li>{@link #removeCycle()}</li>
 *             <li>{@link #createCycle(int)} (testing only)</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Roadmap</h2>
 *
 * <ul>
 *     <li>kthFromEnd()</li>
 *     <li>mergeSortedLists()</li>
 *     <li>removeDuplicates()</li>
 *     <li>palindromeCheck()</li>
 *     <li>recursiveReverse()</li>
 * </ul>
 *
 * @author Vishwajeet Pratap Singh
 * @version 1.0
 */


public class LinkedList {

    private Node head;
    private Node tail;

    private int size = 0;


    public boolean removeCycle() {

        Node slow = head;
        Node fast = head;



        // Detect the cycle
        while (fast != null && fast.next != null) {

            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast)
                break;
        }

        // No cycle
        if (fast == null || fast.next == null)
            return false;

        // Find the start of the cycle
        slow = head;

        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        // Find the last node
        while (fast.next != slow) {
            fast = fast.next;
        }

        // Break the cycle
        fast.next = null;

        return true;
    }


    public boolean detectCycle() {

        Node slow = head;
        Node fast = head;


        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) return true;

        }

        return false;

    }

    public boolean createCycle(int index) {

        if (head == null || 0 > index || index >= size) return false;

        Node temp = head;
        int curr = 0;

        while (temp != null) {
            if (index == curr) {
                tail.next = temp;
                return true;
            }
            curr++;
            temp = temp.next;
        }

        return true;

    }


    public String print() {

        StringBuilder sb = new StringBuilder();

        Node temp = head;


        while (temp != null) {
            sb.append(temp.data);
            if (temp.next != null) sb.append(",");
            temp = temp.next;
        }
        return sb.toString();


    }

    public int[] toArray() {

        int curr = 0;
        Node temp = head;
        int[] arr = new int[size];

        while (temp != null) {
            arr[curr] = temp.data;
            temp = temp.next;
            curr++;
        }
        return arr;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

        head = null;
        tail = null;
        size = 0;

    }

    public int indexOf(int data) {

        if (head == null) return -1;

        Node temp = head;
        int curr = 0;

        while (temp != null) {

            if (temp.data == data) return curr;


            temp = temp.next;
            curr++;
        }
        return -1;
    }

    public int get(int index) {

        if (head == null || index < 0)
            return -1;

        Node node = head;

        int curr = 0;

        while (node != null) {
            if (curr == index) {
                return node.data;
            }

            node = node.next;
            curr++;

        }
        return -1;

    }

    public int findMiddle() {

        if (head == null) return -1;

        Node slow = head;
        Node fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow.data;
    }

    public void reverse() {
        if (head == null || head.next == null) return;

        Node prev = null;
        Node curr = head;

        while (curr != null) {
            Node next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        head = prev;
    }

    public boolean deleteAt(int index) {
        // need to implement

        if (index >= size || 0 > index) return false;


        if (index == 0) {
            removeFirst();
            return true;
        }

        if (index == size - 1) {
            removeLast();
            return true;
        }

        Node current = head;
        int curr = 0;

        while (current != null) {
            if (index - 1 == curr) {
                current.next = current.next.next;
                size--;
                return true;
            }
            curr++;
            current = current.next;
        }
        return false;

    }

    public boolean insertAt(int index, int data) {
        // need to implement

        if (index > size || index < 0) return false;

        if (index == 0) {
            addFirst(data);
            return true;
        }
        if (index == size) {
            addLast(data);
            return true;
        }

        int curr = 0;
        Node newNode = new Node(data, null);

        Node current = head;

        while (current != null) {

            if (index - curr == 1) {
                newNode.next = current.next;
                current.next = newNode;
                size++;
                return true;
            }
            curr++;
            current = current.next;
        }
        return false;

    }

    public boolean contains(int data) {

        Node current = head;

        while (current != null) {
            if (current.data == data) return true;

            current = current.next;
        }

        return false;
    }

    public void removeLast() {

        if (head == null) return;

        Node current = head;


        if (head.next == null) {
            head = null;
            tail = null;
        } else {
            while (current.next != tail) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
        size--;
    }

    public void removeFirst() {

        if (head == null) return;

        if (head.next == null) {
            head = null;
            tail = null;
        } else {
            head = head.next;
        }
        size--;
    }

    public void addLast(int data) {
        Node node = new Node(data, null);

        if (head == null) {
            head = node;
            tail = node;
            size++;
            return;
        }

        tail.next = node;
        tail = node;
        size++;
    }

    public void addFirst(int data) {
        Node node = new Node(data, null);

        if (head == null) {
            head = node;
            tail = node;
            size++;
            return;
        }

        node.next = head;
        head = node;
        size++;

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Node current = this.head;


        while (current != null) {
            sb.append(current.data);

            if (current.next != null) {
                sb.append(" -> ");
            }

            if (current.next == null) {
                sb.append(" -> null");
            }
            current = current.next;
        }


        return sb.toString();
    }


    private static class Node {

        int data;
        Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

    }


}
