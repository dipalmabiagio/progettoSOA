<?php

session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');


//CREAZIONE OGGETTO OAUTH CON CREDENZIALI CLIENT
$connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET);
 
//CREDENZIALI TEMPORANEE
//getRequestToken è in twitteroauth.php
$request_token = $connection->getRequestToken(OAUTH_CALLBACK);


//SALVATAGGIO DELLE CREDENZIALI
$_SESSION['oauth_token'] = $token = $request_token['oauth_token'];
$_SESSION['oauth_token_secret'] = $request_token['oauth_token_secret'];
 
//SE L'ULTIMA CONNESSIONE è FALLITA NON MOSTRARE L'AUTHORIZATION LINK

switch ($connection->http_code) {
  case 200:
    //URL AUTORIZZATA
	//getAuthorizeURL è in twitteroauth.php
    $url = $connection->getAuthorizeURL($token);
    header('Location: ' . $url); 
    break;
  default:
    //NOTIFICA SE QUALCOSA VA STORTO
    echo 'Could not connect to Twitter. Refresh the page or try again later.';
}
