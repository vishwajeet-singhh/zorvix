package com.orvix.own_ds.queue;

import javax.sound.sampled.Line;

public class QueueCheck {

    public static void main(String[] args) {
        LinearQueue linearQueue = new LinearQueue();

        linearQueue.enqueue(10);
        linearQueue.enqueue(20);
        linearQueue.enqueue(30);
        linearQueue.enqueue(40);
        linearQueue.enqueue(50);
        linearQueue.enqueue(60);


        System.out.println(linearQueue);
        linearQueue.dequeue();
        linearQueue.dequeue();
        System.out.println(linearQueue);

    }
}
