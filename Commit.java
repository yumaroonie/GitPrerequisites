import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class Commit {
    String hashOfTree;
    String prevHash;
    String nextHash;
    String author;
    String date;
    String summary;

    public Commit(String sha1, String author, String summary) throws Exception {
        this.prevHash = sha1;
        this.hashOfTree = createTree();
        this.nextHash = "";
        this.author = author;
        this.date = getDate();
        this.summary = summary;
        String hash = getCommitSHA1();
        File file = new File("./objects/" + hash);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        PrintWriter out = new PrintWriter(writer);
        out.print(hashOfTree + "\n" + prevHash + "\n" + nextHash + "\n" + author + "\n" + date + "\n" + summary);
        writer.close();
        out.close();
        

        //updating previous commit to link to this one
        File head = new File ("./HEAD");

        //read HEAD

        if (head.exists ())//i.e. if there is a previous commit
        {
            Scanner scanner = new Scanner(new File("./HEAD"));
            String prevCommit = "./objects/" + scanner.useDelimiter("\\A").next();
            scanner.close();

            //replacing line 3
            Path path = Paths.get(prevCommit);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.set(2, getCommitSHA1 ());
            Files.write(path, lines, StandardCharsets.UTF_8);

            //trimming new line from end
            File commitToTrim = new File (prevCommit);
            Scanner commitScanner = new Scanner(commitToTrim);
            String untrimmed = commitScanner.useDelimiter("\\A").next();
            String trimmed = untrimmed.substring (0, untrimmed.length () - 1);
            commitScanner.close();

            FileWriter prevWriter = new FileWriter(prevCommit,false);
            PrintWriter prevPrint = new PrintWriter(prevWriter);
            prevPrint.print (trimmed);
            prevWriter.close ();
            prevPrint.close ();
        }
        updateHEAD ();
    }

    public Commit(String author, String summary) throws Exception {
        this("", author, summary);
        updateHEAD ();
    }

    public void setNextCommit(String nextCommitHash) throws Exception {
        if (this.nextHash == "") {
            this.nextHash = nextCommitHash;
        }
        String hash = getCommitSHA1();
        File file = new File("./objects/" + hash);
        FileWriter writer = new FileWriter(file);
        PrintWriter out = new PrintWriter(writer);
        out.print(hashOfTree + "\n" + prevHash + "\n" + nextHash + "\n" + author + "\n" + date + "\n" + summary);
        writer.close();
        out.close();
    }

    public String getCommitSHA1() throws Exception {
        String content = hashOfTree + "\n" + prevHash + "\n" + "" + "\n" + author + "\n" + date + "\n" + summary;

        return getSHA1fromString(content);
    }

    public String createTree() throws Exception {
        Tree tree = new Tree();

            Boolean editingOrDeleting = false;

        //adding each file in index
        File index = new File ("index");
        if (index.exists () && index.length () != 0)
        {
            FileInputStream fstream = new FileInputStream("index");

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
            // Print the content on the console
                if (strLine.charAt (0) == 'b' || strLine.charAt (0) == 't')
                {
                    tree.add (strLine);
                }
                else
                {
                    tree.addEditedOrDeleted (strLine, prevHash);
                    editingOrDeleting = true;
                }
        }

        //Close the input stream
        in.close();
    }

        //clearing index
        if (index.exists ())
        {
            index.delete ();
        }
        index.createNewFile ();

        //adding previous tree
        if (!prevHash.equals ("") && !editingOrDeleting)
        {
            tree.add ("tree : " + getTreeSHA1FromCommit(prevHash));
        }

        return tree.writeToFile();
    }

    public void checkout (String commitSHA1) throws IOException
    {
        Scanner scanner = new Scanner(new File ("./objects/" + commitSHA1));
        String commitFirstLine = scanner.useDelimiter("\\n").next();
        scanner.close();
        File checkoutFolder = new File ("./checkoutFolder");
        checkoutFolder.mkdir();
        addEverythingFromTree (commitFirstLine, "");
    }

    public void addEverythingFromTree (String treeSHAString, String prefix) throws IOException
    {
        File tree = new File ("./objects/" + treeSHAString);
        if (tree.length () != 0)
        {
            FileInputStream fstream = new FileInputStream("./objects/" + treeSHAString);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
            // Do epic stuff
            //restore file
                if (strLine.charAt (0) == 't' && strLine.substring (6).indexOf (":") != -1)//making new folder
                {
                    File makeSureThisFolderExists = new File ("./checkoutFolder/" + prefix + strLine.substring (50));//directory addition
                    if (!makeSureThisFolderExists.exists ())
                    {
                        makeSureThisFolderExists.mkdir ();
                    }  
                    addEverythingFromTree (strLine.substring (7, 47), prefix + strLine.substring (50) + "/");
                }
            }
            in.close ();
            FileInputStream fstream2 = new FileInputStream("./objects/" + treeSHAString);

            // Get the object of DataInputStream
            DataInputStream in2 = new DataInputStream(fstream2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));

            String strLine2;

            //Read File Line By Line
            String fileContents = "";
            while ((strLine2 = br2.readLine()) != null)   {
            // Do epic stuff
            //restore file
            fileContents = "";
                if (strLine2.charAt (0) == 'b')
                {
                    File blobFile = new File ("./objects/" + strLine2.substring (7,47));
                    Scanner scanner = new Scanner(blobFile);
                    if (blobFile.length () != 0)
                    {
                        fileContents = scanner.useDelimiter("\\A").next();
                        scanner.close();
                    }
                    File newFile = new File ("./checkoutFolder/" + prefix + strLine2.substring (50));
                    newFile.createNewFile ();
                    FileWriter writer = new FileWriter("./checkoutFolder/" + prefix + strLine2.substring (50),false);
                    PrintWriter out = new PrintWriter(writer);
                    out.print (fileContents);
                    writer.close ();
                    out.close ();
                }
                else if (strLine2.substring (6).indexOf (":") == -1)//is past tree, not directory 
                {
                    addEverythingFromTree (strLine2.substring (7), "./");
                }
            }
            in2.close ();

            
        }
    }

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, uuuu");
        LocalDateTime now = LocalDateTime.now();
        return now.format(dtf);
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

    public void updateHEAD () throws Exception
    {
        File head = new File ("./HEAD");
        head.createNewFile();
        FileWriter writer = new FileWriter("./HEAD",false);
        PrintWriter out = new PrintWriter(writer);
        out.print (getCommitSHA1 ());
        writer.close ();
        out.close ();
    }

    //Method to get Commit's Tree based on a Commit's SHA1
    //This method will open the SHA1 of the Commit and return the hash of the Tree (it's first line)
    public static String getTreeSHA1FromCommit (String commitSHA) throws Exception
    {
        File commitFile = new File ("./objects/" + commitSHA);
        if (!commitFile.exists ())
        {
            throw new Exception ("No commit exists with the given SHA.");
        }
        Scanner scanner = new Scanner(commitFile);
        String commitContents = scanner.useDelimiter("\\n").next();
        scanner.close();
        return commitContents;
    }

}
