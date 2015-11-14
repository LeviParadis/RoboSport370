package Models;

public class CustomForthWord implements ForthWord {

    private String value;
    
    public CustomForthWord(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    
    public static boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthWord(wordString) != null);
    }
    
    
    @Override
    public String forthStringEncoding(){
        return this.value;
    }
    
    @Override
    public String consoleFormat(){
       return this.value; 
    }

    public String toString(){
        return "custom:" + forthStringEncoding();
    }

}
