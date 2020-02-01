package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;

    // You may add extra fields or helper methods though!
    private int size;
    private int capacity;

    public ArrayDictionary() {
        pairs = this.makeArrayOfPairs(10);
        size = 0;
        capacity = 10;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V result = null;
        for (int i = 0; i < this.size(); i++) {
            if (Objects.equals(pairs[i].key, key)) {
                result = pairs[i].value;
            }
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        if (this.containsKey(key)) {
            for (int i = 0; i < this.size(); i++) {
                if (Objects.equals(pairs[i].key, key)) {
                    pairs[i].value = value;
                }
            }
        }
        else {
            if (this.size() == capacity) {
                capacity *= 2;
                Pair<K, V>[] newPairs = this.makeArrayOfPairs(capacity);
                for (int i = 0; i < this.size(); i++) {
                    newPairs[i] = new Pair<K, V>(pairs[i].key, pairs[i].value);
                }
                pairs = newPairs;
            }
            pairs[this.size()] = new Pair<K, V>(key, value);
            size++;
        }
    }

    @Override
    public V remove(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V result = null;
        for (int i = 0; i < this.size(); i++) {
            if (Objects.equals(pairs[i].key, key)) {
                result = pairs[i].value;
                pairs[i] = pairs[this.size() - 1];
                size--;
                return result;
            }
        }
        size--;
        return result;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (Objects.equals(pairs[i].key, key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        int index = getOrDefaultHelper(key);
        if (index < 0) {
            return defaultValue;
        } else {
            return pairs[index].value;
        }
    }

    private int getOrDefaultHelper(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (pairs[i].key == key) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(this.pairs, this.size());
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int size;
        private int currentIndex;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.pairs = pairs;
            this.size = size;
            this.currentIndex = 0;
        }

        public boolean hasNext() {
            return !(currentIndex > size - 1);
        }

        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> result = new KVPair<K, V>(this.pairs[currentIndex].key, this.pairs[currentIndex].value);
            currentIndex++;
            return result;
        }
    }
}
