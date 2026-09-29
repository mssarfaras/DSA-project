package student;

import linkedlist.StudentLinkedList;
import model.Student;
import validation.StudentValidator;
import validation.ValidationException;

/**
 * High-level manager and service class for handling Student Record Management operations.
 * Coordinates between the custom StudentLinkedList data structure and StudentValidator,
 * enforcing business constraints, handling errors, and presenting clear feedback.
 * 
 * Part of CIT300 DSA Project - Student Record Management Component
 * Assigned Member: MS Sarfaras (23DA2-0727)
 */
public class StudentManager {

    private final StudentLinkedList studentList;

    /**
     * Default constructor initializing an empty linked list of students.
     */
    public StudentManager() {
        this.studentList = new StudentLinkedList();
    }

    /**
     * Constructor allowing injection of a pre-existing StudentLinkedList.
     *
     * @param studentList Existing StudentLinkedList instance
     */
    public StudentManager(StudentLinkedList studentList) {
        this.studentList = (studentList != null) ? studentList : new StudentLinkedList();
    }

    /**
     * Adds a new student record to the system with full validation and duplicate checking.
     *
     * @param studentId Unique Student ID
     * @param name      Student Full Name
     * @param programme Enrolled Programme
     * @param marks     Academic Marks (0.0 - 100.0)
     * @return true if student added successfully, false otherwise
     */
    public boolean addStudent(String studentId, String name, String programme, double marks) {
        try {
            // Step 1: Validate input parameters
            StudentValidator.validateAll(studentId, name, programme, marks);

            String cleanId = studentId.trim();

            // Step 2: Check for duplicate Student ID
            if (studentList.containsStudent(cleanId)) {
                System.out.println("  [ERROR] Cannot add student: Duplicate Student ID '" + cleanId + "' already exists.");
                return false;
            }

            // Step 3: Instantiate and append to custom linked list
            Student newStudent = new Student(cleanId, name.trim(), programme.trim(), marks);
            boolean added = studentList.addStudent(newStudent);

            if (added) {
                System.out.println("  [SUCCESS] Student record added: " + newStudent.getName() + " (" + cleanId + ")");
                return true;
            } else {
                System.out.println("  [ERROR] Failed to add student to linked list.");
                return false;
            }

        } catch (ValidationException ve) {
            System.out.println("  [ERROR] Cannot add student: " + ve.getMessage());
            return false;
        }
    }

    /**
     * Adds a student record using a Student model instance.
     *
     * @param student Student object to add
     * @return true if added successfully, false otherwise
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            System.out.println("  [ERROR] Cannot add student: Student object is null.");
            return false;
        }
        return addStudent(student.getStudentId(), student.getName(), student.getProgramme(), student.getMarks());
    }

    /**
     * Searches for a student by Student ID.
     *
     * @param studentId The unique Student ID to find
     * @return The Student record if found; null otherwise
     */
    public Student searchStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("  [ERROR] Search failed: Student ID cannot be empty or null.");
            return null;
        }

        String searchId = studentId.trim();
        Student student = studentList.searchStudent(searchId);

        if (student != null) {
            System.out.println("  [FOUND] Student record found: " + student.getName() + " [ID: " + student.getStudentId() + "]");
            return student;
        } else {
            System.out.println("  [NOT FOUND] No student record exists with ID: '" + searchId + "'");
            return null;
        }
    }

    /**
     * Checks if a student exists in the system without printing search logs.
     * Useful for clean programmatic checks by other components.
     *
     * @param studentId The Student ID to check
     * @return true if found, false otherwise
     */
    public boolean containsStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return studentList.containsStudent(studentId.trim());
    }

    /**
     * Updates an existing student record with new information.
     * Handles missing-record and invalid data constraints.
     *
     * @param studentId    The ID of the student to update
     * @param newName      New student name
     * @param newProgramme New programme
     * @param newMarks     New marks (0.0 - 100.0)
     * @return true if updated successfully, false if student not found or data invalid
     */
    public boolean updateStudent(String studentId, String newName, String newProgramme, double newMarks) {
        try {
            // Step 1: Validate updated fields
            StudentValidator.validateAll(studentId, newName, newProgramme, newMarks);

            String targetId = studentId.trim();

            // Step 2: Check existence of target student
            if (!studentList.containsStudent(targetId)) {
                System.out.println("  [ERROR] Update failed: Student ID '" + targetId + "' does not exist.");
                return false;
            }

            // Step 3: Perform update on linked list
            boolean updated = studentList.updateStudent(targetId, newName.trim(), newProgramme.trim(), newMarks);
            if (updated) {
                System.out.println("  [SUCCESS] Student record updated successfully: " + targetId);
                return true;
            } else {
                System.out.println("  [ERROR] Failed to update student record for ID: " + targetId);
                return false;
            }

        } catch (ValidationException ve) {
            System.out.println("  [ERROR] Cannot update student: " + ve.getMessage());
            return false;
        }
    }

    /**
     * Deletes a student record by Student ID.
     * Handles missing-record validation.
     *
     * @param studentId The ID of the student to delete
     * @return true if deleted successfully, false if student not found
     */
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("  [ERROR] Delete failed: Student ID cannot be empty or null.");
            return false;
        }

        String targetId = studentId.trim();

        // Check if student exists
        if (!studentList.containsStudent(targetId)) {
            System.out.println("  [ERROR] Delete failed: Student record with ID '" + targetId + "' does not exist.");
            return false;
        }

        boolean deleted = studentList.deleteStudent(targetId);
        if (deleted) {
            System.out.println("  [SUCCESS] Student record with ID '" + targetId + "' was deleted successfully.");
            return true;
        } else {
            System.out.println("  [ERROR] Failed to delete student with ID: " + targetId);
            return false;
        }
    }

    /**
     * Displays all student records in the console in a structured table.
     */
    public void displayAllStudents() {
        System.out.println("\n======================================= STUDENT RECORD LIST =======================================");
        studentList.displayStudents();
        System.out.println("===================================================================================================\n");
    }

    /**
     * Returns the total number of students currently stored.
     *
     * @return Current student count
     */
    public int getTotalStudents() {
        return studentList.getSize();
    }

    /**
     * Exposes the underlying custom StudentLinkedList for integration with other components.
     *
     * @return StudentLinkedList instance
     */
    public StudentLinkedList getStudentLinkedList() {
        return studentList;
    }

    /**
     * Returns an array of all Student records for easy traversal and data transfer
     * to other structures (BST, AVL, Hash Table, Stack, Queue, Graph).
     *
     * @return Array of Student objects
     */
    public Student[] getAllStudents() {
        return studentList.toArray();
    }
}
