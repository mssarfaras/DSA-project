# Stack and Queue component

Component for the **University Student Record and Campus Route Management System** (J. Nisathn, 23DA2-0684). This module contains only action history and student service requests; it does not define a `Student` or implement any other project data structures.

## Folder structure

```text
src/
  stack/
    Action.java
    ActionStack.java
  queue/
    ServiceRequest.java
    ServiceRequestQueue.java
  tests/
    StackQueueTest.java
```

## Data structures and API

`ActionStack` is a singly linked LIFO stack. `push(Action)` adds at the top; `pop()` removes and returns the newest action; `peek()` reads the newest action; `isEmpty()`, `size()`, and `display()` inspect it. Display order is newest to oldest. `Action` has `Type` values `ADD`, `UPDATE`, and `DELETE`, plus student ID and description.

`ServiceRequestQueue` is a singly linked FIFO queue with front and rear pointers. `enqueue(ServiceRequest)` appends at the rear; `dequeue()` removes and returns the oldest request; `peek()` reads the oldest request; `isEmpty()`, `size()`, and `display()` inspect it. Display order is arrival order. `ServiceRequest` holds request ID, student ID, and description.

Neither structure uses `java.util.Stack`, `java.util.Queue`, or Java collection classes to store its elements. Null elements and blank/null model fields are rejected with `IllegalArgumentException`; `pop`, `peek`, and `dequeue` on an empty structure throw `NoSuchElementException`.

## Usage / integration

Compile from the project root:

```sh
javac -d out src/stack/*.java src/queue/*.java
```

In the eventual Main class:

```java
import stack.Action;
import stack.ActionStack;
import queue.ServiceRequest;
import queue.ServiceRequestQueue;

ActionStack history = new ActionStack();
history.push(new Action(Action.Type.ADD, "23DA2-0684", "Student record added"));
history.display();
Action mostRecent = history.pop();

ServiceRequestQueue requests = new ServiceRequestQueue();
requests.enqueue(new ServiceRequest("REQ-001", "23DA2-0684", "Request enrollment letter"));
ServiceRequest next = requests.dequeue();
```

Call `peek()` before processing when the head item should stay queued. Catch `NoSuchElementException` when empty operations are possible. Record stack actions alongside operations in the other components; the stack stores IDs and descriptions only, so it does not couple to the final Student class.

## Validation

The executable `tests.StackQueueTest` covers multi-item display, peek, size, LIFO/FIFO removal order, empty inspection/removal exceptions, and invalid null/blank data. Run from the project root:

```sh
javac -d out src/stack/*.java src/queue/*.java src/tests/*.java
java -ea -cp out tests.StackQueueTest
```

Expected final line: `All StackQueueTest checks passed.`

## Suggested commit sequence

1. `Create Action model`
2. `Implement linked ActionStack`
3. `Create ServiceRequest model`
4. `Implement linked ServiceRequestQueue`
5. `Add Stack and Queue validation and integration docs`
