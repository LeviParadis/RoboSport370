package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;

public class ForthDoLoop implements ForthWord {

    private Queue<ForthWord> commands;
    

    public ForthDoLoop(Queue<ForthWord> commandList) throws ForthParseException {
        commands = commandList; 
    }
    
    public Queue<ForthWord> getCommands(){
           return new LinkedList<ForthWord>(commands);
    }

    @Override
    public String forthStringEncoding() {
        String formatedString = "do";
        Iterator<ForthWord> it = commands.iterator();
        while(it.hasNext()){
            ForthWord next = it.next();
            formatedString = formatedString + " " +next.forthStringEncoding();
        }
        
        return formatedString + " loop";
    }

    @Override
    public String consoleFormat() {
        return "do_loop";
    }
    
    public String toString(){
        return consoleFormat();
    }
}
