package Models;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import Controllers.gameVariables;

public class Map {
    boolean test = false;
    private int mapDiameter = gameVariables.mapSize *2 -1;
    private int height = gameVariables.mapSize;
    private int mapSize = gameVariables.mapSize;
    private int xPosMax, xPosMin, yPosMax, yPosMin;
    
    LinkedList<Tile> tiles = new LinkedList<Tile>();
    
    public Map(){
        
        int inity = 0;
        int blwHalfDepth = 0;

        xPosMax = this.mapSize -1;
        yPosMax = this.mapSize -1;
        xPosMin = -this.mapSize +1;
        yPosMin = -this.mapSize +1;
        
        int xPos = -(mapSize-1);
        int yPos = inity;
        
        int count = 0;

        for(int left = 0; left < mapDiameter; left++){
        	
            for(int y = 0; y < height; y++) {
                Tile temp = new Tile(xPos, yPos);
                temp.setType(chooseIndex(left, y));
                if(left > ((mapDiameter-1)/2)+1){
                   
                    tiles.add(temp);    
                    yPos--;
                }else{
                    if(height > mapDiameter) height--;
                    tiles.add(temp);
                    
                    yPos--;
                }
                if(test){
                    System.out.print("(" + temp.getXCoord() +
                            "," + temp.getYCoord() + ")" + temp.getCost());                    
                }
                
                count++;
            }          
            if(left >= ((mapDiameter-1)/2)){
                xPos++;
                yPos = mapSize-1;
                height--;
            }else{
                blwHalfDepth++;
                xPos++;
                yPos = blwHalfDepth;
                height++;    
            }
            if(test)System.out.println();
        }
        
        if(test)System.out.println(count);
    }
    
    /**
     * This function finds a index (0 through 3) based on map position
     * @param tile the tile being chosen
     * @param i the current column
     * @param j the current height
     * @return the index based of the current map tile
     */
    public int chooseIndex(int i, int j) {
        // This is essentially a bunch of magic number manipulation
        // It generates consistent results
        // But makes it so the map isn't either random or just all of one type of tile
        if(i == 9 || i == 12 || ( i == 3 && j > 7)) {
            return 1;
        }
        if((i == 0 || i == mapDiameter - 1) && j == mapSize - mapSize / 2 - 1) {
            return 1;
        }
        if(j < i - mapSize && (i % 3 == 2 || i % 3 == 2)) {
            return 2;
        }
        if(j > i + 3) {
            return 2;
        }

        if(j < 3 && (i % 3 == 2 || i % 3 == 1)) {
            return 2;
        }
        if(i < (mapDiameter / 2 + 1) && i < (mapDiameter / 2 - 1) && (j % 3 == 2 || j % 3 == 1) ) {
            return 3;
        }
        if((i - 1 == mapSize/2) && j < 6 && j > 3) {
            return 3;
        }
        if(i == mapSize / 2 + 3 && j == 6) {
            return 3;
        }
        if(i < 4 && i > 1 && j < 4 && j > 2) {
            return 10;
        }
        if(i > 5 && (j % 5 == 4)) {
            return 10;
        }
        return 1;
    }
    
    public enum DIRECTION{
        NORTH(0,1),
        SOUTH(0,-1),
        NORTH_WEST(-1,0),
        NORTH_EAST(1,1),
        SOUTH_WEST(-1,-1),
        SOUTH_EAST(1,0);
        
        private final int xCoordinate;
        private final int yCoordinate;
        
        DIRECTION(int xCoordinate, int yCoordinate){
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }
        
        public int getXCoord(){
            return xCoordinate;
        }
        public int getYCoord(){
            return yCoordinate;            
        }
    }
    
    public enum SIDE_VECTORS{
        SIDE_ZERO(1,0),
        SIDE_ONE(0,-1),
        SIDE_TWO(-1,-1),
        SIDE_THREE(-1,0),
        SIDE_FOUR(0,1),
        SIDE_FIVE(1,1);
        
        private final int xCoordinate;
        private final int yCoordinate;
        
        SIDE_VECTORS(int xCoordinate, int yCoordinate){
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }
        
        public int getXCoord(){  
            return xCoordinate;
        }
        public int getYCoord(){
            return yCoordinate;            
        }
        
    }
    
    /**
     * returns the direction coordinates with range 1 given the game direction (0,1,2,3...)
     * @param Direction the direction in game format (0,1,2,3...)
     * @return returns the type DIrection with x and y coordinates
     */
    public Point getDirection(int direction, int range){
        int side;
        int depthSide;
        if (range ==0){
           side = 0;
           depthSide = 0;
        } else {
          side = direction/range;
          depthSide = direction % range;
        }
        
        
        int retX, retY;
        
        int x = 0,y = 0,sideX = 0,sideY = 0;
        

        if(side == 0){
           y = DIRECTION.NORTH.getYCoord(); 
           x = DIRECTION.NORTH.getXCoord();
           sideX = SIDE_VECTORS.SIDE_ZERO.getXCoord();
           sideY = SIDE_VECTORS.SIDE_ZERO.getYCoord();
          
        }
        else if(side == 1){
            y = DIRECTION.NORTH_EAST.getYCoord();
            x = DIRECTION.NORTH_EAST.getXCoord();
            sideX = SIDE_VECTORS.SIDE_ONE.getXCoord();
            sideY = SIDE_VECTORS.SIDE_ONE.getYCoord();
        }
        else if(side == 2){
            y = DIRECTION.SOUTH_EAST.getYCoord();
            x = DIRECTION.SOUTH_EAST.getXCoord();
            sideX = SIDE_VECTORS.SIDE_TWO.getXCoord();
            sideY = SIDE_VECTORS.SIDE_TWO.getYCoord();
        }
        else if(side == 3){
            y = DIRECTION.SOUTH.getYCoord();
            x = DIRECTION.SOUTH.getXCoord();
            sideX = SIDE_VECTORS.SIDE_THREE.getXCoord();
            sideY = SIDE_VECTORS.SIDE_THREE.getYCoord();
        }
        else if(side == 4){
            y = DIRECTION.SOUTH_WEST.getYCoord();
            x = DIRECTION.SOUTH_WEST.getXCoord();
            sideX = SIDE_VECTORS.SIDE_FOUR.getXCoord();
            sideY = SIDE_VECTORS.SIDE_FOUR.getYCoord();
        }
        else if(side == 5){
            y = DIRECTION.NORTH_WEST.getYCoord();
            x = DIRECTION.NORTH_WEST.getXCoord();
            sideX = SIDE_VECTORS.SIDE_FIVE.getXCoord();
            sideY = SIDE_VECTORS.SIDE_FIVE.getYCoord();
        }
        
        retX = range * x + depthSide * sideX;
        retY = range * y + depthSide * sideY;
        
        return new Point(retX, retY);
        
       
    }
    
   /**
    * Returns the desired tile if it exist on the map 
    * @param x the x coordinate
    * @param y the y coordinate
    * @return the tile desired if found otherwise returns null
    */
    public Tile findTile(int x, int y){
        Tile toRet = null;
        
        Iterator<Tile> iter = tiles.iterator();
        boolean found = false;
        while(iter.hasNext() && !found){
            Tile temp = iter.next();
            
            if(temp.getXCoord() == x && temp.getYCoord() == y ){
                toRet = temp;
                found = true;
            }
        }
        return toRet;    
    }
    
    public LinkedList<Tile> getTiles(){
        return this.tiles;
    }
    
    public int getMapSize(){
        return this.mapSize;
    }
    
    public int getMapDiameter(){
        return this.mapDiameter;
    }

    public boolean isValidTile(Tile checkTile){
        int checkX = checkTile.getXCoord();
        int checkY = checkTile.getYCoord();
        
        if(checkX == 0){
            //if x is 0, y can range from yMin to yMax
            return (checkY >= this.yPosMin && checkY <= this.yPosMax);
        } else if(checkX < 0 && checkX > xPosMax){
            //if x is negative, y can range from yMin to (yMax + x)
            return (checkY >= this.yPosMin && checkY <= (this.yPosMax + checkX));
        } else if(checkX > 0 && checkX < xPosMin) {
            //if x is positive, y can range from (yMin + x) to yMax
            return (checkY >= (this.yPosMin + checkX) && checkY <= this.yPosMax);
        }
        else{
            return false;
        }
    }
    
    public static void main(String[] args){
        Map test = new Map();   
        Tile one = new Tile(-1, -1);
        Tile two = new Tile(3, 4);
        System.out.println(test.getDirection(7, 2));
    }
} 
