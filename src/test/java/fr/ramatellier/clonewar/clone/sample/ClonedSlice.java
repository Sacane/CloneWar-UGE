package fr.ramatellier.clonewar.clone.sample;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public sealed interface ClonedSlice<E> {
    static <E> ClonedSlice<E> array(E[] elements){
        return new ArraySlice<>(elements);
    }

    static <E> ClonedSlice<E> array(E[] elements, int from, int to){
        if(to < from || to > elements.length || from < 0){
            throw new IndexOutOfBoundsException("value(s) are not allowed in this context: to = " + to + " from = " + from);
        }
        return new SubArrayClonedSlice<>(elements, from, to);
    }
    E get(int index);
    int size();
    ClonedSlice<E> subSlice(int from, int to);
    final class ArraySlice<E> implements ClonedSlice<E>{
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
        public ClonedSlice<E> subSlice(int from, int to) {
            return new SubArrayClonedSlice<>(array, from, to);
        }

        @Override
        public String toString() {
            return "[" + Arrays.stream(array).map(i -> (i == null) ? "null" : i.toString()).collect(Collectors.joining(", ")) + "]";
        }
    }
    final class SubArrayClonedSlice<E> implements ClonedSlice<E>{
        private final E[] array;
        private int from;
        private int to;

        private SubArrayClonedSlice(E[] elements, int from, int to){
            Objects.requireNonNull(elements);
            Objects.checkFromToIndex(from, to, elements.length);
            this.array = elements;
            this.from = from;
            this.to = to;
        }
        @Override
        public E get(int index) {
            //Should not be the same
            Objects.checkIndex(index, size());
            return array[index + from];
        }

        @Override
        public int size() {
            int delta = to - from;
            return delta;
        }

        @Override
        public ClonedSlice<E> subSlice(int from, int to) {
            Objects.checkFromToIndex(from, to, size());
            return new SubArrayClonedSlice<>(array, from + this.from, to + this.from);
        }

        //@Clone using StringBuilder instead of stream
        @Override
        public String toString() {
            var builder = new StringBuilder("[");
            for(int i = from; i < to; i++){
                builder.append((array[i] == null ? "null" : array[i]));
                if(i < to - 1) builder.append(" ,");
            }
            return builder.append("]").toString();
        }
    }
}
