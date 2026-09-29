package queue;

/** A student service request waiting to be processed. */
public final class ServiceRequest {
    private final String requestId;
    private final String studentId;
    private final String description;

    public ServiceRequest(String requestId, String studentId, String description) {
        this.requestId = requireText(requestId, "requestId");
        this.studentId = requireText(studentId, "studentId");
        this.description = requireText(description, "description");
    }

    private static String requireText(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be null or blank");
        }
        return value.trim();
    }

    public String getRequestId() { return requestId; }
    public String getStudentId() { return studentId; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Request " + requestId + " | Student " + studentId + " | " + description;
    }
}
