import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Formatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

    // uses create and delete at the start and end of each 
    // test because the beforeall and afterall methods
    // were not working
    static String projPath = "/Users/ryancheng/p/Honors Topics/GitPrerequisites";
    String fileContents = "this is in file." + "\n" + "this is also in file.";
    static File testfile = new File("testfile.txt");

    @Test
    @DisplayName("[8] Test if initialize and objects are created correctly")
    public void testInitialize() throws Exception {

        Index index = new Index();
        index.initialize(projPath);

        File indexFile = new File("index");
        assertTrue("The index file was not created.", indexFile.exists());
        Path objectsFolder = Paths.get("./objects/");
        assertTrue("The objects folder was not created.", Files.exists(objectsFolder));

    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    public void testAdd() throws Exception {

        createTestFile();

        Index index = new Index();
        index.initialize(projPath);
        index.add("testfile.txt");
        File blobFile = new File(projPath + "/objects/" + getSha1(fileContents));
        assertTrue("Blob file was not created.", blobFile.exists());

        assertEquals("File contents of added blob does not exist.", fileContents, Files.readString(Path.of(projPath + "/objects/" + getSha1(fileContents))));

        String indexContents = Files.readString(Path.of(projPath + "/index"));
        assertEquals("Index contents are incorrect.", indexContents, "testfile.txt : " + getSha1(fileContents) + "\n");

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

        Path pathObject = Paths.get(projPath + "/testfile.txt");
        Files.createFile(pathObject);

        FileWriter writer = new FileWriter(projPath + "/testfile.txt");
        writer.write(fileContents);
        writer.close();
    }

        private void deleteStuff() throws IOException {
        Files.delete(Paths.get(projPath + "/index"));
        Files.delete(Paths.get(projPath + "/testfile.txt"));
        File objectsFolder = new File(projPath + "/objects/");
        String[] entries = objectsFolder.list();
        for (String s : entries) {
            File currentFile = new File(objectsFolder.getPath(), s);
            currentFile.delete();
        }
        Files.delete(Paths.get(projPath + "/objects"));
    }
}
