package com.stephendemo.jucaqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jmfen
 * date 2020-04-07
 */
public class ConcurrentLinkedQueueDemo<T> {
    private TNode<T> head;
    private TNode<T> tail;
    public ConcurrentLinkedQueueDemo(){
        head = tail = new TNode<>(null);
    }

    private boolean offer(T t){
        TNode<T> node = new TNode<>(t);
        boolean flag = tail.castNext(null, node);
        return flag;
    }

    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        queue.offer(5);
        System.out.println(queue.poll());

        ConcurrentLinkedQueueDemo<Integer> queueDemo = new ConcurrentLinkedQueueDemo<>();
        System.out.println(queueDemo.offer(5));
    }

    public static class TNode<T>{
        T item;
        TNode<T> next;

        TNode(T item){
            UNSAFE.putObject(this, itemOffset, item);
        }

        boolean castNext(TNode<T> cmp, TNode<T> val){
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }

        private static final Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;

        static {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE = (Unsafe) field.get(null);

                Class<?> k = TNode.class;
                itemOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}