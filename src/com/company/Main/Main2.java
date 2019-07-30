package com.company.Main;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main2 {

    private static String oauth_cons_token = "4Z9Rv15sn79qWsdP6ZlbdFCLW";
    private static String oauth_user_token = "5fQaD1BiMRA6K74IBFEBeuMyLmci0C06djVUPfJUzMmIp1ySkD";
    private static String oauth_cons_secret = "1152570724084801536-lspbeFY5FWpF1UBv2pprDQjkA5qSZu";
    private static String oauth_user_secret = "YtRHjJJnmxv9m9RkOCNZrjGVT0pcwDQ5e6qTA4fKghZEa";

    public static void main(String args[]) throws TwitterException {

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true).setOAuthConsumerKey(oauth_cons_token).setOAuthConsumerSecret(oauth_cons_secret).setOAuthAccessToken(oauth_user_token).setOAuthAccessTokenSecret(oauth_user_secret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        twitter.updateStatus("ciao");




    }
}
