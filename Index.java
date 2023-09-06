import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Index 
{
    private String pathName;
    private String pathToIndexString;
    private String pathToObjectsString;
    private boolean initialized;
    private HashMap <String, String> nameAndSHAMap;

    public Index ()
    {
        initialized = false;
        nameAndSHAMap = new HashMap <String, String> ();
    }

    public void initialize (String pathName) throws IOException
    {
        initialized = true;
        this.pathName = pathName;
        pathToIndexString = pathName + "/index.txt";
        pathToObjectsString = pathName + "/objects";
        Path indexPath = Paths.get (pathToIndexString);
        Path objectsPath = Paths.get (pathToObjectsString);
        if (Files.notExists (indexPath));
        {
            File indexFile = new File (pathToIndexString);
            indexFile.createNewFile();
        }
        if (Files.notExists (objectsPath));
        {
            File objectsDir = new File (pathToObjectsString);
            if (!objectsDir.exists()){
                objectsDir.mkdirs();
            }
        }
    }

    public void add (String fileName) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before adding a Blob");
        }
        //creating Blob, updating hash map
        Blob addBlob = new Blob (pathName + "/" + fileName);
        addBlob.blobify (pathToObjectsString);
        nameAndSHAMap.put (fileName,addBlob.getSHA1String ());
        updateIndexTxt ();
    }
    
    public void remove (String fileName) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before removing a Blob");
        }
        if (!nameAndSHAMap.containsKey (fileName))
        {
            throw new Exception ("Cannot remove a Blob that was never added");
        }
        Blob removeBlob = new Blob (pathName + "/" + fileName);
        String SHAToRemove = removeBlob.getSHA1String();
        Path fileNamePath = Paths.get (pathToObjectsString + "/" + SHAToRemove);
        if (Files.notExists (fileNamePath))
        {
            throw new Exception ("This Blob was added, but no file exists with this path.");
        }
        nameAndSHAMap.remove (fileName);
        File file = new File(pathToObjectsString + "/" + SHAToRemove);
        file.delete();
        updateIndexTxt();
    }

    public void updateIndexTxt () throws IOException
    {
        FileWriter writer = new FileWriter(pathToIndexString,false);
        PrintWriter out = new PrintWriter(writer);
        for (String key : nameAndSHAMap.keySet ())
        {
            out.println (key + " : " + nameAndSHAMap.get (key));
        }
        writer.close ();
        out.close ();
    }
}