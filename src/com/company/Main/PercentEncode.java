/**
 * Questa classe si occupa di fornire il metodo per il percent encoding,
 * utile per costruire la stringa da firmare
 */

package com.company.Main;

import java.net.URLEncoder;


public class PercentEncode {

    /**
     * metodo per il "percent encoding"
     * @param value - da codificare
     * @return stringa codificata
     */
    static String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb;
    }
}
