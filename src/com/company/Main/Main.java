package com.company.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Main {

    private String oauth_cons_token = "4mEq9HTFWJIsVDAWnh0rOPPaj";
    private String oauth_user_token = "3227549738-zcDNPN4tHKxUxZK7L3dzhkNOK3yyNG7tNBANj7a";
    private String oauth_cons_secret = "DhZlMbuMWYlSZZoM636CShzCC4JW5DyXLkVDkql84rCAamTwWJ";
    private String oauth_user_secret = "HBYf2iWujRKUOg3PBt6GlhW8nejvaAOA24MKN2EFDz3yi";

    // HTTP POST request
    private static void sendPost() throws Exception {

        String url = "/oauth2/token";

        HttpClient client = new DefaultHttpClient();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http");


        builder.setHost("api.twitter.com").setPath(url);
        System.out.println("add param,sethost,setpath complete");

        URI uri = builder.build();
        System.out.println("uri="+uri);

        HttpPost request = new HttpPost("https://api.twitter.com"+url);
        System.out.println("httpPost"+request);

        // add header
        // add request header
        request.addHeader("Accept-Encoding", "gzip");
        request.addHeader("Authorization", ("Basic "+generateBareerToken()));
        request.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        request.addHeader("Host", "api.twitter.com");
        request.addHeader("User-Agent", "My Twitter App v1.0.23");

        //body
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("grant_type", "client-credentials"));
        request.setEntity(new UrlEncodedFormEntity(pairs ));

        System.out.println("STAMPA PACCHETTO");
        //stampa pacchetto
        Header[] headers = request.getAllHeaders();
        System.out.println(request.toString());
        System.out.println(request.getEntity().toString());
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
    private static String generateBareerToken() throws UnsupportedEncodingException {
        String encodedKey = URLEncoder.encode("xvz1evFS4wEEPTGEFPHBog", "UTF-8");
        String encodedSecret = URLEncoder.encode("L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg", "UTF-8");
        String bareer = encodedKey+":"+encodedSecret;
        System.out.println(bareer);
        bareer = Base64.encodeBase64String(bareer.getBytes());

        return bareer;
    }

    public static void main (String args[]) throws Exception {
        System.out.println(generateBareerToken());
        sendPost();
    }
}
