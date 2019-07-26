<?php
//GET ACCESS TOKEN
//VERIFICA DELLE CREDENZIALI E REDIRECT

//START E LIBRERIA
session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');


//SE IL TOKEN è VECCHIO, FARE IL REDIRECT ALLA PAGINA
if (isset($_REQUEST['oauth_token']) && $_SESSION['oauth_token'] !== $_REQUEST['oauth_token']) {
  $_SESSION['oauth_status'] = 'oldtoken';
  header('Location: ./clearsessions.php');
}
//CREAZIONE OGGETTO OAUTH CON APP KEY/SECRET E TOKEN KEY/SECRET

$connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $_SESSION['oauth_token'], $_SESSION['oauth_token_secret']);


//RICHIESTA DI ACCESS TOKEN
$access_token = $connection->getAccessToken($_REQUEST['oauth_verifier']);

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
