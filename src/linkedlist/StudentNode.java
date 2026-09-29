package linkedlist;

import model.Student;

/**
 * Node structure for the custom singly linked list of students.
 * Encapsulates the Student record and a pointer to the next node.
 * 
 * Part of CIT300 DSA Project - Student Record Management Component
 * Assigned Member: MS Sarfaras (23DA2-0727)
 */
public class StudentNode {
    private Student data;
    private StudentNode next;

    /**
     * Constructs a node with student data and a null next pointer.
     *
     * @param data The Student record to store in this node
     */
    public StudentNode(Student data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Constructs a node with student data and an explicit next node pointer.
     *
     * @param data The Student record to store in this node
     * @param next Pointer to the subsequent node
     */
    public StudentNode(Student data, StudentNode next) {
        this.data = data;
        this.next = next;
    }

    // --- Getters and Setters ---

    public Student getData() {
        return data;
    }

    public void setData(Student data) {
        this.data = data;
    }

    public StudentNode getNext() {
        return next;
    }

    public void setNext(StudentNode next) {
        this.next = next;
    }
}
