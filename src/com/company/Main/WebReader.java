/**
 * Questa classe è utile per recuperare il testo dalla pagina web sulla quale
 * vengono salvati i token cifrati, recuperati durante il processo di login
 * dell'utente
 */

package com.company.Main;

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

        System.out.println("line:");
        return line;
    }

    /**
     * Questo metodo invece elabora i token e li inserisce in un array
     * @param line - stringa recuperata dalla lettura del testo nella pagina web
     * @return tokens - array dei tokens decifrati
     */
    public static String[] parseText(String line) {
/*
        String [] arguments = line.split(",");
        String [] tokens = new String [4];
        System.out.println("token cifrati");
        for(int i= 0; i< arguments.length; i++){
            String arg[] = arguments[i].split(" \\");
            tokens[i] = arg[1];
            System.out.println(tokens[i]+"\n");

 */
        String tokens [] = line.split(",");
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
