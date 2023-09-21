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
    @DisplayName("tests the write to file method")
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
