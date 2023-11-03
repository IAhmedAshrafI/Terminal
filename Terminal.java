import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Terminal extends Commands {
  private Parser parser;
  private List<String> commandList;

  public Terminal() {
    this.parser = new Parser();
    this.commandList = new ArrayList<>();
  }

  public void run() {
    while (true) {
      System.out.print("Enter a command: ");
      String input = System.console().readLine();

      if (input.equals("exit")) {
        break;
      }

      if (parser.parse(input)) {
        int index = Arrays.asList(parser.getArgs()).indexOf(">");
        int index2 = Arrays.asList(parser.getArgs()).indexOf(">>");
        if (index != -1) {
          String[] copy = Arrays.copyOfRange(parser.getArgs(), index + 1, parser.getArgs().length);
          String[] main = Arrays.copyOfRange(parser.getArgs(), 0, index);
          System.out.println(copy[0]);
          String commandAns = chooseCommandAction(main, input);
          operator1Command(commandAns, copy);

        } else if (index2 != -1) {
          String[] copy = Arrays.copyOfRange(parser.getArgs(), index2 + 1, parser.getArgs().length);
          String[] main = Arrays.copyOfRange(parser.getArgs(), 0, index2);
          System.out.println(copy[0]);
          String commandAns = chooseCommandAction(main, input);
          operator2Command(commandAns, copy);
        } else {
          String commandAns = chooseCommandAction(parser.getArgs(), input);
          System.out.println(commandAns);
        }
      }

      else {
        System.out.println("Invalid command.");
      }
    }
  }

  public void operator1Command(String commandAns, String[] copy) {
    if (copy.length == 1) {
      try {
        FileWriter file = new FileWriter(copy[0]);

        file.write(commandAns);

        file.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Invalid usage ! Usage : command > <filename>");
    }
  }

  public void operator2Command(String commandAns, String[] copy) {
    if (copy.length == 1) {
      try {
        File file = new File(copy[0]);
        if ((file.exists())) {
          FileWriter file2 = new FileWriter(copy[0]);
          file2.write(commandAns);
          file2.close();
        } else {
          System.out.println("No Such File Exists!");
        }
      }

      catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Invalid usage ! Usage : command >> <filename>");
    }
  }

  public String chooseCommandAction(String[] args, String input) {
    String commandName = parser.getCommandName();
    // String[] args = parser.getArgs();

    switch (commandName) {

      case "echo":
        if (args.length == 1) {
          commandList.add(input);
          return args[0];
        } else
          return ("Invalid usage. Usage: echo <text>");

      case "pwd":
        commandList.add(input);
        return (System.getProperty("user.dir"));

      case "cd":
        commandList.add(input);
        return cdCommand(args);

      case "ls":
        commandList.add(input);
        return lsCommand(args);

      case "touch":
        commandList.add(input);
        if (args.length == 0){return("Invalid usage. Usage: touch <file_name>");}
        return touchCommand(parser.getArgs()[0]);

      case "cp":

        if (args.length == 2) {
          commandList.add(input);
          cpCommand(args[0], args[1]);
        }

        else if (args.length == 3 && args[0].equals("-r")) {
          commandList.add(input);
          cprCommand(new File(args[1]), new File(args[2]));
        }

        else {
          System.out.println("Invalid usage. Usage: cp <file_name> <file_name> || cp -r <folder_name> <folder_name>");
        }

      case "rm":
        commandList.add(input);
        return rmCommand(args);

      case "cat":
        commandList.add(input);
        return catCommand(args);

      case "wc":
        commandList.add(input);
        return wcCommand(args);

      case "mkdir":
        commandList.add(input);
        return mkdir(args);

      case "history":
        commandList.add(input);
        return history();

      default:
        return ("Command Not Found !" + commandName);

    }

  }

  public String history() {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < commandList.size(); i++) {
      result.append((i + 1)).append(" ").append(commandList.get(i)).append("\n");
    }
    return result.toString();
  }
}
