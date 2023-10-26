import java.util.Arrays;
import java.io.File;

public class Terminal {
    private Parser parser;

    public Terminal() {
        this.parser = new Parser();
    }

    public void run() {
        while (true) {
            System.out.print("Enter a command: ");
            String input = System.console().readLine();

            if (input.equals("exit")) {
                break;
            }

            if (parser.parse(input)) {
                chooseCommandAction();
            } else {
                System.out.println("Invalid command.");
            }
        }
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
        } else {
            System.out.println("Command not found: " + commandName);
        }
    }

    public void cdCommand(String[] args) {
        if (args.length == 0) {
            // Case 1: 'cd' with no arguments - change to the home directory.
            String userHome = System.getProperty("user.home");
            System.out.println(System.setProperty("user.dir", userHome));
        } else if (args.length == 1) {
            if (args[0].equals("..")) {
                // Case 2: 'cd ..' - change to the previous directory.
                String currentDir = System.getProperty("user.dir");
                File currentDirectory = new File(currentDir);
                String parentDir = currentDirectory.getParent();
                System.out.println(parentDir);
                if (parentDir != null) {
                    System.out.println(System.setProperty("user.dir", parentDir));
                } else {
                    System.out.println("Already at the root directory.");
                }
            } else {
                // Case 3: 'cd <path>' - change to the specified directory.
                File newDir = new File(args[0]);
                if (newDir.isAbsolute()) {
                    if (newDir.isDirectory()) {
                        System.out.println(System.setProperty("user.dir", newDir.getAbsolutePath()));
                    } else {
                        System.out.println("Not a directory: " + args[0]);
                    }
                } else {
                    String currentDir = System.getProperty("user.dir");
                    File targetDir = new File(currentDir, args[0]);
                    if (targetDir.isDirectory()) {
                        System.out.println(System.setProperty("user.dir", targetDir.getAbsolutePath()));
                    } else {
                        System.out.println("Directory not found: " + args[0]);
                    }
                }
            }
        } else {
            System.out.println("Invalid usage. Usage: cd [<path>|..]");
        }
    }

    public void lsCommand(String[] args) {
        File currentDir = new File(System.getProperty("user.dir"));
        File[] files = currentDir.listFiles();

        if (args.length == 0) {
            // Case 1: 'ls' - list contents of the current directory alphabetically.
            if (files != null) {
                Arrays.sort(files);
                for (File file : files) {
                    System.out.println(file.getName());
                }
            }
        } else if (args.length == 1 && args[0].equals("-r")) {
            // Case 2: 'ls -r' - list contents of the current directory in reverse order.
            if (files != null) {
                Arrays.sort(files, (f1, f2) -> f2.getName().compareTo(f1.getName()));
                for (File file : files) {
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("Invalid usage. Usage: ls [-r]");
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        terminal.run();
    }
}
