/*
To FIX:
- generazione delle nonce
- generazione del timestamp validi
- generazione della firma

ESEMPIO DI PACCHETTO FUNZIONANTE GENERATO CON POSTMAN
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2")
  .get()
  .addHeader("Authorization", "OAuth realm="dipalma.biagio%40gmail.com",oauth_consumer_key="4mEq9HTFWJIsVDAWnh0rOPPaj",oauth_token="3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1563355988",oauth_nonce="DZI4qkCBPBX",oauth_version="1.0",oauth_signature="YAZlWWB8dapP9GBDYh8XD2TFtco%3D"")
  .addHeader("User-Agent", "PostmanRuntime/7.15.0")
  .addHeader("Accept", "*//*")
        .addHeader("Cache-Control", "no-cache")
        .addHeader("Postman-Token", "9fedc401-4b1a-4245-8b11-62e13643b99a,1b23a7b7-f3ed-4e61-9434-237247a564cb")
        .addHeader("Host", "api.twitter.com")
        .addHeader("cookie", "personalization_id="v1_tIuDR0Q7/+Big0z19JL9SA=="; guest_id=v1%3A156120075303723029; lang=it")
        .addHeader("accept-encoding", "gzip, deflate")
        .addHeader("Connection", "keep-alive")
        .addHeader("cache-control", "no-cache")
        .build();

        Response response = client.newCall(request).execute();

ESEMPIO DI RISPOSTA FUNZIONANTE
{
        "created_at": "Mon Jun 24 17:50:46 +0000 2019",
        "id": 1143214899109277697,
        "id_str": "1143214899109277697",
        "text": "We’ve spoken with all developers who’ve contacted us to discuss these new rate limits and elevations, and as of tod… https://t.co/w8WoepBjeU",
        "truncated": true,
        "entities": {
            "hashtags": [],
            "symbols": [],
            "user_mentions": [],
            "urls": [
                {
                    "url": "https://t.co/w8WoepBjeU",
                    "expanded_url": "https://twitter.com/i/web/status/1143214899109277697",
                    "display_url": "twitter.com/i/web/status/1…",
                    "indices": [
                        117,
                        140
                    ]
                }
            ]
        },

 DOCUMENTAZIONE FIRMA USATA DA TWITTER
 https://developer.twitter.com/en/docs/basics/authentication/guides/creating-a-signature.html
 */

/*
Questo file si occupa di:
1. Recuperare i token dal sito di Altervista
2. Decrittare i token
3. Proporre un menù con varie scelte di interrogazioni a twitter



 */

package com.company.Main;

import okhttp3.OkHttpClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class MainProva {
    private static String oauth_cons_token = "4mEq9HTFWJIsVDAWnh0rOPPaj";
    private static String oauth_user_token = "3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a";
    private static String oauth_cons_secret = "DhZlMbuMWYlSZZoM636CShzCC4JW5DyXLkVDkql84rCAamTwWJ";
    private static String oauth_user_secret = "HBYf2iWujRKUOg3PBt6GlhW8nejvaAOA24MKN2EFDz3yi";

    private static String stringToSignTwitter = "POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521";
    private static String keyTwitter = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE";

    private static String encodeCredentials(String key, String value) throws UnsupportedEncodingException {

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

    private static String generateSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) {
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
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = Base64.getEncoder().encodeToString(byteHMAC);
        return str;
    }

    private static String generateOAuth(boolean entities, String method, String URI) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String DST="OAuth ";
        Random RAND = new Random();

        //realm
        DST+= encodeCredentials("realm", "dipalma.biagio@gmail.com");
        DST+=",";

        //consumer key
        DST += encodeCredentials("oauth_consumer_key", oauth_cons_token);
        DST+=",";

        //long timestamp = System.currentTimeMillis() / 1000L;
        //long nonce = timestamp + (long)RAND.nextInt();
        long timestamp = 1561654080;
        String nonce = "sTBrZ1DgvnS";
        System.out.println("timestamp"+timestamp);
        System.out.println("nonce gen: "+nonce);

        DST += encodeCredentials("oauth_token", oauth_user_token);
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


        //generazione della firma
        /*
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
        signatureString = appendSignature(signatureString, ("oauth_consumer_key="+oauth_cons_token));
        signatureString = appendSignature(signatureString, ("oauth_nonce="+nonce));
        signatureString = appendSignature(signatureString, ("oauth_timestamp="+timestamp));
        signatureString = appendSignature(signatureString, ("oauth_token="+oauth_user_token));
        signatureString = appendSignature(signatureString, ("oauth_version=1.0"));
        signatureString = appendSignature(signatureString, ("status=prova"));

        //signatureString = URLEncoder.encode(signatureString, "UTF-8");
        System.out.println("\n STRINGA Parameters\n");
        System.out.println(signatureString);

        /*

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

        System.out.println("\n STRINGA SignatureBase\n");
        System.out.println(signatureBaseString);

        System.out.println("\nStringa presa da Twitter\n");
        System.out.println("POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26oauth_version%3D1.0%26status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521");

        /*
        GENERAZIONE DELLA CHIAVE

         */
        String signingKey = new String();
        signingKey+=PercentEncode.encode(oauth_cons_secret);
        signingKey+="&";
        signingKey+= PercentEncode.encode(oauth_user_secret);

        System.out.println("Signign Key\n");
        System.out.println(signingKey);

        /*
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
        */
        String sign = generateSignature(signatureBaseString, oauth_cons_secret, oauth_user_secret);

        System.out.println(sign);
        return DST;
    }

    //"OAuth realm=\"dipalma.biagio%40gmail.com\",oauth_consumer_key=\"4mEq9HTFWJIsVDAWnh0rOPPaj\",oauth_token=\"3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1561630175\",oauth_nonce=\"OhNcPQO7Geu\",oauth_version=\"1.0\",oauth_signature=\"hr1JfgGzMFJ0v6UuE2Z2SinWBts%3D";
    //
    public static void main (String args[]) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        OkHttpClient client = new OkHttpClient();
        //JSONObject obj = new JSONObject("{...}");

        OauthClient oauthClient = new OauthClient(oauth_cons_token, oauth_cons_secret, oauth_user_secret, oauth_user_token);

        String oauth = oauthClient.generateOAuth(true, "GET", "https://api.twitter.com/1.1/statuses/user_timeline.json", oauthClient);

        /*
        Request request = new Request.Builder()
                .url("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2")
                .get()
                .addHeader("Authorization", "OAuth realm=\"dipalma.biagio%40gmail.com\",oauth_consumer_key=\"4mEq9HTFWJIsVDAWnh0rOPPaj\",oauth_token=\"3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1563629823\",oauth_nonce=\"qlfyJv6FBEe\",oauth_version=\"1.0\",oauth_signature=\"LH8ZrnkNw8rbTYas%2BDN6B%2B0Rhww%3D\"")
                //.addHeader("Authorization", oauth
                .addHeader("Accept", "/**")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Host", "api.twitter.com")
                .addHeader("accept-encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println(response.body().toString());



        System.out.println("INVERSIONE FIRMA");
        System.out.println("\nOauth generato da me:\n");
        System.out.println(oauth);

        System.out.println("\nOauth del pacchetto funzionante\n");
        System.out.println(request.header("Authorization"));



        System.out.println("PACCHETTO INVIATO");
        System.out.println(request.headers());
        */

        System.out.println(oauth);

    }
}
