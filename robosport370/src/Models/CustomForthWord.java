package Models;

public class CustomForthWord implements ForthWord {

    
    private ForthWord nextWord;
    private String value;
    
    public CustomForthWord(String value){
        this.value = value;
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
    
    public static boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthWord(wordString) != null);
    }
    
    
    @Override
    public String forthStringEncoding(){
        return this.value;
    }

    public String toString(){
        return "custom:" + forthStringEncoding();
    }

}
