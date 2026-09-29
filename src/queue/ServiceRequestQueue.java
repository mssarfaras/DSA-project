package queue;

import java.util.NoSuchElementException;

/** A manually linked, first-in-first-out queue of service requests. */
public final class ServiceRequestQueue {
    private static final class Node {
        private final ServiceRequest request;
        private Node next;
        private Node(ServiceRequest request) { this.request = request; }
    }

    private Node front;
    private Node rear;
    private int size;

    /** Adds a non-null request at the rear of the queue. */
    public void enqueue(ServiceRequest request) {
        if (request == null) throw new IllegalArgumentException("request must not be null");
        Node node = new Node(request);
        if (rear == null) front = node;
        else rear.next = node;
        rear = node;
        size++;
    }

    /** Removes and returns the earliest enqueued request. */
    public ServiceRequest dequeue() {
        ensureNotEmpty("dequeue");
        ServiceRequest result = front.request;
        front = front.next;
        size--;
        if (front == null) rear = null;
        return result;
    }

    /** Returns the earliest enqueued request without removing it. */
    public ServiceRequest peek() {
        ensureNotEmpty("peek");
        return front.request;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    /** Prints requests from front to rear. */
    public void display() {
        if (isEmpty()) { System.out.println("Service request queue is empty."); return; }
        for (Node current = front; current != null; current = current.next) {
            System.out.println(current.request);
        }
    }

    private void ensureNotEmpty(String operation) {
        if (isEmpty()) throw new NoSuchElementException("Cannot " + operation + " an empty service request queue");
    }
}
