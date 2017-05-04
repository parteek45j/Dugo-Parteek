<?php

$id = $_POST['id'];

include("dbconfig.php");

$result = @mysql_query("INSERT INTO Request values(null, '$id',null,null)");	
$response = array();
 if($result){
 	$response['success']=1;
 	$response['message']="Record Inserted Sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Insertion Failure";
 }
 
 echo json_encode($response);

?>