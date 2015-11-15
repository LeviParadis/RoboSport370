package Models;

import java.util.HashMap;

/**
 * MainMenu stores the current menu option for the startup screen
 */
public class MainMenu {
	private HashMap<Integer, String> optionMap;
	private Integer currentOption;
	public static Integer NEW_TOURNAMENT = 1;
	public static Integer NEW_SIMULATION = 2;
	public static Integer EXIT = 3;
	
	/**
	 * Constructor for MainMenu, setting the default option to "New Tournament".
	 */
	public MainMenu() {
		optionMap = new HashMap<Integer, String>();

		optionMap.put(1, "New Tournament");
		optionMap.put(2, "New Simulation");
		optionMap.put(3, "Exit");
		
		currentOption = 1;
	}
	
	/**
	 * 
	 * @return current menu option
	 */
	public String currentMenuOption() {
		return optionMap.get(currentOption);
	}
	
	/**
	 * Move the current option up
	 */
	public void menuUp() {
		if(currentOption > 1) {
			currentOption--;
		}
	}
	
	/**
	 * Move the current option down
	 */
	public void menuDown() {
		if(currentOption < 3) {
			currentOption++;
		}
	}
	
	public static void main(String[] args) {

	}

}
