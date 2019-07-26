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

public class MainProva {

    private static String oauth_cons_token = "4mEq9HTFWJIsVDAWnh0rOPPaj";
    private static String oauth_user_token = "3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a";
    private static String oauth_cons_secret = "DhZlMbuMWYlSZZoM636CShzCC4JW5DyXLkVDkql84rCAamTwWJ";
    private static String oauth_user_secret = "HBYf2iWujRKUOg3PBt6GlhW8nejvaAOA24MKN2EFDz3yi";

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
        oauth_cons_secret = AESCrypto.decrypt(tokens[1]);
        oauth_cons_token = AESCrypto.decrypt(tokens[0]);
        oauth_user_secret = AESCrypto.decrypt(tokens[3]);
        oauth_user_token = AESCrypto.decrypt(tokens[2]);

        System.out.println("token decrittati:");
        for (int i= 0; i< tokens.length; i++){
            System.out.println((AESCrypto.decrypt(tokens[i])));
        }
    }

    public static void main (String args[]) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        //Recupero i token dal sito web e li decritto
        fetchTokens();


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
