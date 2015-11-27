package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthCustomWord implements ForthWord {
    private String value;
    
    /**
     * construct a new forth custom word
     * @param value  the word's value
     * @param r the robot running the program, so the custom word can be verified
     * @throws ForthParseException  thrown if the string doesn't represent one of the robot's words
     */
    public ForthCustomWord(String value, Robot r) throws ForthParseException{
        if(!isThisKind(value, r)){
            throw new ForthParseException("attempted to instantiate a forth custom word variable with " + value);
        }
        this.value = value;
    }

    /**
     * @return the custom variable's name
     */
    public String getName(){
        return value;
    }
    
    /**
     * @param r the robot that is running the program
     * @return a string representation of the custom word's forth logic
     */
    public String getWordLogic(Robot r){
        return r.getForthWord(this.value);
    }

    
    /**
     * @param wordString a plain text string
     * @param robot the robot that is running the program
     * @return whether or not the string represents a custom word
     */
    public static boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthWord(wordString) != null);
    }
    
    
    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding(){
        return this.value;
    }
    

    /**
     * @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "performing custom function: " + forthStringEncoding();
    }

}
