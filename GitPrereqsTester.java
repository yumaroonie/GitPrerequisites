import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GitPrereqsTester
{
    public static void main (String [] args) throws Exception
    {
        /*
Path path = Paths.get("exampleCommit");
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    lines.set(2, "wreckedFoo");
    Files.write(path, lines, StandardCharsets.UTF_8);


    File example = new File ("exampleCommit");
Scanner scanner = new Scanner(example);
        String theFile = scanner.useDelimiter("\\A").next();
        String finalFile = theFile.substring (0, theFile.length () - 1);
        scanner.close();

        FileWriter writer = new FileWriter("exampleCommit",false);
        PrintWriter out = new PrintWriter(writer);
        out.print (finalFile);
        writer.close ();
        out.close ();
        
*/
/*

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
*/

//Index i = new Index ();
//i.initialize (".");
//i.editExistingSavedFile("mashallah.txt");
//i.deleteSavedFile ("hehe");
File head = new File ("./HEAD");
        head.delete ();

        Index i = new Index ();
        i.initialize(".");
        
        //making test directories
        File advancedDirectory = new File ("advancedDirectory");
        advancedDirectory.mkdir();
        
        File directoryEmpty = new File ("advancedDirectory/directoryEmpty");
        directoryEmpty.mkdir();
        File directoryFull = new File ("advancedDirectory/directoryFull");
        directoryFull.mkdir();
        
        //making test files and directory
        File firstFile = new File ("./advancedDirectory/textFileOuter1");
        File secondFile = new File ("./advancedDirectory/textFileOuter2");
        File thirdFile = new File ("./advancedDirectory/textFileOuter3");
        File fourthFile = new File ("./advancedDirectory/textFileInner");
        firstFile.createNewFile ();
        secondFile.createNewFile ();
        thirdFile.createNewFile ();
        fourthFile.createNewFile ();

        i.indexAddDirectory("advancedDirectory");
/*
        i.indexAddFile ("./advancedDirectory/textFileOuter1");
        i.indexAddFile ("./advancedDirectory/textFileOuter1");
        i.indexAddFile ("./advancedDirectory/textFileOuter1");
        i.indexAddFile ("./advancedDirectory/directoryFull/textFileInner");
*/
        Commit myCommit = new Commit ("Chris", "correct commit for reversion");

        /* 
        firstFile.delete ();
        secondFile.delete ();
        thirdFile.delete ();
        fourthFile.delete ();
        directoryEmpty.delete ();
        directoryFull.delete ();
        advancedDirectory.delete ();

        Commit secondCommit = new Commit (myCommit.getCommitSHA1(), "Chris", "ew i messed up with this commit");

//Scanner scanner = new Scanner(new File ("./objects/" + Commit.getTreeSHA1FromCommit(myCommit.getCommitSHA1 ())));
  //      String commitContents = scanner.useDelimiter("\\A").next();
    //    scanner.close()

 //       System.out.println (commitContents);

        secondCommit.checkout (myCommit.getCommitSHA1());
        //Commit myCommit = new Commit ("chris2", "iswaytooepic");
        */
        //Tree myTree = new Tree ();
        //myTree.addDirectory ("advancedDirectory");
        
        //tests initialize
        //Index myIndex = new Index ();
        //myIndex.initialize ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites");

        // //tests blobify, printing sha1 of file contents
        // //Blob myBlob = new Blob ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/ToBeBlobbed.txt");
        // //myBlob.blobify ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/objects");
        // //System.out.println (myBlob.getSHA1String());
/*
File foobar = new File ("foobar.txt");
        Scanner scanner = new Scanner(foobar);
        String commitContents = scanner.useDelimiter("\\n").next();
        scanner.close();
        System.out.println (commitContents);
        */
        
        //Index myIndex = new Index ();
        //myIndex.initialize (".");
        //myIndex.indexAddDirectory("advancedDirectory");
/*
        myIndex.indexAddFile ("foo.txt");
        myIndex.indexAddFile ("foobar.txt");
        myIndex.indexAddFile ("bar.txt");
        myIndex.indexAddFile ("bar.txt");
        myIndex.remove("foobar.txt");
        myIndex.updateIndex();
        */

        // Blob blobTest = new Blob("testfile.txt", "./");
        /*
        Commit commit = new Commit("Chris", "My name is Chris");

        String date = commit.getDate();

        String hash = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        String author = "Chris";
        String summary = "My name is Chris";

        StringBuilder sb = new StringBuilder(hash + "\n\n\n" + author + "\n" + date + "\n" + summary);
        String compare = commit.getSHA1fromString(sb.toString());

        System.out.println(commit.createCommitHash());
        System.out.println(compare);
        */
        
    }
}