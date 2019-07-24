/**
 * Questa classe è utile per recuperare il testo dalla pagina web sulla quale
 * vengono salvati i token cifrati, recuperati durante il processo di login
 * dell'utente
 */

package com.company.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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

            try {
                url = new URL("http://www.something.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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

        return line;
    }

    /**
     * Questo metodo invece elabora i token e li inserisce in un array
     * @param line - stringa recuperata dalla lettura del testo nella pagina web
     * @return tokens - array dei tokens decifrati
     */
    public static String[] parseText(String line) {

        String [] arguments;
        String [] tokens = new String [4];


        return tokens;
    }

}
