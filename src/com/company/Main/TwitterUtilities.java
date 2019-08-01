/**
 * Classe all'interno della quale inserire i metodi per le operazioni con twitter.
 */
package com.company.Main;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtilities {

    public static String createTweet(String tweet) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status = twitter.updateStatus(tweet);
        return status.getText();
    }
}
