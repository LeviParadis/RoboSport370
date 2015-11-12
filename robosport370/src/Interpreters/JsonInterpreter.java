package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Models.Robot;

import java.io.*;
import java.util.HashMap;

public class JsonInterpreter {

	public static void main(String[] args) {
   JSONParser parser=new JSONParser(); 
   try {
       JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
       Robot newRobot = robotFromJSON(json);
   } catch (IOException | ParseException e1) {
       e1.printStackTrace();
   }
	}


static public Robot robotFromJSON(JSONObject json){
    Robot newRobot = new Robot();
    
    
    JSONObject root = (JSONObject) json.get("script");
    
    long wins = (long) root.get("wins");
    long kills = (long) root.get("kills");
    
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
    
 
    System.out.println(root);
    return newRobot;
}

}
