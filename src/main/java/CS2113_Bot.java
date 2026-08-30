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

    public static void printWelcomeMessage() {
        printDivider();
        System.out.println(BANNER);
        System.out.println("\nHello! I'm CS2113_Bot.");
        System.out.println("What can I do for you?");
        printDivider();
    }

    public static void printExitMessage() {
        printDivider();
        System.out.println("Bye. Hope to see you again soon!");
        printDivider();
    }

    public static void handleList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ".[" + taskList[i].getStatusIcon() + "] "
                    + taskList[i].getDescription());
        }
        printDivider();
    }

    public static void handleMark(String line) {
        String[] words = line.split(" ");
        int taskIndex = Integer.parseInt(words[1]) - 1;
        boolean isMark = words[0].equalsIgnoreCase("mark");

        taskList[taskIndex].setDone(isMark);
        if (isMark) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("[" + taskList[taskIndex].getStatusIcon() + "] "
                + taskList[taskIndex].getDescription());
        printDivider();
    }

    public static void addTask(String description) {
        System.out.println(description);
        printDivider();
        taskList[taskCount] = new Task(description);
        taskCount++;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printWelcomeMessage();

        String line = scanner.nextLine().trim();
        while (!line.equalsIgnoreCase("bye")) {
            printDivider();
            if (line.equalsIgnoreCase("list")) {
                handleList();
            } else if (line.toLowerCase().startsWith("mark ") || line.toLowerCase().startsWith("unmark ")) {
                handleMark(line);
            } else {
                addTask(line);
            }
            line = scanner.nextLine().trim();
        }

        printExitMessage();
        scanner.close();
    }
}
