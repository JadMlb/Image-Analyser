/**
 * Class representing an image from the internet
 * 
 * @author Jad Malaeb - 201911093
 */
public class SocialMediaImage extends Image
{
    private String username;
    
    /**
     * Creates an instance of SocialMediaImage
     * 
     * @param uri URL of the image
     * @param username Username / unique tag of the account that posted it
     */
    public SocialMediaImage (String uri, String username)
    {
        super (uri);
        this.username = username;
    }

    public String getUsername ()
    {
        return this.username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    @Override
    public String toString ()
    {
        return super.getUri() + " posted by @" + username;
    }
}
