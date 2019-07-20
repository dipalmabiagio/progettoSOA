/*
Questa classe serve a creare un client Oauth per eseguire delle
richieste a twitter


 */
package com.company.Main;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class OauthClient {

    String oauth_cons_token = "4mEq9HTFWJIsVDAWnh0rOPPaj";
    String oauth_user_token = "3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a";
    String oauth_cons_secret = "DhZlMbuMWYlSZZoM636CShzCC4JW5DyXLkVDkql84rCAamTwWJ";
    String oauth_user_secret = "HBYf2iWujRKUOg3PBt6GlhW8nejvaAOA24MKN2EFDz3yi";

    static String stringToSignTwitter = "POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
    static String keyTwitter = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";


    public OauthClient(String oauth_cons_token, String oauth_cons_secret, String oauth_user_secret, String oauth_user_token){
        this.oauth_cons_secret = oauth_cons_secret;
        this.oauth_cons_token = oauth_cons_token;
        this.oauth_user_secret = oauth_user_secret;
        this.oauth_user_token = oauth_user_token;
    }

    static String encodeCredentials(String key, String value) throws UnsupportedEncodingException {

        String q = URLEncoder.encode(key, "UTF-8");
        q+= "=";
        q+= "\"";
        q+=URLEncoder.encode(value, "UTF-8");
        q+= "\"";
        return q;
    }

    public static String appendSignature(String signatureString, String str){
        signatureString+="&";
        signatureString+=str;

        return signatureString;
    }

    static String generateOAuth(boolean entities, String method, String URI, OauthClient client) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String DST="OAuth ";
        Random RAND = new Random();

        //realm
        DST+= encodeCredentials("realm", "dipalma.biagio@gmail.com");
        DST+=",";

        //consumer key
        DST += encodeCredentials("oauth_consumer_key", client.oauth_cons_token);
        DST+=",";

        //long timestamp = System.currentTimeMillis() / 1000L;
        //long nonce = timestamp + (long)RAND.nextInt();
        long timestamp = 1563629823;
        String nonce = "qlfyJv6FBEe";
        System.out.println("timestamp"+timestamp);
        System.out.println("nonce gen: "+nonce);

        DST += encodeCredentials("oauth_token", client.oauth_user_token);
        DST+=",";

        //metodo firma
        DST += encodeCredentials("oauth_signature_method", "HMAC-SHA1");
        DST+=",";

        //timestamp
        DST += encodeCredentials("oauth_timestamp", String.valueOf(timestamp));
        DST+=",";


        //consumer nonce
        DST += encodeCredentials("oauth_nonce", String.valueOf(nonce));
        DST+=",";

        //version
        DST+= encodeCredentials("oauth_version","1.0");
        DST+=",";


        /*
        GENERAZIONE DELLA FIRMA

        1 - Percent encode every key and value that will be signed.
        2 - Sort the list of parameters alphabetically [1] by encoded key [2].
        3 - For each key/value pair:
        4 - Append the encoded key to the output string.
        5 - Append the ‘=’ character to the output string.
        6 - Append the encoded value to the output string.
        7 - If there are more key/value pairs remaining, append a ‘&’ character to the output string.
         */

        System.out.println("\nRiferimento:"+DST+"\n");

        String signatureString = new String();
        //signatureString = URLEncoder.encode("include_entities=true", "UTF-8");
        signatureString += "include_entities=true";
        signatureString = appendSignature(signatureString, ("oauth_consumer_key="+ client.oauth_cons_token));
        signatureString = appendSignature(signatureString, ("oauth_nonce="+nonce));
        signatureString = appendSignature(signatureString, ("oauth_signature_method="+"HMAC-SHA1"));
        signatureString = appendSignature(signatureString, ("oauth_timestamp="+timestamp));
        signatureString = appendSignature(signatureString, ("oauth_token="+ client.oauth_user_token));
        signatureString = appendSignature(signatureString, ("oauth_version=1.0"));
        signatureString = appendSignature(signatureString, ("status=prova"));

        //signatureString = URLEncoder.encode(signatureString, "UTF-8");
        System.out.println("\n STRINGA Parameters\n");
        System.out.println(signatureString);

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
        signatureBaseString+=URLEncoder.encode(URI, "UTF-8");
        signatureBaseString+="&";
        signatureBaseString+=URLEncoder.encode(signatureString, "UTF-8");

        System.out.println("\nSTRINGA SignatureBase\n");
        System.out.println(signatureBaseString);

        System.out.println("\nStringa presa da Twitter\n");
        System.out.println("POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26" +
                "oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26" +
                "oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26" +
                "oauth_signature_method%3DHMAC-SHA1%26" +
                "oauth_timestamp%3D1318622958%26" +
                "oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26" +
                "oauth_version%3D1.0%26" +
                "status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521");

        /*
        1. Percent encode consumer secret
        2. Ampersand (&)
        3. Percent encode the token secret
         */

        String signingKey = new String();
        signingKey+=URLEncoder.encode(client.oauth_cons_secret, "UTF-8");
        signingKey+="&";
        signingKey+=URLEncoder.encode(client.oauth_user_secret, "UTF-8");

        System.out.println("Signign Key\n");
        System.out.println(signingKey);

        System.out.println("\n Nuovo metodo di firma\n Binario:");
        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] result = mac.doFinal(signatureBaseString.getBytes());
        System.out.println(result);

        System.out.println("\nRisultato Base 64:\n");
        System.out.println(Base64.getEncoder().encodeToString(result));

        DST += encodeCredentials("oauth_signature", Base64.getEncoder().encodeToString(result));

        System.out.println("TWITTER\n");
        SecretKeySpec key_Spec = new SecretKeySpec(keyTwitter.getBytes(), "HmacSHA1");

        Mac mymac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] my_result = mac.doFinal(stringToSignTwitter.getBytes());
        System.out.println(my_result);

        System.out.println("\nRisultato Base 64:\n");
        System.out.println(Base64.getEncoder().encodeToString(my_result));

        return DST;
    }
}
