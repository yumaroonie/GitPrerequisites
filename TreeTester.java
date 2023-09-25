import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester {

    String fileContents = "this is in file." + "\n" + "this is also in file.";
    String fileName = new String("testfile.txt");

    @Test
    @DisplayName("Tests adding entries to the trees")
    void testAdd() throws Exception {

        Tree tree = new Tree();
        String blobEntry = "blob : 380ae8e935d67d48591fccd7bcfa00a55b23cf52 : testfile.txt";
        String treeEntry = "tree : d5db5d8200a57fe7d026472b40e65efe2887bd63";

        tree.add(blobEntry);
        tree.add(treeEntry);

        assertTrue("Tree entries were not added.", tree.getEntries().contains(blobEntry) && tree.getEntries().contains(treeEntry));
    }

    @Test
    @DisplayName("tests removing entries from the tree")
    void testRemove() throws Exception {

        Tree tree = new Tree();
        String blobEntry = "blob : 380ae8e935d67d48591fccd7bcfa00a55b23cf52 : testfile.txt";
        String treeEntry = "tree : d5db5d8200a57fe7d026472b40e65efe2887bd63";

        tree.add(blobEntry);
        tree.add(treeEntry);

        tree.remove("testfile.txt");
        assertFalse("Tree entry was not removed.", tree.getEntries().contains(blobEntry));

        tree.remove("d5db5d8200a57fe7d026472b40e65efe2887bd63");
        assertFalse("Tree entry was not removed.", tree.getEntries().contains(treeEntry));
    }

    @Test
    @DisplayName("tests writing the tree to a file")
    void testWriteToFile() throws Exception {

        Tree tree = new Tree();

        String blobEntry = "blob : 380ae8e935d67d48591fccd7bcfa00a55b23cf52 : testfile.txt";
        String treeEntry = "tree : d5db5d8200a57fe7d026472b40e65efe2887bd63";

        tree.add(blobEntry);
        tree.add(treeEntry);
        tree.writeToFile();

        String hash = getSha1(tree.getEntries());
        File treeFile = new File("./objects/" + hash);
        String str = Files.readString(Path.of("./objects/" + hash));
        assertTrue(treeFile.exists());
        assertTrue(str.equals(tree.getEntries()));
    }

    @Test
    @DisplayName("tests  adding a directory to the tree")
    void testAddDirectory() throws Exception {
        //clears objects folder
        File objects = new File ("objects");
        for (File file : objects.listFiles ())
        {
            file.delete ();
        }

        //case 1 (basic)

        //making files
        File basicDirectory = new File ("basicDirectory");
        basicDirectory.mkdir();
        makeTextFile("./basicDirectory/textFile1", "this is the first text file");
        makeTextFile("./basicDirectory/textFile2", "this is the second text file...");
        makeTextFile("./basicDirectory/textFile3", "this is the third text file!");
       
        //testing addDirectory
        Tree myTree = new Tree ();
        myTree.addDirectory ("basicDirectory");
        File file1 = new File ("./objects/8494e675cd817e0d54ec60d5047f1cbffb421969");
        File file2 = new File ("./objects/9276373d472f13edcf0412e0a6ddbaa82c0b2aff");
        File file3 = new File ("./objects/7c90f29bae54ac5e6ce52e2a12f7f7b19180d2ca");

        //checking that non-tree files (file blobs) exist
        assertTrue (file1.exists () && file2.exists () && file3.exists ());
        //checks to see if these files' contents are correct
        assertTrue (fileConsistsOfContents ("./objects/8494e675cd817e0d54ec60d5047f1cbffb421969", "this is the first text file") && fileConsistsOfContents("./objects/9276373d472f13edcf0412e0a6ddbaa82c0b2aff", "this is the second text file...") && fileConsistsOfContents("./objects/7c90f29bae54ac5e6ce52e2a12f7f7b19180d2ca", "this is the third text file!"));
        File file = new File("./objects/e80d6fb7cedbd6e5c1c5ecaa70e1ca389dff5998");
        //checks to see if saved tree file exists
        assertTrue (file.exists ());
        //checks to make sure saved tree file is correct length
        assertTrue (Files.lines (Paths.get("./objects/e80d6fb7cedbd6e5c1c5ecaa70e1ca389dff5998")).count () == 3);
        //checks to see if saved tree file contains correct entries
        assertTrue (fileContainsContents ("./objects/e80d6fb7cedbd6e5c1c5ecaa70e1ca389dff5998", "blob : 8494e675cd817e0d54ec60d5047f1cbffb421969 : textFile1") && fileContainsContents("./objects/e80d6fb7cedbd6e5c1c5ecaa70e1ca389dff5998", "blob : 9276373d472f13edcf0412e0a6ddbaa82c0b2aff : textFile2") && fileContainsContents("./objects/e80d6fb7cedbd6e5c1c5ecaa70e1ca389dff5998", "blob : 7c90f29bae54ac5e6ce52e2a12f7f7b19180d2ca : textFile3"));

        //case 2 (advanced)

    }

    private void makeTextFile (String path, String contents) throws IOException
    {
        File theFile = new File (path);
        theFile.createNewFile ();
        FileWriter writer = new FileWriter(path,false);
        PrintWriter out = new PrintWriter(writer);
        out.print (contents);
        writer.close ();
        out.close ();
    }

    private boolean fileConsistsOfContents (String filePathString, String contents) throws IOException
    {
        Scanner scan = new Scanner(new File (filePathString));
        scan.useDelimiter("\\Z");  
        String scannedContent = scan.next();     
        scan.close ();
        return scannedContent.equals (contents);
    }

    private boolean fileContainsContents (String filePathString, String contents) throws IOException
    {
        Scanner scan = new Scanner(new File (filePathString));
        scan.useDelimiter("\\Z");  
        String scannedContent = scan.next();     
        scan.close ();
        return scannedContent.contains (contents);
    }

    public String getSha1(String input) throws Exception {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(input.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest()) {
            formatter.format("%02x", b);
        }
        String SHA1 = formatter.toString();
        formatter.close();
        return SHA1;
    }
}
