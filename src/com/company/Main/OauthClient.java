/*
Questa classe serve a creare un client Oauth per eseguire delle
richieste a twitter


 */
package com.company.Main;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class OauthClient {

    static String oauth_cons_token;
    static String oauth_user_token;
    static String oauth_cons_secret;
    static String oauth_user_secret;

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Costruttore
     * @param oauth_cons_token
     * @param oauth_cons_secret
     * @param oauth_user_secret
     * @param oauth_user_token
     */
    public OauthClient(String oauth_cons_token, String oauth_cons_secret, String oauth_user_secret, String oauth_user_token){
        this.oauth_cons_secret = oauth_cons_secret;
        this.oauth_cons_token = oauth_cons_token;
        this.oauth_user_secret = oauth_user_secret;
        this.oauth_user_token = oauth_user_token;
    }

    /**
     * Metodo che genera la nonce
     * @return nonce
     */
    private static String generateNonce() {
        String nonce = Long.toString(Math.abs(secureRandom.nextLong())) + System.currentTimeMillis();
        return nonce;
    }

    /**
     * Metodo che genera il timestamp
     * @return timestamp
     */
    private static long generateTimestamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }

    /**
     * Metodo per codificare le credenziali all'interno della stringa
     * dello header Authorization
     * @param key
     * @param value
     * @return
     */
    static String encodeCredentials(String key, String value){

        String q = PercentEncode.encode(key);
        q+= "=";
        q+= "\"";
        q+= PercentEncode.encode(value);
        q+= "\"";
        return q;
    }

    public static String appendSignature(String signatureString, String str){
        signatureString+="&";
        signatureString+=str;

        return signatureString;
    }

    /**
     * Metodo che genera la firma
     * @param signatureBaseStr
     * @param oAuthConsumerSecret
     * @param oAuthTokenSecret
     * @return
     */
    private static String generateSignature(String signatureBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {

        /*
        GENERAZIONE DELLA CHIAVE - signinKey
        1. Percent encode consumer secret
        2. Ampersand (&)
        3. Percent encode the token secret
        */

        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = PercentEncode.encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = PercentEncode.encode(oAuthConsumerSecret) + '&' + PercentEncode.encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatureBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = Base64.getEncoder().encodeToString(byteHMAC);
        return str;
    }


    /**
     * Metodo che genera la stringa da firmare
     * @param DST
     * @param client
     * @param nonce
     * @param timestamp
     * @param method
     * @param URI
     * @return
     */
    private static String generateSignatureBaseString (String DST, OauthClient client, String nonce, String timestamp, String method, String URI){
        /*
        GENERAZIONE DELLA FIRMA - step 1: generazione della signatureString

        1 - Percent encode every key and value that will be signed.
        2 - Sort the list of parameters alphabetically [1] by encoded key [2].
        3 - For each key/value pair:
        4 - Append the encoded key to the output string.
        5 - Append the ‘=’ character to the output string.
        6 - Append the encoded value to the output string.
        7 - If there are more key/value pairs remaining, append a ‘&’ character to the output string.
         */

        String signatureString = new String();
        signatureString = appendSignature(signatureString, ("oauth_consumer_key="+ client.oauth_cons_token));
        signatureString = appendSignature(signatureString, ("oauth_nonce="+nonce));
        signatureString = appendSignature(signatureString, ("oauth_signature_method="+"HMAC-SHA1"));
        signatureString = appendSignature(signatureString, ("oauth_timestamp="+timestamp));
        signatureString = appendSignature(signatureString, ("oauth_token="+ client.oauth_user_token));
        signatureString = appendSignature(signatureString, ("oauth_version=1.0"));

        /*
        GENERAZIONE DELLA STRINGA BASE DA FIRMARE
        1 - Convert the HTTP Method to uppercase and set the output string equal to this value.
        2 - Append the ‘&’ character to the output string.
        3 - Percent encode the URL and append it to the output string.
        4 - Append the ‘&’ character to the output string.
        5 - Percent encode the parameter string and append it to the output string.
         */

        String signatureBaseString = new String();
        signatureBaseString+=method.toUpperCase();
        signatureBaseString+="&";
        signatureBaseString+= PercentEncode.encode(URI);
        signatureBaseString+="&";
        signatureBaseString+= PercentEncode.encode(signatureString);


        return signatureBaseString;
    }

    /**
     * Metodo che genera la stringa da inserire nello header Authorization
     * @param entities
     * @param method
     * @param URI
     * @param client
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
     public static String generateOAuth(boolean entities, String method, String URI, OauthClient client) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String DST="OAuth ";
        Random RAND = new Random();

        System.out.println("1 - "+DST);

        //realm
        DST+= encodeCredentials("realm", "progettosoasec@gmail.com");
        DST+=",";

        System.out.println("2 - "+DST);

        //consumer key
        DST += encodeCredentials("oauth_consumer_key", client.oauth_cons_token);
        DST+=",";

        System.out.println("3 - "+DST);

        DST += encodeCredentials("oauth_token", client.oauth_user_token);
        DST+=",";

         System.out.println("4 - "+DST);

        //metodo firma

        DST += encodeCredentials("oauth_signature_method", "HMAC-SHA1");
        DST+=",";
        System.out.println("5 - "+DST);

        //TIMESTAMP
        long timestamp = generateTimestamp();
        DST += encodeCredentials("oauth_timestamp", Long.toString(timestamp));
        DST+=",";
        System.out.println("6 - "+DST);

        //NONCE
        //long nonce = timestamp + (long)RAND.nextInt()
        String nonce = generateNonce();
        DST += encodeCredentials("oauth_nonce", String.valueOf(nonce));
        DST+=",";
        System.out.println("7 - "+DST);

        //versione di oauth
        DST+= encodeCredentials("oauth_version","1.0");
        DST+=",";
         System.out.println("8 - "+DST);

        //GENERAZIONE FIRMA
        String signatureBaseString = generateSignatureBaseString(DST, client, nonce, String.valueOf(timestamp), method, URI);

        //AGGIUNTA FIRMA
         //DST += encodeCredentials("oauth_signature", generateSignature(signatureBaseString, OauthClient.oauth_cons_secret, OauthClient.oauth_user_secret));
         DST+= generateSignature(signatureBaseString, oauth_cons_secret,oauth_user_secret);
         System.out.println("9 - "+DST);

        return DST;
    }
}
