<?php

include("dbconfig.php");

$result = @mysql_query("select * from UserDetails order by id DESC");	

$response = array();

 if(@mysql_num_rows($result)>0){

	$response['students'] = array();

	while($row=@mysql_fetch_array($result)){
		array_push($response['students'], $row);
	}
}

 if($result){
 	$response['success']=1;
 	$response['message']="Records Retrieved sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Retrieval Failure";
 }

 echo json_encode($response);

?>