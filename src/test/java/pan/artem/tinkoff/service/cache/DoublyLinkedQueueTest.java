package pan.artem.tinkoff.service.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoublyLinkedQueueTest {

    private DoublyLinkedQueue<String> queue;

    @BeforeEach
    void init() {
        queue = new DoublyLinkedQueue<>();
    }

    @Test
    void add() {
        var node = queue.add("one");

        Assertions.assertEquals("one", node.getValue());
    }

    @Test
    void remove() {
        queue.add("one");
        queue.add("two");

        Assertions.assertEquals("one", queue.remove());
        Assertions.assertEquals("two", queue.remove());
    }

    @Test
    void sizeAndIsEmpty() {
        Assertions.assertTrue(queue.isEmpty());
        Assertions.assertEquals(0, queue.size());

        queue.add("one");
        Assertions.assertFalse(queue.isEmpty());
        Assertions.assertEquals(1, queue.size());

        queue.add("two");
        Assertions.assertFalse(queue.isEmpty());
        Assertions.assertEquals(2, queue.size());
    }

    @Test
    void sizeAndIsEmptyRemove() {
        queue.add("one");
        queue.add("two");

        queue.remove();
        Assertions.assertFalse(queue.isEmpty());
        Assertions.assertEquals(1, queue.size());

        queue.remove();
        Assertions.assertTrue(queue.isEmpty());
        Assertions.assertEquals(0, queue.size());
    }
}