import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Terminal extends Commands {
    private Parser parser;
    private List<String> commandList;

    public Terminal() {
        this.parser = new Parser();
        this.commandList = new ArrayList<>();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a command: ");
            String input = scanner.nextLine();

            if (input.equals("exit")) {
                break;
            }

            if (parser.parse(input)) {
                chooseCommandAction();
            } else {
                System.out.println("Invalid command.");
            }
        }

        scanner.close();
    }

    public void chooseCommandAction() {
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();

        if (commandName.equals("echo")) {
            if (args.length == 1) {
                System.out.println(args[0]);
            } else {
                System.out.println("Invalid usage. Usage: echo <text>");
            }
        } else if (commandName.equals("pwd")) {
            System.out.println(System.getProperty("user.dir"));
        } else if (commandName.equals("cd")) {
            cdCommand(args);
        } else if (commandName.equals("ls")) {
            lsCommand(args);
        } else if (commandName.equals("touch")) {
            touchCommand(parser.getArgs()[0]);
        } else if (commandName.equals("cp") && args[0].equals("-r")) {
            cprCommand((new File (parser.getArgs()[0])), new File(parser.getArgs()[1]));
        } else if (commandName.equals("cp")) {
            cpCommand(parser.getArgs()[1], parser.getArgs()[2]);
        } else if (commandName.equals("mkdir")) {
            mkdir(parser.getArgs()[1]);
        } else if (commandName.equals("history")) {
            history();
        } else {
            System.out.println("Command not found: " + commandName);
        }

        // Add the command to the history
        String command = commandName + " " + String.join(" ", args);
        commandList.add(command);
    }

    public void history() {
        for (int i = 0; i < commandList.size(); i++) {
            System.out.println((i + 1) + " " + commandList.get(i));
        }
    }
}