package Models;

public class ForthStringLiteral implements ForthWord {
    
    private ForthWord nextWord;
    private String value;
    
    public ForthStringLiteral(String value){
        int length = value.length();
        if(value.length() > 2 && value.charAt(0) == '.' && value.charAt(1) == '"' && value.charAt(length-1) == '"'){
            this.value = value.substring(2, length-1);
        } else {
            this.value = value;
        }
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
    public boolean isThisKind(String item){
        int length = item.length();
        return  (item.length() > 2 && item.charAt(0) == '.' && item.charAt(1) == '"' && item.charAt(length-1) == '"');
    }
    
    
    @Override
    public String forthStringEncoding(){
        return '.' + '"' + this.value + '"';
    }

    public String toString(){
        return forthStringEncoding();
    }
}
