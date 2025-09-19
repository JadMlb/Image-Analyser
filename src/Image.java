/**
 * Abstract parent class of any image of any source
 * <p>
 * Supported image types are png, jpg, and jpeg
 * 
 * @author Jad Malaeb - 201911093
 */
public abstract class Image
{
    private String uri;

    /**
     * Creates a new instance of Image
     * 
     * @param uri URI to image
     */
    public Image (String uri)
    {
        this.uri = uri;
    }

    public String getUri ()
    {
        return this.uri;
    }

    public void setUri (String uri)
    {
        this.uri = uri;
    }

    /**
     * Image's implementation of toString() method
     */
    @Override
    public abstract String toString ();
}