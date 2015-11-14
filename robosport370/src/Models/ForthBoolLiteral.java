package Models;

public class ForthBoolLiteral implements ForthWord {

    private ForthWord nextWord;
    private boolean value;
    
    public ForthBoolLiteral(String wordString){
        if(wordString.equals("true")){
            this.value = true;
        } else {
            this.value = false;
        }
    }

    public boolean getValue(){
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

    public static boolean isThisKind(String wordString){
       return (wordString.equals("true") || wordString.equals("false"));
    }
    
    @Override
    public String forthStringEncoding(){
        if(value){
            return "true";
        } else {
            return "false";
        }
    }
    
    public String toString(){
        return "bool:" + forthStringEncoding();
    }

}
