package Models;

public class ForthStringLiteral implements ForthWord {
    
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


    public static boolean isThisKind(String item){
        int length = item.length();
        return  (item.length() > 2 && item.charAt(0) == '.' && item.charAt(1) == '"' && item.charAt(length-1) == '"');
    }
    
    
    @Override
    public String forthStringEncoding(){
        return ".\"" + this.value + "\"";
    }

    public String toString(){
        return "string:" + forthStringEncoding();
    }
    
    @Override
    public String consoleFormat() {
        return this.value;
    }
}
