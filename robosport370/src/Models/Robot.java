package Models;

public class Robot {

    int health,strength,hexPosition;
    String name,number;
    
    
	/**
	 * Destroys this robot
	 */
	void destroy()
	{
		
	}
	
	/**
	 * returns an object that contains Win and Loss stats
	 * of this Robot
	 * @return
	 */
	Object getWinsLossStats(){

		return null;
	}
	/**
	 * 
	 * @returns true if robot's health is above 0
	 */
	boolean isAlive()
	{
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
	/**
	 * @return the number of moves this robot can move per turn
	 */
	int getMovesPerTurn(){
	    
	    return 0;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
