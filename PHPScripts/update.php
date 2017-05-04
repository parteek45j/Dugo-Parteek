<?php
session_start();
$donorId='6';//$_POST['donorid'];
//$email=$_POST['email'];
$id=$_SESSION['id'];
include("dbconfig.php");

$result = @mysql_query("update Request set DonorId='$donorId' where SeekerId=$id");	
$response =array();
 if($result){
 	$response['success']=1;
 	$response['message']="Record Updated Sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Updation Failure";
 }
 echo json_encode($response);	
 echo $_SESSION['id'];

?>