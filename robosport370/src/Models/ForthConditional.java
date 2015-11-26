package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;

public class ForthConditional implements ForthWord {
    
    //the commands to be executed if it evaluates to true
    private Queue<ForthWord> trueCommands;
  //the commands to be executed if it evaluates to false
    private Queue<ForthWord> falseCommands;
    

    /**
     * parses through the input words, placing them into either command list
     * Commands are placed in the true branch until it encounters an else word,
     * then commands are placed in the false branch
     * @param stringList a list of forth words that are contained in the if statement
     * @throws ForthParseException thrown if it encounters multiple else statements
     */
    public ForthConditional(Queue<ForthWord> stringList) throws ForthParseException {
        Iterator<ForthWord> it = stringList.iterator();
        trueCommands = new LinkedList<ForthWord>();
        falseCommands = new LinkedList<ForthWord>();
        boolean foundElse = false;
        while(it.hasNext()){
            ForthWord next = it.next();
            if(next instanceof ForthElsePlaceholder){
                if(!foundElse){
                    foundElse = true;
                } else {
                    throw new ForthParseException("found multiple else statements within single if");
                }
            }else if(!foundElse){
                trueCommands.add(next);
            } else {
                falseCommands.add(next);
            }
        }
    }
    
    /**
     * @param result a bool indicating whether to return the true or the false branch
     * @return the list of words for the appropriate branch
     */
    public Queue<ForthWord> getCommandsForResult(boolean result){
        if(result){
            return trueCommands;
        } else {
            return falseCommands;
        }
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        String formatedString = "if";
        Iterator<ForthWord> itTrue = trueCommands.iterator();
        Iterator<ForthWord> itFalse = falseCommands.iterator();
        while(itTrue.hasNext()){
            ForthWord next = itTrue.next();
            formatedString = formatedString + " " +next.forthStringEncoding();
        }
        
        formatedString = formatedString + " else";
        
        while(itFalse.hasNext()){
            ForthWord next = itFalse.next();
            formatedString = formatedString + " " +next.forthStringEncoding();
        }
        return formatedString + " then";
    }


    /**
     * @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "encountered a conditional";
    }

}
