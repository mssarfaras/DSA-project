package graph;

import java.util.NoSuchElementException;

/**
 * A custom singly-linked FIFO (First-In, First-Out) Queue implemented manually
 * from scratch specifically for the Breadth-First Search (BFS) graph traversal.
 * 
 * Satisfies the assignment requirement to implement graph traversals manually
 * using queue concepts without relying on external libraries.
 * 
 * Course: CIT300 Data Structures and Algorithms
 * Project: University Student Record and Campus Route Management System
 * Assigned Member: IF Hasna
 * Student ID: 23DA2-1154
 * Component: Campus Graph Component
 * 
 * @param <T> The type of elements held in this queue.
 */
public class GraphQueue<T> {

    /**
     * Node representation for the custom queue.
     */
    private static class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    /**
     * Initializes an empty GraphQueue.
     */
    public GraphQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Inserts an element at the rear of the queue.
     * Time Complexity: O(1)
     *
     * @param item The element to enqueue.
     * @throws IllegalArgumentException if item is null.
     */
    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue null item into GraphQueue.");
        }
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue.
     * Time Complexity: O(1)
     *
     * @return The front element.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("GraphQueue underflow: Queue is empty.");
        }
        T item = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return item;
    }

    /**
     * Retrieves, but does not remove, the element at the front of the queue.
     * Time Complexity: O(1)
     *
     * @return The front element.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("GraphQueue is empty.");
        }
        return front.data;
    }

    /**
     * Checks whether the queue contains no elements.
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the queue.
     * Time Complexity: O(1)
     *
     * @return Element count.
     */
    public int size() {
        return size;
    }

    /**
     * Resets and empties the queue.
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
}
