package model;

import java.util.Objects;

/**
 * Model class representing an individual Student record.
 * 
 * Part of the "University Student Record and Campus Route Management System"
 * (CIT300 Data Structures and Algorithms Project).
 * 
 * Assigned Member: MS Sarfaras (Student ID: 23DA2-0727)
 * Responsibility: Linked List and Student Record Management Component
 */
public class Student implements Comparable<Student> {
    private String studentId;
    private String name;
    private String programme;
    private double marks;

    /**
     * Default constructor.
     */
    public Student() {
    }

    /**
     * Parameterized constructor to initialize a student record.
     *
     * @param studentId Unique student identifier
     * @param name      Full name of the student
     * @param programme Academic programme enrolled in
     * @param marks     Academic marks (0.0 to 100.0)
     */
    public Student(String studentId, String name, String programme, double marks) {
        this.studentId = studentId != null ? studentId.trim() : null;
        this.name = name != null ? name.trim() : null;
        this.programme = programme != null ? programme.trim() : null;
        this.marks = marks;
    }

    /**
     * Copy constructor for safely cloning student records.
     *
     * @param other The student object to copy from
     */
    public Student(Student other) {
        if (other != null) {
            this.studentId = other.studentId;
            this.name = other.name;
            this.programme = other.programme;
            this.marks = other.marks;
        }
    }

    // --- Getters and Setters ---

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId != null ? studentId.trim() : null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme != null ? programme.trim() : null;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    /**
     * Calculates the letter grade based on marks.
     *
     * @return Letter grade as a String
     */
    public String getGrade() {
        if (marks >= 85.0) return "A+";
        if (marks >= 75.0) return "A";
        if (marks >= 70.0) return "A-";
        if (marks >= 65.0) return "B+";
        if (marks >= 60.0) return "B";
        if (marks >= 55.0) return "B-";
        if (marks >= 50.0) return "C+";
        if (marks >= 45.0) return "C";
        if (marks >= 40.0) return "D";
        return "F";
    }

    /**
     * Formats student details into a clean tabular row.
     *
     * @return Formatted string representation
     */
    public String toFormattedRow() {
        return String.format("| %-15s | %-25s | %-25s | %6.2f | %-4s |",
                studentId, name, programme, marks, getGrade());
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", programme='" + programme + '\'' +
                ", marks=" + String.format("%.2f", marks) +
                ", grade='" + getGrade() + '\'' +
                '}';
    }

    /**
     * Equality is determined strictly by studentId, as it is the unique identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    /**
     * Natural ordering based on studentId (lexicographical).
     * Enables integration with BST / AVL tree components.
     */
    @Override
    public int compareTo(Student o) {
        if (o == null || o.studentId == null) {
            return (this.studentId == null) ? 0 : 1;
        }
        if (this.studentId == null) {
            return -1;
        }
        return this.studentId.compareToIgnoreCase(o.studentId);
    }
}
