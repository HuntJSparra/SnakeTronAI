package TeamiumPremium;

import java.lang.reflect.Array;
import java.util.Arrays;

// RIP
class CircularArrayList<T> {
    private int size;
    private int head;
    private int tail;

    private T[] backingArray;

    private final int MIN_SIZE = 100;
    
    public CircularArrayList() {
        this.size = 0;
        this.head = 0;
        this.tail = 0;

        this.backingArray = (T[])Array.newInstance(Object.class, MIN_SIZE);
    }

    public int size() {
        return this.size;
    }

    public void addFirst(T newElement) {
        if (this.size == backingArray.length) {
            resize();
        }

        this.size++;
        this.head = (this.backingArray.length + this.head - 1) % this.backingArray.length;
        this.backingArray[this.head] = newElement;
    }

    public void addLast(T newElement) {
        if (this.size == backingArray.length) {
            resize();
        }

        this.size++;
        this.tail = (this.tail + 1) % this.backingArray.length;
        this.backingArray[this.tail] = newElement;
    }

    public T removeFirst() {
        this.size--;
        T removedElement = this.backingArray[this.head];
        this.backingArray[this.head] = null;
        this.head = (this.head + 1) % this.backingArray.length;

        if (this.size < backingArray.length/2) {
            resize();
        }

        return removedElement;
    }

    public T removeLast() {
        // System.out.println("Remove");
        this.size--;
        T removedElement = this.backingArray[this.tail];
        this.backingArray[this.tail] = null;
        this.tail = (this.backingArray.length + this.tail - 1) % this.backingArray.length;

        if (this.size < backingArray.length/2) {
            resize();
        }

        return removedElement;
    }

    public T get(int index) {
        int modularIndex = (index + this.head) % this.backingArray.length;
        // System.out.println("Index: "+index+"\tModular Index:"+modularIndex);
        // System.out.println("Head: "+this.head+"\tTail: "+this.tail);
        // System.out.println(Arrays.toString(this.backingArray));
        return this.backingArray[modularIndex];
    }

    private void resize() {
        int newSize = Math.max(MIN_SIZE, 2*size);
        T[] newArray = (T[])Array.newInstance(Object.class, newSize);
        T[] oldArray = this.backingArray;

        //System.arraycopy(oldArray, this.head, newArray, 0, this.size);
        for (int index=0; index<this.size; index++) {
            newArray[index] = get(index);
        }

        this.backingArray = newArray;
        this.head = 0;
        this.tail = size-1;
    }
}