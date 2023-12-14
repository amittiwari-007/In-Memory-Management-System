import java.util.HashMap;
import java.util.Map;
public class Directory {
    public Map<String, Directory> directories;
    public Map<String, File> files;

    public Directory() {
        this.directories = new HashMap<>();
        this.files = new HashMap<>();
    }

    public Directory getOrCreateDirectory(String name) {
        return directories.computeIfAbsent(name, k -> new Directory());
    }

    public Directory getDirectory(String name) {
        return directories.get(name);
    }

    public void listContents() {
        System.out.println("Directories:");
        System.out.println(String.join(" ", directories.keySet()));
        System.out.println("Files:");
        System.out.println(String.join(" ", files.keySet()));
    }

    public File getFile(String name) {
        return files.get(name);
    }

    public void createFile(String name) {
        files.put(name, new File());
    }

    public void removeFile(String name) {
        files.remove(name);
    }
}

