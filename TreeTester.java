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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester {

    String fileContents = "this is in file." + "\n" + "this is also in file.";
    String fileName = new String("testfile.txt");


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File index = new File ("index");
        File objects = new File ("objects");
        index.delete ();
        index.createNewFile ();
        objects.delete ();
        objects.mkdir ();
    }
    @Test
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File index = new File ("index");
        File objects = new File ("objects");
        index.delete ();
        index.createNewFile ();
        objects.delete ();
    }

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
        Index i = new Index ();
        i.initialize ("./");
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

        //clearing objects folder again
        for (File secondFile : objects.listFiles ())
        {
            secondFile.delete ();
        }
        //making test directories
        File advancedDirectory = new File ("advancedDirectory");
        advancedDirectory.mkdir();
        File directoryEmpty = new File ("advancedDirectory/directoryEmpty");
        directoryEmpty.mkdir();
        File directoryFull = new File ("advancedDirectory/directoryFull");
        directoryFull.mkdir();
        //making test files
        makeTextFile("./advancedDirectory/textFileOuter1", "fileOuter1");
        makeTextFile("./advancedDirectory/textFileOuter2", "fileOuter2");
        makeTextFile("./advancedDirectory/textFileOuter3", "fileOuter3");
        makeTextFile("./advancedDirectory/directoryFull/textFileInner", "this file is inner");
        //testing addDirectory
        Tree mySecondTree = new Tree ();
        mySecondTree.addDirectory ("advancedDirectory");
        //checking simple blobs' existence and correctness
        File file4 = new File ("./objects/0c730a39f2a0ec1481f82dd013f97f83ac10b7a0");
        File file5 = new File ("./objects/c63fe4fa48b0e578320db7361a926310c0ca1850");
        File file6 = new File ("./objects/1c652ca5712f0160f74216041d95252e40c78d9f");
        File file7 = new File ("./objects/a16b86280512f4fb3962608623137b6a2e1c1c9c");

        //checking that non-tree files (file blobs) exist
        assertTrue (file4.exists () && file5.exists () && file6.exists () && file7.exists ());
        //checks to see if these files' contents are correct
        assertTrue (fileConsistsOfContents ("./objects/0c730a39f2a0ec1481f82dd013f97f83ac10b7a0", "fileOuter1") && fileConsistsOfContents("./objects/c63fe4fa48b0e578320db7361a926310c0ca1850", "fileOuter2") && fileConsistsOfContents("./objects/1c652ca5712f0160f74216041d95252e40c78d9f", "fileOuter3") && fileConsistsOfContents ("./objects/a16b86280512f4fb3962608623137b6a2e1c1c9c", "this file is inner"));

        File thirdFile = new File("./objects/2c114347ac36cc8605e417ef539019b33823e2c5");
        //checks to see if saved tree file exists
        assertTrue (thirdFile.exists ());
        //checks to make sure saved tree file is correct length
        assertTrue (Files.lines (Paths.get("./objects/2c114347ac36cc8605e417ef539019b33823e2c5")).count () == 5);
        //checks to see if saved tree file contains correct entries
        assertTrue (fileContainsContents ("./objects/2c114347ac36cc8605e417ef539019b33823e2c5", "blob : 0c730a39f2a0ec1481f82dd013f97f83ac10b7a0 : textFileOuter1") && fileContainsContents("./objects/2c114347ac36cc8605e417ef539019b33823e2c5", "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : directoryEmpty") && fileContainsContents("./objects/2c114347ac36cc8605e417ef539019b33823e2c5", "blob : 1c652ca5712f0160f74216041d95252e40c78d9f : textFileOuter3") && fileContainsContents("./objects/2c114347ac36cc8605e417ef539019b33823e2c5", "blob : c63fe4fa48b0e578320db7361a926310c0ca1850 : textFileOuter2") && fileContainsContents("./objects/2c114347ac36cc8605e417ef539019b33823e2c5", "tree : 8d3584978ffa2af44d377fae1175d31164bb9afb : directoryFull"));
        File fourthFile = new File ("./objects/8d3584978ffa2af44d377fae1175d31164bb9afb");
        //checks to see if saved tree file exists
        assertTrue (fourthFile.exists ());
        //checks to see if saved tree file consists of correct entry
        assertTrue (fileConsistsOfContents ("./objects/8d3584978ffa2af44d377fae1175d31164bb9afb", "blob : a16b86280512f4fb3962608623137b6a2e1c1c9c : textFileInner"));
        File fifthFile = new File ("./objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");
        //checks to see if saved tree file exists
        assertTrue (fifthFile.exists ());
        //checks to see if saved tree file is of length 0 as it should be
        assertTrue (fifthFile.length () == 0);
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
