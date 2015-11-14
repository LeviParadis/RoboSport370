package Models;

public class ForthPointerLiteral implements ForthWord {

    private ForthWord nextWord;
    private String value;
    
    public ForthPointerLiteral(String pointerName){
        this.value = pointerName;
    }

    public String getValue(){
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
        return true;
    }
    
    public boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthVariable(wordString) != null);
    }
    
    @Override
    public String forthStringEncoding(){
        return this.value;
    }
    
    public String toString(){
        return forthStringEncoding();
    }
}
