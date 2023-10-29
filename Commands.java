import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Commands {

    public void cdCommand(String[] args) {
        if (args.length == 0) {
            // Case 1: 'cd' with no arguments - change to the home directory.
            System.out.println(System.getProperty("user.dir"));
        } else if (args.length == 1 || args.length == 2 || args.length == 3) {
            if (args[0].equals("..")) {
                // Case 2: 'cd ..' - change to the previous directory.
                String currentDir = System.getProperty("user.dir");
                File currentDirectory = new File(currentDir);
                String parentDir = currentDirectory.getParent();
                if (parentDir != null) {
                    System.out.println(parentDir);
                    System.setProperty("user.dir", parentDir);
                } else {
                    System.out.println("Already at the root directory.");
                }
            } else {
                // Case 3: 'cd <path>' - change to the specified directory.
                File newDir = new File(args[0]);
                if (newDir.isAbsolute()) {
                    if (newDir.isDirectory()) {
                        System.out.println(newDir);
                        System.setProperty("user.dir", newDir.getAbsolutePath());
                    } else {
                        System.out.println("Not a directory: " + args[0]);
                    }
                } else {
                    String currentDir = System.getProperty("user.dir");
                    File targetDir = new File(currentDir, args[0]);
                    if (targetDir.isDirectory()) {
                        System.out.println(targetDir);
                        System.setProperty("user.dir", targetDir.getAbsolutePath());
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

    public void touchCommand(String fileName) {
        File currentDir = new File(fileName);
        try {
            if (!currentDir.exists()) {
                boolean fileCreated = currentDir.createNewFile();
                if (fileCreated) {
                    System.out.println("Creating file: " + fileName);
                } else {
                    System.out.println("The file is not created.");
                }
            } else {
                System.out.println("The file is already created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cpCommand(String source, String destination) {
        Path src = Paths.get(source), des = Paths.get(destination);
        try {
            Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cprCommand(File source, File destination) {
        try {
            if (source.isDirectory()) {
                if (!destination.exists()) {
                    destination.mkdirs();
                }

                String[] files = source.list();
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);
                    cprCommand(srcFile, destFile);
                }
            } else {
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rmCommand(String[] args) {

        switch (args.length) {
            case 1:
                File file = new File(args[0]);

                if (file.delete()) {
                    System.out.println("File Deleted .");
                }

                else {
                    System.out.println("Failed to Delete !");
                }

                break;

            default:
                System.out.println("Invalid Usage ! Usage : rm <filename>");
                break;
        }

    }

    public void catCommand(String[] args) {

        switch (args.length) {

            case 1:

                File file = new File(args[0]);

                if (file.exists()) {

                    try {

                        List<String> ans = Files.readAllLines(Paths.get(args[0]));

                        for (String line : ans)
                            System.out.println(line);

                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    System.out.println("File Not Found !");
                }

                break;

            case 2:

                String[] files = { args[0], args[1] };
                List<String> ans = new ArrayList<String>();

                try {

                    for (String obj : files) {
                        ans.addAll(Files.readAllLines(Paths.get(obj)));
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                for (String line : ans)
                    System.out.println(line);

                break;

            default:

                System.out.println("Invalid Usage ! Usage : cat <file_name> <file_name>(optional)");

                break;
        }

    }

    public void wcCommand(String[] args) {

        switch (args.length) {

            case 1:

                File file = new File(args[0]);

                if (file.exists()) {

                    int lineCount = 0, wordCount = 0, characterCount = 0;

                    try {
                        List<String> ans = Files.readAllLines(Paths.get(args[0]));
                        lineCount = ans.size();

                        for (String line : ans) {
                            String[] words = line.trim().split(" ");
                            wordCount += words.length;
                            for (String word : words)
                                characterCount += word.length();
                        }

                        System.out.println(lineCount + " " + wordCount + " " + characterCount + " " + args[0]);

                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File Not Found !");
                }

                break;

            default:
                System.out.println("Invalid Usage ! Usage : wc <filename>");

        }
    }

}