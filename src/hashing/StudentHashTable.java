package hashing;

import model.Student;

/**
 * Student ID index using a manually implemented separate-chaining hash table.
 * IDs are unique ignoring case and surrounding spaces.
 */
public final class StudentHashTable {
    private static final int DEFAULT_CAPACITY = 11;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private Entry[] buckets;
    private int size;

    public StudentHashTable() {
        this(DEFAULT_CAPACITY);
    }

    /** Creates a table with the requested positive starting capacity. */
    public StudentHashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initial capacity must be positive");
        }
        buckets = new Entry[initialCapacity];
    }

    /**
     * Inserts a student, growing the table when its load factor exceeds 0.75.
     *
     * @return true when inserted, or false when that ID already exists
     * @throws IllegalArgumentException when the student or its ID is invalid
     */
    public boolean insert(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("student must not be null");
        }
        String key = normalizeId(student.getStudentId());
        if (key == null) {
            throw new IllegalArgumentException("student ID must not be null or blank");
        }

        int index = indexForKey(key, buckets.length);
        for (Entry entry = buckets[index]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                return false;
            }
        }

        if ((size + 1.0) / buckets.length > MAX_LOAD_FACTOR) {
            resize();
            index = indexForKey(key, buckets.length);
        }
        buckets[index] = new Entry(key, student, buckets[index]);
        size++;
        return true;
    }

    /** Returns the student with this ID, or null when it is absent or invalid. */
    public Student search(String studentId) {
        String key = normalizeId(studentId);
        if (key == null) {
            return null;
        }
        for (Entry entry = buckets[indexForKey(key, buckets.length)]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                return entry.student;
            }
        }
        return null;
    }

    /** Deletes an entry, returning false when its ID is missing or invalid. */
    public boolean delete(String studentId) {
        String key = normalizeId(studentId);
        if (key == null) {
            return false;
        }
        int index = indexForKey(key, buckets.length);
        Entry previous = null;
        Entry current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public boolean contains(String studentId) {
        return search(studentId) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return buckets.length;
    }

    /**
     * Returns the bucket index for an ID using this table's current capacity.
     * Useful for inspecting and demonstrating collisions.
     */
    public int hashIndex(String studentId) {
        String key = normalizeId(studentId);
        if (key == null) {
            throw new IllegalArgumentException("student ID must not be null or blank");
        }
        return indexForKey(key, buckets.length);
    }

    /** Prints each bucket and its collision chain. */
    public void display() {
        System.out.println("Student hash table (separate chaining, capacity " + buckets.length + "):");
        for (int i = 0; i < buckets.length; i++) {
            StringBuilder line = new StringBuilder("[").append(i).append("]");
            for (Entry entry = buckets[i]; entry != null; entry = entry.next) {
                line.append(" -> ").append(entry.student.getStudentId());
            }
            line.append(" -> null");
            System.out.println(line);
        }
        System.out.println("Total students: " + size);
    }

    private void resize() {
        Entry[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2 + 1];
        for (Entry bucket : oldBuckets) {
            Entry entry = bucket;
            while (entry != null) {
                Entry next = entry.next;
                int index = indexForKey(entry.key, buckets.length);
                entry.next = buckets[index];
                buckets[index] = entry;
                entry = next;
            }
        }
    }

    private static int indexForKey(String key, int capacity) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = 31 * hash + key.charAt(i);
        }
        return Math.floorMod(hash, capacity);
    }

    private static String normalizeId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        String trimmed = studentId.trim();
        StringBuilder normalized = new StringBuilder(trimmed.length());
        for (int i = 0; i < trimmed.length(); i++) {
            char character = trimmed.charAt(i);
            normalized.append(Character.toLowerCase(Character.toUpperCase(character)));
        }
        return normalized.toString();
    }

    private static final class Entry {
        private final String key;
        private final Student student;
        private Entry next;

        private Entry(String key, Student student, Entry next) {
            this.key = key;
            this.student = student;
            this.next = next;
        }
    }
}
