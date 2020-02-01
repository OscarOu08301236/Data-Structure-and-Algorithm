package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    private final double lambda;

    // You MUST use this field to store the contents of your dictionary.
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    private int elementNum;
    private int chainsLength;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.lambda = 1;
        this.elementNum = 0;
        this.chains = makeArrayOfChains(5);
        this.chainsLength = chains.length;
    }

    public ChainedHashDictionary(double lambda) {
        this.lambda = lambda;
        this.elementNum = 0;
        this.chains = makeArrayOfChains(5);
        this.chainsLength = chains.length;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    private int getHashCode(K key) {
        if (key == null) {
            return 0;
        }
        else {
            return Math.abs(key.hashCode()) % chainsLength;
        }
    }

    @Override
    public V get(K key) {
        int newHashCode = getHashCode(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[newHashCode].get(key);
    }

    @Override
    public void put(K key, V value) {
        int newHashCode = getHashCode(key);
        if (chains[newHashCode] == null) {
            chains[newHashCode] = new ArrayDictionary<K, V>();
        }
        if (!chains[newHashCode].containsKey(key)) {
            this.elementNum++;
        }
        chains[newHashCode].put(key, value);
        if ((double) this.elementNum / (double) chainsLength >= lambda) {
            this.chainsLength *= 2;
            IDictionary<K, V>[] newResizeChains = makeArrayOfChains(this.chainsLength);
            for (KVPair<K, V> kvPair : this) {
                int resizeHashCode = getHashCode(kvPair.getKey());
                if (newResizeChains[resizeHashCode] == null) {
                    newResizeChains[resizeHashCode] = new ArrayDictionary<K, V>();
                }
                newResizeChains[resizeHashCode].put(kvPair.getKey(), kvPair.getValue());
            }
            this.chains = newResizeChains;
        }
    }

    @Override
    public V remove(K key) {
        int newHashCode = getHashCode(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        this.elementNum--;
        return chains[newHashCode].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int newHashCode = getHashCode(key);
        if (chains[newHashCode] == null) {
            return false;
        }
        else {
            return chains[newHashCode].containsKey(key);
        }
    }

    @Override
    public int size() {
        return this.elementNum;
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        int newHashCode = getHashCode(key);
        if (chains[newHashCode] == null) {
            return defaultValue;
        } else {
            return chains[newHashCode].getOrDefault(key, defaultValue);
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int currentIndex;
        private Iterator<KVPair<K, V>> iter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.currentIndex = 0;
            this.iter = null;
            if (this.chains[0] != null) {
                this.iter = this.chains[0].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = currentIndex; i < chains.length - 1; i++) {
                if (iter != null && iter.hasNext()) {
                    return true;
                }
                else {
                    this.currentIndex++;
                    if (chains[currentIndex] != null) {
                        iter = chains[currentIndex].iterator();
                    }
                }
            }
            if (iter != null && iter.hasNext()) {
                return true;
            }
            return false;
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return iter.next();
        }
    }
}
