package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;

public class ForthUntilLoop implements ForthWord {

    private Queue<ForthWord> commands;
    

    /**
     * constructs a do loop
     * @param commandList the list of commands to be repeated in the loop
     */
    public ForthUntilLoop(Queue<ForthWord> commandList) throws ForthParseException {
        commands = commandList; 
    }
    
    /**
     * @return the list of commands in the loop
     */
    public Queue<ForthWord> getCommands(){
           return new LinkedList<ForthWord>(commands);
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        String formatedString = "begin";
        Iterator<ForthWord> it = commands.iterator();
        while(it.hasNext()){
            ForthWord next = it.next();
            formatedString = formatedString + " " +next.forthStringEncoding();
        }
        
        return formatedString + " until";
    }

    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "encountered until loop";
    }

}
