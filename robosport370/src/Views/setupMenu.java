package Views;

import java.util.HashMap;

public class setupMenu {
	private HashMap<Integer, String> optionMap;
	private Integer currentOption;
	public static Integer NEW_TOURNAMENT = 1;
	public static Integer NEW_SIMULATION = 2;
	public static Integer EXIT = 3;
	
	public setupMenu() {
		optionMap = new HashMap<Integer, String>();

		optionMap.put(1, "New Tournament");
		optionMap.put(2, "New Simulation");
		optionMap.put(3, "Exit");
		
		currentOption = 1;
	}
	
	public String getCurrentOption() {
		return optionMap.get(currentOption);
	}
	
	public void up() {
		if(currentOption > 1) {
			currentOption--;
		}
	}
	
	public void down() {
		if(currentOption < 3) {
			currentOption++;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
