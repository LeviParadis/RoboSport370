package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Enums.JSONConstants;
import Models.Robot;

import java.io.*;
import java.util.HashMap;

public class JsonInterpreter {
    
    //test method
    public static void main(String[] args) {
        JSONParser parser=new JSONParser(); 
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = robotFromJSON(json);
            System.out.println(json);
            System.out.println(newRobot);
        } catch (IOException | ParseException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Creates a robot object from a standard robot JSON file
     * @param json a JSONObject representing the robot
     * @return a robot object with the information saved in the JSON file
     */
    public static Robot robotFromJSON(JSONObject json){
      
        JSONObject root = (JSONObject) json.get(JSONConstants.BASE_TAG);
    
        //get the identification information
        String name = (String) root.get(JSONConstants.NAME);
        long serial = (long) root.get(JSONConstants.SERIAL);
    
        //get the robot's attributes
        long health = (long) root.get(JSONConstants.HEALTH);
        long strength = (long) root.get(JSONConstants.STRENGTH);
        long moves = (long) root.get(JSONConstants.MOVES_PER_TURN);
    
        //get the robot's win record
        long wins = (long) root.get(JSONConstants.WINS);
        long losses = (long) root.get(JSONConstants.LOSSES);
        long matches = (long) root.get(JSONConstants.MATCHES);
    
        //find the forth code from the json
        JSONArray forth = (JSONArray) root.get(JSONConstants.FORTH_CODE);
        HashMap<String, String> variableList = new HashMap<String, String>();
        HashMap<String, String> wordList = new HashMap<String, String>();
        for(int i=0; i < forth.size(); i++){
            JSONObject thisObject = (JSONObject) forth.get(i);
        
            //check to see if this forth element is a variable
            if(thisObject.get(JSONConstants.FORTH_VAR) != null){
                //store the variable with an empty assignment
                String varName = (String)thisObject.get(JSONConstants.FORTH_VAR);
                variableList.put(varName, "0");
            
                //otherwise, it should be a word
            }else if (thisObject.get(JSONConstants.FORTH_WORD) != null){
                JSONObject word = (JSONObject) thisObject.get(JSONConstants.FORTH_WORD);
                String wordName = (String) word.get(JSONConstants.FORTH_WORD_NAME);
                String wordBody = (String) word.get(JSONConstants.FORTH_WORD_BODY);
                wordList.put(wordName, wordBody);  
            }
        }
    
        //create a robot from the information in the json
        Robot newRobot = new Robot( name, serial, health, strength, moves, 
                                    variableList, wordList);
        return newRobot;
    }
    

    

}
