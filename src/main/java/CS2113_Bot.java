import java.util.Scanner;

public class CS2113_Bot {
    private static final int MAX_TASKS = 100;
    private static final String DIVIDER = "__________________________________";
    private static final String BANNER =
            "_    _ ______ _      _      ____   __          __  {_} _____  _      _____  \n"
                    + "| |  | |  ____| |    | |    / __ \\  \\ \\        / / | |  __ \\| |    |  __ \\ \n"
                    + "| |__| | |__  | |    | |   | |  | |  \\ \\  /\\  / /  | | |__) | |    | |  | |\n"
                    + "|  __  |  __| | |    | |   | |  | |   \\ \\/  \\/ /   | |  _  /| |    | |  | |\n"
                    + "| |  | | |____| |____| |____| |__| |    \\  /\\  /    | | | \\ \\| |____| |__| |\n"
                    + "|_|  |_|______|______|______|\\____/      \\/  \\/     |_| |_| \\_\\______|_____/ ";

    private static Task[] taskList = new Task[MAX_TASKS];
    private static int taskCount = 0;

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
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printDivider();
    }

    public static void handleList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + taskList[i]);
        }
        printDivider();
    }

    public static void handleMark(String line, boolean isDone) throws CS2113BotException{
        String[] words = line.trim().split("\\s+");

        if (words.length != 2) {
            throw new CS2113BotException("Please use: mark <task number>");
        }

        try {
            int taskNumber = Integer.parseInt(words[1]);

            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new CS2113BotException("There is no task numbered " + taskNumber + ".");
            }

            int taskIndex = taskNumber - 1;
            taskList[taskIndex].setDone(isDone);

            if (isDone) {
                System.out.println("Nice! I've marked this task as done:");
            } else {
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + taskList[taskIndex]);
        } catch (NumberFormatException e) {
            throw new CS2113BotException("The task number must be a whole number.");
        }

        printDivider();
    }

    public static void handleTodo(String line) throws CS2113BotException {
        if (taskCount >= MAX_TASKS) {
            throw new CS2113BotException("Your task list is full.");
        }
        try {
            String arguments = line.split(" ", 2)[1];
            if (arguments.trim().isEmpty()) {
                throw new CS2113BotException("Todo description cannot be empty! -.-");
            }
            Todo newTodo = new Todo(arguments.trim());
            taskList[taskCount++] = newTodo;
            printTaskAdded(newTodo);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CS2113BotException("Todo's argument is empty hence there is nothing to do? -.-");
        }
    }

    public static void handleDeadline(String line) throws CS2113BotException{
        if (taskCount >= MAX_TASKS) {
            throw new CS2113BotException("Your task list is full.");
        }
        try {
            // Step 1: Try splitting the command from the arguments
            String arguments = line.split(" ", 2)[1];
            try {
                // Step 2: Try splitting by /by
                String[] parts = arguments.split(" /by ", 2);
                String description = parts[0].trim();
                String by = parts[1].trim();
                if (description.isEmpty()) {
                    throw new CS2113BotException("Deadline description cannot be empty!");
                }
                if (by.isEmpty()) {
                    throw new CS2113BotException("Deadline date cannot be empty!");
                }
                Deadline newDeadline = new Deadline(description, by);
                taskList[taskCount++] = newDeadline;
                printTaskAdded(newDeadline);
            } catch (ArrayIndexOutOfBoundsException e) {
                // Fails here if "/by" is missing or nothing comes after "/by"
                if (!arguments.contains(" /by ")) {
                    throw new CS2113BotException("Missing '/by' flag! Format: deadline <desc> /by <date>");
                } else {
                    throw new CS2113BotException("Deadline date is not created! -.-");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Fails here if input was just "deadline"
            throw new CS2113BotException("Deadline's argument is empty? -.-");
        }
    }

    public static void handleEvent(String line) throws CS2113BotException{
        if (taskCount >= MAX_TASKS) {
            throw new CS2113BotException("Your task list is full.");
        }
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
                Event newEvent = new Event(description, from, to);
                taskList[taskCount++] = newEvent;
                printTaskAdded(newEvent);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new CS2113BotException("Event format invalid! Format: event <desc> /from <start> /to <end>");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CS2113BotException("Event's argument is empty. -.-");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            //String arguments = commandParts[1];
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
