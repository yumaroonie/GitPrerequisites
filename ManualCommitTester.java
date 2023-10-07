import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ManualCommitTester {
    public static void main (String [] args) throws Exception
    {
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
        FileWriter writer2 = new FileWriter(file2,false);
        PrintWriter print2 = new PrintWriter(writer2);
        print2.print ("this is my second file!\nisn't it great?");
        writer2.close ();
        print2.close ();

        i.indexAddFile ("file1.txt");
        i.indexAddFile ("file2.txt");

        Commit myCommit = new Commit ("Chris", "justCommittedTwoFiles");
        /*

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
         */
    }
}
