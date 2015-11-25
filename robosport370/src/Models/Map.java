package Models;
import Controllers.gameVariables;
import Models.Map.DIRECTION;

public class Map {
    
    private int mapDiameter = gameVariables.mapSize *2 -1;
    private int height = gameVariables.mapSize;
    private int mapSize = gameVariables.mapSize;
    private int xPosMax, xPosMin, yPosMax, yPosMin;
    
    Tile[][] tiles = new Tile[mapDiameter][mapDiameter];
    
    public Map(){
        
    	xPosMax = this.mapSize;
    	yPosMax = this.mapSize;
    	xPosMin = -this.mapSize;
    	yPosMin = -this.mapSize;
    	
        //int xPos = -mapDiameter/ 2 ;
        int yPos = (mapSize - 2) + 1/2;
        int count = 0;
        for(int x = 0; x < mapDiameter; x++){
            for(int y = 0; y < height; y++) {
                yPos = yPos -1;
                tiles[x][y] =  new Tile(x, y);
                System.out.print(y);
                count++;
            }
            if( (mapDiameter) / (x+1) >= 2) {
                yPos = yPos + 1 * height + 1/2;
                height++;
            }
            else {
                yPos = yPos + 1 * height - 1/2;
                height--;
            }
            System.out.println();
            
        }
        System.out.println(count);
    }
    
    public static void main(String[] args){
        new Map();
        
        
    }
    
    public int calcDistance(int x1, int x2, int y1, int y2){
        return (int) Math.sqrt( ((x2-x1)*(x2-x1)) + ((y1-y2)*(y1-y2)) );
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
        
        public int getXCoordinate(){
           
            return xCoordinate;
        }
        public int getYCoordinate(){
            return yCoordinate;            
        }
    }
    
    public DIRECTION getDirection(int Direction){
        
        if(Direction == 0){
            return DIRECTION.NORTH;
           
        }
        else if(Direction == 1){
            return DIRECTION.NORTH_EAST;
        }
        else if(Direction == 2){
            return DIRECTION.SOUTH_EAST;
        }
        else if(Direction == 3){
            return DIRECTION.SOUTH;
        }
        else if(Direction == 4){
            return DIRECTION.SOUTH_WEST;
        }
        else if(Direction == 5){
            return DIRECTION.NORTH_WEST;
        }
        else{
            throw new RuntimeException("The provided direction is not valid");
        }
        
       
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
    
} 
