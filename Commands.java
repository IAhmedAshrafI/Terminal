import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Commands {

    public String cdCommand(String[] args) {
        if (args.length == 0) {
            // Case 1: 'cd' with no arguments - change to the home directory.
            return (System.getProperty("user.dir"));
        } else if (args.length == 1 || args.length == 2 || args.length == 3) {
            if (args[0].equals("..")) {
                // Case 2: 'cd ..' - change to the previous directory.
                String currentDir = System.getProperty("user.dir");
                File currentDirectory = new File(currentDir);
                String parentDir = currentDirectory.getParent();
                if (parentDir != null) {
                    System.setProperty("user.dir", parentDir);
                    return (parentDir);

                } else {
                    return ("Already at the root directory.");
                }
            } else {
                // Case 3: 'cd <path>' - change to the specified directory.
                File newDir = new File(args[0]);
                if (newDir.isAbsolute()) {
                    if (newDir.isDirectory()) {
                        System.setProperty("user.dir", newDir.getAbsolutePath());
                        return (newDir.toString());
                    } else {
                        return ("Not a directory: " + args[0]);
                    }
                } else {
                    String currentDir = System.getProperty("user.dir");
                    File targetDir = new File(currentDir, args[0]);
                    if (targetDir.isDirectory()) {
                        System.setProperty("user.dir", targetDir.getAbsolutePath());
                        return (targetDir.toString());
                    } else {
                        return ("Directory not found: " + args[0]);
                    }
                }
            }
        } else {
            return ("Invalid usage. Usage: cd [<path>|..]");
        }
    }

    public String lsCommand(String[] args) {
        File currentDir = new File(System.getProperty("user.dir"));
        File[] files = currentDir.listFiles();
        String ans = "";

        if (args.length == 0) {
            // Case 1: 'ls' - list contents of the current directory alphabetically.
            if (files != null) {
                Arrays.sort(files);
                for (File file : files) {
                    // System.out.println(file.getName());
                    ans += (file.getName() + " \n");
                }
            }

        } else if (args.length == 1 && args[0].equals("-r")) {
            // Case 2: 'ls -r' - list contents of the current directory in reverse order.
            if (files != null) {
                Arrays.sort(files, (f1, f2) -> f2.getName().compareTo(f1.getName()));
                for (File file : files) {
                    // System.out.println(file.getName());
                    ans += (file.getName() + " \n");
                }

            }
        } else {
            // System.out.println("Invalid usage. Usage: ls [-r]");
            ans = "Invalid usage. Usage: ls [-r]";
        }

        return ans;
    }

    public String touchCommand(String fileName) {
        File currentDir = new File(fileName);
        try {
            if (!currentDir.exists()) {
                boolean fileCreated = currentDir.createNewFile();
                if (fileCreated) {
                    return ("Creating file: " + fileName);
                } else {
                    return ("The file is not created.");
                }
            } else {
                return ("The file is already created.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        }
    }

    public String cpCommand(String source, String destination) {
        // if the destination file doesn't exists, create a new one, but if soucre doesn't exists, return void
        File start = new File(source), end = new File(destination);
        Path src = Paths.get(source), des = Paths.get(destination);
        if (!start.exists()){
            return "The Source File doesn't exists.";
        } else if (!end.exists()) {
            try {
                end.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error creating the destination file: " + e.getMessage();
            }
        }
        try {
            Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
            return "Created Successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error !";
        }
    }

    public String cprCommand(File source, File destination) {
        // if the destination file doesn't exists, create a new one, but if soucre doesn't exists, return void
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
                return "";
            } else {
                Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return "Successfully";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String rmCommand(String[] args) {

        switch (args.length) {
            case 1:
                File file = new File(args[0]);

                if (file.exists()) {
                    if (file.delete()) {
                        return ("File Deleted .");
                    }

                    else {
                        return ("Failed to Delete !");
                    }
                }

                else {
                    return ("No Such File Exists !");
                }

            default:
                return ("Invalid Usage ! Usage : rm <filename>");

        }

    }

    public String catCommand(String[] args) {

        String ansStr = "";

        switch (args.length) {
            case 1:

                File file = new File(args[0]);

                try {
                    List<String> ans = Files.readAllLines(Paths.get(args[0]));

                    for (String line : ans)
                        ansStr += (line);
                    return ansStr;
                }

                catch (IOException e) {

                    if (e instanceof NoSuchFileException)
                        ansStr = ("No Such File exists !");

                    return ansStr;
                }

            case 2:

                String[] files = { args[0], args[1] };
                List<String> ans = new ArrayList<String>();

                try {

                    for (String obj : files) {
                        ans.addAll(Files.readAllLines(Paths.get(obj)));
                    }

                    for (String line : ans)
                        ansStr += (line);

                    return ansStr;
                }

                catch (IOException e) {

                    if (e instanceof NoSuchFileException)
                        ansStr = ("No Such Files Exist !");

                    return ansStr;

                }

            default:

                return ("Invalid Usage ! Usage : cat <file_name> <file_name>(optional)");

        }

    }

    public String wcCommand(String[] args) {

        String ansStr = "";

        switch (args.length) {

            case 1:

                File file = new File(args[0]);

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

                    ansStr = (lineCount + " " + wordCount + " " + characterCount + " " + args[0]);

                    return ansStr;

                }

                catch (IOException e) {
                    e.printStackTrace();
                    if (e instanceof NoSuchFileException)
                        ansStr = "File Not Found !";
                    return ansStr;
                }

            default:
                return ("Invalid Usage ! Usage : wc <filename>");

        }
    }

    public String mkdir(String[] newDirs) {
        String targetPath = newDirs[newDirs.length - 1]; // Get the last element as the target path
        File targetDir = new File(targetPath);
        String result = "";

        // full path not given
        if (!targetDir.exists()) {
            String currentDir = System.getProperty("user.dir");

            for (String dir : newDirs) {
                result += createDirectory(currentDir + File.separator + dir);
            }
        } else { // if full path is given
            for (int i = 0; i < newDirs.length - 1; i++) {
                result += createDirectory(targetPath + File.separator + newDirs[i]);
            }
        }

        return result;
    }

    private String createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        String message = "";

        if (directory.exists()) {
            message = "Directory already exists: " + directory.getAbsolutePath() + "\n";
        } else {
            boolean created = directory.mkdir();
            if (created) {
                message = "";
            } else {
                message = "Failed to create directory: " + directory.getAbsolutePath() + "\n";
            }
        }

        return message;
    }

}
