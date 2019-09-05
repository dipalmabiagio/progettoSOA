<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<link href="css.css" type="text/css" rel="stylesheet">
  <head>
    <title>Twitter OAuth in PHP</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <style type="text/css">
      img {border-width: 20}
      * {font-family:'Lucida Grande', sans-serif;}
    </style>
  </head>
  <body>
    <div class="login">
      <h2>Welcome to a Twitter OAuth project</h2>
	<br><br><br>
      <?php if (isset($menu)) { ?>
        <?php echo $menu; ?>
      <?php } ?>
    </div>
	<div class="login">
	
    <?php if (isset($status_text)) { ?>
      <?php echo '<h3>'.$status_text.'</h3>'; ?>
    <?php } ?>
    <p>
      <pre>
        <?php 
		echo "NAME: ".$content->name."<br><br>";
echo "USERNAME: ".$content->screen_name."<br><br>";
echo "PHOTO : <img src='".$content->profile_image_url."'/><br><br><br>";
echo "NUMBER OF FOLLOWERS: ".$content->followers_count."<br><br>";
echo "NUMBER OF FRIENDS: ".$content->friends_count."<br><br>";
echo "CREATED AT: ".$content->created_at."<br><br><br><br><br><br>";
echo "TWEETS IN THE TIMELINE: "."<br>";
print_r($tweets);
	
echo "<br><br>";
echo "<a href='https://progettosoasecurity.altervista.org/connect.php'><button>LOGOUT</button></a>";
		?>
      </pre>
    </p>
	</div>
	
	<br><br><br><br><br><br><br><br>
</body>
<footer>
	<div class="link">
      <p>
        Links:
        <a href='https://github.com/dipalmabiagio/progettoSOA'>Source Code</a> &amp;
        <a href='http://github.com/abraham/twitteroauth'>Twitter PHP Library</a>
      </p>
	</div>
 </footer>
</html>