package Interpreters;

import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Queue;
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
    /**
     * This class is used to run valid forth logic that has been parsed into the appropriate objects
     */
    
    public static final String INIT_WORD = "init";
    public static final String TURN_WORD = "turn";
    public static final String STACK_EXCEPTION_ERROR = "attempted to pop off an empty stack";
    
    public static boolean isPaused;
    
    //indicates whether the current robot has used it's shot. Reset on every execution
    private static boolean shotAvailable;
    //indicates how many moves the current robot has left. Reset on every execution
    private static long movesAvailable;

    //tester
    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = JsonInterpreter.robotFromJSON(json);
            System.out.println(INIT_WORD);
            initRobot(newRobot, null);
            System.out.println();
            System.out.println(TURN_WORD);
            executeTurn(newRobot, null);
            
        } catch (IOException | ParseException | ForthRunTimeException | ForthParseException e1) {
            e1.printStackTrace();
        }
    }

    
    /**
     * pauses the forth program.
     *  If this method is callled, the running turn won't progress until resume is called
     */
    public static void pause(){
        isPaused = true;
    }
    
    /**
     * resumes the forth program.
     *  If the program was previously paused, it will be resumed
     */
    public static void resume(){
        isPaused = false;
    }
    
    /**
     * Run's a robot's forth init logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    public static void initRobot(Robot robot, GameController controller) throws ForthRunTimeException, ForthParseException{
        //robot should not move or shoot in it's init method
        shotAvailable = false;
        movesAvailable = 0;
        isPaused = false;
        
        //find logic string
        String logicString = robot.getForthWord(INIT_WORD);
        //parse into forth words
        Queue<ForthWord> forthBody = ForthParser.parseForthBodyString(logicString, robot);
        //create empty stack for the program to use
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        try{
            //execute command
            executeForthCommand(forthBody, robot, forthStack, controller, null);
        } catch (LeaveLoopException e){
            //thrown if the program encounters a leave command, but it is not inside a loop
            throw new ForthRunTimeException("encountered the word leave ouside of a loop");
        }
    }
    
    /**
     * Run's a robot's forth turn logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     * @param delaySeconds      the amount of time to wait in between commands
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    public static void executeTurn(Robot robot, GameController controller) throws ForthRunTimeException, ForthParseException{
      //set initial values for shot and moves available
        movesAvailable = robot.getMovesPerTurn();
        shotAvailable = true;
        isPaused = false;
        
      //find logic string
        String logicString = robot.getForthWord(TURN_WORD);
      //parse into forth words
        Queue<ForthWord> forthBody = ForthParser.parseForthBodyString(logicString, robot);
        //create empty stack for the program to use
        Stack<ForthWord> forthStack = new Stack<ForthWord>();
        
        try{
            //execute command
            executeForthCommand(forthBody, robot, forthStack, controller, null);
        } catch (LeaveLoopException e){
            //thrown if the program encounters a leave command, but it is not inside a loop
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
            
            //implement a delay so the user can actually watch the match
            //the delay will be looped if the interpreter is paused, so no more instructions will be run
            do{
                //TODO: add in delay when game controller is implemented
                //float speed = GameController.getSpeed()
                //Thread.sleep(speed);
            } while(isPaused);
            
            
            //find the next command
            ForthWord nextItem = commandQueue.poll(); 
            
            //find what kind of word it is, and handle appropriately
            if(nextItem instanceof ForthBoolLiteral || nextItem instanceof ForthIntegerLiteral || nextItem instanceof ForthStringLiteral || nextItem instanceof ForthPointerLiteral){
                //literals are pushed to the stack
                forthStack.push(nextItem);
            } else if (nextItem instanceof ForthSystemWord){
                //system words are executed in the ForthSustemCommands class
                executeSystemCommand((ForthSystemWord)nextItem, forthStack, robot, controller);
            } else if (nextItem instanceof ForthCustomWord){
                //custom words are loaded from the robot, and executed in place
                String wordString = ((ForthCustomWord)nextItem).getWordLogic(robot);
                Queue<ForthWord> wordLogic = ForthParser.parseForthBodyString(wordString, robot);
                executeForthCommand(wordLogic, robot, forthStack, controller, null);
            } else if(nextItem instanceof ForthConditional){
                //conditionals are evaluated and run in place
                executeConditional((ForthConditional)nextItem, robot, forthStack, controller, loopNumber);
            } else if(nextItem instanceof ForthDoLoop){
                //loops are run in place
                executeDoLoop((ForthDoLoop)nextItem, robot, forthStack, controller);
            } else if(nextItem instanceof ForthUntilLoop){
                executeUntilLoop((ForthUntilLoop)nextItem, robot, forthStack, controller, loopNumber);
            } else if(nextItem instanceof ForthLoopNumber && loopNumber != null){
                //if the program is asking for the loop number, push it to the stack
                ForthWord runNum = new  ForthIntegerLiteral(loopNumber.intValue());
                forthStack.push(runNum);
            } else if(nextItem instanceof ForthLeaveLoop){
                //if the program is attempting to leave the loop, throw an exception to be caught in the loop handling methods
                throw new LeaveLoopException("encountered the word leave ouside of a loop");
            } else {
                //of nothing else works, throw an exception
               throw new ForthRunTimeException("found unexpected word: " + nextItem); 
            }
           
        }
    }
    
    /**
     * This method is called when the program encounters a conditional
     * @param conditional the conditional data
     * @param robot       the robot that is running the program
     * @param forthStack  the program's stack
     * @param controller  the game's controller
     * @param loopNumber  the loop index if the conditional is called from inside in a loop
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     * @throws LeaveLoopException if the forth word "leave" comes up, we throw an exception to be handled in the loop function
     */
    private static void executeConditional(ForthConditional conditional, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException, LeaveLoopException{
        try{
            ForthWord first = forthStack.pop();
            if(first instanceof ForthBoolLiteral){
                //find and execute the proper branch of the conditional
                boolean result = ((ForthBoolLiteral)first).getValue();
                Queue<ForthWord> commands = conditional.getCommandsForResult(result);
                executeForthCommand(commands, robot, forthStack, controller, loopNumber);
            } else {
              //if the stack isn't in an expected state, throw an exception
                throw new ForthRunTimeException("attempting to run an if statement without a bool on top of the stack");
            }
        } catch (EmptyStackException e){
            //if the stack can't be popped, throw a runtime exception
            throw new ForthRunTimeException(STACK_EXCEPTION_ERROR);
        }
    }
    
    /**
     * This method is called when the program encounters a do loop
     * @param conditional the loop data
     * @param robot       the robot that is running the program
     * @param forthStack  the program's stack
     * @param controller  the game's controller
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static void executeDoLoop(ForthDoLoop loop, Robot robot, Stack<ForthWord> forthStack, GameController controller) throws ForthRunTimeException, ForthParseException{
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
                //run the loop the desired number of times
                for(int i = start; i<=end; i++){
                    Queue<ForthWord> commands = loop.getCommands();
                    executeForthCommand(commands, robot, forthStack, controller, new Integer(i));
                }
            } else {
              //if the stack isn't in an expected state, throw an exception
                throw new ForthRunTimeException("attempting to run a do loop without two int values on top of the stack");
            }
        }catch (EmptyStackException e){
          //if the stack can't be popped, throw a runtime exception
            throw new ForthRunTimeException(STACK_EXCEPTION_ERROR);
        } catch (LeaveLoopException l){
            //if a lower level encountered the leave word, cancel the rest of the loop
            return;
        }
    }
    
    /**
     * This method is called when the program encounters an until loop
     * @param conditional the loop data
     * @param robot       the robot that is running the program
     * @param forthStack  the program's stack
     * @param controller  the game's controller
     * @param loopNumber  if this loop was embedded in a do loop, keep the loop index available
     * @throws ForthRunTimeException if there is an error that comes up while executing the forth code
     * @throws ForthParseException if the forth parser encounters a word it doesn't know how to handle
     */
    private static void executeUntilLoop(ForthUntilLoop loop, Robot robot, Stack<ForthWord> forthStack, GameController controller, Integer loopNumber) throws ForthRunTimeException, ForthParseException{
        boolean result = false;
        while(!result){
            try{
                //[p[ the latest result. If the result is still false, we will keep looping
                Queue<ForthWord> commands = loop.getCommands();
                executeForthCommand(commands, robot, forthStack, controller, loopNumber);
                ForthWord first = forthStack.pop();
                if(first instanceof ForthBoolLiteral){
                    result = ((ForthBoolLiteral) first).getValue();
                } else {
                  //if the stack isn't in an expected state, throw an exception
                    throw new ForthRunTimeException("attempting to run an until loop that doesn't end with a bool on top of the stack");  
                }
            }catch (EmptyStackException e){
              //if the stack can't be popped, throw a runtime exception
                throw new ForthRunTimeException(STACK_EXCEPTION_ERROR);
            } catch (LeaveLoopException l){
              //if a lower level encountered the leave word, cancel the rest of the loop
                return;
            } 
        }
    }
    /**
     * Will execute the logic for one of the standard built in forth commands
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
          throw new ForthRunTimeException(STACK_EXCEPTION_ERROR);
      }
  
   }
    
    
    
}
