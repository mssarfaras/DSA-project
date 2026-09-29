package service;

import graph.CampusGraph;
import hashing.StudentHashTable;
import java.util.List;
import linkedlist.StudentLinkedList;
import model.Student;
import queue.ServiceRequest;
import queue.ServiceRequestQueue;
import stack.Action;
import stack.ActionStack;
import tree.StudentBST;
import validation.StudentValidator;
import validation.ValidationException;

/**
 * Central system facade and service coordinator.
 * 
 * Synchronizes and coordinates all four core data structure subsystems:
 *  1. StudentLinkedList (MS Sarfaras - 23DA2-0727)
 *  2. ActionStack & ServiceRequestQueue (J. Nisathn - 23DA2-0684)
 *  3. StudentBST & StudentHashTable (Shawky - 23DA2-0588)
 *  4. CampusGraph with BFS/DFS (IF Hasna - 23DA2-1154)
 * 
 * Ensures that when students are added, updated, or deleted, all indexing structures
 * (Linked List, Binary Search Tree, Hash Table) remain fully synchronized, and actions
 * are recorded in the LIFO ActionStack.
 */
public class StudentSystem {

    private final StudentLinkedList studentList;
    private final StudentBST studentBst;
    private final StudentHashTable studentHashTable;
    private final ActionStack actionStack;
    private final ServiceRequestQueue requestQueue;
    private final CampusGraph campusGraph;

    /**
     * Constructs the integrated StudentSystem with all data structures initialized.
     */
    public StudentSystem() {
        this.studentList = new StudentLinkedList();
        this.studentBst = new StudentBST();
        this.studentHashTable = new StudentHashTable();
        this.actionStack = new ActionStack();
        this.requestQueue = new ServiceRequestQueue();
        this.campusGraph = new CampusGraph();
    }

    // =========================================================================
    // 1. STUDENT RECORD OPERATIONS (Synchronized across List, BST, Hash Table)
    // =========================================================================

    /**
     * Adds a new student into the Linked List, BST, and Hash Table, and records the action.
     *
     * @param studentId Unique student identifier
     * @param name      Student full name
     * @param programme Academic programme
     * @param marks     Marks (0.0 to 100.0)
     * @return true if added successfully, false otherwise
     */
    public boolean addStudent(String studentId, String name, String programme, double marks) {
        try {
            // Validate all fields using StudentValidator
            StudentValidator.validateAll(studentId, name, programme, marks);
            String cleanId = studentId.trim();
            String cleanName = name.trim();
            String cleanProg = programme.trim();

            // Check duplicate across data structures
            if (studentHashTable.contains(cleanId) || studentList.containsStudent(cleanId)) {
                System.out.println("  [ERROR] Student ID already exists.");
                return false;
            }

            Student student = new Student(cleanId, cleanName, cleanProg, marks);

            // Synchronized insertion into all 3 structures
            studentList.addStudent(student);
            studentBst.insert(student);
            studentHashTable.insert(student);

            // Record action in LIFO Stack
            actionStack.push(new Action(Action.Type.ADD, cleanId, "Added student: " + cleanName));

            System.out.println("  [SUCCESS] Student added successfully.");
            return true;

        } catch (ValidationException ve) {
            System.out.println("  [ERROR] " + ve.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing student record across all data structures.
     *
     * @param studentId    ID of student to update
     * @param newName      New name
     * @param newProgramme New programme
     * @param newMarks     New marks (0.0 to 100.0)
     * @return true if updated, false if student not found or data invalid
     */
    public boolean updateStudent(String studentId, String newName, String newProgramme, double newMarks) {
        try {
            StudentValidator.validateAll(studentId, newName, newProgramme, newMarks);
            String cleanId = studentId.trim();
            String cleanName = newName.trim();
            String cleanProg = newProgramme.trim();

            Student existing = studentHashTable.search(cleanId);
            if (existing == null) {
                System.out.println("  [ERROR] Update failed: Student not found.");
                return false;
            }

            // Update shared model object via Linked List
            studentList.updateStudent(cleanId, cleanName, cleanProg, newMarks);

            // Synchronize BST (delete and re-insert ensures tree ordering integrity)
            studentBst.delete(cleanId);
            studentBst.insert(existing);

            // Synchronize Hash Table (delete and re-insert updates buckets)
            studentHashTable.delete(cleanId);
            studentHashTable.insert(existing);

            // Record action in LIFO Stack
            actionStack.push(new Action(Action.Type.UPDATE, cleanId, "Updated student: " + cleanName));

            System.out.println("  [SUCCESS] Student updated successfully.");
            return true;

        } catch (ValidationException ve) {
            System.out.println("  [ERROR] " + ve.getMessage());
            return false;
        }
    }

    /**
     * Deletes a student record from Linked List, BST, and Hash Table.
     *
     * @param studentId ID of student to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty.");
            return false;
        }

        String cleanId = studentId.trim();
        Student existing = studentHashTable.search(cleanId);
        if (existing == null) {
            System.out.println("  [ERROR] Delete failed: Student not found.");
            return false;
        }

        String studentName = existing.getName();

        // Remove from all 3 data structures
        studentList.deleteStudent(cleanId);
        studentBst.delete(cleanId);
        studentHashTable.delete(cleanId);

        // Record action in LIFO Stack
        actionStack.push(new Action(Action.Type.DELETE, cleanId, "Deleted student: " + studentName));

        System.out.println("  [SUCCESS] Student deleted successfully.");
        return true;
    }

    /**
     * Displays all student records using the Linked List traversal.
     */
    public void displayAllStudentsLinkedList() {
        if (studentList.isEmpty()) {
            System.out.println("  [INFO] No student records found in the Linked List.");
            return;
        }
        studentList.displayStudents();
    }

    /**
     * Displays all students ordered by Student ID using BST in-order traversal.
     */
    public void displayStudentsBST() {
        if (studentBst.isEmpty()) {
            System.out.println("  [INFO] Student BST is empty. No student records to display.");
            return;
        }
        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");
        System.out.println("| Student ID      | Student Name              | Programme                 | Marks  | Grd  |");
        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");
        studentBst.displayInOrder();
        System.out.println("+-----------------+---------------------------+---------------------------+--------+------+");
        System.out.println("  Total Students (BST In-Order Traversal): " + studentBst.size());
    }

    /**
     * Searches for a student directly using the Hash Table.
     *
     * @param studentId ID of student to find
     * @return Found Student object or null
     */
    public Student searchStudentHashing(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty.");
            return null;
        }

        String cleanId = studentId.trim();
        Student student = studentHashTable.search(cleanId);

        if (student != null) {
            System.out.println("\n  Student found:");
            System.out.println("  ID:        " + student.getStudentId());
            System.out.println("  Name:      " + student.getName());
            System.out.println("  Programme: " + student.getProgramme());
            System.out.println("  Marks:     " + String.format("%.2f", student.getMarks()));
            System.out.println("  Grade:     " + student.getGrade());
            return student;
        } else {
            System.out.println("  [ERROR] Student not found with ID: " + cleanId);
            return null;
        }
    }

    // =========================================================================
    // 2. QUEUE & STACK OPERATIONS
    // =========================================================================

    /**
     * Adds a student service request to the FIFO queue.
     *
     * @param requestId   Unique request ID
     * @param studentId   Associated student ID
     * @param description Nature of request
     * @return true if added, false if invalid
     */
    public boolean addServiceRequest(String requestId, String studentId, String description) {
        if (requestId == null || requestId.trim().isEmpty()) {
            System.out.println("  [ERROR] Request ID cannot be empty.");
            return false;
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("  [ERROR] Student ID cannot be empty.");
            return false;
        }
        if (description == null || description.trim().isEmpty()) {
            System.out.println("  [ERROR] Description cannot be empty.");
            return false;
        }

        String cleanReq = requestId.trim();
        String cleanStudent = studentId.trim();
        String cleanDesc = description.trim();

        if (!studentHashTable.contains(cleanStudent)) {
            System.out.println("  [NOTE] Note: Student ID '" + cleanStudent + "' is not currently registered in the student records.");
        }

        ServiceRequest request = new ServiceRequest(cleanReq, cleanStudent, cleanDesc);
        requestQueue.enqueue(request);
        System.out.println("  [SUCCESS] Service request added to queue: " + request);
        return true;
    }

    /**
     * Processes the next service request in FIFO order.
     *
     * @return The processed ServiceRequest, or null if queue is empty
     */
    public ServiceRequest processNextServiceRequest() {
        if (requestQueue.isEmpty()) {
            System.out.println("  [INFO] Service request queue is empty. No requests to process.");
            return null;
        }

        ServiceRequest processed = requestQueue.dequeue();
        System.out.println("  [PROCESSED] Successfully processed request:");
        System.out.println("  -> " + processed);
        return processed;
    }

    /**
     * Displays all recorded actions from the LIFO stack (newest first).
     */
    public void displayRecentActions() {
        if (actionStack.isEmpty()) {
            System.out.println("  [INFO] Action history is empty. No actions recorded yet.");
            return;
        }
        System.out.println("  Recent Actions (Newest to Oldest - LIFO):");
        actionStack.display();
    }

    // =========================================================================
    // 3. CAMPUS GRAPH OPERATIONS
    // =========================================================================

    /**
     * Adds a new campus location (vertex) to the graph.
     */
    public boolean addCampusLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            System.out.println("  [ERROR] Location name cannot be empty.");
            return false;
        }
        String clean = location.trim();
        if (campusGraph.containsLocation(clean)) {
            System.out.println("  [ERROR] Campus location already exists.");
            return false;
        }
        boolean added = campusGraph.addLocation(clean);
        if (added) {
            System.out.println("  [SUCCESS] Campus location '" + clean + "' added successfully.");
        }
        return added;
    }

    /**
     * Removes a campus location and all connecting roads from the graph.
     */
    public boolean removeCampusLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            System.out.println("  [ERROR] Location name cannot be empty.");
            return false;
        }
        String clean = location.trim();
        if (!campusGraph.containsLocation(clean)) {
            System.out.println("  [ERROR] Location '" + clean + "' does not exist in the campus network.");
            return false;
        }
        return campusGraph.removeLocation(clean);
    }

    /**
     * Adds an undirected connection (road) between two campus locations.
     */
    public boolean addCampusConnection(String location1, String location2) {
        if (location1 == null || location1.trim().isEmpty() || location2 == null || location2.trim().isEmpty()) {
            System.out.println("  [ERROR] Location names cannot be empty.");
            return false;
        }
        String clean1 = location1.trim();
        String clean2 = location2.trim();

        if (!campusGraph.containsLocation(clean1) || !campusGraph.containsLocation(clean2)) {
            System.out.println("  [ERROR] Connection cannot be added because one or both locations do not exist.");
            return false;
        }

        if (clean1.equalsIgnoreCase(clean2)) {
            System.out.println("  [ERROR] Cannot connect location to itself (no self-loops).");
            return false;
        }

        if (campusGraph.hasConnection(clean1, clean2)) {
            System.out.println("  [ERROR] Connection between '" + clean1 + "' and '" + clean2 + "' already exists.");
            return false;
        }

        boolean connected = campusGraph.addConnection(clean1, clean2);
        if (connected) {
            System.out.println("  [SUCCESS] Connection between '" + clean1 + "' and '" + clean2 + "' added successfully.");
        }
        return connected;
    }

    /**
     * Removes an undirected road between two campus locations.
     */
    public boolean removeCampusConnection(String location1, String location2) {
        if (location1 == null || location1.trim().isEmpty() || location2 == null || location2.trim().isEmpty()) {
            System.out.println("  [ERROR] Location names cannot be empty.");
            return false;
        }
        String clean1 = location1.trim();
        String clean2 = location2.trim();

        if (!campusGraph.containsLocation(clean1) || !campusGraph.containsLocation(clean2)) {
            System.out.println("  [ERROR] Cannot remove connection: One or both locations do not exist.");
            return false;
        }

        return campusGraph.removeConnection(clean1, clean2);
    }

    /**
     * Displays all campus locations and their connected roads in Adjacency List format.
     */
    public void displayCampusConnections() {
        if (campusGraph.getLocationCount() == 0) {
            System.out.println("  [INFO] Campus network is currently empty. No locations registered.");
            return;
        }
        campusGraph.displayConnections();
    }

    /**
     * Performs Breadth-First Search (BFS) starting from a location.
     */
    public void traverseCampusBFS(String startLocation) {
        if (startLocation == null || startLocation.trim().isEmpty()) {
            System.out.println("  [ERROR] Starting location cannot be empty.");
            return;
        }
        String clean = startLocation.trim();
        if (!campusGraph.containsLocation(clean)) {
            System.out.println("  [ERROR] Location '" + clean + "' does not exist in the campus network.");
            return;
        }

        List<String> order = campusGraph.bfs(clean);
        System.out.println("  BFS Traversal Order from '" + clean + "':");
        System.out.println("  " + String.join(" -> ", order));
    }

    /**
     * Performs Depth-First Search (DFS) starting from a location.
     */
    public void traverseCampusDFS(String startLocation) {
        if (startLocation == null || startLocation.trim().isEmpty()) {
            System.out.println("  [ERROR] Starting location cannot be empty.");
            return;
        }
        String clean = startLocation.trim();
        if (!campusGraph.containsLocation(clean)) {
            System.out.println("  [ERROR] Location '" + clean + "' does not exist in the campus network.");
            return;
        }

        List<String> order = campusGraph.dfs(clean);
        System.out.println("  DFS Traversal Order from '" + clean + "':");
        System.out.println("  " + String.join(" -> ", order));
    }

    // =========================================================================
    // GETTERS FOR INTERNAL STRUCTURES (Integration / Extension)
    // =========================================================================

    public StudentLinkedList getStudentList() {
        return studentList;
    }

    public StudentBST getStudentBst() {
        return studentBst;
    }

    public StudentHashTable getStudentHashTable() {
        return studentHashTable;
    }

    public ActionStack getActionStack() {
        return actionStack;
    }

    public ServiceRequestQueue getRequestQueue() {
        return requestQueue;
    }

    public CampusGraph getCampusGraph() {
        return campusGraph;
    }
}
