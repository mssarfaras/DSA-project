package tree;

import java.util.ArrayList;
import java.util.List;
import model.Student;

/**
 * An unbalanced binary search tree of students, ordered by Student ID
 * (case-insensitive).
 */
public final class StudentBST {
    private StudentTreeNode root;
    private int size;

    /**
     * Inserts a student. IDs are unique ignoring case and surrounding spaces.
     *
     * @return true when inserted, or false when that ID already exists
     * @throws IllegalArgumentException when the student or its ID is invalid
     */
    public boolean insert(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("student must not be null");
        }
        String studentId = normalizeId(student.getStudentId());
        if (studentId == null) {
            throw new IllegalArgumentException("student ID must not be null or blank");
        }

        if (root == null) {
            root = new StudentTreeNode(studentId, student);
            size++;
            return true;
        }

        StudentTreeNode current = root;
        while (true) {
            int comparison = studentId.compareToIgnoreCase(current.studentId);
            if (comparison == 0) {
                return false;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new StudentTreeNode(studentId, student);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StudentTreeNode(studentId, student);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    /** Returns the student with this ID, or null when it is absent or invalid. */
    public Student search(String studentId) {
        String key = normalizeId(studentId);
        if (key == null) {
            return null;
        }

        StudentTreeNode current = root;
        while (current != null) {
            int comparison = key.compareToIgnoreCase(current.studentId);
            if (comparison == 0) {
                return current.student;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    /**
     * Deletes a student by ID, including the two-child successor case.
     *
     * @return true when deleted, or false when the ID is missing or invalid
     */
    public boolean delete(String studentId) {
        String key = normalizeId(studentId);
        if (key == null || search(key) == null) {
            return false;
        }
        root = deleteNode(root, key);
        size--;
        return true;
    }

    public boolean contains(String studentId) {
        return search(studentId) != null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
     * Returns a new array of student references in ascending Student ID order.
     * The array is independent; the Student objects are the shared model objects.
     */
    public Student[] toArrayInOrder() {
        List<Student> students = new ArrayList<>(size);
        appendInOrder(root, students);
        return students.toArray(new Student[0]);
    }

    /** Prints the students in ascending, case-insensitive Student ID order. */
    public void displayInOrder() {
        if (isEmpty()) {
            System.out.println("Student BST is empty.");
            return;
        }
        for (Student student : toArrayInOrder()) {
            System.out.println(student.toFormattedRow());
        }
    }

    private static StudentTreeNode deleteNode(StudentTreeNode node, String key) {
        int comparison = key.compareToIgnoreCase(node.studentId);
        if (comparison < 0) {
            node.left = deleteNode(node.left, key);
        } else if (comparison > 0) {
            node.right = deleteNode(node.right, key);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            StudentTreeNode successor = minimum(node.right);
            StudentTreeNode replacement = new StudentTreeNode(successor.studentId, successor.student);
            replacement.left = node.left;
            replacement.right = deleteMinimum(node.right, successor.studentId);
            return replacement;
        }
        return node;
    }

    private static StudentTreeNode deleteMinimum(StudentTreeNode node, String key) {
        if (node == null) {
            return null;
        }
        if (key.compareToIgnoreCase(node.studentId) < 0) {
            node.left = deleteMinimum(node.left, key);
        } else if (key.compareToIgnoreCase(node.studentId) > 0) {
            node.right = deleteMinimum(node.right, key);
        } else {
            return node.right;
        }
        return node;
    }

    private static StudentTreeNode minimum(StudentTreeNode node) {
        StudentTreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private static void appendInOrder(StudentTreeNode node, List<Student> students) {
        if (node == null) {
            return;
        }
        appendInOrder(node.left, students);
        students.add(node.student);
        appendInOrder(node.right, students);
    }

    private static String normalizeId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        return studentId.trim();
    }
}
