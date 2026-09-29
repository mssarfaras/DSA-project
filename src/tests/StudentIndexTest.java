package tests;

import hash.StudentHashTable;
import model.Student;
import tree.StudentBST;

/** Executable checks for the Student BST and hash table; run with -ea. */
public final class StudentIndexTest {
    private StudentIndexTest() { }

    public static void main(String[] args) {
        testStudentBst();
        testHashTable();
        testInvalidData();
        System.out.println("All StudentIndexTest checks passed.");
    }

    private static void testStudentBst() {
        StudentBST tree = new StudentBST();
        Student student30 = student("S30");
        Student student10 = student("S10");
        Student student20 = student("S20");
        Student student40 = student("S40");
        Student student35 = student("S35");
        assert tree.isEmpty() && tree.size() == 0;
        assert tree.insert(student30);
        assert tree.insert(student10);
        assert tree.insert(student20);
        assert tree.insert(student40);
        assert tree.insert(student35);
        assert !tree.insert(student("s30"));
        assert tree.search(" s10 ") == student10;
        assert tree.search("S99") == null;
        assert !tree.contains("");
        assert tree.size() == 5;

        Student[] sorted = tree.toArrayInOrder();
        assert sorted.length == 5;
        assert sorted[0] == student10;
        assert sorted[1] == student20;
        assert sorted[2] == student30;
        assert sorted[3] == student35;
        assert sorted[4] == student40;
        tree.displayInOrder();

        assert tree.delete("S30");
        assert tree.search("S30") == null;
        assert !tree.delete("missing");
        assert tree.size() == 4;
        assert tree.delete("S10");
        assert tree.delete("S20");
        assert tree.delete("S35");
        assert tree.delete("S40");
        assert tree.isEmpty();
    }

    private static void testHashTable() {
        StudentHashTable table = new StudentHashTable(5);
        Student first = student("A");
        Student second = student("F");
        Student third = student("K");
        assert table.hashIndex("A") == table.hashIndex("F");
        assert table.hashIndex("F") == table.hashIndex("K");
        assert table.insert(first);
        assert table.insert(second);
        assert table.insert(third);
        assert table.search("f") == second;
        assert table.search("missing") == null;
        assert !table.insert(student("a"));
        assert table.size() == 3;
        table.display();
        assert table.delete("F");
        assert table.search("F") == null;
        assert !table.delete("F");

        for (int i = 0; i < 10; i++) {
            assert table.insert(student("GROW-" + i));
        }
        assert table.capacity() > 5;
        for (int i = 0; i < 10; i++) {
            assert table.contains("grow-" + i);
        }
    }

    private static void testInvalidData() {
        StudentBST tree = new StudentBST();
        StudentHashTable table = new StudentHashTable();
        assert tree.search(null) == null;
        assert !tree.delete(" ");
        assert table.search(null) == null;
        assert !table.delete(" ");
        expectInvalid(() -> tree.insert(null));
        expectInvalid(() -> tree.insert(student(" ")));
        expectInvalid(() -> table.insert(null));
        expectInvalid(() -> table.insert(student(null)));
        expectInvalid(() -> table.hashIndex(" "));
    }

    private static Student student(String id) {
        return new Student(id, "Test Student", "Computer Science", 75.0);
    }

    private static void expectInvalid(Runnable operation) {
        try {
            operation.run();
            throw new AssertionError("invalid input was accepted");
        } catch (IllegalArgumentException expected) {
            // Expected validation behavior.
        }
    }
}
