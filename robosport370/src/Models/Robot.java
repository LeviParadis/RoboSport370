package Models;

import java.util.HashMap;


/**
 * 
 * @author Levi Paradis
 * 
 * Notes for report: Replaced getWinsLossStats with getters for each win loss, and tie 
 * value
 */
public class Robot {

    private int health,strength,hexPosition,movesPerTurn,wins,losses,ties;
    private String name,number;
    HashMap forthVariables,forthCommands; //TODO: Make a hashmap
    
    /**
     * 
     * @return health value
     */
    public int getHealth() {
		return health;
	}
    /**
     * 
     * @param health
     */
	public void setHealth(int health) {
		this.health = health;
	}
	/**
	 * 
	 * @return
	 */
	public int getStrength() {
		return strength;
	}
	/**
	 * 
	 * @param strength
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}
	/**
	 * 
	 * @return
	 */
	public int getHexPosition() {
		return hexPosition;
	}
	/**
	 * 
	 * @param hexPosition
	 */
	public void setHexPosition(int hexPosition) {
		this.hexPosition = hexPosition;
	}
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * 
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * @return the number of moves this robot can move per turn
	 */
	int getMovesPerTurn(){
	    
	    return movesPerTurn;
	}
	
  
	public int getWins() {
		return wins;
	}
	public int getLosses() {
		return losses;
	}
	public int getTies() {
		return ties;
	}
	/**
	 * Destroys this robot
	 */
	void destroy()
	{
		this.health = 0;
	}
	
	/**
	 * @returns true if robot's health is above 0
	 */
	boolean isAlive()
	{
		if (this.health > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param objectToPush the object that gets pushed
	 */
	void pushMailbox(Object objectToPush){

	}
	/**
	 * 
	 * @returns an object off a mailbox
	 */
	Object popMailbox(){
	    
	    return null;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
