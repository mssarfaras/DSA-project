package stack;

import java.util.Objects;

/** A student-related event suitable for recent-action history or undo tracking. */
public final class Action {
    public enum Type { ADD, UPDATE, DELETE }

    private final Type type;
    private final String studentId;
    private final String description;

    public Action(Type type, String studentId, String description) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.studentId = requireText(studentId, "studentId");
        this.description = requireText(description, "description");
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be null or blank");
        }
        return value.trim();
    }

    public Type getType() { return type; }
    public String getStudentId() { return studentId; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return type + " | Student " + studentId + " | " + description;
    }
}
