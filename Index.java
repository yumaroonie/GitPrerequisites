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
        clearMapIfFileCleared ();
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
        clearMapIfFileCleared ();
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
        clearMapIfFileCleared ();
        Blob removeBlob = new Blob (pathName + "/" + fileOrDirectoryName, pathName);
        String SHAToRemove = removeBlob.getSHA1String();
        Path fileNamePath = Paths.get (pathToObjectsString + "/" + SHAToRemove);
        if (Files.notExists (fileNamePath))
        {
            throw new Exception ("This Blob was added, but no file exists with this path.");
        }
        nameAndEntryMap.remove (fileOrDirectoryName);
        updateIndex();
    }

    public void deleteSavedFile (String fileToDelete) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before deleting a saved file");
        }
        clearMapIfFileCleared ();
        nameAndEntryMap.put (fileToDelete, new IndexEntry ("*deleted*", ""));
        updateIndex ();
    }

    public void editExistingSavedFile (String fileToEdit) throws Exception
    {
        if (!initialized)
        {
            throw new Exception ("Project must be initialized before editing a saved file");
        }
        clearMapIfFileCleared ();
        Blob addBlob = new Blob ("./" + fileToEdit, "./");
        nameAndEntryMap.remove (fileToEdit);
        nameAndEntryMap.put (fileToEdit, new IndexEntry ("*edited*", ""));
        updateIndex ();
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
                if (nameAndEntryMap.get (key).getType ().equals ("blob") || nameAndEntryMap.get(key).getType ().equals ("tree"))
                {
                    out.print (nameAndEntryMap.get (key).getType () + " : " + nameAndEntryMap.get (key).getSHA1() + " : " + key);
                }
                else//i.e. if editing or deleting
                {
                    out.print (nameAndEntryMap.get (key).getType () + " " + key);
                }
                first = false;
            }
            else
            {
                if (nameAndEntryMap.get (key).getType ().equals ("blob") || nameAndEntryMap.get(key).getType ().equals ("tree"))
                {
                    out.print ("\n" + nameAndEntryMap.get (key).getType () + " : " + nameAndEntryMap.get (key).getSHA1() + " : " + key);
                }
                else//i.e. if editing or deleting
                {
                    out.print ("\n" + nameAndEntryMap.get (key).getType () + " " + key);
                }
            }
        }
        writer.close ();
        out.close ();
    }

    public void clearMapIfFileCleared ()
    {
        File index = new File ("index");
        if (index.length () == 0)
        {
            nameAndEntryMap.clear ();
        }
    }

    public HashMap <String, IndexEntry> getNameAndEntryMap ()
    {
        return nameAndEntryMap;
    }
}
