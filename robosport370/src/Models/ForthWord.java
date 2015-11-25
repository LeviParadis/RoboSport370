package Models;

public interface ForthWord {
    /**
     * @return string encoding that can read by the forth parser
     */
    public String forthStringEncoding();
    /**
     *  @return the string value that is printed by forth in the  console
     */
    public String toString();
}
