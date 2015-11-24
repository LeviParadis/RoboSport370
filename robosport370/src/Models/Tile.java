package Models;

import java.util.LinkedList;


public class Tile {
    private LinkedList<Robot> robots;
    
    private int xCoordinate;
    private int yCoordinate;
    private int cost; 
    
    private enum TYPE{
        WATER(10),
        MOUNTAINS(3),
        FOREST(2),
        PLAINS(1);
        
        private final int cost;
        
        TYPE(int cost){
            this.cost = cost;
        }
        
        public int getCost(){
            return this.cost;
        }
        
        
        
    }
    
    public void setType(int cost){
        
        if(cost == TYPE.FOREST.getCost()){
            this.cost = cost;
        }
        else if(cost == TYPE.MOUNTAINS.getCost()){
            this.cost = cost;
        }
        else if(cost == TYPE.PLAINS.getCost()){
            this.cost = cost;
        }
        else if(cost == TYPE.WATER.getCost()){
            this.cost = cost;
        }
        
    }
    
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
    
    public void addRobot(Robot robot){
        this.robots.add(robot);
    }
    
    public void removeRobot(Robot robot){
        this.robots.remove(robot);
    }
    
    public int getXCoord(){
        return xCoordinate;
    }
    
    public int getYCoord(){
        return yCoordinate;
    }
}
