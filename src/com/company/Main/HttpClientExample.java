package com.company.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


public class HttpClientExample {

    private String oauth_cons_token = "4mEq9HTFWJIsVDAWnh0rOPPaj";
    private String oauth_user_token = "3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a";
    private String oauth_cons_secret = "DhZlMbuMWYlSZZoM636CShzCC4JW5DyXLkVDkql84rCAamTwWJ";
    private String oauth_user_secret = "HBYf2iWujRKUOg3PBt6GlhW8nejvaAOA24MKN2EFDz3yi";

    private final String USER_AGENT = "Mozilla 5.0";

    public static void main(String[] args) throws Exception {

        HttpClientExample http = new HttpClientExample();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        //System.out.println("\nTesting 2 - Send Http POST request");
        //http.sendPost();

    }

    public String appendSignature(String signatureString, String str){
        signatureString+="&";
        signatureString+=str;

        return signatureString;
    }

    //generazione delle credenziali oauth per le POST
    private String generateOAuth(boolean entities, HttpPost httpPost) throws UnsupportedEncodingException, ParseException {
        String DST="OAuth ";
        Random RAND = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "22-01-2015 10:20:56";
        Date date = sdf.parse(dateInString);

        //generazione di timestamp e nonce
        long timestamp = System.currentTimeMillis() / 1000L;
        long nonce = timestamp + (long)RAND.nextInt();

        //consumer key
        DST += encodeCredentials("oauth_consumer_key", oauth_cons_token);
        DST+=", ";

        //consumer nonce
        DST += encodeCredentials("oauth_nonce", generateNonce());
        DST += ", ";

        //generazione della firma
        String signatureString=URLEncoder.encode("include_entities=true", "UTF-8");

        signatureString = appendSignature(signatureString,URLEncoder.encode(("oauth_consumer_key="+oauth_cons_token), "UTF-8"));
        signatureString = appendSignature(signatureString,URLEncoder.encode(("oauth_nonce=kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"), "UTF-8"));

        System.out.println(timestamp);
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_timestamp="+timestamp), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_token="+oauth_user_token), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_version=1.0"), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode((""), "UTF-8"));

        System.out.println("\n STRINGA GENERATA\n");
        System.out.println(signatureString);

        String signatureBaseString = new String();
        signatureBaseString+=httpPost.getMethod().toUpperCase();
        signatureBaseString+="&";
        signatureBaseString+=URLEncoder.encode(httpPost.getURI().toString(), "UTF-8");
        signatureBaseString+="&";
        signatureBaseString+=URLEncoder.encode(signatureString, "UTF-8");

        System.out.println("\n STRINGA GENERATA2\n");
        System.out.println(signatureBaseString);

        //costruzione della chiave
        String signingKey = new String();
        signingKey+=URLEncoder.encode(oauth_cons_secret, "UTF-8");
        signingKey+="&";
        signingKey+=URLEncoder.encode(oauth_user_secret, "UTF-8");

        String binaryCiphertext = hmacSha(signingKey, signatureBaseString, "HmacSHA1");
        String cipherext = Base64.getEncoder().encodeToString(binaryCiphertext.getBytes());
        System.out.println("\n STRINGA GENERATA3\n");
        System.out.println(cipherext);

        DST += encodeCredentials("oauth_signature", cipherext);
        DST += ", ";

        //metodo firma
        DST += encodeCredentials("oauth_signature_method", "HMAC-SHA1");
        DST+=", ";

        //timestamp
        DST += encodeCredentials("oauth_timestamp", Long.toString(timestamp));
        DST+=", ";

        DST += encodeCredentials("oauth_token", oauth_user_token);
        DST += ", ";

        DST+= encodeCredentials("oauth_version", "1.0");

        return DST;
    }


    //OAuth realm="dipalma.biagio%40gmail.com",oauth_consumer_key="4mEq9HTFWJIsVDAWnh0rOPPaj",oauth_token="3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a",oauth_signature_method="HMAC-SHA1",
    // oauth_timestamp="1561203016",oauth_nonce="zWrTg1QXhtA",oauth_version="1.0",oauth_signature="STnTr9fJTNw%2B%2FZRUMYDKfJIbm0Q%3D"
    //
    //generazione dell'autenticazione oauth per le GET
    private String generateOAuth(boolean entities, HttpGet httpGet) throws UnsupportedEncodingException {
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
        long timestamp = 1561203016;
        String nonce = "zWrTg1QXhtA";
        System.out.println("timestamp"+timestamp);
        System.out.println("nonce gen"+nonce);

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


        //generazione della firma
        //String signatureString=URLEncoder.encode("include_entities=true", "UTF-8");
        String signatureString = new String();
        signatureString = appendSignature(signatureString,URLEncoder.encode(("oauth_consumer_key="+oauth_cons_token), "UTF-8"));
        signatureString = appendSignature(signatureString,URLEncoder.encode(("oauth_nonce="+nonce), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_timestamp="+timestamp), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_token="+oauth_user_token), "UTF-8"));
        signatureString=appendSignature(signatureString, URLEncoder.encode(("oauth_version=1.0"), "UTF-8"));
        //signatureString=appendSignature(signatureString, URLEncoder.encode(("status=prova"), "UTF-8"));

        System.out.println("\n STRINGA GENERATA\n");
        System.out.println(signatureString);

        String signatureBaseString = new String();
        signatureBaseString+=httpGet.getMethod().toUpperCase();
        signatureBaseString+="&";
        signatureBaseString+=URLEncoder.encode(httpGet.getURI().toString(), "UTF-8");
        signatureBaseString+="&";
        signatureBaseString+=URLEncoder.encode(signatureString, "UTF-8");

        System.out.println("\n STRINGA GENERATA2\n");
        System.out.println(signatureBaseString);

        //costruzione della chiave
        String signingKey = new String();
        signingKey+=URLEncoder.encode(oauth_cons_secret, "UTF-8");
        signingKey+="&";
        signingKey+=URLEncoder.encode(oauth_user_secret, "UTF-8");

        String binaryCiphertext = hmacSha(signingKey, signatureBaseString, "HmacSHA1");
        String cipherext = Base64.getEncoder().encodeToString(binaryCiphertext.getBytes());
        System.out.println("\n STRINGA GENERATA3\n");
        System.out.println(cipherext);

       // DST += encodeCredentials("oauth_signature", cipherext);
        DST += encodeCredentials("oauth_signature", "STnTr9fJTNw%2B%2FZRUMYDKfJIbm0Q%3D");
        //DST += ", ";







        //DST+= encodeCredentials("oauth_version", "1.0");

        return DST;
    }

    private static String hmacSha(String KEY, String VALUE, String SHA_TYPE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes("UTF-8"), SHA_TYPE);
            Mac mac = Mac.getInstance(SHA_TYPE);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));

            byte[] hexArray = {
                    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
                    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
                    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
                    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
            };
            byte[] hexChars = new byte[rawHmac.length * 2];
            for ( int j = 0; j < rawHmac.length; j++ ) {
                int v = rawHmac[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String generateNonce(){
        SecureRandom sr = new SecureRandom();
        byte[] rndBytes = new byte[8];
        sr.nextBytes(rndBytes);
        String nonce = Base64.getEncoder().encodeToString(rndBytes);

        return nonce;
    }
    private String encodeCredentials(String key, String value) throws UnsupportedEncodingException {

        String q = URLEncoder.encode(key, "UTF-8");
        q+= "=";
        q+= "\"";
        q+=URLEncoder.encode(value, "UTF-8");
        q+= "\"";
        return q;
    }

    /*
    Host: api.twitter.com
    Authorization: OAuth realm="dipalma.biagio%40gmail.com",oauth_consumer_key="4mEq9HTFWJIsVDAWnh0rOPPaj",oauth_token="3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1561203016",oauth_nonce="zWrTg1QXhtA",oauth_version="1.0",oauth_signature="STnTr9fJTNw%2B%2FZRUMYDKfJIbm0Q%3D"
    User-Agent: PostmanRuntime/7.15.0
    Accept: */ /*
    Cache-Control: no-cache
    Postman-Token: 36249c3a-fd12-43da-b02a-51d6fb90ce5d,9bf61917-c0b2-447d-9f65-a6e0044b8120
    Host: api.twitter.com
    cookie: personalization_id="v1_tIuDR0Q7/+Big0z19JL9SA=="; guest_id=v1%3A156120075303723029; lang=it
    accept-encoding: gzip, deflate
    Connection: keep-alive
    cache-control: no-cache
     */

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // add request header
        //request.addHeader("Host", "api.twitter.com");
        request.addHeader("Authorization", generateOAuth(true, request));
        //request.addHeader("User-Agent", USER_AGENT);
        //request.addHeader("Accept", "*/*");
        //request.addHeader("Cache-Control", "no-cache");
        //request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        System.out.println("PACCHETTO INVIATO");
        for (Header h : request.getAllHeaders()){
            System.out.println(h.toString());
        }
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }


        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());
        System.out.println(result.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "/1.1/statuses/update.json";

        HttpClient client = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder();
        builder.setHost("api.twitter.com").setPath("/1.1/statuses/update.json");
        builder.addParameter("include_entities", "true");
        builder.setScheme("https");
        URI uri = builder.build();
        System.out.println("uri="+uri);

        HttpPost request = new HttpPost(uri);


        request.setURI(builder.build());

        System.out.println("add param,sethost,setpath complete");

        // add header
        // add request header
        request.addHeader("Accept", "*/*");
        request.addHeader("Connection", "close");
        request.addHeader("User-Agent", "OAuth gem v0.4.4");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");

        request.addHeader("Authorization", generateOAuth(true, request));
        request.addHeader("Host", "api.twitter.com");

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        String status = "Hello";
        pairs.add(new BasicNameValuePair("status", URLEncoder.encode(status, "UTF-8")));
        request.setEntity(new UrlEncodedFormEntity(pairs ));

        //stampa pacchetto
        Header[] headers = request.getAllHeaders();
        System.out.println(request.toString());
        for (Header header : headers) {
            System.out.println(header.getName() + ": " + header.getValue());
        }

        HttpResponse response = client.execute(request);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + request.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

}
