package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
      
	        JSONObject root = (JSONObject) json.get("script");
    
	        //get the identification information
	        String name = (String) root.get("name");
	        String team = (String) root.get("team");
	        long serial = (long) root.get("serial");
    
	        //get the robot's attributes
	        long health = (long) root.get("health");
	        long strength = (long) root.get("firepower");
	        long moves = (long) root.get("movement");
    
	        //get the robot's win record
	        long wins = (long) root.get("wins");
	        long losses = (long) root.get("losses");
	        long matches = (long) root.get("matches");
    
	        //find the forth code from the json
	        JSONArray forth = (JSONArray) root.get("code");
	        HashMap<String, String> variableList = new HashMap<String, String>();
	        HashMap<String, String> wordList = new HashMap<String, String>();
	        for(int i=0; i < forth.size(); i++){
	            JSONObject thisObject = (JSONObject) forth.get(i);
        
	            //check to see if this forth element is a variable
	            if(thisObject.get("variable") != null){
	                //store the variable with an empty assignment
	                String varName = (String)thisObject.get("variable");
	                variableList.put(varName, "");
            
	                //otherwise, it should be a word
	            }else if (thisObject.get("word") != null){
	                JSONObject word = (JSONObject) thisObject.get("word");
	                String wordName = (String) word.get("name");
	                String wordBody = (String) word.get("body");
	                wordList.put(wordName, wordBody);  
	            }
	        }
    
	        //create a robot from the information in the json
	        Robot newRobot = new Robot( name, team, serial, health, strength, moves, 
	                                    variableList, wordList, 
	                                    wins, losses, matches);
	        return newRobot;
	    }

}
