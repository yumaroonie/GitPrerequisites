import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

public class TreeTester {

    static String projPath = "/Users/ryancheng/p/Honors Topics/GitPrerequisites";
    String fileContents = "this is in file." + "\n" + "this is also in file.";
    static File testfile = new File("testfile.txt");

    @Test
    void testAdd() throws Exception {

        Tree tree = new Tree();
        String blobEntry = "blob : 380ae8e935d67d48591fccd7bcfa00a55b23cf52 : testfile.txt";
        String treeEntry = "tree : d5db5d8200a57fe7d026472b40e65efe2887bd63";

        tree.add(blobEntry);
        tree.add(treeEntry);

        assertTrue(tree.getEntries().contains(blobEntry) && tree.getEntries().contains(treeEntry));
    }

    @Test
    void testRemove() throws Exception {

        Tree tree = new Tree();
        String blobEntry = "blob : 380ae8e935d67d48591fccd7bcfa00a55b23cf52 : testfile.txt";
        String treeEntry = "tree : d5db5d8200a57fe7d026472b40e65efe2887bd63";

        tree.add(blobEntry);
        tree.add(treeEntry);

        tree.remove("testfile.txt");
        assertTrue(tree.getEntries().contains(blobEntry));

        tree.remove("d5db5d8200a57fe7d026472b40e65efe2887bd63");
        assertTrue(tree.getEntries().contains(treeEntry));
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
