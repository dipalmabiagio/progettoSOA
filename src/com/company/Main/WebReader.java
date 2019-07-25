/**
 * Questa classe è utile per recuperare il testo dalla pagina web sulla quale
 * vengono salvati i token cifrati, recuperati durante il processo di login
 * dell'utente
 */

package com.company.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class WebReader {

    /**
     * Questo metodo recupera il testo da una pagina web
     * @param url della pagina web
     * @return line - stringa del testo recuperato
     * @throws IOException
     */
    public static String fetchPage(URL url) throws IOException {
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return line;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (br != null) {
                br.close();
            }

        }
        System.out.println("line:");
        return line;
    }

    /**
     * Questo metodo invece elabora i token e li inserisce in un array
     * @param line - stringa recuperata dalla lettura del testo nella pagina web
     * @return tokens - array dei tokens decifrati
     */
    //TODO finire questo metodo
    public static String[] parseText(String line) {

        String [] arguments = line.split(",");
        String [] tokens = new String [4];
        for(int i= 0; i< arguments.length; i++){
            String arg[] = arguments[i].split(":");
            tokens[i] = arg[1];
        }
        /*
        ORDINE DEI TOKEN RECUPERATI
        1 - Consumer Key
        2 - Consumer Secret
        3 - Access Token
        4 - Access Token Secret
         */


        return tokens;
    }

}
