package duke.task;

/**
 * Represents an event task.
 */
public class Event extends Task {
    /**
     * Creates an EventTask object.
     *
     * @param isDone Is the task done.
     * @param description Description of the task.
     * @param start When the event starts.
     * @param end When the event ends.
     */
    public Event(boolean isDone, String description, String start, String end) {
        super(isDone, String.format("%s (from: %s to: %s)", description, start, end));
    }

    private Event(boolean isDone, String description) {
        super(isDone, description);
    }

    @Override
    public String toString() {
        return String.format("[E]%s", super.toString());
    }

    @Override
    protected Task createTask(boolean isDone, String description) {
        return new Event(isDone, description);
    }
}