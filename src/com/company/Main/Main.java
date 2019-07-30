/**
 * Questo file si occupa di:
 * 1. Recuperare i token dal sito di Altervista
 * 2. Decrittare i token
 * 3. Proporre un menù con varie scelte di interrogazioni a twitter
 * 4. Eseguire il servizio corrispondente
 */

package com.company.Main;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private static String oauth_cons_token = "CE2Qn7ewHvi1GcnMokbfYylle";
    private static String oauth_user_token = "CRuSu8iv0nPCCHCQmd5AR5j0aylvGhwWcph8NvNJiRsb6jDLvY";
    private static String oauth_cons_secret = "1152570724084801536-DCvhd21uGYb9F3wiX4WsxraY5YKLiN";
    private static String oauth_user_secret = "ZGhsLuSDODJPOOr6chVINgW7Jlu0s9wQcG7Vo4lQwluI7";

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

    public static void main (String args[]) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        //Recupero i token dal sito web e li decritto
        fetchTokens();

        /*
        OkHttpClient myclient = new OkHttpClient();
        OauthClient client = new OauthClient(oauth_cons_token,oauth_cons_secret,oauth_user_secret,oauth_user_token);

        Request request = new Request.Builder()
                .url("https://api.twitter.com/1.1/statuses/user_timeline.json")
                .get()
                .addHeader("Authorization",OauthClient.generateOAuth(true, "GET","https://api.twitter.com/1.1/statuses/user_timeline.json?",client))
                .addHeader("User-Agent", "Mozilla 5.0")
                .addHeader("Accept", "*//*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "api.twitter.com")
                .addHeader("Cookie","guest_id=v1%3A156120075303723029; lang=it")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        System.out.println(request.header("Authorization"));
        Response response = myclient.newCall(request).execute();
        System.out.println(response);

*/
        System.out.println("stringa finale \n\n");
        OauthClient oauthClient = new OauthClient(oauth_cons_token, oauth_cons_secret, oauth_user_secret, oauth_user_token);
        System.out.println(OauthClient.generateOAuth(true, "GET", "https://api.twitter.com/1.1/statuses/user_timeline.json?", oauthClient));


        /*
        OkHttpClient client = new OkHttpClient();
        //JSONObject obj = new JSONObject("{...}");

        OauthClient oauthClient = new OauthClient(oauth_cons_token, oauth_cons_secret, oauth_user_secret, oauth_user_token);

        String oauth = oauthClient.generateOAuth(true, "GET", "https://api.twitter.com/1.1/statuses/user_timeline.json", oauthClient);
        */

        //System.out.println(oauth);

    }
}
