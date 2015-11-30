package Models;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Interfaces.ForthWord;

public class ForthDoLoop implements ForthWord {

    private Queue<ForthWord> commands;

    /**
     * constructs a do loop
     * 
     * @param commandList
     *            the list of commands to be repeated in the loop
     */
    public ForthDoLoop(Queue<ForthWord> commandList) {
        commands = commandList;
    }

    /**
     * @return the list of commands to be repeated
     */
    public Queue<ForthWord> getCommands() {
        return new LinkedList<ForthWord>(commands);
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        String formatedString = "do";
        Iterator<ForthWord> it = commands.iterator();
        while (it.hasNext()) {
            ForthWord next = it.next();
            formatedString = formatedString + " " + next.forthStringEncoding();
        }

        return formatedString + " loop";
    }

    /**
     * @return the string value that is printed by forth in the console
     */
    public String toString() {
        return "encountered do loop";
    }
}
