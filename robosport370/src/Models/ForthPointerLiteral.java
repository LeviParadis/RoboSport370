package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthPointerLiteral implements ForthWord {

    private String value;
    
    
    /**
     * construct a new forth variable pointer
     * @param value  the pointer's name
     * @param r the robot running the program, so the variable can be verified
     * @throws ForthParseException  thrown if the string doesn't represent one of the robot's variables
     */
    public ForthPointerLiteral(String pointerName, Robot r) throws ForthParseException{
        if(!isThisKind(pointerName, r)){
            throw new ForthParseException("attempted to instantiate a forth variable pointer with " + value);
        }
        this.value = pointerName;
    }

    /**
     * @return the name of the pounter
     */
    public String getPointer(){
        return value;
    }
    
    /**
     * @param r the robot that is running the program
     * @return the value saved in the variable
     */
    public String getVariableValue(Robot r){
        return r.getForthVariable(this.value);
    }
    
    /**
     * sets the forth variable to a new value using a string
     * @param r   the robot that is running the program
     * @param newValue the new value to save to the variable
     */
    public void setVariableValue(Robot r, String newValue){
        r.setForthVariable(this.value, newValue);
    }
    
    /**
     * sets the forth variable to a new forth word
     * @param r   the robot that is running the program
     * @param newValue the new value to save to the variable
     */
    public void setVariableValue(Robot r, ForthWord newValue){
        String encoded = newValue.forthStringEncoding();
        r.setForthVariable(this.value, encoded);
    }


    /**
     * @param wordString a plain text string
     * @return whether or not the string represents a variable pointer
     */
    public static boolean isThisKind(String wordString, Robot robot){
        return (robot.getForthVariable(wordString) != null);
    }
    
    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding(){
        return this.value;
    }
    
    
    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "pushing variable to the stack: " + forthStringEncoding();
    }
}
