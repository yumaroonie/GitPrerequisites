public class GitPrereqsTester
{
    public static void main (String [] args) throws Exception
    {
        //tests initialize
        Index myIndex = new Index ();
        myIndex.initialize ("/Users/ryancheng/p/Honors Topics/GitPrerequisites");

        //tests blobify, printing sha1 of file contents
        //Blob myBlob = new Blob ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/ToBeBlobbed.txt");
        //myBlob.blobify ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/objects");
        //System.out.println (myBlob.getSHA1String());

        myIndex.add ("foo.txt");
        myIndex.add ("foobar.txt");
        myIndex.add ("bar.txt");
        myIndex.add ("bar.txt");
        myIndex.remove("foobar.txt");
    }
}