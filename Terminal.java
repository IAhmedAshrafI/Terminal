import java.io.File;
public class Terminal extends Commands {
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
        } else if (commandName.equals("touch")){
            // if (args.length == 1){
            //     System.out.println("Invalid usage. Usage: touch <file_name>");
            // } else {
                touchCommand(parser.getArgs()[0]);
            // }
        } else if (commandName.equals("cp")){
            if (args.length == 2){
                cpCommand(args[0], args[1]);
            } else if (args.length == 3 && args[0].equals("-r")){
                cprCommand(new File(args[1]), new File(args[2]));
            } else {
                System.out.println("Invalid usage. Usage: touch <file_name>");
            }
        }
        
        else if(commandName.equals("rm")){
             rmCommand(args);
        }

        else if(commandName.equals("cat")){
            catCommand(args);
        }

        else if(commandName.equals("wc")){
            wcCommand(args);
        }
        
        
        else {
            System.out.println("Command not found: " + commandName);
        }
    }
}
