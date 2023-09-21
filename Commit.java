public class Commit {
    String hashOfTree;
    String prevHash;
    String nextHash;
    String author;
    String date;
    String summary;

    public Commit(String sha1, String author, String date, String summary) throws Exception {
        Tree tree = new Tree();
        this.hashOfTree = tree.writeToFile();
        this.prevHash = sha1;
        this.nextHash = "";
        this.author = author;
        this.date = date;
        this.summary = summary;
    }

    public Commit(String author, String date, String summary) throws Exception {
        this("", author, date, summary);
    }

    
}
