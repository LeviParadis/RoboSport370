package Models;

public class ForthSystemWord implements ForthWord {

    private ForthWord nextWord;
    private String name;
    private SystemForthWord kind;
    
    public ForthSystemWord(String wordName) {
        this.kind = wordType(wordName);
        this.name = wordName;
    }
    
    public SystemForthWord getValue(){
        return this.kind;
    }

    @Override
    public boolean hasNext() {
        return nextWord==null;
    }

    @Override
    public ForthWord getNext() {
        return nextWord;
    }
    
    @Override
    public void setNextWord(ForthWord next){
        nextWord = next;
    }
    
    private static SystemForthWord wordType(String wordName){
        if(wordName.equals("health")){
            return SystemForthWord.HEALTH;
        } else if(wordName.equals("movesLeft")){
            return SystemForthWord.MOVES_LEFT;
        }else if(wordName.equals("firepower")){
            return SystemForthWord.FIRE_POWER;
        }else if(wordName.equals("team")){
            return SystemForthWord.TEAM;
        }else if(wordName.equals("member")){
            return SystemForthWord.MEMBER;
        }else if(wordName.equals(".")){
            return SystemForthWord.CONSOLE;
        }else if(wordName.equals("random")){
            return SystemForthWord.RANDOM;
        }else if(wordName.equals("shoot!")){
            return SystemForthWord.SHOOT;
        }else if(wordName.equals("move!")){
            return SystemForthWord.MOVE;
        } else if(wordName.equals("scan!")){
            return SystemForthWord.SCAN;
        } else if(wordName.equals("identify!")){
            return SystemForthWord.IDENTIFY;
        } else if(wordName.equals("send!")){
            return SystemForthWord.MAIL_SEND;
        } else if(wordName.equals("mesg?")){
            return SystemForthWord.MAIL_CHECK;
        } else if(wordName.equals("recv!")){
            return SystemForthWord.MAIL_RECIEVE;
        } else if(wordName.equals("hex")){
            return SystemForthWord.HEX;
        } else if(wordName.equals("?")){
            return SystemForthWord.VAR_CHECK;
        } else if(wordName.equals("!")){
            return SystemForthWord.VAR_ASSIGN;
        } else if(wordName.equals("and")){
            return SystemForthWord.AND;
        } else if(wordName.equals("or")){
            return SystemForthWord.OR;
        } else if(wordName.equals("invert")){
            return SystemForthWord.INVERT;
        } else if(wordName.equals("<")){
            return SystemForthWord.LESS;
        } else if(wordName.equals("<=")){
            return SystemForthWord.LESS_EQUAL;
        } else if(wordName.equals("=")){
            return SystemForthWord.EQUAL;
        } else if(wordName.equals("<>")){
            return SystemForthWord.NOT_EQUAL;
        } else if(wordName.equals("=>")){
            return SystemForthWord.GREATER_EQUAL;
        } else if(wordName.equals(">")){
            return SystemForthWord.GREATER;
        } else if(wordName.equals("+")){
            return SystemForthWord.ADD;
        } else if(wordName.equals("-")){
            return SystemForthWord.SUBTRACT;
        } else if(wordName.equals("*")){
            return SystemForthWord.MULTIPLY;
        } else if(wordName.equals("/mod")){
            return SystemForthWord.DIVIDE;
        } else if(wordName.equals("drop")){
            return SystemForthWord.DROP;
        } else if(wordName.equals("dup")){
            return SystemForthWord.DUPLICATE;
        } else if(wordName.equals("swap")){
            return SystemForthWord.SWAP;
        } else if(wordName.equals("rot")){
           return SystemForthWord.ROTATE;
        }
        //command is not one of the built in commands
        return SystemForthWord.NOT_FORTH_WORD;
    }
    
    public static boolean isThisKind(String wordName) {
        SystemForthWord type = wordType(wordName);
        return (type != SystemForthWord.NOT_FORTH_WORD);
    }

    @Override
    public String forthStringEncoding() {
        return this.name;
    }
    
    public String toString(){
        return "system:" + forthStringEncoding();
    }

}
