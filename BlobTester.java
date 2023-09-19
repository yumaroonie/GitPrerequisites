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

public class BlobTester {

    static String projPath = "/Users/ryancheng/p/Honors Topics/GitPrerequisites";
    String fileContents = "this is in file." + "\n" + "this is also in file.";
    static File testfile = new File("testfile.txt");

    @Test
    public void testCreateBlob() throws Exception {

        createTestFile();

        Index index = new Index();
        index.initialize(projPath);
        index.add("testfile.txt");
        File blobFile = new File(projPath + "/objects/" + getSha1(fileContents));
        assertTrue(blobFile.exists());

        assertEquals(fileContents, Files.readString(Path.of(projPath + "/objects/" + getSha1(fileContents))));

        deleteStuff();
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
}
