import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GitPrereqsTester
{
    public static void main (String [] args) throws IOException, NoSuchAlgorithmException
    {
        Index myIndex = new Index ();
        myIndex.initialize ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites");

        Blob myBlob = new Blob ();
        myBlob.blobify ("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/ToBeBlobbed.txt","/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/objects");
        System.out.println (myBlob.getSHA1String("/Users/chrisheadley/Desktop/Comp Sci/GitPrerequisites/ToBeBlobbed.txt"));
    }
}