<?php
//ENCRYPTION CON AES modalità CBC a 128 bit di chiave
//la chiave deve essere lunga 128 bit, quindi 16 bytes
//iv è l'initialization vector
//data sono i dati da criptare
//funzione che ritorna i dati criptati e decriptati in base 64 con l'iv
class PHP_AES_Cipher {

    private static $OPENSSL_CIPHER_NAME = "aes-128-cbc"; //nome del cifrato
    private static $CIPHER_KEY_LEN = 16; //128 bit, 16 bytes
	
	//input: chiave, iv, stringa da criptare
    static function encrypt($key, $iv, $data) {
		//se la lunghezza della chiave è inferiore alla lunghezza accettata dal cifrario, si applica del padding
		//se la lunghezza è maggiore si tronca a 16n bytes
        if (strlen($key) < PHP_AES_Cipher::$CIPHER_KEY_LEN) {
            $key = str_pad("$key", PHP_AES_Cipher::$CIPHER_KEY_LEN, "0"); //padding, aggiunta di zeri
        } else if (strlen($key) > PHP_AES_Cipher::$CIPHER_KEY_LEN) {
            $key = substr($str, 0, PHP_AES_Cipher::$CIPHER_KEY_LEN); //troncato a 16 bytes
        }

		//codifica in base 64, si fa l'encryption prendendo come parametrr: la stringa, il cifrario, la chiave, l'iv
        $encodedEncryptedData = base64_encode(openssl_encrypt($data, PHP_AES_Cipher::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, $iv));
        
		//OPENSSL_RAW_DATA dice a openssl_encrypt () di restituire un cipherText come dati non elaborati. 
		//Per default, lo restituisce con codifica Base64.


		//codifica in base 64 dell'iv
		$encodedIV = base64_encode($iv);
        $encryptedPayload = $encodedEncryptedData.":".$encodedIV;

        return $encryptedPayload;

    }

	//input: chiave, stringa da decriptare
    static function decrypt($key, $data) {
        if (strlen($key) < PHP_AES_Cipher::$CIPHER_KEY_LEN) {
            $key = str_pad("$key", PHP_AES_Cipher::$CIPHER_KEY_LEN, "0"); //0 padding
        } else if (strlen($key) > PHP_AES_Cipher::$CIPHER_KEY_LEN) {
            $key = substr($str, 0, PHP_AES_Cipher::$CIPHER_KEY_LEN); //troncato a 16 bytes
        }

        $parts = explode(':', $data); //Separa i dati criptati dall'iv
		//decryption, decodifica base 64
        $decryptedData = openssl_decrypt(base64_decode($parts[0]), PHP_AES_Cipher::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, base64_decode($parts[1]));

        return $decryptedData;
    }

}
?>