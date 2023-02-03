package duke.task;

import duke.exception.DukeException;
import duke.io.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a task list.
 */
public class TaskList {
    private List<Task> tasks;

    private final Storage storage;

    /**
     * Creates a TaskList object and fill the task list using the data from the specified storage if it exist. If the
     * specified storage does not exist, create it.
     *
     * @param storage The storage to read the task list from and write the task list to.
     * @throws DukeException Indicates an error in loading from storage or creating the storage.
     */
    public TaskList(Storage storage) throws DukeException {
        assert storage != null;

        this.storage = storage;

        if (storage.doesExist()) {
            loadFromStorage();
        } else {
            tasks = new ArrayList<Task>();
            storage.create();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tasks.size(); ++i) {
            stringBuilder.append(String.format("%d.%s\n", i + 1, tasks.get(i).toString()));
        }

        return stringBuilder.toString().trim();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index The index of the task to be returned.
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException Indicates that the index is out of range.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds the specified task to the end of the list and writes the updated task list to storage.
     *
     * @param task The task to be added.
     * @throws DukeException Indicates failure to write to storage.
     */
    public void add(Task task) throws DukeException {
        assert task != null;

        tasks.add(task);

        try {
            writeToStorage();
        } catch (DukeException e) {
            tasks.remove(task);
            throw e;
        }
    }

    /**
     * Removes the task at the specified index, writes the updated task list to storage, and returns the removed task.
     *
     * @param index The index of the task to be removed.
     * @return The removed task.
     * @throws IndexOutOfBoundsException Indicates that the index is out of range.
     * @throws DukeException Indicates failure to write to storage.
     */
    public Task removeAt(int index) throws DukeException {
        Task task = tasks.remove(index);

        try {
            writeToStorage();
        } catch (DukeException e) {
            tasks.add(index, task);
            throw e;
        }

        return task;
    }

    /**
     * Updates the task at index.
     *
     * @param index The index of the task to be updated.
     * @param task The updated task.
     * @throws DukeException Indicates failure to write to storage.
     */
    public void update(int index, Task task) throws DukeException {
        assert task != null;

        Task oldTask = tasks.get(index);
        tasks.set(index, task);

        try {
            writeToStorage();
        } catch (DukeException e) {
            tasks.set(index, oldTask);
            throw e;
        }
    }

    private void loadFromStorage() throws DukeException {
        List<Task> tasks = new ArrayList<Task>();
        String[] taskStrs = storage.read().split("\n");

        for (String taskStr : taskStrs) {
            if (taskStr.isEmpty()) {
                continue;
            }

            tasks.add(createTask(taskStr.split(" \\| ")));
        }

        this.tasks = tasks;
    }

    private Task createTask(String[] args) throws DukeException {
        assert args != null;

        switch (args[0]) {
        case "T":
            return ToDo.createFromStorage(args);
        case "D":
            return Deadline.createFromStorage(args);
        case "E":
            return Event.createFromStorage(args);
        default:
            throw new DukeException("An unknown task type was found in storage.");
        }
    }

    private void writeToStorage() throws DukeException {
        StringBuilder taskStr = new StringBuilder();

        for (Task task : tasks) {
            taskStr.append(task.getStorageStr());
            taskStr.append("\n");
        }

        storage.write(taskStr.toString());
    }
}
