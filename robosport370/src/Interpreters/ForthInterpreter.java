package Interpreters;

import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Controllers.GameController;
import Enums.SystemCommandType;
import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Models.ForthCustomWord;
import Models.ForthElsePlaceholder;
import Models.ForthBoolLiteral;
import Models.ForthConditional;
import Models.ForthIntegerLiteral;
import Models.ForthPointerLiteral;
import Models.ForthStringLiteral;
import Models.ForthSystemWord;
import Models.ForthWord;
import Models.Robot;

public class ForthInterpreter {
    
    private static boolean shotAvailable;
    private static long movesAvailable;

    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = JsonInterpreter.robotFromJSON(json);
            System.out.println("init");
            initRobot(newRobot, null);
            System.out.println();
            System.out.println("turn");
            executeTurn(newRobot, null);
            
        } catch (IOException | ParseException | ForthRunTimeException | ForthParseException e1) {
            e1.printStackTrace();
        }
        
    }
    
    /**
     * Run's a robot's forth init logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    public static void initRobot(Robot robot, GameController controller) throws ForthRunTimeException, ForthParseException{
        shotAvailable = false;
        movesAvailable = 0;
        
        String logicString = robot.getForthWord("init");
        Queue<ForthWord> forthBody = parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        executeForthCommand(forthBody, robot, forthStack, controller);
    }
    
    /**
     * Run's a robot's forth turn logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    public static void executeTurn(Robot robot, GameController controller) throws ForthRunTimeException, ForthParseException{
        movesAvailable = robot.getMovesPerTurn();
        shotAvailable = true;
        
        String logicString = robot.getForthWord("turn");
        Queue<ForthWord> forthBody = parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        System.out.println(forthBody);
        
        executeForthCommand(forthBody, robot, forthStack, controller);
        System.out.println(forthStack);
    }
    
    
    
    
    
    
    
    
    
    /**
     * reads and executes forth commands
     * @param robot        the robot that is calling the command
     * @param command      the name of the forth word to execute
     * @param controller   the controller that control's the game 
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static void executeForthCommand(Queue<ForthWord> commandQueue, Robot robot, Stack<ForthWord> forthStack, GameController controller) throws ForthRunTimeException, ForthParseException{
        while(!commandQueue.isEmpty()){
            //if the robot fired at it's own space and killed itself this turn, don't execute any more commands
            if(!robot.isAlive()){
                return;
            }
            ForthWord nextItem = commandQueue.poll();
            
            System.out.println(forthStack);
            System.out.println(nextItem);
            System.out.println();
            
            
            if(nextItem instanceof ForthBoolLiteral || nextItem instanceof ForthIntegerLiteral || nextItem instanceof ForthStringLiteral || nextItem instanceof ForthPointerLiteral){
                forthStack.push(nextItem);
            } else if (nextItem instanceof ForthSystemWord){
                executeSystemCommand((ForthSystemWord)nextItem, forthStack, robot, controller);
            } else if (nextItem instanceof ForthCustomWord){
                String wordString = ((ForthCustomWord)nextItem).getWordLogic(robot);
                Queue<ForthWord> wordLogic = parseForthBodyString(wordString, robot);
                executeForthCommand(wordLogic, robot, forthStack, controller);
            } else if(nextItem instanceof ForthConditional){
                ForthWord first = forthStack.pop();
                if(first instanceof ForthBoolLiteral){
                    boolean result = ((ForthBoolLiteral)first).getValue();
                    Queue<ForthWord> commands = ((ForthConditional)nextItem).getCommandsForResult(result);
                    executeForthCommand(commands, robot, forthStack, controller);
                } else {
                    throw new ForthRunTimeException("attempting to rn an if statement without a bool on top of the stack");
                }
            }
           
        }
    }
    
    /**
     * Will run logic for one of the built in forth command used as part of the standard
     * @param word         the system word we want to run
     * @param forthStack  the current stack of forth literals
     * @param robot      the robot that is executing this code
     * @param controller  the game controller
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static void executeSystemCommand(ForthSystemWord word, Stack<ForthWord> forthStack, Robot robot, GameController controller) throws ForthRunTimeException, ForthParseException{
      try{      
        SystemCommandType wordType = word.getType();
        ForthWord first;
        ForthWord second;
        ForthWord third;
        ForthWord result;
        switch(wordType){
            case NOT_FORTH_WORD:
                break;
            case HEALTH:
                //returns the robot’s current health (1–3)
                // ( -- i )
                 long health = robot.getHealth();
                 result = new ForthIntegerLiteral(health);
                 forthStack.push(result);
                 break;
            case MOVES_LEFT:
              //returns the robot’s range of movement (0–3)
                result = new ForthIntegerLiteral(movesAvailable);
                forthStack.push(result);
                break;
            case FIRE_POWER:
                //returns the robot’s firepower (1–3)
                //( -- i )
                long strength = robot.getStrength();
                result = new ForthIntegerLiteral(strength);
                forthStack.push(result);
                break;
            case TEAM:
                //returns the robot’s team number
                //member ( -- i )
                long team = robot.getTeamNumber();
                result = new ForthIntegerLiteral(team);
                forthStack.push(result);
                break;
            case MEMBER:
                //returns the robot’s member number
                //member ( -- i )
                long member = robot.getMemberNumber();
                result = new ForthIntegerLiteral(member);
                forthStack.push(result);
                break;
            case CONSOLE:
                //prints the current value out, using the same syntax as they would be input with
                // ( v -- )
                first = forthStack.pop();
                String consoleString = first.consoleFormat();
                System.out.println(consoleString);
                break;
            case RANDOM:
                //generates a random integer between 0 and i inclusive
                //( i -- )
                first = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && ((ForthIntegerLiteral)first).getValue() >= 0){
                    int i = (int)((ForthIntegerLiteral)first).getValue();
                    Random rand = new Random();
                    long r = rand.nextInt(i+1);
                    ForthIntegerLiteral newWord = new ForthIntegerLiteral(r);
                    forthStack.push(newWord);   
                } else {
                    throw new ForthRunTimeException("random word called without a positive int on top of the stack");
                }
                break;
            case SHOOT:
                //fires the robot’s weapon at the space at range ir and direction id;
                //( id ir -- )
                if(shotAvailable){
                    //TODO: After game controller is set up   
                } else {
                    System.out.println("attempted shot, but shot was already used");
                }
                break;
            case MOVE:
              //moves the robot to the space at range ir direction id, provided they have enough movesLeft;
                //( id ir -- )
                if(movesAvailable > 0){
                    movesAvailable--;
                  //TODO: After game controller is set up
                } else {
                    System.out.println("attempted to move, but already moved " + robot.getMovesPerTurn() + " times");
                }
                break;
            case SCAN:
                //scans for the nearest robots, and reports how many targets visible, up to four.
                //( -- i )
              //TODO: After game controller is set up
                break;
            case IDENTIFY:
                //identifies the given target, giving its team number (it), range (ir), direction (id), and health (ih).
                // ( i -- it ir id ih )
              //TODO: After game controller is set up
                break;
            case MAIL_SEND:
                //send a value v to team-member i; returns a boolean indicating success or failure
                // ( i v -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if((first instanceof ForthIntegerLiteral || first instanceof ForthBoolLiteral || first instanceof ForthStringLiteral) && second instanceof ForthIntegerLiteral){
                    int memberNumber = (int)((ForthIntegerLiteral)second).getValue();
                  //TODO: After game controller is set up
                } else {
                    throw new ForthRunTimeException("attempting to send mailbox without the proper stack format");
                }
                break;
            case MAIL_CHECK:
              //indicates whether the robot has a waiting message from team-member i
                // ( i -- b )
                first = forthStack.pop();
                if(first instanceof ForthIntegerLiteral){
                    int memberNumber = (int)((ForthIntegerLiteral)first).getValue();
                    boolean hasMail = robot.hasMailFromMember(memberNumber);
                    result = new ForthBoolLiteral(hasMail);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("attempting to check mailbox without an int on top of the stack");
                }
                break;
            case MAIL_RECIEVE:
                //pushes the next message value onto the stack.
                // ( i -- v )
                first = forthStack.pop();
                if(first instanceof ForthIntegerLiteral){
                    int memberNumber = (int)((ForthIntegerLiteral)first).getValue();
                    ForthWord mail = robot.popMailFromMember(memberNumber);
                    forthStack.push(mail);
                } else {
                    throw new ForthRunTimeException("attempting to get mail without an int on top of the stack");
                }
                break;
            case HEX:
                //returns the population of the given hex
                // ( id ir -- )
                //TODO: After game controller is set up
                break;
            case VAR_CHECK:
                //takes a variable and returns the value it’s storing
                // ( p -- v )
                first = forthStack.pop();
                if(first instanceof ForthPointerLiteral){
                    String value = ((ForthPointerLiteral) first).getVariableValue(robot);
                    result = wordFromString(value, robot);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("? word called without a variable pointer on top of the stack");
                }
                break;
            case VAR_ASSIGN:
                //stores a new value into a pointer
                //( v p -- )
                first = forthStack.pop();
                second = forthStack.pop();
                
                if(first instanceof ForthPointerLiteral){
                    ((ForthPointerLiteral) first).setVariableValue(robot, second);
                } else {
                    throw new ForthRunTimeException("! word called without a variable pointer on top of the stack");
                }
                break;
            case AND:
              //false if either boolean is false, true otherwise
                // ( b b -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthBoolLiteral && second instanceof ForthBoolLiteral){
                    boolean firstBool = ((ForthBoolLiteral) first).getValue();
                    boolean secondBool = ((ForthBoolLiteral) second).getValue();
                    result = new ForthBoolLiteral(firstBool && secondBool);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("AND word called without two booleans on top of the stack");
                }
                break;
            case OR:
                //true if either boolean is true, false otherwise
                // ( b b -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthBoolLiteral && second instanceof ForthBoolLiteral){
                    boolean firstBool = ((ForthBoolLiteral) first).getValue();
                    boolean secondBool = ((ForthBoolLiteral) second).getValue();
                    result = new ForthBoolLiteral(firstBool || secondBool);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("OR word called without two booleans on top of the stack");
                }
                break;
            case INVERT:
              //invert the given boolean
                // ( b -- b )
                first = forthStack.pop();
                if(first instanceof ForthBoolLiteral){
                    boolean firstBool = ((ForthBoolLiteral) first).getValue();
                    result = new ForthBoolLiteral(!firstBool);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("invert word called without a boolean on top of the stack");
                }
                break;
            case LESS:
                //i2 is less than i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt < firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("< word called without two ints on top of the stack");
                }
                break;
            case LESS_EQUAL:
              //i2 is not more than i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt <= firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("<= word called without two ints on top of the stack");
                }
                break;
            case EQUAL:
                //i2 is equal to i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt == firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("= word called without two ints on top of the stack");
                }
                break;
            case NOT_EQUAL:
                //i2 is not equal to i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt != firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("<> word called without two ints on top of the stack");
                }
                break;
            case GREATER_EQUAL:
              //i2 is at least i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt >= firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException(">= word called without two ints on top of the stack");
                }
                break;
            case GREATER:
                //i2 is more than i1
                // ( i2 i1 -- b )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthBoolLiteral(secondInt > firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("> word called without two ints on top of the stack");
                }
                break;
            case ADD:
              //add the two integers, pushing their sum on the stack
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthIntegerLiteral(secondInt + firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("+ word called without two ints on top of the stack");
                }
                break;
            case SUBTRACT:
              //subtract the top integer from the next, pushing their difference on the stack
                //( i2 i1 -- i )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthIntegerLiteral(secondInt - firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("- word called without two ints on top of the stack");
                }
                 break;
            case MULTIPLY:
                //multiply the two top integers, pushing their product on the stack
                //( i i -- i )
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    result = new ForthIntegerLiteral(secondInt * firstInt);
                    forthStack.push(result);
                } else {
                    throw new ForthRunTimeException("* word called without two ints on top of the stack");
                }
                break;
            case DIVIDE:
              //divide the top integer into the next, pushing the remainder and quotient
                // ( iv ie -- iq ir)
                first = forthStack.pop();
                second = forthStack.pop();
                if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                    long firstInt = ((ForthIntegerLiteral) first).getValue();
                    long secondInt = ((ForthIntegerLiteral) second).getValue();
                    ForthWord iq = new ForthIntegerLiteral(secondInt / firstInt);
                    ForthWord ir = new ForthIntegerLiteral(secondInt % firstInt);
                    forthStack.push(ir);
                    forthStack.push(iq);
                } else {
                    throw new ForthRunTimeException("/mod word called without two ints on top of the stack");
                }
                break;
            case DROP:
                //remove the value at the top of the stack
                //( v -- )
                forthStack.pop();
                break;
            case DUPLICATE:
                //duplicate the value at the top of the stack
                //( v -- v v )
                first = forthStack.pop();
                forthStack.push(first);
                forthStack.push(first);
                break;
            case ROTATE:
                //rotate first three items on stack
                // ( v3 v2 v1 -- v3 v1 v2 ) 
               first = forthStack.pop();
               second = forthStack.pop();
               third = forthStack.pop();
               forthStack.push(second);
               forthStack.push(first);
               forthStack.push(third);
                break;
            case SWAP:
                //swap the two values at the top of the stack
                //( v2 v1 -- v2 v1 )
                first = forthStack.pop();
                second = forthStack.pop();
                forthStack.push(first);
                forthStack.push(second);
                break;      
        }
        //if the stack ever fails to pop because it's run out of entries, it will throw
        //an empty stack exception. We can change it into our ForthRunTimeException
      } catch (EmptyStackException e){
          throw new ForthRunTimeException("attempted to pop off an empty stack");
      }
  
   }
    

    /**
     * Parses a stiring into a sequence of understood forth words. Will throw an exception if it encounters a word it can't parse
     * @param logicString the string we want to convert into forth code
     * @param robot the robot that is executing the program
     * @return a queue of forth words that can be executed by the interpreter
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static Queue<ForthWord> parseForthBodyString(String logicString, Robot robot) throws ForthParseException{
       
        Queue<String> forthStrings = findForthWords(logicString);

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
     * takes as input a string of forth values, and breaks it up into an array of potential forth words
     * keeps forth strings together as a single block, but separates everything out into it's own word
     * @param forthString  a string of forth commands in text format
     * @return             a queue of potential forth words ready for parsing, but still in string format
     * @throws ForthParseException thrown if the parser can't find a closing quote for every opener
     */
    private static Queue<String> findForthWords(String forthString) throws ForthParseException{
        String[] elements = forthString.split(" ");
        Queue<String> stringList = new LinkedList<String>();
        for(int i=0; i<elements.length; i++){
            String item = elements[i];
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
            stringList.add(item);
        }
        return stringList;
    }

    /**
     * Maps a string into the most likely forth word it represents
     * @param item  the string we are parsing
     * @param robot the robot that executed this code
     * @return a new ForthWord from the string
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static ForthWord wordFromString(String item, Robot robot) throws ForthParseException{
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
