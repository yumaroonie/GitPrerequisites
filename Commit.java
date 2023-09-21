import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
        this.date = getDate();
        this.summary = summary;
        String hash = createCommitHash();
        File file = new File("./objects/" + hash);
        file.createNewFile();
        FileWriter writer = new FileWriter (file);
        PrintWriter out = new PrintWriter(writer);
        out.print(hashOfTree + "\n" + prevHash + "\n" + nextHash + "\n" + author + "\n" + date + "\n" + summary);
        writer.close();
        out.close();
    }

    public Commit(String author, String summary) throws Exception {
        this("", author, summary);
    }

    public void setNextCommit(String nextCommitHash) throws Exception {
        this.nextHash = nextCommitHash;
        String hash = createCommitHash();
        File file = new File("./objects/" + hash);
        FileWriter writer = new FileWriter (file);
        PrintWriter out = new PrintWriter(writer);
        out.print(hashOfTree + "\n" + prevHash + "\n" + nextHash + "\n" + author + "\n" + date + "\n" + summary);
        writer.close();
        out.close();
    }

    public String createCommitHash() throws Exception {
        String content = hashOfTree + "\n" + prevHash + "\n" + "" + "\n" + author + "\n" + date + "\n" + summary;

        StringBuilder output = new StringBuilder();

        Scanner scanner = new Scanner(content);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            output.append(line);
        }

        scanner.close();

        return getSHA1fromString(output.toString());
    }

    public String createTree() throws Exception {
        Tree tree = new Tree();
        return tree.writeToFile();
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
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
