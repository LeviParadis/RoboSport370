package Models;

public class ForthLeaveLoop implements ForthWord {

    public ForthLeaveLoop() {
    }

    @Override
    public String forthStringEncoding() {
        return "leave";
    }

    @Override
    public String consoleFormat() {
        return forthStringEncoding();
    }
    
    public String toString(){
        return forthStringEncoding();
    }


    public static boolean isThisKind(String item){
        return item.equals("leave");
    }


}
