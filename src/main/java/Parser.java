/**
 * Parses user input commands and arguments.
 */
public class Parser {

    public static String getCommandWord(String fullCommand) {
        String[] parts = fullCommand.trim().split(" ", 2);
        return parts[0].toLowerCase();
    }

    public static String getArguments(String fullCommand) {
        String[] parts = fullCommand.trim().split(" ", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }

    public static int parseTaskNumber(String arguments, String commandName) throws CS2113BotException {
        String[] words = arguments.split("\\s+");
        if (arguments.isEmpty() || words.length != 1) {
            throw new CS2113BotException("Please use: " + commandName + " <task number>");
        }
        try {
            return Integer.parseInt(words[0]);
        } catch (NumberFormatException e) {
            throw new CS2113BotException("The task number must be a whole number.");
        }
    }
}

