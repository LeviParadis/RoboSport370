package Controllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Constants.ConsoleMessageType;
import Constants.GameSpeed;
import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Interfaces.ForthWord;
import Interpreters.ForthInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Views.EndView;
import Views.MapView;

public class GameController {

    /** All of the teams to be run in the simulation. */
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size */
    private Map gameMap;

    private MapView view;

    private boolean isPaused;

    private Thread executionThread;

    /**
     * setting this enum will automatically change the animation speed and the
     * delay duration
     */
    private GameSpeed speedMultiplier;

    /** how long it takes for each animation to complete in milliseconds */
    private int animationSpeed = 400;

    /** how long it waits in between actions in milliseconds */
    private int delayDuration = 500;

    private static int TURN_LIMIT = 500;

    private boolean gameComplete;

    /**
     * initializes the teams and ??sets their position on the map??
     * 
     * @param allTeams
     *            an array that contains all of the teams playing the match
     */
    public GameController(List<Team> allTeams) throws RuntimeException {
        if (allTeams == null) {
            throw new RuntimeException("There must be teams added to begin the game");
        } else if (allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6) {
            throw new RuntimeException("You must select either 2, 3, or 6 teams");
        }

        this.gameComplete = false;
        this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
        gameMap = new Map();
        this.view = new MapView(this, allTeams);

        teams = new ArrayList<Team>();
        Iterator<Team> it = allTeams.iterator();
        while (it.hasNext()) {
            Team nextTeam = it.next();
            teams.add((int) nextTeam.getTeamNumber(), nextTeam);
        }

        initRobotPositions();
        this.executionThread = new Thread() {
            public void run() {
                // init robots
                initRobots();

                int i = 1;
                while (teamsAlive() > 1 && i < TURN_LIMIT) {
                    executeNextTurn(i);
                    i++;
                }
                displayMessage("done", ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                gameComplete = true;
            }
        };

        executionThread.start();

        UIManager manager = UIManager.sharedInstance();
        manager.pushScreen(this.view);
    }

    private void initRobots() {
        displayMessage("Init", ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> it = this.teams.iterator();
        while (it.hasNext()) {
            Team nextTeam = it.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Iterator<Robot> robotIt = nextTeam.getAllRobots().iterator();
            while (robotIt.hasNext()) {
                Robot nextRobot = robotIt.next();
                view.updateRobotInfo(nextRobot, 0);
                displayMessage(nextRobot.getName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                try {
                    ForthInterpreter.initRobot(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    e.printStackTrace();
                    displayMessage("Error: " + e.getMessage(), ConsoleMessageType.CONSOLE_ERROR);
                    displayMessage("Ending Init", ConsoleMessageType.CONSOLE_ERROR);
                }
            }
        }
    }

    /**
     * Initializes the robots on the map depending on the size of the map provided and the number of teams present.
     */
    private void initRobotPositions() {
        int numTeams = teams.size();
        int size = gameMap.getMapSize();
        Point[] teamInitPoints = new Point[numTeams];

        if (numTeams == 2) {
            teamInitPoints[0] = new Point(-(size - 1), -((size - 1) / 2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point(size - 1, (size - 1) / 2);
            teams.get(1).setTeamDirection(0);

            // teamInitPoints[0] = new Point(0, 1); //for testing at closer
            // starting positions
            // teamInitPoints[1] = new Point(1, 0);

        } else if (numTeams == 3) {
            teamInitPoints[0] = new Point(-(size - 1), -((size - 1) / 2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point((size - 1) / 2, size - 1);
            teams.get(1).setTeamDirection(1);
            teamInitPoints[2] = new Point((size - 1) / 2, -((size - 1) / 2));
            teams.get(2).setTeamDirection(-1);

        } else if (numTeams == 6) {
            teamInitPoints[0] = new Point(-(size - 1), -((size - 1) / 2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point(-((size - 1) / 2), (size - 1) / 2);
            teams.get(1).setTeamDirection(-2);
            teamInitPoints[2] = new Point((size - 1) / 2, size - 1);
            teams.get(2).setTeamDirection(-1);
            teamInitPoints[3] = new Point(size - 1, (size - 1) / 2);
            teams.get(3).setTeamDirection(0);
            teamInitPoints[4] = new Point((size - 1) / 2, -((size - 1) / 2));
            teams.get(4).setTeamDirection(1);
            teamInitPoints[5] = new Point(-((size - 1) / 2), -(size - 1));
            teams.get(5).setTeamDirection(2);
        }
        for (int l = 0; l < teamInitPoints.length; l++) {
            Team tempTeam = teams.get(l);
            Queue<Robot> robots = tempTeam.getAllRobots();
            Tile tempTile = gameMap.findTile((int) (teamInitPoints[l].getX()), (int) (teamInitPoints[l].getY()));
            for (int i = 0; i < robots.size(); i++) {
                Robot tempRobot = robots.remove();
                tempRobot.setXPosition((int) teamInitPoints[l].getX());
                tempRobot.setYPosition((int) teamInitPoints[l].getY());
                tempTile.addRobot(tempRobot);
                robots.add(tempRobot);
            }
        }
    }

    /**
     * gives the number of teams remaining with alive robots
     * 
     * @return the number of remaining teams
     */
    public int teamsAlive() {
        int livingNum = 0;
        Iterator<Team> it = this.teams.iterator();
        while (it.hasNext()) {
            Team thisTeam = it.next();
            int livingRobotCount = thisTeam.numberOfLivingRobots();
            if (livingRobotCount > 0) {
                livingNum++;
            }
        }
        return livingNum;
    }

    /**
     * Called when the user pushed the fast forward button. Finds the current
     * 	speed state, and switches to the next one
     * 
     * @return the new state, for updating ui elements
     */
    public GameSpeed switchGameSpeed() {
        switch (this.speedMultiplier) {
        case GAME_SPEED_1X:
            this.speedMultiplier = GameSpeed.GAME_SPEED_2X;
            this.delayDuration = 250;
            this.animationSpeed = 200;
            break;
        case GAME_SPEED_2X:
            this.speedMultiplier = GameSpeed.GAME_SPEED_4X;
            this.delayDuration = 125;
            this.animationSpeed = 100;
            break;
        case GAME_SPEED_4X:
            this.speedMultiplier = GameSpeed.GAME_SPEED_16X;
            this.delayDuration = 30;
            this.animationSpeed = 25;
            break;
        case GAME_SPEED_16X:
            this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
            this.delayDuration = 500;
            this.animationSpeed = 400;
            break;
        }
        return this.speedMultiplier;
    }

    /**
     * @return whether the game should be paused
     */
    public boolean isPaused() {
        return this.isPaused;
    }

    /**
     * puts the game into a paused state
     */
    public void pause() {
        this.isPaused = true;
    }

    /**
     * resumes the game from the paused state
     */
    public void resume() {
        this.isPaused = false;
    }

    /**
     * calls the endgame controller
     */
    public void endGame() {
        // TODO new EndController();
        UIManager manager = UIManager.sharedInstance();
        EndController cont = new EndController();
        EndView endView = new EndView(cont, teams);
        manager.pushScreen(endView);
    }

    /**
     * @return the animation speed
     */
    public int getAnimationSpeed() {
        return this.animationSpeed;
    }

    /**
     * Gets the robot that currently has the turn to play
     * 
     * @param teamNum
     *            the number that represents the team that the robot to be
     *            played is on
     * @param robotNum
     *            the s/n of the robot who's turn it is
     *  
     *  @return Robot a the robot that matches the numbers
     */
    public Robot getRobot(int teamNum, int robotNum) throws IndexOutOfBoundsException {
        return teams.get(teamNum).getTeamMember(robotNum);
    }

    /**
     * Executes the next turn in the thread
     * @param turnNum the current turn the controller is on
     */
    public void executeNextTurn(int turnNum) {
        displayMessage("Turn " + turnNum, ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> teamIt = this.teams.iterator();
        while (teamIt.hasNext()) {
            Team nextTeam = teamIt.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Queue<Robot> robotList = nextTeam.getLivingRobots();
            Iterator<Robot> robotIt = robotList.iterator();
            while (robotIt.hasNext()) {
                if (teamsAlive() <= 1) {
                    // we have a winner. Break
                    return;
                }

                Robot nextRobot = robotIt.next();
                view.updateRobotInfo(nextRobot, turnNum);
                displayMessage(nextRobot.getName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                try {
                    ForthInterpreter.executeTurn(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    e.printStackTrace();
                    // this is thrown when forth encounters an error that it
                    // can't handle.
                    // Display the error, and end the turn
                    displayMessage("Error: " + e.getMessage(), ConsoleMessageType.CONSOLE_ERROR);
                    displayMessage("Ending Turn", ConsoleMessageType.CONSOLE_ERROR);
                }
            }
        }
    }

    /**
     * Gives the best possible path to the desired tile 
     * @param current the tile that the robot is currently sitting on
     * @param destination the tile that the robot wishes to go to.
     * @param movesAvailable the number of moves that the robot is allowed to take
     * @return the best possible path as a list
     */
    public  ArrayList<Tile> findBestPath(Tile current, Tile destination, int movesAvailable){
        List<ArrayList<Tile>> options = new  LinkedList<ArrayList<Tile>>();
        
        List<Tile> allNeighbours = new LinkedList<Tile>();
        allNeighbours.add(gameMap.findTile(current.getXCoord(), current.getYCoord()+1));
        allNeighbours.add(gameMap.findTile(current.getXCoord(), current.getYCoord()-1));
        allNeighbours.add(gameMap.findTile(current.getXCoord() + 1, current.getYCoord()+1));
        allNeighbours.add(gameMap.findTile(current.getXCoord() + 1, current.getYCoord()));
        allNeighbours.add(gameMap.findTile(current.getXCoord() - 1, current.getYCoord()-1));
        allNeighbours.add(gameMap.findTile(current.getXCoord() - 1, current.getYCoord()));
        
        Iterator<Tile> it = allNeighbours.iterator();
        while(it.hasNext()){
        
            Tile neighbourTile = it.next();
                
            //making sure position desired is on map
            if(neighbourTile != null && gameMap.isValidTile(neighbourTile)){
                    //find the cost to reach this neighbor
                    int cost = neighbourTile.getCost();
                    //if we found the destination, return a new list with the destination in it
                    if(neighbourTile == destination && cost <= movesAvailable){
                        ArrayList<Tile> result = new ArrayList<Tile>();
                        result.add(neighbourTile);
                        return result;
                        //if the neighbor isn't the destination but it is reachable, recurse to the neighbor
                    } else if(cost <= movesAvailable){
                        ArrayList<Tile> neighbourResult = findBestPath(neighbourTile, destination, movesAvailable - cost);
                        //if the neighbor was able to find a path, add it to the list of options
                        if(neighbourResult != null){
                            neighbourResult.add(neighbourTile);
                            options.add(neighbourResult);
                        }
                    }
                }
            
        }

        // now we have a list of paths that reach the destination. Look through
        // them all to find the best option
        if (options.size() == 0) {
            // if there are no options, this path is a dead end
            return null;
        } else {
            int bestPath = Integer.MAX_VALUE;
            ArrayList<Tile> bestList = null;
            Iterator<ArrayList<Tile>> itOptions = options.iterator();
            while(itOptions.hasNext()){
                ArrayList<Tile> thisOption = itOptions.next();
                int cost = sizeOfPath(thisOption);
                if (cost < bestPath) {
                    bestPath = cost;
                    bestList = thisOption;
                }
            }
            return bestList;
        }
    }

    /**
     * Gives the cost of the path to a tile
     * @param list the list of tiles along the path
     * @return the cost of the path
     */
    private int sizeOfPath(List<Tile> list) {
        int sum = 0;
        Iterator<Tile> it = list.iterator();
        while (it.hasNext()) {
            Tile next = it.next();
            sum = next.getCost() + sum;
        }
        return sum;
    }
    
    /**
     * Moves a robot along the board
     * @param robotToMove the robot that is to be moved on the board
     * @param TeamNumber the team number of the team that the robot is on
     * @param range the range that the destination is from the robot
     * @param direction the direction that the robot wishes to go
     * @param movesLeft the amount of moves that the robot can travel
     * 
     * @return the number of moves left
     * @throws RuntimeException an error that is caught during runtime
     */
    public int moveRobot(Robot robotToMove, int TeamNumber, int range, int direction, int movesLeft) throws RuntimeException{
           
      int newX;
      int newY;
      Tile curTile = gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition());
      
      Point dir = gameMap.getDirection(direction, range);
      
      Tile dest = gameMap.findTile((int) dir.getX() + curTile.getXCoord(), (int) dir.getY() + curTile.getYCoord());
      
      if(dest == null || !gameMap.isValidTile(dest)){
          throw new RuntimeException("not a valid tile");
      }
      
      
      ArrayList<Tile> bestPath = findBestPath( curTile, dest, movesLeft);
      
      if(bestPath == null){
         throw new RuntimeException("not enough moves");
      }
      else{
          int cost = sizeOfPath(bestPath);

          
          for(int i=bestPath.size()-1; i>=0; i--){
              Tile temp = bestPath.get(i);
              newX = temp.getXCoord();
              newY = temp.getYCoord();
              
              gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition()).removeRobot(robotToMove);
              
              int xOffset = newX - robotToMove.getXPosition();
              int yOffset = newY - robotToMove.getYPosition();
              robotToMove.setXPosition(temp.getXCoord());
              robotToMove.setYPosition(temp.getYCoord());
              
              gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition()).addRobot(robotToMove);
              int currentDirection = getDirection(xOffset, yOffset);
              view.moveRobot((int)(robotToMove.getTeamNumber()), (int)(robotToMove.getMemberNumber()), currentDirection);
          }
          return cost;
      }
        
   }
    
    /**
     * Takes an x and y offset of range 1 and returns the direction (0 5)
     * 
     * @param xOffset
     *            -1, 0, 1
     * @param yOffset
     *            -1, 0, 1
     * @return the direction (0 5) the given offset points to, -1 if invalid
     *         input
     */
    public int getDirection(int xOffset, int yOffset) {
        if (xOffset == 0 && yOffset == 1) {
            return 0;
        } else if (xOffset == 1 && yOffset == 1) {
            return 1;
        } else if (xOffset == 1 && yOffset == 0) {
            return 2;
        } else if (xOffset == 0 && yOffset == -1) {
            return 3;
        } else if (xOffset == -1 && yOffset == -1) {
            return 4;
        } else if (xOffset == -1 && yOffset == 0) {
            return 5;
        } else {
            return -1;
        }
    }

    /**
     * fire at the position passed in
     * 
     * @param shooter
     *            is the robot that will fire a shot
     * @param range
     *            the distance away to shoot
     * @param direction
     *            the direction to shoot
     */
    public void shootAtSpace(Robot shooter, int range, int direction) {

        if (range > 3) {
            this.displayMessage("Error: cannot shoot farther then range 3", ConsoleMessageType.CONSOLE_ERROR);
            return;
        }

        Tile tileToShoot = this.tileNearRobot(shooter, direction, range);
  
        if (tileToShoot != null) {
            LinkedList<Robot> robots = tileToShoot.getRobots();

            view.fireShot((int) (shooter.getTeamNumber()), (int) (shooter.getMemberNumber()), direction, range);

            for (int i = 0; i < robots.size(); i++) {
                Robot temp = robots.get(i);
                temp.inflictDamage(shooter.getStrength());
                if (!temp.isAlive()) {
                    view.destroyRobot((int) (temp.getTeamNumber()), (int) (temp.getMemberNumber()));
                    temp.destroy();
                }

            }
        } else {
            this.displayMessage("Error: Can not shoot off map", ConsoleMessageType.CONSOLE_ERROR);
        }
    }

    /**
     * Finds a returns a list of the closest robots to input robot r Will return
     * up to 4 robots, in a range up to 3 spaces Called by the ForthInterpreter
     * 
     * @param r
     *            the robot asking for closest robots
     * @return List a list containing the closest robots
     */
    public List<Robot> getClosest(Robot r) {
        
        int startX = r.getXPosition();
        int startY = r.getYPosition();

        LinkedList<Robot> closest = new LinkedList<>();
        final int maxDistance = 3;
        final int listLimit = 4;
        
        for (int range = 0; range <= maxDistance; range++) {
            for(int x=-range; x<=range;x++){
                int cap = range - Math.abs(x);
                if(x<0){
                    for(int y=-range; y<=cap; y++){
                        Tile nextTile = gameMap.findTile(startX+x, startY+y);
                        this.addRobotsToToList(nextTile, closest, r, listLimit);
                        if(closest.size() >= 4){
                            return closest;
                        }
                    }
                } else if (x>0){
                    for(int y=range; y>=-cap; y--){
                        Tile nextTile = gameMap.findTile(startX+x, startY+y);
                        this.addRobotsToToList(nextTile, closest, r, listLimit);
                        if(closest.size() >= 4){
                            return closest;
                        }
                    }
                } else {
                    Tile topTile = gameMap.findTile(startX, startY+range);
                    this.addRobotsToToList(topTile, closest, r, listLimit);
                    if(closest.size() >= 4){
                        return closest;
                    }
                    Tile bottomTile = gameMap.findTile(startX, startY-range);
                    this.addRobotsToToList(bottomTile, closest, r, listLimit);
                    if(closest.size() >= 4){
                        return closest;
                    }
                }
            }
        }        
        return closest;
    }
    
    /**
     * Helper method to attempt to add robots from a tile to a list of robots
     * Will only add robots if they are unique to the list, alive, and not the caller
     * @param newTile the tile to add robots from
     * @param robotList the ongoing list of robots
     * @param exclude the robot that is searching for others
     * @param listLimit the max amount allowed in the list
     */
    private void addRobotsToToList(Tile newTile, List<Robot> robotList, Robot exclude, int listLimit){
        if(robotList.size() >= 4){
            return;
        }
        if(gameMap.isValidTile(newTile)){
            List<Robot> robots = newTile.getRobots();
            Iterator<Robot> it = robots.iterator();
            while(it.hasNext()){
                Robot next = it.next();

                if(!robotList.contains(next) && !next.equals(exclude) && next.isAlive()){
                    robotList.add(next);
                    if(robotList.size()==listLimit) { 
                        return;
                    }
                }
            }
        }
    }

    /**
     * Sends a forth word from one robot to the mailbox of another Robots must
     * be on the same team, and the forth word must be either an int, a string,
     * or a boolean Called by the forth interpreter
     * 
     * @param sender
     *            the robot sending the message
     * @param receiverNumber
     *            the member number of the robot to recieve the message (on the
     *            same team)
     * @param value
     *            the value to send
     * @return a bool indicating whether the operation was a success. Will fail
     *         if the receiverNumber is invalid, if the receiver is destroyed,
     *         or if the receiver's mailbox is full
     */
    public boolean sendMail(Robot sender, int receiverNumber, ForthWord value) {
        int teamNum = (int) sender.getTeamNumber();
        Team thisTeam = this.teams.get(teamNum);
        try {
            Robot reciever = thisTeam.getTeamMember(receiverNumber);
            // attempt to send mail. Will return true if it worked, or false if
            // it failed
            return reciever.addMailFromMember((int) sender.getMemberNumber(), value);
        } catch (IndexOutOfBoundsException e) {
            // if there is no team mate with the number requested, return false
            return false;
        }
    }

    /**
     * Will be called by the forth interpreter to show new actions for display
     * in the interface Will be called anytime the robot does anything, so the
     * user can be updated as to what is happening
     * 
     * @param newActionMessage
     *            the latest action being run by a robot
     * @param type
     *            the type of message to display
     */
    public void displayMessage(String newActionMessage, ConsoleMessageType type) {
        this.view.displayMessage(newActionMessage, type);
        // sleep in between messages, so that we get an even printing speed
        // do not progress to the next action if we are paused
        try {
            do {
                Thread.sleep(this.delayDuration);
            } while (this.isPaused);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tells us the number of robots at a given square from a reference robot
     * Called but the Forth Interpreter
     * 
     * @param referencePosition
     *            the robot asking for the population
     * @param direction
     *            the direction to look in
     * @param range
     *            the range to look in
     * @return the number of robots on the space
     */
    public int populationAtPosition(Robot referencePosition, int direction, int range) {
        Tile requestedTile = tileNearRobot(referencePosition, direction, range);
        if(requestedTile != null && gameMap.isValidTile(requestedTile)){
            return requestedTile.getRobots().size();
        } else {
            return 0;
        }
    }
    
    /**
     * Finds a tile some direction and range away from the input robot
     * @param r the robot we are looking in reference from
     * @param direction the direction we are looking away from the robot
     * @param range the range away from the robot we are looking
     * @return the tile at the specified direction and range
     */
    private Tile tileNearRobot(Robot r, int direction, int range){
        int newX = r.getXPosition();
        int newY = r.getYPosition() + range;
        if(direction==0){
            return gameMap.findTile(newX, newY);
        } else {
            //move along edge 1
            for(int i=0; i<range; i++){
                newX++;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
          //move along edge 2
            for(int i=0; i<range; i++){
                newY--;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
            //move along edge 3
            for(int i=0; i<range; i++){
                newX--;
                newY--;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
          //move along edge 4
            for(int i=0; i<range; i++){
                newX--;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
          //move along edge 5
            for(int i=0; i<range; i++){
                newY++;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
          //move along edge 6
            for(int i=0; i<range-1; i++){
                newX++;
                newY++;
                direction--;
                if(direction==0){
                    return gameMap.findTile(newX, newY);
                }
            }
        }
        return null;
    }


    /**
     * Tells us the direction between two robots
     * 
     * @param from
     *            the robot to start from
     * @param to
     *            the robot we are finding the direction to
     *            
     * @return int an integer saying the current direction at the position
     */
    public int directionBetweenRobots(Robot from, Robot to) {        
        int diffX = to.getXPosition() - from.getXPosition();
        int diffY = to.getYPosition() - from.getYPosition();
        
        int range = rangeBetweenRobots(from, to);
        int maxDirection = (range * 6);
        
        //direction found by analyzing the hex map
        if(diffX >= 0){
            if(diffX == range || diffY < 0){
                return (range * 2) - diffY;
            } else {
                 return diffX;
            }
        } else{
            if(diffX == -range){
                return maxDirection - range + diffY;
            } else if(diffY >= 0){
                return maxDirection + diffX;
            } else {
                return (maxDirection/2) - diffX;
            }
        }
    }

    /**
     * Tells us the range between two robots
     * 
     * @param from
     *            the robot to start from
     * @param to
     *            the robot we are finding the range to
     * @return the range between two robots
     */
    public int rangeBetweenRobots(Robot from, Robot to) {
        int diffX = to.getXPosition() - from.getXPosition();
        int diffY = to.getYPosition() - from.getYPosition();
        
        if(diffX == 0){
            return Math.abs(diffY);
        } else if(diffX < 0){
            if(diffY > 0){
                return Math.abs(diffX) + diffY; 
            } else if(diffY < diffX){
                return Math.abs(diffX) + Math.abs(diffX - diffY);
            } else {
                return Math.abs(diffX);
            }
        } else{
            if(diffY < 0){
                return diffX + Math.abs(diffY);
            } else if(diffY > diffX){
                return Math.abs(diffX) + Math.abs(diffX - diffY);
            } else {
                return Math.abs(diffX);
            }
        }
    }

    public void checkGameComplete() {
        if (this.gameComplete) {
            this.endGame();
        }
    }

}
