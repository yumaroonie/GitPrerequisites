import java.io.File;

public class Tree {
    
    StringBuilder entries; 
    public Tree () {

        entries = new StringBuilder();
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();

    }

    public void add(String components) throws Exception {

        String type = components.substring(0, components.indexOf(":"));
        
        if (!entries.toString().contains(components)) {
            if (entries.isEmpty()) {
                entries.append(components);
            } else {
                entries.append("\n");
                entries.append(components);
            }
        }

    }
}
