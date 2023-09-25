import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

public class Blob
{
    private String inputFilePath;
    private String myString;

    public Blob (String inputFilePath, String pathName) throws Exception
    {
        
        File objects = new File(pathName + "/objects");
        if (!objects.exists())
            objects.mkdirs();
        
        this.inputFilePath = inputFilePath;
        blobify(pathName + "/objects");

    }

    public void blobify (String objectsPath) throws NoSuchAlgorithmException, IOException
    {
        //writes new file into objects folder
        FileWriter writer = new FileWriter (new File (objectsPath + "/" + getSHA1String()));
        PrintWriter out = new PrintWriter(writer);

        out.print (myString);
        writer.close ();
        out.close ();
    }

    public String getSHA1String () throws FileNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException
    {
        File fileToRead = new File (inputFilePath);
        if (fileToRead.length () == 0)
        {
            myString = "";
            return "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        }
        //reads file into String myString
        Scanner scanner = new Scanner(fileToRead);
        myString = scanner.useDelimiter("\\A").next();
        scanner.close();
        //hashes file with SHA1 hash code into String called SHA1
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(myString.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        for (byte b : crypt.digest())
        {
            formatter.format("%02x", b);
        }
        String SHA1 = formatter.toString();
        formatter.close();
        return SHA1;
    }
}