/**
 * Questa classe si occupa della decifratura dei token, recuperati dalla pagina web
 * applicando il protocollo AES sia per la decifratura che la decifratura
 */

package com.company.Main;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCrypto{

    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";


    /**
     * Questo metodo si occupa della cifratura, usando il protocollo AES
     * @param value - valore da crittare
     * @return line - stringa cifrata
     */
    public static String encrypt(String value) {
        try {
            //parametri
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            //modalità
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            //codifica in base 64
            byte[] encodedBytes = Base64.getEncoder().encode(value.getBytes());
            String line = encodedBytes.toString();
            return line;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Questo metodo si occupa della decifratura, usando il protocollo AES
     * @param encodedBytes - stringa da decifrare
     * @return line - stringa decifrata
     */
    public static String decrypt(String encodedBytes) {
        try {
            //parametri
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");


            //modalità
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //decodifica base 64

            byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
            String line = decodedBytes.toString();
            return line;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
