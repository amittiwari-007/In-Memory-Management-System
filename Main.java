
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.io.*;
public class Main  {

    // will be saving the current directory
    private String currentDirectory;
    //will be saving the file system mapping with the directories
    private Map<String, Directory> fileSystem;

    public Main() {
        this.currentDirectory = "/";
        this.fileSystem = new HashMap<>();
        this.fileSystem.put("/", new Directory());
    }

    public void mkdir(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        createDirectories(path);
    }


    public void cd(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }

        String[] dirs = path.split("/");
        Directory current = fileSystem.get("/");

        int i=0;
        for (String dir : dirs) {
            if (!dir.isEmpty()) {
                i++;
                if (dir.equals("..")) {
                    // Move to the parent directory
                    if (!currentDirectory.equals("/")) {
                        String temp=dirs[i-1]+dirs[i];
                        int n = temp.length();
                        int l = path.length();
                        path=path.substring(0,l-n-3);
                        if(path.equals(""))path="/";
                    }
                    else{
                        path="/";
                        System.out.println("This is the root directory");
                    }
                } else {
                    Directory next = current.getDirectory(dir);
                    if (next != null) {
                        current = next;
                    } else {
                        System.out.println("Directory not found: " + path);
                        return; // Exit the loop and don't change the current directory
                    }
                }

            }
        }

        currentDirectory = path;

    }

    public void ls(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        Directory current = getDirectory(path);
        current.listContents();
    }

    public void grep(String pattern, String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        File file = getFile(path);
        if (file != null) {
            file.search(pattern);
        }
    }

    public void cat(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        File file = getFile(path);
        if (file != null) {
            file.displayContent();
        }
    }

    public void touch(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        String[] dirsAndFileName = path.split("/");
        String fileName = dirsAndFileName[dirsAndFileName.length - 1];
        String directoryPath =
                path.substring(0, path.length() - fileName.length());
        Directory directory = getDirectory(directoryPath);
        if (directory != null) {
            directory.createFile(fileName);
        }
    }

    public void echo(String content, String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        File file = getFile(path);
        if (file != null) {
            file.writeContent(content);
        }
    }

    public void mv(String source, String destination) {
        source = normalizePath(source);
        destination = normalizePath(destination);

        if (source.startsWith("./")) {
            // Treat it as a relative path
            source = currentDirectory + source.substring(1);
        } else if (!source.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            source = currentDirectory + "/" + source;
        }
        if (destination.startsWith("./")) {
            // Treat it as a relative path
            destination = currentDirectory + destination.substring(1);
        } else if (!destination.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            destination = currentDirectory + "/" + destination;
        }
        File file = getFile(source);
        if (file != null) {
            touch(destination);
            File destFile = getFile(destination);
            if (destFile != null) {
                destFile.writeContent(file.getContent());
                rm(source);
            }
        }
    }

    public void cp(String source, String destination) {
        source = normalizePath(source);
        destination = normalizePath(destination);
        if (source.startsWith("./")) {
            // Treat it as a relative path
            source = currentDirectory + source.substring(1);
        } else if (!source.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            source = currentDirectory + "/" + source;
        }
        if (destination.startsWith("./")) {
            // Treat it as a relative path
            destination = currentDirectory + destination.substring(1);
        } else if (!destination.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            destination = currentDirectory + "/" + destination;
        }

        File file = getFile(source);
        if (file != null) {
            touch(destination);
            File destFile = getFile(destination);
            if (destFile != null) {
                destFile.writeContent(file.getContent());
            }
        }
    }

    public void rm(String path) {
        path = normalizePath(path);
        if (path.startsWith("./")) {
            // Treat it as a relative path
            path = currentDirectory + path.substring(1);
        } else if (!path.startsWith("/")) {
            // Treat it as a relative path without a leading '/'
            path = currentDirectory + "/" + path;
        }
        Directory parentDirectory = getParentDirectory(path);
        String fileName = getFileName(path);
        if (parentDirectory != null && fileName != null) {
            parentDirectory.removeFile(fileName);
        }
    }

    private String normalizePath(String path) {
        path = path.replaceAll("/{2,}", "/");
        if (!path.startsWith("/")) {
            path =
                    currentDirectory.equals("/") ? currentDirectory +
                            path : currentDirectory + "/" + path;
        }
        return path;
    }

    private void createDirectories(String path) {
        String[] dirs = path.split("/");
        Directory current = fileSystem.get("/");
        for (String dir : dirs) {
            if (!dir.isEmpty()) {
                current = current.getOrCreateDirectory(dir);
            }
        }
    }

    private Directory getDirectory(String path) {
        String[] dirs = path.split("/");
        Directory current = fileSystem.get("/");
        for (String dir : dirs) {
            if (!dir.isEmpty()) {
                current = current.getDirectory(dir);
                if (current == null) {
                    System.out.println("Directory not found: " + path);
                    return null;
                }
            }
        }
        return current;
    }

    public File getFile(String path) {
        String fileName = getFileName(path);
        Directory parentDirectory = getParentDirectory(path);
        if (fileName != null && parentDirectory != null) {
            return parentDirectory.getFile(fileName);
        }
        return null;
    }

    private String getFileName(String path) {
        String[] dirsAndFileName = path.split("/");
        return dirsAndFileName[dirsAndFileName.length - 1];
    }

    private Directory getParentDirectory(String path) {
        String[] dirsAndFileName = path.split("/");
        String fileName = dirsAndFileName[dirsAndFileName.length - 1];
        String directoryPath =
                path.substring(0, path.length() - fileName.length());
        return getDirectory(directoryPath);
    }


    private static final String SAVE_FLAG = "--save-state";
    private static final String LOAD_FLAG = "--load-state";
    private static final String PATH_FLAG = "--path";


    public void runFileSystem(String[]args) {
        if (args.length > 0) {
            processCommandLineArgs(args);
        }

        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("In-Memory File System. Enter 'exit' to quit.");

        while (true) {
            System.out.print(currentDirectory + "> ");
            command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                System.out.println("Exiting the file system.");
                break;
            }

            processCommand(command);
        }

        scanner.close();
    }

    private void processCommandLineArgs(String[] args) {
        Map<String, String> flags = parseCommandLineArgs(args);
        String saveState = flags.get(SAVE_FLAG);
        String loadState = flags.get(LOAD_FLAG);
        String path = flags.get(PATH_FLAG);

        if (saveState != null && saveState.equals("true")) {
            saveStateToFile(path);
        } else if (loadState != null && loadState.equals("true")) {
            loadStateFromFile(path);
        }
    }

    private Map<String, String> parseCommandLineArgs(String[] args) {
        Map<String, String> flags = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            flags.put(args[i], args[i + 1]);
        }
        return flags;
    }

    private void saveStateToFile(String path) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(fileSystem);
            System.out.println("File system state saved to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving file system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadStateFromFile(String path) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            fileSystem = (Map<String, Directory>) inputStream.readObject();
            System.out.println("File system state loaded from: " + path);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading file system state: " + e.getMessage());
        }
    }

    private void processCommand(String command) {
        String[] tokens = command.split(" ");

        switch (tokens[0]) {
            case "mkdir":
                if (tokens.length > 1) {
                    mkdir(tokens[1]);
                } else {
                    System.out.println("Usage: mkdir <directory_path>");
                }
                break;
            case "cd":
                if (tokens.length > 1) {
                    cd(tokens[1]);
                } else {
                    System.out.println("Usage: cd <directory_path>");
                }
                break;
            case "ls":
                if (tokens.length > 1) {
                    ls(tokens[1]);
                } else {
                    ls(".");
                }
                break;
            case "grep":
                if (tokens.length > 2) {
                    grep(tokens[1], tokens[2]);
                } else {
                    System.out.println("Usage: grep <pattern> <file_path>");
                }
                break;
            case "cat":
                if (tokens.length > 1) {
                    cat(tokens[1]);
                } else {
                    System.out.println("Usage: cat <file_path>");
                }
                break;
            case "touch":
                if (tokens.length > 1) {
                    touch(tokens[1]);
                } else {
                    System.out.println("Usage: touch <file_path>");
                }
                break;
            case "echo":
                if (tokens.length > 2) {
                    echo(tokens[1], tokens[2]);
                } else {
                    System.out.println("Usage: echo <content> <file_path>");
                }
                break;
            case "mv":
                if (tokens.length > 2) {
                    mv(tokens[1], tokens[2]);
                } else {
                    System.out.println("Usage: mv <source_path> <destination_path>");
                }
                break;
            case "cp":
                if (tokens.length > 2) {
                    cp(tokens[1], tokens[2]);
                } else {
                    System.out.println("Usage: cp <source_path> <destination_path>");
                }
                break;
            case "rm":
                if (tokens.length > 1) {
                    rm(tokens[1]);
                } else {
                    System.out.println("Usage: rm <file_path>");
                }
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public static void main(String[] args) {
        Main fileSystem = new Main();
        fileSystem.runFileSystem(args);
    }
}
