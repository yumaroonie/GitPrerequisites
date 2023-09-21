import java.io.File;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

public class Commit {
    String hashOfTree;
    String prevHash;
    String nextHash;
    String author;
    String date;
    String summary;

    public Commit(String sha1, String author, String date, String summary) throws Exception {
        Tree tree = new Tree();
        this.hashOfTree = tree.writeToFile();
        this.prevHash = sha1;
        this.nextHash = "";
        this.author = author;
        this.date = date;
        this.summary = summary;
    }

    public Commit(String author, String date, String summary) throws Exception {
        this("", author, date, summary);
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
