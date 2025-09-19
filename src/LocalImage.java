import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Class representing an image that is in the dataset
 * 
 * @author Jad Malaeb - 201911093
 */
public class LocalImage extends Image
{
    private String name;
    private HashSet<String> attributes;
    
    /**
     * Generates a new instance of LocalImage
     * 
     * @param uri Full path to the directory containing the image
     * @param name Name of the image
     */
    public LocalImage (String uri, String name)
    {
        super (uri);
        this.name = name;
        this.attributes = retrieveAttributes (uri, name);
    }

    public String getName ()
    {
        return this.name;
    }

    public void setName (String name)
    {
        this.name = name;
    }
    
    public HashSet<String> getAttributes ()
    {
        return this.attributes;
    }

    public void setAttributes (HashSet<String> attributes)
    {
        this.attributes = attributes;
    }
    
    /**
     * Loads json file containing annotations of the image and parses it to retrieve objects in that image.
     * 
     * @param parentDir parent directory of the image to be searched
     * @param imageName name of the image with its extension
     * @return HashSet<String> containing the objects of that image, which are the keywords
     */
    private HashSet<String> retrieveAttributes (String parentDir, String imageName)
    {
        HashSet<String> at = new HashSet<>();
        try
        {
            String imgExt = imageName.substring (imageName.lastIndexOf (".")); 
            File meta = new File (parentDir + "tags" + ((System.getProperty("os.name").toLowerCase().contains ("windows")) ? "\\" : "/") + imageName.replace (imgExt, ".json"));

            if (!meta.exists())
                meta = new File (new ImageDescriptor().describe (parentDir, imageName));

            BufferedReader reader = new BufferedReader (new FileReader (meta));

            String jsonTags = "";
            for (String line = reader.readLine(); line != null; line = reader.readLine())
                jsonTags += line;

            int idx = jsonTags.indexOf ("\"tags\":[");
            if (idx > -1)
            {
                jsonTags = jsonTags.substring(idx + 8).substring (0, jsonTags.indexOf ("]"));
                String [] tagsNConfidence = jsonTags.split (",");
                
                for (String s : tagsNConfidence)
                {
                    String [] tagObj = s.split (":");
                    if (tagObj[0].contains ("\"name\""))
                    {
                        at.add (tagObj[1].substring (1, tagObj[1].length() - 1));
                    }
                }
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return at;
    }

    @Override
    public String toString ()
    {
        return super.getUri() + name;
    }
}
