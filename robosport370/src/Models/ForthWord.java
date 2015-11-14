package Models;

public interface ForthWord {
    public boolean hasNext();
    public ForthWord getNext();
    public void setNextWord(ForthWord next);
    public boolean isThisKind(String wordString);
    public String forthStringEncoding();
}
