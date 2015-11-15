package Models;

public class ForthLoopNumber implements ForthWord {

    public ForthLoopNumber() {
    }

    @Override
    public String forthStringEncoding() {
        return "I";
    }

    @Override
    public String consoleFormat() {
        return forthStringEncoding();
    }
    
    public String toString(){
        return forthStringEncoding();
    }


    public static boolean isThisKind(String item){
        return item.equals("I");
    }
}
