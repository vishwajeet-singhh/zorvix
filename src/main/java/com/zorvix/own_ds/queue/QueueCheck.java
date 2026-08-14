package com.zorvix.own_ds.queue;

public class QueueCheck {

    public static void main(String[] args) {
        LinearQueue linearQueue = new LinearQueue();

        linearQueue.enqueue(10);
        linearQueue.enqueue(20);
        linearQueue.enqueue(30);


        System.out.println(linearQueue);

        linearQueue.enqueue(40);
        linearQueue.enqueue(50);
        linearQueue.enqueue(60);

        System.out.println(linearQueue);

    }
}
