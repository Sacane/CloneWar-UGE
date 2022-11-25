package fr.ramatellier.clonewar.clone.sample;

import java.util.Objects;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Fifo<E> implements Iterable<E>{
    private final int maxSize;
    private final E[] queue;
    private int cursorHead;
    private int cursorTail;
    private int size;

    @SuppressWarnings("unchecked")
    public Fifo(int maxSize){
        if(maxSize < 1){
            throw new IllegalArgumentException("Size can't be less than 1");
        }
        this.maxSize = maxSize;
        queue = (E[])new Object[maxSize];
    }

    public void offer(E element){
        Objects.requireNonNull(element);
        if(size >= maxSize){
            throw new IllegalStateException();
        }
        queue[cursorTail] = element;
        cursorTail = (cursorTail + 1) % maxSize;
        size++;
    }

    public E poll(){
        if(size == 0){
            throw new IllegalStateException();
        }
        var res = queue[cursorHead];
        cursorHead = (cursorHead + 1) % maxSize;
        size--;
        return res;
    }

    @Override
    public String toString() {
        if(size == 0){
            return "[]";
        }
        var joiner = new StringJoiner(", ", "[", "]");
        for(int i = cursorHead, cnt = 0; cnt < size; i = (i + 1) % maxSize, cnt++){
            joiner.add(queue[i].toString());
        }
        return joiner.toString();
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for(int i = cursorHead, cnt = 0; cnt < size; i = (i + 1) % maxSize, cnt++){
            action.accept(queue[i]);
        }
    }

    public Iterator<E> iterator(){
        var instance = this;
        return new Iterator<>() {
            private int index = cursorHead;
            private int curSize;

            @Override
            public boolean hasNext() {
                return curSize < instance.size;
            }

            @Override
            public E next() {
                if(curSize >= size){
                    throw new NoSuchElementException("No more elements");
                }
                var res = instance.queue[index];
                index = (index + 1) % instance.maxSize;
                curSize++;
                return res;
            }
        };
    }


}