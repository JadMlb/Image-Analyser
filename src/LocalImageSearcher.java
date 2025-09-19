import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Class responsible for searching locally in the provided dataset
 * 
 * @author Jad Malaeb - 201911093
 */
public class LocalImageSearcher implements Searcher
{
    /**
     * @param location directory to search into
     */
    @Override
    public ArrayList<LocalImage> search (String location, HashSet<String> keywords, int resNum)
    {
        ArrayList<LocalImage> res = new ArrayList<>();
        ArrayList<String> allImages = imagesIn (location);
        int j = 0;
        
        for (int i = 0; i < allImages.size() && j < resNum && j < Searcher.MAX_RES; i++)
        {
            LocalImage img = new LocalImage (location, allImages.get (i));
            HashSet<String> imgAttribs = img.getAttributes();
            if (!imgAttribs.isEmpty())
            {
                // Check if keywords describe the current image 
                for (Iterator<String> it = keywords.iterator(); it.hasNext();)
                {
                    String curKW = it.next();
                    boolean add = !imgAttribs.stream().filter(att -> (curKW.equals (att) || curKW.contains (att))).collect(Collectors.toList()).isEmpty();
                    
                    // In this case, the image matches the description and can be added to result list
                    if (add)
                    {
                        res.add (img);
                        j++;
                    }
                }
            }
            else // Without any search keywords output all images within the range of the query
            {
                res.add (img);
                j++;
            }
        }

        return res;
    }

    /**
     * Retrieves all images (i.e., files ending with .png, .jpg or .jpeg) from specified directory
     * 
     * @param directory to look in for images
     * @return ArrayList of type String containing the names of the images with their extension
     */
    private ArrayList<String> imagesIn (String directory)
    {
        ArrayList<String> allFiles = new ArrayList<> (Arrays.asList (new File(directory).list()));
        Collections.sort (allFiles);
        return new ArrayList<> (allFiles.stream().filter(file -> (file.endsWith (".png") || file.endsWith (".jpg") || file.endsWith ("jpeg"))).collect (Collectors.toList()));
    }
}