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
    private HashMap <String, IndexEntry> nameAndEntryMap;

    public Index ()
    {   
        initialized = false;
        nameAndEntryMap = new HashMap <String, IndexEntry> ();
    }

    public void initialize (String pathName) throws IOException
    {
        File objects = new File(pathName + "/objects");
        if (!objects.exists())
            objects.mkdirs();

        File index = new File(pathName + "/index");
        if (!index.exists())
            index.createNewFile();

        initialized = true;
        this.pathName = pathName;
        pathToIndexString = pathName + "/index";
        pathToObjectsString = pathName + "/objects";
    }

    public void indexAddFile (String fileName) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before adding a Blob");
        }
        //creating Blob, updating hash map
        Blob addBlob = new Blob (pathName + "/" + fileName, pathName);
        nameAndEntryMap.put (fileName,new IndexEntry ("blob", addBlob.getSHA1String ()));
        updateIndex ();
    }

    public void indexAddDirectory (String folderName) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before adding a Directory");
        }
        Tree addTree = new Tree ();
        addTree.addDirectory (folderName);
        nameAndEntryMap.put(folderName,new IndexEntry ("tree",addTree.getUltimateTreeSHA1String ()));
        updateIndex ();
    }
    
    public void remove (String fileOrDirectoryName) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before removing a Blob");
        }
        if (!nameAndEntryMap.containsKey (fileOrDirectoryName))
        {
            throw new Exception ("Cannot remove a Blob that was never added");
        }
        Blob removeBlob = new Blob (pathName + "/" + fileOrDirectoryName, pathName);
        String SHAToRemove = removeBlob.getSHA1String();
        Path fileNamePath = Paths.get (pathToObjectsString + "/" + SHAToRemove);
        if (Files.notExists (fileNamePath))
        {
            throw new Exception ("This Blob was added, but no file exists with this path.");
        }
        nameAndEntryMap.remove (fileOrDirectoryName);
        File file = new File(pathToObjectsString + "/" + SHAToRemove);
        while (file.exists()) {
            file.delete();
        }
        updateIndex();
    }

    public void updateIndex () throws IOException
    {
        FileWriter writer = new FileWriter(pathToIndexString,false);
        PrintWriter out = new PrintWriter(writer);
        boolean first = true;
        for (String key : nameAndEntryMap.keySet ())
        {
            if (first)
            {
                out.print (nameAndEntryMap.get (key).getType () + " : " + nameAndEntryMap.get (key).getSHA1() + " : " + key);
                first = false;
            }
            else
            {
                out.print ("\n" + nameAndEntryMap.get (key).getType () + " : " + nameAndEntryMap.get (key).getSHA1() + " : " + key);
            }
        }
        writer.close ();
        out.close ();
/*
    
        int numDone = 0;
        for (String key : nameAndEntryMap.keySet ())
        {
            if (nameAndEntryMap.get (key).getType ().equals ("blob"))
            {
                if (numDone == 0)
                {
                    out.print ("blob : " + key + " : " + myMap.get (key).getFileName ());
                }
                else
                {
                    out.print ("\nblob : " + key + " : " + myMap.get (key).getFileName ());
                }
            }
            else
            {
                if (numDone == 0)
                {
                    out.print ("tree : " + key);
                }
                else
                {
                    out.print ("\ntree : " + key);
                }
            }
            numDone++;
        }
        writer.close();
        out.close ();
        //renaming file
        sha1 = SHA1FilePath("./temp");
        File file2 = new File ("./objects/" + sha1);
        myFile.renameTo (file2);
    }
         */

    }

    public HashMap <String, IndexEntry> getNameAndEntryMap ()
    {
        return nameAndEntryMap;
    }
}
