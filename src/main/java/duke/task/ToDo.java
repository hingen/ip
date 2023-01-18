package duke.task;

/**
 * Represents a To-Do task.
 */
public class ToDo extends Task {
    /**
     * Creates a ToDoTask object.
     *
     * @param isDone Is the task done.
     * @param description Description of the task.
     */
    public ToDo(boolean isDone, String description) {
        super(isDone, description);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
