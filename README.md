# Esempio di implementazione OAuth con Twitter

## Cos'é OAuth?

L’obiettivo principale di OAuth è di consentire ad un Client l’accesso ad una risorsa, dopo essere stato approvato dal proprietario della stessa, mediante l’emissione di un token da parte di un Server autoritativo. Questo meccanismo è utilizzato da molte compagnie come ad esempio Google, Facebook, Amazon, Twitter, per consentire all' utente di condividere informazioni circa il proprio account con altre applicazioni o siti web e garantire a questi ultimi di accedervi senza fornire alcuna password e senza condividerne la propria identità.

Gli elementi principali di OAuth sono:
* L’Utente o Resource Owner, che garantisce l’accesso alle risorse (porzioni del
proprio account), di cui è proprietario, protette sul Resource Service. È l’utente dell’applicazione iscritto sulla piattaforma del Service Provider.
* Il Service Provider o Resource Server: il Server utilizzato per poter accedere alle risorse o il servizio web che fornisce le informazioni.
* Il Client o Consumer: è l’applicazione che cerca di ottenere l’accesso all’account, tramite il permesso dell’Utente.
* L’Authorization Server: è il Server che mostra l'interfaccia all’interno della quale l'Utente approva o nega la richiesta di accesso. In piccoli ambienti può coincidere con il Resource Server.

![OAuth](progettoSOA/doc/Schermata 2020-03-02 alle 2.38.49 PM.png)

Per approfondire il funzionamento di Oauth è possibile leggere la [relazione completa] (doc/Progetto Sicurezza delle Architetture Orientate ai Servizi.pdf)

## Componenti del progetto

Il progetto si compone principalmente di 3 elementi:
* Un account Twitter Sviluppatore (sarà proprietario dei token)
* Un Sito web dove implementare Oauth
* Un software che opera in locale che utilizzi i token di twitter per fare richieste

### Sito Web con Oauth

#### Redirect.php
Il sito web dove abbiamo implementato Oauth è disponibile a questo [link](https://progettosoasecurity.altervista.org/callback.php). Questo sito presenta semplicemente un tasto per fare il login con Twitter, questo tasto fa una redirect dell'utente sul social network in modo tale che possa fornire l'accesso alle proprie informazioni personali. 

    //CREAZIONE OGGETTO OAUTH CON CREDENZIALI CLIENT
    $connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET);
 
    //CREDENZIALI TEMPORANEE
    //getRequestToken è in twitteroauth.php
    $request_token = $connection->getRequestToken(OAUTH_CALLBACK);


    //SALVATAGGIO DELLE CREDENZIALI
    $_SESSION['oauth_token'] = $token = $request_token['oauth_token'];
    $_SESSION['oauth_token_secret'] = $request_token['oauth_token_secret'];
    
Il Client, per poter ottenerle, deve effettuare una richiesta HTTP POST all’endpoint “Temporary credential Request”. All’URI è necessario aggiungere il parametro “oauth_callback”, cioè un URI assoluto al quale il Server redirezionerà l'utente quando avrà completato lo step di autorizzazione.

GetRequestToken è una funzione che prende in input il parametro OAUTH_CALLBACK e lo utilizza nella richiesta GET per poter crerare un OAuth Consumer con i parametri oauth_token e oauth_token_secret.

Le credenziali temporanee vengono revocate una volta che il Client ha ottenuto il l’access token. Per garantire maggiore sicurezza, le credenziali temporanee hanno un tempo di vita limitato.

La funzione _getAuthorizeURL_ prende in input il token e il flag "sign in with twitter" impostato a true. A questo punto il Server deve verificare la validità della richiesta e, in caso affermativo, rispondere al Client con un set di credenziali temporanee (sotto forma di identificatore ID e shared secret).

#### Callback.php

La callback.php permette di creare un oggetto OAuth con il CONSUMER_KEY, il CONSUMER_SECRET, l’ APP_TOKEN e l’ APP_TOKEN_SECRET e in seguito di fare una richiesta per recuperare l’ACCESS_TOKEN, che viene successivamente salvato:

    //SE IL TOKEN è VECCHIO, FARE IL REDIRECT ALLA PAGINA
    if (isset($_REQUEST['oauth_token']) && $_SESSION['oauth_token'] !==         $_REQUEST['oauth_token']) {
    $_SESSION['oauth_status'] = 'oldtoken';
    header('Location: ./clearsessions.php');
    }
    //CREAZIONE OGGETTO OAUTH CON APP KEY/SECRET E TOKEN KEY/SECRET
    $connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $_SESSION['oauth_token'],     $_SESSION['oauth_token_secret']);


    //RICHIESTA DI ACCESS TOKEN
    //twitteroauth.php
    $access_token = $connection->getAccessToken($_REQUEST['oauth_verifier']);

La funzione _getAccessToken_ prende in input il parametro oauth_verifier settato a false, poi fa una OAuthRequest di tipo get usando come parametro l'oauth_verifier stesso.
Crea poi un OAuthConsumer avente come token l'oauth_token o l'oauth_token_secret.

    //SALVATAGGIO DELL'ACCESS TOKEN
    $_SESSION['access_token'] = $access_token;

    unset($_SESSION['oauth_token']);
    unset($_SESSION['oauth_token_secret']);

    if (200 == $connection->http_code) {
  
      //L'UTENTE è STATO VERIFICATO E L'ACCESS TOKEN PUò ESSERE SALVATO PER USO FUTURO
      $_SESSION['status'] = 'verified';
     header('Location: ./index.php');
    } else {
      header('Location: ./clearsessions.php');
    }

A questo punto l’utente è stato verificato e viene reindirizzato alla pagina
index.php.

#### Encryption dei token

Dopo aver ottenuto i token si applica una cifratura grazie al cifrario simmetrico AES in modalità CBC (Cipher Block Chaining), attraverso l’uso di una chiave e di un _Initialization Vector_ e si memorizzano all’interno di un file di testo (_token.txt_), che viene ripulito ogni volta che si effettua una nuova login. La funzione che cifra è la denominata _encrypt_ della classe **PHP_AES_Cipher**:
    
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


### Script in  Java in locale

#### Decifratura dei token

Allo stesso modo, così come è implementata la [cifratura](Encryption dei token) viene introdotta la decifratura speculare: questo meccanismo - implementato nella classe _AESCrypto.java_ serve per trasmettere in modo sicuro i token dal sito al software, proteggendoli con la cifratura ed esponendoli al link https://progettosoasecurity.altervista.org/token.txt.

#### Fasi del funzionamento del software locale

1. La componente _WebReader.java_ recupera il testo al link di token.txt 
2. Controlla che i token siano stati scritti e li decifra
3. A questo punto è possibile fare delle chiamate a Twitter utilizzando la libreria [_twitter4j_](http://twitter4j.org/en/)

#### TwitterUtilities.java

TwitterUtilities.java contiene i metodi per l’esecuzione di azioni proposte all’utente, attraverso un menù all’interno della classe Main.java. Queste operazioni possono essere eseguite dal Consumer Server solo dopo aver ottenuto i token. La libreria Twitter4J ha permesso la costruzione di queste utilities. Le azioni implementate sono:
* Postare un Tweet
* Ottenere una timeline della home (tweet postati da altri utenti)
* Ottenere info dell'utente

In questa circostanza è bene spiegare che, inizialmente, l’intento era quello di implementare, in modo del tutto autonomo, del codice che potesse creare delle richieste POST e GET a Twitter al fine di realizzare le funzioni sopracitate.
Il motivo per cui non è stato possibile raggiungere l’obiettivo è la scarsa documentazione ufficiale sulla generazione della firma. I problemi
riscontrati sono discussi nella sezione riguardante il branch oauthGeneration.


## Contributors
* [Biagio Dipalma](https://www.linkedin.com/in/biagio-dipalma/) - Università degli Studi di Milano | La Statale. CDL Magistrale in Sicurezza Informatica.
* [Sara Longo](https://www.linkedin.com/in/sara-longo-2b2830187/) - Università degli Studi di Milano | La Statale. CDL Magistrale in Sicurezza Informatica.




