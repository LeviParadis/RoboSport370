package Models;

public class ForthElsePlaceholder implements ForthWord {

    public ForthElsePlaceholder() {
    }

    @Override
    public String forthStringEncoding() {
        return "else";
    }

    @Override
    public String consoleFormat() {
        return forthStringEncoding();
    }
    
    public String toString(){
        return forthStringEncoding();
    }


    public static boolean isThisKind(String item){
        return item.equals("else");
    }

}