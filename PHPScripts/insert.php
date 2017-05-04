<?php

$name = $_POST['name'];
$phone =$_POST['phone'];
$gender =$_POST['gender'];
$city = $_POST['city'];
$password = $_POST['password'];
$date = $_POST['date'];
$time = $_POST['time'];
$token = $_POST['token'];

include("dbconfig.php");

$result = @mysql_query("INSERT INTO UserDetails VALUES(null,'$name','$phone','$gender','$city','$password','$date','$time','$token')");	
$response = array();
 if($result){
 	$last_id=@mysql_insert_id();
 	$response['UserId']=$last_id;
 	$response['success']=1;
 	$response['message']="Record Inserted Sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Insertion Failure";
 }
 
 echo json_encode($response);

?>