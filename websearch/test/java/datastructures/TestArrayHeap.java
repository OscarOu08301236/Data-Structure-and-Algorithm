package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testBasicRemoveMin() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());

        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testRemoveMinThrowException() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        assertEquals(0, heap.size());
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicPeekMin() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        assertEquals(values[0], heap.peekMin());

        assertEquals(5, heap.size());
    }

    @Test(timeout=SECOND)
    public void testPeekMinThrowException() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        assertEquals(0, heap.size());
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicRemoveMinAndPeekMin() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        assertEquals(values[0], heap.peekMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.peekMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.peekMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(values[3], heap.peekMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.peekMin());
        assertEquals(values[4], heap.removeMin());

        assertEquals(0, heap.size());

        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException err) {
            //do nothing
        }
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        }
        catch (EmptyContainerException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicAdd() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);

        //System.out.println(Arrays.deepToString(array));

        heap.add(1);
        heap.add(5);
        heap.add(2);
        heap.add(4);

        //System.out.println(Arrays.deepToString(array));

        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(4, heap.removeMin());
        assertEquals(5, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testAddNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        try {
            heap.add(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testAddSameElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);

        try {
            heap.add(3);
            fail("Expected InvalidElementException");
        }
        catch (InvalidElementException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicContains() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);
        heap.add(2);
        heap.add(3);

        assertTrue(heap.contains(1));
        assertTrue(heap.contains(2));
        assertTrue(heap.contains(3));
        assertFalse(heap.contains(6));
    }

    @Test(timeout=SECOND)
    public void testContainsThrowException() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        try {
            heap.contains(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.add(4);
        heap.add(5);

        heap.remove(4);
        heap.remove(2);
        heap.remove(1);

        Comparable<Integer>[] array = getArray(heap);

        assertEquals(2, heap.size());
        assertEquals(3, array[0]);
        assertEquals(5, array[1]);
    }

    @Test(timeout=SECOND)
    public void testRemoveNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        try {
            heap.remove(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveNoSuchElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);
        heap.add(2);
        heap.add(3);

        try {
            heap.remove(7);
            fail("Expected InvalidElementException");
        }
        catch (InvalidElementException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testBasicReplace() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);
        heap.add(2);
        heap.add(3);
        heap.replace(2, 4);

        assertEquals(3, heap.size());
        assertEquals(1, heap.removeMin());
        assertEquals(3, heap.removeMin());
        assertEquals(4, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testReplaceNoOldElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        try {
            heap.replace(1, 2);
            fail("Expected InvalidElementException");

        }
        catch (InvalidElementException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testReplaceDuplicateNewElement() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);
        heap.add(2);
        heap.add(3);

        try {
            heap.replace(1, 2);
            fail("Expected InvalidElementException");

        }
        catch (InvalidElementException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testIsNotEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        heap.add(1);

        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testLargeArrayHeapAddAndRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 100; i++) {
            heap.add(100 - i);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(i + 1, heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testLargeArrayHeapResize() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 10001; i++) {
            heap.add(10001 - i);
        }

        assertEquals(10001, heap.size());

        for (int i = 0; i < 10001; i++) {
            assertEquals(i + 1, heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testPeekMinRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 1000; i++) {
            heap.add(999 - i);
        }

        for (int i = 0; i < 1000; i++) {
            heap.remove(heap.peekMin());
        }

        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testRemovingDoesNotRemoveIndicesFromDictionary() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        for (int i = 0; i < 5; i++) {
            heap.add(4 - i);
        }

        heap.removeMin();
        heap.remove(2);
        heap.remove(3);
        heap.replace(1, 6);

        assertEquals(true, heap.contains(6));
        assertEquals(false, heap.contains(1));
        assertEquals(false, heap.contains(0));
        assertEquals(false, heap.contains(3));
        assertEquals(false, heap.contains(2));
    }

    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

}
