package misc;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSorter extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testInvalidK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        try {
            IList<Integer> top = Sorter.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testInvalidInputList() {
        try {
            IList<Integer> top = Sorter.topKSort(5, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException err) {
            //do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(0, list);
        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(9999 - i);
        }

        IList<Integer> top = Sorter.topKSort(50, list);
        for (int i = 0; i < top.size(); i++) {
            assertEquals(9950 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testInputIsEmpty() {
        IList<Integer> list = new DoubleLinkedList<>();

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testKLagerThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();

        for (int i = 0; i < 10; i++) {
            list.add(9 - i);
        }

        IList<Integer> top = Sorter.topKSort(50, list);
        assertEquals(10, top.size());
        for (int i = 0; i < top.size(); i++) {
            //System.out.println(top.get(i));
            assertEquals(i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testMutatesInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(50, list);
        for (int i = 0; i < top.size(); i++) {
            assertEquals(9950 + i, top.get(i));
        }

        for (int i = 0; i < 10000; i++) {
            assertEquals(i, list.get(i));
        }
    }
}
