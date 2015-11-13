package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Models.Robot;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JsonInterpreter {
    
    //we save the json tags here as constants
    //this way, of the standard changes, we only need to change the tags in one place
    private static final String BASE_TAG = "script";
    private static final String NAME = "name";
    private static final String TEAM = "team";
    private static final String SERIAL = "serial";
    private static final String HEALTH = "health";
    private static final String STRENGTH = "firepower";
    private static final String MOVES_PER_TURN = "movement";
    private static final String WINS = "wins";
    private static final String LOSSES = "losses";
    private static final String MATCHES = "matches";
    private static final String FORTH_CODE = "code";
    private static final String FORTH_VAR = "variable";
    private static final String FORTH_WORD = "word";
    private static final String FORTH_WORD_NAME = "name";
    private static final String FORTH_WORD_BODY = "body";


    //test method
	    public static void main(String[] args) {
	        JSONParser parser=new JSONParser(); 
	        try {
	            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
	            Robot newRobot = robotFromJSON(json);
	            System.out.println(newRobot);
	            JSONObject reencoded = JSONfromRobot(newRobot);
	            System.out.println(reencoded);
	            System.out.println(json);
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
      
	        JSONObject root = (JSONObject) json.get(BASE_TAG);
    
	        //get the identification information
	        String name = (String) root.get(NAME);
	        String team = (String) root.get(TEAM);
	        long serial = (long) root.get(SERIAL);
    
	        //get the robot's attributes
	        long health = (long) root.get(HEALTH);
	        long strength = (long) root.get(STRENGTH);
	        long moves = (long) root.get(MOVES_PER_TURN);
    
	        //get the robot's win record
	        long wins = (long) root.get(WINS);
	        long losses = (long) root.get(LOSSES);
	        long matches = (long) root.get(MATCHES);
    
	        //find the forth code from the json
	        JSONArray forth = (JSONArray) root.get(FORTH_CODE);
	        HashMap<String, String> variableList = new HashMap<String, String>();
	        HashMap<String, String> wordList = new HashMap<String, String>();
	        for(int i=0; i < forth.size(); i++){
	            JSONObject thisObject = (JSONObject) forth.get(i);
        
	            //check to see if this forth element is a variable
	            if(thisObject.get(FORTH_VAR) != null){
	                //store the variable with an empty assignment
	                String varName = (String)thisObject.get(FORTH_VAR);
	                variableList.put(varName, "");
            
	                //otherwise, it should be a word
	            }else if (thisObject.get(FORTH_WORD) != null){
	                JSONObject word = (JSONObject) thisObject.get(FORTH_WORD);
	                String wordName = (String) word.get(FORTH_WORD_NAME);
	                String wordBody = (String) word.get(FORTH_WORD_BODY);
	                wordList.put(wordName, wordBody);  
	            }
	        }
    
	        //create a robot from the information in the json
	        Robot newRobot = new Robot( name, team, serial, health, strength, moves, 
	                                    variableList, wordList, 
	                                    wins, losses, matches);
	        return newRobot;
	    }
	    
	    //the json library we use creates warnings when we try to write to JSON files, so we will
	    //have to suppress warnings in this function where it writes to JSON
	    @SuppressWarnings("unchecked")
	    /**
	     * creates a new JSON file from a robot object, so the robot's current stats can be saved and shared
	     * @param robot   the robot we want to encode in JSON
	     * @return        a JSONObject representing the robot, using the standard robot JSON tags
	     */
    public static JSONObject JSONfromRobot(Robot robot){
        //get the identification information
        String name = robot.getName();
        String team = robot.getTeamName();
        long serial = robot.getSerialNumber();
    
        //get the robot's attributes
        long health = robot.getBaseHealth();
        long strength = robot.getStrength();
        long moves = robot.getMovesPerTurn();
    
        //get the robot's win record
        long wins = robot.getWins();
        long losses = robot.getLosses();
        long matches = robot.getTotalNumberOfMatches();
        
        //save the basic data into a new json object
        JSONObject base = new JSONObject();
        
        base.put(NAME, name);
        base.put(TEAM, team);
        base.put(SERIAL, serial);
        
        base.put(HEALTH, health);
        base.put(STRENGTH, strength);
        base.put(MOVES_PER_TURN, moves);
        
        base.put(WINS, wins);
        base.put(LOSSES, losses);
        base.put(MATCHES, matches);
        
        JSONArray forthCode = new JSONArray();
        //save forth variables into json format
        Set<String> vars = robot.getAllForthVariables();
        Iterator<String> varIterator = vars.iterator();
        while(varIterator.hasNext()){
            String thisVar = varIterator.next();
            JSONObject varJSON = new JSONObject();
            varJSON.put(FORTH_VAR, thisVar);
            forthCode.add(varJSON);
        }
        //save forth words into json format
        Set<String> words = robot.getAllForthWords();
        Iterator<String> wordIterator = words.iterator();
        while(wordIterator.hasNext()){
            String wordName = wordIterator.next();
            String wordLogic = robot.getForthWord(wordName);
            
            JSONObject wordJSON = new JSONObject();
            wordJSON.put(FORTH_WORD_NAME, wordName);
            wordJSON.put(FORTH_WORD_BODY, wordLogic);
            //TODO: We aren't saving the forth comments, so it can't be encoded back into JSON
            JSONObject wordRoot = new JSONObject();
            wordRoot.put(FORTH_WORD, wordJSON);
            forthCode.add(wordRoot);
        }
        base.put(FORTH_CODE, forthCode);
        
        
        JSONObject root = new JSONObject();
        root.put(BASE_TAG, base);
        return root;
        
	    }

}
