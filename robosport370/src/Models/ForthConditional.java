package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;

public class ForthConditional implements ForthWord {
    
    private Queue<ForthWord> trueCommands;
    private Queue<ForthWord> falseCommands;
    

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
    
    public Queue<ForthWord> getCommandsForResult(boolean result){
        if(result){
            return trueCommands;
        } else {
            return falseCommands;
        }
    }

    @Override
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

    @Override
    public String consoleFormat() {
        return "if_statement";
    }
    
    public String toString(){
        return forthStringEncoding();
    }

}
