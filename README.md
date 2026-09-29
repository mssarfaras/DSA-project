# University Student Record and Campus Route Management System
## CIT300 Data Structures and Algorithms - Group Assignment

---

### Component: Student Record Management & Custom Singly Linked List
- **Assigned Member:** MS Sarfaras
- **Student ID:** 23DA2-0727
- **Module:** CIT300 Data Structures and Algorithms

---

## 1. Overview and Responsibility

This component is the core **Student Record Management** subsystem of the larger university management application. It is responsible for:
1. Defining the canonical **Student** entity model used throughout the entire system.
2. Implementing a custom, memory-efficient **Singly Linked List (`StudentLinkedList`)** from scratch without relying on `java.util.LinkedList` or any external data structure libraries.
3. Providing full **CRUD** (Create, Read, Update, Delete) student record operations.
4. Implementing robust input validation and defensive error handling (preventing duplicate IDs, invalid marks, null/empty values, and handling missing records).
5. Exposing clean, modular, and decoupled public APIs for seamless integration with other team members' components:
   - **Member 1:** Stack + Queue (Undo/Redo, Registration Processing)
   - **Member 2:** BST/AVL Trees + Hashing (Fast Searching, Indexing)
   - **Member 3:** Graph + BFS/DFS (Campus Route Management & Student Location Mapping)

---

## 2. Directory and File Structure

```
DSA project/
├── .gitignore
├── README.md
├── bin/                              # Compiled .class binaries (git-ignored)
└── src/
    ├── model/
    │   └── Student.java              # Canonical Student domain model
    ├── linkedlist/
    │   ├── StudentNode.java          # Singly linked list node
    │   └── StudentLinkedList.java    # Custom Linked List data structure
    ├── validation/
    │   ├── ValidationException.java  # Custom checked exception
    │   └── StudentValidator.java     # Validation utility rules
    ├── student/
    │   └── StudentManager.java       # High-level management controller/service
    └── test/
        └── StudentRecordTest.java    # Independent demonstration driver (9 test cases)
```

---

## 3. Explanation of Classes

### 3.1 `model.Student`
- **Fields:**
  - `private String studentId` (Unique student identifier)
  - `private String name` (Student's full name)
  - `private String programme` (Enrolled degree programme)
  - `private double marks` (Academic marks, 0.0 - 100.0)
- **Features:**
  - Standard constructors (Default, Parameterized, and Copy Constructor).
  - Getters and Setters adhering to encapsulation principles.
  - `getGrade()`: Dynamically calculates academic letter grades (`A+` down to `F`).
  - `equals(Object o)` & `hashCode()`: Based strictly on the unique `studentId`, allowing seamless compatibility with Hashing and Sets.
  - `Comparable<Student>`: Natural ordering by `studentId`, allowing immediate insertion into BST or AVL trees by Member 2.
  - `toFormattedRow()`: Neatly aligns data columns for tabular terminal output.

### 3.2 `linkedlist.StudentNode`
- **Fields:**
  - `private Student data` (Student payload object)
  - `private StudentNode next` (Reference pointer to the next node)
- **Features:**
  - Standard getters/setters for traversing and mutating the chain.
  - Encapsulated within the `linkedlist` package so external modules never need to manipulate node references directly.

### 3.3 `linkedlist.StudentLinkedList`
- **Fields:**
  - `private StudentNode head` (Pointer to first node)
  - `private int size` (Cached record count)
- **Operations:**
  - `addStudent(Student student)`: Appends student to the list in $O(N)$ time, ensuring uniqueness of `studentId`.
  - `searchStudent(String studentId)`: Traverses the chain and returns the `Student` object or `null` in $O(N)$ time.
  - `containsStudent(String studentId)`: Fast boolean existence check.
  - `updateStudent(String studentId, String newName, String newProgramme, double newMarks)`: Updates fields in-place if record exists.
  - `deleteStudent(String studentId)`: Deletes head, middle, or tail node in $O(N)$ time while updating `head` and `size`.
  - `displayStudents()`: Prints a structured console table.
  - `getSize()` & `isEmpty()`: $O(1)$ size tracking.
  - `toArray()`: Returns `Student[]` array for easy bulk transfers to Trees, Stacks, Queues, or Hash tables.

### 3.4 `validation.StudentValidator` & `validation.ValidationException`
- Reusable validator class preventing bad data from entering any subsystem:
  - **Empty Student ID:** Rejects `null` or blank strings.
  - **Empty Name / Programme:** Rejects `null` or blank strings.
  - **Invalid Marks:** Rejects NaN, infinite, negative values (`< 0.0`), and scores exceeding `100.0`.
  - Static boolean helper methods (`isValidStudentId`, `isValidMarks`, etc.) for safe conditional checks.

### 3.5 `student.StudentManager`
- High-level coordinator between `StudentLinkedList` and `StudentValidator`.
- Wraps business rules:
  - Validates inputs before mutation.
  - Prevents duplicate Student IDs with clear user feedback.
  - Handles missing student lookups and updates gracefully without crashes.
  - Provides formatted console display methods.

### 3.6 `test.StudentRecordTest`
- Standalone test suite with a dedicated `main()` method.
- Verifies all 9 assignment requirements in sequential order without touching any future `Main` class.

---

## 4. Public API Methods for Integration

Other team members can integrate directly with either `StudentManager` or `StudentLinkedList`:

### From `StudentManager`
```java
// Check existence
boolean exists = manager.containsStudent("23DA2-0727");

// Retrieve student reference
Student student = manager.searchStudent("23DA2-0727");

// Register a new student
boolean success = manager.addStudent("23DA2-0727", "MS Sarfaras", "Computer Science", 88.50);

// Update existing student
boolean updated = manager.updateStudent("23DA2-0727", "MS Sarfaras", "Software Engineering", 91.00);

// Delete student
boolean deleted = manager.deleteStudent("23DA2-0727");

// Get total count
int total = manager.getTotalStudents();

// Export array for Trees / Hash Tables / Queues
Student[] allStudents = manager.getAllStudents();

// Access the underlying linked list
StudentLinkedList list = manager.getStudentLinkedList();
```

### From `StudentLinkedList`
```java
StudentLinkedList list = new StudentLinkedList();

list.addStudent(student);
Student s = list.searchStudent("23DA2-0727");
boolean exists = list.containsStudent("23DA2-0727");
list.deleteStudent("23DA2-0727");
list.displayStudents();
int size = list.getSize();
Student[] array = list.toArray();
```

---

## 5. How Other Team Members Can Integrate

### Example 1: Member 1 (Stack + Queue)
```java
// Transferring students to a Queue for processing
Queue<Student> registrationQueue = new CustomQueue<>();
for (Student student : studentManager.getAllStudents()) {
    registrationQueue.enqueue(student);
}
```

### Example 2: Member 2 (BST / AVL / Hashing)
```java
// Student implements Comparable<Student> and has hashCode()/equals() by studentId
AVLTree<Student> studentTree = new AVLTree<>();
CustomHashTable<String, Student> studentHashTable = new CustomHashTable<>();

for (Student student : studentManager.getAllStudents()) {
    studentTree.insert(student);
    studentHashTable.put(student.getStudentId(), student);
}
```

### Example 3: Member 3 (Graph + Route Management)
```java
// Looking up student when assigning campus route or dorm navigation
Student student = studentManager.searchStudent(enteredStudentId);
if (student != null) {
    graph.findShortestPath(studentCampusLocation, destinationBuilding);
}
```

---

## 6. How to Compile and Run

### 6.1 Compile the Component
From the project root:
```bash
javac -d bin src/model/*.java src/linkedlist/*.java src/validation/*.java src/student/*.java src/test/*.java
```

### 6.2 Execute the Test Suite
```bash
java -cp bin test.StudentRecordTest
```
