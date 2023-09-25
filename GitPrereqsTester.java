
public class GitPrereqsTester
{
    public static void main (String [] args) throws Exception
    {
        //Commit myCommit = new Commit ("chris2", "iswaytooepic");
        
        Tree myTree = new Tree ();
        myTree.addDirectory ("advancedDirectory");
        
        // //tests initialize
        // Index myIndex = new Index ();
        // myIndex.initialize ("/Users/ryancheng/p/Honors Topics/GitPrerequisites");

        // //tests blobify, printing sha1 of file contents
        // //Blob myBlob = new Blob ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/ToBeBlobbed.txt");
        // //myBlob.blobify ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/objects");
        // //System.out.println (myBlob.getSHA1String());

        // myIndex.add ("foo.txt");
        // myIndex.add ("foobar.txt");
        // myIndex.add ("bar.txt");
        // myIndex.add ("bar.txt");
        // myIndex.remove("foobar.txt");

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