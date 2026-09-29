package validation;

import model.Student;

/**
 * Utility validator class for enforcing business rules and data integrity on student records.
 * Can be reused across the entire "University Student Record and Campus Route Management System".
 * 
 * Part of CIT300 DSA Project - Student Record Management Component
 * Assigned Member: MS Sarfaras (23DA2-0727)
 */
public class StudentValidator {

    public static final double MIN_MARKS = 0.0;
    public static final double MAX_MARKS = 100.0;

    // Private constructor to prevent instantiation (Utility class)
    private StudentValidator() {
    }

    /**
     * Validates that the Student ID is non-null and not blank.
     *
     * @param studentId The student identifier to validate
     * @throws ValidationException if studentId is null or blank
     */
    public static void validateStudentId(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Validation Error: Student ID cannot be empty or null.");
        }
    }

    /**
     * Validates that the student name is non-null and not blank.
     *
     * @param name The student name to validate
     * @throws ValidationException if name is null or blank
     */
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Validation Error: Student Name cannot be empty or null.");
        }
    }

    /**
     * Validates that the programme is non-null and not blank.
     *
     * @param programme The programme name to validate
     * @throws ValidationException if programme is null or blank
     */
    public static void validateProgramme(String programme) throws ValidationException {
        if (programme == null || programme.trim().isEmpty()) {
            throw new ValidationException("Validation Error: Programme cannot be empty or null.");
        }
    }

    /**
     * Validates that marks are within the valid range [0.0, 100.0].
     *
     * @param marks The marks to validate
     * @throws ValidationException if marks are less than 0 or greater than 100
     */
    public static void validateMarks(double marks) throws ValidationException {
        if (Double.isNaN(marks) || Double.isInfinite(marks)) {
            throw new ValidationException("Validation Error: Marks must be a valid finite number.");
        }
        if (marks < MIN_MARKS) {
            throw new ValidationException(String.format(
                    "Validation Error: Marks cannot be below %.1f. Provided: %.2f", MIN_MARKS, marks));
        }
        if (marks > MAX_MARKS) {
            throw new ValidationException(String.format(
                    "Validation Error: Marks cannot exceed %.1f. Provided: %.2f", MAX_MARKS, marks));
        }
    }

    /**
     * Validates all individual fields of a student.
     *
     * @param studentId Student ID
     * @param name      Student Name
     * @param programme Programme
     * @param marks     Marks
     * @throws ValidationException if any field is invalid
     */
    public static void validateAll(String studentId, String name, String programme, double marks)
            throws ValidationException {
        validateStudentId(studentId);
        validateName(name);
        validateProgramme(programme);
        validateMarks(marks);
    }

    /**
     * Validates an entire Student model object.
     *
     * @param student The Student object to validate
     * @throws ValidationException if student is null or contains invalid data
     */
    public static void validateStudent(Student student) throws ValidationException {
        if (student == null) {
            throw new ValidationException("Validation Error: Student record cannot be null.");
        }
        validateAll(student.getStudentId(), student.getName(), student.getProgramme(), student.getMarks());
    }

    // --- Boolean helper methods (Safe checks without throwing exceptions) ---

    public static boolean isValidStudentId(String studentId) {
        return studentId != null && !studentId.trim().isEmpty();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidProgramme(String programme) {
        return programme != null && !programme.trim().isEmpty();
    }

    public static boolean isValidMarks(double marks) {
        return !Double.isNaN(marks) && !Double.isInfinite(marks) && marks >= MIN_MARKS && marks <= MAX_MARKS;
    }
}
