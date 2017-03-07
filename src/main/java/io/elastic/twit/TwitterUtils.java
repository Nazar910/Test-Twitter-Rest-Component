package io.elastic.twit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.json.JsonObject;

/**
 * Created by pyvov on 03.03.2017.
 */
public class TwitterUtils {
    private static final Logger logger = LoggerFactory.getLogger(TwitterUtils.class);
    private static Twitter twitter;
    private static JsonObject config;

    public static Status updateStatus(String message, JsonObject configuration) {
        try {
            configTwitter(configuration);
            Status status = twitter.updateStatus(message);
            logger.info("Successfully updated status of " + twitter.getScreenName());
            return status;
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getScreenName(JsonObject configuration) {
        try {
            configTwitter(configuration);
            String screenName = twitter.getScreenName();
            logger.info("Get screenName: " + screenName);
            return screenName;
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteStatus(long id, JsonObject configuration) {
        try {
            configTwitter(configuration);
            twitter.destroyStatus(id);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static void configTwitter(JsonObject configuration) {
        if (config != null && config.equals(configuration)) return;
        twitter = new TwitterFactory().getInstance();
        String consumerKey = configuration.getString("CONSUMER_KEY");
        String consumerSecret = configuration.getString("CONSUMER_SECRET");
//        String accessTokenStr = configuration.getString("accessToken");
//        String accessTokenSecret = configuration.getString("accessTokenSecret");
//        AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecret);
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        try {
            RequestToken requestToken = twitter.getOAuthRequestToken();
            twitter.getOAuthAccessToken(requestToken);
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
//        twitter.setOAuthAccessToken(accessToken);
        config = configuration;
    }
}
