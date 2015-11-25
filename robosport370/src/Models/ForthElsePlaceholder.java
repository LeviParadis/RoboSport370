package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthElsePlaceholder implements ForthWord {

    /**
     * constructs an else placeholder value
     * this is used when creating a conditional to know at which point to branch
     * @param item  a string representing the else value
     * @throws ForthParseException if the string dosn't represent the else word
     */
    public ForthElsePlaceholder(String item) throws ForthParseException {
        if(!isThisKind(item)){
            throw new ForthParseException("attempted to instantiate a forth else value with " + item);
        }
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        return "else";
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
     * @return whether or not the string represents an else value
     */
    public static boolean isThisKind(String item){
        return item.equals("else");
    }

}