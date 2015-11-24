package Models;

import java.util.LinkedList;


public class Tile {
    private LinkedList<Robot> robots;
    
    private int xCoordinate;
    private int yCoordinate;
    private TYPE type;
    //private int cost; 
    
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
            this.type = TYPE.FOREST;
        }
        else if(cost == TYPE.MOUNTAINS.getCost()){
            this.type = TYPE.MOUNTAINS;
        }
        else if(cost == TYPE.PLAINS.getCost()){
            this.type = TYPE.PLAINS;
        }
        else if(cost == TYPE.WATER.getCost()){
            this.type = TYPE.WATER;
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
    
    /**
     * Returns the cost to move across the tile
     * @return the tiles cost
     */
    public int getCost(){
        return this.type.getCost();
    }
}
