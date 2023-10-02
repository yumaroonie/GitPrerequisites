import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

public class Tree {

    private StringBuilder entries;
    private String ultimateTreeSHA1String;

    public Tree() {

        entries = new StringBuilder();
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

    }

    public void add(String components) throws Exception {

        //String type = components.substring(0, components.indexOf(":"));

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

    public String addDirectory (String directoryPath) throws Exception
    {
        File mainDirectory = new File (directoryPath);
        if (!mainDirectory.exists ())
        {
            throw new Exception ("Invalid directory; the specified directory does not exist.");
        }
        if (!mainDirectory.canRead ())
        {
            throw new Exception ("Invalid directory; the specified directory exists but is not readable.");
        }
        Index i = new Index ();
        i.initialize(".");
        Tree myTree = new Tree ();
        for (String fileOrDirPath : mainDirectory.list ())
        {
            String fullPath = directoryPath + "/" + fileOrDirPath;
            File currentFileOrDir = new File (fullPath);
            if (currentFileOrDir.isFile ())
            {
                i.indexAddFile (fullPath);
                myTree.add ("blob : " + i.getNameAndEntryMap ().get (fullPath).getSHA1 () + " : " + fileOrDirPath);
            }
            else
            {
                Tree childTree = new Tree ();
                myTree.add ("tree : " + childTree.addDirectory (fullPath) + " : " + fileOrDirPath);
            }
        }
        ultimateTreeSHA1String = myTree.writeToFile ();
        return ultimateTreeSHA1String;
    }

    public String getUltimateTreeSHA1String ()
    {
        return ultimateTreeSHA1String;
    }

    public String getEntriesToString ()
    {
        return entries.toString ();
    }
}
