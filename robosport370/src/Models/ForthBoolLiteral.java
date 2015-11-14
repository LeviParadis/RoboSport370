package Models;

public class ForthBoolLiteral implements ForthWord {

    private boolean value;
    
    public ForthBoolLiteral(String wordString){
        if(wordString.equals("true")){
            this.value = true;
        } else {
            this.value = false;
        }
    }
    
    public ForthBoolLiteral(boolean value){
        this.value = value;
    }

    public boolean getValue(){
        return value;
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
    
    @Override
    public String consoleFormat() {
        return this.forthStringEncoding();
    }

}
