package Models;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthBoolLiteral implements ForthWord {

    private boolean value;

    /**
     * instantiate with a string
     * 
     * @param wordString
     *            a string containing true or false
     * @throws ForthParseException
     *             thrown if the string doesn't represent a bool
     */
    public ForthBoolLiteral(String wordString) throws ForthParseException {
        if (wordString.equals("true")) {
            this.value = true;
        } else if (wordString.equals("false")) {
            this.value = false;
        } else {
            throw new ForthParseException("attempted to instantiate a forth Bool with " + wordString);
        }
    }

    /**
     * Instantiate directly with a bool value
     * 
     * @param value
     *            the value to create a forth bool with
     */
    public ForthBoolLiteral(boolean value) {
        this.value = value;
    }

    /**
     * 
     * @return the bool value saved in the word
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @param wordString
     *            a plain text string
     * @return whether or not the string represents a bool value
     */
    public static boolean isThisKind(String wordString) {
        return (wordString.equals("true") || wordString.equals("false"));
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        if (value) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * @return the string value that is printed by forth in the console
     */
    public String toString() {
        return "pushing bool to stack: " + forthStringEncoding();
    }

}
