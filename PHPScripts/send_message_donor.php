<?php
$response = array();
$donorId=$_POST['donor'];
$id=$_POST['seeker'];

include('dbconfig.php');
include('fcm.php');


$fcm=new GCM();
$tokens=array();
$message="1 donor recieved";
$noti = array("message" => $message);

$result = @mysql_query("UPDATE Request set DonorId='$donorId' where SeekerId=$id");

$resNoti=@mysql_query("SELECT token FROM UserDetails where id='$id'");

if(mysql_num_rows($resNoti)>0 and $result){
	while ($row=@mysql_fetch_array($resNoti)) {
		$dev_id=$row['token'];
		array_push($tokens, $dev_id);
	}
	$fcm->send_notification($tokens,$noti);
	$response["id"] = $id;
	$response["success"] = 1;
	$response["message"] = "Notification Sent";
}else{
	$response["success"] = 0;
	$response["message"] = "Notification Not Sent";
}

echo json_encode($response);
?>					 