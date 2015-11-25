package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthLoopNumber implements ForthWord {

    /**
     * constructs a word that will return the loop index
     * @param item  a string representing the index word (I)
     * @throws ForthParseException if the string dosn't represent the I word
     */
    public ForthLoopNumber(String item) throws ForthParseException {
        if(!isThisKind(item)){
            throw new ForthParseException("attempted to instantiate a forth index value with " + item);
        }
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        return "I";
    }

    @Override
    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String consoleFormat() {
        return forthStringEncoding();
    }
    
    /**
     * @return the string value that appears in the developer's console
     */
    public String toString(){
        return forthStringEncoding();
    }


    /**
     * @param item a plain text string
     * @return whether or not the string represents a loop index value
     */
    public static boolean isThisKind(String item){
        return item.equals("I");
    }
}
