<?php

session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');
include("class_encryption.php");


//SE GLI ACCESS TOKEN NON SONO DISPONIBILI, FARE LA REDIRECT ALLA CONNECT PAGE
if (empty($_SESSION['access_token']) || empty($_SESSION['access_token']['oauth_token']) || empty($_SESSION['access_token']['oauth_token_secret'])) {
    header('Location: ./clearsessions.php');
}

//GET USER ACCESS TOKENS 
$access_token = $_SESSION['access_token'];

//CREAZIONE OGGETTO TWITTER OAUTH CON I TOKEN
$connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $access_token['oauth_token'], $access_token['oauth_token_secret']);


//ISTANZIO LA CLASSE PER LA CIFRATURA TOKEN
$key_enc=new PHP_AES_Cipher(); 
$secr_enc=new PHP_AES_Cipher(); 
$tok_enc=new PHP_AES_Cipher(); 
$tok_sec_enc=new PHP_AES_Cipher();

$chiave = "dipalmalongoprog";
$iv = "progettosoasecur";


//MEMORIZZAZIONE DEI TOKEN IN UN FILE
$file =fopen("token.txt", "w+");
fwrite($file, $key_enc->encrypt($chiave, $iv, CONSUMER_KEY).",");
fwrite($file, $secr_enc->encrypt($chiave, $iv, CONSUMER_SECRET).",");
fwrite($file, $tok_enc->encrypt($chiave, $iv, $access_token['oauth_token']).",");
fwrite($file, $tok_sec_enc->encrypt($chiave, $iv, $access_token['oauth_token_secret']));
fclose($file);

//credenziali
$content = $connection->get('account/verify_credentials');
//tweets timeline
$tweets = $connection->get("statuses/home_timeline", ["count" => 1, "eclude_replies" => true]);



//creazione di un tweet "hello world"
//$status = $connection->post("statuses/update", ["status" => "Ciao!"]);


include('html2.inc');

