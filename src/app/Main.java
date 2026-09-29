package app;

import hash.StudentHashTable;
import model.Student;
import queue.ServiceRequest;
import queue.ServiceRequestQueue;
import stack.Action;
import stack.ActionStack;
import student.StudentManager;
import tree.StudentBST;

import java.util.Scanner;

/** Console entry point integrating the available student and DSA components. */
public final class Main {
    private final Scanner input;
    private final StudentManager studentManager = new StudentManager();
    private final StudentBST studentTree = new StudentBST();
    private final StudentHashTable studentIndex = new StudentHashTable();
    private final ActionStack actionHistory = new ActionStack();
    private final ServiceRequestQueue serviceRequests = new ServiceRequestQueue();
    private int nextRequestNumber = 1;

    private Main(Scanner input) {
        this.input = input;
    }

    public static void main(String[] args) {
        new Main(new Scanner(System.in)).run();
    }

    private void run() {
        System.out.println("University Student Record and Campus Route Management System");
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = readLine("Select an option: ");
            if (choice == null) {
                break;
            }
            switch (choice.trim()) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    searchStudent();
                    break;
                case "3":
                    studentManager.displayAllStudents();
                    break;
                case "4":
                    studentTree.displayInOrder();
                    break;
                case "5":
                    updateStudent();
                    break;
                case "6":
                    deleteStudent();
                    break;
                case "7":
                    actionHistory.display();
                    break;
                case "8":
                    addServiceRequest();
                    break;
                case "9":
                    processServiceRequest();
                    break;
                case "10":
                    serviceRequests.display();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Choose one of the listed menu numbers.");
            }
            System.out.println();
        }
        System.out.println("Exiting system.");
    }

    private void displayMenu() {
        System.out.println();
        System.out.println("1. Add student");
        System.out.println("2. Search student by ID (hash table)");
        System.out.println("3. Display all students");
        System.out.println("4. Display students sorted by ID (BST)");
        System.out.println("5. Update student");
        System.out.println("6. Delete student");
        System.out.println("7. Display recent actions (stack)");
        System.out.println("8. Add service request");
        System.out.println("9. Process next service request");
        System.out.println("10. Display pending service requests");
        System.out.println("0. Exit");
    }

    private void addStudent() {
        String studentId = readLine("Student ID: ");
        String name = readLine("Student name: ");
        String programme = readLine("Programme: ");
        Double marks = readMarks();
        if (studentId == null || name == null || programme == null || marks == null) {
            return;
        }

        if (studentManager.addStudent(studentId, name, programme, marks)) {
            Student[] students = studentManager.getAllStudents();
            Student student = students[students.length - 1];
            studentTree.insert(student);
            studentIndex.insert(student);
            actionHistory.push(new Action(Action.Type.ADD, student.getStudentId(), "Student record added"));
        }
    }

    private void searchStudent() {
        String studentId = readLine("Student ID to search: ");
        if (studentId == null) {
            return;
        }
        Student student = studentIndex.search(studentId);
        if (student == null) {
            System.out.println("No student found for ID: " + studentId);
        } else {
            System.out.println(student.toFormattedRow());
        }
    }

    private void updateStudent() {
        String studentId = readLine("Student ID to update: ");
        if (studentId == null) {
            return;
        }
        if (!studentIndex.contains(studentId)) {
            System.out.println("No student found for ID: " + studentId);
            return;
        }
        String name = readLine("New student name: ");
        String programme = readLine("New programme: ");
        Double marks = readMarks();
        if (name != null && programme != null && marks != null
                && studentManager.updateStudent(studentId, name, programme, marks)) {
            actionHistory.push(new Action(Action.Type.UPDATE, studentId, "Student record updated"));
        }
    }

    private void deleteStudent() {
        String studentId = readLine("Student ID to delete: ");
        if (studentId != null && studentManager.deleteStudent(studentId)) {
            studentTree.delete(studentId);
            studentIndex.delete(studentId);
            actionHistory.push(new Action(Action.Type.DELETE, studentId, "Student record deleted"));
        }
    }

    private void addServiceRequest() {
        String studentId = readLine("Student ID for the request: ");
        if (studentId == null) {
            return;
        }
        if (!studentIndex.contains(studentId)) {
            System.out.println("Cannot add a request: no student found for ID " + studentId);
            return;
        }
        String description = readLine("Request description: ");
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Request description must not be blank.");
            return;
        }
        String requestId = String.format("REQ-%04d", nextRequestNumber++);
        serviceRequests.enqueue(new ServiceRequest(requestId, studentId, description));
        System.out.println("Added service request " + requestId + ".");
    }

    private void processServiceRequest() {
        if (serviceRequests.isEmpty()) {
            System.out.println("There are no pending service requests.");
            return;
        }
        System.out.println("Processed: " + serviceRequests.dequeue());
    }

    private Double readMarks() {
        String value = readLine("Marks (0-100): ");
        if (value == null) {
            return null;
        }
        try {
            return Double.valueOf(value.trim());
        } catch (NumberFormatException exception) {
            System.out.println("Marks must be a valid number.");
            return null;
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return input.hasNextLine() ? input.nextLine() : null;
    }
}
