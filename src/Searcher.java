import java.util.ArrayList;
import java.util.HashSet;

/**
 * Interface abstracting the search algorithm
 * 
 * @author Jad Malaeb - 201911093
 */
public interface Searcher
{
    /**
     * Maximum number of search results
     */ 
    int MAX_RES = 100;
    
    /**
     * Method to search for a certain number of files in a given location
     * 
     * @param location 
     * @param keywords to filter
     * @param resNum output size
     * @return ArrayList of images containing one or more of the included keywords with size = resNum
     */
    public ArrayList<? extends Image> search (String location, HashSet<String> keywords, int resNum);
}
