package Models;

import java.util.LinkedList;


public class Tile {
    private LinkedList<Robot> robots;
    
    private int xCoordinate;
    private int yCoordinate;
    
    public Tile(int xPos, int yPos){
        this.xCoordinate = xPos;
        this.yCoordinate = yPos;
        robots = new LinkedList<Robot>();
    }
    
    public int getNumRobots(){
        return this.robots.size();
    }
    
    public LinkedList<Robot> getRobots(){
        return robots;
    }
    
    public void addRobot(Robot newRobot){
        this.robots.add(newRobot);
    }
    
    public int getXCoord(){
        return xCoordinate;
    }
    
    public int getYCoord(){
        return yCoordinate;
    }
}
