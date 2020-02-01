package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        Node<T> newNode = new Node<T>(item);
        if (front == null) {
            front = newNode;
        }
        else {
            back.next = newNode;
            newNode.prev = back;
        }
        back = newNode;
        size++;
    }

    @Override
    public T remove() {
        if (this.size() == 0) {
            throw new EmptyContainerException();
        }
        Node<T> curr = back;
        if (this.size() == 1) {
            front = null;
            back = null;
        }
        else {
            back = back.prev;
            back.next = null;
        }
        size--;
        return curr.data;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return front.data;
        }
        else if (index == this.size() - 1) {
            return back.data;
        }
        else {
            Node<T> curr = front;
            if (index < this.size() / 2) {
                for (int i = 0; i < index; i++) {
                    curr = curr.next;
                }
            }
            else {
                curr = back;
                for (int i = this.size() - 1; i > index; i--) {
                    curr = curr.prev;
                }
            }
            return curr.data;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<T>(item);
        if (this.size() == 1) {
            front = newNode;
            back = newNode;
        }
        else if (index == 0) {
            newNode.next = front.next;
            front.next.prev = newNode;
            front = newNode;
        }
        else if (index == this.size() - 1) {
            newNode.prev = back.prev;
            back.prev.next = newNode;
            back = newNode;
        }
        else {
            Node<T> curr = front;
            if (index < this.size() / 2) {
                for (int i = 0; i < index; i++) {
                    curr = curr.next;
                }
            } else {
                curr = back;
                for (int i = this.size() - 1; i > index; i--) {
                    curr = curr.prev;
                }
            }
            Node<T> prevNode = curr.prev;
            Node<T> nextNode = curr.next;
            prevNode.next = newNode;
            newNode.prev = prevNode;
            newNode.next = nextNode;
            nextNode.prev = newNode;
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<T>(item);
        if (this.size() == 0) {
            front = newNode;
            back = newNode;
        }
        else if (index == 0) {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
        else if (index == size) {
            newNode.prev = back;
            back.next = newNode;
            back = newNode;
        }
        else {
            Node<T> curr = front;
            if (index < this.size() / 2) {
                for (int i = 0; i < index; i++) {
                    curr = curr.next;
                }
            }
            else {
                curr = back;
                for (int i = this.size() - 1; i > index; i--) {
                    curr = curr.prev;
                }
            }
            Node<T> prevNode = curr.prev;
            prevNode.next = newNode;
            newNode.prev = prevNode;
            newNode.next = curr;
            curr.prev = newNode;
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> curr = front;
        if (this.size() == 1) {
            front = null;
            back = null;
        }
        else {
            if (index == 0) {
                front = front.next;
                front.prev = null;
            }
            else if (index == this.size() - 1) {
                curr = back;
                back = back.prev;
                back.next = null;
            }
            else {
                if (index < this.size() / 2) {
                    for (int i = 0; i < index; i++) {
                        curr = curr.next;
                    }
                }
                else {
                    curr = back;
                    for (int i = this.size() - 1; i > index; i--) {
                        curr = curr.prev;
                    }
                }
                Node<T> prevNode = curr.prev;
                Node<T> nextNode = curr.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
        size--;
        return curr.data;
    }

    @Override
    public int indexOf(T item) {
        int count = 0;
        Node<T> curr = front;
        for (int i = 0; i < this.size(); i++) {
            if (Objects.equals(curr.data, item)) {
                return count;
            }
            else {
                count++;
                curr = curr.next;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return  indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T result = current.data;
            current = current.next;
            return result;
        }
    }
}
