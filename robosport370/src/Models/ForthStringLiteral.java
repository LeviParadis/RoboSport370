package Models;

import Interfaces.ForthWord;

public class ForthStringLiteral implements ForthWord {
    
    private String value;
    
    /**
     * creates a string forth word using the input string
     * @param value the string representing the new forth word. Can be either forth formated (with .") or a plain text string
     */
    public ForthStringLiteral(String value){
        int length = value.length();
        if(value.length() > 2 && value.charAt(0) == '.' && value.charAt(1) == '"' && value.charAt(length-1) == '"'){
            this.value = value.substring(2, length-1);
        } else {
            this.value = value;
        }
    }

    /**
     * @return the value saved to the string
     */
    public String getValue(){
        return value;
    }


    /**
     * @param item a plain text string
     * @return whether or not the string represents a forth string value
     */
    public static boolean isThisKind(String item){
        int length = item.length();
        return  (item.length() > 2 && item.charAt(0) == '.' && item.charAt(1) == '"' && item.charAt(length-1) == '"');
    }
    
    
    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding(){
        return ".\"" + this.value + "\"";
    }

    /**
     * @return the string value that appears in the developer's console
     */
    public String toString(){
        return "string:" + forthStringEncoding();
    }
    
    @Override
    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String consoleFormat() {
        return this.value;
    }
}
