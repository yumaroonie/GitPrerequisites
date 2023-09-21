import java.io.File;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Scanner;

public class Commit {
    String hashOfTree;
    String prevHash;
    String nextHash;
    String author;
    String date;
    String summary;

    public Commit(String sha1, String author, String summary) throws Exception {
        this.hashOfTree = createTree();
        this.prevHash = sha1;
        this.nextHash = "";
        this.author = author;
        this.date = "";
        this.summary = summary;
    }

    public Commit(String author, String summary) throws Exception {
        this("", author, summary);
    }

    public String createTree() throws Exception {
        Tree tree = new Tree();
        return tree.writeToFile();
    }

    public void getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toString();
    }

    public String getSHA1fromString(String myString) throws Exception {
        // hashes file with SHA1 hash code into String called SHA1
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(myString.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest()) {
            formatter.format("%02x", b);
        }
        String SHA1 = formatter.toString();
        formatter.close();
        return SHA1;
    }
}
