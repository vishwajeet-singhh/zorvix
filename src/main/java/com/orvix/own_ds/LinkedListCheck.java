package com.orvix.own_ds;


public class LinkedListCheck {


    public static void main(String[] args) {

        LinkedList list = new LinkedList();

        list.addLast(20);
        list.addLast(30);
        list.addLast(40);
        list.addFirst(10);
        list.addFirst(5);


        System.out.println(list);

       // list.createCycle(2);
        System.out.println("Detect " + list.detectCycle());

        System.out.println("Remove " + list.removeCycle());


    }
}
