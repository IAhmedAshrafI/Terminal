import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public abstract class Commands {

    public void cdCommand(String[] args) {
        if (args.length == 0) {
            // Case 1: 'cd' with no arguments - change to the home directory.
            // String userHome = System.getProperty("user.home");
            System.out.println(System.getProperty("user.dir"));
        } else if (args.length == 1) {
            if (args[0].equals("..")) {
                // Case 2: 'cd ..' - change to the previous directory.
                String currentDir = System.getProperty("user.dir");
                File currentDirectory = new File(currentDir);
                String parentDir = currentDirectory.getParent();
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
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cpCommand(String source, String destination){
        Path src = Paths.get(source), des = Paths.get(destination);
        try{
            Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cprCommand(File source, File destination) {
        try{
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


    //problem while mkdir of a complete path
    public static void mkdir(String... arguments) {
        for (String argument : arguments) {
            Path directoryPath = Paths.get(argument);
            if (Files.isDirectory(directoryPath)) {
                createDirectory(directoryPath);
            } else {
                Path parentPath = directoryPath.getParent();
                if (parentPath != null) {
                    createDirectory(parentPath);
                }
                createDirectory(directoryPath);
            }
        }
    }

    private static void createDirectory(Path directoryPath) {
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
                System.out.println("Directory created: " + directoryPath.toAbsolutePath());
            } else {
                System.out.println("Directory already exists: " + directoryPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Failed to create directory: " + directoryPath.toAbsolutePath());
            e.printStackTrace();
        }
    }


}