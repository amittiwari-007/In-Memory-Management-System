import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class File {
    private String content;

    public File() {
        this.content = "";
    }

    public String getContent() {
        return content;
    }

    public void writeContent(String content) {
        this.content = content;
    }

    public void displayContent() {
        System.out.println(content);
    }

    public void search(String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}