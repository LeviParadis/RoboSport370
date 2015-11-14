package Models;

import Enums.SystemCommandType;

public class ForthSystemWord implements ForthWord {

    private String name;
    private SystemCommandType kind;
    
    public ForthSystemWord(String wordName) {
        this.kind = wordType(wordName);
        this.name = wordName;
    }
    
    public SystemCommandType getType(){
        return this.kind;
    }


    
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
    
    public static boolean isThisKind(String wordName) {
        SystemCommandType type = wordType(wordName);
        return (type != SystemCommandType.NOT_FORTH_WORD);
    }

    @Override
    public String forthStringEncoding() {
        return this.name;
    }
    
    public String toString(){
        return "system:" + forthStringEncoding();
    }

    @Override
    public String consoleFormat() {
        return this.forthStringEncoding();
    }

}
