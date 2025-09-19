import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Class responsible for searching the twitter database
 * 
 * @author Jad Malaeb - 201911093
 */
public class TwitterSearcher implements Searcher
{
    /**
     * @param location Username or tag (@someone)
     */
    @Override
    public ArrayList<SocialMediaImage> search (String location, HashSet<String> keywords, int resNum)
    {
        ArrayList<SocialMediaImage> res = new ArrayList<>();
        String keywordsStr = "";
        for (Iterator<String> it = keywords.iterator(); it.hasNext();)
        {
            if (it.hasNext() && !keywordsStr.isBlank())
                keywordsStr += ", ";
            keywordsStr += it.next();
        }
        
        try
        {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey("").setOAuthConsumerSecret("").setOAuthAccessToken("").setOAuthAccessTokenSecret("");
            
            Twitter tw = new TwitterFactory(cb.build()).getInstance();
            Query query = new Query ("source:" + keywordsStr);
            QueryResult twtRes = tw.search (query);
            int count = 0;

            for (Status tweet : twtRes.getTweets())
            {
                if (count > resNum)
                    break;

                if (location != null && !tweet.getUser().getScreenName().equals (location.substring (1)))
                    continue;
                
                for (MediaEntity media : tweet.getMediaEntities())
                {
                    if (media.getType().equals ("photo"))
                    {
                        res.add (new SocialMediaImage (media.getMediaURL(), tweet.getUser().getScreenName()));
                        count++;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println ("Sorry, an error occured!");
            e.printStackTrace();
        }
        
        return res;
    }
}