package Models;

public class ForthIntegerLiteral implements ForthWord {

    private ForthWord nextWord;
    private long value;
    
    public ForthIntegerLiteral(long value){
        this.value = value;
    }

    public long getValue(){
        return value;
    }

    @Override
    public boolean hasNext() {
        return nextWord==null;
    }

    @Override
    public ForthWord getNext() {
        return nextWord;
    }
    
    @Override
    public void setNextWord(ForthWord next){
        nextWord = next;
    }
    
    @Override
    public boolean isThisKind(String wordString){
        try{
            Integer.parseInt(wordString);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    @Override
    public String forthStringEncoding(){
        return Long.toString(this.value);
    }

    public String toString(){
        return forthStringEncoding();
    }
}
