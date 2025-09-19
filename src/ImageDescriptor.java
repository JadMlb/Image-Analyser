import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.net.URIBuilder;

/**
 * Class responsible to retrieve image tags when needed
 * 
 * @author Jad Malaeb - 201911093
 */
public class ImageDescriptor
{
    /**
     * Method to describe an image using the Microsoft Azure Cognitive Services' Image Analize API 
     * 
     * @param parentDir Full path of the directory containing the image
     * @param imageName The name of the image to be described
     * @return String representation of the name of the descriptor json file
     */
    public String describe (String parentDir, String imageName)
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
		
		try
		{
			URIBuilder builder = new URIBuilder ("https://image-descriptors-extraction.cognitiveservices.azure.com/vision/v3.2/analyze");

            builder.setParameter ("visualFeatures", "Tags");
            builder.setParameter ("language", "en");
            builder.setParameter ("model-version", "latest");

            URI uri = builder.build();
            HttpPost request = new HttpPost (uri);
            request.setHeader ("Content-Type", "application/octet-stream");
            request.setHeader ("Ocp-Apim-Subscription-Key", System.getenv ("Ocp-Apim-Subscription-Key"));


            // Request body
            FileEntity reqEntity = new FileEntity (new File (parentDir + imageName), ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity (reqEntity);

            CloseableHttpResponse response = httpClient.execute (request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                File dir = new File (parentDir + "tags/");
                if (!dir.exists())
                    dir.mkdir();
                String fileName = imageName.replace (imageName.substring (imageName.lastIndexOf (".")), ".json"), separator = (System.getProperty("os.name").toLowerCase().contains ("windows")) ? "\\" : "/";
                BufferedWriter writer = new BufferedWriter (new FileWriter (new File (parentDir + "tags" + separator + fileName)));

                writer.write (EntityUtils.toString (entity));
                writer.close();
                return parentDir + "tags" + separator + fileName;
            }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        return null;
    }
}
