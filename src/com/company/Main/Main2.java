package com.company.Main;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class Main2 {

    private static String oauth_cons_token = "CE2Qn7ewHvi1GcnMokbfYylle";
    private static String oauth_user_token = "CRuSu8iv0nPCCHCQmd5AR5j0aylvGhwWcph8NvNJiRsb6jDLvY";
    private static String oauth_cons_secret = "1152570724084801536-DCvhd21uGYb9F3wiX4WsxraY5YKLiN";
    private static String oauth_user_secret = "ZGhsLuSDODJPOOr6chVINgW7Jlu0s9wQcG7Vo4lQwluI7";

    public static void main(String args[]) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true).setOAuthConsumerKey(oauth_cons_token)
                                .setOAuthConsumerSecret(oauth_cons_secret)
                                .setOAuthAccessToken(oauth_user_token)
                                .setOAuthAccessTokenSecret(oauth_user_secret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        List<Status> status = twitter.getHomeTimeline();

        for (Status s: status){
            System.out.println(s.getUser().getName()+ " -- "+s.getText());
        }


    }
}
