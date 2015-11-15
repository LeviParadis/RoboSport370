package Interpreters;

import java.util.Random;
import java.util.Stack;

import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Models.ForthBoolLiteral;
import Models.ForthIntegerLiteral;
import Models.ForthPointerLiteral;
import Models.ForthStringLiteral;
import Models.ForthWord;
import Models.Robot;

public class ForthSystemCommands {

    protected static void health(Stack<ForthWord> forthStack, Robot robot) {
        ForthWord result;
        //returns the robot’s current health (1–3)
        // ( -- i )
         long health = robot.getHealth();
         result = new ForthIntegerLiteral(health);
         forthStack.push(result);
    }

    protected static void movesLeft(Stack<ForthWord> forthStack, long movesLeft) {
        ForthWord result;
        //returns the robot’s range of movement (0–3)
            result = new ForthIntegerLiteral(movesLeft);
            forthStack.push(result);
    }

    protected static void firePower(Stack<ForthWord> forthStack, Robot robot) {
        ForthWord result;
        //returns the robot’s firepower (1–3)
        //( -- i )
        long strength = robot.getStrength();
        result = new ForthIntegerLiteral(strength);
        forthStack.push(result);
    }

    protected static void teamNumber(Stack<ForthWord> forthStack, Robot robot) {
        ForthWord result;
        //returns the robot’s team number
        //member ( -- i )
        long team = robot.getTeamNumber();
        result = new ForthIntegerLiteral(team);
        forthStack.push(result);
    }

    protected static void memberNumber(Stack<ForthWord> forthStack, Robot robot) {
        ForthWord result;
        //returns the robot’s member number
        //member ( -- i )
        long member = robot.getMemberNumber();
        result = new ForthIntegerLiteral(member);
        forthStack.push(result);
    }

    protected static void console(Stack<ForthWord> forthStack) {
        ForthWord first;
        //prints the current value out, using the same syntax as they would be input with
        // ( v -- )
        first = forthStack.pop();
        String consoleString = first.consoleFormat();
        System.out.println(consoleString);
    }

    protected static void random(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
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
    }

    protected static void sendMail(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
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
    }

    protected static void checkMail(Stack<ForthWord> forthStack, Robot robot) throws ForthRunTimeException {
        ForthWord first;
        ForthWord result;
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
    }

    protected static void recieveMail(Stack<ForthWord> forthStack, Robot robot) throws ForthRunTimeException {
        ForthWord first;
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
    }

    protected static void checkVariable(Stack<ForthWord> forthStack, Robot robot)
            throws ForthParseException, ForthRunTimeException {
        ForthWord first;
        ForthWord result;
        //takes a variable and returns the value it’s storing
        // ( p -- v )
        first = forthStack.pop();
        if(first instanceof ForthPointerLiteral){
            String value = ((ForthPointerLiteral) first).getVariableValue(robot);
            result = ForthParser.wordFromString(value, robot);
            forthStack.push(result);
        } else {
            throw new ForthRunTimeException("? word called without a variable pointer on top of the stack");
        }
    }

    protected static void assignVariable(Stack<ForthWord> forthStack, Robot robot) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        //stores a new value into a pointer
        //( v p -- )
        first = forthStack.pop();
        second = forthStack.pop();
        
        if(first instanceof ForthPointerLiteral){
            ((ForthPointerLiteral) first).setVariableValue(robot, second);
        } else {
            throw new ForthRunTimeException("! word called without a variable pointer on top of the stack");
        }
    }

    protected static void and(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void or(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void invert(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord result;
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
    }

    protected static void less(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void lessOrEqual(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void equal(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void notEqual(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void greaterOrEqual(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void greater(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

    protected static void add(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }


    protected static void subtract(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }

  
    protected static void multiply(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
        ForthWord result;
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
    }


    protected static void divide(Stack<ForthWord> forthStack) throws ForthRunTimeException {
        ForthWord first;
        ForthWord second;
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
    }


    protected static void drop(Stack<ForthWord> forthStack) {
        //remove the value at the top of the stack
        //( v -- )
        forthStack.pop();
    }


    protected static void duplicate(Stack<ForthWord> forthStack) {
        ForthWord first;
        //duplicate the value at the top of the stack
        //( v -- v v )
        first = forthStack.pop();
        forthStack.push(first);
        forthStack.push(first);
    }

    protected static void rotate(Stack<ForthWord> forthStack) {
        ForthWord first;
        ForthWord second;
        ForthWord third;
        //rotate first three items on stack
        // ( v3 v2 v1 -- v3 v1 v2 ) 
            first = forthStack.pop();
            second = forthStack.pop();
            third = forthStack.pop();
            forthStack.push(second);
            forthStack.push(first);
            forthStack.push(third);
    }

    protected static void swap(Stack<ForthWord> forthStack) {
        ForthWord first;
        ForthWord second;
        //swap the two values at the top of the stack
        //( v2 v1 -- v2 v1 )
        first = forthStack.pop();
        second = forthStack.pop();
        forthStack.push(first);
        forthStack.push(second);
    }
    
}
