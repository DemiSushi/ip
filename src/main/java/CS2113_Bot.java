import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A command-line chatbot that manages a list of tasks.
 */
public class CS2113_Bot {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private static final String DIVIDER = "__________________________________";
    private static final String BANNER =
            "_    _ ______ _      _      ____   __          __  {_} _____  _      _____  \n"
                    + "| |  | |  ____| |    | |    / __ \\  \\ \\        / / | |  __ \\| |    |  __ \\ \n"
                    + "| |__| | |__  | |    | |   | |  | |  \\ \\  /\\  / /  | | |__) | |    | |  | |\n"
                    + "|  __  |  __| | |    | |   | |  | |   \\ \\/  \\/ /   | |  _  /| |    | |  | |\n"
                    + "| |  | | |____| |____| |____| |__| |    \\  /\\  /    | | | \\ \\| |____| |__| |\n"
                    + "|_|  |_|______|______|______|\\____/      \\/  \\/     |_| |_| \\_\\______|_____/ ";
    private static ArrayList<Task> taskList = new ArrayList<>();

    /**
     * Saves every task in the current list to the data file.
     *
     * @throws CS2113BotException if a task cannot be saved or the data file cannot be written
     */
    private static void saveTasks() throws CS2113BotException {
        StringBuilder savedTasks = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            savedTasks.append(formatTask(taskList.get(i))).append(System.lineSeparator());
        }

        try {
            if (DATA_FILE.getParent() != null) {
                Files.createDirectories(DATA_FILE.getParent());
            }
            Files.writeString(DATA_FILE, savedTasks.toString(), StandardCharsets.UTF_8);
        } catch (IOException | SecurityException e) {
            throw new CS2113BotException("Unable to save tasks to " + DATA_FILE + ".");
        }
    }

    /**
     * Converts one task to a single line in the task data file.
     */
    private static String formatTask(Task task) throws CS2113BotException {
        if (task == null) {
            throw new CS2113BotException("Unable to save an empty task.");
        }

        String status = task.isDone ? "1" : "0";
        validateTaskField(task.description, "Task description");
        if (task instanceof Todo) {
            return "T | " + status + " | " + task.description;
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            validateTaskField(deadline.by, "Deadline date");
            return "D | " + status + " | " + deadline.description + " | " + deadline.by;
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            validateTaskField(event.from, "Event start time");
            validateTaskField(event.to, "Event end time");
            return "E | " + status + " | " + event.description + " | " + event.from + " | " + event.to;
        }
        throw new CS2113BotException("Unable to save an unknown task type.");
    }

    private static void validateTaskField(String value, String fieldName) throws CS2113BotException {
        if (value == null || value.trim().isEmpty()) {
            throw new CS2113BotException(fieldName + " cannot be empty.");
        }
        if (value.contains(" | ") || value.contains("\n") || value.contains("\r")) {
            throw new CS2113BotException(fieldName + " cannot contain ' | ' or a line break.");
        }
    }

    private static void readTasks(String filePath) throws CS2113BotException {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                return;
            }
            if (!Files.isRegularFile(path)) {
                throw new CS2113BotException("Task data path is not a file: " + filePath + ".");
            }

            taskList.clear();
            int lineNumber = 0;
            try (Scanner fileScanner = new Scanner(path, StandardCharsets.UTF_8)) {
                while (fileScanner.hasNextLine()) {
                    lineNumber++;
                    String line = fileScanner.nextLine();
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    taskList.add(parseSavedTask(line, lineNumber));
                }
            }
        } catch (IOException | InvalidPathException | SecurityException e) {
            throw new CS2113BotException("Unable to read tasks from " + filePath + ".");
        }
    }

    private static Task parseSavedTask(String line, int lineNumber) throws CS2113BotException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3 || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            throw new CS2113BotException("Invalid task data on line " + lineNumber + ".");
        }

        Task task;
        if (parts[0].equals("T") && parts.length == 3) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            task = new Todo(parts[2]);
        } else if (parts[0].equals("D") && parts.length == 4) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            validateTaskField(parts[3], "Deadline date on line " + lineNumber);
            task = new Deadline(parts[2], parts[3]);
        } else if (parts[0].equals("E") && parts.length == 5) {
            validateTaskField(parts[2], "Task description on line " + lineNumber);
            validateTaskField(parts[3], "Event start time on line " + lineNumber);
            validateTaskField(parts[4], "Event end time on line " + lineNumber);
            task = new Event(parts[2], parts[3], parts[4]);
        } else {
            throw new CS2113BotException("Invalid task data on line " + lineNumber + ".");
        }
        task.setDone(parts[1].equals("1"));
        return task;
    }

    public static void printDivider() {
        System.out.println(DIVIDER);
    }

    public static void printWelcome() {
        printDivider();
        System.out.println(BANNER);
        System.out.println("\nHello! I'm CS2113_Bot.");
        System.out.println("What can I do for you?");
        printDivider();
    }

    public static void printExit() {
        printDivider();
        System.out.println("Bye. Hope to see you again soon!");
        printDivider();
    }

    private static void printTaskAdded(Task task) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        printDivider();
    }

    public static void handleList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
        printDivider();
    }

    public static void handleMark(String line, boolean isDone) throws CS2113BotException {
        String[] words = line.trim().split("\\s+");

        if (words.length != 2) {
            throw new CS2113BotException("Please use: mark <task number>");
        }

        try {
            int taskNumber = Integer.parseInt(words[1]);

            if (taskNumber < 1 || taskNumber > taskList.size()) {
                throw new CS2113BotException("There is no task numbered " + taskNumber + ".");
            }

            int taskIndex = taskNumber - 1;
            boolean prevStatus = taskList.get(taskIndex).isDone;
            taskList.get(taskIndex).setDone(isDone);

            try {
                saveTasks();
            } catch (CS2113BotException e) {
                taskList.get(taskIndex).setDone(prevStatus);
                throw e;
            }

            if (isDone) {
                System.out.println("Nice! I've marked this task as done:");
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + taskList.get(taskIndex));
        } catch (NumberFormatException e) {
            throw new CS2113BotException("The task number must be a whole number.");
        }

        printDivider();
    }

    public static void handleTodo(String line) throws CS2113BotException {
        try {
            String arguments = line.split(" ", 2)[1];
            String description = arguments.trim();
            if (description.isEmpty()) {
                throw new CS2113BotException("Todo description cannot be empty! -.-");
            }
            validateTaskField(description, "Todo description");

            Todo newTodo = new Todo(description);
            taskList.add(newTodo);

            try {
                saveTasks();
            } catch (CS2113BotException e) {
                taskList.remove(taskList.size() - 1); // Rollback
                throw e;
            }
            printTaskAdded(newTodo);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CS2113BotException("Todo's argument is empty hence there is nothing to do? -.-");
        }
    }

    public static void handleDeadline(String line) throws CS2113BotException {
        try {
            String arguments = line.split(" ", 2)[1];
            try {
                String[] parts = arguments.split(" /by ", 2);
                String description = parts[0].trim();
                String by = parts[1].trim();
                if (description.isEmpty()) {
                    throw new CS2113BotException("Deadline description cannot be empty!");
                }
                if (by.isEmpty()) {
                    throw new CS2113BotException("Deadline date cannot be empty!");
                }
                validateTaskField(description, "Deadline description");
                validateTaskField(by, "Deadline date");

                Deadline newDeadline = new Deadline(description, by);
                taskList.add(newDeadline);

                try {
                    saveTasks();
                } catch (CS2113BotException e) {
                    taskList.remove(taskList.size() - 1);
                    throw e;
                }
                printTaskAdded(newDeadline);
            } catch (ArrayIndexOutOfBoundsException e) {
                if (!arguments.contains(" /by ")) {
                    throw new CS2113BotException("Missing '/by' flag! Format: deadline <desc> /by <date>");
                } else {
                    throw new CS2113BotException("Deadline date is not created! -.-");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CS2113BotException("Deadline's argument is empty? -.-");
        }
    }

    public static void handleEvent(String line) throws CS2113BotException {
        try {
            String arguments = line.split(" ", 2)[1];
            try {
                String[] parts = arguments.split(" /from | /to ", 3);
                String description = parts[0].trim();
                String from = parts[1].trim();
                String to = parts[2].trim();
                if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new CS2113BotException("Event details cannot be empty!");
                }
                validateTaskField(description, "Event description");
                validateTaskField(from, "Event start time");
                validateTaskField(to, "Event end time");

                Event newEvent = new Event(description, from, to);
                taskList.add(newEvent);

                try {
                    saveTasks();
                } catch (CS2113BotException e) {
                    taskList.remove(taskList.size() - 1);
                    throw e;
                }
                printTaskAdded(newEvent);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new CS2113BotException("Event format invalid! Format: event <desc> /from <start> /to <end>");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CS2113BotException("Event's argument is empty. -.-");
        }
    }

    public static void deleteEvent(String line) throws CS2113BotException {
        String[] words = line.trim().split("\\s+");
        if (words.length != 2) {
            throw new CS2113BotException("Please use: delete <task number>");
        }
        try {
            int taskNumber = Integer.parseInt(words[1]);
            if (taskNumber < 1 || taskNumber > taskList.size()) {
                throw new CS2113BotException("There is no task numbered " + taskNumber + ".");
            }
            int taskIndex = taskNumber - 1;
            Task deletedTask = taskList.remove(taskIndex);

            try {
                saveTasks();
            } catch (CS2113BotException e) {
                taskList.add(taskIndex, deletedTask);
                throw e;
            }

            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + deletedTask);
            System.out.println("Now you have " + taskList.size() + " tasks in the list.");
        } catch (NumberFormatException e) {
            throw new CS2113BotException("The task number must be a whole number.");
        }
        printDivider();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            readTasks(DATA_FILE.toString());
        } catch (CS2113BotException e) {
            System.out.println(e.getMessage());
        }
        printWelcome();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.equalsIgnoreCase("bye")) {
                printExit();
                break;
            }

            printDivider();
            String[] commandParts = line.split(" ", 2);
            String command = commandParts[0].toLowerCase();
            try {
                switch (command) {
                    case "list":
                        handleList();
                        break;
                    case "mark":
                        handleMark(line, true);
                        break;
                    case "unmark":
                        handleMark(line, false);
                        break;
                    case "todo":
                        handleTodo(line);
                        break;
                    case "deadline":
                        handleDeadline(line);
                        break;
                    case "event":
                        handleEvent(line);
                        break;
                    case "delete":
                        deleteEvent(line);
                        break;
                    default:
                        System.out.println("Idk what you typed: " + line);
                        printDivider();
                        break;
                }
            } catch (CS2113BotException e) {
                System.out.println(e.getMessage());
                printDivider();
            }
        }
        scanner.close();
    }
}
