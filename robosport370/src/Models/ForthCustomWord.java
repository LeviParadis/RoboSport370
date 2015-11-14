package Models;

public class ForthCustomWord implements ForthWord {

    private String value;
    
    public ForthCustomWord(String value){
        this.value = value;
    }

    public String getName(){
        return value;
    }
    
    public String getWordLogic(Robot r){
        return r.getForthWord(this.value);
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
