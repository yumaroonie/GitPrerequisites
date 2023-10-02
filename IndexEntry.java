public class IndexEntry
{
    private String type;
    private String SHA1;

    public IndexEntry (String type, String SHA1)
    {
        this.type = type;
        this.SHA1 = SHA1;
    } 

    public String getType ()
    {
        return type;
    }

    public String getSHA1 ()
    {
        return SHA1;
    }
}