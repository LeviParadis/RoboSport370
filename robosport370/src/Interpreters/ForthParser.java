package Interpreters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;

import Models.ForthCustomWord;
import Models.ForthDoLoop;
import Models.ForthElsePlaceholder;
import Models.ForthBoolLiteral;
import Models.ForthConditional;
import Models.ForthIntegerLiteral;
import Models.ForthLeaveLoop;
import Models.ForthLoopNumber;
import Models.ForthPointerLiteral;
import Models.ForthStringLiteral;
import Models.ForthSystemWord;
import Models.ForthUntilLoop;
import Models.ForthWord;
import Models.Robot;

public class ForthParser {


    /**
     * Parses a string into a sequence of understood forth words. Will throw an exception if it encounters a word it can't parse
     * @param logicString the string we want to convert into forth code
     * @param robot the robot that is executing the program
     * @return a queue of forth words that can be executed by the interpreter
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    protected static Queue<ForthWord> parseForthBodyString(String logicString, Robot robot) throws ForthParseException{
       
        String[] elements = logicString.split(" ");
        Queue<String> forthStrings = new LinkedList<String>();
        for(int i=0; i<elements.length; i++){
            String item = elements[i];
            //we need to combine forth strings into a single word, even when there are spaces
            if(item.length() > 1 && item.charAt(0) == '.' && item.charAt(1) == '"' && item.charAt(item.length()-1) != '"'){
                boolean stringEnd = false;
                while(!stringEnd){
                    i++;
                    if(i >= elements.length){
                        //we have reached the end of all words without finding the closing "
                        throw new ForthParseException("could not find closing quote for forth string");
                    }
                    String nextItem = elements[i];
                    item = item + " " + nextItem;
                    stringEnd = (nextItem.charAt(nextItem.length()-1) == '"');
                }
            }
            forthStrings.add(item);
        }

        Iterator<String> wordIterator = forthStrings.iterator();
        Queue<ForthWord> commandQueue = createWordList(forthStrings, wordIterator, robot, null);
        return commandQueue;
    }
    
    private static Queue<ForthWord> createWordList(Queue<String> wordString, Iterator<String> iterator, Robot robot, String expectedEnding) throws ForthParseException{
        Queue<ForthWord> commandQueue = new LinkedList<ForthWord>(); 
        while(iterator.hasNext()){
            String item = iterator.next();
            
            if(item.equals("if")){
                Queue<ForthWord> ifStatement = createWordList(wordString, iterator, robot, "then");
                ForthWord newWord = new ForthConditional(ifStatement);
                commandQueue.add(newWord);
            } else if(item.equals("do")){
                Queue<ForthWord> doLoop = createWordList(wordString, iterator, robot, "loop");
                ForthWord newWord = new ForthDoLoop(doLoop);
                commandQueue.add(newWord);
            } else if(item.equals("begin")){
                Queue<ForthWord> untilLoop = createWordList(wordString, iterator, robot, "until");
                ForthWord newWord = new ForthUntilLoop(untilLoop);
                commandQueue.add(newWord);
            } else if(item.equals(expectedEnding)){
                return commandQueue;
            } else {
                ForthWord newWord = wordFromString(item, robot);
                commandQueue.add(newWord);
            }
        }
        if(expectedEnding != null){
            throw new ForthParseException("Could not parse if/loop statements");
        }
        return commandQueue;
    }
    


    /**
     * Maps a string into the most likely forth word it represents
     * @param item  the string we are parsing
     * @param robot the robot that executed this code
     * @return a new ForthWord from the string
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    protected static ForthWord wordFromString(String item, Robot robot) throws ForthParseException{
        ForthWord newWord;
        if(ForthSystemWord.isThisKind(item)){
            newWord = new ForthSystemWord(item);
        } else if(ForthIntegerLiteral.isThisKind(item)){
            newWord = new ForthIntegerLiteral(item);
        } else if(ForthBoolLiteral.isThisKind(item)){
            newWord = new ForthBoolLiteral(item);
        } else if(ForthStringLiteral.isThisKind(item)){
            newWord = new ForthStringLiteral(item);
        } else if(ForthElsePlaceholder.isThisKind(item)){
            newWord = new ForthElsePlaceholder();
        } else if(ForthLeaveLoop.isThisKind(item)){
            newWord = new ForthLeaveLoop();
        } else if(ForthLoopNumber.isThisKind(item)){
            newWord = new ForthLoopNumber();
        } else if(ForthPointerLiteral.isThisKind(item, robot)){
            newWord = new ForthPointerLiteral(item);
        } else if (ForthCustomWord.isThisKind(item, robot)){
            newWord = new ForthCustomWord(item);
        } else {
            throw new ForthParseException("Could not find meaning of forth word " + item);
        }
        return newWord;
    }
}
