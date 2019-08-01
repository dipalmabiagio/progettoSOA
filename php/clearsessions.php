<?php
//CARICA E PULISCI LA SESSIONE
session_start();
session_destroy();
//REDIRECT ALLA PAGINA CON LA CONNESSIONE A TWITTER

header('Location: ./connect.php');
?>
