<?php

//CONTROLLARE SE IL CONSUMER TOKEN è IMPOSTATO E QUINDI PORTARE L'UTENTE A FARE UNA TOKEN REQUEST

//ERRORE SE CONSUMER_KEY O CONSUMER_SECRET NON SONO DEFINITE
require_once('config.php');
if (CONSUMER_KEY === '' || CONSUMER_SECRET === '') {
  echo 'You need a consumer key and secret to test the sample code. Get one from <a href="https://twitter.com/apps">https://twitter.com/apps</a>';
  exit;
}

//FILE DEI TOKEN VUOTO
file_put_contents("token.txt", '');

//CREARE UN'IMMAGINE LINK PER INIZIARE IL PROCESSO DI REDIRECT

$content = '<a href="./redirect.php"><img src="./images/lighter.png" alt="Sign in with Twitter"/></a>';
 
include('html.inc');
