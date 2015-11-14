package Interpreters;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Controllers.GameController;
import Models.CustomForthWord;
import Models.ForthBoolLiteral;
import Models.ForthIntegerLiteral;
import Models.ForthPointerLiteral;
import Models.ForthStringLiteral;
import Models.ForthSystemWord;
import Models.ForthWord;
import Models.Robot;
import Models.SystemForthWord;

public class ForthInterpreter {

    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = JsonInterpreter.robotFromJSON(json);
            String max = newRobot.getForthVariable("maxRange");
            System.out.println(max);
            executeTurn(newRobot, null);
            max = newRobot.getForthVariable("maxRange");
            System.out.println(max);
            
        } catch (IOException | ParseException e1) {
            e1.printStackTrace();
        }
        
    }
    
    /**
     * Run's a robot's forth init logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     */
    public static void initRobot(Robot robot, GameController controller){
        String logicString = robot.getForthWord("init");
        Queue<ForthWord> forthBody = parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        System.out.println(forthBody);
        
        executeForthCommand(forthBody, robot, forthStack, controller);
    }
    
    /**
     * Run's a robot's forth turn logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     */
    public static void executeTurn(Robot robot, GameController controller){
        String logicString = robot.getForthWord("turn");
        Queue<ForthWord> forthBody = parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        System.out.println(forthBody);
        
        executeForthCommand(forthBody, robot, forthStack, controller);
    }
    
    /**
     * reads and executes forth commands
     * @param robot        the robot that is calling the command
     * @param command      the name of the forth word to execute
     * @param controller   the controller that control's the game 
     */
    private static void executeForthCommand(Queue<ForthWord> commandQueue, Robot robot, Stack<ForthWord> forthStack, GameController controller){
        while(!commandQueue.isEmpty()){
            ForthWord nextItem = commandQueue.poll();
           
        }
    }
    
    private static void executeSystemCommand(ForthSystemWord word, Stack<ForthWord> forthStack, Robot robot, GameController controller){
        SystemForthWord wordType = word.getType();
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
                 ForthIntegerLiteral healthWord = new ForthIntegerLiteral(health);
                 forthStack.push(healthWord);
                 break;
            case MOVES_LEFT:
              //returns the robot’s range of movement (0–3)
                //( -- i )
                //TODO: After game controller is set up
                break;
            case FIRE_POWER:
                //returns the robot’s firepower (1–3)
                //( -- i )
                long strength = robot.getStrength();
                ForthIntegerLiteral wordValue = new ForthIntegerLiteral(strength);
                forthStack.push(wordValue);
                break;
            case TEAM:
                //returns the robot’s team number
                //member ( -- i )
              //TODO: After game controller is set up
                break;
            case MEMBER:
                //returns the robot’s member number
                //member ( -- i )
              //TODO: After game controller is set up
                break;
            case CONSOLE:
                //prints the current value out, using the same syntax as they would be input with
                // ( v -- )
                ForthWord nextValue = forthStack.pop();
                String consoleString = nextValue.consoleFormat();
                System.out.println(consoleString);
                break;
            case RANDOM:
                //generates a random integer between 0 and i inclusive
                //( i -- )
                first = forthStack.pop();
                if(first instanceof ForthIntegerLiteral){
                    int i = (int)((ForthIntegerLiteral)first).getValue();
                    Random rand = new Random();
                    long r = rand.nextInt(i+1);
                    ForthIntegerLiteral newWord = new ForthIntegerLiteral(r);
                    forthStack.push(newWord);   
                } else {
                    //TODO: throw exception
                }
                break;
            case SHOOT:
                //fires the robot’s weapon at the space at range ir and direction id;
                //( id ir -- )
              //TODO: After game controller is set up
                break;
            case MOVE:
              //moves the robot to the space at range ir direction id, provided they have enough movesLeft;
                //( id ir -- )
              //TODO: After game controller is set up
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
                //TODO: After game controller is set up
                break;
            case MAIL_CHECK:
              //indicates whether the robot has a waiting message from team-member i
                // ( i -- b )
                //TODO: After game controller is set up
                break;
            case MAIL_RECIEVE:
                //pushes the next message value onto the stack.
                // ( i -- v )
                //TODO: After game controller is set up
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
                    //TODO: throw exception
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
  
    }
    

    
    private static Queue<ForthWord> parseForthBodyString(String logicString, Robot robot){
        String[] elements = logicString.split(" ");
        Queue<ForthWord> commandQueue = new LinkedList<ForthWord>();
        
        for(int i=0; i<elements.length; i++){
            String item = elements[i];
            //we need to keep strings of text together. If a section starts with .", keep the next sections together
            //until it reaches the closing ".
            if(item.length() > 1 && item.charAt(0) == '.' && item.charAt(1) == '"'){
                boolean stringEnd = false;
                while(!stringEnd && i<elements.length){
                    i++;
                    String nextItem = elements[i];
                    item = item + " " + nextItem;
                    stringEnd = nextItem.indexOf('"') > -1;
                }
            }
            //we now have a string value that represents a forth class
            ForthWord newWord = wordFromString(item, robot);
           
            commandQueue.add(newWord);
        }
        return commandQueue;
    }

    private static ForthWord wordFromString(String item, Robot robot){
        ForthWord newWord;
        if(ForthSystemWord.isThisKind(item)){
            newWord = new ForthSystemWord(item);
        } else if(ForthIntegerLiteral.isThisKind(item)){
            newWord = new ForthIntegerLiteral(item);
        } else if(ForthBoolLiteral.isThisKind(item)){
            newWord = new ForthBoolLiteral(item);
        } else if(ForthStringLiteral.isThisKind(item)){
            newWord = new ForthStringLiteral(item);
        } else if(ForthPointerLiteral.isThisKind(item, robot)){
            newWord = new ForthPointerLiteral(item);
        } else if (CustomForthWord.isThisKind(item, robot)){
            newWord = new CustomForthWord(item);
        } else {
            System.out.print("Could not find word: " + item);
            //TODO: add exception
            newWord = new ForthStringLiteral(item);
        }
        return newWord;
    }
}
