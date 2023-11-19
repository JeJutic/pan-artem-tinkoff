package pan.artem.tinkoff.service.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.NoSuchElementException;

public class DoublyLinkedQueue<E> {

    @AllArgsConstructor
    public static class Node<T> {
        private Node<T> left;
        private Node<T> right;
        @Getter
        private T value;

        public void remove() {
            if (left != null) {
                left.right = right;
            }
            if (right != null) {
                right.left = left;
            }
            left = null;    // for idempotent
            right = null;
        }
    }

    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;

    public Node<E> add(E e) {
        size++;
        if (head == null) {
            head = new Node<>(null, null, e);
            tail = head;
            return head;
        }
        Node<E> newTail = new Node<>(tail, null, e);
        tail.right = newTail;
        tail = newTail;
        return tail;
    }

    public Node<E> renew(Node<E> node) {
        node.remove();
        return add(node.value);
    }

    public E remove() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        if (head == tail) {
            tail = null;
        }
        E e = head.value;
        head = head.right;
        if (head != null) {
            head.left = null;
        }
        size--;
        return e;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
