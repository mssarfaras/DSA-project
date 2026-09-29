package stack;

import java.util.NoSuchElementException;

/** A manually linked, last-in-first-out stack of actions. */
public final class ActionStack {
    private static final class Node {
        private final Action action;
        private Node next;
        private Node(Action action, Node next) { this.action = action; this.next = next; }
    }

    private Node top;
    private int size;

    /** Adds a non-null action to the top of the stack. */
    public void push(Action action) {
        if (action == null) throw new IllegalArgumentException("action must not be null");
        top = new Node(action, top);
        size++;
    }

    /** Removes and returns the most recently added action. */
    public Action pop() {
        ensureNotEmpty("pop");
        Action result = top.action;
        top = top.next;
        size--;
        return result;
    }

    /** Returns the most recently added action without removing it. */
    public Action peek() {
        ensureNotEmpty("peek");
        return top.action;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    /** Prints entries from newest to oldest. */
    public void display() {
        if (isEmpty()) { System.out.println("Action history is empty."); return; }
        for (Node current = top; current != null; current = current.next) {
            System.out.println(current.action);
        }
    }

    private void ensureNotEmpty(String operation) {
        if (isEmpty()) throw new NoSuchElementException("Cannot " + operation + " an empty action stack");
    }
}
