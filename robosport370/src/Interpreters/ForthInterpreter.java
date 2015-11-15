package Interpreters;

import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
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
import Exceptions.LeaveLoopException;
import Models.ForthBoolLiteral;
import Models.ForthConditional;
import Models.ForthCustomWord;
import Models.ForthDoLoop;
import Models.ForthIntegerLiteral;
import Models.ForthLeaveLoop;
import Models.ForthLoopNumber;
import Models.ForthPointerLiteral;
import Models.ForthStringLiteral;
import Models.ForthSystemWord;
import Models.ForthUntilLoop;
import Models.ForthWord;
import Models.Robot;

public class ForthInterpreter {
    
    private static boolean shotAvailable;
    private static long movesAvailable;

    //tester
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
        Queue<ForthWord> forthBody = ForthParser.parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        try{
            executeForthCommand(forthBody, robot, forthStack, controller, null);
        } catch (LeaveLoopException e){
            throw new ForthRunTimeException("encountered the word leave ouside of a loop");
        }
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
        Queue<ForthWord> forthBody = ForthParser.parseForthBodyString(logicString, robot);
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        try{
            executeForthCommand(forthBody, robot, forthStack, controller, null);
        } catch (LeaveLoopException e){
            throw new ForthRunTimeException("encountered the word leave ouside of a loop");
        }
    }
    
    
    /**
     * reads and executes forth commands
     * @param robot        the robot that is calling the command
     * @param command      the name of the forth word to execute
     * @param controller   the controller that control's the game 
     * @param loopNumber  the number of loop counter I if this command was executed as part of a loop
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     * @throws LeaveLoopException if the forth word "leave" comes up when a loop is being run
     */
    private static void executeForthCommand(Queue<ForthWord> commandQueue, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException, LeaveLoopException{
        while(!commandQueue.isEmpty()){
            //if the robot fired at it's own space and killed itself this turn, don't execute any more commands
            if(!robot.isAlive()){
                return;
            }
            ForthWord nextItem = commandQueue.poll(); 
            
            if(nextItem instanceof ForthBoolLiteral || nextItem instanceof ForthIntegerLiteral || nextItem instanceof ForthStringLiteral || nextItem instanceof ForthPointerLiteral){
                forthStack.push(nextItem);
            } else if (nextItem instanceof ForthSystemWord){
                executeSystemCommand((ForthSystemWord)nextItem, forthStack, robot, controller);
            } else if (nextItem instanceof ForthCustomWord){
                String wordString = ((ForthCustomWord)nextItem).getWordLogic(robot);
                Queue<ForthWord> wordLogic = ForthParser.parseForthBodyString(wordString, robot);
                executeForthCommand(wordLogic, robot, forthStack, controller, null);
            } else if(nextItem instanceof ForthConditional){
                executeConditional((ForthConditional)nextItem, robot, forthStack, controller, loopNumber);
            } else if(nextItem instanceof ForthDoLoop){
                executeDoLoop((ForthDoLoop)nextItem, robot, forthStack, controller, loopNumber);
            } else if(nextItem instanceof ForthUntilLoop){
                executeUntilLoop((ForthUntilLoop)nextItem, robot, forthStack, controller, loopNumber);
            } else if(nextItem instanceof ForthLoopNumber && loopNumber != null){
                ForthWord runNum = new  ForthIntegerLiteral(loopNumber.intValue());
                forthStack.push(runNum);
            } else if(nextItem instanceof ForthLeaveLoop){
                throw new LeaveLoopException("encountered the word leave ouside of a loop");
            } else {
               throw new ForthRunTimeException("found unexpected word: " + nextItem); 
            }
           
        }
    }
    
    private static void executeConditional(ForthConditional conditional, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException, LeaveLoopException{
        try{
            ForthWord first = forthStack.pop();
            if(first instanceof ForthBoolLiteral){
                boolean result = ((ForthBoolLiteral)first).getValue();
                Queue<ForthWord> commands = conditional.getCommandsForResult(result);
                executeForthCommand(commands, robot, forthStack, controller, loopNumber);
            } else {
                throw new ForthRunTimeException("attempting to run an if statement without a bool on top of the stack");
            }
        } catch (EmptyStackException e){
            throw new ForthRunTimeException("attempted to pop off an empty stack");
        }
    }
    
    private static void executeDoLoop(ForthDoLoop loop, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException{
        try{  
            ForthWord first = forthStack.pop();
            ForthWord second = forthStack.pop();
            if(first instanceof ForthIntegerLiteral && second instanceof ForthIntegerLiteral){
                int start = (int)((ForthIntegerLiteral)first).getValue();
                int end = (int)((ForthIntegerLiteral)second).getValue();
                //if end is smaller than start, the loop should run once
                if(end < start){
                    end = start;
                }
                
                for(int i = start; i<=end; i++){
                    Queue<ForthWord> commands = loop.getCommands();
                    executeForthCommand(commands, robot, forthStack, controller, new Integer(i));
                }
                
            } else {
                throw new ForthRunTimeException("attempting to run a do loop without two int values on top of the stack");
            }
        }catch (EmptyStackException e){
            throw new ForthRunTimeException("attempted to pop off an empty stack");
        } catch (LeaveLoopException l){
            return;
        }
    }
    
    private static void executeUntilLoop(ForthUntilLoop loop, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException{
        boolean result = false;
        while(!result){
            try{
                Queue<ForthWord> commands = loop.getCommands();
                executeForthCommand(commands, robot, forthStack, controller, loopNumber);
                ForthWord first = forthStack.pop();
                if(first instanceof ForthBoolLiteral){
                    result = ((ForthBoolLiteral) first).getValue();
                } else {
                    throw new ForthRunTimeException("attempting to run an until loop that doesn't end with a bool on top of the stack");  
                }
            }catch (EmptyStackException e){
                throw new ForthRunTimeException("attempted to pop off an empty stack");
            } catch (LeaveLoopException l){
                return;
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
        switch(wordType){
            case NOT_FORTH_WORD:
                break;
            case HEALTH:
                ForthSystemCommands.health(forthStack, robot);
                 break;
            case MOVES_LEFT:
                ForthSystemCommands.movesLeft(forthStack, movesAvailable);
                break;
            case FIRE_POWER:
                ForthSystemCommands.firePower(forthStack, robot);
                break;
            case TEAM:
                ForthSystemCommands.teamNumber(forthStack, robot);
                break;
            case MEMBER:
                ForthSystemCommands.memberNumber(forthStack, robot);
                break;
            case CONSOLE:
                ForthSystemCommands.console(forthStack);
                break;
            case RANDOM:
                ForthSystemCommands.random(forthStack);
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
                ForthSystemCommands.sendMail(forthStack);
                break;
            case MAIL_CHECK:
                ForthSystemCommands.checkMail(forthStack, robot);
                break;
            case MAIL_RECIEVE:
                ForthSystemCommands.recieveMail(forthStack, robot);
                break;
            case HEX:
                //returns the population of the given hex
                // ( id ir -- )
                //TODO: After game controller is set up
                break;
            case VAR_CHECK:
                ForthSystemCommands.checkVariable(forthStack, robot);
                break;
            case VAR_ASSIGN:
                ForthSystemCommands.assignVariable(forthStack, robot);
                break;
            case AND:
                ForthSystemCommands.and(forthStack);
                break;
            case OR:
                ForthSystemCommands.or(forthStack);
                break;
            case INVERT:
                ForthSystemCommands.invert(forthStack);
                break;
            case LESS:
                ForthSystemCommands.less(forthStack);
                break;
            case LESS_EQUAL:
                ForthSystemCommands.lessOrEqual(forthStack);
                break;
            case EQUAL:
                ForthSystemCommands.equal(forthStack);
                break;
            case NOT_EQUAL:
                ForthSystemCommands.notEqual(forthStack);
                break;
            case GREATER_EQUAL:
                ForthSystemCommands.greaterOrEqual(forthStack);
                break;
            case GREATER:
                ForthSystemCommands.greater(forthStack);
                break;
            case ADD:
                ForthSystemCommands.add(forthStack);
                break;
            case SUBTRACT:
                ForthSystemCommands.subtract(forthStack);
                 break;
            case MULTIPLY:
                ForthSystemCommands.multiply(forthStack);
                break;
            case DIVIDE:
                ForthSystemCommands.divide(forthStack);
                break;
            case DROP:
                ForthSystemCommands.drop(forthStack);
                break;
            case DUPLICATE:
                ForthSystemCommands.duplicate(forthStack);
                break;
            case ROTATE:
                ForthSystemCommands.rotate(forthStack);
                break;
            case SWAP:
                ForthSystemCommands.swap(forthStack);
                break;      
        }
        //if the stack ever fails to pop because it's run out of entries, it will throw
        //an empty stack exception. We can change it into our ForthRunTimeException
      } catch (EmptyStackException e){
          throw new ForthRunTimeException("attempted to pop off an empty stack");
      }
  
   }
    
    
}
