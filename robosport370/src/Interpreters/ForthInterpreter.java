package Interpreters;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Controllers.GameController;
import Models.Robot;

public class ForthInterpreter {

    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = JsonInterpreter.robotFromJSON(json);
            initRobot(newRobot, null);
            
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
        executeForthCommand(robot, "init", controller);
    }
    
    /**
     * Run's a robot's forth turn logic
     * @param robot             the robot we are setting up
     * @param GameController    the controller that controls the game
     */
    public static void executeTurn(Robot robot, GameController controller){
        executeForthCommand(robot, "turn", controller);
    }
    
    /**
     * reads and executes forth commands
     * @param robot        the robot that is calling the command
     * @param command      the name of the forth word to execute
     * @param controller   the controller that control's the game 
     */
    private static void executeForthCommand(Robot robot, String command, GameController controller){
        String logicString = robot.getForthWord(command);
        System.out.println(logicString);
    }

}
