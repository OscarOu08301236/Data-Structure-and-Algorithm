package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more fields and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> dict;
    private int capacity;
    private int size;

    public ArrayDisjointSet() {
        this.capacity = 100;
        this.pointers = new int[this.capacity];
        this.dict = new ChainedHashDictionary<>();
    }

    @Override
    public void makeSet(T item) {
        if (dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        if (this.size == capacity) {
            capacity *= 2;
            int[] newPointers = new int[capacity];
            for (int i = 0; i < this.size; i++) {
                newPointers[i] = pointers[i];
            }
            pointers = newPointers;
        }
        dict.put(item, this.size);
        pointers[this.size] = -1;
        this.size += 1;
    }

    @Override
    public int findSet(T item) {
        if (!dict.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        int currElement = dict.get(item);
        while (pointers[currElement] > 0) {
            currElement = pointers[currElement];
        }
        int root = currElement;

        int updateElement = dict.get(item);
        while (pointers[updateElement] > 0) {
            int temp = updateElement;
            updateElement = pointers[updateElement];
            pointers[temp] = root;
        }

        return root;
    }

    @Override
    public void union(T item1, T item2) {
        if (!dict.containsKey(item1) || !dict.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        if (findSet(item1) != findSet(item2)) {
            if (pointers[findSet(item1)] > pointers[findSet(item2)]) {
                pointers[findSet(item1)] = findSet(item2);
            }
            else if (pointers[findSet(item1)] < pointers[findSet(item2)]) {
                pointers[findSet(item2)] = findSet(item1);
            }
            else {
                pointers[findSet(item1)] = findSet(item2);
                pointers[findSet(item2)]--;
            }
        }
    }
}
