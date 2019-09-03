/**
 * Classe all'interno della quale inserire i metodi per le operazioni con twitter.
 */
package com.company.Main;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class TwitterUtilities {

    private static String NOME_UTENTE = "progettosoa";

    /**
     * Questo metodo serve per postare un tweet
     * @param tweet
     * @return
     * @throws TwitterException
     */
    public static String createTweet(String tweet) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Status status = twitter.updateStatus(tweet);
        return status.getText();
    }

    /**
     * Questo metodo stampa la timeline (bacheca) dei tweet dell'utente
     * @throws TwitterException
     */
    public static void getHomeTimeline() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        System.out.println("Mostro la home timeline");
        for (Status status : statuses) {
            System.out.println(status.getUser().getName() + ":" +
                    status.getText());
        }
    }


    /**
     * Questo metodo mostra le info dell'utente
     * @throws TwitterException
     */
    public static void infoUtente () throws TwitterException{
        Twitter twitter = new TwitterFactory().getInstance();
        User user = null;
        try {
            user = twitter.showUser(TwitterUtilities.NOME_UTENTE);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        if (user.getStatus() != null) {
            System.out.println("@" + user.getScreenName() + " - " + user.getStatus().getText());
        } else {
            // the user is protected
            System.out.println("********** NOME UTENTE ***********");
            System.out.println("Nome Utente= @" + user.getScreenName());
        }
        System.exit(0);
    }
}
