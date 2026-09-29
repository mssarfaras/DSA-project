package tree;

import model.Student;

/** A node in the student ID binary search tree. */
public final class StudentTreeNode {
    final String studentId;
    final Student student;
    StudentTreeNode left;
    StudentTreeNode right;

    StudentTreeNode(String studentId, Student student) {
        this.studentId = studentId;
        this.student = student;
    }

    public String getStudentId() {
        return studentId;
    }

    public Student getStudent() {
        return student;
    }

    public StudentTreeNode getLeft() {
        return left;
    }

    public StudentTreeNode getRight() {
        return right;
    }
}
