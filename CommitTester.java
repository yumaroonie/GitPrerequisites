import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class CommitTester {

    @Test
    void testGetCommitSHA1() throws Exception {
        File head = new File ("HEAD");
        head.delete ();

        Commit commit = new Commit("Chris", "My name is Chris");

        String output = commit.getCommitSHA1();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, uuuu");
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(dtf);

        String hash = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        String author = "Chris";
        String summary = "My name is Chris";

        String compare = hash + "\n" + "" + "\n" + "" + "\n" + author + "\n" + date + "\n" + summary;
        compare = commit.getSHA1fromString(compare);

        System.out.println(output);
        System.out.println(compare);

        assertTrue("The Correct Hash is Returned", compare.equals(output));
        
        //test HEAD creation functionality
        //makes sure HEAD exists
        assertTrue (head.exists ());

        Scanner scan = new Scanner(head);
        scan.useDelimiter("\\Z");  
        String scannedContent = scan.next();     
        scan.close ();

        //makes sure HEAD contents consists of correct SHA1 value (SHA1 of commit)
        assertTrue (scannedContent.equals (compare));

        //test HEAD updating functionality
        Commit secondCommit = new Commit ("Andrew", "My name is Andrew Khacha");
        Scanner scan2 = new Scanner(head);
        scan2.useDelimiter("\\Z");  
        String scannedContent2 = scan2.next();     
        scan2.close ();
        assertTrue (scannedContent2.equals (secondCommit.getCommitSHA1 ()));
    }

    @Test
    void testCreateTree() throws Exception {
        Commit commit = new Commit("Chris", "My name is Chris");

        commit.createTree();

        String hash = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

        Path path = Paths.get("./objects/" + hash);

        assertTrue("createTree Method creates a tree", path.toFile().exists());
    }

    @Test
    void testGetDate() throws Exception {
        Commit commit = new Commit("Chris", "My name is Chris");
        
        String time = commit.getDate();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, uuuu");
        LocalDateTime now = LocalDateTime.now();
        String text = now.format(dtf);

        assertTrue("Date is Correct", text.equals(time));
    }

    @Test
    void testGetSHA1fromString() throws Exception {
        Commit commit = new Commit("Chris", "My name is Chris");

        String output = commit.getSHA1fromString("");
        
        assertTrue("Sha1 returned is Correct", output.equals("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
    }

    @Test
    void testSetNextCommit() throws Exception {
        Commit commit = new Commit("Chris", "My name is Chris");
        String oldHash = commit.getCommitSHA1();
        commit.setNextCommit("next");
        
        assertTrue("Hash doesn't change when next commit is changed", commit.getCommitSHA1().equals(oldHash));

        Path path = Paths.get("./objects/" + oldHash);

        String content = Files.readString(path);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, uuuu");
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(dtf);

        String hash = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        String author = "Chris";
        String summary = "My name is Chris";

        String compare = hash + "\n" + "" + "\n" + "next" + "\n" + author + "\n" + date + "\n" + summary;

        assertTrue("Commit File updates with next commit", compare.equals(content));
    }
}
