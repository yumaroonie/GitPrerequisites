import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Index 
{
    public Index ()
    {

    }

    public void initialize (String pathName) throws IOException
    {
        String pathToIndexString = pathName + "/index.txt";
        String pathToObjectsString = pathName + "/objects";
        Path indexPath = Paths.get (pathToIndexString);
        Path objectsPath = Paths.get (pathToObjectsString);
        if (Files.notExists (indexPath));
        {
            File indexFile = new File (pathToIndexString);
            indexFile.createNewFile();
           
            /**
            FileWriter writer = new FileWriter("AccountAudit.txt",false);
        PrintWriter out = new PrintWriter(writer);
        out.println(...)
        writer.close ();
        out.close ();
        */

        }
        if (Files.notExists (objectsPath));
        {
            File objectsDir = new File (pathToObjectsString);
            if (!objectsDir.exists()){
                objectsDir.mkdirs();
            }
        }
    }
}
