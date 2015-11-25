package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthLeaveLoop implements ForthWord {

    /**
     * constructs a word that will exit any loops it is run in
     * @param item  a string representing the leave value
     * @throws ForthParseException if the string dosn't represent the leave word
     */
    public ForthLeaveLoop(String item) throws ForthParseException {
        if(!isThisKind(item)){
            throw new ForthParseException("attempted to instantiate a forth else value with " + item);
        }
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        return "leave";
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
     * @return whether or not the string represents a leave value
     */
    public static boolean isThisKind(String item){
        return item.equals("leave");
    }


}
