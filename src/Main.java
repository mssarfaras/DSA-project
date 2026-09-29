import java.util.Scanner;
import model.Student;
import service.StudentSystem;

/**
 * Main application entry point for the integrated
 * "University Student Record and Campus Route Management System".
 * 
 * CIT300 Data Structures and Algorithms Group Assignment
 * 
 * Integrated Team Contributions:
 *  - MS Sarfaras (23DA2-0727) : Linked List + Student Record Management
 *  - J. Nisathn  (23DA2-0684) : Stack + Queue (Actions & Service Requests)
 *  - Shawky      (23DA2-0588) : BST Indexing + Hash Table
 *  - IF Hasna    (23DA2-1154) : Campus Graph + BFS / DFS Traversals
 */
public class Main {

    public static void main(String[] args) {
        StudentSystem system = new StudentSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printWelcomeBanner();

        while (running) {
            printMainMenu();
            int choice = readMenuChoice(scanner);

            System.out.println();
            switch (choice) {
                case 1 -> handleAddStudent(system, scanner);
                case 2 -> handleUpdateStudent(system, scanner);
                case 3 -> handleDeleteStudent(system, scanner);
                case 4 -> handleDisplayLinkedList(system);
                case 5 -> handleAddServiceRequest(system, scanner);
                case 6 -> handleProcessServiceRequest(system);
                case 7 -> handleDisplayRecentActions(system);
                case 8 -> handleDisplayBST(system);
                case 9 -> handleSearchHashing(system, scanner);
                case 10 -> handleAddLocation(system, scanner);
                case 11 -> handleRemoveLocation(system, scanner);
                case 12 -> handleAddConnection(system, scanner);
                case 13 -> handleRemoveConnection(system, scanner);
                case 14 -> handleDisplayCampusConnections(system);
                case 15 -> handleTraverseBFS(system, scanner);
                case 16 -> handleTraverseDFS(system, scanner);
                case 0 -> {
                    System.out.println("===============================================================================");
                    System.out.println("  Exiting system. Thank you for using the University Management System!");
                    System.out.println("===============================================================================");
                    running = false;
                }
                default -> System.out.println("  [ERROR] Invalid choice. Please enter a number from 0 to 16.");
            }
            System.out.println();
        }

        scanner.close();
    }

    // =========================================================================
    // MENU DISPLAY
    // =========================================================================

    private static void printWelcomeBanner() {
        System.out.println("===============================================================================");
        System.out.println("     UNIVERSITY STUDENT RECORD AND CAMPUS ROUTE MANAGEMENT SYSTEM");
        System.out.println("           CIT300 Data Structures and Algorithms Project");
        System.out.println("===============================================================================");
    }

    private static void printMainMenu() {
        System.out.println("========================================");
        System.out.println("   UNIVERSITY STUDENT RECORD SYSTEM");
        System.out.println("========================================");
        System.out.println(" 1. Add Student Record");
        System.out.println(" 2. Update Student Record");
        System.out.println(" 3. Delete Student Record");
        System.out.println(" 4. Display All Records using Linked List");
        System.out.println();
        System.out.println(" 5. Add Service Request");
        System.out.println(" 6. Process Next Service Request");
        System.out.println(" 7. Display Recent Actions using Stack");
        System.out.println();
        System.out.println(" 8. Display Students using BST/AVL");
        System.out.println(" 9. Search Student using Hashing");
        System.out.println();
        System.out.println("10. Add Campus Location");
        System.out.println("11. Remove Campus Location");
        System.out.println("12. Add Campus Connection/Road");
        System.out.println("13. Remove Campus Connection/Road");
        System.out.println("14. Display Campus Connections");
        System.out.println("15. Traverse Campus Locations using BFS");
        System.out.println("16. Traverse Campus Locations using DFS");
        System.out.println();
        System.out.println(" 0. Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    // =========================================================================
    // HANDLERS FOR STUDENT RECORD OPERATIONS
    // =========================================================================

    private static void handleAddStudent(StudentSystem system, Scanner scanner) {
        System.out.println("--- ADD STUDENT RECORD ---");
        String studentId = readLine(scanner, "Enter Student ID: ");
        if (studentId.isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty or null.");
            return;
        }

        String name = readLine(scanner, "Enter Student Name: ");
        if (name.isEmpty()) {
            System.out.println("  [ERROR] Student Name cannot be empty or null.");
            return;
        }

        String programme = readLine(scanner, "Enter Programme: ");
        if (programme.isEmpty()) {
            System.out.println("  [ERROR] Programme cannot be empty or null.");
            return;
        }

        Double marks = readDouble(scanner, "Enter Marks (0 - 100): ");
        if (marks == null) {
            return;
        }

        system.addStudent(studentId, name, programme, marks);
    }

    private static void handleUpdateStudent(StudentSystem system, Scanner scanner) {
        System.out.println("--- UPDATE STUDENT RECORD ---");
        String studentId = readLine(scanner, "Enter Student ID to update: ");
        if (studentId.isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty or null.");
            return;
        }

        String name = readLine(scanner, "Enter New Student Name: ");
        if (name.isEmpty()) {
            System.out.println("  [ERROR] Student Name cannot be empty or null.");
            return;
        }

        String programme = readLine(scanner, "Enter New Programme: ");
        if (programme.isEmpty()) {
            System.out.println("  [ERROR] Programme cannot be empty or null.");
            return;
        }

        Double marks = readDouble(scanner, "Enter New Marks (0 - 100): ");
        if (marks == null) {
            return;
        }

        system.updateStudent(studentId, name, programme, marks);
    }

    private static void handleDeleteStudent(StudentSystem system, Scanner scanner) {
        System.out.println("--- DELETE STUDENT RECORD ---");
        String studentId = readLine(scanner, "Enter Student ID to delete: ");
        if (studentId.isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty or null.");
            return;
        }

        system.deleteStudent(studentId);
    }

    private static void handleDisplayLinkedList(StudentSystem system) {
        System.out.println("--- DISPLAY ALL RECORDS (USING LINKED LIST) ---");
        system.displayAllStudentsLinkedList();
    }

    // =========================================================================
    // HANDLERS FOR QUEUE & STACK OPERATIONS
    // =========================================================================

    private static void handleAddServiceRequest(StudentSystem system, Scanner scanner) {
        System.out.println("--- ADD SERVICE REQUEST ---");
        String reqId = readLine(scanner, "Enter Request ID: ");
        if (reqId.isEmpty()) {
            System.out.println("  [ERROR] Request ID cannot be empty.");
            return;
        }

        String studentId = readLine(scanner, "Enter Student ID: ");
        if (studentId.isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty.");
            return;
        }

        String desc = readLine(scanner, "Enter Request Description: ");
        if (desc.isEmpty()) {
            System.out.println("  [ERROR] Request description cannot be empty.");
            return;
        }

        system.addServiceRequest(reqId, studentId, desc);
    }

    private static void handleProcessServiceRequest(StudentSystem system) {
        System.out.println("--- PROCESS NEXT SERVICE REQUEST (FIFO) ---");
        system.processNextServiceRequest();
    }

    private static void handleDisplayRecentActions(StudentSystem system) {
        System.out.println("--- RECENT ACTIONS (USING STACK - LIFO) ---");
        system.displayRecentActions();
    }

    // =========================================================================
    // HANDLERS FOR TREE & HASHING OPERATIONS
    // =========================================================================

    private static void handleDisplayBST(StudentSystem system) {
        System.out.println("--- DISPLAY STUDENTS (USING BST IN-ORDER TRAVERSAL) ---");
        system.displayStudentsBST();
    }

    private static void handleSearchHashing(StudentSystem system, Scanner scanner) {
        System.out.println("--- SEARCH STUDENT (USING HASHING) ---");
        String studentId = readLine(scanner, "Enter Student ID: ");
        if (studentId.isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty.");
            return;
        }

        system.searchStudentHashing(studentId);
    }

    // =========================================================================
    // HANDLERS FOR CAMPUS GRAPH OPERATIONS
    // =========================================================================

    private static void handleAddLocation(StudentSystem system, Scanner scanner) {
        System.out.println("--- ADD CAMPUS LOCATION ---");
        String location = readLine(scanner, "Enter Campus Location Name: ");
        if (location.isEmpty()) {
            System.out.println("  [ERROR] Location name cannot be empty.");
            return;
        }
        system.addCampusLocation(location);
    }

    private static void handleRemoveLocation(StudentSystem system, Scanner scanner) {
        System.out.println("--- REMOVE CAMPUS LOCATION ---");
        String location = readLine(scanner, "Enter Campus Location Name to Remove: ");
        if (location.isEmpty()) {
            System.out.println("  [ERROR] Location name cannot be empty.");
            return;
        }
        system.removeCampusLocation(location);
    }

    private static void handleAddConnection(StudentSystem system, Scanner scanner) {
        System.out.println("--- ADD CAMPUS CONNECTION / ROAD ---");
        String loc1 = readLine(scanner, "Enter First Location: ");
        if (loc1.isEmpty()) {
            System.out.println("  [ERROR] First location cannot be empty.");
            return;
        }

        String loc2 = readLine(scanner, "Enter Second Location: ");
        if (loc2.isEmpty()) {
            System.out.println("  [ERROR] Second location cannot be empty.");
            return;
        }

        system.addCampusConnection(loc1, loc2);
    }

    private static void handleRemoveConnection(StudentSystem system, Scanner scanner) {
        System.out.println("--- REMOVE CAMPUS CONNECTION / ROAD ---");
        String loc1 = readLine(scanner, "Enter First Location: ");
        if (loc1.isEmpty()) {
            System.out.println("  [ERROR] First location cannot be empty.");
            return;
        }

        String loc2 = readLine(scanner, "Enter Second Location: ");
        if (loc2.isEmpty()) {
            System.out.println("  [ERROR] Second location cannot be empty.");
            return;
        }

        system.removeCampusConnection(loc1, loc2);
    }

    private static void handleDisplayCampusConnections(StudentSystem system) {
        System.out.println("--- DISPLAY CAMPUS CONNECTIONS ---");
        system.displayCampusConnections();
    }

    private static void handleTraverseBFS(StudentSystem system, Scanner scanner) {
        System.out.println("--- TRAVERSE CAMPUS LOCATIONS USING BFS ---");
        String startLoc = readLine(scanner, "Enter Starting Location: ");
        if (startLoc.isEmpty()) {
            System.out.println("  [ERROR] Starting location cannot be empty.");
            return;
        }
        system.traverseCampusBFS(startLoc);
    }

    private static void handleTraverseDFS(StudentSystem system, Scanner scanner) {
        System.out.println("--- TRAVERSE CAMPUS LOCATIONS USING DFS ---");
        String startLoc = readLine(scanner, "Enter Starting Location: ");
        if (startLoc.isEmpty()) {
            System.out.println("  [ERROR] Starting location cannot be empty.");
            return;
        }
        system.traverseCampusDFS(startLoc);
    }

    // =========================================================================
    // SAFE INPUT UTILITIES (Prevent Crashes)
    // =========================================================================

    private static int readMenuChoice(Scanner scanner) {
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static Double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid numeric input for marks. Expected a number (e.g. 75.50).");
            return null;
        }
    }
}
