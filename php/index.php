<?php

session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');


//SE GLI ACCESS TOKEN NON SONO DISPONIBILI, FARE LA REDIRECT ALLA CONNECT PAGE
if (empty($_SESSION['access_token']) || empty($_SESSION['access_token']['oauth_token']) || empty($_SESSION['access_token']['oauth_token_secret'])) {
    header('Location: ./clearsessions.php');
}

//GET USER ACCESS TOKENS 
$access_token = $_SESSION['access_token'];

//CREAZIONE OGGETTO TWITTER OAUTH CON I TOKEN
$connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $access_token['oauth_token'], $access_token['oauth_token_secret']);

//FUNZIONE PER CRIPTARE E DECRIPTARE I TOKEN
function my_simple_crypt( $string, $action = 'e' ) {
    
    $secret_key = 'SOA_Security_project';
    $secret_iv = '933528-938473';
 
    $output = false;
    $encrypt_method = "AES-256-CBC";
    $key = hash( 'sha256', $secret_key );
    $iv = substr( hash( 'sha256', $secret_iv ), 0, 16 );
 
    if( $action == 'e' ) {
        $output = base64_encode( openssl_encrypt( $string, $encrypt_method, $key, 0, $iv ) );
    }
    else if( $action == 'd' ){
        $output = openssl_decrypt( base64_decode( $string ), $encrypt_method, $key, 0, $iv );
    }
 
    return $output;
}


//ENCRYPTION DEI TOKEN
$key_enc = my_simple_crypt( CONSUMER_KEY, 'e' );
$secr_enc = my_simple_crypt( CONSUMER_SECRET, 'e' );
$tok_enc = my_simple_crypt( $access_token['oauth_token'], 'e' );
$tok_secr_enc = my_simple_crypt( $access_token['oauth_token_secret'], 'e' );

echo CONSUMER_KEY."<br>";
echo $key_enc;
//esempio decriptare
//$decrypted = my_simple_crypt( 'dHNZM28xMWVSWlBhM3d5bmlta25oRTl3aVc2a3RCZjl2U0VqMGV1U0NnST0=', 'd' );


//MEMORIZZAZIONE DEI TOKEN IN UN FILE
$file =fopen("token.txt", "w+");
fwrite($file, "CONSUMER KEY: ".$key_enc.", ");
fwrite($file, "CONSUMER SECRET: ".$secr_enc.", ");
fwrite($file, "ACCESS TOKEN: ".$tok_enc.", ");
fwrite($file, "ACCESS TOKEN SECRET: ".$tok_secr_enc);
fclose($file);

//credenziali
$content = $connection->get('account/verify_credentials');
//tweets timeline
$tweets = $connection->get("statuses/home_timeline", ["count" => 1, "eclude_replies" => true]);



//creazione di un tweet "hello world"
//$status = $connection->post("statuses/update", ["status" => "Ciao!"]);


include('html2.inc');

