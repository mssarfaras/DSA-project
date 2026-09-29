package test;

import java.util.NoSuchElementException;
import queue.ServiceRequest;
import queue.ServiceRequestQueue;
import stack.Action;
import stack.ActionStack;

/** Lightweight executable checks; run with assertions enabled (-ea). */
public final class StackQueueTest {
    private StackQueueTest() { }

    public static void main(String[] args) {
        testStack();
        testQueue();
        testInvalidData();
        System.out.println("All StackQueueTest checks passed.");
    }

    private static void testStack() {
        ActionStack stack = new ActionStack();
        assert stack.isEmpty() && stack.size() == 0;
        Action add = new Action(Action.Type.ADD, "S01", "Added student");
        Action update = new Action(Action.Type.UPDATE, "S02", "Updated address");
        Action delete = new Action(Action.Type.DELETE, "S03", "Deleted student");
        stack.push(add); stack.push(update); stack.push(delete);
        stack.display();
        assert stack.size() == 3 && stack.peek() == delete;
        assert stack.pop() == delete;
        assert stack.pop() == update;
        assert stack.pop() == add;
        assert stack.isEmpty();
        expectEmpty(() -> stack.peek());
        expectEmpty(() -> stack.pop());
    }

    private static void testQueue() {
        ServiceRequestQueue queue = new ServiceRequestQueue();
        assert queue.isEmpty() && queue.size() == 0;
        ServiceRequest first = new ServiceRequest("R01", "S01", "Enrollment letter");
        ServiceRequest second = new ServiceRequest("R02", "S02", "Fee receipt");
        ServiceRequest third = new ServiceRequest("R03", "S03", "Library access");
        queue.enqueue(first); queue.enqueue(second); queue.enqueue(third);
        queue.display();
        assert queue.size() == 3 && queue.peek() == first;
        assert queue.dequeue() == first;
        assert queue.dequeue() == second;
        assert queue.dequeue() == third;
        assert queue.isEmpty();
        expectEmpty(() -> queue.peek());
        expectEmpty(() -> queue.dequeue());
    }

    private static void testInvalidData() {
        try { new Action(Action.Type.ADD, " ", "description"); throw new AssertionError("blank student ID accepted"); }
        catch (IllegalArgumentException expected) { }
        ActionStack stack = new ActionStack();
        try { stack.push(null); throw new AssertionError("null action accepted"); }
        catch (IllegalArgumentException expected) { }
        ServiceRequestQueue queue = new ServiceRequestQueue();
        try { queue.enqueue(null); throw new AssertionError("null request accepted"); }
        catch (IllegalArgumentException expected) { }
    }

    private static void expectEmpty(Runnable operation) {
        try { operation.run(); throw new AssertionError("empty operation did not throw"); }
        catch (NoSuchElementException expected) { }
    }
}
