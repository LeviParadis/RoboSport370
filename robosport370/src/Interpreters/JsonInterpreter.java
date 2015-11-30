package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Enums.JSONConstants;
import Models.Robot;
import Models.RobotGameStats;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class JsonInterpreter {

    public static final String ROBOT_LIBRARIAN_URL = "http://localhost:8080/CrunchifyTutorials/api/crunchifyService";

    /**
     * lists all robots that meet the passed in parameters. All parameters are
     * optional except currentVersionOnly
     * 
     * @param name
     *            the name of the robot we are searching for. Can be a regular
     *            expression
     * @param team
     *            the name of the team of the robots we are looking for
     * @param minWins
     *            the minimum number of wins of the robots we should return
     * @param maxWins
     *            the maximum number of wins of the robots we should return
     * @param minLosses
     *            the minimum number of losses of the robots we should return
     * @param maxLosses
     *            the maximum number of losses of the robots we should return
     * @param minMatches
     *            the minimum number of matches of the robots we should return
     * @param maxMatches
     *            the maximum number of matches of the robots we should return
     * @param currentVersionOnly
     *            determines whether we should show all version of robots, or
     *            just the latest
     * @return a list of robots that match the above parameters
     */
    public static Queue<Robot> listRobots(boolean currentVersionOnly, String name, String team, Integer minWins,
            Integer maxWins, Integer minLosses, Integer maxLosses, Integer minMatches, Integer maxMatches) {

        // create the json object
        JSONObject root = new JSONObject();

        if (name != null) {
            root.put(JSONConstants.NAME, name);
        }

        if (team != null) {
            root.put(JSONConstants.TEAM, team);
        }

        if (minWins != null || maxWins != null) {
            JSONArray wins = createComparisonJSON(minWins, maxWins);
            root.put(JSONConstants.WINS, wins);
        }

        if (minLosses != null || maxLosses != null) {
            JSONArray losses = createComparisonJSON(minLosses, maxLosses);
            root.put(JSONConstants.LOSSES, losses);
        }

        if (minMatches != null || maxMatches != null) {
            JSONArray matches = createComparisonJSON(minMatches, maxMatches);
            root.put(JSONConstants.MATCHES, matches);
        }

        if (currentVersionOnly) {
            root.put(JSONConstants.VERSION, JSONConstants.VERSION_OPTION_LATEST);
        } else {
            root.put(JSONConstants.VERSION, JSONConstants.VERSION_OPTION_ALL);
        }

        root.put(JSONConstants.LIST_TYPE_TAG, JSONConstants.LIST_TYPE_FULL);

        // TODO: implement
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot newRobot = robotFromJSON(json);

            JSONObject json2 = (JSONObject) parser.parse(new FileReader("resources/RobotExample2.JSON"));
            Robot newRobot2 = robotFromJSON(json2);

            JSONObject json3 = (JSONObject) parser.parse(new FileReader("resources/RobotExample3.JSON"));
            Robot newRobot3 = robotFromJSON(json3);

            JSONObject json4 = (JSONObject) parser.parse(new FileReader("resources/RobotExample4.JSON"));
            Robot newRobot4 = robotFromJSON(json4);
            Queue<Robot> list = new LinkedList<Robot>();
            list.add(newRobot);
            list.add(newRobot2);
            list.add(newRobot3);
            list.add(newRobot4);
            return list;
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    private static JSONArray createComparisonJSON(Integer minValueOrNull, Integer maxValueOrNull) {
        JSONArray result = new JSONArray();
        if (minValueOrNull != null) {
            JSONObject minJSON = new JSONObject();
            minJSON.put(JSONConstants.COMPARISON_MIN, minValueOrNull);
            result.add(minJSON);
        }
        if (maxValueOrNull != null) {
            JSONObject maxJSON = new JSONObject();
            maxJSON.put(JSONConstants.COMPARISON_MAX, maxValueOrNull);
            result.add(maxJSON);
        }
        return result;
    }

    /**
     * returns a specific robot
     * 
     * @param serial
     *            the serial number of the robot to search for
     * @return the robot object
     */
    public static Robot getRobot(long serial) {
        // create the json object
        JSONObject json = new JSONObject();
        json.put(JSONConstants.SERIAL, new Integer((int) serial));
        json.put(JSONConstants.VERSION, JSONConstants.VERSION_OPTION_ALL);
        json.put(JSONConstants.LIST_TYPE_TAG, JSONConstants.LIST_TYPE_FULL);

        JSONObject root = new JSONObject();
        root.put(JSONConstants.LIST_TAG, json);

        JSONObject response = contactLibrarian(root);
        Robot newRobot = robotFromJSON(response);

        // TODO: implement
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonRobot = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
            Robot robot = robotFromJSON(jsonRobot);
            return robot;
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    private static JSONObject contactLibrarian(JSONObject inputJSON) throws RuntimeException {
        Client client = Client.create();
        WebResource webResource = client.resource(ROBOT_LIBRARIAN_URL);

        ClientResponse clientResponse = webResource.accept("application/json").get(ClientResponse.class);
        if (clientResponse.getStatus() != 200) {
            throw new RuntimeException("Failed" + clientResponse.toString());
        }

        try {
            JSONObject resObj = (JSONObject) new JSONParser().parse(clientResponse.getEntity(String.class));
            return resObj;
        } catch (ParseException e) {
            throw new RuntimeException("Could not Parse Response From Server");
        }
    }

    /**
     * registers a robot. Returns whether the registration was a success
     * 
     * @param name
     *            the new robot's name
     * @param team
     *            the new robot's team
     * @param firepower
     *            the new robot's firepower
     * @param health
     *            the new robot's health
     * @param movement
     *            the max number of moves the new robot can make in a turn
     * @param forthCode
     *            a text string representing the robot's forth code
     * @return a bool indicating whether the registration was a success
     */
    public static boolean registerRobot(String name, String team, long firepower, long health, long movement,
            String forthCode) throws RuntimeException {

        JSONObject json = new JSONObject();
        json.put(JSONConstants.NAME, name);
        json.put(JSONConstants.TEAM, team);
        json.put(JSONConstants.STRENGTH, firepower);
        json.put(JSONConstants.HEALTH, health);
        json.put(JSONConstants.MOVES_PER_TURN, name);
        json.put(JSONConstants.FORTH_CODE, forthCode);

        JSONObject root = new JSONObject();
        root.put(JSONConstants.REGISTER_TAG, json);

        contactLibrarian(root);

        return true;
    }

    /**
     * allows us to make changes to the attributes of a particular robot
     * 
     * @param serialNumber
     *            the serial number of the robot we want to change
     * @param newFirepower
     *            the new firepower value
     * @param newHealth
     *            the new health value
     * @param newMovement
     *            the new number of moves per turn
     * @param newForthCode
     *            the new forth code
     * @return boolean indicating whether the revision was a success
     */
    public static boolean reviseRobot(long serialNumber, long newFirepower, long newHealth, long newMovement,
            String newForthCode) {
        // TODO: implement
        return true;
    }

    /**
     * retires a robot
     * 
     * @param serialNumber
     *            the serial number of the robot we want to retire
     */
    public static void retireRobot(long serialNumber) throws RuntimeException {
        // TODO: implement
    }

    /**
     * updates a robot's stats on the server
     * 
     * @param serialNumber
     *            the serial number of the robot to update
     * @param currentStats
     *            the stats we want to update
     * @return boolean if stats were updated
     */
    public static boolean updateStats(long serialNumber, RobotGameStats currentStats) {
        // TODO: implement
        return true;
    }

    /**
     * Creates a robot object from a standard robot JSON file Accepts a cache of
     * robot stats files, so we can map similar robots to the same stats object
     * 
     * @param json
     *            a JSONObject representing the robot
     * @return a robot object with the information saved in the JSON file
     */
    protected static Robot robotFromJSON(JSONObject json) {

        JSONObject root = (JSONObject) json.get(JSONConstants.BASE_TAG);

        // get the identification information
        String name = (String) root.get(JSONConstants.NAME);
        long serial = (long) root.get(JSONConstants.SERIAL);

        RobotGameStats stats;

        // get the robot's stats record
        long wins = (long) root.get(JSONConstants.WINS);
        long losses = (long) root.get(JSONConstants.LOSSES);
        long executions = (long) root.get(JSONConstants.EXECUTIONS);
        long gamesDied = (long) root.get(JSONConstants.GAMES_DIED);
        long gamesSurvived = (long) root.get(JSONConstants.GAMES_LIVED);
        long damageGiven = (long) root.get(JSONConstants.DAMAGE_GIVEN);
        long damageRecieved = (long) root.get(JSONConstants.DAMAGE_TAKEN);
        long kills = (long) root.get(JSONConstants.KILLS);
        long distanceMoved = (long) root.get(JSONConstants.DISTANCE_MOVED);
        stats = new RobotGameStats(wins, losses, executions, gamesDied, gamesSurvived, damageGiven, damageRecieved,
                kills, distanceMoved);

        // get the robot's attributes
        long health = (long) root.get(JSONConstants.HEALTH);
        long strength = (long) root.get(JSONConstants.STRENGTH);
        long moves = (long) root.get(JSONConstants.MOVES_PER_TURN);

        // find the forth code from the json
        JSONArray forth = (JSONArray) root.get(JSONConstants.FORTH_CODE);
        HashMap<String, String> variableList = new HashMap<String, String>();
        HashMap<String, String> wordList = new HashMap<String, String>();
        for (int i = 0; i < forth.size(); i++) {
            JSONObject thisObject = (JSONObject) forth.get(i);

            // check to see if this forth element is a variable
            if (thisObject.get(JSONConstants.FORTH_VAR) != null) {
                // store the variable with an empty assignment
                String varName = (String) thisObject.get(JSONConstants.FORTH_VAR);
                variableList.put(varName, "0");

                // otherwise, it should be a word
            } else if (thisObject.get(JSONConstants.FORTH_WORD) != null) {
                JSONObject word = (JSONObject) thisObject.get(JSONConstants.FORTH_WORD);
                String wordName = (String) word.get(JSONConstants.FORTH_WORD_NAME);
                String wordBody = (String) word.get(JSONConstants.FORTH_WORD_BODY);
                wordList.put(wordName, wordBody);
            }
        }

        // create a robot from the information in the json
        Robot newRobot = new Robot(name, serial, health, (int) strength, moves, variableList, wordList, stats);
        return newRobot;
    }

    // the json library we use creates warnings when we try to write to JSON
    // files, so we will
    // have to suppress warnings in this function where it writes to JSON
    @SuppressWarnings("unchecked")
    /**
     * @return a formated JSON object from all the stats from a robot
     */
    private static JSONObject statsToJSON(RobotGameStats stats) {
        JSONObject root = new JSONObject();
        root.put(JSONConstants.LOSSES, stats.getLosses());
        root.put(JSONConstants.WINS, stats.getWins());
        root.put(JSONConstants.EXECUTIONS, stats.getExecutions());
        root.put(JSONConstants.GAMES_LIVED, stats.getGamesSurvived());
        root.put(JSONConstants.GAMES_DIED, stats.getGamesSurvived());
        root.put(JSONConstants.DAMAGE_TAKEN, stats.getDamageAbsorbed());
        root.put(JSONConstants.DAMAGE_GIVEN, stats.getDamageGiven());
        root.put(JSONConstants.KILLS, stats.getKills());
        root.put(JSONConstants.DISTANCE_MOVED, stats.getDistanceMoved());
        return root;
    }

    public static JSONArray forthCodeToJson(Set<String> vars, HashMap<String, String> words) {
        JSONArray forthArr = new JSONArray();

        Iterator<String> varIt = vars.iterator();
        while (varIt.hasNext()) {
            String varName = varIt.next();
            JSONObject newVar = new JSONObject();
            newVar.put(JSONConstants.FORTH_VAR, varName);
            forthArr.add(newVar);
        }

        Set<String> allWordKeys = words.keySet();
        Iterator<String> wordIt = allWordKeys.iterator();
        while (wordIt.hasNext()) {
            String wordName = wordIt.next();
            String wordValue = words.get(wordName);
            JSONObject newWord = new JSONObject();
            newWord.put(JSONConstants.FORTH_WORD_NAME, wordName);
            newWord.put(JSONConstants.FORTH_WORD_BODY, wordValue);
            JSONObject wordOuter = new JSONObject();
            wordOuter.put(JSONConstants.FORTH_WORD, newWord);
            forthArr.add(wordOuter);
        }

        return forthArr;

    }

}
