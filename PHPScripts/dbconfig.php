<?php
$dbhost = 'localhost';
$dbuser = 'u567247881_stud';
$dbpass = '123456';
$db = 'u567247881_stud';

@mysql_connect($dbhost, $dbuser, $dbpass);
@mysql_select_db($db);
?>