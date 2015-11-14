package Models;

public class ForthIntegerLiteral implements ForthWord {

    private long value;
    
    public ForthIntegerLiteral(String wordString){
        try{
            this.value = Integer.parseInt(wordString);
        } catch (NumberFormatException e){
            this.value =0;
        }
    }
    
    public ForthIntegerLiteral(long value){
        this.value = value;
    }

    public long getValue(){
        return value;
    }
    
    public static boolean isThisKind(String wordString){
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
        return "int:" + forthStringEncoding();
    }
    
    @Override
    public String consoleFormat() {
        return this.forthStringEncoding();
    }
}
