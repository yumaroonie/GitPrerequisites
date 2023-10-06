import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

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

Index i = new Index ();
i.initialize (".");
i.editExistingSavedFile("mashallah.txt");
i.deleteSavedFile ("hehe");

        //Commit myCommit = new Commit ("chris2", "iswaytooepic");
        
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