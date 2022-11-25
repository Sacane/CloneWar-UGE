package fr.ramatellier.clonewar.clone.sample;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ClonedFifo<E> implements Iterable<E>{
    private final int maxSize;
    private final E[] queue;
    private int cursorHead;

    //@invisible field name
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public ClonedFifo(int maxSize){
        if(maxSize < 1){
            throw new IllegalArgumentException("Size can't be less than 1");
        }
        this.maxSize = maxSize;
        queue = (E[])new Object[maxSize];
    }

    /**
     * @InvisibleDiff different syntax but same bytecode
     * @param element
     */
    public void offer(E element){
        Objects.requireNonNull(element);
        if(size >= maxSize){
            throw new IllegalStateException("This exception should not interfer with clonage");
        }
        //This instruction is basically at the end of the method, this should be detected as a clone
        size++;
        queue[tail] = element;

        /* The next two lines should be the same code as the following :
        * tail = (tail + 1) % maxSize;
        *  */
        tail = (tail + 1);
        tail = tail % maxSize;

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

    /**
     * @NotCloned replace for to while loop
     * @param action The action to be performed for each element
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        int i = cursorHead, cnt = 0;
        while(cnt < size){
            action.accept(queue[i]);
            i = (i + 1) % maxSize;
            cnt++;
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