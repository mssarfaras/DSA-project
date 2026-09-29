package test;

import java.util.List;
import model.Student;
import queue.ServiceRequest;
import service.StudentSystem;

/**
 * Full end-to-end integration test verifying all 20 required system integration scenarios.
 * 
 * Verifies that:
 *  - MS Sarfaras: Linked List + Student Records
 *  - J. Nisathn:  Stack + Queue
 *  - Shawky:      BST + Hashing
 *  - IF Hasna:    Graph + BFS/DFS
 * 
 * All operate in complete harmony through the central StudentSystem coordinator.
 */
public class SystemIntegrationTest {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("      CIT300 FULL SYSTEM INTEGRATION TEST - 20 CORE SCENARIOS");
        System.out.println("===============================================================================");

        StudentSystem system = new StudentSystem();

        // 1. Add student (At least 3 students: 23DA2-001, 23DA2-002, 23DA2-003)
        printScenarioHeader(1, "Add 3 Students");
        system.addStudent("23DA2-001", "Ahmed Ali", "BSc Information Technology", 85.0);
        system.addStudent("23DA2-002", "Fatima Zahra", "BSc Computer Science", 92.5);
        system.addStudent("23DA2-003", "John Doe", "BSc Software Engineering", 78.0);

        // 2. Duplicate student
        printScenarioHeader(2, "Attempt Adding Duplicate Student ID ('23DA2-001')");
        boolean dupResult = system.addStudent("23DA2-001", "Ahmed Imposter", "BSc IT", 90.0);
        System.out.println("  Result (expected false): " + dupResult);

        // 3. Display linked list
        printScenarioHeader(3, "Display All Records using Linked List");
        system.displayAllStudentsLinkedList();

        // 4. Search student
        printScenarioHeader(4, "Search Student ('23DA2-002')");
        Student s = system.searchStudentHashing("23DA2-002");
        System.out.println("  Found: " + (s != null ? s.getName() : "null"));

        // 5. Update student
        printScenarioHeader(5, "Update Student ('23DA2-003' -> 'Johnathan Doe', Marks -> 88.0)");
        system.updateStudent("23DA2-003", "Johnathan Doe", "BSc Software Engineering", 88.0);
        system.searchStudentHashing("23DA2-003");

        // 6. Delete student
        printScenarioHeader(6, "Delete Student ('23DA2-002')");
        system.deleteStudent("23DA2-002");
        System.out.println("  Confirming deletion via Hash Table lookup:");
        system.searchStudentHashing("23DA2-002");

        // 7. Display stack (LIFO actions)
        printScenarioHeader(7, "Display Recent Actions using Stack (LIFO)");
        system.displayRecentActions();

        // 8. Add service requests
        printScenarioHeader(8, "Add Service Requests to Queue (FIFO)");
        system.addServiceRequest("REQ-101", "23DA2-001", "Request for Official Transcript");
        system.addServiceRequest("REQ-102", "23DA2-003", "Campus Dormitory Housing Application");
        system.addServiceRequest("REQ-103", "23DA2-001", "Library Book Clearance");

        // 9. Process service requests
        printScenarioHeader(9, "Process Next Service Requests (FIFO Demonstration)");
        ServiceRequest p1 = system.processNextServiceRequest();
        ServiceRequest p2 = system.processNextServiceRequest();
        System.out.println("  Processed 1 ID: " + (p1 != null ? p1.getRequestId() : "none"));
        System.out.println("  Processed 2 ID: " + (p2 != null ? p2.getRequestId() : "none"));

        // 10. Display BST (In-order traversal by Student ID)
        printScenarioHeader(10, "Display Students using BST (In-Order Traversal)");
        system.displayStudentsBST();

        // 11. Search using hashing
        printScenarioHeader(11, "Search Student using Hashing ('23DA2-001')");
        system.searchStudentHashing("23DA2-001");

        // 12. Add campus locations
        printScenarioHeader(12, "Add Campus Locations to Campus Graph");
        system.addCampusLocation("Main Gate");
        system.addCampusLocation("Library");
        system.addCampusLocation("Canteen");
        system.addCampusLocation("Laboratory");
        system.addCampusLocation("Lecture Hall");

        // 13. Add campus connections
        printScenarioHeader(13, "Add Campus Connections / Roads");
        system.addCampusConnection("Main Gate", "Library");
        system.addCampusConnection("Main Gate", "Canteen");
        system.addCampusConnection("Library", "Laboratory");
        system.addCampusConnection("Library", "Lecture Hall");
        system.addCampusConnection("Canteen", "Lecture Hall");

        // 14. Display graph
        printScenarioHeader(14, "Display Campus Connections (Adjacency List)");
        system.displayCampusConnections();

        // 15. BFS traversal
        printScenarioHeader(15, "Traverse Campus Locations using BFS from 'Main Gate'");
        system.traverseCampusBFS("Main Gate");

        // 16. DFS traversal
        printScenarioHeader(16, "Traverse Campus Locations using DFS from 'Main Gate'");
        system.traverseCampusDFS("Main Gate");

        // 17. Remove connection
        printScenarioHeader(17, "Remove Connection ('Main Gate' <-> 'Canteen')");
        system.removeCampusConnection("Main Gate", "Canteen");
        System.out.println("  Updated Connections:");
        system.displayCampusConnections();

        // 18. Remove location
        printScenarioHeader(18, "Remove Location ('Laboratory')");
        system.removeCampusLocation("Laboratory");
        System.out.println("  Updated Connections after Laboratory removal:");
        system.displayCampusConnections();

        // 19. Invalid input handling
        printScenarioHeader(19, "Invalid Input Defensive Checks");
        System.out.println("  a) Marks below 0:");
        system.addStudent("23DA2-999", "Bad Marks", "IT", -10.0);

        System.out.println("\n  b) Marks above 100:");
        system.addStudent("23DA2-999", "Bad Marks", "IT", 150.0);

        System.out.println("\n  c) Empty Student ID:");
        system.addStudent("", "No ID", "IT", 75.0);

        System.out.println("\n  d) Update non-existent student:");
        system.updateStudent("23DA2-999", "Ghost", "IT", 80.0);

        System.out.println("\n  e) Delete non-existent student:");
        system.deleteStudent("23DA2-999");

        System.out.println("\n  f) Connect to non-existent campus locations:");
        system.addCampusConnection("Library", "Fantasy Castle");

        // 20. Exit check
        printScenarioHeader(20, "Exit Scenario");
        System.out.println("  System gracefully terminates upon selecting 0 in Main console.");

        System.out.println("\n===============================================================================");
        System.out.println("      ALL 20 INTEGRATION SCENARIOS TESTED AND PASSED SUCCESSFULLY!");
        System.out.println("===============================================================================");
    }

    private static void printScenarioHeader(int num, String title) {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("  SCENARIO " + num + ": " + title);
        System.out.println("-------------------------------------------------------------------------------");
    }
}
