package Models;

import Enums.SystemCommandType;
import Exceptions.ForthParseException;

public class ForthSystemWord implements ForthWord {

    private String name;
    private SystemCommandType kind;
    
    /**
     * create a forth system word from a plain text string
     * @param wordName the plain text version of the word
     * @throws ForthParseException if the input word does not match a system command
     */
    public ForthSystemWord(String wordName) throws ForthParseException {
        SystemCommandType type = wordType(wordName);
       if(type == SystemCommandType.NOT_FORTH_WORD){
           throw new ForthParseException("attempted to instantiate a forth system command with unknown word " + wordName);
       }
        this.kind = type;
        this.name = wordName;
    }
    
    /**
     * @return which system command this represents
     */
    public SystemCommandType getType(){
        return this.kind;
    }
    
    /**
     * tells us which command the plain text string represents
     * @param wordName the plain text string
     * @return an enum value representing one of the system words
     */
    private static SystemCommandType wordType(String wordName){
        if(wordName.equals("health")){
            return SystemCommandType.HEALTH;
        } else if(wordName.equals("movesLeft")){
            return SystemCommandType.MOVES_LEFT;
        }else if(wordName.equals("firepower")){
            return SystemCommandType.FIRE_POWER;
        }else if(wordName.equals("team")){
            return SystemCommandType.TEAM;
        }else if(wordName.equals("member")){
            return SystemCommandType.MEMBER;
        }else if(wordName.equals(".")){
            return SystemCommandType.CONSOLE;
        }else if(wordName.equals("random")){
            return SystemCommandType.RANDOM;
        }else if(wordName.equals("shoot!")){
            return SystemCommandType.SHOOT;
        }else if(wordName.equals("move!")){
            return SystemCommandType.MOVE;
        } else if(wordName.equals("scan!")){
            return SystemCommandType.SCAN;
        } else if(wordName.equals("identify!")){
            return SystemCommandType.IDENTIFY;
        } else if(wordName.equals("send!")){
            return SystemCommandType.MAIL_SEND;
        } else if(wordName.equals("mesg?")){
            return SystemCommandType.MAIL_CHECK;
        } else if(wordName.equals("recv!")){
            return SystemCommandType.MAIL_RECIEVE;
        } else if(wordName.equals("hex")){
            return SystemCommandType.HEX;
        } else if(wordName.equals("?")){
            return SystemCommandType.VAR_CHECK;
        } else if(wordName.equals("!")){
            return SystemCommandType.VAR_ASSIGN;
        } else if(wordName.equals("and")){
            return SystemCommandType.AND;
        } else if(wordName.equals("or")){
            return SystemCommandType.OR;
        } else if(wordName.equals("invert")){
            return SystemCommandType.INVERT;
        } else if(wordName.equals("<")){
            return SystemCommandType.LESS;
        } else if(wordName.equals("<=")){
            return SystemCommandType.LESS_EQUAL;
        } else if(wordName.equals("=")){
            return SystemCommandType.EQUAL;
        } else if(wordName.equals("<>")){
            return SystemCommandType.NOT_EQUAL;
        } else if(wordName.equals("=>")){
            return SystemCommandType.GREATER_EQUAL;
        } else if(wordName.equals(">")){
            return SystemCommandType.GREATER;
        } else if(wordName.equals("+")){
            return SystemCommandType.ADD;
        } else if(wordName.equals("-")){
            return SystemCommandType.SUBTRACT;
        } else if(wordName.equals("*")){
            return SystemCommandType.MULTIPLY;
        } else if(wordName.equals("/mod")){
            return SystemCommandType.DIVIDE;
        } else if(wordName.equals("drop")){
            return SystemCommandType.DROP;
        } else if(wordName.equals("dup")){
            return SystemCommandType.DUPLICATE;
        } else if(wordName.equals("swap")){
            return SystemCommandType.SWAP;
        } else if(wordName.equals("rot")){
           return SystemCommandType.ROTATE;
        }
        //command is not one of the built in commands
        return SystemCommandType.NOT_FORTH_WORD;
    }
    
    /**
     * @param wordName a plain text string
     * @return whether or not the string represents a system forth command
     */
    public static boolean isThisKind(String wordName) {
        SystemCommandType type = wordType(wordName);
        return (type != SystemCommandType.NOT_FORTH_WORD);
    }

    @Override
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding() {
        return this.name;
    }
    
    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String toString(){
        return "performing action: " + forthStringEncoding();
    }


}
