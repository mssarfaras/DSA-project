package linkedlist;

import model.Student;

/**
 * Custom Singly Linked List implementation specifically designed for managing Student records.
 * Built from scratch without using java.util.LinkedList.
 * 
 * Part of CIT300 DSA Project - Student Record Management Component
 * Assigned Member: MS Sarfaras (23DA2-0727)
 */
public class StudentLinkedList {
    private StudentNode head;
    private int size;

    /**
     * Constructs an empty Student Linked List.
     */
    public StudentLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds a student to the end of the linked list.
     * Prevents duplicate Student IDs.
     *
     * @param student The Student record to add
     * @return true if added successfully, false if duplicate ID or invalid student
     */
    public boolean addStudent(Student student) {
        if (student == null || student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            return false;
        }

        // Check for duplicate Student ID
        if (containsStudent(student.getStudentId())) {
            return false;
        }

        StudentNode newNode = new StudentNode(student);

        // If list is empty, make newNode the head
        if (head == null) {
            head = newNode;
        } else {
            // Traverse to the end of the list and append
            StudentNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }

        size++;
        return true;
    }

    /**
     * Searches for a student by Student ID.
     *
     * @param studentId The unique Student ID to search for
     * @return The Student record if found; null otherwise
     */
    public Student searchStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        String searchId = studentId.trim();
        StudentNode current = head;

        while (current != null) {
            if (current.getData() != null && searchId.equalsIgnoreCase(current.getData().getStudentId())) {
                return current.getData();
            }
            current = current.getNext();
        }

        return null; // Not found
    }

    /**
     * Checks if a student with the specified Student ID exists in the list.
     *
     * @param studentId The Student ID to verify
     * @return true if present, false otherwise
     */
    public boolean containsStudent(String studentId) {
        return searchStudent(studentId) != null;
    }

    /**
     * Updates an existing student record.
     *
     * @param studentId    The ID of the student to update
     * @param newName      New student name
     * @param newProgramme New programme
     * @param newMarks     New academic marks
     * @return true if found and updated, false if student not found
     */
    public boolean updateStudent(String studentId, String newName, String newProgramme, double newMarks) {
        Student target = searchStudent(studentId);
        if (target == null) {
            return false; // Student does not exist
        }

        if (newName != null && !newName.trim().isEmpty()) {
            target.setName(newName.trim());
        }
        if (newProgramme != null && !newProgramme.trim().isEmpty()) {
            target.setProgramme(newProgramme.trim());
        }
        target.setMarks(newMarks);
        return true;
    }

    /**
     * Updates an existing student using an updated Student object.
     *
     * @param updatedStudent Student object containing updated details
     * @return true if found and updated, false otherwise
     */
    public boolean updateStudent(Student updatedStudent) {
        if (updatedStudent == null || updatedStudent.getStudentId() == null) {
            return false;
        }
        return updateStudent(updatedStudent.getStudentId(), updatedStudent.getName(),
                updatedStudent.getProgramme(), updatedStudent.getMarks());
    }

    /**
     * Deletes a student from the linked list by Student ID.
     * Handles deletion at head, middle, and end.
     *
     * @param studentId The ID of the student to delete
     * @return true if found and deleted, false if student does not exist
     */
    public boolean deleteStudent(String studentId) {
        if (head == null || studentId == null || studentId.trim().isEmpty()) {
            return false;
        }

        String targetId = studentId.trim();

        // Case 1: Head node is the target
        if (targetId.equalsIgnoreCase(head.getData().getStudentId())) {
            head = head.getNext();
            size--;
            return true;
        }

        // Case 2: Target is in the middle or end
        StudentNode current = head;
        while (current.getNext() != null) {
            if (targetId.equalsIgnoreCase(current.getNext().getData().getStudentId())) {
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }

        return false; // Student not found
    }

    /**
     * Displays all student records in the console in a structured table.
     */
    public void displayStudents() {
        if (head == null) {
            System.out.println("  [INFO] No student records available in the linked list.");
            return;
        }

        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");
        System.out.println("| Student ID      | Student Name              | Programme                 | Marks  | Grd  |");
        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");

        StudentNode current = head;
        while (current != null) {
            if (current.getData() != null) {
                System.out.println(current.getData().toFormattedRow());
            }
            current = current.getNext();
        }

        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");
        System.out.println("  Total Students: " + size);
    }

    /**
     * Returns the number of students currently stored in the linked list.
     *
     * @return Current size of the linked list
     */
    public int getSize() {
        return size;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears all elements from the linked list.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Retrieves the student at a specific index (0-indexed).
     * Useful for iteration without exposing node internals.
     *
     * @param index 0-based index
     * @return Student at index, or null if index out of bounds
     */
    public Student getStudentAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        StudentNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    /**
     * Converts the linked list into an array of Student objects.
     * Allows other group members (Stack/Queue, BST/AVL/Hashing, Graph) to integrate
     * easily without needing custom iterators or modifying list internals.
     *
     * @return Array of Student objects
     */
    public Student[] toArray() {
        Student[] array = new Student[size];
        StudentNode current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.getData();
            current = current.getNext();
        }
        return array;
    }
}
