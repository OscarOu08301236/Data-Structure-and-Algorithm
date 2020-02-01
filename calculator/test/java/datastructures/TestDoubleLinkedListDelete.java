package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified. You should give your tests
 * with a timeout of 1 second.
 *
 * This test extends the BaseTestDoubleLinkedList class. This means that
 * you can use the helper methods defined within BaseTestDoubleLinkedList.
 * @see BaseTestDoubleLinkedList
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDoubleLinkedListDelete extends BaseTestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }
    
    // Above are some examples of provided assert methods from JUnit,
    // but in these tests you will also want to use a custom assert
    // we have provided you in BaseTestDoubleLinkedList called
    // assertListValidAndMatches. It will check many properties of
    // your DoubleLinkedList so you will want to use it frequently.
    // For usage examples, you can refer to TestDoubleLinkedList,
    // and refer to BaseTestDoubleLinkedList for the method comment.

    @Test(timeout=SECOND)
    public void testDeleteMiddleElement() {
        IList<String> list = makeBasicList();

        assertEquals("b", list.delete(1));
        this.assertListValidAndMatches(new String[] {"a", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteIndexOfAndDeleteMiddle() {
        IList<String> list = makeBasicList();
        list.delete(1);

        assertEquals(-1, list.indexOf("b"));
        assertTrue(!list.contains("b"));
        this.assertListValidAndMatches(new String[] {"a", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteUpdatesSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.delete(1);

        assertEquals(initSize - 1, list.size());
        this.assertListValidAndMatches(new String[] {"a", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteFrontElement() {
        IList<String> list = makeBasicList();

        assertEquals("a", list.delete(0));
        this.assertListValidAndMatches(new String[] {"b", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteBackElement() {
        IList<String> list = makeBasicList();

        assertEquals("c", list.delete(2));
        this.assertListValidAndMatches(new String[] {"a", "b"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteDuplicates() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        int cap = 1000;
        for (int i = 0; i < cap; i++) {
            list.add("b");
        }
        list.add("c");
        list.add("b");
        assertEquals(cap + 3, list.size());
        while (list.contains("b")) {
            assertEquals("b", list.delete(list.indexOf("b")));
        }
        this.assertListValidAndMatches(new String[] {"a", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteSingleElementList() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a");
        list.delete(0);

        assertEquals(0, list.size());
        this.assertListValidAndMatches(new String[] {}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteOutOfBoundsThrowsException() {
        IList<String> list = makeBasicList();
        try {
            list.delete(4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing
        }
    }

    @Test(timeout=SECOND)
    public void testDeleteOnEmptyListThrowsException() {
        IList<String> list = this.makeBasicList();
        list.remove();
        list.remove();
        list.remove();
        try {
            list.delete(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=15 * SECOND)
    public void testDeleteFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());

        for (int i = 0; i < cap; i++) {
            list.delete(0);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testDeleteBackIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }
        assertEquals(cap, list.size());

        for (int i = 0; i < cap; i++) {
            list.delete(list.size() - 1);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testDeleteNearEndIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i * 2);
        }

        for (int i = 0; i < cap - 1; i++) {
            list.delete(list.size() - 2);
        }
        assertEquals(1, list.size());
    }

    @Test(timeout=SECOND)
    public void testDeleteNoLowerBound() {
        IList<String> list = this.makeBasicList();
        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing
        }
    }
}
