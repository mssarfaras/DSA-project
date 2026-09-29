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

---

## Component: Stack and Queue

- **Assigned Member:** J. Nisath
- **Student ID:** 23DA2-0684

### Stack: Recent Actions

`stack.ActionStack` is a manually linked LIFO stack. `push(Action)` adds the newest action; `pop()` removes and returns it; `peek()` reads it without removal; `isEmpty()`, `size()`, and `display()` inspect history. `stack.Action` stores an `Action.Type` (`ADD`, `UPDATE`, or `DELETE`), student ID, and description. Display order is newest to oldest.

### Queue: Service Requests

`queue.ServiceRequestQueue` is a manually linked FIFO queue. `enqueue(ServiceRequest)` adds at the rear; `dequeue()` removes and returns the earliest request; `peek()` reads it without removal; `isEmpty()`, `size()`, and `display()` inspect the queue. `queue.ServiceRequest` stores request ID, student ID, and description. Display order is arrival order.

Both structures use their own linked nodes, not Java's built-in Stack or Queue. Null values and blank model fields are rejected with `IllegalArgumentException`. Empty `pop()`, `peek()`, or `dequeue()` calls throw `NoSuchElementException`.

### Integration example

```java
ActionStack history = new ActionStack();
history.push(new Action(Action.Type.ADD, studentId, "Student record added"));

ServiceRequestQueue requests = new ServiceRequestQueue();
requests.enqueue(new ServiceRequest("REQ-001", studentId, "Enrollment letter"));
ServiceRequest next = requests.dequeue();
```

Import `stack.Action`, `stack.ActionStack`, `queue.ServiceRequest`, and `queue.ServiceRequestQueue`. These classes store student IDs only and do not duplicate the project's `Student` model.

### Stack and Queue checks

Compile and run the checks from the project root:

```bash
javac -d bin src/stack/*.java src/queue/*.java src/tests/*.java
java -ea -cp bin tests.StackQueueTest
```

The checks cover multi-item display, peek, LIFO/FIFO order, empty operations, and invalid data. Expected final line: `All StackQueueTest checks passed.`

---

## Component: Student BST and Hash Table

- **Assigned Member:** Shawky
- **Student ID:** 23DA2-0588

### Files

```text
src/
├── hash/
│   └── StudentHashTable.java
├── tests/
│   └── StudentIndexTest.java
└── tree/
    ├── StudentBST.java
    └── StudentTreeNode.java
```

The index structures reuse `model.Student`; they do not introduce another student model.
Student IDs are trimmed and compared case-insensitively. IDs must not be changed while
the corresponding student is indexed; if an ID changes, delete the old entry and insert
the student again so both indexes keep their key ordering and bucket placement valid.

### Binary search tree

`tree.StudentBST` is a manually implemented, unbalanced binary search tree. Each
`StudentTreeNode` stores a Student ID key, the shared `Student` reference, and child
links. Insert and search follow the left or right child according to case-insensitive
ID ordering. Duplicate IDs are rejected without replacing the stored student.

Deletion handles leaf nodes, nodes with one child, and nodes with two children. For
the two-child case, the smallest node in the right subtree replaces the deleted node.
`toArrayInOrder()` and `displayInOrder()` visit left subtree, current node, then right
subtree, displaying IDs in ascending order.

For tree height `h`, insertion, search, and deletion take O(h); they are O(log n) for
a balanced-shaped tree but O(n) in the worst case (for example, already sorted input).
An in-order traversal takes O(n) time.

### Hash table

`hash.StudentHashTable` uses a manually implemented array of buckets with **separate
chaining**: each bucket holds a linked chain of entries, so multiple IDs that map to
the same bucket are retained. The deterministic hash uses the polynomial recurrence
`hash = 31 * hash + character` over the trimmed, case-folded ID, then
`Math.floorMod(hash, capacity)` to obtain a valid bucket index. The table starts at
capacity 11 by default (or accepts a positive initial capacity) and doubles plus one
when insertion would take the load factor above 0.75.

Search, insertion, and deletion are expected O(1) with a reasonable distribution and
O(n) in the worst case when many entries share a bucket. Resizing takes O(n).

### Public API

```java
StudentBST tree = new StudentBST();
boolean addedToTree = tree.insert(student);
Student foundInTree = tree.search(studentId); // null if absent or invalid
boolean removedFromTree = tree.delete(studentId);
boolean treeHasId = tree.contains(studentId);
boolean treeEmpty = tree.isEmpty();
int treeSize = tree.size();
Student[] sortedStudents = tree.toArrayInOrder();
tree.displayInOrder();

StudentHashTable index = new StudentHashTable(); // optionally new StudentHashTable(17)
boolean addedToIndex = index.insert(student);
Student foundInIndex = index.search(studentId); // null if absent or invalid
boolean removedFromIndex = index.delete(studentId);
boolean indexHasId = index.contains(studentId);
int indexSize = index.size();
int bucket = index.hashIndex(studentId); // current bucket, useful to inspect collisions
index.display();
```

Both `insert` methods throw `IllegalArgumentException` for a null student or a null/
blank student ID and return false for a duplicate ID. `search` returns null and
`delete` returns false for missing IDs; blank/null lookup or deletion inputs are
treated as invalid and return those same absent-result values. `StudentTreeNode`
exposes read-only getters; tree links are not part of the integration API.

### Integration with the shared student manager and final Main

Build each index from the canonical list and use hashing for direct lookups while
using the BST when sorted display is required:

```java
StudentBST studentTree = new StudentBST();
StudentHashTable studentIndex = new StudentHashTable();
for (Student student : studentManager.getAllStudents()) {
    studentTree.insert(student);
    studentIndex.insert(student);
}

Student student = studentIndex.search(enteredStudentId);
studentTree.displayInOrder();
```

When a student is added or deleted through the manager, apply the same successful
operation to both indexes. Keep the canonical list/manager as the owner of records;
these structures are search and display indexes, not replacements for the linked list.

### Checks

Compile and run the standalone checks from the project root:

```bash
javac -d bin src/model/Student.java src/tree/*.java src/hash/*.java src/tests/StudentIndexTest.java
java -ea -cp bin tests.StudentIndexTest
```

The checks exercise ordered traversal, two-child and leaf deletion, existing/missing
searches, duplicate and invalid IDs, an explicit collision chain, hash deletion, and
table resizing. Expected final line: `All StudentIndexTest checks passed.`

---

## Console application

`app.Main` is the console entry point for the currently available student record,
BST/hash index, action stack, and service request queue components. Compile all Java
sources and start the menu from the project root:

```powershell
javac -d bin (Get-ChildItem src -Recurse -Filter *.java).FullName
java -cp bin app.Main
```

The main class is in the `app` package, so launch it as `app.Main`, not `Main`.
The campus graph/BFS/DFS component is not currently included in this menu.
