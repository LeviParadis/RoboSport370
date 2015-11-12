package Interpreters;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JsonInterpreter {

	public static void main(String[] args) {
   JSONParser parser=new JSONParser(); 
   try {
    JSONObject json = (JSONObject) parser.parse(new FileReader("resources/RobotExample.JSON"));
    System.out.println(json);
    
    
    
   } catch (IOException | ParseException e1) {
       e1.printStackTrace();
   }


    
	}


public Object robotFromJSON(){
    return null;
}

}
