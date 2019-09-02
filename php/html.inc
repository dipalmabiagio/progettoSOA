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
      <h2>Welcome to a Twitter OAuth PHP example</h2>
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
		print_r($content);
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
        <a href='https://developer.twitter.com/'>Documentation</a> &amp;
        <a href='http://twitter4j.org/en/'>Library</a>
      </p>
	</div>
 </footer>
</html>
