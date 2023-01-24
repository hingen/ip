package duke.command;

import duke.exception.DukeException;
import duke.task.Event;
import duke.task.Task;
import duke.task.TaskList;
import duke.utils.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents an add event command for adding an event to a task list.
 */
public class EventCommand extends AddCommand {
    /**
     * Create an event using the specified input, add it to tasks, and return an acknowledgement message.
     *
     * @param input {@inheritDoc}
     * @param tasks {@inheritDoc}
     * @return {@inheritDoc}
     * @throws DukeException Indicates an error in the input caused by one of the following: missing start or end of
     *                       event, missing description, incorrect format, invalid start or end of event.
     */
    @Override
    public String run(String input, TaskList tasks) throws DukeException {
        return super.run(input, tasks);
    }

    /**
     * {@inheritDoc}
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @throws DukeException Indicates an error in the input caused by one of the following: missing start or end of
     *                       event, missing description, incorrect format, invalid start or end of event.
     */
    @Override
    protected Task createTask(String input) throws DukeException {
        String[] args = extractValidArgs(input);
        return createEvent(args);
    }

    private String[] extractValidArgs(String input) throws DukeException {
        if (!input.matches("event .+ /from .+ /to .+")) {
            throw new DukeException("The event command format should be:\n  event <description> /from <start of event> "
                    + "/to <end of event>");
        }

        String argStr = input.replaceFirst("event ", "");
        String[] args = argStr.split("( /from | /to )", 3);

        for (int i = 0; i < args.length; ++i) {
            args[i] = args[i].trim();
        }

        if (args[0].isEmpty()) {
            throw new DukeException("The description of an event cannot be empty!");
        }

        if (args[1].isEmpty()) {
            throw new DukeException("The start of an event must be specified!");
        }

        if (args[2].isEmpty()) {
            throw new DukeException("The end of an event must be specified!");
        }

        if (!args[1].matches(LocalDateTimeUtils.inputDateTimeRegex)
                || !args[2].matches(LocalDateTimeUtils.inputDateTimeRegex)) {
            throw new DukeException(String.format("Start and end of event should be of the format:\n  %s",
                    LocalDateTimeUtils.inputDateTimeFormat));
        }

        return args;
    }

    private Event createEvent(String[] args) throws DukeException {
        LocalDateTime start;
        try {
            start = LocalDateTime.parse(args[1], LocalDateTimeUtils.inputDateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new DukeException("The start of the event provided is an invalid date and time!");
        }

        LocalDateTime end;
        try {
            end = LocalDateTime.parse(args[2], LocalDateTimeUtils.inputDateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new DukeException("The end of the event provided is an invalid date and time!");
        }

        return new Event(false, args[0], start, end);
    }
}
