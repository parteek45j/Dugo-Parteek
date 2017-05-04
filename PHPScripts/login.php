<?php

$phone=$_POST['phone'];
$password=$_POST['password'];

include("dbconfig.php");

$result = @mysql_query("select * from UserDetails where phone='$phone' and password='$password'");	
$response =array();

 if(@mysql_num_rows($result)>0){
 	$response['message']="Login Sucessful";
 }else{
 	
 	$response['message']="Login Failure";
 }
 echo json_encode($response);

?>