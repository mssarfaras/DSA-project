# University Student Record and Campus Route Management System
## CIT300 Data Structures and Algorithms — Group Assignment

---

## 1. Project Overview & Group Members

This project is a fully integrated, high-performance Java console application developed for the **CIT300 Data Structures and Algorithms** course. It unites four custom, manually implemented data structure subsystems into a single application to manage student records, administrative requests, index lookups, and campus route navigation without external libraries.

### Assigned Group Members & Roles

| Member Name | Student ID | Assigned Contribution | Key Data Structures |
| :--- | :--- | :--- | :--- |
| **MS Sarfaras** | `23DA2-0727` | Student Records & Custom Linked List | Singly Linked List, Student Model, Validation |
| **J. Nisath** | `23DA2-0684` | Action History & Service Request Queue | Custom LIFO Linked Stack, Custom FIFO Linked Queue |
| **BA. Shawky** | `23DA2-0588` | Indexing & Fast Retrieval | Binary Search Tree (BST), Separate-Chaining Hash Table |
| **IF Hasna** | `23DA2-1154` | Campus Route Management & Graph Traversals | Adjacency List Graph, Custom Graph Queue, BFS & DFS |

---

## 2. Final Project Directory Structure

```
DSA-project/
│
├── .gitignore
├── README.md
├── bin/                                # Compiled Java bytecode (git-ignored)
└── src/
    │
    ├── Main.java                       # Single unified interactive console application
    │
    ├── model/
    │   └── Student.java                # Canonical, shared Student model entity
    │
    ├── linkedlist/
    │   ├── StudentNode.java            # Node structure for singly linked list
    │   └── StudentLinkedList.java      # Custom Singly Linked List implementation
    │
    ├── stack/
    │   ├── Action.java                 # Student operation action model
    │   └── ActionStack.java            # Custom LIFO linked stack
    │
    ├── queue/
    │   ├── ServiceRequest.java         # Student service request model
    │   └── ServiceRequestQueue.java    # Custom FIFO linked queue
    │
    ├── tree/
    │   ├── StudentTreeNode.java        # Node structure for student BST
    │   └── StudentBST.java             # Binary Search Tree ordered by Student ID
    │
    ├── hashing/
    │   └── StudentHashTable.java       # Custom separate-chaining Hash Table
    │
    ├── graph/
    │   ├── GraphNode.java              # Campus location vertex with adjacency list
    │   ├── GraphQueue.java             # Custom FIFO queue for BFS traversal
    │   └── CampusGraph.java            # Undirected Adjacency List Campus Network
    │
    ├── service/
    │   └── StudentSystem.java          # Central coordinator keeping all structures in sync
    │
    ├── student/
    │   └── StudentManager.java         # Student record management service
    │
    ├── validation/
    │   ├── ValidationException.java    # Custom checked validation exception
    │   └── StudentValidator.java       # Centralized business validation rules
    │
    └── test/
        ├── SystemIntegrationTest.java  # Comprehensive 20-scenario integration test suite
        ├── StudentRecordTest.java      # Member 1 unit/component test suite
        ├── StackQueueTest.java         # Member 2 unit/component test suite
        ├── StudentIndexTest.java       # Member 3 unit/component test suite
        └── CampusGraphTest.java        # Member 4 unit/component test suite
```

---

## 3. System Architecture & Central Synchronization

A critical architectural rule of this system is that all indexing and storage structures are kept **consistently synchronized** through the central coordinator `service.StudentSystem`.

```
                        User Input (Main.java)
                                  │
                                  ▼
                     service.StudentSystem (Facade)
         ┌───────────────┬────────────────┬────────────────┬──────────────┐
         ▼               ▼                ▼                ▼              ▼
   StudentLinkedList   StudentBST    StudentHashTable  ActionStack  ServiceRequestQueue
   (MS Sarfaras)       (Shawky)          (Shawky)      (J. Nisathn)    (J. Nisathn)
         │               │                │
         └───────────────┼────────────────┘
                         ▼
             Shared model.Student Entity
```

### Data Synchronization Flow:
1. **Adding a Student:**
   - Validates all fields through `StudentValidator`.
   - Checks for duplicate ID across `StudentHashTable` and `StudentLinkedList`.
   - Concurrently inserts the `Student` object into:
     - `StudentLinkedList`
     - `StudentBST`
     - `StudentHashTable`
   - Pushes an `ADD` action onto `ActionStack` (LIFO).
2. **Updating a Student:**
   - Locates student by ID.
   - Validates updated name, programme, and marks.
   - Modifies the shared `Student` instance via `StudentLinkedList`.
   - Re-indexes the node in `StudentBST` and `StudentHashTable`.
   - Pushes an `UPDATE` action onto `ActionStack`.
3. **Deleting a Student:**
   - Verifies existence in `StudentHashTable`.
   - Removes student from `StudentLinkedList`.
   - Removes student from `StudentBST`.
   - Removes student from `StudentHashTable`.
   - Pushes a `DELETE` action onto `ActionStack`.

---

## 4. Component Details & Classes

### 4.1 Shared Model (`model.Student`)
* **Single Source of Truth:** A single `Student.java` model is shared across the entire project.
* **Fields:** `studentId`, `name`, `programme`, `marks`.
* **Encapsulation:** All fields are private with getters and setters.
* **Features:**
  * `getGrade()`: Computes letter grade (`A+` to `F`).
  * `compareTo()`: Lexicographical order by `studentId` for BST ordering.
  * `equals()` & `hashCode()`: Based strictly on unique `studentId` for Hash Table hashing.
  * `toFormattedRow()`: Formatted tabular representation.

### 4.2 Linked List Component (`linkedlist` & `student`)
* **Author:** MS Sarfaras (`23DA2-0727`)
* `StudentNode`: Singly linked list node containing `Student data` and `StudentNode next`.
* `StudentLinkedList`: Custom singly linked list supporting `addStudent()`, `updateStudent()`, `deleteStudent()`, `searchStudent()`, `displayStudents()`, and `toArray()`.
* `StudentManager`: Service handling CRUD operations and validation messages.

### 4.3 Stack & Queue Component (`stack` & `queue`)
* **Author:** J. Nisathn (`23DA2-0684`)
* `Action` & `ActionStack`: Custom linked LIFO stack recording `ADD`, `UPDATE`, and `DELETE` actions. Provides `push()`, `pop()`, `peek()`, `isEmpty()`, and `display()`.
* `ServiceRequest` & `ServiceRequestQueue`: Custom linked FIFO queue managing student administrative requests in arrival order. Provides `enqueue()`, `dequeue()`, `peek()`, and `display()`.

### 4.4 Tree & Hashing Component (`tree` & `hashing`)
* **Author:** Shawky (`23DA2-0588`)
* `StudentTreeNode` & `StudentBST`: Unbalanced Binary Search Tree ordered by `Student ID`. Provides `insert()`, `search()`, `delete()`, and `displayInOrder()`.
* `StudentHashTable`: Custom separate-chaining Hash Table with dynamic resizing when load factor exceeds 0.75. Provides $O(1)$ average search, insert, and delete by `Student ID`.

### 4.5 Campus Graph Component (`graph`)
* **Author:** IF Hasna (`23DA2-1154`)
* `GraphNode`: Vertex representing a campus location with an adjacency list of neighbor locations.
* `GraphQueue`: Custom FIFO queue built from scratch for BFS.
* `CampusGraph`: Undirected graph supporting dynamic addition/removal of locations and bidirectional roads, Adjacency List display, BFS traversal, DFS recursive traversal, and unweighted shortest path calculation.

---

## 5. Console Main Menu (16 Options + Exit)

The interactive application runs through `Main.java` with a clean, looping console menu:

```
========================================
   UNIVERSITY STUDENT RECORD SYSTEM
========================================
 1. Add Student Record
 2. Update Student Record
 3. Delete Student Record
 4. Display All Records using Linked List

 5. Add Service Request
 6. Process Next Service Request
 7. Display Recent Actions using Stack

 8. Display Students using BST/AVL
 9. Search Student using Hashing

10. Add Campus Location
11. Remove Campus Location
12. Add Campus Connection/Road
13. Remove Campus Connection/Road
14. Display Campus Connections
15. Traverse Campus Locations using BFS
16. Traverse Campus Locations using DFS

 0. Exit
========================================
Enter your choice:
```

---

## 6. Defensive Programming & Input Validation

The system implements defensive input handling to prevent runtime crashes:
* **Non-numeric menu inputs:** Handled safely via `try-catch` without crashing.
* **Invalid marks:** Rejects negative marks (`< 0.0`), marks exceeding `100.0`, and non-numeric inputs.
* **Empty / blank inputs:** Rejects null or blank strings for IDs, names, programmes, locations, and request descriptions.
* **Duplicate Student IDs:** Detected and rejected with informative error messages.
* **Missing students / locations:** Handled gracefully with clear user feedback.
* **Empty Stack / Queue / Tree / Graph:** Informative messages displayed instead of throwing exceptions.

---

## 7. Compilation and Execution Instructions

### 7.1 Compile the Project
From the repository root directory:
```bash
javac -d bin -cp bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```
*(On Linux/macOS bash: `javac -d bin -cp bin $(find src -name "*.java")`)*

### 7.2 Run the Main Interactive Application
```bash
java -cp bin Main
```

### 7.3 Run the Test Suites
* **Full 20-Scenario System Integration Test:**
  ```bash
  java -cp bin test.SystemIntegrationTest
  ```
* **Individual Component Tests:**
  ```bash
  java -cp bin test.StudentRecordTest      # Member 1 (Linked List)
  java -ea -cp bin test.StackQueueTest     # Member 2 (Stack & Queue)
  java -ea -cp bin test.StudentIndexTest   # Member 3 (BST & Hash Table)
  java -cp bin test.CampusGraphTest        # Member 4 (Campus Graph)
  ```

---

## 8. Git Branches and Integration History

The integration was completed from feature branches into `master`:
* `sarfaras-student-records`: Linked List + Student Record CRUD + Validation
* `nisathn-stack-queue`: ActionStack (LIFO) + ServiceRequestQueue (FIFO)
* `shawky-bst-hashing`: StudentBST (In-Order) + StudentHashTable (Separate Chaining)
* `hasna-graph`: CampusGraph (Adjacency List) + GraphQueue + BFS/DFS
* `master`: Integrated unified console application (`Main.java`) + `StudentSystem.java` facade
