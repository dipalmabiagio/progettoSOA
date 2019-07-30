package com.company.Main;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main2 {

    private static String oauth_cons_token = "3uJXgZ7RdOh4MB1HdqvVs5RGO";
    private static String oauth_user_token = "3227549738-fDp2NRMuYdnwwyU6Wx5ZaBha8lrvYAPnlR7WyJA";
    private static String oauth_cons_secret = "wdyyLPsaOWbjOprHocSVyM9FYvUOZ71fzqI4k3SCbNHpeSy8IT";
    private static String oauth_user_secret = "kgyKOIoOSa4V4ktJQY7506dg5eT5zaK5qUV7CpbIvWTrh";

    //link dal quale recuperare le credenziali cifrate in AES
    private static URL urlCredentials;

    static {
        try {
            urlCredentials = new URL("http://www.progettosoasecurity.altervista.org/token.txt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Questo metodo recupera i token da remoto e li decifra,
     * a questo punto li salva nelle variabili statiche di questa classe.
     * @throws IOException
     */
    private static void fetchTokens() throws IOException {
        //Recupero i token dal sito web e li decritto
        String textFromPage = WebReader.fetchPage(urlCredentials);
        System.out.println(textFromPage);
        String tokens[] = WebReader.parseText(textFromPage);

        //inserisco i token nei campi privati di questa classe e applico la decifratura
        oauth_cons_secret = AESCrypto.decrypt(AESCrypto.key,tokens[1]);
        oauth_cons_token = AESCrypto.decrypt(AESCrypto.key,tokens[0]);
        oauth_user_secret = AESCrypto.decrypt(AESCrypto.key,tokens[3]);
        oauth_user_token = AESCrypto.decrypt(AESCrypto.key,tokens[2]);

        System.out.println("token decrittati:");
        for (int i= 0; i< tokens.length; i++){
            System.out.println((AESCrypto.decrypt(AESCrypto.key,tokens[i])));
        }
    }

    public static void main(String args[]) throws TwitterException, IOException {

        //fetchTokens();

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true).setOAuthConsumerKey(oauth_cons_token).setOAuthConsumerSecret(oauth_cons_secret).setOAuthAccessToken(oauth_user_token).setOAuthAccessTokenSecret(oauth_user_secret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        twitter.updateStatus("ciao");




    }
}
