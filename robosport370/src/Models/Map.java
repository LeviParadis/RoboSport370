package Models;
import java.awt.Point;

import Controllers.gameVariables;
import Models.Map.DIRECTION;

public class Map {
    boolean test = true;
    private int mapDiameter = gameVariables.mapSize *2 -1;
    private int height = gameVariables.mapSize;
    private int mapSize = gameVariables.mapSize;
    private int xPosMax, xPosMin, yPosMax, yPosMin;
    
    Tile[][] tiles = new Tile[mapDiameter][mapDiameter];
    
    public Map(){
        
        int inity = 0;
        int blwHalfDepth = 0;
        int abvHalfDepth = mapSize-1;
        xPosMax = this.mapSize;
        yPosMax = this.mapSize;
        xPosMin = -this.mapSize;
        yPosMin = -this.mapSize;
        
        int xPos = -(mapSize-1);
        int yPos = inity;
        
        int count = 0;

        for(int left = 0; left < mapDiameter; left++){
        	
            for(int y = 0; y < height; y++) {
                if(left > ((mapDiameter-1)/2)+1){
                   
                    tiles[left][y] = new Tile(xPos, yPos);    
                    yPos--;
                }else{
                    if(height > mapDiameter) height--;
                    tiles[left][y] = new Tile(xPos, yPos);
                    
                    yPos--;
                }
                if(test){
                    System.out.print("(" + tiles[left][y].getXCoord() +
                            "," + tiles[left][y].getYCoord() + ")");                    
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
     * Calculates distance between two tiles
     * @param cur the tile to start on 
     * @param dest the tile that is being travelled to
     * @return the distance as an integer
     */
    public int calcDistance(Tile cur, Tile dest){
    	if (cur.getXCoord() == dest.getXCoord()){
    		return Math.abs(dest.getYCoord() - cur.getYCoord());    	  		  
  	  	}
  	  	else if (cur.getYCoord() == dest.getYCoord()){
  	  		return Math.abs(dest.getXCoord() - cur.getXCoord());    	  		  
  	  	}
  	  	else{
  	  		int dx = Math.abs(dest.getXCoord() - cur.getXCoord());
  	  		int dy = Math.abs(dest.getYCoord() - cur.getYCoord());
  	  		if (cur.getYCoord() < dest.getYCoord()){
  	  			return (int) (dx + dy - (Math.ceil(dx / 2.0))-1);
  	  		}
  	  		else{
  	  			return (int) (dx + dy - (Math.floor(dx / 2.0))-1); 					  
  	  		}
  	  	}
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
        
        int side = direction/range;
        
        int depthSide = direction % range;
        
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
     * returns a direction given a current and destination tile
     * @param current the tile that the direction is going from
     * @param destination the destination tile to calculate direction
     * @return returns the direction as an enum DIRECTION.
     */
    public DIRECTION findDirection(Tile current, Tile destination){
    	int xCoord = destination.getXCoord()- current.getXCoord();
    	int yCoord = destination.getYCoord()- current.getYCoord();
    	
    	DIRECTION toRet = null;
    	for(DIRECTION dir: DIRECTION.values()){
    		if(dir.getXCoord() == xCoord && dir.getYCoord() == yCoord){
    			toRet = dir;
    		}
    	}
    	return toRet;
    }
    
    
    public Tile[][] getTiles(){
        return this.tiles;
    }
    
    public int getMapSize(){
        return this.mapSize;
    }
    
    public int getMapDiameter(){
        return this.mapDiameter;
    }

    public int getMaxY(){
        return yPosMax;
    }
    
    public int getMaxX(){
        return xPosMax;
    }
    
    public int getMinY(){
        return yPosMin;
    }
    
    public int getMinX(){
        return xPosMin;
    }
    
    public static void main(String[] args){
        Map test = new Map();   
        Tile one = new Tile(-1, -1);
        Tile two = new Tile(3, 4);
        System.out.println(test.calcDistance(one, two));
        
    }
} 
