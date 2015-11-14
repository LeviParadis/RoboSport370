package Models;

public class ForthPointerLiteral implements ForthWord {

    private String value;
    
    public ForthPointerLiteral(String pointerName){
        this.value = pointerName;
    }

    public String getPointer(){
        return value;
    }
    
    public String getVariableValue(Robot r){
        return r.getForthVariable(this.value);
    }
    
    public void setVariableValue(Robot r, String newValue){
        r.setForthVariable(this.value, newValue);
    }
    
    public void setVariableValue(Robot r, ForthWord newValue){
        String encoded = newValue.forthStringEncoding();
        r.setForthVariable(this.value, encoded);
    }


    public static boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthVariable(wordString) != null);
    }
    
    @Override
    public String forthStringEncoding(){
        return this.value;
    }
    
    @Override
    public String consoleFormat() {
        return this.forthStringEncoding();
    }
    
    public String toString(){
        return "var:" + forthStringEncoding();
    }
}
