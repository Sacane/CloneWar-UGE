package fr.ramatellier.clonewar.clone.sample;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface Slice<E> {
    static <E> Slice<E> array(E[] elements){
        return new ArraySlice<>(elements);
    }

    static <E> Slice<E> array(E[] elements, int from, int to){
        if(to < from || to > elements.length || from < 0){
            throw new IndexOutOfBoundsException("value(s) are not allowed in this context: to = " + to + " from = " + from);
        }
        return new SubArraySlice<>(elements, from, to);
    }
    E get(int index);
    int size();
    Slice<E> subSlice(int from, int to);
    final class ArraySlice<E> implements Slice<E>{
        private final E[] array;
        int size;

        private ArraySlice(E[] array){
            Objects.requireNonNull(array);

            this.array = array;
            this.size = array.length;
        }

        @Override
        public E get(int index) {
            Objects.checkIndex(index, size);
            for(int i = 0; i < size; i++){
                if(i == index){
                    return array[i];
                }
            }
            return null;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Slice<E> subSlice(int from, int to) {
            return new SubArraySlice<>(array, from, to);
        }

        @Override
        public String toString() {
            return "[" + Arrays.stream(array).map(i -> (i == null) ? "null" : i.toString()).collect(Collectors.joining(", ")) + "]";
        }
    }
    final class SubArraySlice<E> implements Slice<E>{
        private final E[] array;
        private int from;
        private int to;

        private SubArraySlice(E[] elements, int from, int to){
            Objects.requireNonNull(elements);
            Objects.checkFromToIndex(from, to, elements.length);
            this.array = elements;
            this.from = from;
            this.to = to;
        }
        @Override
        public E get(int index) {
            if(index < 0 || index >= size()){
                throw new IndexOutOfBoundsException("Outbound reached");
            }
            return array[index + from];
        }

        @Override
        public int size() {
            return to - from;
        }

        @Override
        public Slice<E> subSlice(int from, int to) {
            Objects.checkFromToIndex(from, to, size());
            return new SubArraySlice<>(array, from + this.from, to + this.from);
        }

        @Override
        public String toString() {
            return "[" + Arrays.stream(array, from, to).map(i -> (i == null) ? "null" : i.toString()).collect(Collectors.joining(", ")) + "]";
        }
    }
}