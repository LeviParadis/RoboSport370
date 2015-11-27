package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthIntegerLiteral implements ForthWord {

    private long value;
    
    /**
     * creates a forth integer word from a plain text string
     * @param wordString a string containing an integer
     * @throws ForthParseException thrown if the string doesn't represent an integer
     */
    public ForthIntegerLiteral(String wordString) throws ForthParseException{
        try{
            this.value = Integer.parseInt(wordString);
        } catch (NumberFormatException e){
            throw new ForthParseException("attempted to instantiate a forth integer with " + wordString);
        }
    }
    
    /**
     * creates a forth integer from a long value
     * @param value the interger to save as a forth word
     */
    public ForthIntegerLiteral(long value){
        this.value = value;
    }

    /**
     * @return the integer value saved in the forth word
     */
    public long getValue(){
        return value;
    }
    
    /**
     * @param wordString a plain text string
     * @return whether or not the string represents an integer value
     */
    public static boolean isThisKind(String wordString){
        try{
            Integer.parseInt(wordString);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding(){
        return Long.toString(this.value);
    }

    /**
     * @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "pushing int to stack: "  + forthStringEncoding();
    }
    
}
