import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommitTester {

 @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File index = new File ("index");
        File objects = new File ("objects");
        File head = new File ("HEAD");
        index.delete ();
        index.createNewFile ();
        objects.delete ();
        objects.mkdir ();
        head.delete ();
    }
    @Test
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        File index = new File ("index");
        File objects = new File ("objects");
        File head = new File ("HEAD");
        index.delete ();
        index.createNewFile ();
        objects.delete ();
        head.delete ();
    }

    @Test
    void testGetCommitSHA1andUpdateHEAD() throws Exception {


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
    void testCreateTree() throws Exception {//not detailed
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

    @Test
    void testGetTreeSHA1FromCommit() throws Exception {
        File head = new File ("./HEAD");
        head.delete ();

        Index i = new Index ();
        i.initialize(".");
        File pookie = new File ("pookieDoodle");
        pookie.createNewFile ();
        FileWriter writer = new FileWriter(pookie,false);
        PrintWriter print = new PrintWriter(writer);
        print.print ("pookieIsAFunnyName");
        writer.close ();
        print.close ();
        
        i.indexAddFile ("pookieDoodle");
        Commit myCommit = new Commit ("Chris", "isCool");
        //makes sure gets correct tree sha1
        assertEquals (Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()), "92750b8d5d1901f627dd556cfc0152c1d2405878");
    }

    @Test
    void oneCommitTest () throws Exception {
        Index i = new Index ();
        i.initialize (".");

        File file1 = new File ("file1.txt");
        file1.createNewFile ();
        FileWriter writer = new FileWriter(file1,false);
        PrintWriter print = new PrintWriter(writer);
        print.print ("this is my first file!");
        writer.close ();
        print.close ();

        File file2 = new File ("file2.txt");
        file2.createNewFile ();
        FileWriter writer2 = new FileWriter(file2,false);
        PrintWriter print2 = new PrintWriter(writer2);
        print2.print ("this is my second file!\nisn't it great?");
        writer2.close ();
        print2.close ();

        i.indexAddFile ("file1.txt");
        i.indexAddFile ("file2.txt");

        Commit myCommit = new Commit ("Chris", "justCommittedTwoFiles");
        System.out.println (Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()));
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()));
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 1),"fab8a5d74f8b8c70c7983c86509a5ac226a77330");
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 2), "");
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 3), "");
        file1.delete ();
        file2.delete ();

    }

    @Test
    void twoCommitTest () throws Exception {
        Index i = new Index ();
        i.initialize (".");

        //first commit
        File file1 = new File ("file1.txt");
        file1.createNewFile ();
        FileWriter writer = new FileWriter(file1,false);
        PrintWriter print = new PrintWriter(writer);
        print.print ("this is my first file!");
        writer.close ();
        print.close ();

        File file2 = new File ("file2.txt");
        file2.createNewFile ();
        FileWriter writer2 = new FileWriter(file1,false);
        PrintWriter print2 = new PrintWriter(writer2);
        print2.print ("this is my second file!\nisn't it great?");
        writer2.close ();
        print2.close ();

        i.indexAddFile ("file1.txt");
        i.indexAddFile ("file2.txt");

        Commit myCommit = new Commit ("Chris", "justCommittedTwoFiles");

        //second commit
        File file3 = new File ("file3.txt");
        file3.createNewFile ();
        FileWriter writer3 = new FileWriter(file3,false);
        PrintWriter print3 = new PrintWriter(writer3);
        print3.print ("this is my third file!");
        writer3.close ();
        print3.close ();

        File file4 = new File ("file4.txt");
        file4.createNewFile ();
        FileWriter writer4 = new FileWriter(file4,false);
        PrintWriter print4 = new PrintWriter(writer4);
        print4.print ("this is my fourth file!\nisn't it great?");
        writer4.close ();
        print4.close ();

        File folder1 = new File ("folder1");
        folder1.mkdir ();

        i.indexAddFile ("file3.txt");
        i.indexAddFile ("file4.txt");
        i.indexAddDirectory ("folder1");

        Commit mySecondCommit = new Commit (myCommit.getCommitSHA1 (), "Chris", "justMadeASecondCommit");

        //first commmit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 2), "");
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 3), mySecondCommit.getCommitSHA1 ());

        //second commit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (mySecondCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 2), myCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 3), "");

        //cleanup
        file1.delete ();
        file2.delete ();
        file3.delete ();
        file4.delete ();
        folder1.delete ();
    }

    @Test
    void fourCommitTest () throws Exception {
        Index i = new Index ();
        i.initialize (".");

        //first commit
        File file1 = new File ("file1.txt");
        file1.createNewFile ();
        FileWriter writer = new FileWriter(file1,false);
        PrintWriter print = new PrintWriter(writer);
        print.print ("this is my first file!");
        writer.close ();
        print.close ();

        File file2 = new File ("file2.txt");
        file2.createNewFile ();
        FileWriter writer2 = new FileWriter(file1,false);
        PrintWriter print2 = new PrintWriter(writer2);
        print2.print ("this is my second file!\nisn't it great?");
        writer2.close ();
        print2.close ();

        i.indexAddFile ("file1.txt");
        i.indexAddFile ("file2.txt");

        Commit myCommit = new Commit ("Chris", "justCommittedTwoFiles");

        //second commit
        File file3 = new File ("file3.txt");
        file3.createNewFile ();
        FileWriter writer3 = new FileWriter(file3,false);
        PrintWriter print3 = new PrintWriter(writer3);
        print3.print ("this is my third file!");
        writer3.close ();
        print3.close ();

        File file4 = new File ("file4.txt");
        file4.createNewFile ();
        FileWriter writer4 = new FileWriter(file4,false);
        PrintWriter print4 = new PrintWriter(writer4);
        print4.print ("this is my fourth file!\nisn't it great?");
        writer4.close ();
        print4.close ();

        File folder1 = new File ("folder1");
        folder1.mkdir ();

        i.indexAddFile ("file3.txt");
        i.indexAddFile ("file4.txt");
        i.indexAddDirectory ("folder1");

        Commit mySecondCommit = new Commit (myCommit.getCommitSHA1 (), "Chris", "justMadeASecondCommit");

        //third commit
        File file5 = new File ("file5.txt");
        file5.createNewFile ();
        FileWriter writer5 = new FileWriter(file5,false);
        PrintWriter print5 = new PrintWriter(writer5);
        print5.print ("this is my fifth file!");
        writer5.close ();
        print5.close ();

        File file6 = new File ("file6.txt");
        file6.createNewFile ();
        FileWriter writer6 = new FileWriter(file6,false);
        PrintWriter print6 = new PrintWriter(writer6);
        print6.print ("this is my sixth file!\nisn't it great?");
        writer6.close ();
        print6.close ();

        i.indexAddFile ("file5.txt");
        i.indexAddFile ("file6.txt");

        Commit myThirdCommit = new Commit (mySecondCommit.getCommitSHA1 (), "Chris", "justCommittedThreeFiles");

        //fourth commit
        File file7 = new File ("file7.txt");
        file7.createNewFile ();
        FileWriter writer7 = new FileWriter(file7,false);
        PrintWriter print7 = new PrintWriter(writer7);
        print7.print ("this is my seventh file!");
        writer7.close ();
        print7.close ();

        File file8 = new File ("file8.txt");
        file8.createNewFile ();
        FileWriter writer8 = new FileWriter(file8,false);
        PrintWriter print8 = new PrintWriter(writer8);
        print8.print ("this is my eighth file!\nisn't it great?");
        writer8.close ();
        print8.close ();

        File folder2 = new File ("folder2");
        folder2.mkdir ();

        i.indexAddFile ("file7.txt");
        i.indexAddFile ("file8.txt");
        i.indexAddDirectory ("folder1");

        Commit myFourthCommit = new Commit (myThirdCommit.getCommitSHA1 (), "Chris", "justMadeAFourthCommit");

        //first commmit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 2), "");
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 3), mySecondCommit.getCommitSHA1 ());

        //second commit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (mySecondCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 2), myCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 3), myThirdCommit.getCommitSHA1 ());

        //third commmit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myThirdCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 2), mySecondCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 3), myFourthCommit.getCommitSHA1 ());

        //fourth commit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myFourthCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 2), myThirdCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 3), "");

        //cleanup
        file1.delete ();
        file2.delete ();
        file3.delete ();
        file4.delete ();
        file5.delete ();
        file6.delete ();
        file7.delete ();
        file8.delete ();
        folder1.delete ();
        folder2.delete ();
    }

    @Test
    void fourCommitTest () throws Exception {
        Index i = new Index ();
        i.initialize (".");

        //first commit
        File file1 = new File ("file1.txt");
        file1.createNewFile ();
        FileWriter writer = new FileWriter(file1,false);
        PrintWriter print = new PrintWriter(writer);
        print.print ("this is my first file!");
        writer.close ();
        print.close ();

        File file2 = new File ("file2.txt");
        file2.createNewFile ();
        FileWriter writer2 = new FileWriter(file1,false);
        PrintWriter print2 = new PrintWriter(writer2);
        print2.print ("this is my second file!\nisn't it great?");
        writer2.close ();
        print2.close ();

        i.indexAddFile ("file1.txt");
        i.indexAddFile ("file2.txt");

        Commit myCommit = new Commit ("Chris", "justCommittedTwoFiles");

        //second commit
        File file3 = new File ("file3.txt");
        file3.createNewFile ();
        FileWriter writer3 = new FileWriter(file3,false);
        PrintWriter print3 = new PrintWriter(writer3);
        print3.print ("this is my third file!");
        writer3.close ();
        print3.close ();

        File file4 = new File ("file4.txt");
        file4.createNewFile ();
        FileWriter writer4 = new FileWriter(file4,false);
        PrintWriter print4 = new PrintWriter(writer4);
        print4.print ("this is my fourth file!\nisn't it great?");
        writer4.close ();
        print4.close ();

        File folder1 = new File ("folder1");
        folder1.mkdir ();

        i.indexAddFile ("file3.txt");
        i.indexAddFile ("file4.txt");
        i.indexAddDirectory ("folder1");

        Commit mySecondCommit = new Commit (myCommit.getCommitSHA1 (), "Chris", "justMadeASecondCommit");

        //third commit
        File file5 = new File ("file5.txt");
        file5.createNewFile ();
        FileWriter writer5 = new FileWriter(file5,false);
        PrintWriter print5 = new PrintWriter(writer5);
        print5.print ("this is my fifth file!");
        writer5.close ();
        print5.close ();

        File file6 = new File ("file6.txt");
        file6.createNewFile ();
        FileWriter writer6 = new FileWriter(file6,false);
        PrintWriter print6 = new PrintWriter(writer6);
        print6.print ("this is my sixth file!\nisn't it great?");
        writer6.close ();
        print6.close ();

        i.indexAddFile ("file5.txt");
        i.indexAddFile ("file6.txt");

        Commit myThirdCommit = new Commit (mySecondCommit.getCommitSHA1 (), "Chris", "justCommittedThreeFiles");

        //fourth commit
        File file7 = new File ("file7.txt");
        file7.createNewFile ();
        FileWriter writer7 = new FileWriter(file7,false);
        PrintWriter print7 = new PrintWriter(writer7);
        print7.print ("this is my seventh file!");
        writer7.close ();
        print7.close ();

        File file8 = new File ("file8.txt");
        file8.createNewFile ();
        FileWriter writer8 = new FileWriter(file8,false);
        PrintWriter print8 = new PrintWriter(writer8);
        print8.print ("this is my eighth file!\nisn't it great?");
        writer8.close ();
        print8.close ();

        File folder2 = new File ("folder2");
        folder2.mkdir ();

        i.indexAddFile ("file7.txt");
        i.indexAddFile ("file8.txt");
        i.indexAddDirectory ("folder1");

        Commit myFourthCommit = new Commit (myThirdCommit.getCommitSHA1 (), "Chris", "justMadeAFourthCommit");

        //first commmit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 2), "");
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myCommit.getCommitSHA1 (), 3), mySecondCommit.getCommitSHA1 ());

        //second commit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (mySecondCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 2), myCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + mySecondCommit.getCommitSHA1 (), 3), myThirdCommit.getCommitSHA1 ());

        //third commmit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myThirdCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 2), mySecondCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myThirdCommit.getCommitSHA1 (), 3), myFourthCommit.getCommitSHA1 ());

        //fourth commit
        //makes sure commit has correct tree
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 1), Commit.getTreeSHA1FromCommit (myFourthCommit.getCommitSHA1 ()));
        //makes sure commit has correct previous sha1
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 2), myThirdCommit.getCommitSHA1 ());
        //makes sure commit has correct next sha1
        assertEquals (getLineOfFile ("./objects/" + myFourthCommit.getCommitSHA1 (), 3), "");

        //cleanup
        file1.delete ();
        file2.delete ();
        file3.delete ();
        file4.delete ();
        file5.delete ();
        file6.delete ();
        file7.delete ();
        file8.delete ();
        folder1.delete ();
        folder2.delete ();
    }

    public String getLineOfFile ( String filePath, int lineNum) throws IOException
    {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return (lines.get (lineNum - 1));
    }
}
