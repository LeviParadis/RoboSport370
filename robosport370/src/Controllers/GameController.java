package Controllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import Enums.ConsoleMessageType;
import Enums.GameSpeed;
import Exceptions.ForthParseException;
import Exceptions.ForthRunTimeException;
import Interfaces.ForthWord;
import Interpreters.ForthInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Views.endView;
import Views.mapView;

public class GameController {
    
    /** All of the teams to be run in the simulation.*/
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    
    private mapView view;
    
    private boolean isPaused;
    
    
    private Thread executionThread;
    

    /**
     * setting this enum will automatically change the animation speed and the delay duration
     */
    private GameSpeed speedMultiplier;
    
    /** how long it takes for each animation to complete in milliseconds */
    private int animationSpeed = 100;
    
    /** how long it waits in between actions in milliseconds */
    private int delayDuration = 500;
    
    private static int TURN_LIMIT = 500;
    
    private boolean gameComplete;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
    public GameController(List<Team> allTeams) throws RuntimeException{
        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
        } else if(allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6){
            throw new RuntimeException("You must select either 2, 3, or 6 teams");
        }
        
        this.gameComplete = false;
        this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
        gameMap = new Map();
        this.view = new mapView(this, allTeams);
   
        teams = new ArrayList<Team>();
        Iterator<Team> it = allTeams.iterator();
        while(it.hasNext()){
            Team nextTeam = it.next();
            teams.add((int) nextTeam.getTeamNumber(), nextTeam);
        }

        initRobotPositions();
        this.executionThread = new Thread(){
            public void run(){
                //init robots
                initRobots();
                
                int i = 1;
                while(teamsAlive() > 1 && i < TURN_LIMIT){
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
    
    private void initRobots(){
        displayMessage("Init", ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> it = this.teams.iterator();
        while(it.hasNext()){
            Team nextTeam = it.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Iterator<Robot> robotIt = nextTeam.getAllRobots().iterator();
            while(robotIt.hasNext()){
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
     * Adds the robot teams to their starting positions on the map
     */
    private void initRobotPositions(){
        int numTeams = teams.size();
        int size = gameMap.getMapSize();
        Point[] teamInitPoints = new Point[numTeams];
       
        if(numTeams == 2){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point(size-1, (size-1)/2);
            teams.get(1).setTeamDirection(0);
            
//            teamInitPoints[0] = new Point(0, 1); //for testing at closer starting positions
//            teamInitPoints[1] = new Point(1, 0);
            
            
        }
        else if(numTeams == 3){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point((size-1)/2, size-1);
            teams.get(1).setTeamDirection(1);
            teamInitPoints[2] = new Point((size-1)/2, -((size-1)/2));
            teams.get(2).setTeamDirection(-1);
            
        }
        else if(numTeams == 6){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teams.get(0).setTeamDirection(-3);
            teamInitPoints[1] = new Point(-((size-1)/2), (size-1)/2);
            teams.get(1).setTeamDirection(-2);
            teamInitPoints[2] = new Point((size-1)/2, size-1);
            teams.get(2).setTeamDirection(-1);
            teamInitPoints[3] = new Point(size-1, (size-1)/2);
            teams.get(3).setTeamDirection(0);
            teamInitPoints[4] = new Point((size-1)/2, -((size-1)/2));
            teams.get(4).setTeamDirection(1);
            teamInitPoints[5] = new Point(-((size-1)/2), -(size-1));
            teams.get(5).setTeamDirection(2);
        }
        for(int l = 0; l < teamInitPoints.length; l++){
            Team tempTeam = teams.get(l);
            Queue<Robot> robots = tempTeam.getAllRobots();
            Tile tempTile = gameMap.findTile((int)(teamInitPoints[l].getX()),(int)(teamInitPoints[l].getY()));
            for(int i = 0; i < robots.size(); i++){
                Robot tempRobot = robots.remove();
                tempRobot.setXPosition((int) teamInitPoints[l].getX());
                tempRobot.setYPosition((int) teamInitPoints[l].getY());
                displayMessage(teamInitPoints[l].getX() + "," + teamInitPoints[l].getY(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                tempTile.addRobot(tempRobot);
                robots.add(tempRobot);
            }
        }
    }
    
    public int teamsAlive(){
        int livingNum = 0;
        Iterator<Team> it = this.teams.iterator();
        while(it.hasNext()){
            Team thisTeam = it.next();
            int livingRobotCount = thisTeam.numberOfLivingRobots();
            if(livingRobotCount > 0){
                livingNum++;
            }
        }
        return livingNum;
    }
   
    /**
     * Called when the user pushed the fast forward button. 
     * Finds the current speed state, and switches to the next one
     * @return the new state, for updating ui elements
     */
    public GameSpeed switchGameSpeed(){
        switch(this.speedMultiplier){
            case GAME_SPEED_1X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_2X;
                this.delayDuration = 250;
                this.animationSpeed = 50;
                 break;
            case GAME_SPEED_2X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_4X;
                this.delayDuration = 125;
                this.animationSpeed = 25;
                break;
            case GAME_SPEED_4X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_16X;
                this.delayDuration = 30;
                this.animationSpeed = 7;
                break;
            case GAME_SPEED_16X:
                this.speedMultiplier = GameSpeed.GAME_SPEED_1X;
                this.delayDuration = 500;
                this.animationSpeed = 100;
                break;
        }
        return this.speedMultiplier;
    }
    
    
    /**
     * @return whether the game should be paused
     */
    public boolean isPaused(){
        return this.isPaused;
    }
    
    /**
     * puts the game into a paused state
     */
    public void pause(){
        this.isPaused = true;
    }
    
    /**
     * resumes the game from the paused state
     */
    public void resume(){
        this.isPaused = false;
    }
    
    /**
     * calls the endgame controller
     */
    public void endGame(){
        //TODO new EndController();
        UIManager manager = UIManager.sharedInstance();
        EndController cont = new EndController();
        endView endView = new endView(cont, teams);
        manager.pushScreen(endView);
    }
    

    /**
     * @return the animation speed
     */
    public int getAnimationSpeed(){
        return this.animationSpeed;
    }
    
    
    /**
     * Gets the robot that currently has the turn to play
     * @param teamNum the number that represents the team that the robot
     * to be played is on
     * @param robotNum the s/n of the robot who's turn it is
     */
    public Robot getRobot(int teamNum, int robotNum) throws IndexOutOfBoundsException{
        return teams.get(teamNum).getTeamMember(robotNum);
    }
    
    /**
     * executes a round of turns
     */
    public void executeNextTurn(int turnNum){
        displayMessage("Turn " + turnNum, ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
        Iterator<Team> teamIt = this.teams.iterator();
        while(teamIt.hasNext()){
            Team nextTeam = teamIt.next();
            displayMessage(nextTeam.getTeamName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
            Queue<Robot> robotList = nextTeam.getLivingRobots();
            Iterator<Robot> robotIt = robotList.iterator();
            while(robotIt.hasNext()){
                Robot nextRobot = robotIt.next();
                view.updateRobotInfo(nextRobot, turnNum);
                displayMessage(nextRobot.getName(), ConsoleMessageType.CONSOLE_SIMULATOR_MESSAGE);
                try {
                    ForthInterpreter.executeTurn(nextRobot, this);
                } catch (ForthRunTimeException | ForthParseException e) {
                    e.printStackTrace();
                    //this is thrown when forth encounters an error that it can't handle. 
                    //Display the error, and end the turn
                    displayMessage("Error: " + e.getMessage(), ConsoleMessageType.CONSOLE_ERROR);
                    displayMessage("Ending Turn", ConsoleMessageType.CONSOLE_ERROR);
                }
            }
        }
    }


    public  List<Tile> findBestPath(Tile current, Tile destination, int movesAvailable){
        int currentX = current.getXCoord();
        int currentY = current.getYCoord();
        
        
        List<List<Tile>> options = new  LinkedList<List<Tile>>();
        
        //iterate through all neighbor tiles
        for (int newX = currentX-1; newX<currentX+1; newX++ ){
            for(int newY = currentY-1; newY<currentY+1; newY++ ){
                
                //making sure position desired is on map
                if(newX > gameMap.getMinX() && newX < gameMap.getMaxX() && 
                        newY > gameMap.getMinY() && newY < gameMap.getMaxY()){
                    
                    
                    if(newX != currentX && newY != currentY){
                        Tile neighbourTile = gameMap.findTile(newX, newY);
                        
                        
                        //find the cost to reach this neighbor
                        int cost = neighbourTile.getCost();
                        //if we found the destination, return a new list with the destination in it
                        if(neighbourTile == destination){
                            LinkedList<Tile> result = new LinkedList<Tile>();
                            result.add(neighbourTile);
                            return result;
                            //if the neighbor isn't the destination but it is reachable, recurse to the neighbor
                        } else if(cost <= movesAvailable){
                            List<Tile> neighbourResult = findBestPath(neighbourTile, destination, movesAvailable - cost);
                            //if the neighbor was able to find a path, add it to the list of options
                            if(neighbourResult != null){
                                options.add(neighbourResult);
                            }
                        }
                    }
                }
            }
        }
        
        //now we have a list of paths that reach the destination. Look through them all to find the best option
        if(options.size()==0){
            //if there are no options, this path is a dead end
            return null;
        } else {
            int bestPath = Integer.MAX_VALUE;
            List<Tile> bestList = null;
            Iterator<List<Tile>> it = options.iterator();
            while(it.hasNext()){
                List<Tile> thisOption = it.next();
                int cost = sizeOfPath(thisOption);
                if(cost < bestPath){
                    bestPath = cost;
                    bestList = thisOption;
                }
            }
            bestList.add(current);
            return bestList;
        }
    }

    private int sizeOfPath(List<Tile> list){
        int sum = 0;
        Iterator<Tile> it = list.iterator();
        while(it.hasNext()){
            Tile next = it.next();
            sum = next.getCost() + sum;
        }
        return sum;
    }
    
    public int moveRobot(Robot robotToMove, int TeamNumber, int range, int direction, int movesLeft) throws RuntimeException{
           
      int newX;
      int newY;
      int movesRemain = movesLeft;
      Tile curTile = gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition());
      
      Point dir = gameMap.getDirection(direction, range);
      
      Tile dest = gameMap.findTile((int) dir.getX(), (int) dir.getY());
      
      
      List<Tile> bestPath = findBestPath( curTile, dest, movesRemain);
      
      if(bestPath == null){
          displayMessage("No path to desired tile with remaining number of moves!",ConsoleMessageType.CONSOLE_ERROR);
      }
      else{
          
          Iterator<Tile> iter = bestPath.iterator();
          
          while(iter.hasNext()){
              Tile temp = iter.next();
              newX = temp.getXCoord();
              newY = temp.getYCoord();
              
              gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition()).removeRobot(robotToMove);
              
              robotToMove.setXPosition(temp.getXCoord());
              robotToMove.setYPosition(temp.getYCoord());
              
              gameMap.findTile(robotToMove.getXPosition(), robotToMove.getYPosition()).addRobot(robotToMove);
              int xOffset = newX - robotToMove.getXPosition();
              int yOffset = newY - robotToMove.getYPosition();
              int currentDirection = getDirection(xOffset, yOffset);
              displayMessage("new position", ConsoleMessageType.CONSOLE_ROBOT_MESSAGE);
              view.moveRobot((int)(robotToMove.getTeamNumber()), (int)(robotToMove.getMemberNumber()), currentDirection);
          }
      }
    return movesRemain;
        
   }
    
    /**
     * Takes an x and y offset of range 1 and returns the direction (0->5)
     * @param xOffset -1, 0, 1
     * @param yOffset -1, 0, 1
     * @return the direction (0->5) the given offset points to, -1 if invalid input
     */
    public int getDirection(int xOffset, int yOffset) {
    	if(xOffset == 0 && yOffset == 1) {
    		return 0;
    	}
    	else if(xOffset == 1 && yOffset == 1) {
    		return 1;
    	}
		else if(xOffset == 1 && yOffset == 0) {
			return 2;	
		}
		else if(xOffset == 0 && yOffset == -1) {
			return 3;
		}
		else if(xOffset == -1 && yOffset == -1) {
			return 4;
		}
		else if(xOffset == -1 && yOffset == 0) {
			return 5;
		}
		else {
			return -1;
		}
    }
        
    
    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param range the distance away to shoot
     * @param direction the direction to shoot
     */
    public void shootAtSpace(Robot shooter, int range, int direction){
        
        if(range > 3){
            this.displayMessage("cannot shoot farther then range 3", ConsoleMessageType.CONSOLE_ERROR);
            return;
        }
        
        Point dir = gameMap.getDirection(direction, range);
        
        int xPos = (int) (dir.getX()*range);
        int yPos = (int) (dir.getY()*range);
        
        LinkedList<Robot> robots = gameMap.findTile(xPos, yPos).getRobots();
        
        Iterator<Robot> iter = robots.iterator();
        
        while(iter.hasNext()){
            Robot temp = iter.next();
            temp.inflictDamage(shooter.getStrength());
            view.fireShot((int)(shooter.getTeamNumber()), (int) (shooter.getMemberNumber()), direction, range);
            if(temp.getHealth() <= 0){
                temp.destroy();
                view.destroyRobot((int) (temp.getTeamNumber()), (int) (temp.getMemberNumber()));
                robots.remove(temp);

            }
            
        }
    }
    
    /**
     * Finds a returns a list of the closest robots to input robot r
     * Will return up to 4 robots, in a range up to 3 spaces
     * Called by the ForthInterpreter
     * @param r the robot asking for closest robots
     * @return
     */
    public List<Robot> getClosest(Robot r) {
        
        LinkedList<Robot> closest = new LinkedList<>();
        boolean foundFour = false;
        //TODO find how much range increases for second for loop
        for(int range = 1; range < gameMap.getMapSize(); range++){
            for(int direction = 0; direction < (range*6)-1; direction++){
                Point dirToGO = gameMap.getDirection(range, direction);
                
                Tile tempTile = gameMap.findTile(r.getXPosition() + (int)dirToGO.getX(), 
                                                 r.getYPosition() + (int)dirToGO.getY());
                //check to see if tile is on map esle skips it
                if(tempTile.getXCoord() > gameMap.getMinX() && tempTile.getXCoord() < gameMap.getMaxX() &&
                        tempTile.getYCoord() > gameMap.getMinY() && tempTile.getYCoord() < gameMap.getMaxY()){
                    Iterator<Robot> iter = tempTile.getRobots().iterator();
                    while(iter.hasNext() && !foundFour){
                        closest.add(iter.next());
                        if(closest.size() == 4) foundFour = true;
                    }
                }
                // else tile is not on map    
                if(foundFour) return closest;
            }  
        }
        
        return closest;
    }
    
    /**
     * Sends a forth word from one robot to the mailbox of another
     * Robots must be on the same team, and the forth word must be either an int, a string, or a boolean
     * Called by the forth interpreter
     * @param sender  the robot sending the message
     * @param receiverNumber the member number of the robot to recieve the message (on the same team)
     * @param value the value to send
     * @return a bool indicating whether the operation was a success. 
     *         Will fail if the receiverNumber is invalid, if the receiver is destroyed, or if the receiver's mailbox is full
     */
    public boolean sendMail(Robot sender, int receiverNumber, ForthWord value){
        int teamNum = (int) sender.getTeamNumber();
        Team thisTeam = this.teams.get(teamNum);
        try{
            Robot reciever = thisTeam.getTeamMember(receiverNumber);
            //attempt to send mail. Will return true if it worked, or false if it failed
            return reciever.addMailFromMember((int)sender.getMemberNumber(), value);
        } catch (IndexOutOfBoundsException e){
            //if there is no team mate with the number requested, return false
            return false;
        }
    }

    
    /**
     * Will be called by the forth interpreter to show new actions for display in the interface
     * Will be called anytime the robot does anything, so the user can be updated as to what is happening
     * @param newActionMessage the latest action being run by a robot
     * @param type the type of message to display
     */
    public void displayMessage(String newActionMessage, ConsoleMessageType type){
        this.view.displayMessage(newActionMessage, type);
        //sleep in between messages, so that we get an even printing speed
        //do not progress to the next action if we are paused
        try {
            do{
                Thread.sleep(this.delayDuration);
            } while (this.isPaused);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Tells us the number of robots at a given square from a reference robot
     * Called but the Forth Interpreter
     * @param referencePosition the robot asking for the population
     * @param direction the direction to look in
     * @param range the range to look in
     * @return the number of robots on the space
     */
    public int populationAtPosition(Robot referencePosition, int direction, int range){
        return 0;
    }
    
    
    /**
     * Tells us the direction between two robots
     * @param from the robot to start from
     * @param to the robot we are finding the direction to
     */
    public int directionBetweenRobots(Robot from, Robot to){
        return 0;
    }
    
    /**
     * Tells us the range between two robots
     * @param from the robot to start from
     * @param to the robot we are finding the range to
     */
    public int rangeBetweenRobots(Robot from, Robot to){
        return 0;
    }

    public void checkGameComplete(){
        if(this.gameComplete){
            this.endGame();
        }
    }
    

}


