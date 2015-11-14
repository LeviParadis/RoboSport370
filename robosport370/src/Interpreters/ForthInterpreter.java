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
    /*
    private static boolean executeSystemCommand(String wordName, Stack<ForthWord> forthStack, Robot robot, GameController controller){
        if(wordName.equals("health")){
            //returns the robot’s current health (1–3)
           // ( -- i )
            long health = robot.getHealth();
            forthStack.push(Long.toString(health));
            return true;
        } else if(wordName.equals("movesLeft")){
            //returns the robot’s range of movement (0–3)
            //( -- i )
            //TODO: After game controller is set up
            return true;
        }else if(wordName.equals("firepower")){
            //returns the robot’s firepower (1–3)
            //( -- i )
            long strength = robot.getStrength();
            forthStack.push(Long.toString(strength));
            return true;
        }else if(wordName.equals("team")){
          //returns the robot’s team number
            //member ( -- i )
          //TODO: After game controller is set up
            return true;
        }else if(wordName.equals("member")){
            //returns the robot’s member number
            //member ( -- i )
          //TODO: After game controller is set up
            return true;
        }else if(wordName.equals(".")){
            //prints the current value out, using the same syntax as they would be input with
            // ( v -- )
            String nextValue = forthStack.pop();
            System.out.println(nextValue);
            return true;
        }else if(wordName.equals("random")){
            //generates a random integer between 0 and i inclusive
            //( i -- )
            int i = Integer.parseInt(forthStack.pop());
            Random rand = new Random();
            int r = rand.nextInt(i+1);
            forthStack.push(Integer.toString(r));
            return true;
        }else if(wordName.equals("shoot!")){
            //fires the robot’s weapon at the space at range ir and direction id;
            //( id ir -- )
          //TODO: After game controller is set up
            return true;
        }else if(wordName.equals("move!")){
            //moves the robot to the space at range ir direction id, provided they have enough movesLeft;
            //( id ir -- )
          //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("scan!")){
            //scans for the nearest robots, and reports how many targets visible, up to four.
            //( -- i )
          //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("identify!")){
            //identifies the given target, giving its team number (it), range (ir), direction (id), and health (ih).
            // ( i -- it ir id ih )
          //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("send!")){
            //send a value v to team-member i; returns a boolean indicating success or failure
            // ( i v -- b )
            //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("mesg?")){
            //indicates whether the robot has a waiting message from team-member i
            // ( i -- b )
            //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("recv!")){
            //pushes the next message value onto the stack.
            // ( i -- v )
            //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("hex")){
            //returns the population of the given hex
            // ( id ir -- )
            //TODO: After game controller is set up
            return true;
        } else if(wordName.equals("?")){
            //takes a variable and returns the value it’s storing
            // ( p -- v )
            String pointer = forthStack.pop();
            String value = robot.getForthVariable(pointer);
            forthStack.push(value);
            return true;
        } else if(wordName.equals("!")){
            //stores a new value into a pointer
            //( v p -- )
            String pointer = forthStack.pop();
            String value = forthStack.pop();
            robot.setForthVariable(pointer, value);
            return true;
        } else if(wordName.equals("and")){
            //false if either boolean is false, true otherwise
            // ( b b -- b )
            boolean first = forthStack.pop() == "true" ? true : false;
            boolean second = forthStack.pop() == "true" ? true : false;
            if(first && second){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("or")){
            //true if either boolean is true, false otherwise
            // ( b b -- b )
            boolean first = forthStack.pop() == "true" ? true : false;
            boolean second = forthStack.pop() == "true" ? true : false;
            if(first || second){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("invert")){
            //invert the given boolean
            // ( b -- b )
            boolean first = forthStack.pop() == "true" ? true : false;
            if(first){
                forthStack.push("false");
            } else {
                forthStack.push("true");
            }
            return true;
        } else if(wordName.equals("<")){
            //i2 is less than i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 < i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("<=")){
            //i2 is not more than i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 <= i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("=")){
            //i2 is equal to i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 == i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("<>")){
            //i2 is not equal to i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 != i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("=>")){
            //i2 is at least i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 >= i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals(">")){
            //i2 is more than i1
            // ( i2 i1 -- b )
            int i1 = Integer.parseInt(forthStack.pop());
            int i2 = Integer.parseInt(forthStack.pop());
            if(i2 > i1){
                forthStack.push("true");
            } else {
                forthStack.push("false");
            }
            return true;
        } else if(wordName.equals("+")){
            //add the two integers, pushing their sum on the stack
            //( i i -- i)
            int first = Integer.parseInt(forthStack.pop());
            int second = Integer.parseInt(forthStack.pop());
            int sum = second + first;
            forthStack.push(Integer.toString(sum));
            return true;
        } else if(wordName.equals("-")){
            //subtract the top integer from the next, pushing their difference on the stack
            //( i2 i1 -- i )
            int first = Integer.parseInt(forthStack.pop());
            int second = Integer.parseInt(forthStack.pop());
            int diff = second - first;
            forthStack.push(Integer.toString(diff));
            return true;
        } else if(wordName.equals("*")){
            //multiply the two top integers, pushing their product on the stack
            //( i i -- i )
            int first = Integer.parseInt(forthStack.pop());
            int second = Integer.parseInt(forthStack.pop());
            int mult = first * second;
            forthStack.push(Integer.toString(mult));
            return true;
        } else if(wordName.equals("/mod")){
            //divide the top integer into the next, pushing the remainder and quotient
            // ( iv ie -- iq ir)
            int first = Integer.parseInt(forthStack.pop());
            int second = Integer.parseInt(forthStack.pop());
            int iq = second / first;
            int ir = second % first;
            forthStack.push(Integer.toString(ir));
            forthStack.push(Integer.toString(iq));
            return true;
        } else if(wordName.equals("drop")){
            //remove the value at the top of the stack
            //( v -- )
            forthStack.pop();
            return true;
        } else if(wordName.equals("dup")){
            //duplicate the value at the top of the stack
            //( v -- v v )
            String first = forthStack.pop();
            forthStack.push(first);
            forthStack.push(first);
            return true;
        } else if(wordName.equals("swap")){
            //swap the two values at the top of the stack
            //( v2 v1 -- v2 v1 )
            String first = forthStack.pop();
            String second = forthStack.pop();
            forthStack.push(first);
            forthStack.push(second);
            return true;
        } else if(wordName.equals("rot")){
            //rotate first three items on stack
            // ( v3 v2 v1 -- v3 v1 v2 ) 
           String first = forthStack.pop();
           String second = forthStack.pop();
           String third = forthStack.pop();
           forthStack.push(second);
           forthStack.push(first);
           forthStack.push(third);
           return true;
        }
        //command is not one of the built in commands
        return false;
    }
    */

    
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
                break;
            }
            commandQueue.add(newWord);
        }
        return commandQueue;
    }
    


}
