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

    public static void handleMark(String line, boolean isDone) {
        String[] words = line.split(" ");
        int taskIndex = Integer.parseInt(words[1]) - 1;
        taskList[taskIndex].setDone(isDone);

        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + taskList[taskIndex]);
        printDivider();
    }

    public static void handleTodo(String arguments) {
        Todo newTodo = new Todo(arguments.trim());
        taskList[taskCount++] = newTodo;
        printTaskAdded(newTodo);
    }

    public static void handleDeadline(String arguments) {
        String[] parts = arguments.split(" /by ", 2);
        String description = parts[0].trim();
        String by = parts[1].trim();

        Deadline newDeadline = new Deadline(description, by);
        taskList[taskCount++] = newDeadline;
        printTaskAdded(newDeadline);
    }

    public static void handleEvent(String arguments) {
        String[] parts = arguments.split(" /from | /to ", 3);
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();

        Event newEvent = new Event(description, from, to);
        taskList[taskCount++] = newEvent;
        printTaskAdded(newEvent);
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
            String arguments = commandParts.length > 1 ? commandParts[1] : "";

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
                    handleTodo(arguments);
                    break;
                case "deadline":
                    handleDeadline(arguments);
                    break;
                case "event":
                    handleEvent(arguments);
                    break;
                default:
                    System.out.println("Unknown command: " + line);
                    printDivider();
                    break;
            }
        }
        scanner.close();
    }
}
