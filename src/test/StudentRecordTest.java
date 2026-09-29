package test;

import model.Student;
import student.StudentManager;

/**
 * Test and demonstration driver for the Student Record Management component.
 * 
 * Course: CIT300 Data Structures and Algorithms
 * Project: University Student Record and Campus Route Management System
 * Assigned Member: MS Sarfaras
 * Student ID: 23DA2-0727
 * Component: Linked List and Student Record Management
 * 
 * Demonstrates:
 *  1. Add 3 students
 *  2. Display students
 *  3. Search for an existing student
 *  4. Search for a missing student
 *  5. Update a student
 *  6. Delete a student
 *  7. Attempt duplicate Student ID
 *  8. Attempt invalid marks (and boundary/validation checks)
 *  9. Display the final list
 */
public class StudentRecordTest {

    public static void main(String[] args) {
        printHeader();

        StudentManager manager = new StudentManager();

        // ---------------------------------------------------------------------
        // TEST CASE 1: Add 3 students
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 1: Add 3 Students to the Linked List");
        manager.addStudent("23DA2-0727", "MS Sarfaras", "Computer Science", 88.50);
        manager.addStudent("23DA2-0101", "Alice Smith", "Software Engineering", 76.00);
        manager.addStudent("23DA2-0202", "Bob Johnson", "Data Science", 92.00);

        // ---------------------------------------------------------------------
        // TEST CASE 2: Display students
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 2: Display All Students");
        manager.displayAllStudents();

        // ---------------------------------------------------------------------
        // TEST CASE 3: Search for an existing student
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 3: Search for an Existing Student ('23DA2-0727')");
        Student foundStudent = manager.searchStudent("23DA2-0727");
        if (foundStudent != null) {
            System.out.println("  Verified Student Details: " + foundStudent);
        }

        // ---------------------------------------------------------------------
        // TEST CASE 4: Search for a missing student
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 4: Search for a Missing Student ('23DA2-9999')");
        Student missingStudent = manager.searchStudent("23DA2-9999");
        if (missingStudent == null) {
            System.out.println("  [CONFIRMED] Returned null for non-existing student.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 5: Update a student
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 5: Update an Existing Student ('23DA2-0101')");
        System.out.println("  Before Update:");
        Student beforeUpdate = manager.searchStudent("23DA2-0101");
        System.out.println("    " + beforeUpdate);

        System.out.println("\n  Applying update: Name -> 'Alice Brown', Programme -> 'Cyber Security', Marks -> 84.50");
        manager.updateStudent("23DA2-0101", "Alice Brown", "Cyber Security", 84.50);

        System.out.println("\n  After Update:");
        Student afterUpdate = manager.searchStudent("23DA2-0101");
        System.out.println("    " + afterUpdate);

        // Also test updating a non-existing student
        System.out.println("\n  Attempting to update a non-existing student ('23DA2-8888'):");
        manager.updateStudent("23DA2-8888", "Ghost User", "Unknown", 50.0);

        // ---------------------------------------------------------------------
        // TEST CASE 6: Delete a student
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 6: Delete a Student ('23DA2-0202' - Bob Johnson)");
        System.out.println("  Current total students before deletion: " + manager.getTotalStudents());
        manager.deleteStudent("23DA2-0202");
        System.out.println("  Current total students after deletion: " + manager.getTotalStudents());

        // Also test deleting a non-existing student
        System.out.println("\n  Attempting to delete non-existing student ('23DA2-9999'):");
        manager.deleteStudent("23DA2-9999");

        // ---------------------------------------------------------------------
        // TEST CASE 7: Attempt duplicate Student ID
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 7: Attempt Duplicate Student ID ('23DA2-0727')");
        System.out.println("  Attempting to add another student with already existing ID '23DA2-0727':");
        boolean duplicateAdded = manager.addStudent("23DA2-0727", "Imposter Name", "Information Systems", 65.00);
        System.out.println("  Duplicate addition result: " + (duplicateAdded ? "FAILED (duplicate was added)" : "PASSED (duplicate rejected)"));

        // ---------------------------------------------------------------------
        // TEST CASE 8: Attempt invalid marks and field validation
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 8: Attempt Invalid Marks & Field Validation");
        System.out.println("  Attempting marks below 0 (-15.0):");
        manager.addStudent("23DA2-0303", "Charlie", "Artificial Intelligence", -15.0);

        System.out.println("\n  Attempting marks above 100 (125.0):");
        manager.addStudent("23DA2-0303", "Charlie", "Artificial Intelligence", 125.0);

        System.out.println("\n  Attempting empty Student ID (\"\"):");
        manager.addStudent("", "Diana", "Computer Science", 78.0);

        System.out.println("\n  Attempting empty Student Name (null):");
        manager.addStudent("23DA2-0404", null, "Software Engineering", 82.0);

        System.out.println("\n  Attempting empty Programme (\"\"):");
        manager.addStudent("23DA2-0505", "Evan", "", 90.0);

        // ---------------------------------------------------------------------
        // TEST CASE 9: Display the final list
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 9: Display the Final List");
        manager.displayAllStudents();

        // ---------------------------------------------------------------------
        // Integration verification check
        // ---------------------------------------------------------------------
        printSectionHeader("INTEGRATION VERIFICATION CHECK FOR TEAM MEMBERS");
        System.out.println("  Checking integration methods:");
        System.out.println("  - manager.containsStudent(\"23DA2-0727\"): " + manager.containsStudent("23DA2-0727"));
        System.out.println("  - manager.containsStudent(\"23DA2-0202\"): " + manager.containsStudent("23DA2-0202"));
        System.out.println("  - Total students in list: " + manager.getTotalStudents());
        System.out.println("  - Direct array export size: " + manager.getAllStudents().length);
        System.out.println("\n  ALL TESTS COMPLETED SUCCESSFULLY.");
    }

    private static void printHeader() {
        System.out.println("===============================================================================");
        System.out.println("   UNIVERSITY STUDENT RECORD AND CAMPUS ROUTE MANAGEMENT SYSTEM");
        System.out.println("   CIT300 Data Structures and Algorithms Project");
        System.out.println("   Component: Singly Linked List & Student Record Management");
        System.out.println("   Assigned Member: MS Sarfaras | Student ID: 23DA2-0727");
        System.out.println("===============================================================================");
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("  " + title);
        System.out.println("-------------------------------------------------------------------------------");
    }
}
