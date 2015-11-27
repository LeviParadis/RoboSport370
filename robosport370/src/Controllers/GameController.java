package Controllers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import Interpreters.JsonInterpreter;
import Models.Robot;
import Models.Team;
import Models.Tile;
import Models.Map;
import Models.Map.DIRECTION;
import Views.mapView;

public class GameController{
    
    /** All of the teams to be run in the simulation.*/
    private ArrayList<Team> teams;
    /** The map that holds the information for calculations and the size*/
    private Map gameMap;
    /** holds the value to which team goes next*/
    private Queue<Team> nextTeamIdx;
    
    
    
    
    /** Stores the type of map view */
    mapView view;
    
    /** how long it takes for each animation to complete in milliseconds */
    int animationSpeed = 100;
    
    /**
     * initializes the teams and ??sets their position on the map??
     * @param allTeams an array that contains all of the teams playing the match
     * @param hexSize the size of the map on one side
     */
   
    public GameController(List<Team> allTeams) throws RuntimeException{
        if(allTeams == null){
            throw new RuntimeException("There must be teams added to begin the game");
     
        } else if(allTeams.size() != 2 && allTeams.size() != 3 && allTeams.size() != 6){
            throw new RuntimeException("Not a valid number of teams");
        } else {
            Iterator<Team> it = allTeams.iterator();
            while(it.hasNext()){
                Team nextTeam = it.next();
                teams.add((int) nextTeam.getTeamNumber(), nextTeam);
                this.nextTeamIdx.add(nextTeam);
            }
        }
        
        initRobotPositions(); // add robots to map at starting positions      
        
        
        //TODO // GameLog gameLog = new GameLog();
    }
    
    /**
     * Adds the robot teams to their starting positions on the map
     */
    private void initRobotPositions(){
        int numTeams = teams.size();
        int size = gameMap.getMapSize();
        Point[] teamInitPoints = new Point[6];
        
//        position 
        if(numTeams == 2){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teamInitPoints[1] = new Point(size-1, (size-1)/2);
        }
        else if(numTeams == 3){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teamInitPoints[1] = new Point((size-1)/2, size-1);
            teamInitPoints[2] = new Point((size-1)/2, -((size-1)/2));
            
        }
        else if(numTeams == 6){
            teamInitPoints[0] = new Point(-(size-1), -((size-1)/2));
            teamInitPoints[1] = new Point(-((size-1)/2), (size-1)/2);
            teamInitPoints[2] = new Point((size-1)/2, size-1);
            teamInitPoints[3] = new Point(size-1, (size-1)/2);
            teamInitPoints[4] = new Point((size-1)/2, -((size-1)/2));
            teamInitPoints[5] = new Point(-((size-1)/2), -(size-1));
        }
        else{
            throw new RuntimeException("there must be 2,3, or 6 teams for a tournament");
        }
        for(int l = 0; l < teamInitPoints.length; l++){
            Team tempTeam = teams.get(l);
            Queue<Robot> robots = tempTeam.getAllRobots();
            for(int i = 0; i < robots.size(); i++){
                Robot tempRobot = robots.remove();
                tempRobot.setXPosition((int) teamInitPoints[l].getX());
                tempRobot.setYPosition((int) teamInitPoints[l].getY());
                robots.add(tempRobot);
            }
        }
        
    }
    
    /**
     * puts the game into a paused state
     */
    public void pause(){
    	//this.pause();
    }
    
    /**
     * resumes the game from the paused state
     */
    public void resume(){
        //this.resume();
    }
    
    /**
     * ends game stops all game threads from running and calls the endgame controller
     */
    public void endGame(){
        //TODO new EndController();
    }
    
    /**
     * Changes the animation speed to a new one in milliseconds.
     * @param newSpeed
     */
    public void setAnimationSpeed(int newSpeed){
        this.animationSpeed = newSpeed;
    }
    
    public int getAnimationSpeed(){
        return this.animationSpeed;
    }
    
    
    
    
    /**
     * Gets the robot that currently has the turn to play
     * @param teamNum the number that represents the team that the robot
     * to be played is on
     * @param robotNum the s/n of the robot who's turn it is
     */
    public Robot getRobot(int teamNum, int robotNum){
        return teams.get(teamNum).getTeamMember(robotNum);
    }
    
    /**
     * figures out which team gets to move next based off of the queue
     */
    public Team nextTurn(){
        while(this.nextTeamIdx.peek().numberOfLivingRobots() == 0){
            this.nextTeamIdx.remove();
        }
        Team temp = this.nextTeamIdx.remove();
        if(temp.numberOfLivingRobots() != 0){
            this.nextTeamIdx.add(temp);            
        }
        return temp;
    }


    public LinkedList<Tile> findBestPath(Tile current, Tile destination, int numMoves, 
    		Tile[][] allTiles){
		LinkedList<Tile> temp = new LinkedList<>();
		int movesLeft = numMoves;
		DIRECTION dir = gameMap.findDirection(current, destination);
		
		
		boolean atLeastOne;
		//go straight
		int x = current.getXCoord()+dir.getXCoordinate() ;
		int y  = current.getYCoord()+dir.getYCoordinate();
		
		if(allTiles[x][y].getCost() > movesLeft){
			atLeastOne = false;
		}else{
			//If distance is 3 only possible direction is straight and on Plains
			if(gameMap.calcDistance(current, destination) == numMoves){
				
			}
		}
		
		while(movesLeft > 0){
			
			
		}
		
    	
    	return null;
    }
    
    public void moveRobot(Robot robotToMove, int TeamNumber, int range, int Direction, int movesLeft){
           
      int newX;
      int newY;

      DIRECTION dir = gameMap.getDirection(Direction);
      newX = dir.getXCoordinate();
      newY = dir.getYCoordinate();
        
      newX = newX*range;
      newY = newY*range;
      
      //TODO add the team specific direction here
      if(newY > gameMap.getMaxY() || newY < gameMap.getMinY()){
          throw new RuntimeException("can't go off the edge in the Y coordinate");
      }
      if(newX > gameMap.getMaxX() || newX < gameMap.getMinY()){
          throw new RuntimeException("can't go off the edge in the X coordinate");
      }
      
//      for(int i = 0; i < teams.size(); i++){
//          Team temp = teams.get(i);
//          if(teams.get(i).getTeamNumber() == TeamNumber){
//              if(temp.getTeamDirection() == 5){
//                  newX = newX
//              }
//              newX = newX+teams.get(i).getTeamDirection();
//              
//          }
//      }
       
      Robot temp = this.teams.get(TeamNumber).getTeamMember((int) robotToMove.getMemberNumber());
       
      Tile[][] allTiles = this.gameMap.getTiles();
      
      if(movesLeft < allTiles[newX][newY].getCost()){
          throw new RuntimeException("There are not enough moves left to go there");
      }
      //Removing the robot from it's current tile
      allTiles[temp.getXPosition()][temp.getYPoisition()].removeRobot(temp);
       
      if(movesLeft < range){
          throw new RuntimeException("range to move cannot be higher than the amount of moves remaining");
      }
      
      temp.setXPosition(newX);
      temp.setYPosition(newY);
        
      //Adding the robot to the new tile
      allTiles[temp.getXPosition()][temp.getYPoisition()].addRobot(temp);
        
   }
        
    
    /**
     *  fire at the position passed in
     * @param shooter is the robot that will fire a shot
     * @param hexPos is the position that the robot is firing towards
     */
    public void shootAtSpace(Robot shooter, int range, int Direction){
    	//direction/(range*6) * 2pi - pi/2
        Tile[][] allTiles = this.gameMap.getTiles();
        
        if(range > 3){
            throw new RuntimeException("cannot shoot farther then range 3");
        }
        
        DIRECTION dir = gameMap.getDirection(Direction);
        
        int xPos = dir.getXCoordinate()*range;
        int yPos = dir.getYCoordinate()*range;
        
        LinkedList<Robot> robots = allTiles[xPos][yPos].getRobots();
        
        Iterator<Robot> iter = robots.iterator();
        
        while(iter.hasNext()){
            Robot temp = iter.next();
            temp.inflictDamage(shooter.getStrength());
            if(temp.getHealth() <= 0){
                temp.destroy();
                robots.remove(temp);
            }
        }
    }
    
    /**
     * Called every frame
     */
    public void render(){
        this.render();
    }
    
    
    public static void main(String[] args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RobotSport370";
        config.height = 800;
        config.width = 1280;
        
        Queue<Team> teamList = new LinkedList<Team>();
        for(int i=0; i<6; i++){
            Queue<Robot> robotList = JsonInterpreter.listRobots(true, null, null, null, null, null, null, null, null);
            Team newTeam = new Team(robotList, i);
            teamList.add(newTeam);
        }
        
       
    }

}


