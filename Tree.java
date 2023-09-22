import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

public class Tree {

    StringBuilder entries;

    public Tree() {

        entries = new StringBuilder();
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

    }

    public void add(String components) throws Exception {

        String type = components.substring(0, components.indexOf(":"));

        if (!entries.toString().contains(components)) {
            if (entries.isEmpty()) {
                entries.append(components);
            } else {
                entries.append("\n");
                entries.append(components);
            }
        }

    }

    public void remove(String components) throws Exception {

        String str = entries.toString();
        StringBuilder afterRemove = new StringBuilder();

        Scanner scanner = new Scanner(str);

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            if (!line.contains(components)) {
                afterRemove.append(line);
            }
        }

        scanner.close();

        entries = afterRemove;
        
    }

    public String writeToFile() throws Exception {

        String hash = getSHA1fromString(entries.toString());
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + hash)));

        printWriter.print(entries.toString());

        printWriter.close();

        return hash;
    }

    public String getEntries() {
        return entries.toString();
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
