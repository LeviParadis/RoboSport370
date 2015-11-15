package Controllers;

import Views.mapView;

public class GameController {
	
    /**
     * All of the teams to be run in the simulation.
     */
	Team teams[];
	
    /**
     * holds the value to which team goes next
     */
	int nextTeamIdx;

	/**
	 * 
	 */
	LogContoller logger;
	
	/**
	 * The size of the hexmap, the size is one side length
	 */
	int hexSize;
	
	/**
	 * CHANGE
	 */
	boolean isHeadless;
	
	/**
	 * Stores the type of map view
	 */
	mapView view;
	
	/**
	 * how long it takes for each animation to complete in ??miliseconds??
	 */
	int animationSpeed;
	
	/**
	 * initializes the teams and ??sets their position on the map??
	 * @param allTeams an array that contains all of the teams playing the match
	 * @param hexSize the size of the map on one side
	 * @precond hexSize cannot be null
	 */
	public void init(Team allTeams[], int hexSize){
	    this.hexSize = hexSize;
	    
	}
	
	
	public void pause(){
	    
	}
	
	public void resume(){
	    
	}
	
	public void endGame(){
	    
	}
	
	public void scanSpace(int hexPos){
	    
	}
	
	/**
	 * Gets the robot that currently has the turn to play
	 * @param teamNum the number that represents the team that the robot
	 * to be played is on
	 * @param robotNum the s/n of the robot who's turn it is
	 */
	public void getRobot(int teamNum, int robotNum){
	    
	}
	
	public void nextTurn(){
	    
	}
	
	
	public void moveRobot(){
	    
	}
	
	/**
	 *  fire at the position passed in
	 * @param shooter is the robot that will fire a shot
	 * @param hexPos is the position that the robot is firing towards
	 * @precond the robot must be alive
	 * @precond the hexPos must be on the map
	 */
	public void shootAtSpace(Robot shooter, int hexPos){
	    
	}
	public static void main(){
		
	}
}
