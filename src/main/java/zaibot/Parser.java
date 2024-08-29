package zaibot;

import zaibot.command.*;
import zaibot.exception.ZaibotException;

import java.util.HashMap;

/**
 * The Parser class is responsible for parsing the commands
 * and creating the Command object responsible for
 * executing the logic as intended.
 */
public class Parser {
    /**
     * This takes a command split by spaces, and processes them into separate commands
     * The Hashmap returned contains key-value pairs of {parameter name: argument value}
     *
     * @param command A command
     * @return A zaibot.command.Command with the parsed arguments.
     */
    public static Command parse(String command) throws ZaibotException {
        HashMap<String, String> arguments = new HashMap<>();

        if (command.indexOf(' ') == -1) {
            switch (command) {
                case "bye":
                    return new ExitCommand();
                case "list":
                    return new TaskListingCommand();
                default:
                    throw new ZaibotException("Are you missing arguments? Invalid command.");
            }

        }

        String commandName = command.substring(0, command.indexOf(' '));

        switch (commandName) {
        case "mark":
            // fallthrough
        case "unmark":
            // fallthrough
        case "delete":
            arguments.put("number", command.substring(command.indexOf(" ") + 1));
            return new TaskUpdateCommand(commandName, arguments);
        case "find":
            arguments.put("name", command.substring(command.indexOf(" ") + 1));
            return new TaskFindCommand(commandName, arguments);
        case "todo":
            // fallthrough
        case "deadline":
            // fallthrough
        case "event":
            String optionString = command.substring(command.indexOf(' '));
            String[] options = optionString.split(" /");

                if (optionString.isEmpty() || options[0].isEmpty()) {
                    throw new ZaibotException("Name cannot be empty.");
                }

                arguments.put("name", options[0].trim());

                for (int i = 1; i < options.length; i++) {
                    String option = options[i];
                    String optionName = option.substring(0, option.indexOf(' ')).trim();
                    String optionValue = option.substring(option.indexOf(' ')).trim();
                    if (optionValue.isEmpty()) {
                        throw new ZaibotException(String.format("Option %s cannot be empty.", optionName));
                    }
                    arguments.put(optionName, optionValue);
                }
                return new TaskAdditionCommand(commandName, arguments);
            default:
                throw new ZaibotException("Unknown command.");
        }
    }
}
