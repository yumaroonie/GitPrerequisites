import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

    // uses create and delete at the start and end of each 
    // test because the beforeall and afterall methods
    // were not working
    String fileContents = "this is in file." + "\n" + "this is also in file.";
    String fileName = new String("testfile.txt");

    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    public void testInitialize() throws Exception {

        Index index = new Index();
        index.initialize("./");

        File indexFile = new File("index");
        assertTrue("The index file was not created.", indexFile.exists());
        Path objectsFolder = Paths.get("./objects/");
        assertTrue("The objects folder was not created.", Files.exists(objectsFolder));

    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    public void testIndexAddFile() throws Exception {

        createTestFile();

        Index index = new Index();
        index.initialize("./");
        index.indexAddFile("testfile.txt");
        File blobFile = new File("./objects/" + getSha1(fileContents));
        assertTrue("Blob file was not created.", blobFile.exists());

        assertEquals("File contents of added blob does not exist.", fileContents, Files.readString(Path.of("./objects/" + getSha1(fileContents))));

        String indexContents = Files.readString(Path.of("./index"));
        assertEquals("Index contents are incorrect.", indexContents, "blob : " + getSha1(fileContents) + " : testfile.txt");

        deleteStuff();
    }

        @Test
    @DisplayName("[15] Test if adding a directory works.")
    public void testIndexAddDirectory() throws Exception {
        //creating test folder with file inside
        File testDirectory = new File ("testDirectory");
        testDirectory.mkdir ();
        File innerFile = new File ("./testDirectory/innerFile");
        innerFile.createNewFile ();
        FileWriter writer = new FileWriter("./testDirectory/innerFile",false);
        PrintWriter out = new PrintWriter(writer);
        out.print ("getwrecked");
        writer.close ();
        out.close ();

        Index index = new Index();
        index.initialize("./");
        index.indexAddDirectory("testDirectory");
        File innerBlob = new File ("./objects/87b448f8911df0174555fe6e37f9473543b023a8");
        assertTrue (innerBlob.exists ());
        Scanner scanner = new Scanner(innerBlob);
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        assertEquals (myString, "getwrecked");
        File blobbedDirectory = new File ("./objects/f9444c31bf8e9f42d618650f233b96bb49fe2d82");
        assertTrue (blobbedDirectory.exists ());
        Scanner scanner2 = new Scanner(blobbedDirectory);
        myString = scanner2.useDelimiter("\\A").next();
        scanner2.close();
        assertEquals (myString, "blob : 87b448f8911df0174555fe6e37f9473543b023a8 : innerFile");

        File indexFile2 = new File ("./index");
        Scanner scanner3 = new Scanner(indexFile2);
        myString = scanner3.useDelimiter("\\A").next();
        scanner3.close();
        assertEquals (myString, "tree : f9444c31bf8e9f42d618650f233b96bb49fe2d82 : testDirectory");
        
        innerFile.delete ();
        testDirectory.delete ();
    }

    @Test
    @DisplayName("Test if removing a blob works.")
    public void testRemove() throws Exception{

        createTestFile();
        Index index = new Index();
        index.initialize("./");
        index.indexAddFile("testfile.txt");
        File blobFile = new File("./objects/" + getSha1(fileContents));
        assertTrue("Blob file was not created.", blobFile.exists());

        assertEquals("File contents of added blob does not exist.", fileContents, Files.readString(Path.of("./objects/" + getSha1(fileContents))));

        String indexContents = Files.readString(Path.of("./index"));
        assertEquals("Index contents are incorrect.", indexContents, "blob : " + getSha1(fileContents) + " : testfile.txt");

        index.remove("testfile.txt");
        indexContents = Files.readString(Path.of("./index"));
        assertTrue("Blob file was not removed.", !blobFile.exists());
        assertEquals("Index contents were not removed.", indexContents, "");
        deleteStuff();
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

    public void createTestFile() throws Exception {
        Path pathObject = Paths.get("./testfile.txt");
        if (!pathObject.toFile().exists()) {
            Files.createFile(pathObject);
        }

        FileWriter writer = new FileWriter("./testfile.txt");
        writer.write(fileContents);
        writer.close();
    }

    private void deleteStuff() throws IOException {
        File index = new File("./index");
        if (!index.exists())
            index.delete();
        Files.delete(Paths.get("./testfile.txt"));
        File objectsFolder = new File("./objects/");
        String[] entries = objectsFolder.list();
        for (String s : entries) {
            File currentFile = new File(objectsFolder.getPath(), s);
            currentFile.delete();
        }
        Files.delete(Paths.get("./objects"));
    }
}
