package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;


/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    private int size;
    private int capacity;
    private IDictionary<T, Integer> map;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.capacity = 10000;
        this.heap = makeArrayOfT(capacity);
        this.size = 0;
        this.map = new ChainedHashDictionary<>();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        if (index > 0) {
            if (heap[index].compareTo(heap[(index - 1) / NUM_CHILDREN]) < 0) {
                swap(index, (index - 1) / NUM_CHILDREN);
                percolateUp((index - 1) / NUM_CHILDREN);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        if ((4 * index) + 1 < this.size()) {
            int minIndex = (4 * index) + 1;
            for (int i = 1; i < 4; i++) {
                if ((4 * index) + 1 + i >= this.size()) {
                    break;
                }
                if (heap[minIndex].compareTo(heap[(4 * index) + 1 + i]) > 0) {
                    minIndex = (4 * index) + 1 + i;
                }
            }
            if (heap[minIndex].compareTo(heap[index]) < 0) {
                swap(minIndex, index);
                percolateDown(minIndex);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (index == 0) {
            percolateDown(index);
        }
        else {
            int parentIndex = (index - 1) / NUM_CHILDREN;
            if (heap[index].compareTo(heap[parentIndex]) < 0) {
                percolateUp(index);
            }
            else if (heap[index].compareTo(heap[parentIndex]) > 0) {
                percolateDown(index);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        T oldA = heap[a];
        T oldB = heap[b];
        heap[a] = oldB;
        heap[b] = oldA;
        map.put(oldB, a);
        map.put(oldA, b);
    }

    @Override
    public T removeMin() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        T result = heap[0];
        size--;
        map.remove(heap[0]);
        heap[0] = heap[this.size()];
        map.put(heap[this.size()], 0);
        percolateDown(0);
        return result;
    }

    @Override
    public T peekMin() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.contains(item)) {
            throw new InvalidElementException();
        }
        if (this.size() == capacity) {
            T[] oldHeap = heap;
            capacity *= 2;
            heap = makeArrayOfT(capacity);
            for (int i = 0; i < this.size(); i++) {
                heap[i] = oldHeap[i];
            }
        }
        heap[this.size()] = item;
        map.put(item, this.size());
        size++;
        percolateUp(this.size() - 1);
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return map.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(item)) {
            throw new InvalidElementException();
        }
        int index = map.remove(item);
        size--;
        if (index == this.size()) {
            heap[index] = null;
        }
        else {
            T last = heap[this.size()];
            heap[index] = last;
            map.put(last, index);
            percolate(index);
        }
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (!this.contains(oldItem)) {
            throw new InvalidElementException();
        }
        if (this.contains(newItem)) {
            throw new InvalidElementException();
        }
        int index = map.remove(oldItem);
        heap[index] = newItem;
        map.put(newItem, index);
        percolate(index);
    }

    @Override
    public int size() {
        return size;
    }
}
