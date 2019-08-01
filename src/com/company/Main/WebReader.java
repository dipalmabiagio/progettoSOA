/**
 * Questa classe è utile per recuperare il testo dalla pagina web sulla quale
 * vengono salvati i token cifrati, recuperati durante il processo di login
 * dell'utente
 */

package com.company.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class WebReader {

    /**
     * Questo metodo recupera il testo da una pagina web
     * @param url della pagina web
     * @return line - stringa del testo recuperato
     * @throws IOException
     */
    public static String fetchPage(URL url) throws IOException {
        Scanner s = new Scanner(url.openStream());
        String line = s.nextLine();
        return line;
    }

    /**
     * Questo metodo invece elabora i token e li inserisce in un array
     * @param line - stringa recuperata dalla lettura del testo nella pagina web
     * @return tokens - array dei tokens decifrati
     */
    public static String[] parseText(String line) throws IOException {

        String tokens [] = line.split(",");

        //creazione del file delle property per la libreria twitter4j
        File file = new File("twitter4j.properties");

        //Create the file
        if (file.createNewFile())
        {
            System.out.println("Il file delle credenziali è stato generato!");
        } else {
            //se il file esiste, va ripulito e riscritto
            FileWriter writer = new FileWriter(file);
            writer.write("debug=true\noauth.consumerKey="+tokens[0]+"\noauth.consumerSecret="+tokens[1]+"\noauth.accessToken="+tokens[2]+"\noauth.accessTokenSecret="+tokens[3]+"\n");
            writer.close();

        }

        //Write Content
        FileWriter writer = new FileWriter(file);
        writer.write("debug=true\noauth.consumerKey="+tokens[0]+"\noauth.consumerSecret="+tokens[1]+"\noauth.accessToken="+tokens[2]+"\noauth.accessTokenSecret="+tokens[3]+"\n");
        writer.close();

        return tokens;
        /*
        ORDINE DEI TOKEN RECUPERATI
        1 - Consumer Key
        2 - Consumer Secret
        3 - Access Token
        4 - Access Token Secret
         */
    }

}
