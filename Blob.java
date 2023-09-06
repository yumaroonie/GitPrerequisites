import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

public class Blob
{
    private String SHA1;

    public void blobify (String inputFileName, String objectsPath) throws NoSuchAlgorithmException, IOException
    {
        //reads file into String myString
        Scanner scanner = new Scanner(new File(inputFileName));
        String myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        //hashes file with SHA1 hash code into String called SHA1)
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(myString.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest())
        {
            formatter.format("%02x", b);
        }
        SHA1 = formatter.toString();
        formatter.close();
        //writes new file into objects folder
        FileWriter writer = new FileWriter(new File (objectsPath + "/" + SHA1));
        PrintWriter out = new PrintWriter(writer);
        out.println (myString);
        writer.close ();
        out.close ();
    }

    public String getSHA1String ()
    {
        return SHA1;
    }
}