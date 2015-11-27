package Interpreters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ForthParseException;
import Interfaces.ForthWord;
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
import Models.Robot;

public class ForthParser {
    
    /**
     * This class is used to parse text into forth words
     */
    

    public static final String IF_START_WORD = "if";
    public static final String IF_END_WORD = "then";
    public static final String DO_LOOP_START_WORD = "do";
    public static final String DO_LOOP_END_WORD = "loop";
    public static final String UNTIL_LOOP_START_WORD = "begin";
    public static final String UNTIL_LOOP_END_WORD = "until";

    /**
     * Parses text into a sequence of forth word objects. Will throw an exception if it encounters a word it can't parse
     * @param logicString the string we want to convert into forth code
     * @param robot the robot that is executing the program
     * @return a queue of forth words that can be executed by the interpreter
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    protected static Queue<ForthWord> parseForthBodyString(String logicString, Robot robot) throws ForthParseException{
       //get a list of all words seperated by a space
        String[] elements = logicString.split(" ");
        //put the words into a queue
        Queue<String> forthStrings = new LinkedList<String>();
        for(int i=0; i<elements.length; i++){
            String item = elements[i];
            //if we find a forth string, keep it as one word
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
            //add the word to the queue
            forthStrings.add(item);
        }

        //start parsing the text into forth word objects
        Iterator<String> wordIterator = forthStrings.iterator();
        Queue<ForthWord> commandQueue = createWordList(forthStrings, wordIterator, robot, null);
        //return the list of forth words
        return commandQueue;
    }
    
    /**
     * Runs through a list of text words, and converts them into forth words
     * @param wordString   a queue of plain text words
     * @param iterator     used to keep track of our place in the list, so the method can be called recursively but retain placement
     * @param robot       the robot that is running the program
     * @param expectedEnding   if the function is called recursively, you can give an ending word that will cause the function to return immediately
     * @return a queue of forth words that can be run by the forth interpreter
     * @throws ForthParseException thrown if the forth text cannot be parsed
     */
    private static Queue<ForthWord> createWordList(Queue<String> wordString, Iterator<String> iterator, Robot robot, String expectedEnding) throws ForthParseException{
        //our ongoing list of commands
        Queue<ForthWord> commandQueue = new LinkedList<ForthWord>(); 
        //go through each word once
        while(iterator.hasNext()){
            String item = iterator.next();
            if(item.equals(IF_START_WORD)){
                //if we encounter an if statement, call the function recursively, and stop when we reach then
                //everything in between if and then will be collapsed into an if object
                Queue<ForthWord> ifStatement = createWordList(wordString, iterator, robot, IF_END_WORD);
                //create a new word containing all words in between if and then
                ForthWord newWord = new ForthConditional(ifStatement);
                commandQueue.add(newWord);
            } else if(item.equals(DO_LOOP_START_WORD)){
                //we do the same thing with the words between do and loop
                Queue<ForthWord> doLoop = createWordList(wordString, iterator, robot, DO_LOOP_END_WORD);
                ForthWord newWord = new ForthDoLoop(doLoop);
                commandQueue.add(newWord);
            } else if(item.equals(UNTIL_LOOP_START_WORD)){
              //we do the same thing with the words between begin and until
                Queue<ForthWord> untilLoop = createWordList(wordString, iterator, robot, UNTIL_LOOP_END_WORD);
                ForthWord newWord = new ForthUntilLoop(untilLoop);
                commandQueue.add(newWord);
            } else if(item.equals(expectedEnding)){
                //if we reach the designated ending word (then, loop, until, etc) return 
                //what we have collected so far immediately
                return commandQueue;
            } else {
                //if it is not a control flow word, use a seperate function to determine what forth word it is
                ForthWord newWord = wordFromString(item, robot);
                commandQueue.add(newWord);
            }
        }
        if(expectedEnding != null){
            //if we reach the end of the words without reaching the expected ending, something
            //went wrong with our parsing. Throw a forth parse exception
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
            newWord = new ForthElsePlaceholder(item);
        } else if(ForthLeaveLoop.isThisKind(item)){
            newWord = new ForthLeaveLoop(item);
        } else if(ForthLoopNumber.isThisKind(item)){
            newWord = new ForthLoopNumber(item);
        } else if(ForthPointerLiteral.isThisKind(item, robot)){
            newWord = new ForthPointerLiteral(item, robot);
        } else if (ForthCustomWord.isThisKind(item, robot)){
            newWord = new ForthCustomWord(item, robot);
        } else {
            throw new ForthParseException("Could not find meaning of forth word " + item);
        }
        return newWord;
    }
}
